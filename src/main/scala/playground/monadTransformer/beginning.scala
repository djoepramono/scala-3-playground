package playground.monadTransformer

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration

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

    // flatMap or for comprehension can only unwrap 1 layer at a time
    // but what if we can mesh 2 layers into 1 one layer?

    // what if Future[Option[T]] was a monad
    //    val x = for {
    //      user <- getUser("Jo")
    //      score <- getScores(user)
    //      comment = giveConstructiveFeedback(score)
    //    } yield comment
  }
}


