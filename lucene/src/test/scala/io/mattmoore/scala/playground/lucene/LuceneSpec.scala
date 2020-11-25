package io.mattmoore.scala.playground.lucene

import com.outr.lucene4s.keyword.KeywordIndexing
import com.outr.lucene4s.query.Sort
import com.outr.lucene4s.{DirectLucene, wildcard}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class LuceneSpec extends AnyFunSpec with Matchers {
  describe("full text search") {
    val lucene = new DirectLucene(Nil, defaultFullTextSearchable = true)

    val name = lucene.create.field[String]("name")
    val address = lucene.create.field[String]("address")

    lucene.doc().fields(name("John Doe"), address("123 Somewhere Rd.")).index()
    lucene.doc().fields(name("Jane Doe"), address("123 Somewhere Rd.")).index()

    describe("return all results in the index") {
      val paged = lucene.query().sort(Sort(name)).search()

      it("creates a new index and returns search results") {
        paged.total shouldBe 2
        paged.results.map(_ (name)) shouldBe Vector("Jane Doe", "John Doe")
      }
    }

    describe("search the index with a query") {
      val paged = lucene.query().sort(Sort(name)).filter(wildcard(name("doe*"))).search()

      it("returns matching result") {
        paged.total shouldBe 2
        paged.results.map(_ (name)) shouldBe Vector("Jane Doe", "John Doe")
      }
    }

    describe("search the index with a query that matches jane") {
      val paged = lucene.query().sort(Sort(name)).filter(wildcard(name("jane*"))).search()

      it("returns matching result") {
        paged.total shouldBe 1
        paged.results.map(_ (name)) shouldBe Vector("Jane Doe")
      }
    }

    describe("search the index with a query that matches john") {
      val paged = lucene.query().sort(Sort(name)).filter(wildcard(name("john*"))).search()

      it("returns matching result") {
        paged.total shouldBe 1
        paged.results.map(_ (name)) shouldBe Vector("John Doe")
      }
    }
  }

  describe("keyword search") {
    val lucene = new DirectLucene(uniqueFields = List("name"), defaultFullTextSearchable = true, autoCommit = true)
    val keywordIndexing = KeywordIndexing(lucene, "keywords")

    val name = lucene.create.field[String]("name")
    val address = lucene.create.field[String]("address")

    lucene.doc().fields(name("John Doe"), address("123 Somewhere Rd.")).index()
    lucene.doc().fields(name("Jane Doe"), address("123 Somewhere Rd.")).index()

    describe("search the index with a query that matches john") {
      val keywords = keywordIndexing.search("do*")

      it("returns matching result") {
        keywords.total.value shouldBe 1
        keywords.results.map(_.word) shouldBe List("Doe")
        keywords.results.head.score shouldBe 1.0
        keywords.results.length shouldBe 1
      }
    }
  }
}
