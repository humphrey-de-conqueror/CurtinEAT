package com.example.curtineat.database

class OrderRepository(
    private val orderDao: OrderDao
) {

    suspend fun insertOrder(order: Order) {
        orderDao.insertOrder(order)
    }

    suspend fun updateOrder(order: Order) {
        orderDao.updateOrder(order)
    }

    suspend fun deleteOrder(order: Order) {
        orderDao.deleteOrder(order)
    }

    suspend fun getAllOrders(): List<Order> {
        return orderDao.getAllOrders()
    }

    suspend fun getOrderById(orderId: Int): Order? {
        return orderDao.getOrderById(orderId)
    }

    suspend fun getOrdersByStudent(studentId: String): List<Order> {
        return orderDao.getOrdersByStudent(studentId)
    }

    suspend fun getOrdersByVendor(vendorId: Int): List<Order> {
        return orderDao.getOrdersByVendor(vendorId)
    }

    suspend fun getOrdersByStatus(orderStatus: String): List<Order> {
        return orderDao.getOrdersByStatus(orderStatus)
    }

    suspend fun getOrdersByPaymentStatus(paymentStatus: String): List<Order> {
        return orderDao.getOrdersByPaymentStatus(paymentStatus)
    }
}