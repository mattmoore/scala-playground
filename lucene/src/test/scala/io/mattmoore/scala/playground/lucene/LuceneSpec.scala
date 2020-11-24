package io.mattmoore.scala.playground.lucene

import java.nio.file.Paths

import com.outr.lucene4s.DirectLucene
import com.outr.lucene4s.query.Sort
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class LuceneSpec extends AnyFunSpec with Matchers {
  describe("indexing a document") {
    val directory = Paths.get("index")
    val lucene = new DirectLucene(Nil)

    val name = lucene.create.field[String]("name")
    val address = lucene.create.field[String]("address")

    lucene.doc().fields(name("John Doe"), address("123 Somewhere Rd.")).index()
    lucene.doc().fields(name("Jane Doe"), address("123 Somewhere Rd.")).index()

    describe("searching the index") {
      val paged = lucene.query().sort(Sort(name)).search()
      paged.results.foreach { searchResult =>
        println(s"Name: ${searchResult(name)}, Address: ${searchResult(address)}")
      }

      it("creates a new index and returns search results") {
        paged.results.map(_ (name)) shouldBe Vector("Jane Doe", "John Doe")
      }
    }
  }
}
