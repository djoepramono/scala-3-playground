package playground.freeMonad

import cats.instances.string
import cats.free.Free
import cats.free.Free.liftF
import cats.free.Free.liftInject
import cats.~>
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration

import cats.effect.unsafe.implicits._
import cats.effect.IO
import cats.data.EitherK
import cats.InjectK
import cats.Functor

// What if User and Score are based of different algebra set?
// What if there are two different backend supporting it
object Combined {

  case class User(name: String)

  // Two sets of algebra
  sealed trait UserActionA[A]
  case class GetUser(name: String) extends UserActionA[User]

  sealed trait ScoreActionA[A]
  case class GetScore(user: User) extends ScoreActionA[Int]

  // A single monad type
  // EitherK creates a monad type from two functor types
  type BackendAction[A] = EitherK[UserActionA, ScoreActionA, A]

  // ---------------------------
  // Instead of Free[ActionA, A] or Action[A] in the previous example,
  //   we need Free[F,A]
  // In other words, instead of committing to a specific functor,
  //   we say it would be a functor and we will provide the implementation implicitly
  // Instead of liftF, we are using liftInject
  class UserAction[F[_]](using I: InjectK[UserActionA, F] ){
    def getUser(name: String): Free[F, User] =
      liftInject[F](GetUser(name))
  }

  object UserAction {
    given UserAction[BackendAction] with
      def getUser[F[_]](using I: InjectK[UserActionA, F]): UserAction[F] = new UserAction[F]
  }

  // And a simpler version
  class ScoreAction[F[_]](using I: InjectK[ScoreActionA, F]) {
    def getScore(user: User): Free[F, Int] =
      liftInject(GetScore(user))
  }

  object ScoreAction {
    given ScoreAction[BackendAction] with
      def getScore[F[_]](using I: InjectK[ScoreActionA, F]): ScoreAction[F] = new ScoreAction[F]
  }
  //-------------------------------------

  // Now we need to write the interpreter
  val userActionInterpreter = new (UserActionA ~> IO) {
    override def apply[A](uaa: UserActionA[A]): IO[A] = uaa match {
      case GetUser(name)  => IO(User("Always Joe"))      
    }
  }

  val scoreActionInterpreter = new (ScoreActionA ~> IO) {
    override def apply[A](saa: ScoreActionA[A]): IO[A] = saa match {
      case GetScore(user)  => IO(100)      
    }
  }

  val combinedInterpreter: BackendAction ~> IO = userActionInterpreter or scoreActionInterpreter

  def main(args: Array[String]): Unit = {
    println("combined free")

    def program(using ua: UserAction[BackendAction], sa:ScoreAction[BackendAction]): Free[BackendAction, Int] = for {
      // remember that weird object?
      joe <- ua.getUser("Joe")
      score <- sa.getScore(joe)      
    } yield score

    val score = program.foldMap(combinedInterpreter)
    println(score.unsafeRunSync())

  }
}

// Questions
// Why the algebra is Action[A] instead of just Action
//   because Free Monad needs F[A]
//   because we need to record the output type in the Algebra of the execution in the algebra
//   because it's common to for the actions to have a context,
//     i.e. the actions requires a side effect executor e.g. API connector / DB library

// Extra notes
// Free Monad is a construction that is left adjoint to a forgetful functor
//   whose domain is the category of Monads and whose co-domain is the category of Endofunctors
// Free Monad build a monad from a functor with `Pure` and `Suspend`
