package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.lifecycle.ViewModelProvider
import com.example.ui.SparkApp
import com.example.ui.SparkViewModel
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    
    // Instantiate our SQLite and AI powered ViewModel
    val viewModel = ViewModelProvider(this)[SparkViewModel::class.java]

    setContent {
      MyApplicationTheme {
        SparkApp(viewModel = viewModel)
      }
    }
  }
}
