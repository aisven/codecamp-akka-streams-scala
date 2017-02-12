package net.sourcekick.codecamp.akka.stream.kata06

import java.time.Instant
import java.util.concurrent.{ExecutionException, TimeoutException}

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, Sink, Source}
import net.sourcekick.codecamp.akka.stream.constants.Constants
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.scalatest.{AsyncWordSpec, Matchers, ParallelTestExecution}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.immutable.Seq
import scala.concurrent.Await
import scala.concurrent.duration._

class Kata06KafkaSinkSpec extends AsyncWordSpec with Matchers with ParallelTestExecution {

  private val log: Logger = LoggerFactory.getLogger("Kata06KafkaSinkSpec")

  private val KATA06_RECORD = "kata06-record"
  private val KATA06_TOPIC = "kata06-topic"
  private val KATA06_PARTITION = 0

  private[kata06] def createTopicPartitionKata06 = new TopicPartition(KATA06_TOPIC, KATA06_PARTITION)

  "Kata06KafkaSink" must {

    "consume correctly" in {
      import Kata06KafkaSink.system
      import Kata06KafkaSink.materializer

      // obtain sink under test
      val producerSink = Kata06KafkaSink.createKafkaSink()

      // determine current and next offset by looking into what is currently in the topic
      val currentOffset = determineLatestOffsetByReadingAllRecords(system)
      val nextOffset = currentOffset + 1
      // obtain sink under test
      //val producerSink = Kata06KafkaSink.createKafkaSink()

      // recipe for producing records
      val producerRecordSource = Source(nextOffset to nextOffset + 9)
        .map(i => KATA06_RECORD + "-" + i)
        .map(v => new ProducerRecord(KATA06_TOPIC, KATA06_PARTITION, Instant.now().toEpochMilli(), v, v))
        .log("producerRecordSource")

      // start producing
      val firstProducerFuture = producerRecordSource.runWith(producerSink) // Future[Done]

      // define the consumer for checking what has been produced
      val consumerSource = Consumer.plainSource(
        createConsumerSettings(system, "kata06-assert-consumergroup", "none", autoCommit = true),
        Subscriptions.assignmentWithOffset(createTopicPartitionKata06, nextOffset)
      )
      val firstConsumerRunnableGraph = consumerSource.log("firstConsumerSource").toMat(Sink.seq)(Keep.both)

      // start consuming
      val pair = firstConsumerRunnableGraph.run()
      val firstConsumerControl = pair._1
      val firstConsumerFuture = pair._2

      // wait for production to complete
      val firstProducerDone = Await.result(firstProducerFuture, 5.seconds)

      // wait for consumption to succeed
      Thread.sleep(1500)

      // shutdown the consumer
      val firstConsumerShutdownFuture = firstConsumerControl.shutdown
      val firstConsumerShutdownDone = Await.result(firstConsumerShutdownFuture, 7.seconds)

      // assert consumed result
      firstConsumerFuture.map(consumerRecords => {
        createExpectedList(nextOffset, nextOffset + 9) shouldBe consumerRecords.map(_.value())
      })
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

  private def createExpectedList(from: Int, to: Int): Seq[String] = {
    (from to to).toVector.map(i => KATA06_RECORD + "-" + i)
  }

  @throws[InterruptedException]
  @throws[ExecutionException]
  @throws[TimeoutException]
  private def determineLatestOffsetByReadingAllRecords(system: ActorSystem)(implicit materializer: ActorMaterializer) = {
    // define consumer
    val consumerSource = Consumer.plainSource(
      createConsumerSettings(system, "kata06-getoffset-consumergroup", "earliest", autoCommit = false),
      Subscriptions.assignment(createTopicPartitionKata06))
    val g = consumerSource.toMat(Sink.seq)(Keep.both) // RunnableGraph[(Control, Future[Seq[ConsumerRecord[String, String]]])]
    // start consumer
    val pair = g.run()
    // wait some time and shutdown the consumer
    Thread.sleep(2000)
    // shutdown the consumer
    val control = pair._1
    val shutdownFuture = control.shutdown
    val shutdownDone = Await.result(shutdownFuture, 7.seconds)
    // obtain result
    val seqFuture = pair._2
    val seqResult: Seq[ConsumerRecord[String, String]] = Await.result(seqFuture, 7.seconds)
    val latestOffset = seqResult.size - 1
    log.debug(s"latest offset determined by reading all records is $latestOffset")
    latestOffset
  }

}
