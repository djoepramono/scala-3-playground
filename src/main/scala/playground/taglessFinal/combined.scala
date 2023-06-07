package playground.taglessFinal

import cats.Monad
import cats.effect.IO
import cats.implicits._
import cats.effect.unsafe.implicits._

object Combined {

  case class User(name: String)
  
  trait UserRepository[F[_]] {
    def getUser(name: String): F[User]    
  }

  trait ScoreRepository[F[_]] {
    def getScore(user: User): F[Int]
  }

  def main(args: Array[String]): Unit = {

    // Combining 2 algebras in Tagless Final is basically injecting 2 repositories 
    // It's easier because to combine algebras you need a functor
    //   and both repositories already uses a functor that is a monad    
    class Program[F[_]: Monad](ur: UserRepository[F], sr: ScoreRepository[F]) {
      def run(name: String): F[Int] = for {
        user <- ur.getUser(name)
        score <- sr.getScore(user)
      } yield score
    }

    trait UserInterpreter extends UserRepository[IO] {
      override def getUser(name: String) = IO(User("Joe"))      
    }

    trait ScoreInterpreter extends ScoreRepository[IO] {      
      override def getScore(user: User): IO[Int] = IO(10)
    }

    val result: IO[Int] = new Program(new UserInterpreter {}, new ScoreInterpreter {}).run("Joe")
    println(result.unsafeRunSync())
  }

}
