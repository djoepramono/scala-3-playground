package playground.polymorphism

object ParametricWithBound {

  // Also known as generics
  // Usually the works happens on the wrapper type

  trait Applicant {
    val name: String
  }
  case class InternalApplicant(name: String) extends Applicant
  case class IndependentApplicant(name: String) extends Applicant

  class Group[+A](inputList: List[A] = List.empty) {
    val queue: List[A] = inputList

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

    // now we can use Group[InternalApplicant] in place of Group[Applicant]
    val internalApplicantGroup = Group[InternalApplicant]()
    val anotherApplicantGroup: Group[Applicant] = internalApplicantGroup

    // the above code works because instead of being invariant, now Group is covariant
    // the trick here is to change `add` which is covariant by nature
    //   to accept another type B which is a supertype of A
    // it also return a new Group where that new type B and thus satisfy the covariant requirement
  }




}

