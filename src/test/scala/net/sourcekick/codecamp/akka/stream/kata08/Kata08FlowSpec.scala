/**
  * Copyright 2017 by Sven Ludwig. All rights reserved.
  */
package net.sourcekick.codecamp.akka.stream.kata08

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, Sink, Source}
import org.scalatest.{AsyncWordSpec, Matchers, ParallelTestExecution}

class Kata08FlowSpec extends AsyncWordSpec with Matchers with ParallelTestExecution {

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

  "Kata08Flow" must {

    "create the correct flow" in {

      implicit val system = ActorSystem("Kata08")
      implicit val materializer = ActorMaterializer()

      val stringLengthFlow = Kata08Flow.createStringLengthFlow()

      val testInputSource = Source.fromIterator(() => Seq("a", "ab", "abc", "abcd", "abcde", "t").iterator)

      val runnableGraph = testInputSource.via(stringLengthFlow).toMat(Sink.seq)(Keep.right)

      runnableGraph
        .run()
        .map(integers => {
          integers.size shouldBe 6
          integers shouldBe Seq(1, 2, 3, 4, 5, 1)
        })
    }

  }

}
