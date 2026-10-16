package com.example.curtineat.util

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.curtineat.MainActivity
import com.example.curtineat.data.local.room.entity.NotificationEntity

class NotificationHelper(private val context: Context) {

	companion object {
		const val CHANNEL_ID = "order_updates"
	}

	fun createChannel() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

		val channel = NotificationChannel(
			CHANNEL_ID,
			"Order updates",
			NotificationManager.IMPORTANCE_HIGH
		).apply {
			description = "Updates about your orders"
			enableVibration(true)
			vibrationPattern = longArrayOf(0, 250, 150, 250)
		}

		context.getSystemService(NotificationManager::class.java)
			.createNotificationChannel(channel)
	}

	fun onNewNotifications(notifications: List<NotificationEntity>) {
		if (isAppInForeground()) {
			// Bell badge already shows it, so just a short buzz
			vibrate()
		} else {
			notifications.forEach { show(it) }
		}
	}

	private fun isAppInForeground(): Boolean =
		ProcessLifecycleOwner.get().lifecycle.currentState
			.isAtLeast(Lifecycle.State.STARTED)

	@SuppressLint("MissingPermission")
	private fun show(notification: NotificationEntity) {
		val manager = NotificationManagerCompat.from(context)
		if (!manager.areNotificationsEnabled()) return

		val intent = Intent(context, MainActivity::class.java).apply {
			flags = Intent.FLAG_ACTIVITY_NEW_TASK or
				Intent.FLAG_ACTIVITY_SINGLE_TOP
		}

		val pendingIntent = PendingIntent.getActivity(
			context,
			0,
			intent,
			PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
		)

		val built = NotificationCompat.Builder(context, CHANNEL_ID)
			// placeholder icon, replace with your own white-on-transparent vector
			.setSmallIcon(android.R.drawable.ic_dialog_info)
			.setContentTitle("CurtinEAT")
			.setContentText(notification.message)
			.setStyle(NotificationCompat.BigTextStyle().bigText(notification.message))
			.setPriority(NotificationCompat.PRIORITY_HIGH)
			.setDefaults(NotificationCompat.DEFAULT_VIBRATE)
			.setAutoCancel(true)
			.setContentIntent(pendingIntent)
			.build()

		manager.notify(notification.notificationId.hashCode(), built)
	}

	private fun vibrate() {
		val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			(context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE)
				as VibratorManager).defaultVibrator
		} else {
			@Suppress("DEPRECATION")
			context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			vibrator.vibrate(
				VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE)
			)
		} else {
			@Suppress("DEPRECATION")
			vibrator.vibrate(250)
		}
	}
}