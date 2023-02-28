package playground.monadTransformer

import cats.data.OptionT
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object CatsOptionT {
  def main(args: Array[String]): Unit = {
    val x = for {
      user <- OptionT(getUser("Joe"))
      score <- OptionT(getScores(user))
      comment = giveConstructiveFeedback(score)
    } yield comment

    println(Await.result(x.value, Duration(100, "millis")))
  }
}
