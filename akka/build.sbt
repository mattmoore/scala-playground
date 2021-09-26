import Dependencies._

name := "akka"
libraryDependencies ++= Seq(
  Akka.actor,
  Akka.stream
)
