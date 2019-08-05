# Atosym (A TOol for SYmbolic Math)

![alt text](https://media.discordapp.net/attachments/376468898800730116/560199740155887619/icon.png "Logo")

Atosym is a free open source parser that helps the user dealing with calculations concerning algebraic expressions (in the form of strings). The main features are the following : 

* Calculation of any series of numbers split by operations (+, -, *, /, ^), giving a reduced result.
* Reduction / Expansion of any algebraic expression, giving the most simplified result.
* Calculation of an image from any function with the given x / y / ... values
* Calculation of the derivative of any function
* Calculation of the solution(s) of any system of first degree equations given, containing n variables (not functional yet)
* Functions such as `cos()` or `sqrt()` are supported

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
	    <groupId>com.github.User</groupId>
	    <artifactId>Repo</artifactId>
	    <version>Tag</version>
	</dependency>
```
See the [releases](https://github.com/Askigh/Mask/releases) section for the last release tag

## How to use it

> See the wiki for all the required information. You might see the term "Mask-algebra" used, which simply
 refers to "Atosym", "Mask-algebra" being the former name of the library. <br>
 Note that the french wiki is incompatible with the master branch, containing a different
architecture. An updated version will come soon.

## Also

If you want to check a direct implementation of the Atosym library, see [MaskInterface](https://github.com/lolilolulolilol/MaskInterface)
