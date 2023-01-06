package playground.polymorphism


object Implicit {

  def main(args: Array[String]): Unit = {

    // type class
    // whether you realised it or not, we actually already used type class

    trait Applicant {
      val name: String
    }
    case class InternalApplicant(name: String) extends Applicant
    case class IndependentApplicant(name: String) extends Applicant

    trait Processor[A] {
      def process(a: A): Boolean
    }

    object Processor {
      given Processor[InternalApplicant] with
        def process(a: InternalApplicant): Boolean = true
      given Processor[IndependentApplicant] with
        def process(a: IndependentApplicant): Boolean = true
    }

    def process[A](a :A)(using processor: Processor[A]): Boolean = {
      processor.process(a)
    }

    println(process(InternalApplicant("Jill")))
  }
}
