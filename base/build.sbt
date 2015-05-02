name := """play-base-plugin"""

version := "3.1-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  "commons-beanutils" % "commons-beanutils" % "1.9.2"
)

organization := "com.ssachtleben"

publishArtifact in(Compile, packageDoc) := false