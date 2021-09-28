import Dependencies._

name := "property-based-testing"
libraryDependencies ++= Seq(
  Testing.scalacheck,
  Testing.scalacheckShapeless
)