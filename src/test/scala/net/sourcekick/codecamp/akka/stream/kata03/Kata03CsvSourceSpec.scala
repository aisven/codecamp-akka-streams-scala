/**
  * Copyright 2017 by Sven Ludwig. All rights reserved.
  */
package net.sourcekick.codecamp.akka.stream.kata03

import java.time.Instant

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, Sink}
import net.sourcekick.codecamp.akka.stream.model.{TemperatureReading, TemperatureUnit}
import org.scalatest.{AsyncWordSpec, Matchers, ParallelTestExecution}

import scala.collection.immutable.Seq

class Kata03CsvSourceSpec extends AsyncWordSpec with Matchers with ParallelTestExecution {

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

  "Kata03CsvSource" must {

    "create the correct stages" in {

      implicit val system = ActorSystem("Kata03")
      implicit val materializer = ActorMaterializer()

      val lineSource = Kata03CsvSource.createSourceOfLinesInFileKata03Csv() // Source[String, Future[IOResult]]

      val tempFlow = Kata03CsvSource.createFlowOfStringToTemperatureReading() // Flow[String, TemperatureReading, NotUsed]

      val runnableGraph = lineSource.viaMat(tempFlow)(Keep.left).toMat(Sink.seq)(Keep.both) //  RunnableGraph[(Future[IOResult], Future[Seq[TemperatureReading]])]

      val tuple = runnableGraph.run() // (Future[IOResult], Future[Seq[TemperatureReading]])

      val ioResultFuture = tuple._1 // Future[IOResult]

      val seqFuture = tuple._2 // Future[Seq[TemperatureReading]]

      ioResultFuture
        .map(ioResult => {
          ioResult shouldBe 'wasSuccessful
        })
        .flatMap(_ => seqFuture)
        .map(integers => {
          integers.size shouldBe 5
          integers shouldBe Seq(
            TemperatureReading("548e841a-6d98-4591-b4f9-3cc6ec04776d",
                               Instant.parse("2017-01-05T09:00:00Z"),
                               20.0F,
                               TemperatureUnit.Celsius),
            TemperatureReading("d7256984-06bf-4bec-983c-428239dd0cce",
                               Instant.parse("2017-01-05T09:30:00Z"),
                               20.3F,
                               TemperatureUnit.Celsius),
            TemperatureReading("1d651905-0d0d-4c0c-ad93-49f893b209ca",
                               Instant.parse("2017-01-05T10:00:00Z"),
                               20.5F,
                               TemperatureUnit.Celsius),
            TemperatureReading("af544d18-b3fe-4ea6-a6bb-fcc3677b090c",
                               Instant.parse("2017-01-05T10:30:00Z"),
                               21.1F,
                               TemperatureUnit.Celsius),
            TemperatureReading("02afe543-721e-4d73-b62c-0d64fda4c9d5",
                               Instant.parse("2017-01-05T11:00:00Z"),
                               20.7F,
                               TemperatureUnit.Celsius)
          )

        })
    }

  }

}
