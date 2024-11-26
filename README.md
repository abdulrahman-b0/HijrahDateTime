# HijrahDateTime

[![Kotlin Beta](https://kotl.in/badges/beta.svg)]()
[![Kotlin](https://img.shields.io/badge/kotlin-2.0-blue.svg?logo=kotlin)]()
[![Java](https://img.shields.io/badge/java-17-orange.svg?logo=java)]()
[![Test Coverage](https://s3.amazonaws.com/assets.coveralls.io/badges/coveralls_99.svg)]()

HijrahDateTime is a Kotlin/JVM library that is built on top of java.time to facilitates work with Hijrah date and time APIs.

See [Using in your projects](#using-in-your-projects) for the instructions how to set up a dependency in your project.



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

- `OffsetHijrahDate` to represent hijrah date components with a fixed offset from UTC. It's a combination of the `LocalHijrahDate` and the `ZoneOffset` classes.

- `HijrahMonth` to represent the hijrah months in a descriptive manner.


### Extensions

The library provides a set of extensions for the java.time APIs to facilitate the work on hijrah dates and times. For example:

- The library provides extensions for the `HijrahDate` to integrate it with this library's classes and functions in `HijrahDates` singleton object.
- The library provides extensions for the `Instant` to integrate it with this library's classes and functions in `Instants` singleton object.

More extensions to the java.time classes are planned to be added in the next releases, by the well of Allah.



## Hijrah Formatters

### Built-in formatters

The library provides `HijrahFormats` singleton object, which has a set of built-in hijrah datetime formatters for parsing and formatting strings.
Some of those formatters are:

- `HIJRAH_DATE` to parse and format the hijrah date in the format `yyyy-MM-dd`, which the main format for the `HijrahDate` class. For example, `1446-10-12`.

- `HIJRAH_DATE_TIME` to parse and format the hijrah date and time in the format `yyyy-MM-ddTHH:mm:ss`, which the main format for the `HijrahDateTime` class. For example, `1446-10-12T12:30:00`.

- `HIJRAH_OFFSET_DATE` to parse and format the hijrah date with the offset in the format `yyyy-MM-ddZ`, which the main format for the `OffsetHijrahDate` class. For example, `1446-10-12+03:00`.

- `HIJRAH_OFFSET_DATE_TIME` to parse and format the hijrah date and time with the offset in the format `yyyy-MM-ddTHH:mm:ssZ`, which the main format for the `OffsetHijrahDateTime` class. For example, `1446-10-12T12:30:00+03:00`.

- `HIJRAH_ZONED_DATE_TIME` to parse and format the hijrah date and time with the time zone in the format `yyyy-MM-ddTHH:mm:ss[.SSS]Z`, which the main format for the `ZonedHijrahDateTime` class. For example, `1446-10-12T12:30:00+03:00[Asia/Riyadh]`.

- `LOCAL_TIME_12_HOURS` to parse and format the time in the 12-hour format in the format `hh:mm:ss a`. For example, `12:30:00 PM`. This also support formatting nano of second if the time has nano of second. For example, `12:30:00.123456789 PM`.


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
  .appendValue(ChronoField.YEAR, 4, 4, SignStyle.NOT_NEGATIVE)
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

#### Using the built-in builder functions

The above two methods are very flexible and allow you to define any format you want.
However, if you want to modify the formatter a bit such as changing the literal from `-` to `/`, or changing the hour format from 24-hour to 12-hour,
it could be boring to define the whole formatters from scratch. For this reason, the library provides a set of builder functions that allow you to build the formatter with a few lines of code.
For example:

```kotlin
import com.abdulrahman_b.hijrahdatetime.formats.HijrahFormatters
import com.abdulrahman_b.hijrahdatetime.LocalHijrahDate

val dateFormat = HijrahFormatters.buildHijrahDateFormatter(separator = "/")
val date: HijrahDate = dateFormat.parse("1446/10/12", HijrahDate::from)
println(date.format(dateFormat)) // "1446/10/12"

val dateTimeFormat = HijrahFormatters.buildHijrahDateTimeFormatter(
  localHijrahDateFormatter = dateFormat,
  datetimeSeparator = ' ', // Change the separator from 'T' to ' '
  timeFormatter= HijrahFormatters.LOCAL_TIME_12_HOURS // Use the 12-hour format
)
val dateTime: HijrahDateTime = dateTimeFormat.parse("1446/10/12 12:30:00 PM", HijrahDateTime::from)
println(dateTime.format(dateTimeFormat)) // "1446/10/12 12:30:00 PM"


```

Other builder functions are available to build the formatters for the other datetime classes. You can find them in the `HijrahFormatters` singleton object.
Formatting done! If you have any questions or issues or a requirement to support another format, please create an issue in the repository.


## Serialization

### Using java serialization

The library provides serialization support with java serialization. You can serialize and deserialize the datetime classes using the java serialization API without any additional configuration.


### Using kotlinx.serialization

The library provides serialization support with kotlinx.serialization library in the artifact `HijrahDateTime-serialization-kotlinx`.
The library provides a list of custom serializers, Each of which use the recommended built-in formatters to serialize and deserialize the datetime classes from and to `String`.
The serializers are:

- `HijrahDateSerializer` to serialize and deserialize the `HijrahDate` class.
- `HijrahDateTimeSerializer` to serialize and deserialize the `HijrahDateTime` class.
- `OffsetHijrahDateSerializer` to serialize and deserialize the `OffsetHijrahDate` class.
- `ZonedHijrahDateTimeSerializer` to serialize and deserialize the `ZonedHijrahDateTime` class.
- `OffsetHijrahDateTimeSerializer` to serialize and deserialize the `OffsetHijrahDateTime` class.

You can either use the serializers directly in your data classes with `@Serializable(with = ...)` annotation or use the contextual serialization to register the serializers at runtime. The latter allows you to customize the format of the serialized string:

```kotlin

fun main() {
    val formatter = DateTimeFormatter
        .ofPattern("yyyy/MM/dd hh:mm:ss a") // Custom format
        .withChronology(HijrahChronology.INSTANCE)

    val json = Json {
        serializersModule = SerializersModule {
            contextual(HijrahDateTime::class, HijrahDateTimeSerializer(formatter)) // Register the serializer with the custom format
        }
    }
    
    val date = HijrahDateTime.of(1446, 10, 12, 12, 30)
    val jsonString: String = json.encodeToString(date)
    println(jsonString) // "1446/10/12 12:30:00 PM"

}
```

You can also add other serializers for the other datetime classes in the same way. Or you can use `HijrahChronoSerializersModule` class that registers all serializers for all library datetime classes, and allows you to customize the formatters. For example:

```kotlin
// Code omitted for brevity
val json = Json {
    serializersModule = HijrahChronoSerializersModule(localHijrahDateTimeFormatter = formatter).get()
}
// Code omitted for brevity
```


### Using Jackson:

The library provides serialization support with Jackson library in the artifact `HijrahDateTime-serialization-jackson`.

The library provides a list of custom serializers and deserializers, Each of which use the recommended built-in formatters to serialize and deserialize the datetime classes from and to `String`.

Since jackson defines separate classes for serializer and deserializer for each type, the library provides the following classes:

- `HijrahDateSerialization.Serializer` to serialize the `HijrahDate` class.
- `HijrahDateSerialization.Deserializer` to deserialize the `HijrahDate` class.
- `HijrahDateTimeSerialization.Serializer` to serialize the `HijrahDateTime` class. 
- `HijrahDateTimeSerialization.Deserializer` to deserialize the `HijrahDateTime` class.
- `ZonedHijrahDateTimeSerialization.Serializer` to serialize the `ZonedHijrahDateTime` class.
- `ZonedHijrahDateTimeSerialization.Deserializer` to deserialize the `ZonedHijrahDateTime` class.
- `OffsetHijrahDateSerialization.Serializer` to serialize the `OffsetHijrahDate` class.
- `OffsetHijrahDateSerialization.Deserializer` to deserialize the `OffsetHijrahDate` class.
- `OffsetHijrahDateTimeSerialization.Serializer` to serialize the `OffsetHijrahDateTime` class.
- `OffsetHijrahDateTimeSerialization.Deserializer` to deserialize the `OffsetHijrahDateTime` class.

For organization purposes, the serializer and deserializer for each type is defined as a nested class.

So, you can use them in your data classes with `@JsonSerialize(using = ...)` and `@JsonDeserialize(using = ...)` annotations,
or you can use the `HijrahChronoSerializersModule` module class that registers all the serializers and deserializers for you, while allowing you to customize the formatters. For example:

```kotlin
import com.abdulrahman_b.hijrahdatetime.serialization.jackson.HijrahChronoSerializationModule
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

fun main() {

    val formatter = DateTimeFormatter
        .ofPattern("yyyy/MM/dd hh:mm:ss a")
        .withChronology(HijrahChronology.INSTANCE)

    val module = HijrahChronoSerializationModule(hijrahDateTimeFormatter = formatter)
    val mapper = ObjectMapper().registerModule(module)

    val data = HijrahDateTime.of(1446, 10, 12, 12, 30)
    val json: String = mapper.writeValueAsString(data)
    println(json) // "1446/10/12 12:30:00 PM"
}
```

Serialization done! If you have any questions or issues or a requirement to support another serialization libraries, please create an issue in the repository.




## Jakarta Bean Validation

The library provides support for Jakarta Bean Validation 3.0 API in the artifact `HijrahDateTime-jakarta-validation`.

The Jakarta Bean Validation API provides a set of standard constraints annotations to validate the classes, including `@Future`, `@Past`, `@FutureOrPresent`, and `@PastOrPresent` annotations.
However, those annotations works only on java.time built-in classes, and they do not work on the hijrah datetime classes provided by this library.

The library artifact `HijrahDateTime-jakarta-validation` adds support for validating the hijrah datetime classes using the standard constraints annotations provided by the Jakarta Bean Validation API.
That means with this library artifact, you can use `@Future`, `@Past`, `@FutureOrPresent`, and `@PastOrPresent` annotations to validate the hijrah datetime classes. Cool, right?

For example, in a Spring boot application, you can use the `@Future` annotation to validate the `HijrahDateTime` class like this:

```kotlin
data class MyData(
    @field:Future //In kotlin, you have to use the field: prefix to apply the annotation to the field.
    val date: HijrahDateTime
)

@Controller("/datetime")
class DateTimeController {

    @PostMapping("/echo")
    fun myEndpoint(@Valid @RequestBody data: MyData): ResponseEntity<MyData> {
        return ResponseEntity.ok(data)
    }
}
```

In the above example, the `@Future` annotation validates the `date` field to ensure that the date is in the future.
If the date is not in the future, the validation will fail, and the Spring Boot will throw a `MethodArgumentNotValidException` exception, which you can handle in the global exception handler or in the controller advice.

Note that the `HijrahDateTime``` class must be serializable to serialize and deserialize request/response body, refer to the [Serialization](#serialization) section for more information on how to serialize the datetime classes.


## Using in your projects

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
    implementation("com.abdulrahman-b:HijrahDateTime:1.0.0-beta.5") //Core library
    implementation("com.abdulrahman-b:HijrahDateTime-serialization-kotlinx:1.0.0-beta.5") //Kotlinx Serialization support
    implementation("com.abdulrahman-b:HijrahDateTime-serialization-jackson:1.0.0-beta.5") //Jackson Serialization support
    implementation("com.abdulrahman-b:HijrahDateTime-jakarta-validation:1.0.0-beta.5") //Jakarta Bean Validation support
}
```

#### Using Gradle Groovy DSL
```groovy build.gradle
dependencies {
    implementation 'com.abdulrahman-b:HijrahDateTime:1.0.0-beta.5' //Core library
    implementation 'com.abdulrahman-b:HijrahDateTime-serialization-kotlinx:1.0.0-beta.5' //Kotlinx Serialization support
    implementation 'com.abdulrahman-b:HijrahDateTime-serialization-jackson:1.0.0-beta.5' //Jackson Serialization support
    implementation 'com.abdulrahman-b:HijrahDateTime-jakarta-validation:1.0.0-beta.5' //Jakarta Bean Validation support
}
```

### Maven

Add a dependency to the `<dependencies>` element:

```xml pom.xml


<dependencies>

    <dependency>
        <groupId>com.abdulrahman-b</groupId>
        <artifactId>HijrahDateTime</artifactId>
        <version>1.0.0-beta.5</version>
    </dependency> <!--Core library-->

    <dependency>
        <groupId>com.abdulrahman-b</groupId>
        <artifactId>HijrahDateTime-serialization-kotlinx</artifactId>
        <version>1.0.0-beta.5</version>
    </dependency> <!--Kotlinx Serialization support-->

    <dependency>
        <groupId>com.abdulrahman-b</groupId>
        <artifactId>HijrahDateTime-serialization-jackson</artifactId>
        <version>1.0.0-beta.5</version>
    </dependency> <!--Jackson Serialization support-->

    <dependency>
        <groupId>com.abdulrahman-b</groupId>

        <artifactId>HijrahDateTime-jakarta-validation</artifactId>
        <version>1.0.0-beta.5</version>
    </dependency> <!--Jakarta Bean Validation support-->
</dependencies>

```

## Building

The library is compatible with the Kotlin Standard Library not lower than `1.9.0`.
The library also requires JDK 17 or higher to build and run the dependencies.
