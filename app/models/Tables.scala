package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.PostgresProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Books.schema ++ Favorites.schema ++ Reviews.schema ++ Users.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Books
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param author Database column author SqlType(varchar), Length(50,true)
   *  @param title Database column title SqlType(varchar), Length(100,true)
   *  @param genre Database column genre SqlType(varchar), Length(20,true)
   *  @param year Database column year SqlType(varchar), Length(5,true)
   *  @param `type` Database column type SqlType(varchar), Length(20,true) */
  case class BooksRow(id: Int, author: String, title: String, genre: String, year: String, `type`: String)
  /** GetResult implicit for fetching BooksRow objects using plain SQL queries */
  implicit def GetResultBooksRow(implicit e0: GR[Int], e1: GR[String]): GR[BooksRow] = GR{
    prs => import prs._
    BooksRow.tupled((<<[Int], <<[String], <<[String], <<[String], <<[String], <<[String]))
  }
  /** Table description of table books. Objects of this class serve as prototypes for rows in queries.
   *  NOTE: The following names collided with Scala keywords and were escaped: type */
  class Books(_tableTag: Tag) extends profile.api.Table[BooksRow](_tableTag, "books") {
    def * = (id, author, title, genre, year, `type`) <> (BooksRow.tupled, BooksRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(author), Rep.Some(title), Rep.Some(genre), Rep.Some(year), Rep.Some(`type`))).shaped.<>({r=>import r._; _1.map(_=> BooksRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column author SqlType(varchar), Length(50,true) */
    val author: Rep[String] = column[String]("author", O.Length(50,varying=true))
    /** Database column title SqlType(varchar), Length(100,true) */
    val title: Rep[String] = column[String]("title", O.Length(100,varying=true))
    /** Database column genre SqlType(varchar), Length(20,true) */
    val genre: Rep[String] = column[String]("genre", O.Length(20,varying=true))
    /** Database column year SqlType(varchar), Length(5,true) */
    val year: Rep[String] = column[String]("year", O.Length(5,varying=true))
    /** Database column type SqlType(varchar), Length(20,true)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[String] = column[String]("type", O.Length(20,varying=true))
  }
  /** Collection-like TableQuery object for table Books */
  lazy val Books = new TableQuery(tag => new Books(tag))

  /** Entity class storing rows of table Favorites
   *  @param favoriteId Database column favorite_id SqlType(serial), AutoInc, PrimaryKey
   *  @param userId Database column user_id SqlType(int4)
   *  @param bookId Database column book_id SqlType(int4)
   *  @param favoritedAt Database column favorited_at SqlType(timestamp without time zone)
   *  @param username Database column username SqlType(varchar), Length(50,true) */
  case class FavoritesRow(favoriteId: Int, userId: Int, bookId: Int, favoritedAt: java.sql.Timestamp, username: String)
  /** GetResult implicit for fetching FavoritesRow objects using plain SQL queries */
  implicit def GetResultFavoritesRow(implicit e0: GR[Int], e1: GR[java.sql.Timestamp], e2: GR[String]): GR[FavoritesRow] = GR{
    prs => import prs._
    FavoritesRow.tupled((<<[Int], <<[Int], <<[Int], <<[java.sql.Timestamp], <<[String]))
  }
  /** Table description of table favorites. Objects of this class serve as prototypes for rows in queries. */
  class Favorites(_tableTag: Tag) extends profile.api.Table[FavoritesRow](_tableTag, "favorites") {
    def * = (favoriteId, userId, bookId, favoritedAt, username) <> (FavoritesRow.tupled, FavoritesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(favoriteId), Rep.Some(userId), Rep.Some(bookId), Rep.Some(favoritedAt), Rep.Some(username))).shaped.<>({r=>import r._; _1.map(_=> FavoritesRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column favorite_id SqlType(serial), AutoInc, PrimaryKey */
    val favoriteId: Rep[Int] = column[Int]("favorite_id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(int4) */
    val userId: Rep[Int] = column[Int]("user_id")
    /** Database column book_id SqlType(int4) */
    val bookId: Rep[Int] = column[Int]("book_id")
    /** Database column favorited_at SqlType(timestamp without time zone) */
    val favoritedAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("favorited_at")
    /** Database column username SqlType(varchar), Length(50,true) */
    val username: Rep[String] = column[String]("username", O.Length(50,varying=true))

    /** Foreign key referencing Books (database name favorites_book_id_fkey) */
    lazy val booksFk = foreignKey("favorites_book_id_fkey", bookId, Books)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing Users (database name favorites_user_id_fkey) */
    lazy val usersFk = foreignKey("favorites_user_id_fkey", userId, Users)(r => r.userId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table Favorites */
  lazy val Favorites = new TableQuery(tag => new Favorites(tag))

  /** Entity class storing rows of table Reviews
   *  @param reviewId Database column review_id SqlType(serial), AutoInc, PrimaryKey
   *  @param bookId Database column book_id SqlType(int4)
   *  @param review Database column review SqlType(text)
   *  @param rating Database column rating SqlType(int4)
   *  @param reviewedAt Database column reviewed_at SqlType(timestamp without time zone)
   *  @param username Database column username SqlType(varchar), Length(50,true) */
  case class ReviewsRow(reviewId: Int, bookId: Int, review: String, rating: Int, reviewedAt: java.sql.Timestamp, username: String)
  /** GetResult implicit for fetching ReviewsRow objects using plain SQL queries */
  implicit def GetResultReviewsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[ReviewsRow] = GR{
    prs => import prs._
    ReviewsRow.tupled((<<[Int], <<[Int], <<[String], <<[Int], <<[java.sql.Timestamp], <<[String]))
  }
  /** Table description of table reviews. Objects of this class serve as prototypes for rows in queries. */
  class Reviews(_tableTag: Tag) extends profile.api.Table[ReviewsRow](_tableTag, "reviews") {
    def * = (reviewId, bookId, review, rating, reviewedAt, username) <> (ReviewsRow.tupled, ReviewsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(reviewId), Rep.Some(bookId), Rep.Some(review), Rep.Some(rating), Rep.Some(reviewedAt), Rep.Some(username))).shaped.<>({r=>import r._; _1.map(_=> ReviewsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column review_id SqlType(serial), AutoInc, PrimaryKey */
    val reviewId: Rep[Int] = column[Int]("review_id", O.AutoInc, O.PrimaryKey)
    /** Database column book_id SqlType(int4) */
    val bookId: Rep[Int] = column[Int]("book_id")
    /** Database column review SqlType(text) */
    val review: Rep[String] = column[String]("review")
    /** Database column rating SqlType(int4) */
    val rating: Rep[Int] = column[Int]("rating")
    /** Database column reviewed_at SqlType(timestamp without time zone) */
    val reviewedAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("reviewed_at")
    /** Database column username SqlType(varchar), Length(50,true) */
    val username: Rep[String] = column[String]("username", O.Length(50,varying=true))

    /** Foreign key referencing Books (database name reviews_book_id_fkey) */
    lazy val booksFk = foreignKey("reviews_book_id_fkey", bookId, Books)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table Reviews */
  lazy val Reviews = new TableQuery(tag => new Reviews(tag))

  /** Entity class storing rows of table Users
   *  @param userId Database column user_id SqlType(serial), AutoInc, PrimaryKey
   *  @param username Database column username SqlType(varchar), Length(20,true)
   *  @param password Database column password SqlType(varchar), Length(200,true) */
  case class UsersRow(userId: Int, username: String, password: String)
  /** GetResult implicit for fetching UsersRow objects using plain SQL queries */
  implicit def GetResultUsersRow(implicit e0: GR[Int], e1: GR[String]): GR[UsersRow] = GR{
    prs => import prs._
    UsersRow.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table users. Objects of this class serve as prototypes for rows in queries. */
  class Users(_tableTag: Tag) extends profile.api.Table[UsersRow](_tableTag, "users") {
    def * = (userId, username, password) <> (UsersRow.tupled, UsersRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(userId), Rep.Some(username), Rep.Some(password))).shaped.<>({r=>import r._; _1.map(_=> UsersRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column user_id SqlType(serial), AutoInc, PrimaryKey */
    val userId: Rep[Int] = column[Int]("user_id", O.AutoInc, O.PrimaryKey)
    /** Database column username SqlType(varchar), Length(20,true) */
    val username: Rep[String] = column[String]("username", O.Length(20,varying=true))
    /** Database column password SqlType(varchar), Length(200,true) */
    val password: Rep[String] = column[String]("password", O.Length(200,varying=true))
  }
  /** Collection-like TableQuery object for table Users */
  lazy val Users = new TableQuery(tag => new Users(tag))
}
