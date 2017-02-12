/**
  * Copyright 2017 by Sven Ludwig. All rights reserved.
  */
package net.sourcekick.codecamp.akka.stream.kata04

import akka.NotUsed
import akka.stream.IOResult
import akka.stream.scaladsl.{Flow, Source}
import net.sourcekick.codecamp.akka.stream.model.TemperatureReading

import scala.concurrent.Future

private[kata04] object Kata04DirtyCsvSource {

  /**
    * Task: Create a akka.stream.scaladsl.Source that emits line by line of kata04.csv file.
    * <p/>
    * Reference: See kata03.
    *
    * @return The source.
    */
  private[kata04] def createSourceOfLinesInFileKata04Csv(): Source[String, Future[IOResult]] = {
    ???
  }

  /**
    * Task: This is just like kata 03, but this time the CSV contains lines with data problems.
    * The flow must not stop the stream with a failure when a line with a data problem is encountered.
    * Instead, each line that fails to be transformed into a [TemperatureReading] must be skipped.
    * <p/>
    * Reference: http://doc.akka.io/docs/akka/current/scala/stream/stream-error.html
    * <p/>
    * Check: The kata is solved when the corresponding unit test is green.
    *
    * @return The source.
    */
  private[kata04] def createRobustFlowOfStringToTemperatureReading(): Flow[String, TemperatureReading, NotUsed] = {
    ???
  }

}
