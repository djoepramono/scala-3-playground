package playground.polymorphism

object ParametricWithBound {

  // Coming back to the playground.polymorphism.Parametric example
  // Can we make Group covariant?
  trait Applicant {
    val name: String
  }
  case class InternalApplicant(name: String) extends Applicant
  case class IndependentApplicant(name: String) extends Applicant

  class Group[+A](inputList: List[A] = List.empty) {
    val queue: List[A] = inputList

    // this line has two tricks
    // - introduce a more generic type B
    // - change the group type to B if B is supplied
    def add[B >: A](a: B): Group[B] = {
      new Group(a :: inputList )
    }
  }

  def main(args: Array[String]): Unit = {

    // A Group of Applicants can have InternalApplicant and IndependentApplicant inside
    // because the subtype can be used in place of the parent type
    val theGroup = Group[Applicant]()
    theGroup.add(InternalApplicant("Jon"))
    theGroup.add(IndependentApplicant("Jess"))

    // for Group[InternalApplicant] to be able to be used in place of Group[Applicant], it needs to be able to accept Applicant
    // the trick here is to change `add` which is contravariant by nature
    //   to accept another type B which is a supertype of A
    // it also return a new Group where that new type B and thus satisfy the covariant requirement
    val internalApplicantGroup = Group[InternalApplicant]()
    val anotherApplicantGroup: Group[Applicant] = internalApplicantGroup

    // :> is also known as bounds, e.g. B :> A, with A being the lower bound
  }

}

