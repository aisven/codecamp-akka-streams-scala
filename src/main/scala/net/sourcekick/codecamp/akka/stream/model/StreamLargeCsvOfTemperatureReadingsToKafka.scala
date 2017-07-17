/**
  * Copyright (C) Recogizer Group GmbH - All Rights Reserved
  * Unauthorized copying of this file, via any medium is strictly prohibited
  * Proprietary and confidential
  * Created on 17.07.17.
  */
package net.sourcekick.codecamp.akka.stream.model

import java.nio.file.Paths
import java.time.Instant

import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.{FileIO, Flow, Framing, Keep, RunnableGraph, Sink, Source}
import akka.stream.{ActorAttributes, ActorMaterializer, IOResult, Supervision}
import akka.util.ByteString
import akka.{Done, NotUsed}
import net.sourcekick.codecamp.akka.stream.constants.Constants
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Random

object StreamLargeCsvOfTemperatureReadingsToKafka {

  private val log: Logger = LoggerFactory.getLogger(GenerateCsvOfTemperatureReadings.getClass.getName)

  def main(args: Array[String]) {

    implicit val system = ActorSystem("TimeseriesCsvGenerator")
    implicit val materializer = ActorMaterializer()

    val start1 = Instant.now()

    stream("/p/codecamp-akka-streams-scala/ordered.csv")

    val stop1 = Instant.now()
    val duration1 = java.time.Duration.between(start1, stop1).toMillis
    log.info("Took {} ms.", duration1)

    val start2 = Instant.now()

    streamWithSorting("/p/codecamp-akka-streams-scala/shuffled.csv")

    val stop2 = Instant.now()
    val duration2 = java.time.Duration.between(start2, stop2).toMillis
    log.info("Took {} ms.", duration2)

    val terminationFuture = system.terminate()
    Await.result(terminationFuture, 100.seconds)
  }

  private val random = Random

  def stream(path: String)(implicit system: ActorSystem, materializer: ActorMaterializer): Unit = {
    // source
    val source: Source[String, Future[IOResult]] = FileIO
      .fromPath(Paths.get(path))
      .via(Framing.delimiter(ByteString(";"), maximumFrameLength = 2048, allowTruncation = true))
      .map(bs => bs.utf8String)

    // flow
    val flow: Flow[String, TemperatureReading, NotUsed] = createRobustFlowOfStringToTemperatureReading().log("reading")

    // flow 2
    val flow2: Flow[TemperatureReading, ProducerRecord[String, String], NotUsed] =
      Flow.fromFunction(t => new ProducerRecord("stream-topic", 0, t.instant.toEpochMilli, t.uuid, t.toCsv))

    // sink
    val sink: Sink[ProducerRecord[String, String], Future[Done]] = createKafkaSink()

    // runnable graph
    val rg: RunnableGraph[(Future[IOResult], Future[Done])] =
      source.viaMat(flow)(Keep.left).viaMat(flow2)(Keep.left).async.toMat(sink)(Keep.both)

    // run
    val result: (Future[IOResult], Future[Done]) = rg.run()
    val ioResult = Await.result(result._1, 20000.seconds)
    if (!ioResult.wasSuccessful) {
      log.info("Exception during Source IO.", ioResult.getError)
    } else {
      log.info("Source IO finished successful.")
    }

    Await.ready(result._2, 20000.seconds)
  }

  def streamWithSorting(path: String)(implicit system: ActorSystem, materializer: ActorMaterializer): Unit = {
    // source
    val source: Source[String, Future[IOResult]] = FileIO
      .fromPath(Paths.get(path))
      .via(Framing.delimiter(ByteString(";"), maximumFrameLength = 2048, allowTruncation = true))
      .map(bs => bs.utf8String)

    // flow
    val flow: Flow[String, TemperatureReading, NotUsed] = createRobustFlowOfStringToTemperatureReading()

    // flow 2
    val flow2: Flow[TemperatureReading, ProducerRecord[String, String], NotUsed] =
      Flow.fromFunction(t => new ProducerRecord(t.uuid, t.toCsv))

    // sink
    val sink: Sink[ProducerRecord[String, String], Future[Done]] = createKafkaSink()

    // runnable graph
    val rg: RunnableGraph[(Future[IOResult], Future[Done])] =
      source.viaMat(flow)(Keep.left).viaMat(flow2)(Keep.left).async.toMat(sink)(Keep.both)

    // run
    val result: (Future[IOResult], Future[Done]) = rg.run()
    val ioResult = Await.result(result._1, 120.seconds)
    if (!ioResult.wasSuccessful) {
      log.info("Exception during Source IO.", ioResult.getError)
    } else {
      log.info("Source IO finished successful.")
    }

    Await.ready(result._2, 120.seconds)
  }

  private def createRobustFlowOfStringToTemperatureReading(): Flow[String, TemperatureReading, NotUsed] = {

    val decider: Supervision.Decider = {
      case _: TemperatureReadingParseException => Supervision.Resume
      case _                                   => Supervision.Stop
    }

    Flow.fromFunction(toTemperatureReading).withAttributes(ActorAttributes.supervisionStrategy(decider))
  }

  private def toTemperatureReading(s: String): TemperatureReading = {
    try {

      val a: Array[String] = s.split(",")
      TemperatureReading(a(0), Instant.parse(a(1)), a(2).toFloat, TemperatureUnit.fromString(a(3)))

    } catch {
      case t: Exception => throw TemperatureReadingParseException(cause = t)
    }
  }

  case class TemperatureReadingParseException(message: String = "", cause: Throwable = None.orNull)
      extends RuntimeException(message, cause)

  private def createKafkaSink()(implicit system: ActorSystem): Sink[ProducerRecord[String, String], Future[Done]] = {
    Producer.plainSink[String, String](createProducerSettings())
  }

  private def createProducerSettings()(implicit system: ActorSystem) = {
    ProducerSettings
      .create(system, new StringSerializer, new StringSerializer)
      .withBootstrapServers(Constants.KAFKA_IP_OR_HOST + ":" + Constants.KAFKA_CLIENT_PORT)
      .withParallelism(1)
      .withProperty("max.in.flight.requests.per.connection", "1")
  }

}
