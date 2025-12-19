### Листинг 37 — Элемент отображения текущего бронирования (Horizontal Booking)

Для отображения активных бронирований пользователя используется горизонтальный список с карточками. Каждая карточка содержит ключевую информацию о времени, дате и месте посещения, а также предоставляет быстрый доступ к отмене брони (Листинг 37).

```xml
<!-- Упрощенная структура item_horizontal_booking.xml -->
<com.google.android.material.card.MaterialCardView
    android:layout_width="280dp"
    android:layout_height="wrap_content"
    app:cardCornerRadius="16dp"
    app:cardBackgroundColor="?attr/colorSurface">

    <LinearLayout android:orientation="vertical" android:padding="16dp">
        <TextView android:id="@+id/dateTextView" android:textStyle="bold" />
        <TextView android:id="@+id/timeTextView" android:textColor="?attr/colorPrimary" />
        <TextView android:id="@+id/restaurantNameTextView" android:textStyle="bold" />
        
        <Button
            android:id="@+id/cancelButton"
            style="@style/Widget.MaterialComponents.Button.TextButton"
            android:text="Отменить"
            android:textColor="#E53935" />
    </LinearLayout>
</com.google.android.material.card.MaterialCardView>
```

### Листинг 38 — Условная обработка действий пользователя и администратора

В приложении реализована логика разграничения действий. Обычный пользователь видит кнопку «Отменить» для своих броней, в то время как в панели администратора удаление реализовано через контекстное меню или долгое нажатие, что предотвращает случайные изменения (Листинг 38).

```kotlin
// Условная установка слушателя в BookingAdapter
holder.cancelButton.setOnClickListener {
    // Вызов диалога подтверждения перед удалением из БД
    showCancelConfirmation(booking.id)
}

// В SuperAdminActivity: удаление через долгое нажатие
bookingAdapter = BookingAdapter(onLongClick = { booking ->
    if (userRole == "superadmin") {
        showAdminDeleteDialog(booking.id)
    }
})
```

### Листинг 39 — Визуальное кодирование и индикация данных

Вместо явных статусов (завершено/отменено), приложение использует цветовую индикацию для акцентирования внимания на важных данных. Время выделяется основным цветом темы (`colorPrimary`), а критические действия (удаление/отмена) — контрастным красным цветом (Листинг 39).

```kotlin
// Динамическое оформление элементов в адаптере
fun bind(booking: Booking) {
    timeText.text = booking.time
    timeText.setTextColor(ContextCompat.getColor(context, R.color.purple_500))
    
    // Индикация количества гостей
    guestsText.text = "Гостей: ${booking.guests}"
    guestsText.alpha = 0.7f // Визуальное приглушение второстепенной информации
}
```

### Листинг 40 — Логика разделения потоков данных в интерфейсе

Интерфейс «RestaurantBooking» использует гибридную модель отображения: текущие задачи пользователя выносятся в отдельный горизонтальный блок, в то время как основной контент (список ресторанов) занимает основную часть экрана, обеспечивая удобный поиск новых мест (Листинг 40).

```kotlin
// Распределение данных по разным компонентам в UserRestaurantsActivity
lifecycleScope.launch {
    val allBookings = viewModel.getUserBookings(userId)
    
    // Отправка данных в горизонтальный список активных броней
    horizontalAdapter.submitList(allBookings)
    
    // Загрузка основного списка ресторанов
    val restaurants = viewModel.getAllRestaurants()
    restaurantAdapter.submitList(restaurants)
}
```
