# 🚚 Courier Tracking API

Courier Tracking API, kuryelerinin lokasyonlarını takip eder, mağazalara belirli bir mesafeye yaklaştıklarında giriş kaydı oluşturur ve toplam seyahat mesafesini hesaplar. Uygulama, Spring Boot, PostgreSQL ve çeşitli tasarım desenleri kullanılarak geliştirilmiştir.

---

## 📦 Özellikler

- 📍 Kurye lokasyonlarını kaydetme
- 🏬 Kurye, bir X mağazasına 100 metre yaklaştığında giriş kaydı oluşturma
- ⏱ Aynı mağazaya 1 dakikadan kısa sürede yeniden giriş yapılmasını engelleme
- 📏 Kuryenin toplam seyahat mesafesini hesaplama (Cosines/Haversine) bu algoritmaları google dan buldum :)
- 🛠 Strateji & Factory & Singleton tasarım desenleri kullanımı
- 🛠 Custom Validasyon kullanıldı
- 🛠 Store dönerken in-memory Cache kullanıldı

---

## 🧰 Kullanılan Teknolojiler

- Java 21
- Spring Boot 3.4.5
- PostgreSQL
- JPA/Hibernate
- Lombok
- MapStruct
- JUnit 5 & Mockito
- Maven
- Constraint Validation

> 🛑 **Not:** Courier konumu gönderilirken, `trackingEntryDate`, sistem zamanı (T) ile `T + 30 dakika` aralığında olmalıdır. Bu kural, custom bir validator ile kontrol edilmektedir.
> > 🛑 **Not:** Hibernate sayesinde db connection için uygulamayı run etmek yeterli olacaktır.
> > > 🧪 Test etmek için `app/asset/courier.http` dosyasındaki `trackingEntryDate` tarihi değiştirerek kayıt olusturabilirsiniz.


