package playground.polymorphism

object ParametricContravariance {

  // Let's revisit the exercise from playground.polymorphism.Parametric
  // But this time we simplify it a little bit
  // Instead of Group, we are going to have Processor that doesn't have an internal state
  //   i.e. Processor.process doesn't store the output in a variable inside Processor

  trait Applicant {
    val name: String
  }
  case class InternalApplicant(name: String) extends Applicant
  case class ExternalApplicant(name: String) extends Applicant

  def main(args: Array[String]): Unit = {

    // Processor has `-` before A
    // abstract class can be replaced with trait
    abstract class Processor[-A] {
      def process(a: A): Unit
    }

    val applicantProcessor = new Processor[Applicant] {
      def process(a: Applicant): Unit = {
        println(s"""processing applicant: ${a.name}""")
      }
    }

    // we can use Processor[Applicant] for Processor[InternalApplicant]
    // because the contravariant relationship
    val internalApplicantProcessor: Processor[InternalApplicant] = applicantProcessor
    internalApplicantProcessor.process(InternalApplicant(name = "Joe"))

    // what if we go the other way around?
    // using Processor[InternalApplicant] for Processor[Applicant]
    // this would not work, because anotherProcessor is too specific for the parent case
    // which is what we want
    val anotherProcessor = new Processor[InternalApplicant] {
      def process(a: InternalApplicant): Unit = {
        println(s"""processing internal applicant: ${a.name}""")
      }
    }
    // val bigP: Processor[Applicant] = anotherProcessor

    // Contravariance
    // InternalApplicant can be used in place of Applicant
    // Processor[Applicant] can be used in a place of Processor[InternalApplicant]
  }
}

