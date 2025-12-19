### Листинг 17 — Информационная карточка приветствия

Для повышения лояльности пользователя на главном экране может быть реализована информационная карточка с персональным приветствием. Она строится на базе `MaterialCardView`, что обеспечивает современный внешний вид с тенями и закруглениями (Листинг 17).

```xml
<com.google.android.material.card.MaterialCardView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="16dp"
    app:cardCornerRadius="16dp"
    app:cardBackgroundColor="?attr/colorPrimaryContainer">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="20dp">

        <TextView
            android:id="@+id/welcomeTextView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Добро пожаловать!"
            android:textSize="20sp"
            android:textStyle="bold"
            android:textColor="?attr/colorOnPrimaryContainer" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Забронируйте столик за пару кликов"
            android:alpha="0.8"
            android:textColor="?attr/colorOnPrimaryContainer" />
            
    </LinearLayout>
</com.google.android.material.card.MaterialCardView>
```

### Листинг 18 — Основные навигационные действия

Доступ к ключевым функциям приложения реализован через интерактивные элементы управления. В зависимости от роли пользователя (гость или администратор), набор доступных действий изменяется, что обеспечивается логикой в `Activity` (Листинг 18).

```kotlin
// Пример настройки действий для пользователя:
// 1. Поиск ресторана (выбор из списка)
// 2. Просмотр своих бронирований (горизонтальный список)
// 3. Написание отзыва (в деталях ресторана)

// Пример настройки действия для администратора:
addRestaurantButton.setOnClickListener {
    // Логика перехода к созданию нового заведения
    val intent = Intent(this, AddRestaurantActivity::class.java)
    startActivity(intent)
}
```

### Листинг 19 — Компонент интерактивного элемента (карточка ресторана)

Вместо простых кнопок в приложении активно используются интерактивные карточки (`MaterialCardView`). Они позволяют группировать иконки, текстовые описания и рейтинги в единый визуальный блок с поддержкой эффекта нажатия (Ripple effect) (Листинг 19).

```xml
<!-- Структура интерактивного элемента на основе Material Design -->
<com.google.android.material.card.MaterialCardView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:clickable="true"
    android:focusable="true"
    android:foreground="?android:attr/selectableItemBackground"
    app:cardCornerRadius="12dp"
    app:cardBackgroundColor="?attr/colorSurface">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:padding="16dp"
        android:gravity="center_vertical">

        <ImageView
            android:layout_width="32dp"
            android:layout_height="32dp"
            android:src="@drawable/ic_restaurant"
            app:tint="?attr/colorPrimary" />

        <LinearLayout
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:layout_marginStart="16dp"
            android:orientation="vertical">

            <TextView
                android:id="@+id/itemTitle"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:textStyle="bold"
                android:textSize="16sp" />

            <TextView
                android:id="@+id/itemDescription"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:textSize="12sp"
                android:alpha="0.7" />
        </LinearLayout>
    </LinearLayout>
</com.google.android.material.card.MaterialCardView>
```
