package playground.catamorphism

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class TreeFoldSpec extends AnyFunSpec with Matchers {
  describe("folds used with trees") {
    sealed trait Tree
    case object Leaf extends Tree
    case class Node(left: Tree, value: Int, right: Tree) extends Tree

    describe("sum as a recursive function") {
      def sum(tree: Tree): Int = tree match {
        case Leaf => 0
        case Node(left, value, right) => sum(left) + value + sum(right)
      }

      describe("with a tree containing a value and two leafs") {
        val tree = Node(Leaf, 1, Leaf)

        it("should sum all the node values") {
          sum(tree) shouldBe 1
        }
      }

      describe("with a tree containing a value and right and left nodes") {
        val tree = Node(Node(Leaf, 1, Leaf), 1, Node(Leaf, 1, Leaf))

        it("should sum all the node values") {
          sum(tree) shouldBe 3
        }
      }
    }

    describe("sum using a fold allows for abstracting the operation `f` to perform") {
      def fold(start: Int, f: (Int, Int, Int) => Int)(tree: Tree): Int = tree match {
        case Leaf => start
        case Node(left, value, right) => f(value, fold(start, f)(left), fold(start, f)(right))
      }

      def sum(tree: Tree): Int = fold(0, (a: Int, b: Int, c: Int) => a + b + c)(tree)

      describe("with a tree containing a value and two leafs") {
        val tree = Node(Leaf, 1, Leaf)

        it("should sum all the node values") {
          sum(tree) shouldBe 1
        }
      }

      describe("with a tree containing a value and right and left nodes") {
        val tree = Node(Node(Node(Node(Leaf, 1, Leaf ), 1, Node(Leaf, 8, Leaf)), 1, Leaf), 1, Node(Leaf, 1, Leaf))

        it("should sum all the node values") {
          sum(tree) shouldBe 13
        }
      }
    }

    describe("sum using fold with currying and function `f`") {
      def fold(start: Int)(f: (Int, Int, Int) => Int)(tree: Tree): Int = tree match {
        case Leaf => start
        case Node(left, value, right) => f(value, fold(start)(f)(left), fold(start)(f)(right))
      }

      def sum(tree: Tree): Int = fold(0)((a: Int, b: Int, c: Int) => a + b + c)(tree)

      describe("with a tree containing a value and two leafs") {
        val tree = Node(Leaf, 1, Leaf)

        it("should sum all the node values") {
          sum(tree) shouldBe 1
        }
      }

      describe("with a tree containing a value and right and left nodes") {
        val tree = Node(Node(Leaf, 1, Leaf), 1, Node(Leaf, 1, Leaf))

        it("should sum all the node values") {
          sum(tree) shouldBe 3
        }
      }
    }
  }
}
