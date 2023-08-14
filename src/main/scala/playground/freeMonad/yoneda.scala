package playground.freeMonad

import cats.Functor

object Yoneda {

  // Assuming we have 2 functions that we want to apply to a list
  def timesTwo(input: Int): Int = input * 2
  def addThree(input: Int): String = input.toString()

  def main(args: Array[String]): Unit = {
    // timesTwo and addThree are repeated 10 times
    val result = (0 to 10).map(timesTwo).map(addThree)
    println(result)
  }

  // High level steps
  // -

  trait LazyFunctor[F[_], A] { self =>
    // functions to be applied after we choose run
    // functions, because it can be functions composed together into one function
    // In Coyoneda, we apply the transformation in `map`, in here we apply the transformation in `run`
    def transform[B](f: A => B): F[B]
    def run: F[A] = transform(identity)

    def map[B](f: A => B): LazyFunctor[F, B] = new LazyFunctor[F, B] {
      def transform[C](g: B => C): F[C] = self.transform(g compose f)
    }
  }

  object LazyFunctor {
    def lift[F[_], A](fa: F[A])(using F: Functor[F]): LazyFunctor[F, A] = new LazyFunctor[F, A] {
      override def transform[B](f: A => B): F[B] = F.map(fa)(f)
    }
  }

}
