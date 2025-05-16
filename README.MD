# 🚚 Courier Tracking API

The Courier Tracking API tracks the locations of couriers, creates a log when they approach stores within a certain distance and calculates the total travel distance. The application was developed using Spring Boot, PostgreSQL and various design patterns.

---

## 📦 Features

- 📍 Save courier locations
- 🏬 Creating an entry record when a courier gets within 100 meters of a store X
- ⏱ Prevent re-entry to the same store in more than 1 minute
- 📏 Calculating the total travel distance of the courier (Cosines/Haversine) I found these algorithms from google :)
- 🛠 Use of Strategy & Factory & Singleton design patterns
- 🛠 Custom Validation used
- 🛠 In-memory Cache was used when returning the Store

---

## 🧰 Technologies Used

- Java 21
- Spring Boot 3.4.5
- PostgreSQL
- JPA/Hibernate
- Lombok
- MapStruct
- JUnit 5 & Mockito
- Maven
- Constraint Validation

> 🛑 **Note:** When sending a Courier location, `trackingEntryDate` must be between system time (T) and `T + 1 minutes`. This rule is checked with a custom validator.
> > 🛑 **Note:** Thanks to Hibernate, it will be enough to run the application for db connection.
> > > 🧪 To test, you can create a record by changing the `trackingEntryDate` date in `app/asset/courier.http` file.


> 🛑 **Note:** I did not save the courier entries greater than 1 minute to the database because it says do not save records exceeding 1 minute in the case. Courier entries less than 1 minute are saved.