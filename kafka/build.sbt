name := "kafka"
resolvers += "confluent" at "https://packages.confluent.io/maven/"

val log4catsV = "2.1.1"
val testContainersV = "0.39.8"

libraryDependencies ++= Seq(
  "com.github.fd4s" %% "fs2-kafka" % "2.2.0",
  "com.dimafeng" %% "testcontainers-scala" % testContainersV,
  "com.dimafeng" %% "testcontainers-scala-kafka" % testContainersV,
  "org.typelevel" %% "cats-effect-testing-scalatest" % "1.3.0" % "test,it",
  "org.typelevel" %% "log4cats-slf4j" % log4catsV,
  "org.typelevel" %% "log4cats-testing" % log4catsV % "test,it",
  "org.slf4j" % "slf4j-simple" % "1.7.32"
)
