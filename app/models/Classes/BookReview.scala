package models.Classes

import org.joda.time.DateTime
import org.joda.time.DateTimeZone.UTC
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat

case class BookReview(date: DateTime, user: String, rating: Int, review: String){
  def dateToString: String = {
    val format = ISODateTimeFormat.dateHourMinute()
    format.print(date).replace('T', ' ')
  }
}
