package com.example.curtineat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.curtineat.database.Order
import com.example.curtineat.database.OrderItem
import com.example.curtineat.database.OrderItemRepository
import com.example.curtineat.database.OrderRepository
import com.example.curtineat.database.Product
import com.example.curtineat.database.ProductRepository
import com.example.curtineat.database.Student
import com.example.curtineat.database.StudentRepository
import com.example.curtineat.database.Vendor
import com.example.curtineat.database.VendorRepository
import kotlinx.coroutines.launch


/*
 * Dear meimei and jojo:
 *
 * Greeting from gorgor, here is the AppViewModel API for the frontend. You dont need to
 * worry about what happens inside the database, DAO, or repository. Just call the API
 * provided by the ViewModel.
 *
 * For example:
 *
 * @Composable
 * fun SomeScreen( appViewModel: AppViewModel) {
 *      appViewModel.getVendors { vendors -> UI(vendors) }
 * }
 *
 *
 * You can similarly call things like:
 *
 * appViewModel.getProductsByVender(vendorId) { products -> UI(products) }
 * appViewModel.addOrder(order)
 *
 * Regards your mental health,
 * gorgor
 *
 *
 *
 * ------------------------------------------------------------
 * STUDENT
 * ------------------------------------------------------------
 *
 * addStudent(student)
 *     Register a new student.
 *
 * updateStudent(student)
 *     Update student information.
 *
 * deleteStudent(student)
 *     Delete a student.
 *
 * getStudent(studentId, onResult)
 *     Get a student's information using their student ID.
 *
 * ------------------------------------------------------------
 * VENDOR
 * ------------------------------------------------------------
 *
 * addVendor(vendor)
 *     Register a new vendor/store.
 *
 * updateVendor(vendor)
 *     Update vendor/store information.
 *
 * deleteVendor(vendor)
 *     Delete a vendor/store.
 *
 * getVendors(onResult)
 *     Get all registered vendors.
 *
 * getVendor(vendorId, onResult)
 *     Get one vendor using its ID.
 *
 * ------------------------------------------------------------
 * PRODUCT / MENU
 * ------------------------------------------------------------
 *
 * addProduct(product)
 *     Add a new product/menu item to a vendor.
 *
 * updateProduct(product)
 *     Modify an existing product/menu item.
 *
 * deleteProduct(product)
 *     Remove a product/menu item.
 *
 * getProducts(onResult)
 *     Get all products.
 *
 * getProductsByVendor(vendorId, onResult)
 *     Get the menu belonging to one vendor.
 *
 * getAvailableProducts(onResult)
 *     Get products that are currently available for ordering.
 *
 * ------------------------------------------------------------
 * ORDER
 * ------------------------------------------------------------
 *
 * addOrder(order)
 *     Create a new order.
 *
 * updateOrder(order)
 *     Update an existing order.
 *     This can be used for order status or payment status changes.
 *
 * deleteOrder(order)
 *     Delete an order.
 *
 * getOrder(orderId, onResult)
 *     Get one order using its ID.
 *
 * getStudentOrders(studentId, onResult)
 *     Get the order history of a student.
 *
 * getVendorOrders(vendorId, onResult)
 *     Get orders belonging to a vendor.
 *
 * ------------------------------------------------------------
 * ORDER ITEM
 * ------------------------------------------------------------
 *
 * addOrderItem(orderItem)
 *     Add a product/quantity to an order.
 *
 * updateOrderItem(orderItem)
 *     Change the quantity or information of an order item.
 *
 * deleteOrderItem(orderItem)
 *     Remove an item from an order.
 *
 * getOrderItems(orderId, onResult)
 *     Get all products/items belonging to an order.
 *
 * ------------------------------------------------------------
 * FRONTEND USAGE
 * ------------------------------------------------------------
 *
 * Screens receive the same AppViewModel instance from MainActivity.
 *
 * Example:
 *
 *     appViewModel.getProductsByVendor(vendorId) { products ->
 *         // Update UI state with products
 *     }
 *
 *     appViewModel.addProduct(product)
 *
 * Do NOT access repositories or DAOs directly from the UI.
 *
 * ============================================================
 */

class AppViewModel(
    private val studentRepository: StudentRepository,
    private val vendorRepository: VendorRepository,
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository
) : ViewModel() {

    // Student

    fun addStudent(student: Student) {
        viewModelScope.launch {
            studentRepository.insertStudent(student)
        }
    }

    fun updateStudent(student: Student) {
        viewModelScope.launch {
            studentRepository.updateStudent(student)
        }
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch {
            studentRepository.deleteStudent(student)
        }
    }

    fun getStudent(studentId: String, onResult: (Student?) -> Unit) {
        viewModelScope.launch {
            val student = studentRepository.getStudentById(studentId)
            onResult(student)
        }
    }

    // Vendor

    fun addVendor(vendor: Vendor) {
        viewModelScope.launch {
            vendorRepository.insertVendor(vendor)
        }
    }

    fun updateVendor(vendor: Vendor) {
        viewModelScope.launch {
            vendorRepository.updateVendor(vendor)
        }
    }

    fun deleteVendor(vendor: Vendor) {
        viewModelScope.launch {
            vendorRepository.deleteVendor(vendor)
        }
    }

    fun getVendors(onResult: (List<Vendor>) -> Unit) {
        viewModelScope.launch {
            val vendors = vendorRepository.getAllVendors()
            onResult(vendors)
        }
    }

    fun getVendor(vendorId: Int, onResult: (Vendor?) -> Unit) {
        viewModelScope.launch {
            val vendor = vendorRepository.getVendorById(vendorId)
            onResult(vendor)
        }
    }

    // Product

    fun addProduct(product: Product) {
        viewModelScope.launch {
            productRepository.insertProduct(product)
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            productRepository.updateProduct(product)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            productRepository.deleteProduct(product)
        }
    }

    fun getProducts(onResult: (List<Product>) -> Unit) {
        viewModelScope.launch {
            val products = productRepository.getAllProducts()
            onResult(products)
        }
    }

    fun getProductsByVendor(
        vendorId: Int,
        onResult: (List<Product>) -> Unit
    ) {
        viewModelScope.launch {
            val products = productRepository.getProductsByVendor(vendorId)
            onResult(products)
        }
    }

    fun getAvailableProducts(onResult: (List<Product>) -> Unit) {
        viewModelScope.launch {
            val products = productRepository.getAvailableProducts()
            onResult(products)
        }
    }

    // Order

    fun addOrder(order: Order) {
        viewModelScope.launch {
            orderRepository.insertOrder(order)
        }
    }

    fun updateOrder(order: Order) {
        viewModelScope.launch {
            orderRepository.updateOrder(order)
        }
    }

    fun deleteOrder(order: Order) {
        viewModelScope.launch {
            orderRepository.deleteOrder(order)
        }
    }

    fun getOrder(orderId: Int, onResult: (Order?) -> Unit) {
        viewModelScope.launch {
            val order = orderRepository.getOrderById(orderId)
            onResult(order)
        }
    }

    fun getStudentOrders(
        studentId: String,
        onResult: (List<Order>) -> Unit
    ) {
        viewModelScope.launch {
            val orders = orderRepository.getOrdersByStudent(studentId)
            onResult(orders)
        }
    }

    fun getVendorOrders(
        vendorId: Int,
        onResult: (List<Order>) -> Unit
    ) {
        viewModelScope.launch {
            val orders = orderRepository.getOrdersByVendor(vendorId)
            onResult(orders)
        }
    }

    // Order Item

    fun addOrderItem(orderItem: OrderItem) {
        viewModelScope.launch {
            orderItemRepository.insertOrderItem(orderItem)
        }
    }

    fun updateOrderItem(orderItem: OrderItem) {
        viewModelScope.launch {
            orderItemRepository.updateOrderItem(orderItem)
        }
    }

    fun deleteOrderItem(orderItem: OrderItem) {
        viewModelScope.launch {
            orderItemRepository.deleteOrderItem(orderItem)
        }
    }

    fun getOrderItems(
        orderId: Int,
        onResult: (List<OrderItem>) -> Unit
    ) {
        viewModelScope.launch {
            val items = orderItemRepository.getOrderItemsByOrder(orderId)
            onResult(items)
        }
    }
}