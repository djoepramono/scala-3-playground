package playground.polymorphism

object Subtype {

  // Inheritance based polymorphism

  // The next line can be changed to abstract class and it will work the same
  sealed trait Applicant() {
    val name: String
  }

  case class InternalApplicant(
    name: String,
    yearsInTheCompany: Int,
  ) extends Applicant


  case class IndependentApplicant(
    name: String,
    numberOfReferees: Int
  ) extends Applicant

  case class ReferredApplicant(
    name: String,
    agencyName: String
  ) extends Applicant


  def main(args: Array[String]): Unit = {

    def process(a: Applicant): Boolean = a match {
      case a: InternalApplicant => true
      case a: IndependentApplicant => false
      case a: ReferredApplicant => false
    }

    // we can use the subtype in a function that accept
    process(InternalApplicant("Jake",3))
    process(IndependentApplicant("Jake",1))
    process(ReferredApplicant("Jake","Yesterday Agency"))
  }
}


  // Notes

// Parametric polymorphism: generic
// Subtyping polymorphism: using inheritance / extension to limit the type i.e P<:Applicant
// Adhoc polymorphism: implicit, function and operator overloading
// Type class support AdHoc polymorphism

// Variance and covariance only applies on a wrapper type e.g. Box[] as a custom list
// Function that accept parent class as opposed to the child class. That's not variance / covariance
