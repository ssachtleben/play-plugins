import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "play-compress-plugin"
    val libVersion      = "2.1-SNAPSHOT"

    val appDependencies = Seq(
        javaCore,
        "com.googlecode.htmlcompressor" % "htmlcompressor" % "1.5.2",
        "com.yahoo.platform.yui" % "yuicompressor" % "2.4.6"
    )

    val main = play.Project(appName, libVersion, appDependencies).settings(
		organization := "com.ssachtleben",
        publishArtifact in(Compile, packageDoc) := false
    )
}