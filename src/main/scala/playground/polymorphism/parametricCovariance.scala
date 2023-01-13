package playground.polymorphism

import scala.collection.mutable.ListBuffer

object ParametricCovariance {

  // Let's revisit the exercise from playground.polymorphism.Parametric
  // But this time we simplify it a little bit
  // Instead of Group, we are going to have Queue that only has an internal state
  //   i.e. Queue.queue is set via constructor

  trait Applicant {
    val name: String
  }
  case class InternalApplicant(name: String) extends Applicant
  case class IndependentApplicant(name: String) extends Applicant

  def main(args: Array[String]): Unit = {

    // Queue has `+` before A
    abstract class Queue[+A] {
      val queue: List[A]
    }

    val internalApplicantQueue = new Queue[InternalApplicant] {
      val queue: List[InternalApplicant] = List.empty
    }

    // we can use Queue[InternalApplicant] for Queue[Applicant]
    // because the covariant relationship
    val applicantQueue: Queue[Applicant] = internalApplicantQueue

    // what if we go the other way around?
    // using Queue[Applicant] for Queue[InternalApplicant]
    // this would not work, because Queue[Applicant] is not specific enough
    // which is what we want
    val anotherApplicantQueue = new Queue[Applicant] {
      val queue: List[Applicant] = List.empty
    }
    // val anotherInternalApplicantQueue: List[InternalApplicant] = anotherApplicantQueue

    // Covariance
    // InternalApplicant can be used in a place Applicant
    // Queue[InternalApplicant] can be used in a place of Queue[Applicant]
  }
}

