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
import com.example.curtineat.model.Account
import com.example.curtineat.model.CartItem
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
    var account by mutableStateOf(Account())
        private set

    var searchVendorId by mutableStateOf<Int?>(null)
        private set

    var cart by mutableStateOf(listOf<CartItem>())
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

    //Checkout testing
    var checkoutMessage by mutableStateOf<String?>(null)
        private set



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

    fun addToCart(product: Product) {

        // if cart already has another vendor, clear it
        if (cart.isNotEmpty()) {
            val currentVendorId = cart.first().product.vendorID

            if (currentVendorId != product.vendorID) {
                cart = emptyList()
            }
        }

        val existingItem = cart.find {
            it.product.productId == product.productId
        }

        if (existingItem != null) {
            cart = cart.map {
                if (it.product.productId == product.productId) {
                    it.copy(quantity = it.quantity + 1)
                } else {
                    it
                }
            }
        } else {
            cart = cart + CartItem(product)
        }
    }

    fun cartTotalPrice(): Double {
        return cart.sumOf { cartItem ->
            cartItem.product.productPrice * cartItem.quantity
        }
    }

    fun increaseQuantity(productId: Int) {
        cart = cart.map { cartItem ->
            if (cartItem.product.productId == productId) {
                cartItem.copy(quantity = cartItem.quantity + 1)
            } else {
                cartItem
            }
        }
    }

    fun decreaseQuantity(productId: Int) {
        cart = cart.mapNotNull { cartItem ->
            if (cartItem.product.productId == productId) {
                if (cartItem.quantity > 1) {
                    cartItem.copy(quantity = cartItem.quantity - 1)
                } else {
                    null
                }
            } else {
                cartItem
            }
        }
    }

    fun clearCart() {
        cart = emptyList()
    }

    // search method
    fun searchProduct(productName: String) {
        val productFound = product.find { eachProduct ->
            eachProduct.productName.contains(productName, ignoreCase = true)
        }

        if (productFound != null) {
            searchVendorId = productFound.vendorID
        } else {
            searchVendorId = null
        }
    }

    //for checking out & create order & order item
    fun checkoutTesting(customerId: Int) = viewModelScope.launch {

        if (cart.isEmpty()) {
            return@launch
        }

        val vendorId = cart.first().product.vendorID
        val totalPrice = cartTotalPrice()

        // Create Order
        val order = Order(
            customerId = customerId,
            vendorId = vendorId,
            totalPrice = totalPrice
        )

        // Get actual generated order ID
        val generatedOrderId = orderDao.insertOrder(order)

        val message = StringBuilder()

        message.appendLine("ORDER CREATED")
        message.appendLine("Order ID: $generatedOrderId")
        message.appendLine("Customer ID: $customerId")
        message.appendLine("Vendor ID: $vendorId")
        message.appendLine("Total Price: RM %.2f".format(totalPrice))
        message.appendLine()
        message.appendLine("ORDER ITEMS")
        message.appendLine("--------------------")

        cart.forEachIndexed { index, cartItem ->

            val orderItem = OrderItem(
                orderId = generatedOrderId.toInt(),
                productId = cartItem.product.productId,
                quantity = cartItem.quantity
            )

            // Get actual generated OrderItem ID
            val generatedOrderItemId =
                orderItemDao.insertOrderItem(orderItem)

            val subtotal =
                cartItem.product.productPrice * cartItem.quantity

            message.appendLine("Item ${index + 1}")
            message.appendLine("Order Item ID: $generatedOrderItemId")
            message.appendLine("Order ID: $generatedOrderId")
            message.appendLine("Product ID: ${cartItem.product.productId}")
            message.appendLine("Product Name: ${cartItem.product.productName}")
            message.appendLine("Quantity: ${cartItem.quantity}")
            message.appendLine(
                "Unit Price: RM %.2f".format(
                    cartItem.product.productPrice
                )
            )
            message.appendLine(
                "Subtotal: RM %.2f".format(subtotal)
            )
            message.appendLine("--------------------")
        }

        checkoutMessage = message.toString()

        cart = emptyList()

        refresh()
    }

    //for wallet top up
//    fun topUp(customer: Customer, amount: Double) = viewModelScope.launch {
//        val updatedCustomer = customer.copy(
//            walletBalance = customer.walletBalance + amount
//        )
//
//        customerDao.updateCustomer(updatedCustomer)
//        refresh()
//    }

//    ================================
//          login method
//    ================================

    fun login(
        email: String,
        password: String,
        isVendor: Boolean
    ) {
        if (isVendor) {
            val vendorFound = vendor.find { eachVendor ->
                eachVendor.vendorEmail == email && eachVendor.vendorPassword == password
            }

            if (vendorFound != null) {
                account = account.copy(
                    vendorId = vendorFound.vendorId
                )
            }
        } else {
            val customerFound = customer.find { eachCustomer ->
                eachCustomer.customerEmail == email && eachCustomer.customerPassword == password
            }

            if (customerFound != null) {
                account = account.copy(
                    customerId = customerFound.customerId
                )
            }
        }
    }

    fun register(
        email: String,
        password: String,
        isVendor: Boolean
    ) = viewModelScope.launch {
        if (isVendor) {
            // this must fail for registration to proceed
            val vendorExists = vendor.any { eachVendor ->
                eachVendor.vendorEmail == email
            }

            if (!vendorExists) {
                val newVendor = Vendor(
                    vendorEmail = email,
                    vendorPassword = password
                )

                // Insert into Room
                vendorDao.insertVendor(newVendor)
            }
        } else {
            val customerExists = customer.any { eachCustomer ->
                eachCustomer.customerEmail == email
            }

            if (!customerExists) {
                val newCustomer = Customer(
                    customerEmail = email,
                    customerPassword = password
                )

                // Insert into Room
                customerDao.insertCustomer(newCustomer)
            }
        }
    }



    // please remove this seed data in production
    suspend fun seedData() {
        vendorDao.insertVendor(Vendor(vendorName = "Mama's Kitchen", rating = 4.5, category = "Local Food", distance = 0.3, vendorPassword = "a", vendorEmail = "mamakitchen@gmail.com"))
        vendorDao.insertVendor(Vendor(vendorName = "Burger Bros", rating = 4.2, category = "Western", distance = 0.8, vendorPassword = "b", vendorEmail = "burgerbros@gmail.com"))
        vendorDao.insertVendor(Vendor(vendorName = "Sushi Zen", rating = 4.8, category = "Japanese", distance = 1.2, vendorPassword = "c", vendorEmail = "sushizen@gmail.com"))
        vendorDao.insertVendor(Vendor(vendorName = "Taco Fiesta", rating = 3.9, category = "Mexican", distance = 2.0, vendorPassword = "d", vendorEmail = "tacofiesta@gmail.com"))
        vendorDao.insertVendor(Vendor(vendorName = "Pizza Palace", rating = 4.1, category = "Western", distance = 1.5, vendorPassword = "e", vendorEmail = "pizzapalace@gmail.com"))

        customerDao.insertCustomer(Customer(customerName = "Customer satu", customerEmail = "b", customerPassword = "c",))

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
}