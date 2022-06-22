package models.Classes

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat

case class UserReview(date: DateTime, title: String, bookId: Int, rating: Int, review: String){
  def dateToString: String = {
    val format = ISODateTimeFormat.dateHourMinute()
    format.print(date).replace('T', ' ')
  }
}
