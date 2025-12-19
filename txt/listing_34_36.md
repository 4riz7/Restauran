### Листинг 34 — Обработка пустых состояний интерфейса

В приложении реализована логика обработки пустых состояний (Empty States). Если база данных ресторанов пуста или поисковый запрос не дал результатов, пользователю отображается соответствующее информационное сообщение вместо пустого экрана (Листинг 34).

```kotlin
// Логика отображения пустого состояния в UserRestaurantsActivity
private fun updateEmptyState(isEmpty: Boolean, isSearch: Boolean) {
    if (isEmpty) {
        restaurantsRecyclerView.visibility = View.GONE
        emptyStateLayout.visibility = View.VISIBLE
        
        emptyStateTextView.text = if (isSearch) {
            "По вашему запросу ничего не найдено"
        } else {
            "Список ресторанов пуст. Добавьте заведения в панели управления."
        }
    } else {
        restaurantsRecyclerView.visibility = View.VISIBLE
        emptyStateLayout.visibility = View.GONE
    }
}
```

### Листинг 35 — Организация панелей управления и разделения списков

Вместо системы табов, в приложении «RestaurantBooking» используется иерархическая структура экранов и разделение данных на логические блоки внутри одной Activity. Например, в панели супер-администратора реализовано вертикальное разделение для одновременного контроля всех бронирований и всех заведений (Листинг 35).

```xml
<!-- Разделение функциональных зон в activity_super_admin.xml -->
<LinearLayout ...>
    <!-- Блок 1: Глобальный список бронирований -->
    <TextView android:text="Все бронирования системы" ... />
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/allBookingsRecyclerView"
        android:layout_weight="1" ... />

    <!-- Блок 2: Глобальный список заведений -->
    <TextView android:text="Управление ресторанами" ... />
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/allRestaurantsRecyclerView"
        android:layout_weight="1" ... />
</LinearLayout>
```

### Листинг 36 — Механизм уведомлений и обратной связи

Для информирования пользователя о результатах операций (успех, ошибка, подтверждение удаления) в приложении используется комбинация всплывающих уведомлений `Toast` и диалоговых окон `AlertDialog`. Это обеспечивает наглядность и предотвращает случайные действия при модерации данных (Листинг 36).

```kotlin
// Пример обратной связи при удалении отзыва (SuperAdminActivity / ManageAdminsActivity)
fun confirmDeletion(reviewId: Int) {
    AlertDialog.Builder(this)
        .setTitle("Подтверждение")
        .setMessage("Вы уверены, что хотите безвозвратно удалить этот отзыв?")
        .setPositiveButton("Да, удалить") { _, _ ->
            lifecycleScope.launch {
                viewModel.deleteReview(reviewId)
                // Визуальное подтверждение успеха
                Toast.makeText(this@Activity, "Отзыв успешно удален", Toast.LENGTH_SHORT).show()
                loadData() // Обновление интерфейса
            }
        }
        .setNegativeButton("Отмена", null)
        .show()
}
```
