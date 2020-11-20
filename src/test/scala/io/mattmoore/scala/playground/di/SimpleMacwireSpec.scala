package io.mattmoore.scala.playground.di

import com.softwaremill.macwire.wire
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class SimpleMacwireSpec extends AnyFunSpec with Matchers {
  case class Milk()
  case class CoffeeBean() {
    def beanType: String = "monkey poop"
  }
  case class Coffee(milk: Milk, coffeeBean: CoffeeBean)

  describe("without macwire") {
    val milk = new Milk
    val coffeeBean = new CoffeeBean
    val coffee = new Coffee(milk, coffeeBean)
    coffee should not be (null)
    coffee.coffeeBean.beanType shouldBe "monkey poop"
  }

  describe("with macwire") {
    val milk = wire[Milk]
    val coffeeBean = wire[CoffeeBean]
    val coffee = wire[Coffee]
    coffee should not be (null)
    coffee.coffeeBean.beanType shouldBe "monkey poop"
  }
}
