package playground.polymorphism

object ParametricContravariance {

  trait Applicant {
    val name: String
  }
  case class InternalApplicant(name: String) extends Applicant
  case class IndependentApplicant(name: String) extends Applicant

  def main(args: Array[String]): Unit = {

    // let's create an abstract class that takes a type parameter
    // this type parameter is used in a method's parameter
    // don't forget to put - in the parameter type
    // this means Processor is contravariant in A

    // abstract class can be replaced with trait
    abstract class Processor[-A] {
      def process(a: A): Unit
    }


    // now let's create the implementation in a class targetting the parent type
    val applicantProcessor = new Processor[Applicant] {
      def process(a: Applicant): Unit = {
        println(s"""processing applicant: ${a.name}""")
      }
    }

    // we can use ApplicantProcessor for InternalApplicantProcessor
    // because the contravariant relationship
    val internalApplicantProcessor: Processor[InternalApplicant] = applicantProcessor
    internalApplicantProcessor.process(InternalApplicant(name = "Joe"))

    // the method process is using the type parameter, this usually indicates contravariance relationship

    // what if we go specific
    val anotherProcessor = new Processor[InternalApplicant] {
      def process(a: InternalApplicant): Unit = {
        println(s"""processing applicant: ${a.name}""")
      }
    }

    // this would not work, because anotherProcessor is too specific for the parent case
    // which is what we want
    // val bigP: Processor[Applicant] = anotherProcessor

    // Contravariance
    // InternalApplicant can be used in place of Applicant
    // Processor[Applicant] can be sue in a place of Processor[InternalApplicant]
  }
}

