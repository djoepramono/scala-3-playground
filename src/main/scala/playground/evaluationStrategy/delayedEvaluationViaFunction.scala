package playground.evaluationStrategy

object DelayedEvaluationViaFunction {

    enum Color:
        case Red, Yellow, Green;

    // in callByNameOrValue.scala, we learnt about call-by-name
    // which has the effect of delaying a parameter evaluation until it is referred
    // the benefit of this is when we the parameter passed in have a long evaluation time, then there could be a benefit in delaying it
    def colorToHexCodeByName(c: => Color): String = {
        println("start");
        println(s"the input color is: $c");
        c match {
            case Color.Red => "#ff0000" ;
            case Color.Yellow => "#ffff00";
            case Color.Green => "#ff0000";
        }
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

    def main(args: Array[String]): Unit = {

        println("================");
        println("CALL-BY-NAME VIA OBJECT");
        println("================");
        println(colorToHexCodeByName(slowYellowGenerator()));
        println("----------------");

        // so if we think that call-by-value is easier to reason about, then how do we get a similar effect?
        // it's by creating a function colorToHexCode that accept a function instead of a value
        println("================");
        println("CALL-BY-VALUE VIA FUNCTION");
        println("================");
        println(colorToHexCodeViaFunction(slowYellowGenerator));
        println("----------------");
    }

    // a slow function
    def slowYellowGenerator(): Color = {
        println("creating yellow slowly");
        Thread.sleep(5000);
        return Color.Yellow;
    }

    // closing words, as a rule of thumbs
    // - use call-by-value whenever possible over call-by-name
    // - if you need to delay an expensive calculation use a function
}
