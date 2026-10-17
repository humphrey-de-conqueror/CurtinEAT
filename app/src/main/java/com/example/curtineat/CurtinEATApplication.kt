package com.example.curtineat

import android.app.Application
import com.example.curtineat.data.AppContainer
import com.example.curtineat.util.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CurtinEATApplication : Application() {

	lateinit var appContainer: AppContainer
		private set

	private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

	override fun onCreate() {
		super.onCreate()

		appContainer = AppContainer(
			context = applicationContext
		)

		val notificationHelper = NotificationHelper(applicationContext)
		notificationHelper.createChannel()

		appScope.launch {
			appContainer.notificationSync.newNotifications
				.collect {
					android.util.Log.d("NOTIF_DEBUG", "2 collector got ${it.size}")
					notificationHelper.onNewNotifications(it)
				}
		}
	}
}