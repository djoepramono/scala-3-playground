package playground.evaluationStrategy

import scala.annotation.tailrec
import java.lang.Thread.State


// So when do we think that call-by-name + lazy val is useful?
// I cannot think of a straight forward example, but let's see the following
// Let's assume StateManager class below is supplied by third party library outside of our control
class StateManager() {
    println("slowly constructing StateManager");
    Thread.sleep(5000);
    var state: Int = 0;    

    def updateState(input: Int): Unit = {
        println(s"updating state to $input");
        Thread.sleep(1000);
        this.state = input;        
    }
}

object recursiveFunction {

    // And for whatever reason, we are going to pass in that StateManager
    // Say we have function that add all natural numbers from a start counter
    //   for example give 3, we would calculate 3+2+1 or 1+2+3
    // This function might update the state every time we executes it based on a boolean toggle
    //   and thus it makes sense call/pass StateManager by name
    def addAllNaturalStartingFrom(counter: Int, isUsingStateManager: Boolean, sm: => StateManager): Int = {        
        if counter == 0 then {
            if isUsingStateManager then sm.updateState(0);
            return 0;
        }
        else {
            // the next line basically prepares the subroutines
            // and thus when it is executed, it'll printout the last prepared subroutines first
            val x = counter + addAllNaturalStartingFrom(counter - 1, isUsingStateManager, sm);
            if isUsingStateManager then sm.updateState(x);
            return x;
        }
    }
    
    def main(args: Array[String]): Unit = {
        
        // we learnt from callByNameOrValue.scala, that passing in a thunk could be dangerous
        // but for the sake of this exerice, let's pass the thunk in: `new StateManager`
        //   instead of passing in StateManager instance/object

        // if the toggle is false, the code run very quickly
        println(addAllNaturalStartingFrom(3, false, new StateManager));

        // but if the toggle is true, the StateManager would be slowly constructed multiple times
        // println(addAllNaturalStartingFrom(3, true, new StateManager));

        // and this is when lazy can help        
        // lazy val mySM = new StateManager;
        // println(addAllNaturalStartingFrom(3, true, mySM));        
    }
    

    // If any of you was wondering if addAllNaturalStartingFrom should use tailrec
    // Well yes, it's possible
    // The problem here though, it seems to always refer to sm even if the boolean is false
    // @tailrec
    // def addAllNaturalStartingFrom(
    //     counter: Int, 
    //     total:Int, 
    //     isUsingStateManager: Boolean, 
    //     sm: => StateManager
    // ): Int = 
    //     counter match {
    //         case 0 => { 
    //             if isUsingStateManager then sm.updateState(total); 
    //             total 
    //         };
    //         case _ => {
    //             if isUsingStateManager then sm.updateState(total); 
    //             addAllNaturalStartingFrom(counter - 1, total + counter, true, sm) 
    //         };
    //     }    

}