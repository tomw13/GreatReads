package models


import models.DBs.LibraryDB
import models.LibraryService._
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.Application

import javax.inject._
import play.api.db.slick.DatabaseConfigProvider
import play.api.test.Injecting
import play.db.NamedDatabase
import play.test.WithApplication
import slick.jdbc.JdbcProfile

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class LibrarySpec @Inject()(currentApplication: Application, @NamedDatabase("library") protected val dbConfigProvider: DatabaseConfigProvider)
  extends PlaySpec with GuiceOneAppPerTest with Injecting{

  trait WithDatabaseConfig {
    lazy val (profile, db) = {
      val dbConfig = dbConfigProvider.get[JdbcProfile]
      (dbConfig.config, dbConfig.db)
    }
  }

  "Library model" should {
    def libraryDao(implicit app: Application) = {
      val LibraryDAO = Application.instanceCache[LibraryDB]
      LibraryDAO(app)
    }

    "be inserted into db correctly" in new WithApplication with WithDatabaseConfig {

      val bookInsertResult = Await.result(libraryDao(currentApplication).addBook(
        Book("author", "title", "genre", "year", "type")),
        Duration.Inf
      )
      val books = Await.result(libraryDao(currentApplication).books, Duration.Inf)

      books.size mustEqual  1
    }
  }

}
