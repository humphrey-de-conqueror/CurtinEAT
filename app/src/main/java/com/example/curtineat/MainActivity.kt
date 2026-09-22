package com.example.curtineat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.curtineat.ui.theme.CurtinEATTheme
import com.example.curtineat.view.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            CurtinEATTheme {
                Scaffold(
                    topBar = {
                        //TopAppBar(...)
                    },
                    bottomBar = {
                        //NavigationBar(...)
                    }
                ) { innerPadding -> // to prevent overlapping with top&bottom nav bar

                    MainScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(20.dp)

                    )
                }
            }
        }
    }
}