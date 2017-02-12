package net.sourcekick.codecamp.akka.stream.kata09

import akka.NotUsed
import akka.stream.{FanInShape2, Graph}

object Kata09FanInShape {

  /**
    * Task: Create a fan in shape as a raw unconnected graph
    * that can combine a stream of strings
    * and a stream of integers into a stream of pairs.
    * <p/>
    * Tip: To define processing pipelines it is sufficient to use:
    * <ul>
    * <li>akka.stream.scaladsl.Source</li>
    * <li>akka.stream.scaladsl.Flow</li>
    * <li>akka.stream.scaladsl.Sink</li>
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
    * @return The flow.
    */
  private[kata09] def createFanInShape2(): Graph[FanInShape2[String, Int, (String, Int)], NotUsed] = {
    ???
  }

}
