package net.sourcekick.codecamp.akka.stream.kata03

import akka.NotUsed
import akka.stream.IOResult
import akka.stream.scaladsl.{Flow, Source}
import net.sourcekick.codecamp.akka.stream.model.TemperatureReading

import scala.concurrent.Future

object Kata03CsvSource {

  /**
    * Task: Create a akka.stream.scaladsl.Source that emits line by line of kata03.csv file.
    * <p/>
    * Reference: See kata02.
    *
    * @return The source.
    */
  def createSourceOfLinesInFileKata03Csv(): Source[String, Future[IOResult]] = {
    ???
  }

  /**
    * Task: Create a akka.stream.scaladsl.Flow that accepts a string
    * and transforms (i.e. parses) it into a [TemperatureReading].
    * In the unit test the inlet of this flow will be connected to the outlet of the source
    * created by [Kata03CsvSource#createSourceOfLinesInFileKata03Csv()]
    * Thus the flow needs to expect and treat the string to be in the format of a line in the given CSV file.
    * <p/>
    * Reference: http://doc.akka.io/docs/akka/current/scala/stream/stream-flows-and-basics.html#Defining_sources__sinks_and_flows
    * <p/>
    * Check: The kata is solved when the corresponding unit test is green.
    *
    * @return The source.
    */
  def createFlowOfStringToTemperatureReading(): Flow[String, TemperatureReading, NotUsed] = {
    ???
  }

}
