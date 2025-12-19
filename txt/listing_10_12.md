### Листинг 10 — Архитектура навигации на основе Intent

Для архитектуры навигации в приложении «RestaurantBooking» реализована многоактивная структура, где каждый крупный функциональный блок представлен отдельной Activity. Переходы между экранами управляются с помощью системы `Intent`, которая обеспечивает передачу необходимых параметров (например, ID пользователя или ресторана) между компонентами приложения (Листинг 10).

```kotlin
// Пример запуска экрана деталей ресторана из общего списка
val intent = Intent(this, RestaurantDetailsActivity::class.java).apply {
    putExtra("RESTAURANT_ID", restaurant.id)
    putExtra("RESTAURANT_NAME", restaurant.name)
    putExtra("USER_ID", userId)
}
startActivity(intent)
```

### Листинг 11 — Ключевые экраны и маршруты навигации

В приложении определены ключевые активности, каждая из которых отвечает за свой уровень доступа и функционал. Навигация построена таким образом, чтобы минимизировать вложенность и обеспечить быстрый возврат к списку ресторанов (Листинг 11).

```kotlin
// Навигация из LoginActivity в зависимости от роли пользователя:
val targetActivity = when (user.role) {
    "superadmin" -> SuperAdminActivity::class.java
    "admin" -> AdminPanelActivity::class.java
    else -> UserRestaurantsActivity::class.java
}
startActivity(Intent(this, targetActivity).apply {
    putExtra("USER_ID", user.id)
})
finish() // Закрытие экрана входа после успешной авторизации
```

### Листинг 12 — Обработка переходов и возвратов

Для перехода между экранами используются слушатели нажатий (`OnClickListener`), а для возврата к предыдущему состоянию — стандартный механизм стека активностей Android. Это гарантирует привычное пользователю поведение кнопки «Назад» и сохранение контекста приложения (Листинг 12).

```kotlin
// В UserRestaurantsActivity:
// Клик по карточке ресторана для перехода к деталям
adapter = RestaurantAdapter(onItemClick = { restaurant ->
    val intent = Intent(this, RestaurantDetailsActivity::class.java)
    intent.putExtra("RESTAURANT_ID", restaurant.id)
    startActivity(intent)
})

// В RestaurantDetailsActivity:
// Возврат назад после завершения действий
backButton.setOnClickListener {
    onBackPressed() 
}
```
