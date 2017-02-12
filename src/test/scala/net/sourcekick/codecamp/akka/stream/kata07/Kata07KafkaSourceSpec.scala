/**
  * Copyright 2017 by Sven Ludwig. All rights reserved.
  */
package net.sourcekick.codecamp.akka.stream.kata07

import java.time.Instant
import java.util.concurrent.{ExecutionException, TimeoutException}

import akka.actor.ActorSystem
import akka.kafka.scaladsl.{Consumer, Producer}
import akka.kafka.{ConsumerMessage, ConsumerSettings, ProducerSettings}
import akka.stream.Materializer
import akka.stream.scaladsl.{Flow, Keep, RunnableGraph, Sink, Source}
import net.sourcekick.codecamp.akka.stream.constants.Constants
import net.sourcekick.codecamp.akka.stream.model.FunResult
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import org.scalatest.{AsyncWordSpec, Matchers, ParallelTestExecution}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class Kata07KafkaSourceSpec extends AsyncWordSpec with Matchers with ParallelTestExecution {

  /*
   * Please do not read this test code while solving any of the katas! Spoiler warning!
   *
   * Just run it with right-click on the test class name above.
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   *
   */

  import Kata07KafkaSource.{materializer, system}

  private val KATA07_RECORD: String = "kata07-record"
  private val KATA07_TOPIC: String = "kata07-topic"
  private val KATA07_PARTITION: Int = 0

  "Kata07KafkaSource" must {

    "emit the correct elements" in {
      // obtain source
      val source = Kata07KafkaSource.createKafkaSource(s => FunResult(s + "_fun"))

      // produce 10 records
      produce(10, "producer1")

      // consume all records currently in the topic
      {
        val flow1 =
          Flow.fromFunction[Tuple2[ConsumerMessage.CommittableMessage[String, String], FunResult], String](p => {
            p._1.committableOffset.commitScaladsl()
            p._2.result
          })
        val g1 = source.viaMat(flow1)(Keep.left).toMat(Sink.seq)(Keep.both)
        consume(g1, 0, Integer.MAX_VALUE, doAssert = false)
      }

      // produce 10 new records
      produce(10, "producer2")

      // recipe for consumption
      val flow =
        Flow.fromFunction[Tuple2[ConsumerMessage.CommittableMessage[String, String], FunResult], String](p => {
          p._1.committableOffset.commitScaladsl()
          p._2.result
        })

      // consume the first 3 records of the 10 that have just been produced and committing the consumer offset
      {
        val rg = source.viaMat(flow.take(3))(Keep.left).toMat(Sink.seq)(Keep.both)
        consume(rg, 0, 2, doAssert = true)
      }

      // consume the other records of the 10 that have just been produced and committing the consumer offset
      {
        val rg = source.viaMat(flow)(Keep.left).toMat(Sink.seq)(Keep.both)
        consume(rg, 3, 9, doAssert = true)
      }

      // end waiting some more for asynchronous things to complete
      Future {
        Thread.sleep(20)
      }.map(u => {
        Thread.sleep(2000)
        // 0 is 0 is 0 is 0
        0 shouldBe 0
      })
    }

  }

  @throws[InterruptedException]
  @throws[ExecutionException]
  @throws[TimeoutException]
  private def produce(toProduce: Int, loggerName: String)(implicit materializer: Materializer) {
    // recipe for records to produce
    val producerRecordSource = Source(0 until toProduce)
      .map(i => KATA07_RECORD + "-" + i)
      .map(v => new ProducerRecord(KATA07_TOPIC, KATA07_PARTITION, Instant.now().toEpochMilli(), v, v))

    // sink
    val producerSink = Producer.plainSink(createProducerSettings(system))

    // start producing
    val producerFuture = producerRecordSource.log(loggerName).runWith(producerSink)

    // wait for production to complete
    val producerDone = Await.result(producerFuture, 7.seconds)
    Thread.sleep(2000)
  }

  private def createProducerSettings(system: ActorSystem) =
    ProducerSettings
      .create(system, new StringSerializer, new StringSerializer)
      .withBootstrapServers(Constants.KAFKA_IP_OR_HOST + ":" + Constants.KAFKA_CLIENT_PORT)
      .withParallelism(1)
      .withProperty("max.in.flight.requests.per.connection", "1")
      .withProperty("batch.size", "0")

  @throws[InterruptedException]
  @throws[TimeoutException]
  @throws[ExecutionException]
  private def consume(g: RunnableGraph[Pair[Consumer.Control, Future[Seq[String]]]],
                      fromIndex: Int,
                      toIndex: Int,
                      doAssert: Boolean)(implicit materializer: Materializer) {
    // preconditions
    if (toIndex < fromIndex) throw new IllegalStateException("toIndex must be greater than fromIndex")

    // start consumer
    val pair = g.run()

    // wait some time for consumption to succeed
    Thread.sleep(1000)

    val consumerControl = pair._1
    val consumerFuture = pair._2

    // assume that consumption succeeded completely and shutdown the consumer
    val consumerShutdownFuture = consumerControl.shutdown
    val consumerShutdownDone = Await.result(consumerShutdownFuture, 7.seconds)

    // obtain the consumed result
    val resultStrings = Await.result(consumerFuture, 7.seconds)

    // assertions on consumed result
    if (doAssert) {
      (1 + toIndex - fromIndex) shouldBe resultStrings.size
      var index = fromIndex
      for (resultString <- resultStrings) {
        resultString.startsWith(KATA07_RECORD) shouldBe true
        resultString.endsWith("-" + index + "_fun") shouldBe true
        index += 1
      }
    }
  }

  private def createConsumerSettings(system: ActorSystem,
                                     consumerGroupId: String,
                                     autoOffsetResetConfig: String,
                                     autoCommit: Boolean) = {
    val settings = ConsumerSettings
      .create(system, new StringDeserializer, new StringDeserializer)
      .withBootstrapServers(Constants.KAFKA_IP_OR_HOST + ":" + Constants.KAFKA_CLIENT_PORT)
      .withGroupId(consumerGroupId)
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetResetConfig)
      .withProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, String.valueOf(autoCommit))

    if (autoCommit) settings.withProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "5")

    settings
  }

}
