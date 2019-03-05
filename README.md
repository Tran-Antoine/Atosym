# Mask

Mask is a free open source library that helps the user dealing with mathematical expressions. The main features (most of them still in development) are the following : 

* Calculation of any series of numbers split by operations (+, -, *, /, ^), giving a reduced result.
* Calculation of any polynomial, giving the most reduced result.
* Calculation of an image from any function with the given x / y / ... values
* Calculation of the derivative of any function
* Calculation of the solution(s) of any equation given, containing n variables

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

## How to use it

> See the wiki for all the required information