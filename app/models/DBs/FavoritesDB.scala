package models.DBs

import models.Classes.Favorite
import models.Tables.{Books, Favorites, FavoritesRow, Reviews}
import org.joda.time.DateTime
import org.joda.time.DateTimeZone.UTC
import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import scala.concurrent.{ExecutionContext, Future}

class FavoritesDB(db: Database)(implicit ec: ExecutionContext) {

  def addFavorite(bookId: Int, userId: Int, username: String): Future[Boolean] = {

    val data = Query((userId, bookId, new Timestamp(DateTime.now().getMillis), username))
    val exists = Favorites.filter(r => r.userId === userId && r.bookId === bookId).exists
    val selectExpression = data.filterNot(_ => exists)

    db.run(Favorites.map(f => (f.userId, f.bookId, f.favoritedAt, f.username)).
      forceInsertQuery(selectExpression)).map(_ > 0)

  }


  def getFavorites(username: String): Future[Seq[Favorite]] =
    db.run(Books.join(Favorites).on(_.id === _.bookId).
      filter { case (_, favorite) => favorite.username === username }.sortBy { case (_, favorite) => favorite.favoritedAt.desc }.result).map { results =>
        results.map { result =>
          val (book, favorite) = result
          Favorite(new DateTime(favorite.favoritedAt.getTime, UTC), book.title, book.author, book.id)
        }
    }

  def deleteFavorite(bookId: Int, username: String): Future[Boolean] =
    db.run(Favorites.filter(row => row.bookId === bookId && row.username === username).delete).map(_ > 0)


}
