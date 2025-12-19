### Листинг 7 — Методы для подсчёта статистики пользователя

В приложении реализованы методы для получения количественных показателей активности пользователя, которые могут отображаться в его личном кабинете или профиле (Листинг 7).

```kotlin
// Подсчет общего количества бронирований пользователя
suspend fun getUserTotalBookingsCount(userId: Int): Int {
    return repository.getUserBookings(userId).size
}

// Подсчет количества оставленных отзывов
suspend fun getUserReviewsCount(userId: Int, restaurantId: Int): Int {
    return repository.getReviewsForRestaurant(restaurantId)
        .count { it.userId == userId }
}
```

### Листинг 8 — Взаимодействие UI-слоя с ViewModel

Слой пользовательского интерфейса (Activity) взаимодействует с `BookingViewModel` для выполнения бизнес-операций. Поскольку операции с базой данных являются асинхронными, вызовы обернуты в корутины (`lifecycleScope`), что предотвращает зависание интерфейса (Листинг 8).

```kotlin
// Пример вызова бронирования из Activity
lifecycleScope.launch {
    val isSuccess = viewModel.createBooking(
        userId = currentUserId,
        restaurantId = selectedId,
        date = selectedDate,
        time = selectedTime,
        guests = numGuests
    )

    if (isSuccess) {
        Toast.makeText(this@Activity, "Успешно забронировано!", Toast.LENGTH_SHORT).show()
        finish() // Возврат на предыдущий экран
    } else {
        showErrorDialog("В выбранное время нет свободных мест")
    }
}
```

### Листинг 9 — Инициализация приложения (LoginActivity)

**LoginActivity** является точкой входа в приложение (или одним из ключевых экранов), инициализирующим базу данных и настраивающим логику аутентификации. Класс наследуется от `AppCompatActivity` — стандартного компонента для обеспечения совместимости интерфейса (Листинг 9).

```kotlin
class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: BookingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Инициализация базы данных и репозитория
        val db = AppDatabase.getDatabase(this)
        val repository = BookingRepository(
            db.userDao(), 
            db.restaurantDao(), 
            db.bookingDao(), 
            db.reviewDao()
        )
        viewModel = BookingViewModel(repository)

        // Настройка слушателей кнопок и логики входа...
    }
}
```
