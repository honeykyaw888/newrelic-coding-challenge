# coding-challenge

Coding Challenge Build Framework

## Starter build framework for the coding challenge

First, you do not need to use this starter framework for your project.
If you would rather use a different build system (maven, javac, ...)
you are free to so long as you provide clear commands to build your
project and start your server.  Failure to do so will invalidate your
submission.


## Install Java

The code was tested and compile in Oralce latest version of JDK 11 and JDK 8. 


## Gradle

The build framework provided here uses gradle to build your project
and manage your dependencies.  The `gradlew` command used here will
automatically download gradle for you so you shouldn't need to install
anything other than java.


### Project Layout

All source code should be located in the `src/main/java` folder.
If you wish to write any tests (not a requirement) they should be
located in the `src/test/java` folder.

A starter `Main.java` file has been provided in the `com/newrelic/codingchallenge` package under `src/main/java`.


### Dependencies

If your project has any dependencies you can list them in the
`build.gradle` file in the `dependencies` section.


### Building your project from the command line

To build the project on Linux or MacOS run the command `./gradlew build` in a shell terminal.  This will build the source code in
`src/main/java`, run any tests in `src/test/java` and create an output
jar file in the `build/libs` folder.

To clean out any intermediate files run `./gradlew clean`.  This will
remove all files in the `build` folder.


### Running your application from the command line

You first must create a shadow jar file.  This is a file which contains your project code and all dependencies in a single jar file.  To build a shadow jar from your project run `./gradlew shadowJar`.  This will create a `codeing-challenge-shadow.jar` file in the `build/libs` directory.

You can then start your application by running the command
`java -jar ./build/libs/coding-challenge-shadow.jar`

You can then start the client by running the command
Client generates number of length 0 to 9 and send them to server through client socket channel
`$cd build/libs`
`$java -cp coding-challenge-shadow.jar com.newrelic.codingchallenge.client.SocketClient`

## SAMPLE OUTPUT
$ java -jar ./build/libs/coding-challenge-shadow.jar 
23:07:14.103 [main] INFO com.newrelic.codingchallenge.Main - Starting up server ....
23:07:14.108 [Message-Processor] INFO com.newrelic.codingchallenge.Main - Running in message processor
23:07:24.115 [pool-1-thread-1] INFO com.newrelic.codingchallenge.report.ReportGenerator - Received 0 unique numbers, 0. Unique total: 0
23:07:34.116 [pool-1-thread-1] INFO com.newrelic.codingchallenge.report.ReportGenerator - Received 0 unique numbers, 0. Unique total: 0
23:07:38.578 [pool-2-thread-1] INFO com.newrelic.codingchallenge.client.SocketClient - Acquired a permit from semaphore. We have 4 permit left!
23:07:38.578 [main] INFO com.newrelic.codingchallenge.server.SocketServer - Connection Accepted: /127.0.0.1:50139

23:07:45.313 [pool-1-thread-1] INFO com.newrelic.codingchallenge.report.ReportGenerator - Received 1155413 unique numbers, 674. Unique total: 1156087
23:07:56.189 [pool-1-thread-1] INFO com.newrelic.codingchallenge.report.ReportGenerator - Received 2595472 unique numbers, 106858. Unique total: 3858417
23:08:05.611 [pool-1-thread-1] INFO com.newrelic.codingchallenge.report.ReportGenerator - Received 2390053 unique numbers, 160516. Unique total: 6408986
23:08:16.029 [pool-1-thread-1] INFO com.newrelic.codingchallenge.report.ReportGenerator - Received 2767640 unique numbers, 160026. Unique total: 9336652
23:08:26.351 [pool-1-thread-1] INFO com.newrelic.codingchallenge.report.ReportGenerator - Received 2661532 unique numbers, 205016. Unique total: 12203200


$ java -cp coding-challenge-shadow.jar com.newrelic.codingchallenge.client.SocketClient
23:07:38.577 [main] INFO com.newrelic.codingchallenge.client.SocketClient - Connecting to Server on port 8000...


## IDEA

You are free to use whichever editor or IDE you want providing your
projects build does not depend on that IDE.  Most of the Java
developers at New Relic use IDEA from
[JetBrains](https://www.jetbrains.com/).  JetBrains provides
a community edition of IDEA which you can download and use without
charge.

If you are planning to use IDEA you can generate the IDEA project files
by running `./gradlew idea` and directly opening the project folder
as a project in idea.

