import Dependencies._

ThisBuild / scalaVersion := "2.13.6"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "io.mattmoore"
ThisBuild / organizationName := "mattmoore"

scalacOptions := Seq("-deprecation", "-feature")

lazy val root = (project in file("."))
  .aggregate(
    akka,
    cats,
    catamorphisms,
    collections,
    implicits,
    lucene,
    oopVsFp,
    patternMatching,
    refinementTypes,
    typeClasses
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

lazy val cats = project
  .settings(
    name := "cats"
  )

lazy val catamorphisms = project
  .settings(
    name := "catamorphisms"
  )

lazy val collections = project
  .settings(
    name := "collections"
  )

lazy val implicits = project
  .settings(
    name := "implicits"
  )

lazy val lucene = project
  .settings(
    name := "lucene",
    libraryDependencies ++= Seq(
      Lucene.lucene4s
    )
  )

lazy val oopVsFp = project
  .settings(
    name := "oop-vs-fp"
  )

lazy val patternMatching = project
  .settings(
    name := "pattern-matching"
  )

lazy val refinementTypes = project
  .settings(
    name := "refinement-types",
    libraryDependencies ++= Seq(
      Refined.core
    )
  )

lazy val typeClasses = project
  .settings(
    name := "typeclasses"
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.