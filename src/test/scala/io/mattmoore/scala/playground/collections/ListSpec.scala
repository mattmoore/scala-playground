package io.mattmoore.scala.playground.collections

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable.ListBuffer

class ListSpec extends AnyFunSpec with Matchers {
  describe("prepending to a list") {
    val list = List[String]()

    it("should add to the beginning of the list") {
      ("item 1" :: list) shouldBe List[String]("item 1")
    }
  }

  describe("appending to a list, you'll want to use a mutable ListBuffer") {
    describe("to add to the ListBuffer") {
      var list = ListBuffer[String]()
      list += "item 1"
      list += "item 2"

      it("should add the new item to the end of the list, but is still a ListBuffer") {
        list shouldBe List[String]("item 1", "item 2")
        list shouldBe a[ListBuffer[String]]
      }
    }

    describe("you can convert a ListBuffer to an immutable List") {
      val list = ListBuffer[String]("item 1", "item 2").toList

      it("can be converted to an immutable List") {
        list shouldBe List[String]("item 1", "item 2")
        list shouldBe a[List[String]]
      }
    }
  }
}
