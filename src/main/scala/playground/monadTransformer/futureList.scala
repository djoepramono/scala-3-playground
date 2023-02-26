package playground.monadTransformer

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.Future

object FutureList {

  def getUser(name: String): Future[List[User]] =
    if name == "Joe"
    then Future(List(User("Joe"), User("Joel")))
    else Future(List.empty)

  def getScores(user: User): Future[List[Int]] =
    if user.name == "Joe" | user.name == "Joel"
    then Future(List(100, 99))
    else Future(List(50))

  case class FutureList[A](value: Future[List[A]]) {

    def map[B](f: A => B): FutureList[B] = {
      FutureList(value.map(valueOpt => valueOpt.map(f)))
    }

    // the concept is similar to futureOption
    // we need to unwrap `value` and apply f
    // but instead of pattern matching on the second step, we do map then fold
    //  map for applying f(_).value
    //  fold to turn List[Future[List[B]]] to Future[List[B]] i.e. removing the outer list
    def flatMap[B](f: A => FutureList[B]): FutureList[B] = {
      FutureList(value.flatMap(list =>
        list
          .map(x => f(x).value)
          .fold(Future.successful(List.empty[B]))((acc, curr) => {
            for {
              l1 <- acc
              l2 <- curr
            } yield l1 ::: l2
          })
      ))
    }
  }


  def main(args: Array[String]): Unit = {

    val x = for {
      user <- FutureList(getUser("Joe"))
      score <- FutureList(getScores(user))
    } yield score

    println(Await.result(x.value, Duration(100, "millis")))
  }
}


