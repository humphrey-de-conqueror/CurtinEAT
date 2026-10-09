package com.example.curtineat

import android.app.Application
import com.example.curtineat.data.AppContainer

class CurtinEATApplication : Application() {

	lateinit var appContainer: AppContainer
		private set

	override fun onCreate() {
		super.onCreate()

		appContainer = AppContainer(
			context = applicationContext
		)
	}
}