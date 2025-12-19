### Листинг 13 — Механизм возврата данных и закрытия экранов

Для экрана создания бронирования реализован механизм завершения активности после успешного выполнения операции. Это гарантирует, что пользователь вернется на предыдущий экран (список ресторанов), который автоматически обновит данные (Листинг 13).

```kotlin
// Внутри метода создания бронирования в Activity
if (isSuccess) {
    Toast.makeText(this, "Бронирование подтверждено", Toast.LENGTH_SHORT).show()
    finish() // Завершение текущей Activity и возврат назад
}
```

### Листинг 14 — Единый механизм навигации «Назад»

Все вспомогательные экраны приложения используют стандартный механизм возврата, обеспечиваемый методом `onBackPressed()` или вызовом `finish()`, что соответствует системному поведению Android-приложений (Листинг 14).

```kotlin
// Обработка нажатия на кнопку "Назад" в тулбаре или интерфейсе
backButton.setOnClickListener {
    finish()
}
```

### Листинг 15 — Структура главного экрана (UserRestaurantsActivity)

Главный экран пользователя построен на основе вертикального `LinearLayout`, который объединяет разделы текущих бронирований и список доступных заведений. Для обеспечения плавности интерфейса используются компоненты `RecyclerView` с оптимизированными адаптерами (Листинг 15).

```xml
<!-- Упрощенная структура activity_user_restaurants.xml -->
<LinearLayout 
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <TextView
        android:id="@+id/myBookingsHeader"
        android:text="Мои текущие бронирования"
        android:textSize="18sp"
        android:textStyle="bold" />

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/myBookingsRecyclerView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal" />

    <TextView
        android:text="Выберите ресторан"
        android:background="@color/purple_500"
        android:textColor="@android:color/white" />

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/restaurantsRecyclerView"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1" />
</LinearLayout>
```

### Листинг 16 — Кастомная панель заголовка (Header)

Вместо стандартного ActionBar в приложении реализованы кастомные элементы заголовков, оформленные в фирменном стиле (индиго/пурпурный). Это обеспечивает уникальный брендинг и позволяет размещать дополнительные элементы управления, такие как кнопки добавления данных (Листинг 16).

```xml
<!-- Пример заголовка из activity_super_admin.xml -->
<TextView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Супер-админ панель"
    android:textSize="20sp"
    android:textStyle="bold"
    android:gravity="center"
    android:padding="16dp"
    android:background="@color/purple_700"
    android:textColor="@color/white" />
```
