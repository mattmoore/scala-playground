name := "time"

lazy val DoobieV = "1.0.0-RC1"
lazy val TestContainersV = "0.39.12"
lazy val FlywayV = "8.0.2"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % "3.2.9",
  // Start with this one
  "org.tpolecat" %% "doobie-core" % "1.0.0-RC1",
  // And add any of these as needed
  "org.tpolecat" %% "doobie-h2" % DoobieV, // H2 driver 1.4.200 + type mappings.
  "org.tpolecat" %% "doobie-hikari" % DoobieV, // HikariCP transactor.
  "org.tpolecat" %% "doobie-postgres" % DoobieV, // Postgres driver 42.3.1 + type mappings.
  "org.tpolecat" %% "doobie-specs2" % DoobieV % "test", // Specs2 support for typechecking statements.
  "org.tpolecat" %% "doobie-scalatest" % DoobieV % "test", // ScalaTest support for typechecking statements.
  "org.flywaydb" % "flyway-core" % FlywayV,
  "com.dimafeng" %% "testcontainers-scala-scalatest" % TestContainersV % "test",
  "com.dimafeng" %% "testcontainers-scala-postgresql" % TestContainersV % "test",
  "com.dimafeng" %% "testcontainers-scala-mysql" % TestContainersV % "test"
)
