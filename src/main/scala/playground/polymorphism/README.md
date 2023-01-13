# Parametric Polymorphism

- Doesn't matter which type parameter it has, the function that comes with the type is implemented in the same way
- In general things happen in the wrapper type, e.g. reversing a list
- Sometimes known as generics

# Adhoc Polymorphism

- Depending on the type parameter, the function that comes with the type may have different implementation
- This different implementation usually supplied adhocly along with the type
- The type parameter matters more e.g. json decoder. Decoding class A and class B might be different
- Sometimes known as function overloading

# Subtype Polymorphism

- If you come from OO though, there's also subtype polymorphism, which heavily depends on inheritance
- This is where things like invariance, covariance, and contravariance type might happen

# Type Class

- Dubbed as the ultimate adhoc polymorphism by George
- It's more flexible that interface, because you cannot extend a class with interface if you don't control the class. But you can extend that class with type class.
- In scala this is not the case, as type class is more of the design pattern, done via implicits
- Implicits is affected by the scope / imports
