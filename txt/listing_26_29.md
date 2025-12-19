### Листинг 26 — Динамическое управление доступностью функций

В приложении реализовано управление состоянием интерфейса в зависимости от роли пользователя или состояния данных. Например, в панели управления заведениями кнопки удаления или редактирования становятся активными только для пользователей с соответствующими правами (Листинг 26).

```kotlin
// Управление видимостью функций в SuperAdminActivity
lifecycleScope.launch {
    val currentUser = viewModel.getUserById(userId)
    if (currentUser?.role == "superadmin") {
        // Показать кнопку добавления ресторана только для супер-админа
        addRestaurantButton.visibility = View.VISIBLE
        setupDeleteListeners() // Активация удаления долгим нажатием
    } else {
        addRestaurantButton.visibility = View.GONE
    }
}
```

### Листинг 27 — Логика создания нового отзыва

Создание нового отзыва включает в себя сбор данных из формы (рейтинг и комментарий), проверку заполненности обязательных полей и передачу объекта в бизнес-логику для сохранения в базу данных Room (Листинг 27).

```kotlin
submitButton.setOnClickListener {
    val rating = ratingBar.rating.toInt()
    val comment = commentEditText.text.toString()

    if (rating > 0) {
        lifecycleScope.launch {
            // Передача данных во ViewModel для регистрации отзыва
            viewModel.addReview(
                restaurantId = restaurantId,
                userId = userId,
                userName = "User #$userId", // Имя подтягивается из сессии
                rating = rating,
                comment = comment
            )
            
            // Сброс формы после успеха
            commentEditText.text.clear()
            formLayout.visibility = View.GONE
            loadReviews() // Обновление списка
        }
    } else {
        Toast.makeText(this, "Пожалуйста, поставьте оценку", Toast.LENGTH_SHORT).show()
    }
}
```

### Листинг 28 — Структура формирования объекта бронирования

При подтверждении бронирования столика приложение формирует объект сущности `Booking`, автоматически устанавливая временную метку создания и связывая запись с конкретным пользователем и рестораном (Листинг 28).

```kotlin
// Формирование объекта в BookingActivity
val booking = Booking(
    userId = userId,                // ID текущего пользователя
    restaurantId = restaurantId,    // ID выбранного заведения
    date = selectedDate,            // Дата из DatePickerDialog
    time = selectedTime,            // Время из TimePickerDialog
    guests = guestsNumberPicker.value, // Количество гостей из счетчика
    createdAt = System.currentTimeMillis() // Время совершения операции
)

// Вызов асинхронного метода сохранения
lifecycleScope.launch {
    val isBooked = viewModel.createBooking(
        booking.userId, booking.restaurantId, 
        booking.date, booking.time, booking.guests
    )
    if (isBooked) finish()
}
```

### Листинг 29 — Алгоритм фильтрации и поиска ресторанов

Экран выбора заведений (`UserRestaurantsActivity`) поддерживает поиск и фильтрацию данных. Список ресторанов обновляется динамически на основе введенного пользователем запроса, позволяя быстро находить заведение по названию или адресу (Листинг 29).

```kotlin
// Пример логики поиска на уровне ViewModel/Repository
suspend fun searchRestaurants(query: String): List<RestaurantWithRating> {
    val allRestaurants = repository.getAllRestaurants()
    
    return if (query.isEmpty()) {
        allRestaurants // Показать все, если поиск пуст
    } else {
        // Фильтрация по совпадению в названии или адресе
        allRestaurants.filter { restaurant ->
            restaurant.name.contains(query, ignoreCase = true) ||
            restaurant.address.contains(query, ignoreCase = true)
        }
    }
}
```
