### Листинг 4 — Метод бронирования с проверкой условий

Метод создания бронирования в репозитории проверяет множество условий, таких как наличие свободных столиков в выбранное время, прежде чем подтвердить операцию (Листинг 4).

```kotlin
suspend fun createBooking(userId: Int, restaurantId: Int, date: String, time: String, guests: Int): Boolean {
    val restaurant = restaurantDao.getRestaurantById(restaurantId) ?: return false
    val currentBookings = bookingDao.countBookingsByRestaurantAndDateTime(restaurantId, date, time)

    // Проверка наличия свободных мест (столиков) в ресторане
    if (currentBookings >= restaurant.tableCount) {
         return false // Места закончились
    }

    val booking = Booking(
        userId = userId,
        restaurantId = restaurantId,
        date = date,
        time = time,
        guests = guests
    )
    bookingDao.insert(booking)
    return true
}
```

### Листинг 5 — Разделение прав доступа и удаление данных

В приложении реализовано строгое разделение прав доступа: обычный пользователь может отменять только свои бронирования, в то время как супер-администратор обладает привилегиями на удаление любых бронирований, отзывов и даже самих заведений (Листинг 5).

```kotlin
// Удаление бронирования (доступно пользователю для своих и админу для всех)
suspend fun deleteBooking(bookingId: Int) {
    bookingDao.deleteBookingById(bookingId)
}

// Модерация: Удаление отзыва (только для супер-администратора)
suspend fun deleteReview(reviewId: Int) {
    reviewDao.deleteById(reviewId)
}

// Модерация: Удаление ресторана (только для супер-администратора)
suspend fun deleteRestaurant(restaurant: Restaurant) {
    restaurantDao.delete(restaurant)
}
```

### Листинг 6 — Методы получения данных для различных экранов

В проекте реализована гибкая система методов для получения отфильтрованных данных в соответствии с потребностями различных экранов приложения (Листинг 6).

```kotlin
// Для главного экрана пользователя (все доступные рестораны с рейтингом)
suspend fun getAllRestaurants(): List<RestaurantWithRating> {
    return restaurantDao.getAllRestaurantsWithRating()
}

// Для экрана текущих бронирований пользователя
suspend fun getUserBookings(userId: Int): List<BookingWithRestaurant> {
    return bookingDao.getUserBookings(userId)
}

// Для панели супер-администратора (все бронирования в системе)
suspend fun getAllBookings(): List<BookingWithRestaurant> {
    return bookingDao.getAllBookingsWithRestaurant()
}

// Для страницы деталей конкретного ресторана (список отзывов)
suspend fun getReviewsForRestaurant(restaurantId: Int): List<Review> {
    return reviewDao.getReviewsForRestaurant(restaurantId)
}
```
