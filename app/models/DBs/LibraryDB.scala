package models.DBs

import models.LibraryService
import models.Tables.{Books, BooksRow}
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

class LibraryDB(db: Database)(implicit ec: ExecutionContext) {
  import LibraryService._

  // def reset: Future[Boolean] = ???

  // Returns all the books as a sequence of their id and their Book class instance
  def books: Future[Seq[(Int, Book)]] = {
    db.run(Books.result).
      map(result => result.
        map(row => row.id -> Book(row.author, row.title, row.genre, row.year, row.`type`)))

  }

  // checks to see if the book is already in the library, if not it adds it to the table
  def addBook(book: Book): Future[Boolean] = {
    // compares new book to all others based on title, year and author

    val data = Query((book.author, book.title, book.genre, book.year, book.bookType))
    val exists = Books.filter(b => b.author === book.author && b.title === book.title).exists
    val selectExpression = data.filterNot(_ => exists)

    db.run(Books.map(b => (b.author, b.title, b.genre, b.year, b.`type`)).
      forceInsertQuery(selectExpression)).map(result => result > 0)

  }

  def getBook(index: Int): Future[(Int, Book)] = db.run(Books.filter(_.id === index).result).
    map(result => result.map(row => row.id -> Book(row.author, row.title, row.genre, row.year, row.`type`)).head)

  // removes a book from the table given it's id
  def removeBook(id: Int): Future[Boolean] = db.run(Books.filter(_.id === id).delete).map(_ > 0)

  // given a parameter and query, returns books matching the given criteria
  def find(param: String, query: String): Future[Seq[(Int, Book)]] = param match {
    case "author" => findByAuthor(query)
    case "genre"  => findByGenre(query)
    case "title"  => findByTitle(query)
    case _        => throw new Exception("unexpected argument")
  }

  // returns all books by a given author
  def findByAuthor(author: String): Future[Seq[(Int, Book)]] =
    db.run(Books.filter(_.author.toLowerCase === author.toLowerCase).result).
      map(result => result.
        map(row => row.id -> Book(row.author, row.title, row.genre, row.year, row.`type`)))

  // returns all books of a given genre
  def findByGenre(genre: String): Future[Seq[(Int, Book)]] =
    db.run(Books.filter(_.genre.toLowerCase === genre.toLowerCase).result).
      map(result => result.
        map(row => row.id -> Book(row.author, row.title, row.genre, row.year, row.`type`)))

  // returns all books matching a title -- will want to implement a better form of title searching that includes close matches
  def findByTitle(title: String): Future[Seq[(Int, Book)]] =
    db.run(Books.filter(_.title.toLowerCase === title.toLowerCase).result).
      map(result => result.
        map(row => row.id -> Book(row.author, row.title, row.genre, row.year, row.`type`)))
}
