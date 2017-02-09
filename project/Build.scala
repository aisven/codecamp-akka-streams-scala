import com.typesafe.sbt.GitPlugin
import com.typesafe.sbt.GitPlugin.autoImport._
import org.scalafmt.sbt.ScalaFmtPlugin
import org.scalafmt.sbt.ScalaFmtPlugin.autoImport._
import sbt._
import sbt.plugins.JvmPlugin
import sbt.Keys._

// format: off

object Build extends AutoPlugin {

  override def requires =
    JvmPlugin && GitPlugin && ScalaFmtPlugin

  override def trigger = allRequirements

  override def projectSettings =
    reformatOnCompileSettings ++
    Vector(
      // Core settings
      organization := "net.sourcekick",
//      licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0")),
//      mappings.in(Compile, packageBin) += baseDirectory.in(ThisBuild).value / "LICENSE" -> "LICENSE",
      scalaVersion := Version.Scala,
      version := "0.1.0-SNAPSHOT",
      crossScalaVersions := Vector(scalaVersion.value),
      scalacOptions ++= Vector(
       "-unchecked",
       "-deprecation",
       "-language:_",
       "-target:jvm-1.8",
       "-encoding", "UTF-8"
      ),
      unmanagedSourceDirectories.in(Compile) := Vector(scalaSource.in(Compile).value),
      unmanagedSourceDirectories.in(Test) := Vector(scalaSource.in(Test).value),

      // scalafmt settings
      formatSbtFiles := false,
      scalafmtConfig := Some(baseDirectory.in(ThisBuild).value / ".scalafmt.conf"),
      ivyScala       := ivyScala.value.map(_.copy(overrideScalaVersion = sbtPlugin.value)), // TODO Remove once this workaround no longer needed (https://github.com/sbt/sbt/issues/2786)!

      // Git settings
      git.useGitDescribe := true

    )
}
