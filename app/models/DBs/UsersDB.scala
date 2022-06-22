package models.DBs

import models.Tables.{Users, UsersRow}
import models.forms.UserInfo
import org.mindrot.jbcrypt.BCrypt
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

class UsersDB(db: Database)(implicit ec: ExecutionContext) {

  // validates user by filtering users based on given username, then compares password to hashed password.
  def validateUser(user: UserInfo): Future[Option[Int]] = {
    db.run(Users.filter(userRow => userRow.username === user.username).result).
      map(userRows => userRows.filter(userRow => BCrypt.checkpw(user.password, userRow.password)).
        map(_.userId).headOption)
  }

  // creates a user only if the username is not already in use. Uses bcrypt to hash the plaintext password
  def createUser(user: UserInfo): Future[Boolean] = {

    val data = Query((user.username, BCrypt.hashpw(user.password, BCrypt.gensalt())))
    val exists = Users.filter(u => u.username === user.username).exists
    val selectExpression = data.filterNot(_ => exists)

    db.run(Users.map(u => u.username -> u.password).forceInsertQuery(selectExpression)).map(_ > 0)

  }

  // will add in failure condition
  def getUser(username: String): Future[(Int, String)] = {
    db.run(Users.filter(userRow => userRow.username === username).result).map(result => result.map(
      user => user.userId -> user.username).head
    )
  }


}
