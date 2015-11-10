name := """play-auth-plugin"""

version := "4.1"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
    "org.apache.commons" % "commons-lang3" % "3.3.2",
    "org.scribe" % "scribe" % "1.3.7",
    "com.ssachtleben" %% "play-base-plugin" % "4.0",
    "com.ssachtleben" %% "play-event-plugin" % "4.0"
)

organization := "com.ssachtleben"

publishArtifact in(Compile, packageDoc) := false

resolvers += Resolver.url("ssachtleben repository (snapshots)", url("http://ssachtleben.github.io/play-plugins/repository/snapshots/"))(Resolver.ivyStylePatterns)