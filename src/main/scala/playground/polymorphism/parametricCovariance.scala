package playground.polymorphism

import scala.collection.mutable.ListBuffer

object ParametricCovariance {

  trait Applicant {
    val name: String
  }
  case class InternalApplicant(name: String) extends Applicant
  case class IndependentApplicant(name: String) extends Applicant

  def main(args: Array[String]): Unit = {

    // let's create a class that uses the type parameter
    // but this time instead of a method, it's a property
    abstract class Queue[+A] {
      val queue: List[A]
    }

    // now let's create the implementation in a class targetting the child type
    val internalApplicantQueue = new Queue[InternalApplicant] {
      val queue: List[InternalApplicant] = List(InternalApplicant("Jack"))
    }

    // we can use InternalApplicantQueue for ApplicantQueue
    // because the covariant relationship
    val applicantQueue: Queue[Applicant] = internalApplicantQueue

    // but just like the contravariance example, it's not possible to ApplicantQueue on InternalApplicantQueue


    val anotherApplicantQueue = new Queue[Applicant] {
      val queue: List[Applicant] = List.empty
    }

    // because it is not specific enough
    //val anotherInternalApplicantQueue: List[InternalApplicant] = anotherApplicantQueue

    // Covariance
    // InternalApplicant can be used in a place Applicant
    // Queue[InternalApplicant] can be used in a place of Queue[Applicant]
  }
}

