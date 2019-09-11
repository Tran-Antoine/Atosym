<p align="center">
  <img width="600" height="320" src="https://i.imgur.com/7EpsvTB.png">
</p>

**Atosym** is a free open source parser that helps the user deal with calculations concerning **algebraic expressions**. Build a calculator, improve your algorithms or even compute entity trajectories in your game, everything related to algebra can be simpler with this tool !
***

Table of contents
=====================

* [Features](#features) <br>
*An exhaustive list of the principal aspects of the library* <br>

* [Sample](#sample) <br>
*A code example to illustrate the tool*

* [Build](#build) <br>
	* Gradle
	* Maven
* [How to use Atosym](#how-to-use-atosym) <br>
	* Wiki
* [Documentation](#documentation)
* [See also](#also) <br>
*A direct implementation of the tool through a calculator*

***

## Features

#### Calculation of any series of numbers split by different operators

Basically, it acts as a classic numeric calculator. With a given entry, the library outputs the number that the entry equals. <br>
Entries support: 

* Arabic numbers, fitting the specified precision. Integers and decimal numbers are both supported 
* Basic operators : `+` for sums, `-` for subtractions, `*` for multiplications, `/` for divisions, `^` for powers
* Parenthesis, ruling the priority of operations over natural priority (`^` > `*` & `/` > `+` & `-`) 
* Mathematical functions with one or several parameters, such as `cos(value)` or `root(value, base)`
***

#### Evaluation of any algebraic expression, giving the most simplified result

It evaluates expressions containing **unknowns** (or **variables**). Results are not necessarily numbers, therefore the **most simplified** expression is returned.

In addition to the previous feature, entries support:

* Literal characters, uppercase or lowercase. The supported characters list can be extended or restricted by changing the [ValidityChecks](https://tran-antoine.github.io/Atosym/javadoc/index.html?net/akami/mask/check/ValidityCheck.html)
***

#### Image calculations of any function from given values for given unknowns

It replaces defined unknowns by `litteral` or `numeric` values. <br>
Entries support exactly what the previous feature does. Additionally, they require mapped unknowns and values. 
***

#### Differentiation of functions

It computes the derivative of a given function. Entries are given the same way as for evaluating algebraic expressions. <br>
Additionally, the unknown of the function must be specified. In a differentiation context, functions have a single unknown, and other **non-numeric** elements are considered as constants.
***

#### Customization of calculations

Depending on the context and the need of the user, a single calculation might result in several different outcomes. For instance, simplifying `cos(90)` might outputs `0` or `-0.448074` depending on the unit chosen (degrees for the first result, radians for the second one)
As another example, expansions might want to be limited, thereby simplifying `(a+b)^100` may output `(a+b)^100` or `a^100 + b^100 + 100a^99b + ...` <br>

Therefore, Atosym allows calculation customization through [MaskContexts](https://tran-antoine.github.io/Atosym/javadoc/index.html?net/akami/mask/core/MaskContext.html). See [How to use Atosym](#how-to-use-atosym) for further information about calculation environments.
***

## Sample

The following code serves as a quick example to show how a series of calculations using **Atosym** would look like. See the [how to use Atosym](#how-to-use-atosym) section for further information. <br>

```java
public class AtosymDemo {

    public static void main(String[] args) {

        // Creates 3 objects handling various expressions
        Mask mask1 = new Mask("cos(90) - a/b/c/d");
        Mask mask2 = new Mask("(a+b)^100.0 + (a+b)^3");
        Mask mask3 = new Mask("(3-x)^3 + 3x - 12x^2 - 2x^2");

        MaskOperatorHandler handler = generateHandler();
        sample1(mask1, handler);
        sample2(mask2, handler);
        sample3(mask3, handler);
    }

    private static MaskOperatorHandler generateHandler() {
        // Initializing a calculation environment to customize the calculations
        MaskContext context = new MaskContext();
        // Alteration #1 : Changes angles to degrees
        context.addGlobalModifier(new DegreeUnit(), AngleUnitDependent.class);
        // Alteration #2 : Stops (...)^x expansions when x is over 10
        context.addGlobalCanceller(new PowExpansionLimit(10, context), PowerCalculator.class);
        // Alteration #3 : Adds a cache that store the results from the previous calculations to increase the performances
        // This alteration must be duplicated, which means that every object computing results must have its own cache
        context.addClonedCanceller(CalculationCache::new, BinaryOperationHandler.class);
        return new MaskOperatorHandler(context);
    }

    private static void sample1(Mask input, MaskOperatorHandler handler) {
        // Stores the simplified result to the temporary Mask instance, in order to get its string value
        String result1 = handler
                .compute(MaskSimplifier.class, input, Mask.TEMP, null)
                .asExpression();
        // cos(90) indeed gives 0 since we set the angle unit to degrees
        System.out.printf("%s = %s\n", input.getExpression(), result1);
    }

    private static void sample2(Mask input, MaskOperatorHandler handler) {
        String result2 = handler
                // "begin(Mask)" is an alternative to putting (in this case) mask2 before "Mask.TEMP" as in the previous example
                .begin(input)
                .compute(MaskSimplifier.class, Mask.TEMP, null)
                .asExpression();
        // (a+b)^100 is not expanded, whereas (a+b)^3 is, since we set the expansion limit to 10
        System.out.printf("%s = %s\n", input.getExpression(), result2);
    }

    private static void sample3(Mask input, MaskOperatorHandler handler) {
        // Instead of retrieving the string value, we want to store the result of the calculation in a different object
        Mask result3 = new Mask();
        handler
                .begin(input)
                // instead of using Mask.TEMP, we change the mask itself so its new string value is simplified
                .compute(MaskSimplifier.class, input, null)
                // we store the derivative in another object
                .compute(MaskDerivativeCalculator.class, result3, 'x');
        // The left expression is simplified, since we changed the value of "mask3" when we simplified the expression
        System.out.printf("(%s)' = %s", input.getExpression(), result3.getExpression());
    }
}
```
```
Output :

cos(90)-a/b/c/d = -a/(bcd)
(a+b)^100.0+(a+b)^3 = (a+b)^100.0+a^3.0+b^3.0+3.0a^2.0b+3.0b^2.0a
(-x^3.0-5.0x^2.0-24.0x+27.0)' = -3.0x^2.0-10.0x-24.0
```

## Build

**Gradle :**

```groovy
repositories {
    maven { url "https://jitpack.io"}
}
```
```groovy
dependencies {
    implementation 'com.github.Tran-Antoine:Atosym:latest_version_here'
}
```

**Maven :**

```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```
```xml
	<dependency>
	    <groupId>com.github.Tran-Antoine</groupId>
	    <artifactId>Atosym</artifactId>
	    <version>latest_version_here</version>
	</dependency>
```
See the [releases](https://github.com/Askigh/Mask/releases) section for the last release tag

***
## How to use Atosym

> The [Wiki](https://github.com/Tran-Antoine/Atosym/wiki) is a great place to start.  <br>

You'll find various pieces of information explaining how to properly use the library, as well as how to customize or add features to it.
You might want to add different operators which are not implemented, such as function integration, or even new customizations for calculations, a limit for "long numerators" division for example. <br>

Several examples of what to do or what not to do are also provided.

:warning: The french wiki is incompatible with the master branch, containing an outdated architecture. An updated version might come soon. <br>
***

## Documentation

The documentation is available on the repository's description. Note that the tool still being at an early stage, the javadoc is not fully complete by now.
***

## Also

For a direct implementation of the Atosym library, look at [MaskInterface](https://github.com/lolilolulolilol/MaskInterface). <br>
**MaskInterface** is a GUI based calculator that implements the following features of the library:

* [Calculation of any series of numbers split by different operators](#calculation-of-any-series-of-numbers-split-by-different-operators)
* [Evaluation of any algebraic expression, giving the most simplified result](#evaluation-of-any-algebraic-expression-giving-the-most-simplified-result)
* [Images calculation of any function from given values for given unknowns](#image-calculations-of-any-function-from-given-values-for-given-unknowns)
* [Differentiation of functions](#differentiation-of-functions)
<br>

Calculations customization is not supported by **MaskInterface**. <br> For instance, be aware that angles are assumed to be given in radians
