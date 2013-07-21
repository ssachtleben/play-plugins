import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "play-cron"
    val libVersion      = "0.1-SNAPSHOT"

    val appDependencies = Seq(
        javaCore,
		"us.theatr" %% "akka-quartz" % "0.2.0"
    )

    val main = play.Project(appName, libVersion, appDependencies).settings(
        publishArtifact in(Compile, packageDoc) := false
    )
}