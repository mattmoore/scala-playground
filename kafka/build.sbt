name := "kafka"
resolvers += "confluent" at "https://packages.confluent.io/maven/"
libraryDependencies ++= Seq(
  "com.github.fd4s" %% "fs2-kafka" % "2.2.0"
)
