package playground.freeMonad

import cats.Functor

object Coyoneda {

  // Given a type constructor F[A], get me a Functor for it so that I can modify the inner value without changing the container
  // F[A] to F[B] without F.map

  // High level steps
  // - we need to lift F[A] into another container type that has a map and run,
  //   - `lift` to put everything in a new container. The new container type needs to know both F and A, let's say C[F[_],A]
  //   - `map` to run the transformation
  //   - `run` to turn the container back to F[B]
  // - Other than the methods, we would need to know the context too
  //   - underlyingValue, F[A]
  //   - transformation, the function that would change A to B
  // - Tricks
  //   - apart from underlyingValue we would also need UnderlyingType
  //   - start with `lift`
  //   - continue with `run`, turning F[UnderlyingType] to F[A]
  //   - implement `map` with `lift`
  //   - implement `run` with the help of functor parameter

  trait FreeFunctor[F[_], A] {
    // underlyingValue also contains underlyingType
    type UnderlyingType
    val underlyingValue: F[UnderlyingType]
    val transformation: UnderlyingType => A

    // Turn underlyingValue or F[UnderlyingType] => F[A]
    def run(f: Functor[F]): F[A] = f.map(underlyingValue)(transformation)

    // Implement `map` with the helpf of `lift`
    def map[B](f: A => B): FreeFunctor[F, B] =
      FreeFunctor.lift(underlyingValue)(transformation andThen f)
  }

  // Given F[A] and A => B, give me a FreeFunctor[F, B]
  object FreeFunctor {
    def lift[F[_], A, B](fa: F[A])(f: A => B): FreeFunctor[F, B] = new FreeFunctor[F, B] {
      type UnderlyingType = A
      val underlyingValue: F[A] = fa
      val transformation: A => B = f
    }
  }

  // Let's see things in action
  // Imagine there's a type constructor to Action
  case class Action[A](input: A)

  def main(args: Array[String]): Unit = {
    // Turn Action[A] into Functor
    // the second parameter can be an identity function e.g. +0 or *1
    val coyoneda = FreeFunctor.lift(Action(2))(_ + 2)
    val coyoneda2 = coyoneda.map(_ + 3)

    val actionFunctor = new Functor[Action] {
      override def map[A, B](fa: Action[A])(f: A => B): Action[B] = Action(f(fa.input))
    }

    val result = coyoneda2.run(actionFunctor)

    // Nothing executes until the next line
    println(result)
  }

}
