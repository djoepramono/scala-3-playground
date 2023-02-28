package playground.monadTransformer
import scala.concurrent.Future
import cats.instances.future
import scala.concurrent.Await
import scala.concurrent.duration.Duration

case class FutureOption[A](futureOption: Future[Option[A]]) {

  def map[B](f: A => B): FutureOption[B] = {
    // There is only one way of creating FutureOption[B]
    // It's through the case class constructor
    // Which means we would need to apply f on unwrapped futureOption
    FutureOption(futureOption.map(valueOpt => valueOpt.map(f)))
  }

  // functors are composable, you can stack map over map

  def flatMap[B](f: A => FutureOption[B]): FutureOption[B] = {
    // The concept is the same, use f on the unwrapped futureOption

    // You cannot stack flatMap because the second flatMap on option doesn't work with any tool that we have
    // The second flatmap expects a function A => Option[Any] because it's a flatMap on Option
    // f is A => FutureOption[B] so we cannot use f while stacking flatMap

    // f is still used to turn A to B
    // We just need to do pattern matching after the first flatMap

    // One thing that you might not notice right away is
    //  case class params is accessible as a property in the instantiated object
    //  e.g. you can call .futureOption on FutureOption[A]

     FutureOption(futureOption.flatMap(x => x match {
       case Some(v) => f(v).futureOption
       case None => Future.successful(None)
     }))
  }

  // monads are not composable, you cannot stack flatmap over flatmap
}

object FutureOption {
  def main(args: Array[String]): Unit = {
    val x = for {
      user <- FutureOption(getUser("Joe"))
      score <- FutureOption(getScores(user))
      comment = giveConstructiveFeedback(score)
    } yield comment

    println(Await.result(x.futureOption, Duration(100, "millis")))
  }
}


