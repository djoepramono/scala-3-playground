package playground.monadTransformer

import scala.concurrent.Await
import scala.concurrent.duration.Duration

def getUser2(name: String): Option[Option[User]] =
  if name == "Joe"
  then Some(Some(User("Joe")))
  else Some(None)

def getScores2(user: User): Option[Option[Int]] =
  if user.name == "Joe"
  then Some(Some(100))
  else Some(None)

case class OptionOption[A](optionOptionz: Option[Option[A]]) {

  def map[B](f: A => B): OptionOption[B] = {
    OptionOption(optionOptionz.map(valueOpt => valueOpt.map(f)))
  }

  def flatMap[B](f: A => OptionOption[B]): OptionOption[B] = {
    OptionOption(optionOptionz.flatMap(x => x match {
      case Some(v) => f(v).optionOptionz
      case None => Some(None)
    }))
  }
}

object optionOption {
  def main(args: Array[String]): Unit = {

    val x = for {
      user <- OptionOption(getUser2("Joe"))
      score <- OptionOption(getScores2(user))
    } yield score

    println(x.optionOptionz.flatten)
  }
}


