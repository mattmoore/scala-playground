package playground.catamorphism

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class CatamorphismSpec extends AnyFunSpec with Matchers {
  describe("fold") {
    describe("combines list of numbers via an operation") {
      def sum =
        List(1, 2, 3).fold(0)((a, b) => a + b)

      def sumShorthand =
        List(1, 2, 3).fold(0)(_ + _)

      it("should sum the numbers") {
        sum shouldBe 6
        sumShorthand shouldBe 6
      }
    }

    describe("fold used with classes") {
      case class Person(val firstName: String, val lastName: String, val email: String)

      val people = List(
        Person("Matt", "Moore", "matt@mattmoore.io"),
        Person("Jane", "Doe", "jane@janedoe.io")
      )

      def transformEmails =
        people.foldLeft(List[String]())((a, b) => a :+ b.email)

      def transformEmailsShorthand =
        people.foldLeft(List[String]())(_ :+ _.email)

      it("should transform the people list into an email list") {
        val expected = List("matt@mattmoore.io", "jane@janedoe.io")
        transformEmails shouldBe expected
        transformEmailsShorthand shouldBe expected
      }
    }

    describe("folds used with trees") {
      sealed trait Tree[+A]
      case object EmptyTree extends Tree[Nothing]
      case class Node[A](value: A, left: Tree[A], right: Tree[A]) extends Tree[A]

      object Tree {
        def empty[A]: Tree[A] = EmptyTree
        def node[A](value: A, left: Tree[A] = empty, right: Tree[A] = empty): Tree[A] = Node(value, left, right)
      }

      val t1 = Tree.node(4,
        Tree.node(2,
          Tree.node(1),
          Tree.node(3)
        ),
        Tree.node(7,
          Tree.node(6),
          Tree.node(9)
        )
      )

      def fold[A, B](t: Tree[A], z: B)(f: (B, A, B) => B): B = t match {
        case EmptyTree => z
        case Node(x, l, r) => f(fold(l, z)(f), x, fold(r, z)(f))
      }

      def size[T](tree: Tree[T]) =
        fold(tree, 0: Int) { (l, x, r) => l + r + 1 }

//      def sum[T](tree: Tree[T]) =
//        fold(tree, 0: Int) { (l, x, r) => l + r + 1 }

      describe("size") {
        it("should count the number of nodes") {
          size(t1) shouldBe 7
        }
      }

//      describe("sum") {
//        it("should sum all the node values") {
//          sum(t1) shouldBe 7
//        }
//      }
    }
  }
}
