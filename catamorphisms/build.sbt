name := "catamorphisms"

lazy val fs2Version = "3.2.2"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.6.1",
  "co.fs2" %% "fs2-core" % fs2Version,
  "co.fs2" %% "fs2-io" % fs2Version,
  "co.fs2" %% "fs2-reactive-streams" % fs2Version
)
