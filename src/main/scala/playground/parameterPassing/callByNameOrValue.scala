package playground.parameterPassing

object CallByNameOrValue {

    enum Color:
        case Red, Yellow, Green;
    
    // Call by value
    // The argument is evaluated first
    def colorToHexCodeByValue(c: Color): String = {
        println("start");
        println(s"the input color is: $c");
        c match {
            case Color.Red => "#ff0000" ;
            case Color.Yellow => "#ffff00";
            case Color.Green => "#ff0000";
        }
    }

    // Call by name
    // The argument is substituted into the function body
    def colorToHexCodeByName(c: => Color): String = {
        println("start");
        println(s"the input color is: $c");
        c match {
            case Color.Red => "#ff0000" ;
            case Color.Yellow => "#ffff00";
            case Color.Green => "#ff0000";
        }
    }


    def main(args: Array[String]): Unit = {
        // passing in an already evaluated parameter
        println("PASSING IN AN EVALUATED PARAMETER");
        println(colorToHexCodeByValue(Color.Red));
        println(colorToHexCodeByName(Color.Red));
        println("----------------");

        // passing in a thunk
        // thunk = a set of instructions that is returned as part of a function. Basically the code within {}
        // in other interpretation, thunk = a zero argument function to delay the a block of code from running
        println("PASSING IN A THUNK");
        println(colorToHexCodeByValue({println("beep"); Color.Red}));
        println(colorToHexCodeByName({println("beep"); Color.Red}));
        println("----------------");

        // passing in an executed function
        // it's the same as passing in a thunk
        println("PASSING IN EXECUTED FUNCTION");
        val redGenerator = () => { println("beep"); Color.Red };
        println(colorToHexCodeByValue(redGenerator()));
        println(colorToHexCodeByName(redGenerator()));
        println("----------------");

        // passing in a lazy val
        // this is the closest implementation to call by needs in Scala
        // it is not cheap as there are implicit bandaid to suppress the side effect behind the hood        
        println("PASSING IN LAZY VAL");
        lazy val red = { println("beep"); Color.Red } ;
        println(colorToHexCodeByValue(red));        
        println(colorToHexCodeByName(red));
        println("----------------");
    }
    
    // so which one is better? At a glance, it depends
    // the problem here is that parameters in Scala can perform side effects
    // if the parameters do not have a side effect, it's better to use call-by-value. Safe and faster to compute    
    // if the parameters perform a side effect, then it's about when we want to execute the side effect
    //   - if we want to perform side effect before the rest of the code in the function, then use call-by-value
    //   - if we want to perform side effect whenever we refer to it, then we use call-by-name
    //   - if we want to perform side effect only once, when we first refer to it, the we use lazy val    

    // so mathematically how do we want to categorise this parameter passing evaluation in Scala
    // based on when to evaluate the parameter, there are two: non strict (call-by-name) vs strict (call-by-value)
    // both strict and non strict can accept lazy parameter and evaluated parameter
    // however strict is arguably more easy to reason about. Because any parameters would always be evaluated first before the function body

    // how about call-by-reference?
    // it's not about when to evaluate the parameter, so technically it's out of this category / context
    // call-by-reference is more on whether 
    //   - you want to have a mutable parameter (a pointer to a value in memory) 
    //   - or a copied parameter (a literal copy in memory)    
    // confusingly the opposite of call-by-reference and call-by-name are both called call-by-value. But these two call-by-value, are not the same thing.
    // in Scala, all parameter passed in a val, and thus immutable. So we do not have a way of call-by-reference
}