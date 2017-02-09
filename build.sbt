lazy val `codecamp-akka-streams-scala-project` = project
  .in(file("."))
  .enablePlugins(GitVersioning)
  .aggregate(`code-camp-akka-streams-scala`)

lazy val `code-camp-akka-streams-scala` = project
  .in(file("katas"))
  .enablePlugins(JavaAppPackaging, DockerPlugin)

// settings for the project-root
name := "codecamp-akka-streams-scala-root"
organization := "net.sourcekick"
version := "PROJECT-ROOT"
unmanagedSourceDirectories.in(Compile) := Vector.empty
unmanagedSourceDirectories.in(Test) := Vector.empty
publishArtifact := false
scalaVersion := "2.12.0"
