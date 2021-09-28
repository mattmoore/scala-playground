package io.mattmoore.scala.playground.pbt

import org.scalacheck.Prop.forAll
import org.scalacheck.Properties

object StringSpec extends Properties("String") {
  property("startsWith") = forAll { (a: String, b: String) =>
    (a + b).startsWith(a)
  }

  // This would fail for empty strings, as the total length of them combined is still 0.
  // property("concatenate") = forAll { (a: String, b: String) =>
  //   (a + b).length > a.length && (a + b).length > b.length
  // }

  property("substring") = forAll { (a: String, b: String, c: String) =>
    (a + b + c).substring(a.length, a.length + b.length) == b
  }
}
