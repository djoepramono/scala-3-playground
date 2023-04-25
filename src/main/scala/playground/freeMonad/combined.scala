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

  // Instead of Free[ActionA, A] or Action[A] in the previous example,
  //   we need Free[F,A]
  // In other words, instead of committing to a specific functor,
  //   we say it would be a functor and we will provide the implementation implicitly
  // Instead of liftF, we are using liftInject
  class UserAction[F[_]: Functor](using I: InjectK[UserActionA, F] ){
    def getUser(name: String): Free[F, User] =
      liftInject[F](GetUser(name))
  }

  // The companion object to store the implicits
  // object UserAction {
  //   given [UserAction] with
  //     def userAction[F[_]](using I: InjectK[Interact, F]): UserAction[F] = new UserAction[F]
  // }

  // And a simpler version
  class ScoreAction[F[_]](using I: InjectK[ScoreActionA, F]) {
    def getScore(user: User): Free[F, Int] =
      liftInject(GetScore(user))
  }

  // We can also use different interpreters e.g. use IO instead of Future
  // val ioInterpreter = new (ActionA ~> IO) {
  //   override def apply[A](fa: ActionA[A]): IO[A] = fa match {
  //     case GetUser(name)  => IO(Some(User("Always Joe")))
  //     case GetScore(user) => IO(Some(100))
  //   }
  // }

  def main(args: Array[String]): Unit = {
    println("combined free")

    // we can use Monad Transformer from the previous session
    // we need the type parameter, otherwise it'll be treated as Any
    // make sense because it's Action[Option[A]], which mean it can be anything
    // val maybeScore = for {
    //   joe <- OptionT(getUser("Joe"))
    //   score <- OptionT(getScore(joe))
    // } yield score

    // // Remember since maybeScore is a Monad Transformer
    // // We need to extract the value (Free Monad)
    // // foldMap is basically running the Free Monads i.e. Action[A] and accumulate the result into a monad

    // // With futureInterpreter
    // // val x = maybeScore.value.foldMap(futureInterpreter)
    // // println(Await.result(x, Duration(100, "millis")))

    // // With IOInterpreter
    // val x = maybeScore.value.foldMap(ioInterpreter)
    // println(x.unsafeRunSync())
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
