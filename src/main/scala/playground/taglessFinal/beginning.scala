package playground.taglessFinal

import cats.Monad
import cats.effect.IO
import cats.implicits._
import cats.effect.unsafe.implicits._

object Beginning {

  case class User(name: String)

  // Unlike Free, the algebra is a trait with some methods describing the repository
  // The algebra is listed as a product type
  trait ActionRepository[F[_]] {
    def getUser(name: String): F[User]
    def getScore(user: User): F[Int]
  }

  def main(args: Array[String]): Unit = {

    class Program[F[_]: Monad](ar: ActionRepository[F]) {
      def run(name: String): F[Int] = for {
        user <- ar.getUser(name)
        score <- ar.getScore(user)
      } yield score
    }

    trait ActionInterpreter extends ActionRepository[IO] {
      override def getUser(name: String) = IO(User("Joe"))
      override def getScore(user: User): IO[Int] = IO(10)
    }

    val result: IO[Int] = new Program(new ActionInterpreter {}).run("Joe")
    println(result.unsafeRunSync())
  }

}
