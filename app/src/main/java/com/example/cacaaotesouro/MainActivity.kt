package com.example.cacaaotesouro

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cacaaotesouro.ui.theme.CacaAoTesouroTheme
import kotlin.system.measureTimeMillis

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CacaAoTesouroTheme {
                val navController = rememberNavController()
                var startTime by remember { mutableStateOf(System.currentTimeMillis()) }

                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        HomeScreen(navController, onStart = { startTime = System.currentTimeMillis() })
                    }
                    composable("pista1") {
                        PistaScreen(navController, "Qual é o maior planeta do sistema solar?", "Júpiter", "pista2")
                    }
                    composable("pista2") {
                        PistaScreen(navController, "Qual é o animal mais rápido do mundo?", "Guepardo", "pista3")
                    }
                    composable("pista3") {
                        PistaScreen(navController, "Quantos continentes existem?", "7", "tesouro")
                    }
                    composable("tesouro") {
                        val timeTaken = (System.currentTimeMillis() - startTime) / 1000
                        TreasureScreen(navController, timeTaken)
                    }
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController, onStart: () -> Unit) {
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Bem-vindo à Caça ao Tesouro!")
            Button(onClick = {
                onStart()
                navController.navigate("pista1")
            }) {
                Text(text = "Iniciar Caça ao Tesouro")
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PistaScreen(navController: NavController, question: String, answer: String, nextRoute: String) {
    var userAnswer by remember { mutableStateOf("") }
    var isCorrect by remember { mutableStateOf(false) }

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = question)
            androidx.compose.material3.TextField(
                value = userAnswer,
                onValueChange = {
                    userAnswer = it
                    isCorrect = false // Resetar a validação ao mudar a resposta
                },
                label = { Text("Digite sua resposta") }
            )
            Button(
                onClick = {
                    isCorrect = userAnswer.equals(answer, ignoreCase = true)
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(text = "Verificar Resposta")
            }

            if (isCorrect) {
                Text(
                    text = "Resposta correta! Avance para a próxima pista.",
                    modifier = Modifier.padding(8.dp)
                )
                Button(
                    onClick = { navController.navigate(nextRoute) },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(text = "Próxima Pista")
                }
            }

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(text = "Voltar")
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TreasureScreen(navController: NavController, timeTaken: Long) {
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Parabéns! Você encontrou o tesouro!")
            Text(text = "Tempo total: $timeTaken segundos.")
            Button(
                onClick = { navController.navigate("home") },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(text = "Recomeçar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CacaAoTesouroTheme {
        val navController = rememberNavController()
        HomeScreen(navController = navController, onStart = {})
    }
}
