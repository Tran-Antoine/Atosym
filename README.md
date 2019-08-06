<p align="center">
  <img width="600" height="320" src="https://i.imgur.com/7EpsvTB.png">
</p>

**Atosym** is a free open source parser that helps the user deal with calculations concerning **algebraic expressions**. Build a calculator, improve your algorithms or compute entity trajectories in your game, everything related to algebra can be simpler using this tool !
***

Table of contents
=====================

* [Features](#features) <br>
*An exhaustive list of the principal aspects of the library* <br>

* [Build](#build) <br>
	* Gradle
	* Maven
* [How to use Atosym](#how-to-use-atosym) <br>
	* Wiki
	* General remarks on **how** and **why** use or not use Atosym
* [Documentation](#documentation)
* [See also](#also) <br>
*A direct implementation of the tool through a calculator*

***

## Features

#### Calculation of any series of numbers split by different operators

Basically, it acts as a classic numeric calculator. With a given entry, the library outputs the number that the entry equals to. <br>
Entries support: 

* Arabic numbers, fitting the specified precision. Integers and decimal numbers are both supported 
* Basic operators : `+` for sums, `-` for subtractions, `*` for multiplications, `/` for divisions, `^` for powers
* Parenthesis, ruling the priority of operations over natural priority (`^` > `*` & `/` > `+` & `-`) 
* Mathematical functions with one or several parameters, such as `cos(value)` or `root(value, base)`
***

#### Evaluation of any algebraic expression, giving the most simplified result

It evaluates expressions containing **unknowns** (or **variables**). Results are not necessarily numbers, therefore the **most simplified** expression is returned.

In addition to the previous feature, entries support:

* Litteral characters, uppercase or lowercase. The supported characters list can be extended or restricted by changing the [ValidityChecks](https://tran-antoine.github.io/Atosym/javadoc/index.html?net/akami/mask/check/ValidityCheck.html)
***

#### Images calculation of any function from given values for given unknowns

It replaces defined unknowns by `litteral` or `numeric` values. <br>
Entries support exactly what the previous feature does. Additionally, they require mapped unknowns and values. 
***

#### Differentiation of functions

It computes the derivative of a given function. Entries are given the same way as for evaluating algebraic expressions. <br>
Additionally, the unknown of the function must be specified. In a differentiation context, functions have a single unknown, and other **non-numeric** elements are considered as constants.
***

#### Customization of calculations

Depending on the context and the need of the user, a single calculation might result in several different outcomes. For instance, simplifying `cos(90)` might outputs `0` or `-0.448074` depending on the unit chosen (degrees for the first result, radians for the second one)
As another example, expansion might want to be limited, thereby simplifying `(a+b)^100` may output `(a+b)^100` or `a^100 + b^100 + 100a^99b + ...` <br>

Therefore, Atosym allows calculation customization through [MaskContexts](https://tran-antoine.github.io/Atosym/javadoc/index.html?net/akami/mask/core/MaskContext.html). See [How to use Atosym](#how-to-use-atosym) for further information about calculation environments.
***

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
Note that the french wiki is incompatible with the master branch, containing a different
architecture. An updated version will come soon. <br>
You might see the term "Mask-algebra" used, which simply
refers to "Atosym", "Mask-algebra" being the former name of the library.
***

## Documentation

The documentation is available on the repository's description. Note that the API still being at an early stage, the javadoc is quite incomplete for now.
***

## Also

For a direct implementation of the Atosym library, see [MaskInterface](https://github.com/lolilolulolilol/MaskInterface). <br>
**MaskInterface** is a GUI based calculator that implements the following features of the library:

* [Calculation of any series of numbers split by different operators](#calculation-of-any-series-of-numbers-split-by-different-operators)
* [Evaluation of any algebraic expression, giving the most simplified result](#evaluation-of-any-algebraic-expression-giving-the-most-simplified-result)
* [Images calculation of any function from given values for given unknowns](#images-calculation-of-any-function-from-given-values-for-given-unknowns)
* [Differentiation of functions](#differentiation-of-functions)
<br>

Calculations customization is not supported by **MaskInterface**. <br>
:warning: Angles are assumed to be given in radians
