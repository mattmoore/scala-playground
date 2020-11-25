import sbt._

object Dependencies {

  object Akka {
    lazy val stream = "com.typesafe.akka" %% "akka-stream" % "2.6.6"
    lazy val actor = "com.typesafe.akka" %% "akka-actor" % "2.6.6"
    lazy val actorTestkitTyped = "com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.6.6"
    lazy val protobufV3 = "com.typesafe.akka" %% "akka-protobuf-v3" % "2.6.6"
  }

  object Cats {
    lazy val core = "org.typelevel" %% "cats-core" % "2.0.0"
    lazy val effect = "org.typelevel" %% "cats-effect" % "2.0.0"
  }

  object Lucene {
    lazy val lucene4s = "com.outr" %% "lucene4s" % "1.10.0"
  }

  object Refined {
    lazy val core = "eu.timepit" %% "refined" % "0.9.13"
    lazy val cats = "eu.timepit" %% "refined-cats" % "0.9.13"
    lazy val eval = "eu.timepit" %% "refined-eval" % "0.9.13"
    lazy val jsonPath = "eu.timepit" %% "refined-jsonpath" % "0.9.13"
    lazy val pureConfig = "eu.timepit" %% "refined-pureconfig" % "0.9.13"
    lazy val scalaCheck = "eu.timepit" %% "refined-scalacheck" % "0.9.13"
    lazy val scalaz = "eu.timepit" %% "refined-scalaz" % "0.9.13"
    lazy val scodec = "eu.timepit" %% "refined-scodec" % "0.9.13"
    lazy val scopt = "eu.timepit" %% "refined-scopt" % "0.9.13"
    lazy val shapeless = "eu.timepit" %% "refined-shapeless" % "0.9.13"
  }

  object Testing {
    lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.1.1" % Test
  }

  object Zio {
    lazy val zio = "dev.zio" %% "zio" % "1.0.0-RC18-2"
    lazy val streams = "dev.zio" %% "zio-streams" % "1.0.0-RC18-2"
  }

}
