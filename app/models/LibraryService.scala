package models

import play.api.libs.json._

object LibraryService {

//  final case class Author(firstName: String, lastName: String) {
//    def name: String = s"$firstName $lastName"
//  }
//
//  implicit val AuthorFormat: OFormat[Author] = Json.format[Author]

//  sealed trait Book
//  final case class FictionBook(author: String, title: String, genre: String, year: String) extends Book
//  final case class NonFictionBook(author: String, title: String, genre: String, year: String) extends Book
  final case class Book(author: String, title: String, genre: String, year: String, bookType: String)

  implicit val BookFormat: Format[Book] = Json.format[Book]

//  val FictionBookFormat: OFormat[FictionBook] = Json.format[FictionBook]
//  val NonFictionBookFormat: OFormat[NonFictionBook] = Json.format[NonFictionBook]
//
//  implicit object BookFormat extends OFormat[Book] {
//    def reads(in: JsValue): JsResult[Book] = (in \ "type").get match {
//      case JsString("Fiction") => FictionBookFormat.reads(in)
//      case JsString("Non-Fiction") => NonFictionBookFormat.reads(in)
//      case other => JsError(JsPath \ "type", s"Invalid type: $other")
//    }
//
//
//    def writes(in: Book): JsObject = in match {
//      case in: FictionBook => FictionBookFormat.writes(in) ++ Json.obj("type" -> "Fiction")
//      case in: NonFictionBook => NonFictionBookFormat.writes(in) ++ Json.obj("type" -> "Non-Fiction")
//    }
//  }

}
