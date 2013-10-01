import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "play-json-plugin"
    val libVersion      = "1.1-SNAPSHOT"

    val appDependencies = Seq(
        javaCore
        //"com.fasterxml.jackson.core" % "jackson-databind" % "2.2.2",
        //"com.fasterxml.jackson.core" % "jackson-core" % "2.2.2",
        //"com.fasterxml.jackson.core" % "jackson-annotations" % "2.2.2"
    )

    val main = play.Project(appName, libVersion, appDependencies).settings(
		organization := "com.ssachtleben",
        publishArtifact in(Compile, packageDoc) := false
    )
}