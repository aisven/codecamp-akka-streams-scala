package net.sourcekick.codecamp.akka.stream.kata06

import akka.Done
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import org.apache.kafka.clients.producer.ProducerRecord

import scala.concurrent.Future

private[kata06] object Kata06KafkaSink {

  implicit val system = ActorSystem("Kata06")
  implicit val materializer = ActorMaterializer()

  /**
    * Task: Create a akka.stream.scaladsl.Sink that writes each element
    * to the Kafka topic named kata06-topic to the partition 0.
    * <p/>
    * Tip: See the README.md for additional instructions regarding Kafka.
    * <p/>
    * Tip: To start Kafka execute in the project root directory: docker-compose up -d
    * <p/>
    * Tip: For the Kafka client port see in the project root directory: docker-compose.yml
    * <p/>
    * Tip: Use the library Akka Streams Kafka. The dependency is already present in this project.
    * <p/>
    * Tip: Use [net.codecamp.akka.streams.scala.constants.Constants#KAFKA_IP_OR_HOST]
    * and [net.codecamp.akka.streams.scala.constants.Constants#KAFKA_CLIENT_PORT]
    * where KAFKA_IP_OR_HOST must be the current IP of your machine if you use Docker for Mac
    * and localhost if you use Linux or Windows.
    * <p/>
    * Tip: To provide producer settings see: http://doc.akka.io/docs/akka-stream-kafka/0.13/producer.html#settings
    * <p/>
    * Reference: http://doc.akka.io/docs/akka-stream-kafka/0.13/producer.html#producer-as-a-sink
    * <p/>
    * Check: The kata is solved when the corresponding unit test is green.
    *
    * @return The sink.
    */
  private[kata06] def createKafkaSink(): Sink[ProducerRecord[String, String], Future[Done]] = {
    ???
  }
}
