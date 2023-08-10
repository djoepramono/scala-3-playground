package playground.freeMonad

import cats.Functor

object Coyoneda {

  // Given a type constructor F[A], get me a Functor for it

  // High level steps
  // - underlyingValue and transformation as context
  // - implement FreeFunctor.map with liftF

  // map is running A => B, lifting B to F[B]: Monad, and flatMap on F[B]: Monad
  // A => B => C

  trait FreeFunctor[F[_], A] {
    type UnderlyingType // it's the type inside the type constructor
    val underlyingValue: F[
      UnderlyingType
    ] // it's the type constructor that we'd like to turn into a Functor, which is F[A]
    val transformation: UnderlyingType => A
    def map[B](f: A => B): FreeFunctor[F, B] = liftF(underlyingValue)(transformation andThen f)

    // Executing Functor's map
    // Turning FreeFunctor[A] to F[A]
    def run(f: Functor[F]): F[A] = f.map(underlyingValue)(transformation)
  }

  // Given F[A] and A => B, give me a FreeFunctor[F, B]
  def liftF[F[_], A, B](fa: F[A])(f: A => B): FreeFunctor[F, B] = new FreeFunctor[F, B] {
    type UnderlyingType = A
    val underlyingValue: F[A] = fa
    val transformation: A => B = f
  }

  case class Coyoneda[F[_], A, B](fa: F[A], ab: A => B)

  def main(args: Array[String]): Unit = { println("coyoneda") }

}
