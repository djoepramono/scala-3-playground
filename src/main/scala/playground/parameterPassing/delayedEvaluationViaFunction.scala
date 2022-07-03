package playground.parameterPassing

object DelayedEvaluationViaFunction {
    
    enum Color:
        case Red, Yellow, Green;

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
        
        // in parameterPassing.scala, we learnt about call-by-name
        // which has the effect of delaying a parameter evaluation until it is referred
        // the benefit of this is when we the parameter passed in have a long evaluation time, then there could be a benefit in delaying it
        println("CALL-BY-NAME VIA OBJECT");
        println(colorToHexCodeByName(slowYellowGenerator()));
        println("----------------");

        // so if we think that call-by-value is easier to reason about, then how do we get a similar effect?
        // it's by creating a function colorToHexCode that accept a function instead of a value
        println("CALL-BY-VALUE VIA FUNCTION");
        println(colorToHexCodeViaFunction(slowYellowGenerator));
        println("----------------");
    }

    // a slow function
    def slowYellowGenerator(): Color = {
        println("creating yellow slowly");
        Thread.sleep(5000);
        return Color.Yellow;
    }

    // passing a function by value, can get the same effect as call-by-name
    // the catch here is that we need to execute the function
    // but we have the control if we want to execute the function multiple times, or save a copy of the result in a variable
    def colorToHexCodeViaFunction(f: () => Color): String = {
        println("start");
        println(s"the input color is: ${f()}");        
        f() match {
            case Color.Red => "#ff0000" ;
            case Color.Yellow => "#ffff00";
            case Color.Green => "#ff0000";
        }
    }

    // closing words
    // just use call-by-value, and if you need to delay use a function
}
