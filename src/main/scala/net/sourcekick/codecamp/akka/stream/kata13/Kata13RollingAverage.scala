/**
  * Copyright 2017 by Sven Ludwig. All rights reserved.
  */
package net.sourcekick.codecamp.akka.stream.kata13

import akka.NotUsed
import akka.stream.scaladsl.Flow

private[kata13] object Kata13RollingAverage {

  /**
    * Task: Create a akka.stream.scaladsl.Flow that calculates the rolling average
    * over the incoming sequence of Float values
    * with sliding window semantics
    * where each window contains 100 values
    * and where the following window does not contain the first 10 values of the previous window
    * and where the following window contains also the 10 first values of the window thereafter.
    * <p/>
    * Check: The kata is solved when the corresponding unit test is green.
    *
    * @return The flow.
    */
  private[kata13] def createRollingAverageFlow(): Flow[Float, Float, NotUsed] = {
    ???
  }

}
