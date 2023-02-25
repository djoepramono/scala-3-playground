package playground.monadTransformer

import cats.*
import cats.implicits.*

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration

// Now let's try to make a case class that can replace FutureOption[A] and OptionOption[A]
// We need to import cats, because we need a higher kinded type F[_] that is a Monad
case class OptionT[F[_], A](value: F[Option[A]])(using m: Monad[F]) {
  def map[B](f: A => B): OptionT[F, B] = {
   OptionT(value.map(valueOpt => valueOpt.map(f)))
  }

  def flatMap[B](f: A => OptionT[F, B]): OptionT[F, B] = {
    // val result = value.flatMap { valueOpt =>
    //   valueOpt match {
    //     case Some(v) => f(v).value
    //     case None => m.pure[Option[B]](None)
    //   }
    // }
    // OptionT(result)
    OptionT(value.flatMap(x => x match {
      case Some(y) => f(y).value
      case None => m.pure(None)
    }))
  }
}

object OptionT {
  def main(args: Array[String]): Unit = {
    val x = for {
      user <- OptionT(getUser("Joe"))
      score <- OptionT(getScores(user))
      comment = giveConstructiveFeedback(score)
    } yield comment

    println(Await.result(x.value, Duration(100, "millis")))
  }
}
