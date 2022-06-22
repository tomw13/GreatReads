package controllers

import models.DBs.{FavoritesDB, ReviewsDB, UsersDB}

import javax.inject._
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.I18nSupport
import play.api.libs.json._
import play.api.mvc._
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}


// Controller for all user related tasks like login/out, creation and home page.
@Singleton
class UserController @Inject()(val controllerComponents: ControllerComponents, protected val dbConfigProvider: DatabaseConfigProvider)
                               (implicit ec: ExecutionContext) extends BaseController with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport{


  import models.forms.UserInfo

  // access our users DataBase
  private val users = new UsersDB(db)
  private val reviews = new ReviewsDB(db)
  private val favorites = new FavoritesDB(db)

  // create a form template for logging in/creating an account
  val userForm: Form[UserInfo] = Form(mapping(
    "username"  -> nonEmptyText,
    "password"  -> nonEmptyText
  )(UserInfo.apply)(UserInfo.unapply))

  // load the home/login page with a form for login and create account
  def load(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.login(userForm, userForm))
  }

  // redirect back to the login page and remove the previous session
  def logout(): Action[AnyContent] = Action { implicit request =>
    Redirect(routes.UserController.load()).withNewSession
  }

  // create a new user and direct to the library page
  def createUser(): Action[AnyContent] = Action.async { implicit request =>
    userForm.bindFromRequest().fold(
      // binds the form, passes the form with errors back to the view if incorrect
      formErrors => Future.successful(BadRequest(views.html.login(userForm, formErrors))),
      // if form is correct, creates a new user in the database
      userInfo => users.createUser(userInfo).flatMap {
        // if successful, redirect to the library and create a new session
        case true => Future.successful(Redirect(routes.LibraryController.getBooks()).
          withSession("username" -> userInfo.username))
        // if failed, the user already existed, so reload the home page and pass an error message
        case false => Future.successful(Redirect(routes.UserController.load()).
          flashing("Error" -> "User already exists"))
      }

    )
  }

  // Validate a user's credentials and log in
  def login(): Action[AnyContent] = Action.async {implicit request =>
    userForm.bindFromRequest().fold(
      // binds the form, passes the form with errors back to the view if incorrect
      formErrors => Future.successful(BadRequest(views.html.login(formErrors, userForm))),
      // if form is correct, validates the user in the database
      userInfo => users.withUser(userInfo).flatMap {
        // if successful, redirect to the library and create a new session
        case Some(id) => Future.successful(Redirect(routes.LibraryController.getBooks()).
          withSession("username" -> userInfo.username))
        // if the user is invalid, reload the home page and pass an error message
        case None => Future.successful(Redirect(routes.UserController.load()).
          flashing("Error" -> "Incorrect username/password"))
      }
    )
  }

  // Sends a user to a given profile page
  def profile(user: Option[String]): Action[AnyContent] = Action.async {implicit request =>
    withUser { sessionUser => user match {
      // if a username is given, send to that user's profile
        case Some(username) => reviews.getUserReviews(username).flatMap{ userReviews =>
          favorites.getFavorites(username).map { userFavorites =>
            // get the given user's reviews and favorites and pass to profile view
            Ok(views.html.profile(username, userReviews, userFavorites, canEdit = false))
          }
        }
        // if user is omitted, send to own profile page
        case None => reviews.getUserReviews(sessionUser).flatMap{ userReviews =>
          favorites.getFavorites(sessionUser).map { userFavorites =>
            // get the session user's reviews and favorites and pass to view
            Ok(views.html.profile(sessionUser, userReviews, userFavorites, canEdit = true))
          }
        }
      }
    }
  }

  // add a book to the current user's favorites list
  def addToFavorites(bookId: Int): Action[AnyContent] = Action.async { implicit request =>
    withUser { username =>
      // get the user info of the current user
      users.getUser(username).flatMap { result =>
        val (userId, name) = result
        favorites.addFavorite(bookId, userId, name).map {
          //  add the book to the user's favorites list in db, throw error if already in there
          case true => Redirect(routes.LibraryController.goToBook(bookId)).
            flashing("Success" -> "Book added to favorites")
          case false => Redirect(routes.LibraryController.goToBook(bookId)).
            flashing("Error" -> "Book already in favorites")
        }
      }
    }
  }

  // remove a favorite from a user's list
  def deleteFavorite(): Action[AnyContent] = Action.async { implicit request =>
    withUser { username =>
        request.body.asFormUrlEncoded.map { args =>
          // try to extract the book id and the username from the form body
          val bookId = args("bookId").head.toInt
          val username = args("username").head
          // pass the vals to deleteFavorite
          favorites.deleteFavorite(bookId, username).map {
            // reload the page with relative success or error message
            case true => Redirect(routes.UserController.profile(None)).
              flashing("Success" -> "Favorite removed")
            case false => Redirect(routes.UserController.profile(None)).
              flashing("Error" -> "Favorite did not exist")
          }
        }.getOrElse(Future.successful(Redirect(routes.UserController.profile(None))
          .flashing("Error" -> "Form body incorrect")))
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
