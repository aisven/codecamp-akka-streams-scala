val company = "net.sourcekick"
name := "codecamp-akka-streams-scala"

val versionString: String = "1.0.1-SNAPSHOT"
version := versionString

scalaVersion := Version.Scala
crossScalaVersions := Seq(Version.Scala)

parallelExecution in Test := true

libraryDependencies ++= Vector(
  Library.scalaTest % Test,
  // Akka
  Library.akkaTestkit % Test,
  Library.akkaSlf4j,
  Library.akkaStreams,
  Library.akkaStreamsKafka,
  Library.scalaTime,
  // logging
  Library.logbackClassic
)

publishArtifact := false
publishArtifact in Test := false

//initialCommands := """|import de.heikoseeberger.gabbler.user._|""".stripMargin

/******************************************************************************************
                                      Docker
  ******************************************************************************************/
enablePlugins(JavaAppPackaging)
enablePlugins(AshScriptPlugin)

mappings in Universal += {
  // we are using the reference.conf as default application.conf
  // the user can override settings here
  val conf = (resourceDirectory in Compile).value / "reference.conf"
  conf -> "conf/application.conf"
}
