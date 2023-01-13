package playground.polymorphism

trait Applicant {
  val name: String
}
case class InternalApplicant(name: String) extends Applicant
case class IndependentApplicant(name: String) extends Applicant

object Implicit {

  // similar to abstract class in the previous example, we are going to use trait as type class
  trait Processor[A] {
    def process(a: A): Boolean
  }

  // the difference here is, we are using given
  object Processor {
    // in Scala 3, we can only have 1 instance of Processor[InternalApplicant]
    // in Scala 2, this is not the case, and sometimes we cannot tell which instance will be used
    given Processor[InternalApplicant] with
      def process(a: InternalApplicant): Boolean = true
    given Processor[IndependentApplicant] with
      def process(a: IndependentApplicant): Boolean = true
  }

  // using here is also known as context parameter
  // in other words process accept a:A but A has to be processable i.e. have Processor[A]
  // using doesn't need a name but personally for me it's easier to read if there's a name
  // without giving it a name e.g. processor, we would need to summon the implicit
  def process[A](a :A)(using processor: Processor[A]): Boolean = {
    processor.process(a)
  }

  def main(args: Array[String]): Unit = {

    // type class
    // whether you realised it or not, we actually already used type class*
    // not the actual type class e.g. Haskell's one but a similar concept to that with trait Processor[A]

    // now process can be called eventhough the second params: processor are not passed in
    println(process(InternalApplicant("Jill")))


    // not sure which implicit would be use?
    // you can summon the implicit type class and even use it directly
    val y = summon[Processor[InternalApplicant]]
    println(y.process(InternalApplicant("Jack")))
  }
}
