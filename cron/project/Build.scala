import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "play-cron-plugin"
    val libVersion      = "0.1-SNAPSHOT"

    val appDependencies = Seq(
        javaCore,
        "us.theatr" %% "akka-quartz" % "0.2.0",
        "com.ssachtleben" %% "play-base-plugin" % "0.1-SNAPSHOT"
    )

    val main = play.Project(appName, libVersion, appDependencies).settings(
		resolvers += "Theatr.us repository" at "http://repo.theatr.us/",
		resolvers += Resolver.url("ssachtleben repository (snapshots)", url("http://ssachtleben.github.io/play-plugins/repository/snapshots/"))(Resolver.ivyStylePatterns),
		organization := "com.ssachtleben",
        publishArtifact in(Compile, packageDoc) := false
    )
}