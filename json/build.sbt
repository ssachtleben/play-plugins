name := """play-json-plugin"""

version := "3.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

organization := "com.ssachtleben"

publishArtifact in(Compile, packageDoc) := false