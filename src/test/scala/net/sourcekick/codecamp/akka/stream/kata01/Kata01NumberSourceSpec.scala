/**
  * Copyright 2017 by Sven Ludwig. All rights reserved.
  */
package net.sourcekick.codecamp.akka.stream.kata01

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, Sink}
import org.scalatest.{AsyncWordSpec, Matchers, ParallelTestExecution}
import org.slf4j.LoggerFactory

import scala.collection.immutable.Seq

class Kata01NumberSourceSpec extends AsyncWordSpec with Matchers with ParallelTestExecution {

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

  val log = LoggerFactory.getLogger("Kata01NumberSource")

  "Kata01NumberSource" must {

    "create the correct source" in {

      implicit val system = ActorSystem("Kata01")
      implicit val materializer = ActorMaterializer()

      val numberSource = Kata01NumberSource.createSourceOfNaturalNumbers1to10() // Source[Int, NotUsed]

      val runnableGraph = numberSource.toMat(Sink.seq)(Keep.right) // RunnableGraph[Future[Seq[Int]]]

      val seqFuture = runnableGraph.run() // Future[Seq[Int]]

      seqFuture.map(integers => {
        Thread.sleep(1000) // to see all log output
        integers.size shouldBe 10
        integers shouldBe Seq(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
      })
    }

  }

}
