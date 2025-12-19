### Листинг 23 — Поля ввода даты и времени с валидацией

Для выбора параметров бронирования используются стандартные поля ввода `EditText`, которые настроены на вызов системных диалогов выбора даты и времени. Это гарантирует корректный формат вводимых данных и упрощает взаимодействие для пользователя (Листинг 23).

```kotlin
// Настройка выбора даты пользователем
dateEditText.setOnClickListener {
    val calendar = Calendar.getInstance()
    DatePickerDialog(this, { _, year, month, day ->
        val selectedDate = String.format("%02d.%02d.%d", day, month + 1, year)
        dateEditText.setText(selectedDate)
    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
}

// Поле ввода в XML макете
/*
<com.google.android.material.textfield.TextInputLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="Выберите дату"
    style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox">

    <com.google.android.material.textfield.TextInputEditText
        android:id="@+id/dateEditText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:focusable="false"
        android:clickable="true" />
</com.google.android.material.textfield.TextInputLayout>
*/
```

### Листинг 24 — Выбор количества гостей через NumberPicker

Для выбора количества человек реализован компонент `NumberPicker`, который ограничивает ввод минимальным и максимальным значением, предотвращая ошибки в бизнес-логике (Листинг 24).

```kotlin
// Настройка компонента выбора количества гостей
val guestsNumberPicker = findViewById<NumberPicker>(R.id.guestsNumberPicker)
guestsNumberPicker.minValue = 1
guestsNumberPicker.maxValue = 10
guestsNumberPicker.value = 1

// Получение значения при оформлении брони
val guestsCount = guestsNumberPicker.value
```

### Листинг 25 — Логика подтверждения бронирования и проверки мест

Вместо финансовых расчетов, приложение выполняет проверку доступности ресурсов ресторана. Метод `createBooking` возвращает результат операции, который обрабатывается на уровне UI для информирования пользователя об успехе или отсутствии мест (Листинг 25).

```kotlin
// Финальное подтверждение бронирования
bookButton.setOnClickListener {
    val date = dateEditText.text.toString()
    val time = timeEditText.text.toString()
    val guests = guestsNumberPicker.value

    if (date.isEmpty() || time.isEmpty()) {
        Toast.makeText(this, "Необходимо заполнить все поля", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
    }

    lifecycleScope.launch {
        // Вызов бизнес-логики через ViewModel
        val success = viewModel.createBooking(userId, restaurantId, date, time, guests)
        
        if (success) {
            // Визуальное подтверждение успеха
            showSuccessDialog("Столик успешно забронирован на $date в $time")
            finish()
        } else {
            // Информирование о нехватке мест
            showErrorNotification("К сожалению, на это время свободных мест нет")
        }
    }
}
```
