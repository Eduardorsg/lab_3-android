package com.example.lab_3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdivinanzaNumericaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    JuegoAdivinanza()
                }
            }
        }
    }
}

@Composable
fun JuegoAdivinanza() {
    var numeroSecreto by remember { mutableIntStateOf(Random.nextInt(1, 101)) }
    var inputUsuario by remember { mutableStateOf("") }
    var vidasRestantes by remember { mutableIntStateOf(5) }
    var estadoJuego by remember { mutableStateOf("Encuentra el número secreto del 1 al 100") }
    var juegoTerminado by remember { mutableStateOf(false) }
    var victoria by remember { mutableStateOf(false) }
    var listaIntentos by remember { mutableStateOf(mutableListOf<String>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Título principal
        Text(
            text = "Adivinanza Numérica",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF6A4C93),
            textAlign = TextAlign.Center
        )

        // Mensaje de estado del juego
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = when {
                    victoria -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                    juegoTerminado -> Color(0xFFE91E63).copy(alpha = 0.2f)
                    else -> Color(0xFF2196F3).copy(alpha = 0.1f)
                }
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = estadoJuego,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(20.dp),
                textAlign = TextAlign.Center,
                color = when {
                    victoria -> Color(0xFF2E7D32)
                    juegoTerminado -> Color(0xFFC2185B)
                    else -> Color(0xFF1976D2)
                }
            )
        }

        // Campo de entrada
        OutlinedTextField(
            value = inputUsuario,
            onValueChange = { nuevoValor ->
                if (nuevoValor.isEmpty() || (nuevoValor.toIntOrNull()?.let { it in 1..100 } == true)) {
                    inputUsuario = nuevoValor
                }
            },
            label = { Text("Ingresa tu número") },
            placeholder = { Text("1 - 100") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            enabled = !juegoTerminado,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF6A4C93),
                focusedLabelColor = Color(0xFF6A4C93)
            )
        )

        // Contador de vidas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "💖 Vidas: ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$vidasRestantes",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (vidasRestantes <= 2) Color(0xFFE91E63) else Color(0xFF4CAF50)
            )
        }

        // Botón de intento
        Button(
            onClick = {
                val numeroIngresado = inputUsuario.toIntOrNull()
                if (numeroIngresado != null && numeroIngresado in 1..100) {
                    vidasRestantes--

                    when {
                        numeroIngresado == numeroSecreto -> {
                            estadoJuego = "🏆 ¡Increíble! ¡Has encontrado el número secreto!"
                            juegoTerminado = true
                            victoria = true
                        }
                        vidasRestantes == 0 -> {
                            estadoJuego = "💀 ¡Game Over! El número secreto era: $numeroSecreto"
                            juegoTerminado = true
                            victoria = false
                        }
                        numeroIngresado < numeroSecreto -> {
                            estadoJuego = "⬆️ ¡Sube más! Tu número es demasiado pequeño"
                            listaIntentos.add("$numeroIngresado → Muy bajo")
                        }
                        numeroIngresado > numeroSecreto -> {
                            estadoJuego = "⬇️ ¡Baja más! Tu número es demasiado grande"
                            listaIntentos.add("$numeroIngresado → Muy alto")
                        }
                    }
                    inputUsuario = ""
                }
            },
            enabled = !juegoTerminado && inputUsuario.isNotEmpty() && inputUsuario.toIntOrNull() != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6A4C93),
                disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
            )
        ) {
            Text(
                text = if (juegoTerminado) "Juego Terminado" else "🎲 Probar Suerte",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Historial de intentos
        if (listaIntentos.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF5F5F5)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "📝 Registro de intentos:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF424242),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    listaIntentos.takeLast(3).forEach { intento ->
                        Text(
                            text = "▶ $intento",
                            fontSize = 14.sp,
                            color = Color(0xFF616161),
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Botón reiniciar
        if (juegoTerminado) {
            Button(
                onClick = {
                    numeroSecreto = Random.nextInt(1, 101)
                    inputUsuario = ""
                    vidasRestantes = 5
                    estadoJuego = "Encuentra el número secreto del 1 al 100"
                    juegoTerminado = false
                    victoria = false
                    listaIntentos.clear()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF9800)
                )
            ) {
                Text(
                    text = "🔄 Nueva Partida",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun AdivinanzaNumericaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF6A4C93),
            secondary = Color(0xFFFF9800),
            background = Color(0xFFFAFAFA)
        ),
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun JuegoAdivinanzaPreview() {
    AdivinanzaNumericaTheme {
        JuegoAdivinanza()
    }
}