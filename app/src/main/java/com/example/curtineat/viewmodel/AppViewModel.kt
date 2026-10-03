package com.example.curtineat.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.curtineat.data.remote.firebase.model.FirebaseCustomerData
import com.example.curtineat.data.remote.firebase.model.FirebaseNotificationData
import com.example.curtineat.data.remote.firebase.model.FirebaseOrderData
import com.example.curtineat.data.remote.firebase.model.FirebaseOrderProductData
import com.example.curtineat.data.remote.firebase.model.FirebaseProductData
import com.example.curtineat.data.remote.firebase.model.FirebaseVendorData
import com.example.curtineat.data.repository.firebase.FirebaseCustomerRepository
import com.example.curtineat.data.repository.firebase.FirebaseNotificationRepository
import com.example.curtineat.data.repository.firebase.FirebaseOrderRepository
import com.example.curtineat.data.repository.firebase.FirebaseProductRepository
import com.example.curtineat.data.repository.firebase.FirebaseVendorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.curtineat.model.CartItem
import com.example.curtineat.model.Account


class AppViewModel(
    private val vendorRepository: FirebaseVendorRepository,
    private val customerRepository: FirebaseCustomerRepository,
    private val productRepository: FirebaseProductRepository,
    private val orderRepository: FirebaseOrderRepository,
    private val notificationRepository: FirebaseNotificationRepository
) : ViewModel() {

    /* ====================
     * Account
     * ==================== */

    private val _account = MutableStateFlow(Account())

    val account: StateFlow<Account> =
        _account.asStateFlow()

    /* ====================
     * Vendors
     * ==================== */

    private val _vendors =
        MutableStateFlow<List<FirebaseVendorData>>(emptyList())

    val vendors: StateFlow<List<FirebaseVendorData>> =
        _vendors.asStateFlow()

    /* ====================
     * Customers
     * ==================== */

    private val _customers =
        MutableStateFlow<List<FirebaseCustomerData>>(emptyList())

    val customers: StateFlow<List<FirebaseCustomerData>> =
        _customers.asStateFlow()

    /* ====================
     * Products
     * ==================== */

    private val _products =
        MutableStateFlow<List<FirebaseProductData>>(emptyList())

    val products: StateFlow<List<FirebaseProductData>> =
        _products.asStateFlow()

    /* ====================
     * Orders
     * ==================== */

    private val _orders =
        MutableStateFlow<List<FirebaseOrderData>>(emptyList())

    val orders: StateFlow<List<FirebaseOrderData>> =
        _orders.asStateFlow()

    /* ====================
     * Notifications
     * ==================== */

    private val _notifications =
        MutableStateFlow<List<FirebaseNotificationData>>(emptyList())

    val notifications: StateFlow<List<FirebaseNotificationData>> =
        _notifications.asStateFlow()

    /* ====================
     * Cart
     * ==================== */

    private val _cart =
        MutableStateFlow<List<CartItem>>(emptyList())

    val cart: StateFlow<List<CartItem>> =
        _cart.asStateFlow()

    /* ====================
     * Search
     * ==================== */

    private val _searchVendorId =
        MutableStateFlow<String?>(null)

    val searchVendorId: StateFlow<String?> =
        _searchVendorId.asStateFlow()

    /* ====================
     * Checkout
     * ==================== */

    private val _checkoutCompleted =
        MutableStateFlow(false)

    val checkoutCompleted: StateFlow<Boolean> =
        _checkoutCompleted.asStateFlow()

    /* ====================
     * Vendor
     * ==================== */

    fun loadVendors() {

        viewModelScope.launch {

            try {
                _vendors.value =
                    vendorRepository.getAllVendors()

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to load vendors",
                    e
                )
            }
        }
    }

    fun getVendorById(
        vendorId: String,
        onResult: (FirebaseVendorData?) -> Unit
    ) {

        viewModelScope.launch {

            try {
                onResult(
                    vendorRepository.getVendorById(vendorId)
                )

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to get vendor",
                    e
                )

                onResult(null)
            }
        }
    }

    fun addVendor(
        vendor: FirebaseVendorData,
        onResult: (Boolean) -> Unit = {}
    ) {

        viewModelScope.launch {

            try {
                vendorRepository.addVendor(vendor)
                onResult(true)

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to add vendor",
                    e
                )

                onResult(false)
            }
        }
    }

    fun updateVendor(
        vendor: FirebaseVendorData,
        onResult: (Boolean) -> Unit = {}
    ) {

        viewModelScope.launch {

            try {
                vendorRepository.updateVendor(vendor)
                onResult(true)

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to update vendor",
                    e
                )

                onResult(false)
            }
        }
    }

    fun deleteVendor(
        vendorId: String,
        onResult: (Boolean) -> Unit = {}
    ) {

        viewModelScope.launch {

            try {
                vendorRepository.deleteVendor(vendorId)
                onResult(true)

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to delete vendor",
                    e
                )

                onResult(false)
            }
        }
    }

    /* ====================
     * Customer
     * ==================== */

    fun loadCustomers() {

        viewModelScope.launch {

            try {
                _customers.value =
                    customerRepository.getAllCustomers()

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to load customers",
                    e
                )
            }
        }
    }

    fun getCustomerById(
        customerId: String,
        onResult: (FirebaseCustomerData?) -> Unit
    ) {

        viewModelScope.launch {

            try {
                onResult(
                    customerRepository.getCustomerById(customerId)
                )

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to get customer",
                    e
                )

                onResult(null)
            }
        }
    }

    fun addCustomer(
        customer: FirebaseCustomerData,
        onResult: (Boolean) -> Unit = {}
    ) {

        viewModelScope.launch {

            try {
                customerRepository.addCustomer(customer)
                onResult(true)

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to add customer",
                    e
                )

                onResult(false)
            }
        }
    }

    fun updateCustomer(
        customer: FirebaseCustomerData,
        onResult: (Boolean) -> Unit = {}
    ) {

        viewModelScope.launch {

            try {
                customerRepository.updateCustomer(customer)
                onResult(true)

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to update customer",
                    e
                )

                onResult(false)
            }
        }
    }

    fun deleteCustomer(
        customerId: String,
        onResult: (Boolean) -> Unit = {}
    ) {

        viewModelScope.launch {

            try {
                customerRepository.deleteCustomer(customerId)
                onResult(true)

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to delete customer",
                    e
                )

                onResult(false)
            }
        }
    }

    /* ====================
     * Product
     * ==================== */

    fun loadProducts() {

        viewModelScope.launch {

            try {
                _products.value =
                    productRepository.getAllProducts()

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to load products",
                    e
                )
            }
        }
    }

    fun loadProductsByVendor(
        vendorId: String
    ) {

        viewModelScope.launch {

            try {
                _products.value =
                    productRepository.getProductsByVendorId(
                        vendorId
                    )

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to load vendor products",
                    e
                )
            }
        }
    }

    fun getProductById(
        productId: String,
        onResult: (FirebaseProductData?) -> Unit
    ) {

        viewModelScope.launch {

            try {
                onResult(
                    productRepository.getProductById(productId)
                )

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to get product",
                    e
                )

                onResult(null)
            }
        }
    }

    fun addProduct(
        product: FirebaseProductData,
        onResult: (Boolean) -> Unit = {}
    ) {

        viewModelScope.launch {

            try {
                productRepository.addProduct(product)
                onResult(true)

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to add product",
                    e
                )

                onResult(false)
            }
        }
    }

    fun updateProduct(
        product: FirebaseProductData,
        onResult: (Boolean) -> Unit = {}
    ) {

        viewModelScope.launch {

            try {
                productRepository.updateProduct(product)
                onResult(true)

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to update product",
                    e
                )

                onResult(false)
            }
        }
    }

    fun deleteProduct(
        productId: String,
        onResult: (Boolean) -> Unit = {}
    ) {

        viewModelScope.launch {

            try {
                productRepository.deleteProduct(productId)
                onResult(true)

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to delete product",
                    e
                )

                onResult(false)
            }
        }
    }

    /* ====================
     * Orders
     * ==================== */

    fun loadOrders() {

        viewModelScope.launch {

            try {
                _orders.value =
                    orderRepository.getAllOrders()

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to load orders",
                    e
                )
            }
        }
    }

    fun loadCustomerOrders(
        customerId: String
    ) {

        viewModelScope.launch {

            try {
                _orders.value =
                    orderRepository.getOrdersByCustomerId(
                        customerId
                    )

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to load customer orders",
                    e
                )
            }
        }
    }

    fun loadVendorOrders(
        vendorId: String
    ) {

        viewModelScope.launch {

            try {
                _orders.value =
                    orderRepository.getOrdersByVendorId(
                        vendorId
                    )

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to load vendor orders",
                    e
                )
            }
        }
    }

    fun getOrderById(
        orderId: String,
        onResult: (FirebaseOrderData?) -> Unit
    ) {

        viewModelScope.launch {

            try {
                onResult(
                    orderRepository.getOrderById(orderId)
                )

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to get order",
                    e
                )

                onResult(null)
            }
        }
    }

    fun addOrder(
        order: FirebaseOrderData,
        onResult: (Boolean) -> Unit = {}
    ) {

        viewModelScope.launch {

            try {
                orderRepository.addOrder(order)
                onResult(true)

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to add order",
                    e
                )

                onResult(false)
            }
        }
    }

    fun updateOrder(
        order: FirebaseOrderData,
        onResult: (Boolean) -> Unit = {}
    ) {

        viewModelScope.launch {

            try {
                orderRepository.updateOrder(order)
                onResult(true)

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to update order",
                    e
                )

                onResult(false)
            }
        }
    }

    fun deleteOrder(
        orderId: String,
        onResult: (Boolean) -> Unit = {}
    ) {

        viewModelScope.launch {

            try {
                orderRepository.deleteOrder(orderId)
                onResult(true)

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to delete order",
                    e
                )

                onResult(false)
            }
        }
    }

    /* ====================
     * Notifications
     * ==================== */

    fun loadNotifications() {

        viewModelScope.launch {

            try {
                _notifications.value =
                    notificationRepository.getAllNotifications()

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to load notifications",
                    e
                )
            }
        }
    }

    fun loadNotificationsForRecipient(
        recipientId: String
    ) {

        viewModelScope.launch {

            try {
                _notifications.value =
                    notificationRepository
                        .getNotificationsByRecipientId(
                            recipientId
                        )

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to load recipient notifications",
                    e
                )
            }
        }
    }

    fun getNotificationById(
        notificationId: String,
        onResult: (FirebaseNotificationData?) -> Unit
    ) {

        viewModelScope.launch {

            try {
                onResult(
                    notificationRepository
                        .getNotificationById(notificationId)
                )

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to get notification",
                    e
                )

                onResult(null)
            }
        }
    }

    fun addNotification(
        notification: FirebaseNotificationData,
        onResult: (Boolean) -> Unit = {}
    ) {

        viewModelScope.launch {

            try {
                notificationRepository
                    .addNotification(notification)

                onResult(true)

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to add notification",
                    e
                )

                onResult(false)
            }
        }
    }

    fun updateNotification(
        notification: FirebaseNotificationData,
        onResult: (Boolean) -> Unit = {}
    ) {

        viewModelScope.launch {

            try {
                notificationRepository
                    .updateNotification(notification)

                onResult(true)

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to update notification",
                    e
                )

                onResult(false)
            }
        }
    }

    fun deleteNotification(
        notificationId: String,
        onResult: (Boolean) -> Unit = {}
    ) {

        viewModelScope.launch {

            try {
                notificationRepository
                    .deleteNotification(notificationId)

                onResult(true)

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to delete notification",
                    e
                )

                onResult(false)
            }
        }
    }

    /* ====================
     * Authentication
     * ==================== */

    fun login(
        email: String,
        password: String,
        isVendor: Boolean,
        onResult: (Boolean) -> Unit = {}
    ) {
        println(
            "TODO: Firebase Authentication login " +
                    "for ${if (isVendor) "vendor" else "customer"}"
        )

        onResult(false)
    }

    fun register(
        email: String,
        password: String,
        isVendor: Boolean,
        onResult: (RegisterResult) -> Unit = {}
    ) {
        println(
            "TODO: Firebase Authentication registration " +
                    "for ${if (isVendor) "vendor" else "customer"}"
        )

        onResult(RegisterResult.ERROR)
    }

    fun logout() {
        println("TODO: Firebase Authentication logout")

        clearAccount()
        clearCart()
    }

    /* ====================
     * Account
     * ==================== */

    fun setCustomerAccount(
        customerId: String
    ) {
        _account.value =
            Account(
                customerId = customerId
            )
    }

    fun setVendorAccount(
        vendorId: String
    ) {
        _account.value =
            Account(
                vendorId = vendorId
            )
    }

    fun clearAccount() {
        _account.value = Account()
    }

    /* ====================
     * Wallet
     * ==================== */

    fun getCurrentBalance(
        onResult: (Double?) -> Unit
    ) {

        viewModelScope.launch {

            try {

                val currentAccount = _account.value

                if (currentAccount.customerId != null) {

                    val customer =
                        customerRepository.getCustomerById(
                            currentAccount.customerId
                        )

                    onResult(
                        customer?.moneyBalance
                    )

                    return@launch
                }

                if (currentAccount.vendorId != null) {

                    val vendor =
                        vendorRepository.getVendorById(
                            currentAccount.vendorId
                        )

                    onResult(
                        vendor?.moneyBalance
                    )

                    return@launch
                }

                onResult(null)

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Failed to get current balance",
                    e
                )

                onResult(null)
            }
        }
    }

    fun topUp(
        amount: Double,
        onResult: (TopUpResult) -> Unit = {}
    ) {

        if (amount <= 0.0) {
            onResult(
                TopUpResult.INVALID_AMOUNT
            )

            return
        }

        println(
            "TODO: Implement payment processing " +
                    "for top-up of $amount"
        )

        onResult(
            TopUpResult.ERROR
        )
    }

    /* ====================
     * Cart
     * ==================== */

    fun addToCart(
        product: FirebaseProductData
    ) {

        val currentCart =
            _cart.value.toMutableList()

        if (
            currentCart.isNotEmpty() &&
            currentCart.first().product.vendorId !=
            product.vendorId
        ) {
            currentCart.clear()
        }

        val existingIndex =
            currentCart.indexOfFirst {
                it.product.productId ==
                        product.productId
            }

        if (existingIndex >= 0) {

            val existingItem =
                currentCart[existingIndex]

            currentCart[existingIndex] =
                existingItem.copy(
                    quantity =
                        existingItem.quantity + 1
                )

        } else {

            currentCart.add(
                CartItem(
                    product = product,
                    quantity = 1
                )
            )
        }

        _cart.value = currentCart
    }

    fun increaseQuantity(
        productId: String
    ) {

        _cart.value =
            _cart.value.map { item ->

                if (
                    item.product.productId ==
                    productId
                ) {
                    item.copy(
                        quantity =
                            item.quantity + 1
                    )
                } else {
                    item
                }
            }
    }

    fun decreaseQuantity(
        productId: String
    ) {

        _cart.value =
            _cart.value.mapNotNull { item ->

                if (
                    item.product.productId !=
                    productId
                ) {
                    item

                } else if (
                    item.quantity > 1
                ) {
                    item.copy(
                        quantity =
                            item.quantity - 1
                    )

                } else {
                    null
                }
            }
    }

    fun clearCart() {
        _cart.value = emptyList()
    }

    fun cartTotalPrice(): Double {

        return _cart.value.sumOf { item ->
            item.product.productPrice *
                    item.quantity
        }
    }

    /* ====================
     * Search
     * ==================== */

    fun searchProduct(
        productName: String
    ) {

        val result =
            _products.value.firstOrNull { product ->

                product.productName.equals(
                    productName,
                    ignoreCase = true
                )
            }

        _searchVendorId.value =
            result?.vendorId
    }

    fun clearSearch() {
        _searchVendorId.value = null
    }

    /* ====================
     * Checkout
     * ==================== */

    fun checkout(
        customerId: String,
        onResult: (Boolean, String) -> Unit =
            { _, _ -> }
    ) {

        viewModelScope.launch {

            try {

                if (_cart.value.isEmpty()) {

                    onResult(
                        false,
                        "Cart is empty."
                    )

                    return@launch
                }

                val vendorId =
                    _cart.value
                        .first()
                        .product
                        .vendorId

                val totalPrice =
                    cartTotalPrice()

                val customer =
                    customerRepository
                        .getCustomerById(
                            customerId
                        )

                if (customer == null) {

                    onResult(
                        false,
                        "Customer not found."
                    )

                    return@launch
                }

                val vendor =
                    vendorRepository
                        .getVendorById(
                            vendorId
                        )

                if (vendor == null) {

                    onResult(
                        false,
                        "Vendor not found."
                    )

                    return@launch
                }

                if (
                    customer.moneyBalance <
                    totalPrice
                ) {

                    onResult(
                        false,
                        "Insufficient balance."
                    )

                    return@launch
                }

                val orderProducts =
                    _cart.value.map { item ->

                        FirebaseOrderProductData(
                            productId =
                                item.product.productId,

                            productName =
                                item.product.productName,

                            productPrice =
                                item.product.productPrice,

                            quantity =
                                item.quantity
                        )
                    }

                val order =
                    FirebaseOrderData(
                        customerId =
                            customerId,

                        vendorId =
                            vendorId,

                        totalPrice =
                            totalPrice,

                        status =
                            "PENDING",

                        products =
                            orderProducts
                    )

                orderRepository.addOrder(order)

                customerRepository.updateCustomer(
                    customer.copy(
                        moneyBalance =
                            customer.moneyBalance -
                                    totalPrice
                    )
                )

                vendorRepository.updateVendor(
                    vendor.copy(
                        moneyBalance =
                            vendor.moneyBalance +
                                    totalPrice
                    )
                )

                _cart.value = emptyList()

                _checkoutCompleted.value =
                    true

                onResult(
                    true,
                    "Checkout successful."
                )

            } catch (e: Exception) {

                Log.e(
                    "AppViewModel",
                    "Checkout failed",
                    e
                )

                onResult(
                    false,
                    e.message ?: "Checkout failed."
                )
            }
        }
    }

    fun clearCheckoutCompleted() {
        _checkoutCompleted.value = false
    }
}