package playground.freeMonad

import cats.instances.string
import cats.free.Free
import cats.free.Free.liftF
import cats.data.OptionT
import cats.~>
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration


case class User(name: String)

// ActionA = Action Algebra
// The algebra contains case classes
sealed trait ActionA[A]
case class GetUser(name: String) extends ActionA[Option[User]]
case class GetScore(user: User) extends ActionA[Option[Int]]

// Action[A] represents a free (gratis) monad for your algebra
type Action[A] = Free[ActionA, A]

// Too many type parameters to look for, several type parameters can be removed
// - liftF type parameter can be omitted
// - The algebra type parameter can be omitted
// getUser is a helper function albeit like higher order function
//   that turn the algebra into a ready to use function that is returns a monad

def getUser(name: String): Action[Option[User]] =
    liftF[ActionA, Option[User]](GetUser(name))

def getScore(user: User): Action[Option[Int]] =
    liftF(GetScore(user))

implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

// The intepreter would translate GetUser[A](name: String) to Future[Option[A]]
// The interpreter result would need to be monadic
// Future is a monad and not a monad at the same time.
//   This is because Future evaluates eagerly
//   Future(100) is a monad, Future(Random.nextInt(100)) is not a monad
//     because it's not referentially transparent
val futureInterpreter = new (ActionA ~> Future) {
    override def apply[A](fa: ActionA[A]): Future[A] = fa match {
        case GetUser(name) => Future.successful(Some(User("Always Joe")))
        case GetScore(user) => Future.successful(Some(100))
    }
}

object FreeMonad {
    def main(args: Array[String]): Unit = {

        // we can use Monad Transformer from the previous session
        // we need the type parameter, otherwise it'll be treated as Any
        // make sense because it's Action[Option[A]], which mean it can be anything
        val maybeScore = for {
            joe <- OptionT(getUser("Joe"))
            score <- OptionT(getScore(joe))
        } yield score

        println(maybeScore)

        // Remember since maybeScore is a Monad Transformer
        // We need to extract the value (Free Monad)
        // foldMap is basically running the Free Monads i.e. Action[A] and accumulate the result into a monad
        val x = maybeScore.value.foldMap(futureInterpreter)
        println(Await.result(x, Duration(100, "millis")))
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
