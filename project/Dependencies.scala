import sbt._

// format: off

object Version {
  final val Scala                    = "2.12.0"
  // test
  final val ScalaTest                = "3.0.1"
// AKka
  final val Akka                     = "2.4.17"
// time
  final val ScalaTime                = "0.4.1"
// logging
  final val Logback                  = "1.1.7"
}

object Library {
// test
  val scalaTest                  = "org.scalatest"            %% "scalatest"                    % Version.ScalaTest
// AKka
  val akkaTestkit                = "com.typesafe.akka"        %% "akka-testkit"                 % Version.Akka
  val akkaSlf4j                  = "com.typesafe.akka"        %% "akka-slf4j"                   % Version.Akka
  val akkaStreams                = "com.typesafe.akka"        %% "akka-stream"                  % Version.Akka
// time
  val scalaTime                  = "codes.reactive"           %% "scala-time"                   % Version.ScalaTime
// logging
  val logbackClassic             = "ch.qos.logback"           %  "logback-classic"              % Version.Logback
//  val log4jCore                  = "org.apache.logging.log4j" %  "log4j-core"                   % Version.Log4j
//  val log4jSlf4jImpl             = "org.apache.logging.log4j" %  "log4j-slf4j-impl"             % Version.Log4j
}
