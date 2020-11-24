package io.mattmoore.scala.playground.lucene

import com.outr.lucene4s.DirectLucene
import com.outr.lucene4s.query.Sort
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class LuceneSpec extends AnyFunSpec with Matchers {
  describe("indexing a document") {
    val lucene = new DirectLucene(Nil)

    val name = lucene.create.field[String]("name")
    val address = lucene.create.field[String]("address")

    lucene.doc().fields(name("John"), address("123 Somewhere Rd.")).index()
    lucene.doc().fields(name("Jane"), address("123 Somewhere Rd.")).index()

    describe("return all results in the index") {
      val paged = lucene.query().sort(Sort(name)).search()

      it("creates a new index and returns search results") {
        paged.total shouldBe 2
        paged.results.map(_ (name)) shouldBe Vector("Jane", "John")
      }
    }
  }
}
