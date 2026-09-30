package com.example.curtineat.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.curtineat.database.Customer
import com.example.curtineat.database.CustomerDao
import com.example.curtineat.database.Notification
import com.example.curtineat.database.NotificationDao
import com.example.curtineat.database.Order
import com.example.curtineat.database.OrderDao
import com.example.curtineat.database.OrderItem
import com.example.curtineat.database.OrderItemDao
import com.example.curtineat.database.Product
import com.example.curtineat.database.ProductDao
import com.example.curtineat.database.VendorDao
import com.example.daodao.Vendor
import kotlinx.coroutines.launch

class AppViewModel(
    private var vendorDao: VendorDao,
    private var customerDao: CustomerDao,
    private var productDao: ProductDao,
    private var notificationDao: NotificationDao,
    private var orderDao: OrderDao,
    private var orderItemDao: OrderItemDao
): ViewModel() {

    var username by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var passwordCheck by mutableStateOf("")
        private set

    var selectedGender by mutableStateOf("")
        private set

    var dob by mutableStateOf("")
        private set

    var search by mutableStateOf("")
        private set

    var cart by mutableStateOf(listOf<Product>())
        private set

    var vendor by mutableStateOf(listOf<Vendor>())
        private set

    var customer by mutableStateOf(listOf<Customer>())
        private set

    var product by mutableStateOf(listOf<Product>())
        private set

    var notification by mutableStateOf(listOf<Notification>())
        private set

    var order by mutableStateOf(listOf<Order>())
        private set

    var orderItem by mutableStateOf(listOf<OrderItem>())
        private set

    var myVendorId by mutableStateOf<Int?>(null)
        private set

    var isVendorMode by mutableStateOf(false)
        private set


    var myStoreName by mutableStateOf("")
        private set

    fun updateVendorMode(value: Boolean) {
        isVendorMode = value
    }

    fun createStore(name: String) = viewModelScope.launch {
        val newId = vendorDao.insertVendor(
            Vendor(
                vendorName = name,
                rating = 0.0,            // placeholders until the store form grows
                category = "",
                distance = 0.0,
                vendorPassword = ""
            )
        )
        myVendorId = newId.toInt()
        myStoreName = name
        refresh()
    }

    fun addProduct(name: String, price: Double, imagePath: String) {
        val vendorId = myVendorId ?: return
        viewModelScope.launch {
            productDao.insertProduct(
                Product(
                    vendorID = vendorId,
                    productName = name,
                    productPrice = price,
                    productImage = imagePath
                )
            )
            refresh()
        }
    }



    init {
        viewModelScope.launch {

            if (
                vendorDao.getAllVendor().isEmpty()              ||
                customerDao.getAllCustomer().isEmpty()          ||
                productDao.getAllProduct().isEmpty()            ||
                notificationDao.getAllNotification().isEmpty()
//              no need to seed order
//              no need to seed order item
            ) {
                seedData()
            }

            refresh()
        }
    }

    fun updateUsername(value: String) {
        username = value
    }

    fun updateEmail(value: String) {
        email = value
    }

    fun updatePassword(value: String) {
        password = value
    }

    fun updatePasswordCheck(value: String) {
        passwordCheck = value
    }

    fun updateGender(value: String) {
        selectedGender = value
    }

    fun updateDob(value: String) {
        dob = value
    }

    fun Login() {
        // Login logic
    }

    fun CreateAccount() {
        // Create account logic
    }

    fun refresh() = viewModelScope.launch {
        vendor          = vendorDao.getAllVendor()
        customer        = customerDao.getAllCustomer()
        product         = productDao.getAllProduct()
        notification    = notificationDao.getAllNotification()
        order           = orderDao.getAllOrder()
        orderItem       = orderItemDao.getAllOrderItem()
    }

    suspend fun clearAll() {
        vendorDao.deleteAllVendor()
        customerDao.deleteAllCustomer()
        productDao.deleteAllProduct()
        notificationDao.deleteAllNotification()
        orderDao.deleteAllOrder()
        orderItemDao.deleteAllOrderItem()
    }

    // vendor dao
    fun insertVendor(vendor: Vendor) = viewModelScope.launch {
        vendorDao.insertVendor(vendor)
        refresh()
    }

    fun updateVendor(vendor: Vendor) = viewModelScope.launch {
        vendorDao.updateVendor(vendor)
        refresh()
    }

    fun deleteVendor(vendor: Vendor) = viewModelScope.launch {
        vendorDao.deleteVendor(vendor)
        refresh()
    }

    // customer dao
    fun insertCustomer(customer: Customer) = viewModelScope.launch {
        customerDao.insertCustomer(customer)
        refresh()
    }

    fun updateCustomer(customer: Customer) = viewModelScope.launch {
        customerDao.updateCustomer(customer)
        refresh()
    }

    fun deleteCustomer(customer: Customer) = viewModelScope.launch {
        customerDao.deleteCustomer(customer)
        refresh()
    }

    // product dao
    fun insertProduct(product: Product) = viewModelScope.launch {
        productDao.insertProduct(product)
        refresh()
    }

    fun updateProduct(product: Product) = viewModelScope.launch {
        productDao.updateProduct(product)
        refresh()
    }

    fun deleteProduct(product: Product) = viewModelScope.launch {
        productDao.deleteProduct(product)
        refresh()
    }

    // notification dao
    fun insertNotification(notification: Notification) = viewModelScope.launch {
        notificationDao.insertNotification(notification)
        refresh()
    }

    fun updateNotification(notification: Notification) = viewModelScope.launch {
        notificationDao.updateNotification(notification)
        refresh()
    }

    fun deleteNotification(notification: Notification) = viewModelScope.launch {
        notificationDao.deleteNotification(notification)
        refresh()
    }

    // order dao
    fun insertOrder(order: Order) = viewModelScope.launch {
        orderDao.insertOrder(order)
        refresh()
    }

    fun updateOrder(order: Order) = viewModelScope.launch {
        orderDao.updateOrder(order)
        refresh()
    }

    fun deleteOrder(order: Order) = viewModelScope.launch {
        orderDao.deleteOrder(order)
        refresh()
    }

    // order item dao
    fun insertOrderItem(orderItem: OrderItem) = viewModelScope.launch {
        orderItemDao.insertOrderItem(orderItem)
        refresh()
    }

    fun updateOrderItem(orderItem: OrderItem) = viewModelScope.launch {
        orderItemDao.updateOrderItem(orderItem)
        refresh()
    }

    fun deleteOrderItem(orderItem: OrderItem) = viewModelScope.launch {
        orderItemDao.deleteOrderItem(orderItem)
        refresh()
    }

    // card method
    fun addToCart(product: Product) = viewModelScope.launch {
        cart += product
    }

    // sumOf is a synchronous function, no launch needed
    fun cartTotalPrice(): Double {
        return cart.sumOf { eachProduct ->
            eachProduct.productPrice
        }
    }


    // please remove this seed data in production
    suspend fun seedData() {
        vendorDao.insertVendor(Vendor(vendorName = "Mama's Kitchen", rating = 4.5, category = "Local Food", distance = 0.3, vendorPassword = "a"))
        vendorDao.insertVendor(Vendor(vendorName = "Burger Bros", rating = 4.2, category = "Western", distance = 0.8, vendorPassword = "b"))
        vendorDao.insertVendor(Vendor(vendorName = "Sushi Zen", rating = 4.8, category = "Japanese", distance = 1.2, vendorPassword = "c"))
        vendorDao.insertVendor(Vendor(vendorName = "Taco Fiesta", rating = 3.9, category = "Mexican", distance = 2.0, vendorPassword = "d"))
        vendorDao.insertVendor(Vendor(vendorName = "Pizza Palace", rating = 4.1, category = "Western", distance = 1.5, vendorPassword = "e"))

        customerDao.insertCustomer(Customer(customerName = "a", customerEmail = "b", customerPassword = "c"))

        productDao.insertProduct(Product(vendorID = 1, productName = "Nasi Lemak", productPrice = 5.50, productImage = "nasi_lemak"))
        productDao.insertProduct(Product(vendorID = 1, productName = "Mee Goreng", productPrice = 6.00, productImage = "mee_goreng"))
        productDao.insertProduct(Product(vendorID = 2, productName = "Cheeseburger", productPrice = 12.90, productImage = "cheeseburger"))
        productDao.insertProduct(Product(vendorID = 2, productName = "Chicken Wings", productPrice = 9.90, productImage = "chicken_wings"))
        productDao.insertProduct(Product(vendorID = 3, productName = "Salmon Sushi", productPrice = 18.00, productImage = "salmon_sushi"))
        productDao.insertProduct(Product(vendorID = 3, productName = "Miso Soup", productPrice = 4.50, productImage = "miso_soup"))
        productDao.insertProduct(Product(vendorID = 4, productName = "Beef Taco", productPrice = 8.90, productImage = "beef_taco"))
        productDao.insertProduct(Product(vendorID = 5, productName = "Margherita Pizza", productPrice = 22.00, productImage = "margherita"))
        productDao.insertProduct(Product(vendorID = 5, productName = "Garlic Bread", productPrice = 5.00, productImage = "garlic_bread"))

        notificationDao.insertNotification(Notification(message = "order submitted, pending for vendor verification", time = "12 p.m."))
        notificationDao.insertNotification(Notification(message = "vendor accept your order, please wait", time = "11 p.m."))
        notificationDao.insertNotification(Notification(message = "food prepared, please pick up or i buang your food", time = "10 p.m."))
        notificationDao.insertNotification(Notification(message = "vendor blocklist you ", time = "14 p.m."))
    }

    // pls remove in production
    var tempOrder by mutableStateOf(listOf<Order>())
        private set

    var tempOrderItem by mutableStateOf(listOf<OrderItem>())
        private set

    fun getTempOrder() = viewModelScope.launch {
        tempOrder = orderDao.getAllOrder()
        tempOrderItem = orderItemDao.getAllOrderItem()
    }
}