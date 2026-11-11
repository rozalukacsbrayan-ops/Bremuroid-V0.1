package com.bremuroid.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Bg = Color(0xFF101114)
private val Panel = Color(0xFF1B1D22)
private val Red = Color(0xFFE53935)
private val Muted = Color(0xFF9A9DA6)

data class GameEntry(val name: String, val uri: Uri, val extension: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { BremuroidApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BremuroidApp() {
    var selectedTab by remember { mutableStateOf(0) }
    var games by remember { mutableStateOf(listOf<GameEntry>()) }
    var search by remember { mutableStateOf("") }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        val additions = uris.mapNotNull { uri ->
            val name = uri.lastPathSegment?.substringAfterLast('/') ?: "Juego importado"
            val ext = name.substringAfterLast('.', "archivo").uppercase()
            GameEntry(name, uri, ext)
        }
        games = (games + additions).distinctBy { it.uri }
    }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Red,
            background = Bg,
            surface = Panel,
            onBackground = Color.White,
            onSurface = Color.White
        )
    ) {
        Scaffold(
            containerColor = Bg,
            topBar = {
                Column(Modifier.fillMaxWidth().background(Bg).padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier.size(42.dp).background(Red, RoundedCornerShape(13.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Gamepad, contentDescription = null, tint = Color.White, modifier = Modifier.size(27.dp))
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("BREMUROID", fontWeight = FontWeight.Black, letterSpacing = 2.sp, fontSize = 20.sp)
                            Text("V0.1  •  YOUR GAME LIBRARY", color = Muted, fontSize = 10.sp, letterSpacing = 1.2.sp)
                        }
                    }
                }
            },
            bottomBar = {
                NavigationBar(containerColor = Panel, contentColor = Color.White) {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = { Icon(Icons.Default.Gamepad, contentDescription = "Biblioteca") },
                        label = { Text("Biblioteca") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = { Icon(Icons.Default.Memory, contentDescription = "Sistemas") },
                        label = { Text("Sistemas") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = { Icon(Icons.Default.FolderOpen, contentDescription = "BIOS") },
                        label = { Text("BIOS") }
                    )
                }
            },
            floatingActionButton = {
                if (selectedTab == 0) {
                    FloatingActionButton(
                        onClick = { importLauncher.launch(arrayOf("*/*")) },
                        containerColor = Red,
                        contentColor = Color.White
                    ) { Icon(Icons.Default.Add, contentDescription = "Añadir juego") }
                }
            }
        ) { padding ->
            when (selectedTab) {
                0 -> LibraryScreen(games, search, { search = it }, Modifier.padding(padding))
                1 -> SystemsScreen(Modifier.padding(padding))
                else -> BiosScreen(
                    onImport = { importLauncher.launch(arrayOf("*/*")) },
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

@Composable
fun LibraryScreen(games: List<GameEntry>, search: String, onSearch: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().padding(horizontal = 18.dp)) {
        Text("Tu colección", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text("${games.size} juegos añadidos", color = Muted, fontSize = 13.sp)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = search,
            onValueChange = onSearch,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar en tu biblioteca") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Red,
                unfocusedBorderColor = Panel,
                focusedContainerColor = Panel,
                unfocusedContainerColor = Panel
            )
        )
        Spacer(Modifier.height(18.dp))
        val filtered = games.filter { it.name.contains(search, ignoreCase = true) }
        if (filtered.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Gamepad, null, tint = Red, modifier = Modifier.size(58.dp))
                    Spacer(Modifier.height(12.dp))
                    Text(if (games.isEmpty()) "Tu biblioteca empieza aquí" else "No hay resultados", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(if (games.isEmpty()) "Pulsa + para importar tus juegos" else "Prueba con otro nombre", color = Muted, fontSize = 13.sp)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(bottom = 90.dp)) {
                items(filtered) { game ->
                    Row(
                        Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(16.dp)).padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(Modifier.size(48.dp).background(Color(0xFF302126), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Gamepad, null, tint = Red)
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(game.name, fontWeight = FontWeight.SemiBold)
                            Text(game.extension, color = Muted, fontSize = 11.sp)
                        }
                        Text("IMPORTADO", color = Red, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun SystemsScreen(modifier: Modifier = Modifier) {
    val groups = listOf(
        "NINTENDO" to listOf("NES", "SNES", "Game Boy / Color", "Game Boy Advance", "Nintendo 64", "Nintendo DS", "GameCube", "Wii"),
        "PLAYSTATION" to listOf("PlayStation", "PlayStation 2", "PSP"),
        "XBOX" to listOf("Xbox original", "Xbox 360 · experimental"),
        "SEGA" to listOf("Master System", "Mega Drive / Genesis", "Saturn", "Dreamcast")
    )
    LazyColumn(modifier.fillMaxSize().padding(horizontal = 18.dp), verticalArrangement = Arrangement.spacedBy(14.dp), contentPadding = PaddingValues(bottom = 20.dp)) {
        item {
            Text("Sistemas", fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text("Catálogo previsto para futuras integraciones", color = Muted, fontSize = 13.sp)
        }
        items(groups) { (brand, systems) ->
            Column(Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(16.dp)).padding(16.dp)) {
                Text(brand, color = Red, fontWeight = FontWeight.Black, letterSpacing = 1.4.sp)
                Spacer(Modifier.height(8.dp))
                systems.forEach { system ->
                    Text("•  $system", modifier = Modifier.padding(vertical = 5.dp), fontSize = 14.sp)
                }
            }
        }
        item {
            Text("Nota: aparecer en este catálogo no significa que el sistema ya pueda ejecutar juegos. La emulación se añadirá mediante núcleos compatibles.", color = Muted, fontSize = 12.sp)
        }
    }
}

@Composable
fun BiosScreen(onImport: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().padding(20.dp)) {
        Text("Gestor de BIOS", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))
        Text("Añade firmware que tengas derecho a utilizar.", color = Muted, fontSize = 13.sp)
        Spacer(Modifier.height(20.dp))
        Column(Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(18.dp)).padding(18.dp)) {
            Icon(Icons.Default.Memory, null, tint = Red, modifier = Modifier.size(34.dp))
            Spacer(Modifier.height(10.dp))
            Text("Todavía no hay BIOS importadas", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text("Bremuroid V0.1 no incluye ni descarga BIOS. Algunos núcleos pueden necesitarlas; consulta la documentación del núcleo para conocer el archivo exacto.", color = Muted, fontSize = 13.sp)
            Spacer(Modifier.height(14.dp))
            Button(onClick = onImport, colors = ButtonDefaults.buttonColors(containerColor = Red)) {
                Icon(Icons.Default.Add, null)
                Spacer(Modifier.width(8.dp))
                Text("Importar archivo")
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("Consejo: no compartas BIOS, firmware ni ROM protegidas por derechos de autor en repositorios públicos.", color = Muted, fontSize = 12.sp)
    }
}
