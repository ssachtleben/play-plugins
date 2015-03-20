name := """play-compress-plugin"""

version := "2.2-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "com.googlecode.htmlcompressor" % "htmlcompressor" % "1.5.2",
  "com.yahoo.platform.yui" % "yuicompressor" % "2.4.6"
)

organization := "com.ssachtleben"

publishArtifact in(Compile, packageDoc) := false