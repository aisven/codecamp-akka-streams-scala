package net.sourcekick.codecamp.akka.stream.kata05

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, Source}
import org.scalatest.{AsyncWordSpec, Matchers, ParallelTestExecution}

class Kata05FoldingSinkSpec extends AsyncWordSpec with Matchers with ParallelTestExecution {

  "Kata05FoldingSink" must {

    "create the correct stages" in {

      implicit val system = ActorSystem("Kata05")
      implicit val materializer = ActorMaterializer()

      val addSink = Kata05FoldingSink.createFoldingSinkWithAddition() // Sink[Int, Future[Integer]]

      val multiplySink = Kata05FoldingSink.createFoldingSinkWithMultiplication() // Sink[Int, Future[Integer]]

      val add3To6RunnableGraph = Source(3 to 6).toMat(addSink)(Keep.right)
      val multiply3To6RunnableGraph = Source(3 to 6).toMat(multiplySink)(Keep.right)
      val add1To10RunnableGraph = Source(1 to 10).toMat(addSink)(Keep.right)
      val multiply1To10RunnableGraph = Source(1 to 10).toMat(multiplySink)(Keep.right)

      val add3To6Future = add3To6RunnableGraph.run()
      val multiply3To6Future = multiply3To6RunnableGraph.run()
      val add1To10Future = add1To10RunnableGraph.run()
      val multiply1To10Future = multiply1To10RunnableGraph.run()

      add3To6Future
        .map(sum => {
          sum shouldBe 18
        })
        .flatMap(_ => multiply3To6Future)
        .map(product => {
          product shouldBe 360
        })
        .flatMap(_ => add1To10Future)
        .map(product => {
          product shouldBe 55
        })
        .flatMap(_ => multiply1To10Future)
        .map(product => {
          product shouldBe 3628800
        })
    }

  }

}
