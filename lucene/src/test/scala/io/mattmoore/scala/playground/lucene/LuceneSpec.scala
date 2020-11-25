package io.mattmoore.scala.playground.lucene

import com.outr.lucene4s.field.Field
import com.outr.lucene4s.keyword.KeywordIndexing
import com.outr.lucene4s.mapper.Searchable
import com.outr.lucene4s.query.{SearchTerm, Sort}
import com.outr.lucene4s.{DirectLucene, Stringify, exact, wildcard}
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

    describe("search the index with a query that matches doe") {
      val keywords = keywordIndexing.search("do*")

      it("returns matching result") {
        keywords.total.value shouldBe 1
        keywords.results.map(_.word) shouldBe List("Doe")
        keywords.results.head.score shouldBe 1.0
        keywords.results.length shouldBe 1
      }
    }
  }

  describe("searchable classes") {
    case class Person(id: Id[Person], name: String, address: String)

    case class Id[T](value: String) {
      override def toString: String = value
    }

    trait SearchablePerson extends Searchable[Person] {
      override def idSearchTerms(person: Person): List[SearchTerm] = List(exact(id(person.id)))

      implicit def stringifyId[T]: Stringify[Id[T]] = Stringify[Id[T]]((s: String) => Id[T](s))

      val id: Field[Id[Person]] = lucene.create.stringifiedField[Id[Person]]("id", fullTextSearchable = false)
    }

    val lucene = new DirectLucene(uniqueFields = List("name"), defaultFullTextSearchable = true, autoCommit = true)
    val people = lucene.create.searchable[SearchablePerson]

    people.insert(Person(Id("F1D8E1F9-8E5E-452A-A3CC-FF71C7BF1CFE"), "John Doe", "123 Somewhere Rd.")).index()
    people.insert(Person(Id("AFD0333E-E697-4ACC-8A08-A79C01250566"), "Jane Doe", "123 Somewhere Rd.")).index()

    describe("search the index with a query that matches Jane") {
      val paged = people.query().filter("ja*").search()

      it("returns matching result") {
        paged.total shouldBe 1
        paged.entries shouldBe Vector(Person(
          Id("AFD0333E-E697-4ACC-8A08-A79C01250566"),
          "Jane Doe",
          "123 Somewhere Rd."
        ))
        paged.results.length shouldBe 1
      }
    }
  }
}
