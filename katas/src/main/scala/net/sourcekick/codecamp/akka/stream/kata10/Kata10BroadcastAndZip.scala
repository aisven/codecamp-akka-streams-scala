package net.sourcekick.codecamp.akka.stream.kata10

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.stream.{ClosedShape, Graph}

import scala.collection.immutable.Seq
import scala.concurrent.Future

object Kata10BroadcastAndZip {

  /**
    * Task: Create an akka.stream.Graph that has no unconnected inlets or outlets
    * anywhere and that looks like the following:
    * <ul>
    * <li>It starts with source createSource()</li>.
    * <li>It forks via a broadcasting uniform fan out into three branches.</li>.
    * <li>Branch 0 goes via createFlow0_0()
    * and then createFlow0_1().</li>.
    * <li>Branch 1 just goes via createFlow1_0().</li>.
    * <li>Branch 2 goes via createFlow2_0()
    * and createFlow2_1()
    * and then createFlow2_2().</li>.
    * <li>It joins the three branches again via a zipping fan in
    * that simply uses addition to combine each three integers by calculating their sum.</li>.
    * <li>The outlet of the fan in is connected to the sink createSink().</li>.
    * </ul>
    * </ul>
    * To define processing graphs with patterns such as forks, fan out, balancing,
    * joins or fan in it is necessary to work with the abstractions Shape or AbstractShape
    * and their sub-classes in order to create an akka.stream.Graph.
    * For more information see the reference documentation linked below.
    * <p/>
    * Reference: http://doc.akka.io/docs/akka/current/scala/stream/stream-graphs.html
    * <p/>
    * Check: The kata is solved when the corresponding unit test is green.
    *
    * @return The graph.
    */
  private[kata10] def createGraphWithFanInAndFanOut: Graph[ClosedShape.type, Future[Seq[Int]]] = {
    ???
  }

  private def createSource: Source[Int, NotUsed] = Source(1 to 10)

  private def createFlow0_0: Flow[Int, Int, NotUsed] = Flow.fromFunction[Int, Int](i => i - 1)

  private def createFlow0_1: Flow[Int, Int, NotUsed] = Flow.fromFunction[Int, Int](i => i - 2)

  private def createFlow1_0: Flow[Int, Int, NotUsed] = Flow.fromFunction[Int, Int](i => i * 2)

  private def createFlow2_0: Flow[Int, Int, NotUsed] = Flow.fromFunction[Int, Int](i => i + 1)

  private def createFlow2_1: Flow[Int, Int, NotUsed] = Flow.fromFunction[Int, Int](i => i + 2)

  private def createFlow2_2: Flow[Int, Int, NotUsed] = Flow.fromFunction[Int, Int](i => i + 3)

  private def createSink: Sink[Int, Future[Seq[Int]]] = Sink.seq[Int]

}
