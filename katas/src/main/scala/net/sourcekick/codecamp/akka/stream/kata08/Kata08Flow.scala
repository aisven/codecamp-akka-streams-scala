package net.sourcekick.codecamp.akka.stream.kata08

import akka.NotUsed
import akka.stream.scaladsl.Flow

object Kata08Flow {

  /**
    * Task: Create a simple akka.stream.scaladsl.Flow that transforms each string into an integer
    * which is the length of the string.
    * <p/>
    * Tip: An akka.stream.scaladsl.Flow is a specialization of akka.stream.FlowShape
    * and always has exactly one input and one output.
    * <p/>
    * Tip: Look at the API of akka.stream.scaladsl.Flow and consider to use an available factory method.
    * <p/>
    * Reference: http://doc.akka.io/docs/akka/current/scala/stream/stream-composition.html
    * <p/>
    * Check: The kata is solved when the corresponding unit test is green.
    *
    * @return The flow.
    */
  private[kata08] def createStringLengthFlow(): Flow[String, Int, NotUsed] = {
    ???
  }

}
