package io.mattmoore.scala.playground.lucene

import java.util.UUID

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
      val paged = lucene.query().search()

      it("creates a new index and returns search results") {
        paged.total shouldBe 2
        paged.results.map(_ (name)) contains "Jane Doe"
        paged.results.map(_ (name)) contains "John Doe"
      }
    }

    describe("return all results in the index sorted by name") {
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
    case class Person(id: UUID, name: String, address: String, aliases: List[String])

    trait SearchablePerson extends Searchable[Person] {
      override def idSearchTerms(person: Person): List[SearchTerm] = List(exact(id(person.id)))

      implicit def stringify[T <: String]: Stringify[T] = Stringify[T]((s: String) => s.asInstanceOf[T])

      implicit def stringifyUUID[T <: UUID]: Stringify[T] = Stringify[T]((s: String) => UUID.fromString(s).asInstanceOf[T])

      val id: Field[UUID] = lucene.create.stringifiedField[UUID]("id", fullTextSearchable = false)

      val name: Field[String] = lucene.create.stringifiedField[String]("name", fullTextSearchable = true)
    }

    val lucene = new DirectLucene(uniqueFields = List("id"), defaultFullTextSearchable = true, autoCommit = true)
    val people = lucene.create.searchable[SearchablePerson]

    val john = Person(
      UUID.fromString("F1D8E1F9-8E5E-452A-A3CC-FF71C7BF1CFE"),
      "John Doe",
      "123 Somewhere Rd.",
      List("johnny")
    )

    val jane = Person(
      UUID.fromString("AFD0333E-E697-4ACC-8A08-A79C01250566"),
      "Jane Doe",
      "123 Somewhere Rd.",
      List("janey")
    )

    people.insert(john).index()
    people.insert(jane).index()

    describe("search the index with a query that matches Jane") {
      val paged = people.query().filter("ja*").search()

      it("returns matching result") {
        paged.total shouldBe 1
        paged.entries shouldBe Vector(Person(
          UUID.fromString("AFD0333E-E697-4ACC-8A08-A79C01250566"),
          "Jane Doe",
          "123 Somewhere Rd.",
          List("janey")
        ))
        paged.results.length shouldBe 1
      }
    }
  }
}
