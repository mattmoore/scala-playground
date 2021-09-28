import Dependencies._

name := "fs2"
libraryDependencies ++= Seq(
  FS2.core,
  FS2.io,
  FS2.reactiveStreams
)