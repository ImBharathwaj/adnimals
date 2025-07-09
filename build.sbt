ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.6"

val PekkoVersion       = "1.1.0"
val PekkoCirceVersion  = "3.2.1"

lazy val root = (project in file("."))
  .settings(
    name := "adnimals",
    version := "0.1.0",
    libraryDependencies ++= Seq(
      "org.apache.pekko" %% "pekko-http" % "1.0.1",
      "org.apache.pekko" %% "pekko-stream" % "1.0.1",
      "io.circe" %% "circe-core" % "0.14.7",
      "io.circe" %% "circe-generic" % "0.14.7",
      "io.circe" %% "circe-parser" % "0.14.7",
      "ch.qos.logback" % "logback-classic" % "1.4.11",
      "com.github.pjfanning" %% "pekko-http-circe" % PekkoCirceVersion,
      "org.scalatest" %% "scalatest" % "3.2.17" % Test
    )
  )
