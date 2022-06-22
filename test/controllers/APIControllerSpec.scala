package controllers

import models.Library
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json._
import play.api.test._
import play.api.test.Helpers._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global
import models.LibraryService._
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class APIControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with BeforeAndAfterEach{

//  "APIController GET" should {
//    "return all books in the library" in {
//      val db = DatabaseConfigProvider.get("default")
//      val db2 = DatabaseConfigProvider.get[JdbcProfile]
//      val controller = new APIController(stubControllerComponents(), new DatabaseConfigProvider { def get[P <: slick.basic.BasicProfile]: slick.basic.DatabaseConfig[P] = DatabaseConfigProvider.get[P] })
//      val books = controller.getBooks().apply(FakeRequest(GET, "/books"))
//
//      status(books) mustBe OK
//      contentType(books) mustBe Some("application/json")
////      Json.parse[Map[Int, Book]](contentAsString(books))
//      contentAsString(books) must include ("A Brief History of Time")
//      contentAsString(books) must include ("Fellowship of the Ring")
//    }
//  }
//
//  "APIController GET books / genre" should {
//    "return all books with a given genre" in {
//      val controller = new APIController(stubControllerComponents(), null)
//      // val fantasyBooks = controller.searchByGenre("Fantasy").apply(FakeRequest(GET, "/books/Fantasy"))
//      // val scienceBooks = controller.searchByGenre("Science").apply(FakeRequest(GET, "/books/Science"))
//
//      // status(fantasyBooks) mustBe OK
//      // contentType(fantasyBooks) mustBe Some("application/json")
//      // contentAsString(fantasyBooks) must not include ("A Brief History of Time")
//      // contentAsString(fantasyBooks) must include ("Fellowship of the Ring")
//
//      // contentAsString(scienceBooks) must include ("A Brief History of Time")
//      // contentAsString(scienceBooks) must not include ("Fellowship of the Ring")
//    }
//
//    "return an error if no books match genre" in {
//      val controller = new APIController(stubControllerComponents(), null)
//      //val errorBooks = controller.searchByGenre("Romance").apply(FakeRequest(GET, "/books/Romance"))
//
//      // status(errorBooks) mustBe OK
//      // contentType(errorBooks) mustBe Some("application/json")
//      // contentAsString(errorBooks) must include ("Empty")
//    }
//
//  }
//
//  "APIController POST add / book" should {
//    "add a new book to the library" in {
//      val controller = new APIController(stubControllerComponents(), null)
//      // create in Scala, then convert to Json, then send as body
//      val jsonString = """{"author": {"firstName": "Tom","lastName": "Waghorn"}, "title": "This is a Test","genre": "Test","year": "1954","type": "Fiction"}"""
//      val json = Json.parse(jsonString)
//      val addBook = controller.addBook().apply(FakeRequest(POST, "/books").
//        withHeaders("Content-Type" -> "application/json").
//        withBody[JsValue](json))
//
//      status(addBook) mustBe OK
//      contentType(addBook) mustBe Some("application/json")
//      contentAsString(addBook) must include ("Success")
//
//      //val testBook = controller.searchByGenre("Test").apply(FakeRequest(GET, "/books/Test"))
//
//      //contentAsString(testBook) must include ("This is a Test")
//    }
//
//    "throw a bad request on bad json" in {
//      val controller = new APIController(stubControllerComponents(), null)
//      // create in Scala, then convert to Json, then send as body
//      val book = Book("test", "this is a test", "test", "1984", "Fiction")
//      val jsonString = """{"title": "This is a Test","genre": "Test","year": "1954","type": "Fiction"}"""
//      val json = Json.toJson[Book](book)
//      val addBook = controller.addBook().apply(FakeRequest(POST, "/books").
//        withHeaders("Content-Type" -> "application/json").
//        withBody[JsValue](json))
//
//      status(addBook) mustBe BAD_REQUEST
//      contentAsString(addBook) must include ("Error")
//    }
//
//  }
//
//  "APIController DELETE remove / book" should {
//    "remove a book given its id" in {
//      val controller = new APIController(stubControllerComponents(), null)
//      val removedBook = controller.removeBook(0).apply(FakeRequest(DELETE, "/books/0"))
//
//      status(removedBook) mustBe OK
//
//      val books = controller.getBooks().apply(FakeRequest(GET, "/books"))
//
//      status(books) mustBe OK
//      contentAsString(books) must not include ("A Brief History of Time")
//      contentAsString(books) must include ("Fellowship of the Ring")
//    }
//
//    "should throw an error if given an incorrect id" in {
//      val controller = new APIController(stubControllerComponents(), null)
//      val removedBook = controller.removeBook(3).apply(FakeRequest(DELETE, "/books/3"))
//
//      status(removedBook) mustBe BAD_REQUEST
//      contentType(removedBook) mustBe Some("application/json")
//      contentAsString(removedBook) must include ("Error")
//
//      val books = controller.getBooks().apply(FakeRequest(GET, "/books"))
//
//      status(books) mustBe OK
//      contentAsString(books) must include ("A Brief History of Time")
//      contentAsString(books) must include ("Fellowship of the Ring")
//    }
//  }
}