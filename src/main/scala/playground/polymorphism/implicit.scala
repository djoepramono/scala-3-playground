package playground.polymorphism

trait Applicant {
  val name: String
}
case class InternalApplicant(name: String) extends Applicant
case class IndependentApplicant(name: String) extends Applicant

object Implicit {

  // similar to abstract class in the previous example, we are going to use trait as type class
  trait Processor[A] {
    def process(a: A): Boolean
  }

  // the difference here is, we are using given
  object Processor {
    given Processor[InternalApplicant] with
      def process(a: InternalApplicant): Boolean = true
    given Processor[IndependentApplicant] with
      def process(a: IndependentApplicant): Boolean = true
  }

  // using here is also known as context parameter
  def process[A](a :A)(using processor: Processor[A]): Boolean = {
    processor.process(a)
  }

  def main(args: Array[String]): Unit = {

    // type class
    // whether you realised it or not, we actually already used type class

    // now process can be called eventhough the second params: processor are not passed in
    println(process(InternalApplicant("Jill")))
  }
}
