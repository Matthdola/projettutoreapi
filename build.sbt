name := "medicalbaseapi"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "net.vz.mongodb.jackson" % "play-mongo-jackson-mapper_2.10" % "1.1.0",
  "joda-time" % "joda-time" % "2.5",
  "org.mongodb" % "mongo-java-driver" % "2.12.4" withSources(),
  "org.apache.commons" % "commons-csv" % "1.4" withSources(),
  "commons-validator" % "commons-validator" % "1.5.1"  withSources()
)

javaOptions in Test += "-Dconfig.file=conf/test.conf"
