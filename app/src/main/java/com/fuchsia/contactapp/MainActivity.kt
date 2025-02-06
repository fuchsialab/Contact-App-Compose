package com.fuchsia.contactapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Database
import com.fuchsia.contactapp.data.database.DatabaseInit
import com.fuchsia.contactapp.data.entities.Contact
import com.fuchsia.contactapp.data.repository.ContactRepository
import com.fuchsia.contactapp.navigation.appNavigation.AppNavigation
import com.fuchsia.contactapp.presentation.ContactViewModel
import com.fuchsia.contactapp.ui.theme.ContactAppTheme
import kotlinx.coroutines.launch
import java.util.Calendar

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val repository = ContactRepository(DatabaseInit.getDatabase(this).dao)
            val viewModel= viewModel {
                ContactViewModel(repository)
            }

            Text(text = "Hello" )

            ContactAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    AppNavigation(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!", modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ContactAppTheme {
        Greeting("Android")
    }
}