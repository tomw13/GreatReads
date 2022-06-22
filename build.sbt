name := """tom"""
organization := "tom"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.13"

libraryDependencies ++= Seq(
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,
  "com.typesafe.slick" %% "slick" % "3.3.3",
  "com.typesafe.play" %% "play-slick" % "5.0.0",
  "com.typesafe.slick" %% "slick-codegen" % "3.3.2",
  "org.postgresql" % "postgresql" % "42.2.11",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",
  "org.mindrot" % "jbcrypt" % "0.4",
  "org.scalamock" %% "scalamock" % "5.1.0" % Test
)

Test / fork := true

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "tom.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "tom.binders._"
