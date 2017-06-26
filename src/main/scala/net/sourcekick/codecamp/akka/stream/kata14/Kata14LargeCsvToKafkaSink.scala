package net.sourcekick.codecamp.akka.stream.kata14

import java.nio.file.Paths
import java.time.Instant

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.{ActorMaterializer, IOResult}
import akka.stream.scaladsl.{FileIO, Flow, Framing, Sink, Source}
import akka.util.ByteString
import net.sourcekick.codecamp.akka.stream.constants.Constants
import net.sourcekick.codecamp.akka.stream.model.{TemperatureReading, TemperatureUnit}
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer

import scala.concurrent.Future

class Kata14LargeCsvToKafkaSink {

  implicit val system = ActorSystem("Kata14")
  implicit val materializer = ActorMaterializer()

  /**
    * Task: Create a akka.stream.scaladsl.Source that emits line by line of kata14.csv file.
    * <p/>
    * Reference: See kata03.
    *
    * @return The source.
    */
  private[kata03] def createSourceOfLinesInFileKata14Csv(): Source[String, Future[IOResult]] = {

    val url = Thread.currentThread().getContextClassLoader.getResource("kata14.csv")

    val path = Paths.get(url.getPath)

    FileIO.fromPath(path).via(Framing.delimiter(ByteString("\n"), 4096, allowTruncation = true)).map(bs => bs.utf8String)
  }

  /**
    * Task: Create a akka.stream.scaladsl.Flow that accepts a string
    * and transforms (i.e. parses) it into a [TemperatureReading].
    * In the unit test the inlet of this flow will be connected to the outlet of the source
    * created by [Kata14CsvSource#createSourceOfLinesInFileKata14Csv()]
    * Thus the flow needs to expect and treat the string to be in the format of a line in the given CSV file.
    * <p/>
    * Reference: See kata03.
    * <p/>
    * Check: The kata is solved when the corresponding unit test is green.
    *
    * @return The source.
    */
  private[kata03] def createFlowOfStringToTemperatureReading(): Flow[String, TemperatureReading, NotUsed] = {
    Flow.fromFunction(toTemperatureReading)
  }

  private def toTemperatureReading(s: String): TemperatureReading = {
    val a: Array[String] = s.split(",")

    TemperatureReading(a(0), Instant.parse(a(1)), a(2).toFloat, TemperatureUnit.fromString(a(3)))
  }

  /**
    * Task: Create a akka.stream.scaladsl.Sink that writes each element
    * to the Kafka topic named kata14-topic to the partition 0.
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
    Producer.plainSink[String, String](createProducerSettings(system))
  }


  private def createProducerSettings(system: ActorSystem) = {
    ProducerSettings
      .create(system, new StringSerializer, new StringSerializer)
      .withBootstrapServers(Constants.KAFKA_IP_OR_HOST + ":" + Constants.KAFKA_CLIENT_PORT)
      .withParallelism(1)
      .withProperty("max.in.flight.requests.per.connection", "1")
  }

}
