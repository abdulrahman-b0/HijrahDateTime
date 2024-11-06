# HijrahDateTime

[![Kotlin Alpha](https://kotl.in/badges/beta.svg)]()
[![Kotlin](https://img.shields.io/badge/kotlin-2.0-blue.svg?logo=kotlin)]()
[![Java](https://img.shields.io/badge/java-17-orange.svg?logo=java)]()
[![Test Coverage](https://s3.amazonaws.com/assets.coveralls.io/badges/coveralls_94.svg)]()

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

- `HijrahMonth` to represent the hijrah months in a descriptive manner.


### Extensions

The library provides a set of extensions for the java.time APIs to facilitate the work on hijrah dates and times. For example:

- the library provides extensions for the `HijrahDate` to integrate it with this library's classes and functions.
- the library provides extensions for the `Instant` to integrate it with this library's classes and functions.
- the library provides extensions for the `LocalDate` class to convert it to `HijrahDate`. Same for other classes.

For java developers, you can access those extensions by using the `HijrahDates`, and `Instants` singleton objects.
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

Formatting done! If you have any questions or issues or a requirement to support another format, please create an issue in the repository.


## Serialization

### Using java serialization

The library provides serialization support with java serialization. You can serialize and deserialize the datetime classes using the java serialization API without any additional configuration.


### Using kotlinx.serialization

The library provides serialization support with kotlinx.serialization library in the artifact `HijrahDateTime-serialization-kotlinx`. 

#### Using the custom serializers

The library provides a list of custom serializers, Each of which use the recommended built-in formatters to serialize and deserialize the datetime classes from and to `String`.
The serializers are:

- `HijrahDateSerializer` to serialize and deserialize the `HijrahDate` class.
- `HijrahDateTimeSerializer` to serialize and deserialize the `HijrahDateTime` class.
- `ZonedHijrahDateTimeSerializer` to serialize and deserialize the `ZonedHijrahDateTime` class.
- `OffsetHijrahDateTimeSerializer` to serialize and deserialize the `OffsetHijrahDateTime` class.

So, you can use them in your data classes like this:

```kotlin
import com.abdulrahman_b.hijrah_datetime.serialization.kotlinx.HijrahDateTimeSerializer
import kotlinx.serialization.Serializable

data class MyData(
    @Serializable(with = HijrahDateTimeSerializer::class)
    val date: HijrahDateTime
)

fun main() {
    val data = MyData(HijrahDateTime.of(1446, 10, 12, 12, 30))
    val json = Json.encodeToString(data)
    println(json) // {"date":"1446-10-12T12:30:00"}
}
```
#### Using contextual serialization

The above serializers are useful when you want to serialize and deserialize the datetime classes, regardless of the string format. However, if you want to use another format, you have to use the contextual serialization.

Contextual serialization is a feature of kotlinx.serialization that allows you to register serializers at runtime, which allows you to customize the serialization and deserialization process according to your needs.
The library provides a `HijrahChronoSerializersModule` class that contextually registers all the serializers for you, while allowing you to customize the formatters. For example:

```kotlin
import com.abdulrahman_b.hijrah_datetime.serialization.kotlinx.HijrahDateTimeSerializer
import kotlinx.serialization.Contextual

data class MyData(
    @Contextual
    val date: HijrahDateTime
)

fun main() {
    
    val formatter = DateTimeFormatter
        .ofPattern("yyyy/MM/dd hh:mm:ss a") // Custom format
        .withChronology(HijrahChronology.INSTANCE)
    
    val json = Json {
        serializersModule = HijrahChronoSerializersModule(hijrahDateTimeFormatter = formatter).get()
    }

    val data = MyData(HijrahDateTime.of(1446, 10, 12, 12, 30))
    val json = json.encodeToString(data)
    println(json) // {"date":"1446/10/12 12:30:00 PM"
}
```

Alternatively, you can define the contextual serialization manually, like this:

```kotlin
//For brevity, other code is omitted

val json = Json {
    serializersModule = SerializersModule {
        contextual(HijrahDateTime::class, HijrahDateTimeSerializer(formatter)) // Register the serializer with the custom format
    }
}

//For brevity, other code is omitted, the output should be the same as the above example.
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
- `OffsetHijrahDateTimeSerialization.Serializer` to serialize the `OffsetHijrahDateTime` class.
- `OffsetHijrahDateTimeSerialization.Deserializer` to deserialize the `OffsetHijrahDateTime` class.

For organization purposes, the serializer and deserializer for each type is defined as a nested class.

So, you can use them in your data classes like this:

```kotlin
import com.abdulrahman_b.hijrah_datetime.serialization.jackson.HijrahDateTimeSerialization
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize

data class MyData(
    @JsonSerialize(using = HijrahDateTimeSerialization.Serializer::class)
    @JsonDeserialize(using = HijrahDateTimeSerialization.Deserializer::class)
    val date: HijrahDateTime
)

fun main() {
    val data = MyData(HijrahDateTime.of(1446, 10, 12, 12, 30))
    val json = ObjectMapper().writeValueAsString(data)
    println(json) // {"date":"1446-10-12T12:30:00"}
}
```

Alternatively, you can use the `HijrahChronoSerializersModule` class that registers all the serializers and deserializers for you, while allowing you to customize the formatters. For example:

```kotlin
import com.abdulrahman_b.hijrah_datetime.serialization.jackson.HijrahChronoSerializationModule
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

data class MyData(
    val date: HijrahDateTime
)

fun main() {

    val formatter = DateTimeFormatter
        .ofPattern("yyyy/MM/dd hh:mm:ss a")
        .withChronology(HijrahChronology.INSTANCE)

    val module = HijrahChronoSerializationModule(hijrahDateTimeFormatter = formatter)
    val mapper = ObjectMapper().registerModule(module)
        .registerKotlinModule() //Don't forget to register the kotlin module if you are using kotlin data classes. You find it in the jackson-module-kotlin library.

    val data = MyData(HijrahDateTime.of(1446, 10, 12, 12, 30))
    val json = mapper.writeValueAsString(data)
    println(json) // {"date":"1446/10/12 12:30:00 PM"}
}
```

Serialization done! If you have any questions or issues or a requirement to support another serialization libraries, please create an issue in the repository.




## Jakarta Bean Validation

The library provides support for Jakarta Bean Validation 3.0 API in the artifact `HijrahDateTime-jakarta-validators`.

The library implements a set of validators for the hijrah datetime classes, which allows you to validate the datetime classes according to your requirements. This is especially useful for JVM backend applications such as Spring Boot.

If you are familiar with the Jakarta Bean Validation API, you supposed to know that it provides a set of standard constraints annotations to validate the classes according to your requirements.
This library does not provide any new additional constraints annotations. Instead, it uses the standard constraints annotations provided by the Jakarta Bean Validation API.
That means you can use `@Future`, `@Past`, `@FutureOrPresent`, and `@PastOrPresent` annotations to validate the hijrah datetime classes. Cool, right?

For example, in a Spring boot application, you can use the `@Future` annotation to validate the `HijrahDateTime` class like this:

```kotlin
data class MyData(
    @field:Future //In java, you can use @Future annotation directly without the field keyword.
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

Note that the `HijrahDateTime` class must be serializable to serialize and deserialize request/response body, refer to the [Serialization](#serialization) section for more information on how to serialize the datetime classes.


## Using in your projects

> Note that the library is experimental, and the API is subject to change.
> However, the library is almost stable. You can use it in your projects, and if you face any issues or incorrect behavior, consider creating an issue in the repository.


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
    implementation("com.abdulrahman-b:HijrahDateTime:1.0.0-beta.3") //Core library
    implementation("com.abdulrahman-b:HijrahDateTime-serialization-kotlinx:1.0.0-beta.3") //Kotlinx Serialization support
    implementation("com.abdulrahman-b:HijrahDateTime-serialization-jackson:1.0.0-beta.3") //Jackson Serialization support
    implementation("com.abdulrahman-b:HijrahDateTime-jakarta-validators:1.0.0-beta.3") //Jakarta Bean Validation support
}
```

#### Using Gradle Groovy DSL
```groovy build.gradle
dependencies {
    implementation 'com.abdulrahman-b:HijrahDateTime:1.0.0-beta.3' //Core library
    implementation 'com.abdulrahman-b:HijrahDateTime-serialization-kotlinx:1.0.0-beta.3' //Kotlinx Serialization support
    implementation 'com.abdulrahman-b:HijrahDateTime-serialization-jackson:1.0.0-beta.3' //Jackson Serialization support
    implementation 'com.abdulrahman-b:HijrahDateTime-jakarta-validators:1.0.0-beta.3' //Jakarta Bean Validation support
}
```

### Maven

Add a dependency to the `<dependencies>` element:

```xml pom.xml
<dependency>
    <groupId>com.abdulrahman-b</groupId>
    <artifactId>HijrahDateTime</artifactId>
    <version>1.0.0-beta.3</version>
</dependency> <!--Core library-->

<dependency>
    <groupId>com.abdulrahman-b</groupId>
    <artifactId>HijrahDateTime-serialization-kotlinx</artifactId>
    <version>1.0.0-beta.3</version>
</dependency> <!--Kotlinx Serialization support-->

<dependency>
    <groupId>com.abdulrahman-b</groupId>
    <artifactId>HijrahDateTime-serialization-jackson</artifactId>
    <version>1.0.0-beta.3</version>
</dependency> <!--Jackson Serialization support-->

<dependency>
    <groupId>com.abdulrahman-b</groupId>
    <artifactId>HijrahDateTime-jakarta-validators</artifactId>
    <version>1.0.0-beta.3</version>
</dependency> <!--Jakarta Bean Validation support-->
```

## Building

The library is compatible with the Kotlin Standard Library not lower than `2.0`.
The library also requires JDK 17 or higher to build and run the dependencies.
