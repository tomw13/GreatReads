package models

import scala.collection.mutable
import scala.collection.mutable.Map

object Library {
  import LibraryService._

//  private val sHawking: Author = Author("Stephen", "Hawking")
//  private val jTolkien: Author = Author("J. R. R.", "Tolkien")

//  private val aBriefHistory = NonFictionBook("Stephen Hawking", "A Brief History of Time", "Science", "1988")
//  private val fellowship = FictionBook("J. R. R. Tolkien", "Fellowship of the Ring", "Fantasy", "1954")

//  private var library: mutable.Map[Int, Book] = mutable.Map((0 -> aBriefHistory), (1 -> fellowship))
//
//  private var id = 2
//
//  def reset: Boolean = {
//    library = mutable.Map((0 -> aBriefHistory), (1 -> fellowship))
//    id = 2
//    true
//  }
//
//  def books: mutable.Map[Int, Book] = library
//
//  def addBook(book: Book): Int = {
//    val temp = id
//    library += (id -> book)
//    id += 1
//    temp
//  }
//
//  def removeBook(id: Int): Boolean = library.remove(id) match {
//    case Some(key) => true
//    case None => false
//  }

  // just return a map, no need for the mutable
//  def findByGenre(genre: String): mutable.Map[Int, Book] = {
//    def matchGenre(targetGenre: String, book: Book) = book match {
//      case FictionBook(author, title,  bookGenre, _) => targetGenre == bookGenre
//      case NonFictionBook(author, title, bookGenre, _) => targetGenre == bookGenre
//    }
//    // can use filter here rather than cloning
//    val filteredLibrary = library.clone().retain((key, value) => matchGenre(genre, value))
//    filteredLibrary
//  }


}
