package playground.polymorphism

object Subtype {

  // Inheritance based polymorphism

  // Sealed trait can be changed to abstract class and it will work the same
  // We can only extend from one abstract class but we can extend from multiple traits
  sealed trait Applicant() {
    val name: String
  }

  case class InternalApplicant(
    name: String,
    yearsInTheCompany: Int,
  ) extends Applicant

  case class ExternalApplicant(
    name: String,
    agencyName: String
  ) extends Applicant

  def main(args: Array[String]): Unit = {

    def process(a: Applicant): Boolean = a match {
      case a: InternalApplicant => true
      case a: ExternalApplicant=> false
    }

    // The Liskov Substitution Principle states that subclasses should be substitutable for their base class
    // L in SOLID Principle
    // Internal Applicant <: Applicant
    println(s"processing internal applicant ${process(InternalApplicant("Jake",3))}")
    println(s"processing external applicant: ${process(ExternalApplicant("Jake","Yesterday Agency"))}")
  }

  // Extension:
  // Applicant can be made to extend another trait
  // Applicant <: Candidate
  // And we can modify process to accept Candidate in the function definition
}
