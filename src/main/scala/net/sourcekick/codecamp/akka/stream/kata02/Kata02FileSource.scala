/**
  * Copyright 2017 by Sven Ludwig. All rights reserved.
  */
package net.sourcekick.codecamp.akka.stream.kata02

import akka.stream.IOResult
import akka.stream.scaladsl.Source

import scala.concurrent.Future

private[kata02] object Kata02FileSource {

  /**
    * Task: Create a akka.stream.scaladsl.Source that emits line by line of kata02.txt file.
    * <p/>
    * Tip: The direct relative path to the file is usually src/main/resources/kata02.txt when using an IDE.
    * <p/>
    * Tip: http://doc.akka.io/docs/akka/current/scala/stream/stream-cookbook.html#Logging_elements_of_a_stream
    * <p/>
    * Reference: http://doc.akka.io/docs/akka/current/scala/stream/stream-io.html#Streaming_File_IO
    * <p/>
    * Check: The kata is solved when the corresponding unit test is green.
    *
    * @return The source.
    */
  private[kata02] def createSourceOfLinesInFileKata02Txt(): Source[String, Future[IOResult]] = {
    ???
  }

}
