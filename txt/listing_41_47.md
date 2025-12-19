### Листинг 41 — Обработка бизнес-логики через ViewModel

Все ключевые операции по управлению данными (отмена бронирования, модерация отзывов или удаление заведений) выполняются асинхронно через `ViewModel`, которая возвращает результат операции для корректного отображения обратной связи в UI (Листинг 41).

```kotlin
// Пример удаления (отмены) бронирования
fun cancelBooking(bookingId: Int) {
    lifecycleScope.launch {
        try {
            viewModel.deleteBooking(bookingId)
            showToast("Бронирование успешно отменено")
            loadUserBookings() // Перезагрузка списка
        } catch (e: Exception) {
            showError("Не удалось отменить бронирование")
        }
    }
}

// Пример модераторского действия (удаление отзыва)
fun removeReview(reviewId: Int) {
    lifecycleScope.launch {
        viewModel.deleteReview(reviewId)
        showToast("Отзыв удален модератором")
        refreshReviewsList()
    }
}
```

### Листинг 42 — Модель данных пользователя и управление профилем

Для взаимодействия с профилем пользователя используется сущность `User`, содержащая данные о имени, почте, пароле и роли в системе. В приложении реализована возможность обновления этих данных через методы `updateUser` (Листинг 42).

```kotlin
// Сущность пользователя в Room
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val role: String = "user", // "user", "admin", "superadmin"
    val restaurantId: Int? = null // Для роли admin
)
```

### Листинг 43 — Реализация адаптивных заголовков и навигации

Верхняя панель приложения настраивается в зависимости от типа экрана и прав доступа пользователя. Для этого используются стандартные компоненты `Toolbar` или кастомные `TextView` в XML-макетах (Листинг 43).

```xml
<!-- Кастомный AppBar в макете профиля/настроек -->
<com.google.android.material.appbar.AppBarLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content">
    
    <com.google.android.material.appbar.MaterialToolbar
        android:id="@+id/profileToolbar"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        app:title="Мой профиль"
        app:navigationIcon="@drawable/ic_back" />
</com.google.android.material.appbar.AppBarLayout>
```

### Листинг 44 — Визуализация рейтинговой системы

Для отображения рейтинга заведений используется компонент `RatingBar` или текстовая индикация в `MaterialCardView`, что позволяет пользователю мгновенно оценить популярность места на основе среднего балла всех отзывов (Листинг 44).

```xml
<com.google.android.material.card.MaterialCardView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:cardBackgroundColor="?attr/colorSecondaryContainer">

    <Row
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="16dp"
        android:gravity="center">

        <ImageView android:src="@drawable/ic_star" ... />
        <TextView
            android:id="@+id/ratingText"
            android:text="Рейтинг: 4.8 ★"
            android:textStyle="bold"
            android:textSize="20sp" />
    </Row>
</com.google.android.material.card.MaterialCardView>
```

### Листинг 45 — Условный рендеринг полей в режиме просмотра и редактирования

В приложении используется механизм программного переключения прав доступа к полям ввода. В режиме «Просмотр» поля заблокированы, а при переходе в режим «Редактирование» они становятся доступными для изменения (Листинг 45).

```kotlin
// Логика переключения режима в Activity
fun setEditMode(enabled: Boolean) {
    nameEditText.isEnabled = enabled
    emailEditText.isEnabled = enabled
    saveButton.visibility = if (enabled) View.VISIBLE else View.GONE
    editButton.text = if (enabled) "Отмена" else "Редактировать"
}
```

### Листинг 46 — Расчёт агрегированной статистики активности

Приложение выполняет расчет метрик пользователя на основе данных из всех таблиц БД: количество посещенных ресторанов, сумма оставленных отзывов и средняя оценка, поставленная пользователем (Листинг 46).

```kotlin
// Расчет статистики во ViewModel/Repository
suspend fun getUserStats(userId: Int): Map<String, Any> {
    val bookings = repository.getUserBookings(userId)
    val reviews = repository.getAllReviews().filter { it.userId == userId }
    
    return mapOf(
        "total_bookings" to bookings.size,
        "total_reviews" to reviews.size,
        "avg_rating_given" to if (reviews.isEmpty()) 0.0 else reviews.map { it.rating }.average()
    )
}
```

### Листинг 47 — Визуализация метрик в интерфейсе пользователя

Статистические показатели выводятся в виде компактных блоков, что позволяет пользователю видеть историю своего взаимодействия с сервисом (Листинг 47).

```xml
<!-- Секция статистики из макета профиля -->
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="horizontal"
    android:weightSum="3">

    <include layout="@layout/view_stat_column" android:id="@+id/colBookings" />
    <include layout="@layout/view_stat_column" android:id="@+id/colReviews" />
    <include layout="@layout/view_stat_column" android:id="@+id/colSaved" />

</LinearLayout>
```
