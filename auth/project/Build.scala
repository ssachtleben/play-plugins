import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "play-auth-plugin"
    val libVersion      = "0.1-SNAPSHOT"

    val appDependencies = Seq(
        javaCore,
        "org.scribe" % "scribe" % "1.3.5"
    )

    val main = play.Project(appName, libVersion, appDependencies).settings(
		organization := "com.ssachtleben",
        publishArtifact in(Compile, packageDoc) := false
    )
}