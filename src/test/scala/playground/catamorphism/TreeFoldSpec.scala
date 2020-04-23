package playground.catamorphism

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class TreeFoldSpec extends AnyFunSpec with Matchers {
  describe("folds used with trees") {
    sealed trait Tree
    case object Leaf extends Tree
    case class Node(value: Int, left: Tree, right: Tree) extends Tree

    describe("sum as a recursive function") {
      def sum(tree: Tree): Int = tree match {
        case Leaf => 0
        case Node(value, left, right) => value + sum(left) + sum(right)
      }

      describe("with a tree containing a value and two leafs") {
        val tree = Node(1, Leaf, Leaf)

        it("should sum all the node values") {
          sum(tree) shouldBe 1
        }
      }

      describe("with a tree containing a value and right and left nodes") {
        val tree = Node(1, Node(1, Leaf, Leaf), Node(1, Leaf, Leaf))

        it("should sum all the node values") {
          sum(tree) shouldBe 3
        }
      }
    }

    describe("sum using a fold allows for abstracting the operation `f` to perform") {
      def fold(zero: Int, f: (Int, Int, Int) => Int)(tree: Tree): Int = tree match {
        case Leaf => zero
        case Node(value, left, right) => f(value, fold(zero, f)(left), fold(zero, f)(right))
      }

      def sum(tree: Tree): Int = fold(0, (a: Int, b: Int, c: Int) => a + b + c)(tree)

      describe("with a tree containing a value and two leafs") {
        val tree = Node(1, Leaf, Leaf)

        it("should sum all the node values") {
          sum(tree) shouldBe 1
        }
      }

      describe("with a tree containing a value and right and left nodes") {
        val tree = Node(1, Node(1, Leaf, Leaf), Node(1, Leaf, Leaf))

        it("should sum all the node values") {
          sum(tree) shouldBe 3
        }
      }
    }
  }
}
