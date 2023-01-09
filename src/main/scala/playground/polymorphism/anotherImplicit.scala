package playground.polymorphism

import playground.polymorphism.Implicit._

object AnotherImplicit {

  def main(args: Array[String]): Unit = {

    // let's use Processor form `implicit.scala` exercise
    // but this time we use a separate case class
    case class ReferredApplicant(name: String)

    // and we can provide our own processor
    object ReferredApplicant {
      given Processor[ReferredApplicant] with
        def process(a: ReferredApplicant): Boolean = true
    }

    // this one of the benefits of using implicits,
    //   we can introduce a new case class and how to handle it encapsulated in a separate file
    println(process(ReferredApplicant("Jill")))
  }
}
