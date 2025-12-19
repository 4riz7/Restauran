### Листинг 30 — Реализация поиска и фильтрации заведений

В приложении реализована возможность мгновенного поиска ресторанов по их названию или адресу. Фильтрация данных происходит асинхронно, что обеспечивает высокую отзывчивость интерфейса даже при большом количестве заведений в базе (Листинг 30).

```kotlin
// Логика фильтрации списка в UserRestaurantsActivity
fun filterRestaurants(query: String) {
    lifecycleScope.launch {
        // Получение актуальных данных через ViewModel
        val allRestaurants = viewModel.getAllRestaurants()
        
        // Фильтрация по совпадению в названии или адресе (регистронезависимо)
        val filtered = allRestaurants.filter { restaurant ->
            restaurant.name.contains(query, ignoreCase = true) || 
            restaurant.address.contains(query, ignoreCase = true) 
        }
        
        // Обновление адаптера после фильтрации
        restaurantAdapter.submitList(filtered)
    }
}
```

### Листинг 31 — Отображение индикатора текущих бронирований

Для удобства пользователя на главном экране реализована секция «Мои текущие бронирования». Она отображает список актуальных визитов, позволяя пользователю быстро увидеть детали предстоящего посещения. Если активных бронирований нет, секция автоматически скрывается для экономии места (Листинг 31).

```kotlin
// Получение и отображение активных бронирований пользователя
lifecycleScope.launch {
    val myBookings = viewModel.getUserBookings(userId)
    
    if (myBookings.isNotEmpty()) {
        // Показ заголовка и горизонтального списка
        myBookingsHeader.visibility = View.VISIBLE
        myBookingsRecyclerView.visibility = View.VISIBLE
        horizontalBookingAdapter.submitList(myBookings)
    } else {
        // Скрытие секции, если текущих бронирований нет
        myBookingsHeader.visibility = View.GONE
        myBookingsRecyclerView.visibility = View.GONE
    }
}
```

### Листинг 32 — Структура адаптера для отображения данных

В проекте используются универсальные адаптеры на базе `RecyclerView`, которые связывают модели данных с визуальными элементами. Каждый элемент списка (карточка ресторана или брони) обрабатывается в отдельном `ViewHolder` (Листинг 32).

```kotlin
// Пример ViewHolder в RestaurantAdapter
inner class RestaurantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val nameText = view.findViewById<TextView>(R.id.restaurantNameTextView)
    private val addressText = view.findViewById<TextView>(R.id.restaurantAddressTextView)
    private val ratingText = view.findViewById<TextView>(R.id.restaurantRatingTextView)

    fun bind(restaurant: RestaurantWithRating) {
        nameText.text = restaurant.name
        addressText.text = restaurant.address
        ratingText.text = "Рейтинг: ${String.format("%.1f", restaurant.averageRating)} ★"
        ratingText.visibility = if (restaurant.reviewCount > 0) View.VISIBLE else View.GONE
    }
}
```

### Листинг 33 — Условное управление логикой кнопок бронирования

В зависимости от состояния системы (например, наличие свободных мест), интерфейс динамически изменяет доступность действий. Если в ресторане на выбранное время закончились свободные столики, кнопка подтверждения блокируется, предотвращая некорректные записи (Листинг 33).

```kotlin
// Логика блокировки кнопки в BookingActivity
lifecycleScope.launch {
    val currentBookings = viewModel.getBookingsForRestaurant(restaurantId)
    val restaurant = viewModel.getRestaurantById(restaurantId)

    // Проверка заполненности зала
    if (currentBookings.size >= (restaurant?.tableCount ?: 0)) {
        // Сценарий: Все столики заняты
        bookButton.isEnabled = false
        bookButton.alpha = 0.5f
        bookButton.text = "Мест нет"
    } else {
        // Сценарий: Бронирование доступно
        bookButton.isEnabled = true
        bookButton.alpha = 1.0f
        bookButton.text = "Забронировать"
    }
}
```
