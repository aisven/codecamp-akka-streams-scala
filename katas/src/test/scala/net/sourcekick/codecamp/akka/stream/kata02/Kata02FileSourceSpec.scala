package net.sourcekick.codecamp.akka.stream.kata02

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, Sink}
import org.scalatest.{AsyncWordSpec, Matchers, ParallelTestExecution}

import scala.collection.immutable.Seq

class Kata02FileSourceSpec extends AsyncWordSpec with Matchers with ParallelTestExecution {

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

  "Kata02FileSource" must {

    "create the correct source" in {

      implicit val system = ActorSystem("Kata02")
      implicit val materializer = ActorMaterializer()

      val numberSource = Kata02FileSource.createSourceOfLinesInFileKata02Txt() // Source[String, CompletionStage[IOResult]]

      val runnableGraph = numberSource.toMat(Sink.seq)(Keep.both) // RunnableGraph[(CompletionStage[IOResult], Future[Seq[String]])]

      val tuple = runnableGraph.run() // (Future[IOResult], Future[Seq[String]])

      val ioResultFuture = tuple._1 // Future[IOResult]

      val seqFuture = tuple._2 // Future[Seq[String]]

      ioResultFuture
        .map(ioResult => {
          ioResult shouldBe 'wasSuccessful
        })
        .flatMap(_ => seqFuture)
        .map(integers => {
          integers.size shouldBe 10
          integers shouldBe Seq("line one",
                                "this line is the second line",
                                "the third line",
                                "line four",
                                "line five",
                                "line six",
                                "line seven",
                                "line eight",
                                "line nine",
                                "last line")

        })

    }
  }

}
