package net.sourcekick.codecamp.akka.stream.kata07

import akka.actor.ActorSystem
import akka.kafka.ConsumerMessage.CommittableMessage
import akka.kafka.scaladsl.Consumer.Control
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import net.sourcekick.codecamp.akka.stream.model.FunResult

object Kata07KafkaSource {

  implicit val system = ActorSystem("Kata07")
  implicit val materializer = ActorMaterializer()

  /**
    * Task: Create a akka.stream.scaladsl.Source that
    * reads each record from the Kafka topic named kata07-topic,
    * transforms each record value into a FunResult by passing each record value to the given function fun,
    * emits a pair of [ConsumerMessage.CommittableMessage[String, String], FunResult]
    * and supports explicitly committing the offset of a consumed element to Kafka.
    * Decouple from the call to the given function by defining an asynchronous boundary.
    * Pass the ActorSystem [Kata07KafkaSource#system] to the consumer settings.
    * Use the string kata07-consumergroup as the groupId in the consumer settings.
    * Make sure that the applied consumer settings also contain the following settings:
    * <ul>
    * <li>ConsumerConfig.AUTO_OFFSET_RESET_CONFIG "earliest"</li>
    * <li>ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG "false"</li>
    * <li>ConsumerConfig.RECEIVE_BUFFER_CONFIG "8"</li>
    * <li>ConsumerConfig.FETCH_MIN_BYTES_CONFIG "1"</li>
    * <li>ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG "100"</li>
    * <li>ConsumerConfig.RETRY_BACKOFF_MS_CONFIG "50"</li>
    * </ul>
    * <p/>
    * Tip: FunResult can be imported from the package called model contained in this project.
    * <p/>
    * Tip: Just create the source, i.e. do not run it e.g. with a sink.
    * <p/>
    * Tip: See kata06 for instructions regarding Kafka.
    * <p/>
    * Tip: To provide consumer settings see: http://doc.akka.io/docs/akka-stream-kafka/0.13/consumer.html#settings
    * <p/>
    * Reference: http://doc.akka.io/docs/akka-stream-kafka/0.13/consumer.html#offset-storage-in-kafka
    * <p/>
    * Check: The kata is solved when the corresponding unit test is green.
    *
    * @return The source.
    */
  private[kata07] def createKafkaSource(
      fun: Function[String, FunResult]): Source[(CommittableMessage[String, String], FunResult), Control] = {
    ???
  }

}
