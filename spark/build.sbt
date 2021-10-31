name := "spark"

lazy val log4catsV = "2.1.1"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.6.1",
  "org.typelevel" %% "cats-effect" % "3.2.9",
  "org.typelevel" %% "cats-effect-testing-scalatest" % "1.3.0" % Test,
  "org.typelevel" %% "log4cats-slf4j" % log4catsV,
  "org.typelevel" %% "log4cats-testing" % log4catsV % "test,it",
  "org.slf4j" % "slf4j-simple" % "1.7.32",
  "org.apache.spark" %% "spark-core" % "3.2.0"
)
