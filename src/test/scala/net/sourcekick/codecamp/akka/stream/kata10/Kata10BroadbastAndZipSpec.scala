/**
  * Copyright 2017 by Sven Ludwig. All rights reserved.
  */
package net.sourcekick.codecamp.akka.stream.kata10

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.RunnableGraph
import org.scalatest.{AsyncWordSpec, Matchers, ParallelTestExecution}

class Kata10BroadbastAndZipSpec extends AsyncWordSpec with Matchers with ParallelTestExecution {

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

  "Kata10BroadbastAndZip" must {

    "create the correct graph" in {

      implicit val system = ActorSystem("Kata10")
      implicit val materializer = ActorMaterializer()

      val g = Kata10BroadcastAndZip.createGraphWithFanInAndFanOut

      val rg = RunnableGraph.fromGraph(g)

      rg.run()
        .map(integers => {
          integers shouldBe Seq(7, 11, 15, 19, 23, 27, 31, 35, 39, 43)
        })
    }

  }

}
