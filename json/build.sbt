name := """play-json-plugin"""

version := "4.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

organization := "com.ssachtleben"

publishArtifact in(Compile, packageDoc) := false