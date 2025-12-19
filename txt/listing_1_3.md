### Листинг 1 — Booking.kt

```kotlin
package com.example.restaurantbooking.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val restaurantId: Int,
    val date: String, // Формат: "15.12.2024"
    val time: String, // Формат: "19:00"
    val guests: Int,
    val createdAt: Long = System.currentTimeMillis()
)
```

Файл **BookingViewModel** — это ключевой файл, который связывает данные и интерфейс. Класс наследуется от `ViewModel` и служит для отделения логики приложения от пользовательского интерфейса. Все изменения состояния или запросы к данным происходят внутри ViewModel, что гарантирует сохранение данных при повороте экрана и обеспечивает реактивное обновление UI через взаимодействие с репозиторием (Листинг – 2).

### Листинг 2 — BookingViewModel.kt

```kotlin
package com.example.restaurantbooking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantbooking.data.repository.BookingRepository
import kotlinx.coroutines.launch

class BookingViewModel(private val repository: BookingRepository) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.createSuperAdminIfNotExists()
        }
    }

    suspend fun getAllRestaurants() = repository.getAllRestaurants()
    
    suspend fun getUserBookings(userId: Int) = repository.getUserBookings(userId)
    
    // Другие методы взаимодействия с репозиторием...
}
```

Далее следует метод создания нового бронирования. Метод передает данные в репозиторий, который проверяет доступность мест в ресторане, автоматически генерирует уникальный идентификатор записи в базе данных Room и сохраняет информацию (Листинг – 3).

### Листинг 3 — Метод создания бронирования

```kotlin
suspend fun createBooking(
    userId: Int, 
    restaurantId: Int, 
    date: String, 
    time: String, 
    guests: Int
): Boolean {
    // Вся логика генерации ID и сохранения реализована на уровне Repo и DAO
    return repository.createBooking(userId, restaurantId, date, time, guests)
}
```
