import sbt._

object Dependencies {

  object Akka {
    lazy val stream = "com.typesafe.akka" %% "akka-stream" % "2.6.6"
    lazy val actor = "com.typesafe.akka" %% "akka-actor" % "2.6.6"
    lazy val actorTestkitTyped = "com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.6.6"
    lazy val protobufV3 = "com.typesafe.akka" %% "akka-protobuf-v3" % "2.6.6"
  }

  object Cats {
    lazy val core = "org.typelevel" %% "cats-core" % "2.6.1"
    lazy val effect = "org.typelevel" %% "cats-effect" % "3.2.9"
  }

  object Lucene {
    lazy val lucene4s = "com.outr" %% "lucene4s" % "1.10.0"
  }

  object Refined {
    lazy val refinedVersion = "0.9.27"
    lazy val core = "eu.timepit" %% "refined" % refinedVersion
    lazy val cats = "eu.timepit" %% "refined-cats" % refinedVersion
    lazy val eval = "eu.timepit" %% "refined-eval" % refinedVersion
    lazy val jsonPath = "eu.timepit" %% "refined-jsonpath" % refinedVersion
    lazy val pureConfig = "eu.timepit" %% "refined-pureconfig" % refinedVersion
    lazy val scalaCheck = "eu.timepit" %% "refined-scalacheck" % refinedVersion
    lazy val scalaz = "eu.timepit" %% "refined-scalaz" % refinedVersion
    lazy val scodec = "eu.timepit" %% "refined-scodec" % refinedVersion
    lazy val scopt = "eu.timepit" %% "refined-scopt" % refinedVersion
    lazy val shapeless = "eu.timepit" %% "refined-shapeless" % refinedVersion
  }

  object Testing {
    lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.1.1" % Test
    lazy val scalacheck = "org.scalacheck" %% "scalacheck" % "1.14.1" % Test
  }

  object Zio {
    lazy val zio = "dev.zio" %% "zio" % "1.0.0-RC18-2"
    lazy val streams = "dev.zio" %% "zio-streams" % "1.0.0-RC18-2"
  }

}
