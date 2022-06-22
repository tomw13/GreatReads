package models.DBs

import models.Classes.{BookReview, UserReview}
import models.Tables.{Books, Reviews, ReviewsRow}
import org.joda.time.DateTimeZone.UTC
import org.joda.time.DateTime
import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import scala.concurrent.{ExecutionContext, Future}

class ReviewsDB(db: Database)(implicit ec: ExecutionContext) {

  def postReview(bookId: Int, username: String, rating: Int, review: String): Future[Boolean] = {

    val data = Query((bookId, review, rating, new Timestamp(DateTime.now().getMillis), username))
    val exists = Reviews.filter(r => r.username === username && r.bookId === bookId).exists
    val selectExpression = data.filterNot(_ => exists)

    db.run(Reviews.map(r => (r.bookId, r.review, r.rating, r.reviewedAt, r.username)).
      forceInsertQuery(selectExpression)).map(_ > 0)

  }


  // returns date, username, rating, review
  def getBookReviews(bookId: Int): Future[Seq[BookReview]] =
    db.run(Reviews.filter(row => row.bookId === bookId).sortBy(_.reviewedAt.desc).result).map { results =>
      results.map { result =>
        val date = new DateTime(result.reviewedAt.getTime, UTC)
        BookReview(date, result.username, result.rating, result.review)
      }
    }

  // returns date, title, rating, review
  def getUserReviews(username: String): Future[Seq[UserReview]] =
    db.run(Books.join(Reviews).on(_.id === _.bookId).
      filter{ case (_, review) => review.username === username}.sortBy{ case (_, review) => review.reviewedAt.desc}.result).map { results =>
        results.map { result =>
          val (book, review) = result
          val date = new DateTime(review.reviewedAt.getTime, UTC)
          UserReview(date, book.title, book.id, review.rating, review.review)
        }
    }

  def deleteReview(username: String, bookId: Int): Future[Boolean] =
    db.run(Reviews.filter(row => row.username === username && row.bookId === bookId).delete).map(_ > 0)

}
