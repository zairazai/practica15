package com.example.practica15

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.practica15.presentation.*
import com.example.practica15.ui.theme.Practica15Theme

class MainActivity : ComponentActivity() {

    private val viewModel: PublicacionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Practica15Theme {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "lista"
                ) {

                    // Pantalla 1: listado de publicaciones
                    composable("lista") {
                        ListaScreen(viewModel) {
                            viewModel.nuevoFormulario()
                            navController.navigate("formulario")
                        }
                    }

                    // Pantalla 2: formulario de alta
                    composable("formulario") {
                        FormularioScreen(viewModel) {
                            navController.popBackStack()
                        }
                    }
                }
            }
        }
    }
}
