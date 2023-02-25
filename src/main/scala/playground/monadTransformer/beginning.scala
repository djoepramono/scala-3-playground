package playground.monadTransformer

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration

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

object Beginning {
  def main(args: Array[String]): Unit = {

    val x = for {
      user <- getUser("Jo")
      score <- user match {
        case Some(u) => getScores(u)
        case None => Future.successful(None)
      }
      comment = score match {
        case Some(score) => Some(giveConstructiveFeedback(score))
        case None => None
      }
    } yield comment

    println(Await.result(x, Duration(100, "millis")))

    // what if Future[Option[T]] was a monad
    //    val x = for {
    //      user <- getUser("Jo")
    //      score <- getScores(user)
    //      comment = giveConstructiveFeedback(score)
    //    } yield comment
  }
}


