package playground.monadTransformer

import scala.concurrent.Await
import scala.concurrent.duration.Duration

// how about Option[Option[A]] can we turn it into a monad?
// yes, we can
object OptionOption {

  def getUser(name: String): Option[Option[User]] =
    if name == "Joe"
    then Some(Some(User("Joe")))
    else Some(None)

  def getScores(user: User): Option[Option[Int]] =
    if user.name == "Joe"
    then Some(Some(100))
    else Some(None)

  case class OptionOption[A](value: Option[Option[A]]) {
    def map[B](f: A => B): OptionOption[B] = {
      OptionOption(value.map(valueOpt => valueOpt.map(f)))
    }

    def flatMap[B](f: A => OptionOption[B]): OptionOption[B] = {
      OptionOption(value.flatMap(x => x match {
        case Some(v) => f(v).value
        case None => Some(None)
      }))
    }
  }

  def main(args: Array[String]): Unit = {

    val x = for {
      user <- OptionOption(getUser("Joe"))
      score <- OptionOption(getScores(user))
    } yield score

    println(x.value.flatten)
  }
}


