package net.sourcekick.codecamp.akka.stream.kata11

import java.nio.file.Paths

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{FileIO, Flow, Framing, Keep, Sink, Source}
import akka.stream.{ActorMaterializer, IOResult}
import akka.util.ByteString
import org.scalatest.{AsyncWordSpec, Matchers, ParallelTestExecution}

import scala.concurrent.Future

class Kata11WordCountFlowSpec extends AsyncWordSpec with Matchers with ParallelTestExecution {

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

  "Kata11WordCountFlow" must {

    "count words correctly" in {

      implicit val system = ActorSystem("Kata11")
      implicit val materializer = ActorMaterializer()

      val wordSource: Source[String, Future[IOResult]] = createFile2WordsSource("kata11.txt")

      val wordCountFlow: Flow[String, (String, Int), NotUsed] = Kata11WordCountFlow.createWordCountFlow

      val runnableGraph = wordSource.viaMat(wordCountFlow)(Keep.left).toMat(Sink.seq)(Keep.both)

      val tuple = runnableGraph.run()

      val ioResultFuture = tuple._1

      val resultFuture = tuple._2

      ioResultFuture
        .map(ioResult => {
          ioResult.wasSuccessful shouldBe true
        })
        .flatMap(_ => resultFuture)
        .map(counts => {
          counts should contain("streams", 1)
          counts should contain("it", 1)
          counts should contain("they", 3)
          counts should contain("the", 8)
        })
    }
  }

  private def createFile2WordsSource(fileName: String): Source[String, Future[IOResult]] = {
    val url = Thread.currentThread().getContextClassLoader.getResource(fileName)

    val lineSource: Source[String, Future[IOResult]] = FileIO
      .fromPath(Paths.get(url.getPath))
      .viaMat(Framing.delimiter(ByteString("\n"), 1000, allowTruncation = true))(Keep.left)
      .viaMat(Flow.fromFunction(bs => bs.utf8String))(Keep.left)

    val wordSource: Source[String, Future[IOResult]] = lineSource
      .viaMat(Flow.fromFunction(s => s.split(" ")))(Keep.left)
      .flatMapConcat(a => Source.fromIterator[String](() => a.iterator))

    val cleanWordSource =
      wordSource.map(s => s.replace(";", "")).map(s => s.replace(",", "")).map(s => s.replace(".", ""))

    cleanWordSource
  }

}
