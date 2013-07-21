import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "play-cron-plugin-usage"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    javaCore,
    javaJdbc,
    javaEbean,
    "com.ssachtleben" %% "play-cron-plugin" % "0.1-SNAPSHOT"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
      resolvers += Resolver.url("ssachtleben repo (snapshots)", url("http://ssachtleben.github.io/play-plugins/repository/snapshots/"))(Resolver.ivyStylePatterns)
  )

}
