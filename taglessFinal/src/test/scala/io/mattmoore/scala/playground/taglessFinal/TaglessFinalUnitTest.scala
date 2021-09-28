package io.mattmoore.scala.playground.taglessFinal

import io.mattmoore.scala.playground.taglessFinal.TaglessFinal.{Product, Program, ProgramWithDep}
import org.scalatest.flatspec.AnyFlatSpec

class TaglessFinalUnitTest extends AnyFlatSpec {

  "Program.createAndAddToCart" should "create a cart and add a new product using the implicit object resolution" in {
    import io.mattmoore.scala.playground.taglessFinal.TaglessFinal.ShoppingCarts._
    val iPhone = Product("iPhone13", "The new iPhone 13")

    val cartState = Program.createAndToCart[ScRepoState](iPhone, "cart1")
    val (_, cart) = cartState.run(Map()).value

    assert {
      cart.get.products == List(iPhone)
    }
  }

  "Program.createAndAddToCart" should "create a cart and add a new product using the smart constructors" in {
    import io.mattmoore.scala.playground.taglessFinal.TaglessFinal.ShoppingCarts._
    val iPhone = Product("iPhone13", "The new iPhone 13")
    val program = ProgramWithDep(ShoppingCartWithDependencyInterpreter.make())

    val cartState = program.createAndToCart(iPhone, "cart1")
    val (_, cart) = cartState.run(Map()).value

    assert {
      cart.get.products == List(iPhone)
    }
  }
}
