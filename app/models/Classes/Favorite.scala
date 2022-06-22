package models.Classes

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat

case class Favorite(date: DateTime, title: String, author: String, bookId: Int){
  def dateToString: String = {
    val format = ISODateTimeFormat.dateHourMinute()
    format.print(date).replace('T', ' ')
  }
}
