
package com.example.curtineat.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.curtineat.data.local.room.entity.CustomerEntity
import com.example.curtineat.data.local.room.entity.NotificationEntity
import com.example.curtineat.data.local.room.entity.OrderEntity
import com.example.curtineat.data.local.room.entity.ProductEntity
import com.example.curtineat.data.local.room.entity.VendorEntity
import com.example.curtineat.data.remote.firebase.model.RecipientType
import com.example.curtineat.data.repository.api.ImageUploadRepository
import com.example.curtineat.data.repository.auth.AuthenticationRepository
import com.example.curtineat.data.repository.auth.LoginResult
import com.example.curtineat.data.repository.image.ImageCacheRepository
import com.example.curtineat.data.repository.sync.CustomerSyncRepository
import com.example.curtineat.data.repository.sync.NotificationSyncRepository
import com.example.curtineat.data.repository.sync.OrderSyncRepository
import com.example.curtineat.data.repository.sync.ProductSyncRepository
import com.example.curtineat.data.repository.sync.VendorSyncRepository
import com.example.curtineat.viewmodel.state.AuthenticationUiState
import com.example.curtineat.viewmodel.state.DataUiState
import com.example.curtineat.viewmodel.state.ImageUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(
    private val vendorSync: VendorSyncRepository,
    private val productSync: ProductSyncRepository,
    private val customerSync: CustomerSyncRepository,
    private val orderSync: OrderSyncRepository,
    private val notificationSync: NotificationSyncRepository,
    private val imageCache: ImageCacheRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val imageUploadRepository: ImageUploadRepository
) : ViewModel() {

    // ---------------------------------------------------------
    // UI state
    // ---------------------------------------------------------

    private val _vendorState =
        MutableStateFlow(DataUiState<List<VendorEntity>>(emptyList()))

    val vendorState: StateFlow<DataUiState<List<VendorEntity>>> =
        _vendorState.asStateFlow()

    private val _productState =
        MutableStateFlow(DataUiState<List<ProductEntity>>(emptyList()))

    val productState: StateFlow<DataUiState<List<ProductEntity>>> =
        _productState.asStateFlow()

    private val _customerState =
        MutableStateFlow(DataUiState<CustomerEntity?>(null))

    val customerState: StateFlow<DataUiState<CustomerEntity?>> =
        _customerState.asStateFlow()

    private val _vendorProfileState =
        MutableStateFlow(DataUiState<VendorEntity?>(null))

    val vendorProfileState: StateFlow<DataUiState<VendorEntity?>> =
        _vendorProfileState.asStateFlow()

    private val _orderState =
        MutableStateFlow(DataUiState<List<OrderEntity>>(emptyList()))

    val orderState: StateFlow<DataUiState<List<OrderEntity>>> =
        _orderState.asStateFlow()

    private val _notificationState =
        MutableStateFlow(DataUiState<List<NotificationEntity>>(emptyList()))

    val notificationState: StateFlow<DataUiState<List<NotificationEntity>>> =
        _notificationState.asStateFlow()

    private val _imageStates =
        MutableStateFlow<Map<String, ImageUiState>>(emptyMap())

    val imageStates: StateFlow<Map<String, ImageUiState>> =
        _imageStates.asStateFlow()

    private var customerObservationJob: Job? = null
    private var vendorProfileObservationJob: Job? = null
    private var orderObservationJob: Job? = null
    private var notificationObservationJob: Job? = null

    private var observedCustomerId: String? = null
    private var observedVendorId: String? = null
    private var observedOrderScope: String? = null
    private var observedRecipientId: String? = null
    private var observedRecipientType: RecipientType? = null

    init {
        observeVendors()
        observeProducts()
    }

    // ---------------------------------------------------------
    // Observe cached Room data
    // ---------------------------------------------------------

    private fun observeVendors() {
        viewModelScope.launch {
            vendorSync.observeVendors().collect { vendors ->
                _vendorState.value = _vendorState.value.copy(
                    data = vendors,
                    hasLoaded = true,
                    isInitialLoading = false
                )
            }
        }
    }

    private fun observeProducts() {
        viewModelScope.launch {
            productSync.observeProducts().collect { products ->
                _productState.value = _productState.value.copy(
                    data = products,
                    hasLoaded = true,
                    isInitialLoading = false
                )
            }
        }
    }

    fun observeCustomer(customerId: String) {
        if (
            observedCustomerId == customerId &&
            customerObservationJob?.isActive == true
        ) {
            return
        }

        observedCustomerId = customerId
        customerObservationJob?.cancel()

        _customerState.value = DataUiState(
            data = _customerState.value.data,
            isInitialLoading = true
        )

        customerObservationJob = viewModelScope.launch {
            launch {
                customerSync.observeCustomer(customerId).collect { customer ->
                    _customerState.value = _customerState.value.copy(
                        data = customer,
                        hasLoaded = true,
                        isInitialLoading = false
                    )
                }
            }

            refreshCustomer(customerId)
        }
    }

    fun observeVendorProfile(vendorId: String) {
        if (
            observedVendorId == vendorId &&
            vendorProfileObservationJob?.isActive == true
        ) {
            return
        }

        observedVendorId = vendorId
        vendorProfileObservationJob?.cancel()

        _vendorProfileState.value = DataUiState(
            data = null,
            isInitialLoading = true
        )

        vendorProfileObservationJob = viewModelScope.launch {
            launch {
                vendorSync.observeVendor(vendorId).collect { vendor ->
                    _vendorProfileState.value =
                        _vendorProfileState.value.copy(
                            data = vendor,
                            hasLoaded = true,
                            isInitialLoading = false
                        )
                }
            }

            perform(
                getState = { _vendorProfileState.value },
                setState = { _vendorProfileState.value = it },
                fallbackMessage = "Unable to refresh vendor profile"
            ) {
                vendorSync.refresh()
            }
        }
    }

    fun observeOrdersByCustomer(customerId: String) {
        observeOrderScope(
            scope = "customer:$customerId",
            observe = {
                orderSync.observeOrdersByCustomer(customerId)
            },
            refresh = {
                orderSync.refreshByCustomer(customerId)
            }
        )
    }

    fun observeOrdersByVendor(vendorId: String) {
        observeOrderScope(
            scope = "vendor:$vendorId",
            observe = {
                orderSync.observeOrdersByVendor(vendorId)
            },
            refresh = {
                orderSync.refreshByVendor(vendorId)
            }
        )
    }

    private fun observeOrderScope(
        scope: String,
        observe: () -> Flow<List<OrderEntity>>,
        refresh: suspend () -> Unit
    ) {
        if (
            observedOrderScope == scope &&
            orderObservationJob?.isActive == true
        ) {
            return
        }

        observedOrderScope = scope
        orderObservationJob?.cancel()

        _orderState.value = DataUiState(
            data = emptyList(),
            isInitialLoading = true
        )

        orderObservationJob = viewModelScope.launch {
            launch {
                observe().collect { orders ->
                    _orderState.value = _orderState.value.copy(
                        data = orders,
                        hasLoaded = true,
                        isInitialLoading = false
                    )
                }
            }

            refreshOrdersInScope(refresh)
        }
    }

    private suspend fun refreshOrdersInScope(
        refresh: suspend () -> Unit
    ) {
        perform(
            getState = { _orderState.value },
            setState = { _orderState.value = it },
            fallbackMessage = "Unable to refresh orders",
            action = refresh
        )
    }

    fun observeNotifications(
        recipientId: String,
        type: RecipientType
    ) {
        if (
            observedRecipientId == recipientId &&
            observedRecipientType == type &&
            notificationObservationJob?.isActive == true
        ) {
            return
        }

        observedRecipientId = recipientId
        observedRecipientType = type
        notificationObservationJob?.cancel()

        _notificationState.value = DataUiState(
            data = emptyList(),
            isInitialLoading = true
        )

        notificationObservationJob = viewModelScope.launch {
            launch {
                notificationSync
                    .observeNotifications(recipientId, type.name)
                    .collect { notifications ->
                        _notificationState.value =
                            _notificationState.value.copy(
                                data = notifications,
                                hasLoaded = true,
                                isInitialLoading = false
                            )
                    }
            }

            refreshNotificationsFor(recipientId, type)
        }
    }

    private suspend fun refreshNotificationsFor(
        recipientId: String,
        type: RecipientType
    ) {
        perform(
            getState = { _notificationState.value },
            setState = { _notificationState.value = it },
            fallbackMessage = "Unable to refresh notifications"
        ) {
            notificationSync.refreshByRecipient(recipientId, type)
        }
    }

    // ---------------------------------------------------------
    // Refresh operations
    // ---------------------------------------------------------

    fun refreshVendors() {
        execute(
            getState = { _vendorState.value },
            setState = { _vendorState.value = it },
            fallbackMessage = "Unable to refresh vendors"
        ) {
            vendorSync.refresh()
        }
    }

    fun refreshProducts() {
        execute(
            getState = { _productState.value },
            setState = { _productState.value = it },
            fallbackMessage = "Unable to refresh products"
        ) {
            productSync.refreshAll()
        }
    }

    fun refreshProductsByVendor(vendorId: String) {
        execute(
            getState = { _productState.value },
            setState = { _productState.value = it },
            fallbackMessage = "Unable to refresh vendor products"
        ) {
            productSync.refreshByVendor(vendorId)
        }
    }

    fun refreshCustomer(customerId: String) {
        execute(
            getState = { _customerState.value },
            setState = { _customerState.value = it },
            fallbackMessage = "Unable to refresh customer profile"
        ) {
            customerSync.refresh(customerId)
        }
    }

    fun refreshOrders() {
        execute(
            getState = { _orderState.value },
            setState = { _orderState.value = it },
            fallbackMessage = "Unable to refresh orders"
        ) {
            orderSync.refreshAll()
        }
    }

    fun refreshNotifications() {
        val id = observedRecipientId
        val type = observedRecipientType

        if (id != null && type != null) {
            execute(
                getState = { _notificationState.value },
                setState = { _notificationState.value = it },
                fallbackMessage = "Unable to refresh notifications"
            ) {
                notificationSync.refreshByRecipient(id, type)
            }
        } else {
            execute(
                getState = { _notificationState.value },
                setState = { _notificationState.value = it },
                fallbackMessage = "Unable to refresh notifications"
            ) {
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
    // Profile operations
    // ---------------------------------------------------------

    fun updateCustomerProfile(
        customerId: String,
        customerName: String,
        customerImage: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                customerSync.updateProfile(
                    customerId = customerId,
                    customerName = customerName,
                    customerImage = customerImage
                )

                onResult(true)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                onResult(false)
            }
        }
    }

    fun updateVendorProfile(
        vendorId: String,
        vendorName: String,
        category: String,
        vendorImage: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                vendorSync.updateProfile(
                    vendorId = vendorId,
                    vendorName = vendorName,
                    category = category,
                    vendorImage = vendorImage
                )

                onResult(true)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                onResult(false)
            }
        }
    }

    // ---------------------------------------------------------
    // Image operations
    // ---------------------------------------------------------

    fun uploadImage(
        uri: Uri,
        onResult: (String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val imageId = imageUploadRepository.uploadImage(uri)
                onResult(imageId)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                onResult(null)
            }
        }
    }

    fun loadImage(imageId: String) {
        if (imageId.isBlank()) return

        val currentState = _imageStates.value[imageId]
        if (
            currentState is ImageUiState.Loading ||
            currentState is ImageUiState.Loaded
        ) {
            return
        }

        _imageStates.value = _imageStates.value +
                (imageId to ImageUiState.Loading)

        viewModelScope.launch {
            try {
                val bytes = imageCache.getImageBytes(imageId)

                _imageStates.value = _imageStates.value +
                        (
                                imageId to if (bytes != null) {
                                    ImageUiState.Loaded(bytes)
                                } else {
                                    ImageUiState.Unavailable
                                }
                                )
            } catch (exception: CancellationException) {
                _imageStates.value = _imageStates.value - imageId
                throw exception
            } catch (exception: Exception) {
                _imageStates.value = _imageStates.value +
                        (imageId to ImageUiState.Unavailable)
            }
        }
    }

    fun retryImage(imageId: String) {
        _imageStates.value = _imageStates.value - imageId
        loadImage(imageId)
    }

    fun removeCachedImage(imageId: String) {
        viewModelScope.launch {
            try {
                imageCache.removeCachedImage(imageId)
                _imageStates.value = _imageStates.value - imageId
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _errorForImageRemoval.value =
                    "Unable to remove cached image"
            }
        }
    }

    private val _errorForImageRemoval = MutableStateFlow<String?>(null)

    val errorForImageRemoval: StateFlow<String?> =
        _errorForImageRemoval.asStateFlow()

    fun clearImageRemovalError() {
        _errorForImageRemoval.value = null
    }

    // ---------------------------------------------------------
    // Per-domain operation state
    // ---------------------------------------------------------

    private fun <T> execute(
        getState: () -> DataUiState<T>,
        setState: (DataUiState<T>) -> Unit,
        fallbackMessage: String,
        action: suspend () -> Unit
    ) {
        viewModelScope.launch {
            perform(
                getState = getState,
                setState = setState,
                fallbackMessage = fallbackMessage,
                action = action
            )
        }
    }

    private suspend fun <T> perform(
        getState: () -> DataUiState<T>,
        setState: (DataUiState<T>) -> Unit,
        fallbackMessage: String,
        action: suspend () -> Unit
    ) {
        val previous = getState()

        setState(
            previous.copy(
                isInitialLoading = !previous.hasLoaded,
                isRefreshing = previous.hasLoaded,
                errorMessage = null
            )
        )

        try {
            action()

            val current = getState()
            setState(
                current.copy(
                    isInitialLoading = false,
                    isRefreshing = false,
                    errorMessage = null
                )
            )
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            val current = getState()
            setState(
                current.copy(
                    isInitialLoading = false,
                    isRefreshing = false,
                    errorMessage = exception.message ?: fallbackMessage
                )
            )
        }
    }

    // ---------------------------------------------------------
    // Authentication
    // ---------------------------------------------------------

    private val _authenticationState =
        MutableStateFlow(AuthenticationUiState())

    val authenticationState: StateFlow<AuthenticationUiState> =
        _authenticationState.asStateFlow()

    fun login(
        email: String,
        password: String,
        isVendor: Boolean,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            _authenticationState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {
                when (
                    val result = authenticationRepository.login(
                        email = email,
                        password = password,
                        isVendor = isVendor
                    )
                ) {
                    is LoginResult.Success -> {
                        _authenticationState.update {
                            it.copy(
                                userId = result.userId,
                                role = result.role,
                                isLoading = false,
                                errorMessage = null
                            )
                        }

                        when (result.role) {
                            UserRole.CUSTOMER ->
                                refreshCustomer(result.userId)

                            UserRole.VENDOR ->
                                refreshVendors()

                            UserRole.NONE -> Unit
                        }

                        onResult(true)
                    }

                    LoginResult.InvalidCredentials,
                    LoginResult.WrongRole,
                    LoginResult.Error -> {
                        _authenticationState.update {
                            it.copy(
                                userId = null,
                                role = UserRole.NONE,
                                isLoading = false,
                                errorMessage = when (result) {
                                    LoginResult.InvalidCredentials ->
                                        "Invalid email or password"

                                    LoginResult.WrongRole ->
                                        "This account does not match the selected role"

                                    else ->
                                        "Unable to log in. Please try again"
                                }
                            )
                        }

                        onResult(false)
                    }
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _authenticationState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage =
                            "Unable to log in. Please try again"
                    )
                }

                onResult(false)
            }
        }
    }

    fun register(
        email: String,
        password: String,
        name: String,
        category: String,
        isVendor: Boolean,
        onResult: (RegisterResult) -> Unit
    ) {
        viewModelScope.launch {
            _authenticationState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {
                val result = authenticationRepository.register(
                    email = email,
                    password = password,
                    name = name,
                    category = category,
                    isVendor = isVendor
                )

                if (result == RegisterResult.SUCCESS) {
                    val userId = authenticationRepository.currentUserId

                    _authenticationState.update {
                        it.copy(
                            userId = userId,
                            role = if (isVendor) {
                                UserRole.VENDOR
                            } else {
                                UserRole.CUSTOMER
                            },
                            isLoading = false,
                            errorMessage = null
                        )
                    }

                    if (userId != null) {
                        if (isVendor) {
                            refreshVendors()
                        } else {
                            refreshCustomer(userId)
                        }
                    }
                } else {
                    _authenticationState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Registration failed"
                        )
                    }
                }

                onResult(result)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _authenticationState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage =
                            "Registration failed. Please try again"
                    )
                }

                onResult(RegisterResult.ERROR)
            }
        }
    }

    fun checkCurrentUserRole(
        onResult: (UserRole) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val role = authenticationRepository.getCurrentUserRole()
                val userId = authenticationRepository.currentUserId

                _authenticationState.update {
                    it.copy(
                        userId = userId,
                        role = role,
                        isLoading = false,
                        errorMessage = null
                    )
                }

                onResult(role)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _authenticationState.update {
                    it.copy(
                        role = UserRole.NONE,
                        isLoading = false,
                        errorMessage = "Unable to verify account"
                    )
                }

                onResult(UserRole.NONE)
            }
        }
    }

    fun signOut() {
        authenticationRepository.signOut()

        _authenticationState.value = AuthenticationUiState()

        observedCustomerId = null
        observedVendorId = null

        customerObservationJob?.cancel()
        customerObservationJob = null

        vendorProfileObservationJob?.cancel()
        vendorProfileObservationJob = null

        _customerState.value = DataUiState(data = null)
        _vendorProfileState.value = DataUiState(data = null)
    }

    fun clearAuthenticationError() {
        _authenticationState.update {
            it.copy(errorMessage = null)
        }
    }
}