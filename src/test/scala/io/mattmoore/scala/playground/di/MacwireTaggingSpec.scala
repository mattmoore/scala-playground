package io.mattmoore.scala.playground.di

import com.softwaremill.macwire.wire
import com.softwaremill.tagging._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class MacwireTaggingSpec extends AnyFunSpec with Matchers {
  describe("without macwire") {
    case class Milk()

    case class CoffeeBean() {
      def description = "not implemented"
    }

    trait DefaultBean extends CoffeeBean {
      override def description = "default coffee bean"
    }

    trait MonkeyBean extends CoffeeBean {
      override def description = "monkey bean"
    }

    case class Coffee(milk: Milk, defaultBean: CoffeeBean, monkeyBean: CoffeeBean)

    val milk = new Milk
    val defaultBean = new CoffeeBean with DefaultBean
    val monkeyBean = new CoffeeBean with MonkeyBean
    val coffee = new Coffee(milk, defaultBean, monkeyBean)

    it("should initialize correctly") {
      coffee should not be (null)
    }
  }

  describe("with macwire") {
    case class Milk()

//    trait DefaultBean {
//      def description = "default coffee bean"
//    }

    trait MonkeyBean {
      def description = "monkey bean"
    }

    case class CoffeeBean() {
      def description = "not implemented"
    }

    case class Coffee(milk: Milk, bean: CoffeeBean)

    val milk = wire[Milk]
    val bean = wire[CoffeeBean].taggedWith[MonkeyBean]
    val coffee = wire[Coffee]

    it("should inject things") {
      coffee should not be (null)
      coffee.bean.description shouldBe "monkey bean"
    }
  }
}
