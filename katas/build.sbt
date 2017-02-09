libraryDependencies ++= Vector(
  Library.scalaTest % Test,
// Akka
  Library.akkaTestkit % Test,
  Library.akkaSlf4j,
  Library.scalaTime,
// logging
  Library.logbackClassic
)

parallelExecution in Test := false

publishArtifact := false
publishArtifact in Test := false
