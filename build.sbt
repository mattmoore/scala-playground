import Dependencies._

ThisBuild / scalaVersion     := "2.13.1"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "io.mattmoore"
ThisBuild / organizationName := "mattmoore"
ThisBuild / scapegoatVersion := "1.4.3"

lazy val root = (project in file("."))
  .settings(
    name := "Scala Playground",
    libraryDependencies += scalaTest % Test
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.

// Cats
libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.0.0",
  "org.typelevel" %% "cats-effect" % "2.0.0"
)

// ZIO
libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % "1.0.0-RC18-2",
  "dev.zio" %% "zio-streams" % "1.0.0-RC18-2"
)

// Refined
libraryDependencies ++= Seq(
  "eu.timepit" %% "refined"                 % "0.9.13",
  "eu.timepit" %% "refined-cats"            % "0.9.13", // optional
  "eu.timepit" %% "refined-eval"            % "0.9.13", // optional, JVM-only
  "eu.timepit" %% "refined-jsonpath"        % "0.9.13", // optional, JVM-only
  "eu.timepit" %% "refined-pureconfig"      % "0.9.13", // optional, JVM-only
  "eu.timepit" %% "refined-scalacheck"      % "0.9.13", // optional
  "eu.timepit" %% "refined-scalaz"          % "0.9.13", // optional
  "eu.timepit" %% "refined-scodec"          % "0.9.13", // optional
  "eu.timepit" %% "refined-scopt"           % "0.9.13", // optional
  "eu.timepit" %% "refined-shapeless"       % "0.9.13"  // optional
)

// Akka
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % "2.6.3"
)