package com.example.curtineat.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.curtineat.data.local.room.entity.OrderEntity
import com.example.curtineat.data.local.room.entity.OrderProductEntity
import com.example.curtineat.viewmodel.AppViewModel

private val PaperColor = Color(0xFFFFFDF7)
private val InkColor = Color(0xFF222222)
private val FaintInkColor = Color(0xFF9E9E9E)

// Rectangle with a zigzag (torn) bottom edge
private val ReceiptShape = GenericShape { size, _ ->

	val tooth = 16f
	val teeth = (size.width / (tooth * 2)).toInt().coerceAtLeast(1)
	val step = size.width / teeth / 2f

	moveTo(0f, 0f)
	lineTo(size.width, 0f)
	lineTo(size.width, size.height)

	var x = size.width

	for (i in 0 until teeth) {
		x -= step
		lineTo(x, size.height - tooth)
		x -= step
		lineTo(x, size.height)
	}

	close()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
	appViewModel: AppViewModel,
	orderId: String,
	onBackButtonClick: () -> Unit
) {
	val vendorState by appViewModel.vendorState.collectAsState()
	val customerState by appViewModel.customerState.collectAsState()

	var order by remember(orderId) {
		mutableStateOf<OrderEntity?>(null)
	}

	var products by remember(orderId) {
		mutableStateOf<List<OrderProductEntity>>(emptyList())
	}

	var loadFinished by remember(orderId) {
		mutableStateOf(false)
	}

	LaunchedEffect(orderId) {
		loadFinished = false

		appViewModel.loadOrderDetails(orderId) { loadedOrder, loadedProducts ->
			order = loadedOrder
			products = loadedProducts
			loadFinished = true
		}

		if (vendorState.data.isEmpty()) {
			appViewModel.refreshVendors()
		}
	}

	Scaffold(
		containerColor = MaterialTheme.colorScheme.surfaceVariant,

		// TOP WALL
		topBar = {
			TopAppBar(
				title = {
					Text("Order Details")
				},
				navigationIcon = {
					IconButton(
						onClick = onBackButtonClick
					) {
						Icon(
							imageVector = Icons.AutoMirrored.Filled.ArrowBack,
							contentDescription = "Go back"
						)
					}
				}
			)
		},

		// BOTTOM WALL
		bottomBar = {
			BottomAppBar {
				Text(
					text = "Thank you for ordering with CurtinEAT",
					modifier = Modifier.fillMaxWidth(),
					textAlign = TextAlign.Center,
					style = MaterialTheme.typography.bodyMedium
				)
			}
		}

	) { innerPadding ->

		Box(
			modifier = Modifier
				.padding(innerPadding)
				.fillMaxSize(),
			contentAlignment = Alignment.TopCenter
		) {

			if (order == null) {
				Text(
					text = if (loadFinished) {
						"Order not found"
					} else {
						"Loading..."
					},
					modifier = Modifier
						.align(Alignment.Center)
						.padding(32.dp)
				)
			} else {

				val currentOrder = order!!

				val vendorName =
					vendorState.data.firstOrNull {
						it.vendorId == currentOrder.vendorId
					}?.vendorName ?: "Vendor"

				val customerName =
					customerState.data
						?.takeIf {
							it.customerId == currentOrder.customerId
						}
						?.customerName

				Column(
					modifier = Modifier
						.fillMaxSize()
						.verticalScroll(rememberScrollState())
						.padding(16.dp),
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					ReceiptPaper(
						order = currentOrder,
						products = products,
						vendorName = vendorName,
						customerName = customerName
					)
				}
			}
		}
	}
}

@Composable
private fun ReceiptPaper(
	order: OrderEntity,
	products: List<OrderProductEntity>,
	vendorName: String,
	customerName: String?
) {
	Surface(
		modifier = Modifier
			.widthIn(max = 420.dp)
			.fillMaxWidth(),
		shape = ReceiptShape,
		color = PaperColor,
		shadowElevation = 6.dp
	) {
		Column(
			modifier = Modifier
				.padding(horizontal = 20.dp)
				.padding(top = 24.dp, bottom = 36.dp)
		) {

			// HEADER
			ReceiptText(
				text = "CURTINEAT",
				modifier = Modifier.fillMaxWidth(),
				fontSize = 22.sp,
				bold = true,
				align = TextAlign.Center
			)

			ReceiptText(
				text = vendorName.uppercase(),
				modifier = Modifier.fillMaxWidth(),
				fontSize = 16.sp,
				align = TextAlign.Center
			)

			ReceiptText(
				text = "ORDER RECEIPT",
				modifier = Modifier.fillMaxWidth(),
				fontSize = 12.sp,
				align = TextAlign.Center
			)

			DashedDivider()

			// ORDER INFO
			ReceiptRow(
				left = "Order",
				right = "#${order.orderId.take(8).uppercase()}"
			)

			ReceiptRow(
				left = "Date",
				right = formatOrderDateTime(
					java.util.Date(order.timestamp)
				)
			)

			if (customerName != null) {
				ReceiptRow(
					left = "Customer",
					right = customerName
				)
			}

			ReceiptRow(
				left = "Status",
				right = order.status.uppercase()
			)

			DashedDivider()

			// ITEMS
			ReceiptRow(
				left = "ITEM",
				right = "AMOUNT",
				bold = true
			)

			products.forEach { product ->

				ReceiptText(
					text = product.productName.uppercase(),
					modifier = Modifier.padding(top = 8.dp)
				)

				ReceiptRow(
					left = "  ${product.quantity} x RM %.2f"
						.format(product.productPrice),
					right = "RM %.2f"
						.format(product.productPrice * product.quantity)
				)
			}

			DashedDivider()

			// TOTALS
			ReceiptRow(
				left = "Items",
				right = products.sumOf { it.quantity }.toString()
			)

			Spacer(
				modifier = Modifier.height(4.dp)
			)

			ReceiptRow(
				left = "TOTAL",
				right = "RM %.2f".format(order.totalPrice),
				bold = true,
				fontSize = 18.sp
			)

			DashedDivider()

			// FOOTER
			Barcode(
				seed = order.orderId
			)

			Spacer(
				modifier = Modifier.height(12.dp)
			)

			ReceiptText(
				text = "THANK YOU!",
				modifier = Modifier.fillMaxWidth(),
				bold = true,
				align = TextAlign.Center
			)
		}
	}
}

@Composable
private fun ReceiptText(
	text: String,
	modifier: Modifier = Modifier,
	fontSize: TextUnit = 14.sp,
	bold: Boolean = false,
	align: TextAlign? = null
) {
	Text(
		text = text,
		modifier = modifier,
		color = InkColor,
		fontFamily = FontFamily.Monospace,
		fontSize = fontSize,
		fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
		textAlign = align
	)
}

@Composable
private fun ReceiptRow(
	left: String,
	right: String,
	bold: Boolean = false,
	fontSize: TextUnit = 14.sp
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 2.dp),
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		ReceiptText(
			text = left,
			modifier = Modifier.weight(1f),
			fontSize = fontSize,
			bold = bold
		)

		ReceiptText(
			text = right,
			fontSize = fontSize,
			bold = bold,
			align = TextAlign.End
		)
	}
}

@Composable
private fun DashedDivider() {
	Canvas(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 12.dp)
			.height(1.dp)
	) {
		drawLine(
			color = FaintInkColor,
			start = Offset(0f, 0f),
			end = Offset(size.width, 0f),
			strokeWidth = 2f,
			pathEffect = PathEffect.dashPathEffect(
				floatArrayOf(12f, 10f)
			)
		)
	}
}

// Decorative barcode, drawn from the order id so each receipt looks different
@Composable
private fun Barcode(
	seed: String
) {
	val chars = seed.ifEmpty { "0" }

	Canvas(
		modifier = Modifier
			.fillMaxWidth()
			.height(48.dp)
	) {
		var x = 0f
		var i = 0

		while (x < size.width) {
			val code = chars[i % chars.length].code

			val barWidth = (code % 3 + 1) * 3f
			val gapWidth = (code % 2 + 1) * 3f

			drawRect(
				color = InkColor,
				topLeft = Offset(x, 0f),
				size = Size(
					minOf(barWidth, size.width - x),
					size.height
				)
			)

			x += barWidth + gapWidth
			i++
		}
	}
}