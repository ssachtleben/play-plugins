import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "play-event-plugin"
    val libVersion      = "2.1-SNAPSHOT"

    val appDependencies = Seq(
        javaCore,
        "com.ssachtleben" %% "play-base-plugin" % "2.1-SNAPSHOT"
    )

    val main = play.Project(appName, libVersion, appDependencies).settings(
        resolvers += Resolver.url("ssachtleben repository (snapshots)", url("http://ssachtleben.github.io/play-plugins/repository/snapshots/"))(Resolver.ivyStylePatterns),
        organization := "com.ssachtleben",
        publishArtifact in(Compile, packageDoc) := false
    )
    
}