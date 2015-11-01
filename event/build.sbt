name := """play-event-plugin"""

version := "4.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "com.ssachtleben" %% "play-base-plugin" % "4.0"
)

organization := "com.ssachtleben"

publishArtifact in(Compile, packageDoc) := false

resolvers += Resolver.url("ssachtleben repository (snapshots)", url("http://ssachtleben.github.io/play-plugins/repository/snapshots/"))(Resolver.ivyStylePatterns)