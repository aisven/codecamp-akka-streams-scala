/**
  * Copyright (C) Recogizer Group GmbH - All Rights Reserved
  * Unauthorized copying of this file, via any medium is strictly prohibited
  * Proprietary and confidential
  * Created on 17.07.17.
  */
package net.sourcekick.codecamp.akka.stream.model

import java.nio.file.Paths
import java.util.UUID

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{FileIO, Flow, Framing, Keep, RunnableGraph, Sink, Source}
import akka.stream.{ActorMaterializer, IOResult}
import akka.util.ByteString
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.immutable
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Random

object StreamLargeCsvOfTemperatureReadingsToKafka {

  private val log: Logger = LoggerFactory.getLogger(GenerateCsvOfTemperatureReadings.getClass.getName)

  def main(args: Array[String]) {

    implicit val system = ActorSystem("TimeseriesCsvGenerator")
    implicit val materializer = ActorMaterializer()

    stream("ordered.csv")

    streamWithSorting("shuffled.csv")

    val terminationFuture = system.terminate()
    Await.result(terminationFuture, 100.seconds)
  }

  private val random = Random

  def stream(path: String)(implicit materializer: ActorMaterializer): Unit = {
    // source
    val source: Source[ByteString, Future[IOResult]] = FileIO
      .fromPath(Paths.get(path))
      .via(Framing.delimiter(ByteString(";"), maximumFrameLength = 2048, allowTruncation = false))

    // flow
    val flow: Flow[TemperatureReading, ByteString, NotUsed] =
      Flow.apply.mapAsync(1)(t =>
        Future {
          ByteString(t.toString.replace("TemperatureReading(", "").replace(")", "") + "\n")
      })

    // sink
    val url = Thread.currentThread().getContextClassLoader.getResource("kata02.txt")
    val targetPath = Paths.get(path)
    log.info("Going to stream generated time series into file " + targetPath)
    val sink: Sink[ByteString, Future[IOResult]] = FileIO.toPath(targetPath)

    // runnable graph
    val rg: RunnableGraph[Future[IOResult]] =
      source.take(numberOfTemperatureReadings).via(flow).async.toMat(sink)(Keep.right)

    // run
    val ioResult = rg.run()
    val result = Await.result(ioResult, 20000.seconds)
    if (!result.wasSuccessful) {
      log.info("Exception.", result.getError)
    } else {
      log.info("Finished successful.")
    }
  }

  def streamWithSorting(path: String)(implicit materializer: ActorMaterializer): Unit = {

    // source
    val source: Source[TemperatureReading, NotUsed] = Source.unfold(first)(previous => {
      val t = generateTemperatureReading(previous)
      Option((t, t))
    })

    // shuffle flow
    val shuffleFlow: Flow[TemperatureReading, TemperatureReading, NotUsed] =
      Flow.apply
        .sliding(100, 100)
        .map((s: immutable.Seq[TemperatureReading]) => Random.shuffle(s))
        .flatMapConcat(s => Source.fromIterator(() => s.iterator))

    // flow
    val flow: Flow[TemperatureReading, ByteString, NotUsed] =
      Flow.apply.mapAsync(1)(t =>
        Future {
          ByteString(t.toString.replace("TemperatureReading(", "").replace(")", "") + "\n")
      })

    // sink
    val url = Thread.currentThread().getContextClassLoader.getResource("kata02.txt")
    val targetPath = Paths.get(path)
    log.info("Going to stream generated time series into file " + targetPath)
    val sink: Sink[ByteString, Future[IOResult]] = FileIO.toPath(targetPath)

    // runnable graph
    val rg: RunnableGraph[Future[IOResult]] =
      source.take(numberOfTemperatureReadings).via(shuffleFlow).via(flow).async.toMat(sink)(Keep.right)

    // run
    val ioResult = rg.run()
    val result = Await.result(ioResult, 20000.seconds)
    if (!result.wasSuccessful) {
      log.info("Exception.", result.getError)
    } else {
      log.info("Finished successful.")
    }
  }

}
