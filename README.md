# HijrahDateTime

[![Kotlin Alpha](https://kotl.in/badges/beta.svg)]()
[![Kotlin](https://img.shields.io/badge/kotlin-2.0-blue.svg?logo=kotlin)]()
[![Java](https://img.shields.io/badge/java-17-orange.svg?logo=java)]()
[![Test Coverage](https://s3.amazonaws.com/assets.coveralls.io/badges/coveralls_90.svg)]()

HijrahDateTime is a Kotlin/JVM library that is built on top of java.time to facilitates work with Hijrah date and time APIs.

See [Using in your projects](#using-in-your-projects) for the instructions how to setup a dependency in your project.



## Purpose

The Java.time API provides a comprehensive set of classes for working with date and time and shortcuts for working with them, abstracting the complexity of the date and time calculations.
However, this is only limited to the georgian calendar. Although the API provides some support for the Hijrah calendar, it's not enough to work with it effectively.
Some features are missing, while others are available but not straightforward to use and require a deeper understanding of Java.time APIs in order to use them, and there are almost no code shortcuts compared with the existing ISO calendar classes and functions.
However, the Hijrah calendar is the main calendar used in daily life in the Islamic world, and it's important to have a comprehensive set of classes and functions to work with it effectively.

The HijrahDateTime library aims to fill this gap and provide a comprehensive set of classes and functions to work with the Hijrah calendar effectively, and to provide shortcuts for working with the Hijrah calendar as there are for the ISO calendar in the java.time API.
Abstracting the work with the Hijrah calendar, and providing shortcuts for working with it, will make it easier to work with the Hijrah calendar, and will make the development process faster and more efficient, and will reduce the complexity of the code, by the well of Allah.




## Types

### Classes

The library provides a basic set of classes for working with date and time:

- `HijrahDateTime` to represent hijrah date and time components without a reference to the particular time zone. It's similar to java `LocalDateTime` except that it uses the `HijrahDate` instead of `LocalDate`.

- `ZonedHijrahDateTime` to represent hijrah date and time components with a reference to the particular time zone. It's similar to java `ZonedDateTime` except that it uses the `HijrahDateTime` instead of `LocalDateTime`.

- `OffsetHijrahDateTime` to represent hijrah date and time components with a fixed offset from UTC. It's similar to java `OffsetDateTime` except that it uses the `HijrahDateTime` instead of `LocalDateTime`.

- `HijrahMonth` to represent the hijrah months in a descriptive way.


### Extensions

The library provides a set of extensions for the java.time APIs to facilitate the work on hijrah dates and times. For example:

- the library provides extensions for the `HijrahDate` to integrate it with this library's classes and functions.
- the library provides extensions for the `Instant` to integrate it with this library's classes and functions.
- the library provides extensions for the `LocalDate` class to convert it to `HijrahDate`. Same for other classes.

For java developers, you can access those extensions by using the `HijrahDates`, `Instants`, `DateTimesConversions` singleton objects.
More extensions to the java.time classes are planned to be added in the next releases, by the well of Allah.



## Hijrah Formatters

### Built-in formatters

The library provides `HijrahFormats` singleton object, which has a set of built-in hijrah datetime formatters for parsing and formatting strings.
Some of those formatters are:

- `HIJRAH_DATE` to parse and format the hijrah date in the format `yyyy-MM-dd`, which the main format for the `HijrahDate` class. For example, `1446-10-12`.

- `HIJRAH_DATE_TIME` to parse and format the hijrah date and time in the format `yyyy-MM-ddTHH:mm:ss`, which the main format for the `HijrahDateTime` class. For example, `1446-10-12T12:30:00`.

- `HIJRAH_OFFSET_DATE_TIME` to parse and format the hijrah date and time with the offset in the format `yyyy-MM-ddTHH:mm:ssZ`, which the main format for the `OffsetHijrahDateTime` class. For example, `1446-10-12T12:30:00+03:00`.

- `HIJRAH_ZONED_DATE_TIME` to parse and format the hijrah date and time with the time zone in the format `yyyy-MM-ddTHH:mm:ss[.SSS]Z`, which the main format for the `ZonedHijrahDateTime` class. For example, `1446-10-12T12:30:00+03:00[Asia/Riyadh]`.



### Working with other string formats

When some data needs to be formatted in another format, you can define your own format, either by using the `DateTimeFormatterBuilder` class or by using the `DateTimeFormatter.ofPattern()`.
For example:



#### Using the `DateTimeFormatterBuilder` class

```kotlin
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.SignStyle
import java.time.temporal.ChronoField

val dateFormat: DateTimeFormatter = DateTimeFormatterBuilder()
  .appendValue(ChronoField.YEAR, 4, 4, SignStyle.EXCEEDS_PAD)
  .appendLiteral('/')
  .appendValue(ChronoField.MONTH_OF_YEAR, 2)
  .appendLiteral('/')
  .appendValue(ChronoField.DAY_OF_MONTH, 2)
  .toFormatter()
  .withChronology(HijrahChronology.INSTANCE) // Very important to use the Hijrah Chronology.

val date: HijrahDate = dateFormat.parse("1446/10/12", HijrahDate::from)
println(date.format(dateFormat)) // "1446/10/12"
```

#### Using the `DateTimeFormatter.ofPattern()` function

```kotlin
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter

val dateFormat: DateTimeFormatter = DateTimeFormatter
    .ofPattern("yyyy/MM/dd")
    .withChronology(HijrahChronology.INSTANCE) // Very important to use the Hijrah Chronology.

val date: HijrahDate = dateFormat.parse("1446/10/12", HijrahDate::from)
println(date.format(dateFormat)) // "1446/10/12"
```




## Serialization

### Using kotlinx.serialization

The library provides serialization support with kotlinx.serialization library. Simply, all the new datetime classes are serializable by default.
For `HijrahDate` class, since it's java built-in class, the library provides a custom serializer for it; Which is `HijrahDateSerializer`. Use it to serialize and deserialize the `HijrahDate` class.



### Using java serialization

The library provides serialization support with java serialization. You can serialize and deserialize the datetime classes using the java serialization API without any additional configuration.




## Using in your projects

> Note that the library is experimental, and the API is subject to change.
> However, the library is built on top of the Java Time API, and errors are not expected to occur in the core functionality. You can use it in your projects, and if you face any issues or incorrect behavior, please report them.


The library is published to Maven Central.


### Gradle

- Add the Maven Central repository if it is not already there:

```kotlin
repositories {
    mavenCentral()
}
```


- Add a dependency to the `dependencies` block:

#### Using Gradle Kotlin DSL
```kotlin build.gradle.kts
dependencies {
    implementation("com.abdulrahman-b:HijrahDateTime:1.0.0-beta.1")
}
```

#### Using Gradle Groovy DSL
```groovy build.gradle
dependencies {
    implementation 'com.abdulrahman-b:HijrahDateTime:1.0.0-beta.1'
}
```

### Maven

Add a dependency to the `<dependencies>` element:

```xml pom.xml
<dependency>
    <groupId>com.abdulrahman-b</groupId>
    <artifactId>HijrahDateTime</artifactId>
    <version>1.0.0-beta.1</version>
</dependency>
```

## Building

The library is compatible with the Kotlin Standard Library not lower than `2.0`.
The library also requires JDK 14 or higher to build and run the dependencies.
