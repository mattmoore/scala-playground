package io.mattmoore.scala.playground.oopVsFp

final class Person(val name: String)

object PersonOps {
  implicit class PersonOps(person: Person) {
    def greet = s"Hello, ${person.name}!"
  }
}
