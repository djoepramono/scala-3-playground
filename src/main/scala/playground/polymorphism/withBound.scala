package playground.polymorphism

object WithBound {

  // coming back to the playground.polymorphism.Parametric example
  // Group is both contravariant on `add` and covariant in `queue`
  trait Applicant {
    val name: String
  }
  case class InternalApplicant(name: String) extends Applicant
  case class IndependentApplicant(name: String) extends Applicant

  // can we make Group covariant?
  // - introduce a more generic type B, and use it in `add` which is contravariant by nature
  // - change the group type to B if B is supplied
  // - make sure the constructor allow us to set the internal property
  class Group[+A](inputList: List[A] = List.empty) {
    val queue: List[A] = inputList

    def add[B >: A](a: B): Group[B] = {
      new Group(a :: inputList )
    }
  }

  def main(args: Array[String]): Unit = {

    // a Group[Applicant] can have InternalApplicant and IndependentApplicant inside
    // because the subtype can be used in place of the parent type
    val theGroup = Group[Applicant]()
    theGroup.add(InternalApplicant("Jon"))
    theGroup.add(IndependentApplicant("Jess"))

    val internalApplicantGroup = Group[InternalApplicant]()
    val anotherApplicantGroup: Group[Applicant] = internalApplicantGroup

    // :> is also known as bounds, e.g. B :> A, with A being the lower bound
  }

}

