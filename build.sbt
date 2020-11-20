import Dependencies._

ThisBuild / scalaVersion := "2.13.3"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "io.mattmoore"
ThisBuild / organizationName := "mattmoore"
ThisBuild / scapegoatVersion := "1.4.6"

scalacOptions := Seq("-deprecation", "-feature")

lazy val root = (project in file("."))
  .aggregate(
    akka,
    collections,
    lucene,
    refinementTypes
  )
  .settings(
    name := "scala-playground",
    libraryDependencies in ThisBuild ++= Seq(
      Testing.scalaTest
    )
  )

lazy val akka = project
  .settings(
    name := "akka",
    libraryDependencies ++= Seq(
      Akka.actor,
      Akka.stream
    )
  )

lazy val collections = project
  .settings(
    name := "collections",
    libraryDependencies ++= Seq(
    )
  )

lazy val lucene = project
  .settings(
    name := "lucene",
    libraryDependencies ++= Seq(
      Lucene.lucene4s
    )
  )

lazy val refinementTypes = project
  .settings(
    name := "refinement-types",
    libraryDependencies ++= Seq(
      Refined.core
    )
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.