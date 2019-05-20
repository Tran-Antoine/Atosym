# Mask

![alt text](https://media.discordapp.net/attachments/376468898800730116/560199740155887619/icon.png "Logo")

Mask is a free open source library that helps the user dealing with calculations concerning mathematical expressions (in the form of character strings). The main features are the following : 

* Calculation of any series of numbers split by operations (+, -, *, /, ^), giving a reduced result.
* Reduction / Expansion of any polynomial, giving the most reduced result.
* Calculation of an image from any function with the given x / y / ... values
* Calculation of the derivative of any function
* Calculation of the solution(s) of any system of first degree equations given, containing n variables

***

### Build

You will need gradle to build the api. Your build.gradle should have the following lines :

```groovy
repositories {
    maven { url "https://jitpack.io"}
}

dependencies {
    implementation 'com.github.Askigh:Mask:<latest_version_here>'
}
```
See [releases](https://github.com/Askigh/Mask/releases) section for the last release tag

## How to use it

> See the wiki for all the required information. Note that the wiki is incompatible with the master branch, containing the new
architecture. An updated version will come soon

## Documentation

The documentation is available on the repository's description. Note that the API still being at an early stage, the javadoc is quite incomplete for now.

## Also

If you want to check a direct implementation of the Mask library, see [MaskInterface](https://github.com/lolilolulolilol/MaskInterface)
