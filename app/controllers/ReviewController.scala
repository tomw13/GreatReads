package controllers

import models.DBs.ReviewsDB
import models.forms.{Review, SearchQuery}
import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText, number, text}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.I18nSupport
import play.api.libs.json._
import play.api.mvc._
import slick.jdbc.JdbcProfile

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try


// Controller for all review related actions
@Singleton
class ReviewController @Inject()(val controllerComponents: ControllerComponents, protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
  extends BaseController with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport {
  import models.LibraryService._

  // give access to the reviews database
  private val reviews = new ReviewsDB(db)

  // form template for reviews
  val reviewForm: Form[Review] = Form(mapping(
    "rating" -> nonEmptyText,
    "review" -> text
  )(Review.apply)(Review.unapply))

  // adds a review of a book to the db
  def reviewBook(index: Int): Action[AnyContent] = Action.async { implicit request =>

    // small helper function to convert String => Option[Int]
    def strToOptionInt(str: String) = Try(str.toInt).toOption

    withUser { username =>
      reviewForm.bindFromRequest().fold(
        // grab the form info, throw errors if incorrect
        formErrors => Future.successful(Redirect(routes.LibraryController.goToBook(index)).
          flashing("Error" -> "Form entered Incorrectly")),
        newReview =>
          // check rating to make sure it converts to Int nicely
          strToOptionInt(newReview.rating) match {
            case Some(num) =>
              // if parsed correctly, post the review and respond accordingly
              reviews.postReview(index, username, newReview.rating.toInt, newReview.review).flatMap {
              case true => Future.successful(Redirect(routes.LibraryController.goToBook(index)).
                flashing("Success" -> "Book Reviewed"))
              case false => Future.successful(Redirect(routes.LibraryController.goToBook(index)).
                flashing("Error" -> "Book already reviewed"))
            }
            case None => Future.successful(BadRequest(Json.obj("error" -> "Rating not a number")))
          }
      )
    }
  }

  // deletes a review from the db
  def deleteReview(): Action[AnyContent] = Action.async { implicit request =>
    withUser { sessionUser =>
      request.body.asFormUrlEncoded.map { args =>
        // tries to grab the book id and username from the form body
        val bookId = args("bookId").head.toInt
        val username = args("username").head
        // a user can only delete their own reviews
        if (sessionUser == username) {
          // delete the review, respond accordingly
          reviews.deleteReview(username, bookId).map {
            case true =>Redirect(routes.UserController.profile(Some(username))).
              flashing("Success" -> "Review Deleted")
            case false => Redirect(routes.UserController.profile(Some(username))).
              flashing("Error" -> "Review didn't exist")
          }
        } else {
          Future.successful(BadRequest(Json.obj("error" -> "Not authorised to delete this review")))
        }
      }.getOrElse(Future.successful(Redirect(routes.LibraryController.getBooks())))
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
