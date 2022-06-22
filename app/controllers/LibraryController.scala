package controllers

import models.DBs.{LibraryDB, ReviewsDB}
import models.forms.{Review, SearchQuery}
import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText, text}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json._
import play.api.mvc._
import slick.jdbc.JdbcProfile

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import play.api.i18n.I18nSupport


// Controller for all library related actions
@Singleton
class LibraryController @Inject()(val controllerComponents: ControllerComponents, protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
  extends BaseController with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport {
  import models.LibraryService._

  // give access to the library database
  private val model = new LibraryDB(db)
  private val reviews = new ReviewsDB(db)

  // form template for adding a new book, has some constraints for the year field
  val newBookForm: Form[Book] = Form(mapping(
    "author"    -> nonEmptyText,
    "title"     -> nonEmptyText,
    "genre"     -> nonEmptyText,
    "year"      -> nonEmptyText.verifying("Must be a valid year", year => year.matches("^[12][0-9]{3}$")),
    "bookType"  -> nonEmptyText
  )(Book.apply)(Book.unapply))

  // form template for searches
  val searchForm: Form[SearchQuery] = Form(mapping(
    "param" -> nonEmptyText,
    "query" -> nonEmptyText
  )(SearchQuery.apply)(SearchQuery.unapply))

  // form template for reviews
  val reviewForm: Form[Review] = Form(mapping(
    "rating" -> nonEmptyText,
    "review" -> text
  )(Review.apply)(Review.unapply))

  // action to get all books from the db and return them in the library view
  def getBooks(): Action[AnyContent] = Action.async { implicit request =>
    withUser { username =>
      // gets books from db and passes them to the view
      model.books.map {
        results => Ok(views.html.library(results, newBookForm, searchForm, username))
      }
    }
  }

  // sends user to the page dedicated to a given book
  def goToBook(index: Int): Action[AnyContent] = Action.async { implicit request =>
    withUser { username =>
      // gets the book with the given index from the table
      model.getBook(index).flatMap {
        // gets the reviews for said book
        result => reviews.getBookReviews(index).map { bookReviews =>
          // pass both set of results to the book page
          Ok(views.html.book(result, reviewForm, bookReviews))
        }
      }
    }
  }

  // adds a book to the db, uses content type to pick which method
  def addBook(): Action[AnyContent] = Action.async { implicit request =>
    request.contentType match {
      case Some(content) => content match {
        // pattern match to the content type and choose how to add the new book
        case "application/json" => addBookJson
        case "application/x-www-form-urlencoded" => submitNewBookForm
        case _ => Future.successful(BadRequest(Json.obj("error" -> "unexpected content type")))
      }
      case None => Future.successful(BadRequest(Json.obj("error" -> "request had no content")))
    }
  }

  def addBookJson(implicit request: Request[AnyContent]): Future[Result] = {
    // reads the json to a Book
    request.body.asJson.map { result =>
      Json.fromJson[Book](result) match {
        // matches the result to a bad request if JsError, or to the db method if successful
        case JsError(_) => Future.successful(BadRequest(Json.obj("Error" -> "Json incorrect")))
        case JsSuccess(book, _) =>
          model.addBook(book).flatMap {
            case true => Future.successful(Ok(Json.obj("Success" -> "Book added")))
            case false => Future.successful(BadRequest(Json.obj("Error" -> "Book already exists")))
          } recover {
            case e: Throwable =>
              InternalServerError(Json.obj("Error" -> "Something failed"))
          }
      }
    }.getOrElse(Future.successful(BadRequest(Json.obj("error" -> "couldn't get json from request body"))))
  }

  // adds a book to the bd -- FORM METHOD
  def submitNewBookForm(implicit request: Request[AnyContent]): Future[Result] =  {
      withUser { username =>
        newBookForm.bindFromRequest().fold(
          // if the form contained errors, reload the page with an error flash
          formErrors => Future.successful(Redirect(routes.LibraryController.getBooks()).
            flashing("Error" -> "Form entered Incorrectly")),
          newBook => model.addBook(newBook).flatMap {
            // tries to add the book to the db, reloading the page and showing a given message (success or error)
            case true => Future.successful(Redirect(routes.LibraryController.getBooks()).
              flashing("Success" -> "Book Added"))
            case false => Future.successful(Redirect(routes.LibraryController.getBooks()).
              flashing("Error" -> "Book already exists"))
          }
        )
      }
  }



  // removes a book from the db given its id
  def removeBook(index: Int): Action[AnyContent] = Action.async { implicit request =>
    model.removeBook(index).map { result =>
      if (result) Redirect(routes.LibraryController.getBooks())
      else BadRequest(Json.obj("Error" -> "Index not in library"))
    }
  }

  def deleteBook(): Action[AnyContent] = Action.async { implicit request =>
    withUser {
      // validates that the user is the admin, who has ability to delete books
      case "admin" => {
        request.body.asFormUrlEncoded.map { args =>
          // grabs the book index from the request body
          val index = args("index").head.toInt
          model.removeBook(index).map {
            // tries to remove the book from the database, redirects with an appropriate response
            case true => Redirect(routes.LibraryController.getBooks()).
              flashing("Success" -> "Book Deleted")
            case false => Redirect(routes.LibraryController.getBooks()).
              flashing("Error" -> "Book didn't exist")
          }
        }.getOrElse(Future.successful(Redirect(routes.LibraryController.getBooks())))
      }
      case _ => Future.successful(BadRequest(Json.obj("error" -> "not authorized")))
    }
  }

  // search for books based on a given parameter and search query
  def search(): Action[AnyContent] = Action.async { implicit request =>
    // if there is no session, redirect back to the log in page
    withUser { username =>
        searchForm.bindFromRequest().fold(
          // if the form had errors, reload the page with an error
          formErrors => Future.successful(Redirect(routes.LibraryController.getBooks()).
            flashing("Error" -> "Form entered Incorrectly")),
          // if search form is fine, pass the two vals down to the database to complete the search.
          newSearch => model.find(newSearch.param, newSearch.query).map {
            results => Ok(views.html.search(results))
          }
        )
      }
  }

  // Helper function for validating a user session.
  // Tries to get the username from the session cookie of the implicit request
  // if this does not exist, redirects back to the login page
  // if it does, it passes the username to the function argument
  def withUser(f: String => Future[Result])(implicit request: Request[AnyContent]): Future[Result] = {
    request.session.get("username") match {
      case Some(username) => f(username)
      case None => Future.successful(Redirect(routes.UserController.load()))
    }
  }

}
