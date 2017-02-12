package net.sourcekick.codecamp.akka.stream.kata11

import akka.NotUsed
import akka.stream.scaladsl.Flow

private[kata11] object Kata11WordCountFlow {

  /**
    * Task: Create a akka.stream.scaladsl.Flow for word count, i.e. with input type String
    * and output type (String, Int) that counts the number of occurrences of each String.
    * <p/>
    * Reference: http://doc.akka.io/docs/akka/current/scala/stream/stream-cookbook.html
    * <p/>
    * Check: The kata is solved when the corresponding unit test is green.
    *
    * @return The flow.
    */
  private[kata11] def createWordCountFlow: Flow[String, (String, Int), NotUsed] = {
    ???
  }

}
