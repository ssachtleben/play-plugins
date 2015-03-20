# Play Compress Plugin - A html compress plugin for Play Framework 2.1.x

**IMPORTANT: The plugin is still in develop and maybe cause unexpected behaviors**

## Usage

Add this in the 'build.sbt':

resolvers += Resolver.url("ssachtleben repo (snapshots)", url("http://ssachtleben.github.io/play-plugins/repository/snapshots/"))(Resolver.ivyStylePatterns)

libraryDependencies += "com.ssachtleben" %% "play-compress-plugin" % "3.0-SNAPSHOT"