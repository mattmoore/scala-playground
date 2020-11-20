import Dependencies._

ThisBuild / scalaVersion := "2.13.3"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "io.mattmoore"
ThisBuild / organizationName := "mattmoore"
ThisBuild / scapegoatVersion := "1.4.6"

lazy val root = (project in file("."))
  .settings(
    name := "scala-playground",
    libraryDependencies ++= Seq(
      Refined.core,
      scalaTest
    )
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.