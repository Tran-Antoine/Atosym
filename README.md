<p align="center">
  <img width="600" height="250" src="https://i.imgur.com/7EpsvTB.png">
</p>
======

**Atosym** is a free open source parser that helps the user deal with calculations concerning algebraic expressions. The main features are the following : 

* Calculation of any series of numbers split by operations (+, -, *, /, ^), giving a reduced result.
* Evaluation of any algebraic expression, giving the most simplified result.
* Calculation of images from any function with the given x / y / ... values
* Differentiation of any function
* Support for functions such as `cos()` or `sqrt()`
* Customization for calculations, such as angle unit or expansion limit
***

### Build

Gradle :

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

Maven :

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

## How to use it

> The Wiki would be a great place to start.  <br>
 Note that the french wiki is incompatible with the master branch, containing a different
architecture. An updated version will come soon. <br>
You might see the term "Mask-algebra" used, which simply
 refers to "Atosym", "Mask-algebra" being the former name of the library.

## Documentation

The documentation is available on the repository's description. Note that the API still being at an early stage, the javadoc is quite incomplete for now.

## Also

If you want to check a direct implementation of the Atosym library, see [MaskInterface](https://github.com/lolilolulolilol/MaskInterface)
