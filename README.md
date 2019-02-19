# Mask

Mask is a free open source library that helps the user dealing with mathematical expressions. The main features (most of them still in development) are the following : 

* Calculation of any series of numbers split by operations (+, -, *, /, ^), giving a reduced result.
* Calculation of any polynomial, giving the most reduced result.
* Calculation of an image from any function with the given x / y / ... values
* Calculation of the derivative of any function
* Calculation of the solution(s) of any equation given, containing n variables

***

### Build

We recommend using gradle for building the api. Your build.gradle should have the following lines :

```groovy
repositories {
    maven { url "https://jitpack.io"}
}

dependencies {
    implementation 'com.github.Askigh:Mask:-SNAPSHOT'
}
```

## How to use it

A mathematical expression is represented by a class called MaskExpression. The following code shows how to create one properly : 
```java
package test;

import net.akami.mask.math.MaskExpression;
import net.akami.mask.math.MaskOperator;

public class MathTest {

    public static void main(String... args) {
  
        // Creates the math expression
        MaskExpression curve = new MathExpression("3x^2 + 2x + 6 + 2y");
        // Prepares the operator. At least the next calculation will be performed from "curve"
        MaskOperator operator = MaskOperator.begin(curve);
        
        /* null indicates that the result of the calculation "imageFor" doesn't need to be saved as a mask.
           Instead, a final temporary variable will contain the result. The boolean indicates that the mask selected
           for calculations need to be changed after the first calculation. In fact, as we want to convert the result to
           an integer, we want the result of the calculation to be the actual mask, so that we can convert it to an
           integer afterwards
        */
        System.out.println(operator.imageFor(null, true, 2, 0).asInt());
        System.out.println(curve);
        
        // The default mask used for calculation is now set to null
        operator.end();
        /* Output : 
        
            22
            3x^2+2x+6
        */
        
        // Let's now imagine we want to change the curve expression, by replacing all the x's by 3.
        
        /* We need to call begin again because we called end() before. If you know that there will be no calculations
           between the last one and this one, you don't need to call end(), neither do you need to call begin() again
        */
        operator.begin(curve);
        operator.imageFor(true, 3);
        
        /* 
        We use 'false' so the mask used for calculations remains 39+2y, not the result of the calculation. 
        We use null again so that it affects the temporary variable, and not the mask itself. 
        When calling asFloat, we need to specify that we want the float value of the temporary variable, 
        by default the method gives us the value of the default mask, here 39+2y. */
        float image1 = operator.imageFor(null, false, 0).asFloat(MaskExpression.TEMP);
        float image2 = operator.imageFor(null, false, 2).asFloat(MaskExpression.TEMP);
        System.out.println(curve);
        System.out.println(image1 + " / " + image2);
        
        // Output : 39+2y
        //          39 / 43
        
        // The expression has indeed been changed. Note that masks are mutable objects, so be careful with constants.
    }
}
```