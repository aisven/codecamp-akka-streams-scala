/**
  * Copyright 2017 by Sven Ludwig. All rights reserved.
  */
package net.sourcekick.codecamp.akka.stream.kata05

import akka.stream.scaladsl.Sink

import scala.concurrent.Future

private[kata05] object Kata05FoldingSink {

  /**
    * Task: Create a akka.stream.scaladsl.Sink that consumes integers,
    * folds over them using addition and returns the aggregated result,
    * i.e. the aggregated sum of all the consumed integers.
    * <p/>
    * Tip: Use the appropriate factory method to be found in akka.stream.scaladsl.Sink to create the sink.
    * <p/>
    * Reference: http://doc.akka.io/docs/akka/current/scala/stream/stream-flows-and-basics.html
    * <p/>
    * Check: The kata is solved when the corresponding unit test is green.
    *
    * @return The source.
    */
  private[kata05] def createFoldingSinkWithAddition(): Sink[Int, Future[Int]] = {
    ???
  }

  /**
    * Task: Create a akka.stream.scaladsl.Sink that consumes integers,
    * folds over them using multiplication and returns the aggregated result,
    * i.e. the aggregated product of all the consumed integers.
    * <p/>
    * Tip: Use the appropriate factory method to be found in akka.stream.scaladsl.Sink to create the sink.
    * <p/>
    * Reference: http://doc.akka.io/docs/akka/current/scala/stream/stream-flows-and-basics.html
    * <p/>
    * Check: The kata is solved when the corresponding unit test is green.
    *
    * @return The sink.
    */
  private[kata05] def createFoldingSinkWithMultiplication(): Sink[Int, Future[Int]] = {
    ???
  }

}
