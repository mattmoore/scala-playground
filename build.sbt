import Dependencies._

ThisBuild / scalaVersion := "2.13.3"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "io.mattmoore"
ThisBuild / organizationName := "mattmoore"
ThisBuild / scapegoatVersion := "1.4.6"

scalacOptions := Seq("-deprecation", "-feature")

lazy val root = (project in file("."))
  .aggregate(lucene)
  .settings(
    name := "scala-playground",
    libraryDependencies in ThisBuild ++= Seq(
      Refined.core,
      Testing.scalaTest
    )
  )

lazy val lucene = project
  .settings(
    name := "lucene",
    libraryDependencies ++= Seq(
      Lucene.lucene4s
    )
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.