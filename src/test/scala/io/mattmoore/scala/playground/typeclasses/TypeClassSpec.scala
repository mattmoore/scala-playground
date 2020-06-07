package io.mattmoore.scala.playground.typeclasses

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

/**
 * Type Classes are like interfaces, but data and methods are separate.
 */
class TypeClassSpec extends AnyFunSpec with Matchers {
  describe("animal example") {
    describe("without type classes, how you might do it OOP style") {
      abstract class Animal {
        def hello(): String
      }

      class Dog extends Animal {
        override def hello() = "bark"
      }

      class Bird extends Animal {
        override def hello() = "chirp"
      }

      it("dogs bark") {
        (new Dog).hello shouldBe "bark"
      }

      it("birds chirp") {
        (new Bird).hello shouldBe "chirp"
      }
    }

    describe("without type classes, how you might do this with trait (like a Java interface, more FP-like)") {
      trait Speech {
        def hello(): String
      }

      final class Dog extends Speech {
        def hello = "bark"
      }

      final class Bird extends Speech {
        def hello = "chirp"
      }

      it("barks like a dog") {
        (new Dog).hello shouldBe "bark"
      }

      it("chirps like a bird") {
        (new Bird).hello shouldBe "chirp"
      }
    }

    /**
     * Note that the previous examples require us to implement the hello function in the class.
     * What if Dog and Bird are outside our control, perhaps from another library?
     * In that case, we can't extend them with the hello method.
     * The examples below demonstrate extending Dog and Bird via type classes.
     */

    describe("with type classes via trait and companion") {
      final class Dog(val name: String)
      final class Bird(val name: String)

      trait SpeechBehavior[A] {
        def hello(a: A): String
      }

      object SpeechBehavior {
        val speakingDog: SpeechBehavior[Dog] =
          new SpeechBehavior[Dog] {
            def hello(a: Dog): String = s"${a.name}: bark"
          }
        val speakingBird: SpeechBehavior[Bird] =
          new SpeechBehavior[Bird] {
            override def hello(a: Bird): String = s"${a.name}: chirp"
          }
      }

      import SpeechBehavior.{speakingBird, speakingDog}

      it("implements the hello method on classes outside our control") {
        speakingDog.hello(new Dog("Fido")) shouldBe "Fido: bark"
        speakingBird.hello(new Bird("Sparkles")) shouldBe "Sparkles: chirp"
      }
    }

    /**
     * OK, so we've got a type class implemented in the previous example, but it requires we write out code like this:
     *
     * <code>speakingDog.hello(dog)</code>
     *
     * What if we want to treat our Dog and Bird classes as though they implement hello?
     * This way we can write the following:
     *
     * <code>dog.hello</code>
     */
    describe("with type classes via implicits") {
      final class Dog(val name: String)
      final class Bird(val name: String)

      implicit class DogSpeech(dog: Dog) {
        def hello: String = s"${dog.name}: bark"
        def stayAway: String = s"${dog.name}: bark bark bark!"
      }

      implicit class BirdSpeech(bird: Bird) {
        def hello: String = s"${bird.name}: chirp"
        def stayAway: String = s"${bird.name}: chirp chirp chirp!"
      }

      val dog = new Dog("Spot")
      val bird = new Bird("Sparkles")

      it("dog says hello") {
        dog.hello shouldBe "Spot: bark"
      }

      it("dog says stay away") {
        dog.stayAway shouldBe "Spot: bark bark bark!"
      }

      it("bird says hello") {
        bird.hello shouldBe "Sparkles: chirp"
      }

      it("bird says stay away") {
        bird.stayAway shouldBe "Sparkles: chirp chirp chirp!"
      }
    }
  }
}
