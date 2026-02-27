# HijrahDateTime

[![Kotlin](https://img.shields.io/badge/Kotlin-2.3.10-purple.svg?logo=kotlin)](https://kotlinlang.org/)
[![Platform](https://img.shields.io/badge/Platform-JVM%20%7C%20iOS%20%7C%20macOS-blue)](https://kotlinlang.org/docs/multiplatform.html)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)

HijrahDateTime is a Kotlin Multiplatform library for working with the Hijrah calendar system. It provides a robust set of classes and functions to handle Hijrah dates and times across different platforms, integrating seamlessly with `kotlinx-datetime` and `kotlinx-serialization`.

Starting from version 2.0, this library has been rewritten as a Kotlin Multiplatform project, moving away from the previous `java.time`-only implementation to support a wider range of targets.

---

## Features

- **Kotlin Multiplatform**: Supports JVM, iOS (Arm64, Simulator Arm64), and macOS (Arm64).
- **Core Types**: Includes `HijrahDate`, `HijrahDateTime`, `HijrahYearMonth`, and `HijrahMonth`.
- **Arithmetic**: Support for date/time arithmetic using `plus` and `minus` with `DatePeriod` and `DateTimeUnit`.
- **Formatting & Parsing**: Flexible formatting and parsing through `HijrahDateTimeFormat` and a DSL-based builder.
- **Serialization**: First-class support for `kotlinx-serialization`.
- **Integration**: Easy conversion to and from `kotlinx-datetime` types (`LocalDate`, `LocalDateTime`, `Instant`).
- **Ranges & Progressions**: Support for date ranges and progressions (e.g., `date1..date2`, `date1 downTo date2`).

---

## Supported Platforms

- **JVM** (JDK 11+)
- **iOS** (Arm64, Simulator Arm64)
- **macOS** (Arm64)

*More targets may be added in the future.*

---

## Getting Started

### Gradle

Add the dependency to your `commonMain` source set:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("com.abdulrahman-b.hijrahdatetime:hijrahdatetime:2.0.0")
        }
    }
}
```

### Maven (JVM only)

```xml
<dependency>
    <groupId>com.abdulrahman-b.hijrahdatetime</groupId>
    <artifactId>hijrahdatetime-jvm</artifactId>
    <version>2.0.0</version>
</dependency>
```

---

## Core Concepts

### HijrahDate

Represents a date (year, month, day) in the Hijrah calendar.

```kotlin
// Create a HijrahDate
val date = HijrahDate(1446, 10, 12)

// Arithmetic
val tomorrow = date plusDays 1
val nextMonth = date plusMonths 1
val nextYear = date plusYears 1

// Convert to/from kotlinx-datetime.LocalDate
val localDate = date.toLocalDate()
val hijrahDate = localDate.toHijrahDate()
```

### HijrahDateTime

Combines a `HijrahDate` with a `LocalTime`.

```kotlin
val dateTime = HijrahDateTime(1446, 10, 12, 12, 30, 0, 0)
println(dateTime.date) // 1446-10-12
println(dateTime.time) // 12:30:00
```

### HijrahYearMonth

Represents a specific year and month in the Hijrah calendar.

```kotlin
val yearMonth = HijrahYearMonth(1446, HijrahMonth.RAMADAN)
println(yearMonth.numberOfDays) // Number of days in Ramadan 1446
val firstDay = yearMonth.firstDay
val lastDay = yearMonth.lastDay
```

---

## Formatting and Parsing

You can use predefined formats or build your own using the DSL.

```kotlin
// Using predefined formats
val date = HijrahDate(1446, 10, 12)
val formatted = date.format(HijrahDateTimeFormats.DATE_ISO) // "1446-10-12"

// Using custom pattern
val customFormat = HijrahDateTimeFormat.ofPattern("yyyy/MM/dd")
val parsedDate = HijrahDate.parse("1446/10/12", customFormat)

// Using DSL builder
val dslFormat = buildDateTimeFormat {
    year()
    char('/')
    monthName(NameStyle.FULL)
    char('/')
    dayOfMonth()
}
println(date.format(dslFormat)) // "1446/Shawwal/12"
```

---

## Serialization

Core types are annotated with `@Serializable`. You can use them directly in your serializable classes.

```kotlin
@Serializable
data class Event(
    val name: String,
    val date: HijrahDate
)

val json = Json.encodeToString(Event("Eid Al-Fitr", HijrahDate(1446, 10, 1)))
```

By default, it uses a component-based serializer. For ISO-string serialization, use `HijrahDateIsoSerializer`:

```kotlin
@Serializable
data class Event(
    @Serializable(with = HijrahDateIsoSerializer::class)
    val date: HijrahDate
)
```

---

## Ranges and Progressions

```kotlin
val start = HijrahDate(1446, 9, 1)
val end = HijrahDate(1446, 9, 30)

// Range
val ramadan = start..end

// Progression
for (day in start..end) {
    println(day)
}

// DownTo
for (day in end downTo start) {
    println(day)
}
```

---

## Breaking Changes from 1.x

Version 2.0 is a complete rewrite of the library to support Kotlin Multiplatform. This introduced several breaking changes:

- **Technology Shift**: Migrated from `java.time` to a pure Kotlin implementation with `kotlinx-datetime` integration.
- **Package Name**: Changed from `com.abdulrahman_b.hijrahdatetime` to `com.abdulrahman-b.hijrahdatetime` (using a hyphen in the artifact ID, but keeping underscores in package names for Kotlin compatibility where necessary).
- **Class Changes**:
    - `HijrahDate` is now an `expect`/`actual` class.
    - `SimpleHijrahDate`, `EarlyHijrahDate`, and `OffsetHijrahDate` have been removed in favor of a unified `HijrahDate`.
    - `ZonedHijrahDateTime` and `OffsetHijrahDateTime` are currently not available in 2.0. Use `HijrahDateTime` and convert to/from `Instant` using `TimeZone`.
- **API Changes**:
    - Methods like `plusDays`, `minusMonths` etc. are now extension functions.
    - Formatting now uses `HijrahDateTimeFormat` instead of `java.time.format.DateTimeFormatter`.
    - Serialization now uses `kotlinx-serialization` instead of Java Serialization or Jackson.

---

## Future Implementation & Philosophy

Will the older date types (like `ZonedHijrahDateTime` or `OffsetHijrahDateTime`) be implemented again in this version?

It is **not guaranteed**. This library follows the philosophy of [kotlinx-datetime](https://github.com/Kotlin/kotlinx-datetime) and tries to adopt its way of working with dates and times. 

However, if a feature previously existed in version 1.0 but is currently missing in version 2.0, it **might** be implemented in the future if:
1. It can be implemented safely and consistently across all supported platforms.
2. it aligns with the core goals of the library.

Please note that there are no guarantees or promises regarding the re-implementation of specific legacy types.

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## Support Me

If you'd like to support my work on this project, you can do so via [PayPal](https://www.paypal.com/paypalme/AbdulrahmanBahamel). Your contributions are greatly appreciated!
