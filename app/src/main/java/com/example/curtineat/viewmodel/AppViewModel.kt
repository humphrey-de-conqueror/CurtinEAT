
package com.example.curtineat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.curtineat.data.local.room.entity.CustomerEntity
import com.example.curtineat.data.local.room.entity.NotificationEntity
import com.example.curtineat.data.local.room.entity.OrderEntity
import com.example.curtineat.data.local.room.entity.ProductEntity
import com.example.curtineat.data.local.room.entity.VendorEntity
import com.example.curtineat.data.remote.firebase.model.RecipientType
import com.example.curtineat.data.repository.image.ImageCacheRepository
import com.example.curtineat.data.repository.sync.CustomerSyncRepository
import com.example.curtineat.data.repository.sync.NotificationSyncRepository
import com.example.curtineat.data.repository.sync.OrderSyncRepository
import com.example.curtineat.data.repository.sync.ProductSyncRepository
import com.example.curtineat.data.repository.sync.VendorSyncRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel(
    private val vendorSync: VendorSyncRepository,
    private val productSync: ProductSyncRepository,
    private val customerSync: CustomerSyncRepository,
    private val orderSync: OrderSyncRepository,
    private val notificationSync: NotificationSyncRepository,
    private val imageCache: ImageCacheRepository
) : ViewModel() {

    // ---------------------------------------------------------
    // Observable application state
    // ---------------------------------------------------------

    private val _vendors =
        MutableStateFlow<List<VendorEntity>>(emptyList())
    val vendors: StateFlow<List<VendorEntity>> =
        _vendors.asStateFlow()

    private val _products =
        MutableStateFlow<List<ProductEntity>>(emptyList())
    val products: StateFlow<List<ProductEntity>> =
        _products.asStateFlow()

    private val _customer =
        MutableStateFlow<CustomerEntity?>(null)
    val customer: StateFlow<CustomerEntity?> =
        _customer.asStateFlow()

    private val _orders =
        MutableStateFlow<List<OrderEntity>>(emptyList())
    val orders: StateFlow<List<OrderEntity>> =
        _orders.asStateFlow()

    private val _notifications =
        MutableStateFlow<List<NotificationEntity>>(emptyList())
    val notifications: StateFlow<List<NotificationEntity>> =
        _notifications.asStateFlow()

    private val _images =
        MutableStateFlow<Map<String, ByteArray>>(emptyMap())
    val images: StateFlow<Map<String, ByteArray>> =
        _images.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> =
        _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> =
        _errorMessage.asStateFlow()

    private var customerObservationJob: Job? = null
    private var orderObservationJob: Job? = null
    private var notificationObservationJob: Job? = null

    private var customerId: String? = null
    private var observedOrderScope: String? = null
    private var recipientId: String? = null
    private var recipientType: RecipientType? = null

    // All ViewModel state mutations occur on the main dispatcher.
    private var activeOperations = 0

    // Prevent duplicate ViewModel requests for the same image.
    private val loadingImageIds = mutableSetOf<String>()

    init {
        observeVendors()
        observeProducts()
    }

    // ---------------------------------------------------------
    // Observe Room data
    // ---------------------------------------------------------

    private fun observeVendors() {
        viewModelScope.launch {
            vendorSync.observeVendors().collect { result ->
                _vendors.value = result
            }
        }
    }

    private fun observeProducts() {
        viewModelScope.launch {
            productSync.observeProducts().collect { result ->
                _products.value = result
            }
        }
    }

    fun observeCustomer(customerId: String) {
        if (
            this.customerId == customerId &&
            customerObservationJob?.isActive == true
        ) {
            return
        }

        this.customerId = customerId
        customerObservationJob?.cancel()

        customerObservationJob = viewModelScope.launch {
            // Start observing Room before refreshing Firebase.
            launch {
                customerSync.observeCustomer(customerId).collect { result ->
                    _customer.value = result
                }
            }

            perform(
                fallbackMessage = "Unable to refresh customer profile"
            ) {
                customerSync.refresh(customerId)
            }
        }
    }

    fun observeOrdersByCustomer(customerId: String) {
        val scope = "customer:$customerId"

        if (
            observedOrderScope == scope &&
            orderObservationJob?.isActive == true
        ) {
            return
        }

        observedOrderScope = scope
        orderObservationJob?.cancel()

        orderObservationJob = viewModelScope.launch {
            // Room continues emitting cached orders even if refresh fails.
            launch {
                orderSync.observeOrdersByCustomer(customerId).collect { result ->
                    _orders.value = result
                }
            }

            perform(
                fallbackMessage = "Unable to refresh customer orders"
            ) {
                orderSync.refreshByCustomer(customerId)
            }
        }
    }

    fun observeOrdersByVendor(vendorId: String) {
        val scope = "vendor:$vendorId"

        if (
            observedOrderScope == scope &&
            orderObservationJob?.isActive == true
        ) {
            return
        }

        observedOrderScope = scope
        orderObservationJob?.cancel()

        orderObservationJob = viewModelScope.launch {
            launch {
                orderSync.observeOrdersByVendor(vendorId).collect { result ->
                    _orders.value = result
                }
            }

            perform(
                fallbackMessage = "Unable to refresh vendor orders"
            ) {
                orderSync.refreshByVendor(vendorId)
            }
        }
    }

    fun observeNotifications(
        id: String,
        type: RecipientType
    ) {
        if (
            recipientId == id &&
            recipientType == type &&
            notificationObservationJob?.isActive == true
        ) {
            return
        }

        recipientId = id
        recipientType = type
        notificationObservationJob?.cancel()

        notificationObservationJob = viewModelScope.launch {
            launch {
                notificationSync
                    .observeNotifications(id, type.name)
                    .collect { result ->
                        _notifications.value = result
                    }
            }

            perform(
                fallbackMessage = "Unable to refresh notifications"
            ) {
                notificationSync.refreshByRecipient(id, type)
            }
        }
    }

    // ---------------------------------------------------------
    // Refresh data from Firebase
    // ---------------------------------------------------------

    fun refreshVendors() {
        execute("Unable to refresh vendors") {
            vendorSync.refresh()
        }
    }

    fun refreshProducts() {
        execute("Unable to refresh products") {
            productSync.refreshAll()
        }
    }

    fun refreshProductsByVendor(vendorId: String) {
        execute("Unable to refresh vendor products") {
            productSync.refreshByVendor(vendorId)
        }
    }

    fun refreshCustomer(customerId: String) {
        execute("Unable to refresh customer profile") {
            customerSync.refresh(customerId)
        }
    }

    fun refreshOrders() {
        execute("Unable to refresh orders") {
            orderSync.refreshAll()
        }
    }

    fun refreshNotifications() {
        val id = recipientId
        val type = recipientType

        execute("Unable to refresh notifications") {
            if (id != null && type != null) {
                notificationSync.refreshByRecipient(id, type)
            } else {
                notificationSync.refreshAll()
            }
        }
    }

    fun refreshAll() {
        refreshVendors()
        refreshProducts()
        refreshOrders()
        refreshNotifications()
    }

    // ---------------------------------------------------------
    // Image cache
    // ---------------------------------------------------------

    fun loadImage(imageId: String) {
        if (
            imageId.isBlank() ||
            _images.value.containsKey(imageId) ||
            !loadingImageIds.add(imageId)
        ) {
            return
        }

        viewModelScope.launch {
            try {
                val bytes = imageCache.getImageBytes(imageId)

                if (bytes != null) {
                    _images.value = _images.value + (imageId to bytes)
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _errorMessage.value = "Unable to load image"
            } finally {
                loadingImageIds.remove(imageId)
            }
        }
    }

    fun removeCachedImage(imageId: String) {
        execute("Unable to remove cached image") {
            imageCache.removeCachedImage(imageId)
            _images.value = _images.value - imageId
        }
    }

    // ---------------------------------------------------------
    // General state helpers
    // ---------------------------------------------------------

    fun clearError() {
        _errorMessage.value = null
    }

    private fun execute(
        fallbackMessage: String,
        action: suspend () -> Unit
    ) {
        viewModelScope.launch {
            perform(fallbackMessage, action)
        }
    }

    private suspend fun perform(
        fallbackMessage: String,
        action: suspend () -> Unit
    ) {
        activeOperations++
        _isLoading.value = true

        try {
            action()
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            _errorMessage.value =
                exception.message ?: fallbackMessage
        } finally {
            activeOperations--
            _isLoading.value = activeOperations > 0
        }
    }
}