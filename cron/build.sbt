name := """play-cron-plugin"""

version := "3.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
    "us.theatr" %% "akka-quartz" % "0.3.0" exclude("org.scala-lang", "scala-library"),
    "com.ssachtleben" %% "play-base-plugin" % "3.0-SNAPSHOT"
)

organization := "com.ssachtleben"

publishArtifact in(Compile, packageDoc) := false

resolvers += "Theatr.us repository" at "http://repo.theatr.us/"

resolvers += Resolver.url("ssachtleben repository (snapshots)", url("http://ssachtleben.github.io/play-plugins/repository/snapshots/"))(Resolver.ivyStylePatterns)