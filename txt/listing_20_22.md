### Листинг 20 — Интерактивная панель статистики пользователя

Для отображения активности пользователя на главном экране или в профиле реализована панель статистики. Она строится с использованием `MaterialCardView` и позволяет пользователю мгновенно оценить количество совершенных бронирований и оставленных отзывов (Листинг 20).

```xml
<!-- Секция статистики в макете пользователя -->
<com.google.android.material.card.MaterialCardView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="16dp"
    app:cardCornerRadius="16dp">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="16dp">

        <TextView
            android:text="Ваша активность"
            android:textStyle="bold"
            android:layout_marginBottom="12dp" />

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal">

            <include layout="@layout/stat_item" android:id="@+id/statBookings" />
            <include layout="@layout/stat_item" android:id="@+id/statReviews" />
            <include layout="@layout/stat_item" android:id="@+id/statRating" />

        </LinearLayout>
    </LinearLayout>
</com.google.android.material.card.MaterialCardView>
```

### Листинг 21 — Экран деталей ресторана и форма бронирования

Вместо отдельного экрана создания, в приложении используется `RestaurantDetailsActivity`, где под информацией о заведении размещена форма бронирования. Она напрямую взаимодействует с `BookingViewModel` для проверки бизнес-правил, таких как доступность мест (Листинг 21).

```kotlin
// В RestaurantDetailsActivity: инициализация полей формы
val dateEditText = findViewById<EditText>(R.id.dateEditText)
val timeEditText = findViewById<EditText>(R.id.timeEditText)
val guestsEditText = findViewById<EditText>(R.id.guestsEditText)
val bookButton = findViewById<Button>(R.id.bookButton)

// Обработка бизнес-логики через ViewModel
lifecycleScope.launch {
    val restaurant = viewModel.getRestaurantById(restaurantId)
    val bookings = viewModel.getBookingsForRestaurant(restaurantId)
    
    // Проверка заполненности ресторана на уровне UI
    val tablesTaken = bookings.size
    val totalTables = restaurant?.tableCount ?: 0
}
```

### Листинг 22 — Система контроля доступности мест

Для предотвращения бронирований в переполненные заведения реализована система валидации. Если количество текущих броней на выбранное время достигает лимита столиков, приложение блокирует возможность создания новой записи и информирует об этом пользователя (Листинг 22).

```kotlin
// Логика проверки и отображения предупреждения
if (currentBookingsCount >= restaurantCapacity) {
    // Показ информационного блока о невозможности бронирования
    bookingFormCard.visibility = View.GONE
    warningCard.visibility = View.VISIBLE
    warningTextView.text = "К сожалению, на выбранное время мест нет. " +
                           "Максимум столиков: $restaurantCapacity"
} else {
    bookingFormCard.visibility = View.VISIBLE
    warningCard.visibility = View.GONE
}
```
