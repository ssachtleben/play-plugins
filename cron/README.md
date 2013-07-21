# Play Cron Plugin - A time-based job scheduler plugin for Play Framework 2.1.x

This plugin provides a cron service to run time-based jobs. 

## Features

* Find and register any ```@Cronjob``` annotated job classes

* Execute jobs at the given [cron expression](http://quartz-scheduler.org/documentation/quartz-2.1.x/tutorials/tutorial-lesson-06#TutorialLesson6-CronExpressions)

* Disable jobs without removing them (set active boolean to false)

## Requirements

**play-cron-plugin currently needs Play Framework 2.x**

The `master` branch contains the code for 2.1.0 and the plugin is only tested with Play Framework 2.1.0.

## Installation

* Add to repository resolvers: ```resolvers += Resolver.url("ssachtleben repo (snapshots)", url("http://ssachtleben.github.io/play-plugins/repository/snapshots/"))(Resolver.ivyStylePatterns)```

* Add to your dependencies: ```"com.ssachtleben" %% "play-cron-plugin" % "0.1-SNAPSHOT"```

* Create if not exists a file called ```play.plugins``` in your ```app/conf``` directory

* Add ```1500:com.ssachtleben.play.plugin.cron.CronPlugin``` to ```play.plugins```

## Usage

Creating new cronjobs never been easier. Just create a new job class. It must be impements the Runnable interface and add the @Cronjob annotation:

```java
import com.ssachtleben.play.plugin.cron.Cronjob;

@Cronjob(pattern = "0 * * * * ?")
public class EveryMinuteJob implements Runnable {

  @Override
  public void run() {
    // Do whatever you want here ...
  }

}
```

Maybe you want to disable a job temporarily or permanent but not delete the job code itself its possible to set the active boolean to false and the job will not run:

```java
import com.ssachtleben.play.plugin.cron.Cronjob;

@Cronjob(pattern = "0 * * * * ?", active = false)
public class DisabledJob implements Runnable {

  @Override
  public void run() {
    // Do whatever you want here ...
  }

}
```

If any problem or unexpected behavior occur change the log level in ```app/conf/application.conf``` to debug for play-cron-plugin, try again and check the logs. Maybe its just a wrong pattern or something like that.

```
# Logger for play-cron-plugin
logger.com.ssachtleben.play.plugin.cron=DEBUG
```

For more details check out the [play-cron-plugin-usage](samples/play-cron-plugin-usage) example project.

## Todos

* Add test cases
* Add more cronjobs to "play-cron-plugin-usage" project (For example with database read and write parts)
* Add possibility to pass a package path for the class search (Speed up the search for larger projects)
* Check job behaviour on cluster deployments (Jobs shouldnt run on multiple servers)

## Licence

This software is licensed under the Apache 2 license, quoted below.

Copyright (c) 2013 Sebastian Sachtleben

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.