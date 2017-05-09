package net.sourcekick.codecamp.akka.stream.kata09

import akka.actor.ActorSystem
import akka.stream.scaladsl.{GraphDSL, RunnableGraph, Sink, Source}
import akka.stream.{ActorMaterializer, ClosedShape}
import org.scalatest.{AsyncWordSpec, Matchers, ParallelTestExecution}

import scala.collection.immutable.Seq
import scala.concurrent.Future

class Kata09FanInShapeSpec extends AsyncWordSpec with Matchers with ParallelTestExecution {

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

  "Kata09FanInShape" must {

    "zip correctly" in {
      implicit val system = ActorSystem("Kata09")
      implicit val materializer = ActorMaterializer()

      val fanInShape2 = Kata09FanInShape.createFanInShape2()

      val stringSource = Source.fromIterator(() => Seq("1", "2", "3").iterator)
      val intSource = Source(1 to 3)
      val seqSink = Sink.seq[(String, Int)]

      val graph = GraphDSL.create(seqSink) { implicit builder => sink =>
        import GraphDSL.Implicits._

        val stringSourceShape = builder.add(stringSource)
        val intSourceShape = builder.add(intSource)
        val fanInShape2Shape = builder.add(fanInShape2)

        stringSourceShape ~> fanInShape2Shape.in0
        intSourceShape ~> fanInShape2Shape.in1

        fanInShape2Shape.out ~> sink
        ClosedShape
      }

      val runnableGraph: RunnableGraph[Future[Seq[(String, Int)]]] = RunnableGraph.fromGraph(graph)

      runnableGraph
        .run()
        .map(tuples => {
          tuples.size shouldBe 3
          tuples.map(t => t._1) shouldBe Vector("1", "2", "3")
          tuples.map(t => t._2) shouldBe (1 to 3)
        })
    }

  }

}
