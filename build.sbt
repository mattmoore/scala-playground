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
    taglessFinal,
    typeclasses
  )
  .settings(
    name := "scala-playground",
    libraryDependencies in ThisBuild ++= Seq(
      Testing.scalaTest
    )
  )

lazy val akka = project
lazy val cats = project
lazy val catamorphisms = project
lazy val collections = project
lazy val implicits = project
lazy val lucene = project
lazy val oopVsFp = project
lazy val patternMatching = project
lazy val refinementTypes = project
lazy val taglessFinal = project
lazy val typeclasses = project
