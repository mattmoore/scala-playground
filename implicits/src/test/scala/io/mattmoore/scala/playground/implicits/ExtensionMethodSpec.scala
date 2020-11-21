package io.mattmoore.scala.playground.implicits

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class ExtensionMethodSpec extends AnyFunSpec with Matchers {
  describe("add a method to an existing class without modifying the class source") {
    case class Person(val firstName: String, val lastName: String)

    implicit class ExtendedPerson(p: Person) {
      def greet = s"Hello, ${p.firstName} ${p.lastName}!"
    }

    it("now has a greet method") {
      Person("Matt", "Moore").greet shouldBe "Hello, Matt Moore!"
    }
  }

  describe("extension methods via implicits") {
    implicit class RichInt(i: Int) {
      def squared: Int = i * i
    }

    it("makes int have a square method") {
      2.squared shouldBe 4
    }
  }
}
