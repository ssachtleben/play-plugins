import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "play-auth-plugin"
    val libVersion      = "2.1-SNAPSHOT"

    val appDependencies = Seq(
        javaCore,
        "commons-lang" % "commons-lang" % "2.4",
        "org.scribe" % "scribe" % "1.3.5",
        "com.ssachtleben" %% "play-base-plugin" % "2.1-SNAPSHOT",
        "com.ssachtleben" %% "play-event-plugin" % "2.1-SNAPSHOT"
    )

    val main = play.Project(appName, libVersion, appDependencies).settings(
		organization := "com.ssachtleben",
        publishArtifact in(Compile, packageDoc) := false,
        resolvers += Resolver.url("ssachtleben repository (snapshots)", url("http://ssachtleben.github.io/play-plugins/repository/snapshots/"))(Resolver.ivyStylePatterns)
    )
}