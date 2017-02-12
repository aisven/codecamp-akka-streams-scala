/**
  * Copyright 2017 by Sven Ludwig. All rights reserved.
  */
package net.sourcekick.codecamp.akka.stream.kata13

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.scaladsl.{FileIO, Flow, Framing, Keep, Sink, Source}
import akka.stream.{ActorMaterializer, IOResult}
import akka.util.ByteString
import org.scalatest.{AsyncWordSpec, Matchers, ParallelTestExecution}

import scala.concurrent.Future
import scala.util.Random

class Kata13RollingAverageSpec extends AsyncWordSpec with Matchers with ParallelTestExecution {

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

  "Kata13RollingAverage" must {

    "calculate the correct rolling average" in {

      implicit val system = ActorSystem("Kata13")
      implicit val materializer = ActorMaterializer()

      val floatSource = createFile2FloatSource("kata13.txt")
      //val floatSource = createFile2FloatSource("1to1000.txt")

      val flow = Kata13RollingAverage.createRollingAverageFlow()

      val runnableGraph = floatSource.viaMat(flow)(Keep.left).toMat(Sink.seq)(Keep.both)

      val tuple = runnableGraph.run()

      val ioResultFuture = tuple._1
      val resultFuture = tuple._2

      val expectedFuture = createFile2FloatSource("kata13expected.txt").runWith(Sink.seq)

      expectedFuture
        .map(expectedAverages => {
          expectedAverages.size shouldBe 91
        })
        .flatMap(_ => ioResultFuture)
        .map(ioResult => {
          ioResult.wasSuccessful shouldBe true
        })
        .flatMap(_ => resultFuture)
        .map(averages => {
          //averages.foreach(println)
          averages.size shouldBe expectedFuture.value.get.toOption.get.size
          averages shouldBe expectedFuture.value.get.toOption.get
        })
    }

    "generate new random float sequence" in {

      val r = Random
      Future {
        (1 to 1000)
          .map(_ => r.nextFloat() * 100F)
          .map(f => f % 4)
          .map(f => f + 19F)
          .map(f => "%.2f".format(f).toFloat)
          .sorted
      }.map(floats => {
        //floats.foreach(println)
        floats.size shouldBe 1000
      })
    }

  }

  private def createFile2FloatSource(fileName: String): Source[Float, Future[IOResult]] = {
    val url = Thread.currentThread().getContextClassLoader.getResource(fileName)

    val lineSource: Source[String, Future[IOResult]] = FileIO
      .fromPath(Paths.get(url.getPath))
      .viaMat(Framing.delimiter(ByteString("\n"), 1000, allowTruncation = true))(Keep.left)
      .viaMat(Flow.fromFunction(bs => bs.utf8String))(Keep.left)

    val floatSource = lineSource.viaMat(Flow.fromFunction(s => s.toFloat))(Keep.left)

    floatSource
  }

}
