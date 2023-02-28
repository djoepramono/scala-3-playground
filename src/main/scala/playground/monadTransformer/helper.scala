package playground.monadTransformer

import scala.concurrent.Future

implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

case class User(name: String)

def getUser(name: String): Future[Option[User]] =
  if name == "Joe"
  then Future(Some(User("Joe")))
  else Future(None)

def getScores(user: User): Future[Option[Int]] =
  if user.name == "Joe"
  then Future(Some(100))
  else Future(None)

def giveConstructiveFeedback(score: Int): String = {
  if score > 50
  then "good job Joe"
  else "not bad Joe"
}
