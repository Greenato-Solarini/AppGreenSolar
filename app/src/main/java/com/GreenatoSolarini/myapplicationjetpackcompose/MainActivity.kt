package com.greenatosolarini.myapplicationjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.greenatosolarini.myapplicationjetpackcompose.ui.theme.MyApplicationJetpackComposeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationJetpackComposeTheme {
                val navController = rememberNavController()
                val viewModels = rememberAppViewModels(LocalContext.current)
                AppNavGraph(navController, viewModels)
            }
        }
    }
}
