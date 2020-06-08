package io.mattmoore.scala.playground.oopVsFp

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class OopClassVsScalaClass extends AnyFunSpec with Matchers {
  describe("an OOP-style class") {
    case class Person(var name: String) {
      def greet = s"Hello, $name!"
    }

    describe("greet") {
      val person = new Person("Matt")

      it("should greet the person") {
        person.greet shouldBe "Hello, Matt!"
      }
    }
  }

  describe("an FP-style class") {
    case class Person(val name: String)

    def greet(person: Person) = s"Hello, ${person.name}!"

    val person = new Person("Matt")

    it("should greet the person, FP-style") {
      greet(person) shouldBe "Hello, Matt!"
    }
  }

  describe("an FP-style class, with implicits") {
    case class Person(val name: String)

    implicit class PersonGreeter(person: Person) {
      def greet = s"Hello, ${person.name}!"
    }

    val person = new Person("Matt")

    it("should greet the person, FP-style") {
      person.greet shouldBe "Hello, Matt!"
    }
  }

  describe("testing a class from outside of the test") {
    import io.mattmoore.scala.playground.Person
    import io.mattmoore.scala.playground.PersonOps.PersonOps

    val person = new Person("Matt")

    it("greets") {
      person.greet shouldBe "Hello, Matt!"
    }
  }
}
