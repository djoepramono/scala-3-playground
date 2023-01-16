# Guild Talk

1. Polymorphism
- There are _sort of_ two major polymorphism classifications: parametric and adhoc
- But they are not exclusive to each other
- In OO, when people mention polymorphism,_more often than not_, it actually refers to subtype polymorphism

2. Subtype Polymorphism
- Inheritance based polymorphism
- Liskov Substitution Principle, subclasses should be substitutable for their base class
- This is where things like invariance, covariance, and contravariance type might happen

3. Parametric Polymorphism
- Usually the works happen at wrapper type
- Also known as generics
- e.g. List[A] and you can reverse a list doesn't matter what A is
- In Scala, this wrapper type is invariant in A by default

4. Covariance
- It happens when the the wrapper type has a property that is of type A
- Analogy: a dog or a cat lover can be considered as an animal lover

5. Contravariance
- It happens when the wrapper type has a function that is accepting A as parameter
- Analogy: a vet can check your dogs and cats, but a cat specialist cannot check your dogs.

6. Upper / Lower bound
- Since subtyping exists, there's a concept of lower bound and upper bound
- Analogy: you can train the cat specialist to be able to treat dog, but then the cat specialist becomes a vet.

7. Adhoc Polymorphism
- Depending on the type parameters, the implementation might be different
- Also known as function overloading
- Depending on what A is, it would need to be sorted / decoded differently
- Type class is the ultimate adhoc polymorphism ~ George 2017
- In Scala, implicits are used to mimic Haskell type class implementation. It's more of a design pattern
- Implicits is affected by the scope / imports

8. Implicits is also used as contextual abstractions
- Omitting repetitive parameters i.e. context parameters
- The context parameters still can be passed explicitly via `using`
