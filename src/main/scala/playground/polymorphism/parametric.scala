package playground.polymorphism

object Parametric {

  // Parametric polymorphism, also known as generics
  // Usually the works happens on the wrapper type

  trait Applicant {
    val name: String
  }
  case class InternalApplicant(name: String) extends Applicant
  case class IndependentApplicant(name: String) extends Applicant

  class Group[A] {
    var queue: List[A] = List.empty

    def add(a: A): Unit = {
      queue = queue :+ a
    }
  }

  def main(args: Array[String]): Unit = {

    // A Group of Applicants can have InternalApplicant and IndependentApplicant inside
    // because the subtype can be used in place of the parent type
    val theGroup = Group[Applicant]
    theGroup.add(InternalApplicant("Jon"))
    theGroup.add(IndependentApplicant("Jess"))

    // Can we use a Group[InternalApplicant] in place of Group[Applicant] ?
    // Some of us might think that this should be fine
    // but we cannot use Group[InternalApplicant] in place of Group[Applicant]
    val internalApplicantGroup = Group[InternalApplicant]
    // val anotherApplicantGroup: Group[Applicant] = internalApplicantGroup //this won't work

    // Group is invariant in A.
    // Group[InternalApplicant] is not a subtype of Group[Applicant]
    // eventhough InternalApplicant is a subtype of Applicant
    // Invariance in Scala is the default behaviour
  }




}

