package playground.polymorphism

object Parametric {

  trait Applicant {
    val name: String
  }
  case class InternalApplicant(name: String) extends Applicant
  case class ExternalApplicant(name: String) extends Applicant

  // lets create a custom class: Group[A]
  // it has both a property and a method `add` that utilise A
  class Group[A] {
    var queue: List[A] = List.empty

    def add(a: A): Unit = {
      queue = queue :+ a
    }
  }

  def main(args: Array[String]): Unit = {

    // A Group[Applicant] can have InternalApplicant and IndependentApplicant inside
    // because the subtype can be used in place of the parent type
    val theGroup = Group[Applicant]
    theGroup.add(InternalApplicant("Jon"))
    theGroup.add(ExternalApplicant("Jess"))

    // Can we use a Group[InternalApplicant] in place of Group[Applicant] ?
    // Some of us might think that this should be fine
    // but we cannot use Group[InternalApplicant] in place of Group[Applicant]
    val internalApplicantGroup = Group[InternalApplicant]
    // val applicantGroup: Group[Applicant] = internalApplicantGroup

    // Group is invariant in A.
    // Group[InternalApplicant] is not a subtype of Group[Applicant]
    // eventhough InternalApplicant is a subtype of Applicant
    // Invariance in Scala is the default behaviour
  }

}

