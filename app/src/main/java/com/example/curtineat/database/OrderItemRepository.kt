package com.example.curtineat.database

class OrderItemRepository(
    private val orderItemDao: OrderItemDao
) {

    suspend fun insertOrderItem(orderItem: OrderItem) {
        orderItemDao.insertOrderItem(orderItem)
    }

    suspend fun updateOrderItem(orderItem: OrderItem) {
        orderItemDao.updateOrderItem(orderItem)
    }

    suspend fun deleteOrderItem(orderItem: OrderItem) {
        orderItemDao.deleteOrderItem(orderItem)
    }

    suspend fun getAllOrderItems(): List<OrderItem> {
        return orderItemDao.getAllOrderItems()
    }

    suspend fun getOrderItemById(orderItemId: Int): OrderItem? {
        return orderItemDao.getOrderItemById(orderItemId)
    }

    suspend fun getOrderItemsByOrder(orderId: Int): List<OrderItem> {
        return orderItemDao.getOrderItemsByOrder(orderId)
    }

    suspend fun getOrderItemsByProduct(productId: Int): List<OrderItem> {
        return orderItemDao.getOrderItemsByProduct(productId)
    }
}