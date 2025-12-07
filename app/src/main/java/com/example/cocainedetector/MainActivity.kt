package com.example.cocainedetector

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cocainedetector.data.Detection
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as CocaineDetectorApplication
    val detectionsFlow: Flow<List<Detection>> = remember { application.database.detectionDao().getAll() }
    val detections by detectionsFlow.collectAsStateWithLifecycle(initialValue = emptyList())

    var selectedDetection by remember { mutableStateOf<Detection?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Cocaine Detector",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }) {
                Text("Enable Accessibility")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Detections Log:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(detections) { detection ->
                DetectionItem(detection) {
                    selectedDetection = detection
                }
            }
        }

        if (selectedDetection != null) {
            AlertDialog(
                onDismissRequest = { selectedDetection = null },
                title = {
                    Text(text = "Detected Text")
                },
                text = {
                    Text(text = selectedDetection?.screenText ?: "")
                },
                confirmButton = {
                    TextButton(onClick = { selectedDetection = null }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}

@Composable
fun DetectionItem(detection: Detection, onClick: () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "App: ${detection.packageName}", style = MaterialTheme.typography.bodyLarge)
            Text(
                text = "Time: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(detection.timestamp))}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
