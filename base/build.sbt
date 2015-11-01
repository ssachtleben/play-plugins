name := """play-base-plugin"""

version := "4.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "commons-beanutils" % "commons-beanutils" % "1.9.2"
)

organization := "com.ssachtleben"

publishArtifact in(Compile, packageDoc) := false

fork in run := true