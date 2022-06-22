package models

import org.joda.time.DateTime
import org.joda.time.DateTimeZone.UTC
import slick.ast.BaseTypedType
import slick.jdbc.{JdbcProfile, JdbcType}

import java.sql.Timestamp

trait ColumnTypeMappings {

  val profile: JdbcProfile
  import profile.api._

  implicit def jodaDateTimeType: JdbcType[DateTime] with BaseTypedType[DateTime] =
    MappedColumnType.base[DateTime, Timestamp](
      dt => new Timestamp(dt.getMillis),
      ts => new DateTime(ts.getTime, UTC)
    )
}
