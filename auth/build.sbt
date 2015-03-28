name := """play-auth-plugin"""

version := "3.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
    "commons-lang" % "commons-lang" % "2.4",
    "org.scribe" % "scribe" % "1.3.5",
    "com.ssachtleben" %% "play-base-plugin" % "3.0-SNAPSHOT",
    "com.ssachtleben" %% "play-event-plugin" % "3.0-SNAPSHOT"
)

organization := "com.ssachtleben"

publishArtifact in(Compile, packageDoc) := false

resolvers += Resolver.url("ssachtleben repository (snapshots)", url("http://ssachtleben.github.io/play-plugins/repository/snapshots/"))(Resolver.ivyStylePatterns)