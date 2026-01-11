package com.praveen.snoretrack.ui

import android.text.format.DateUtils
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.praveen.snoretrack.data.Session
import java.text.SimpleDateFormat
import java.util.*

val StopIcon: ImageVector
    get() {
        if (_stopIcon != null) return _stopIcon!!
        _stopIcon = ImageVector.Builder(
            name = "Stop",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = androidx.compose.ui.graphics.SolidColor(Color.White)) {
                moveTo(6f, 6f)
                lineTo(18f, 6f)
                lineTo(18f, 18f)
                lineTo(6f, 18f)
                close()
            }
        }.build()
        return _stopIcon!!
    }
private var _stopIcon: ImageVector? = null

val TrashIcon: ImageVector
    get() {
        if (_trashIcon != null) return _trashIcon!!
        _trashIcon = ImageVector.Builder(
            name = "Trash",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = androidx.compose.ui.graphics.SolidColor(Color.White)) {
                moveTo(6f, 19f)
                curveTo(6f, 20.1f, 6.9f, 21f, 8f, 21f)
                lineTo(16f, 21f)
                curveTo(17.1f, 21f, 18f, 20.1f, 18f, 19f)
                lineTo(18f, 7f)
                lineTo(6f, 7f)
                lineTo(6f, 19f)
                close()
                moveTo(19f, 4f)
                lineTo(15.5f, 4f)
                lineTo(14.5f, 3f)
                lineTo(9.5f, 3f)
                lineTo(8.5f, 4f)
                lineTo(5f, 4f)
                lineTo(5f, 6f)
                lineTo(19f, 6f)
                lineTo(19f, 4f)
                close()
            }
        }.build()
        return _trashIcon!!
    }
private var _trashIcon: ImageVector? = null

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val isRecording by viewModel.isRecording.collectAsState()
    val analysis by viewModel.analysisResult.collectAsState()
    val sessions by viewModel.sessions.collectAsState()
    val currentlyPlayingId by viewModel.currentlyPlayingSessionId.collectAsState()
    
    val context = LocalContext.current
    
    // Permission Launcher
    val requiredPermissions = mutableListOf(
        android.Manifest.permission.RECORD_AUDIO
    ).apply {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            add(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }.toTypedArray()

    val permissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            val allGranted = perms.values.all { it }
            if (allGranted) {
                viewModel.toggleRecording()
            }
        }
    )

    // Night Theme Background
    val deepBlue = Color(0xFF0F172A)
    val midnightBlue = Color(0xFF1E293B)
    val accentPurple = Color(0xFF8B5CF6)
    
    val gradient = Brush.verticalGradient(
        colors = listOf(deepBlue, midnightBlue)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "SnoreTrack",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = Color.White.copy(alpha = 0.9f)
        )
        Text(
            text = if (isRecording) "Monitoring Sleep..." else "Good Night",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White.copy(alpha = 0.6f)
        )
        
        Spacer(modifier = Modifier.height(48.dp))

        // Big Recording Button with Pulse
        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
        val scale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = if (isRecording) 1.1f else 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse_scale"
        )
        
        Box(
            modifier = Modifier
                .size(200.dp)
                .scale(scale)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = if (isRecording) 
                            listOf(Color(0xFFEF4444).copy(alpha = 0.8f), Color(0xFF991B1B))
                        else 
                            listOf(accentPurple.copy(alpha = 0.8f), Color(0xFF5B21B6))
                    )
                )
                .clickable {
                    if (isRecording) {
                        viewModel.toggleRecording()
                    } else {
                        permissionLauncher.launch(requiredPermissions)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (isRecording) "STOP" else "START",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                if (isRecording) {
                    Text(
                        text = "${analysis?.amplitude?.toInt() ?: 0} dB",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        AnimatedVisibility(visible = analysis?.isSnore == true) {
             Text(
                text = "💤 Snore Detected!", 
                color = Color(0xFFFDBA74),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Text(
            text = "Recent Sessions", 
            style = MaterialTheme.typography.titleLarge, 
            color = Color.White,
            modifier = Modifier.align(Alignment.Start)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sessions) { session ->
                SessionItem(
                    session = session, 
                    isPlaying = currentlyPlayingId == session.id,
                    onPlayClick = { viewModel.playSession(session) },
                    onDeleteClick = { viewModel.deleteSession(session) }
                )
            }
        }
    }
}

@Composable
fun SessionItem(session: Session, isPlaying: Boolean, onPlayClick: () -> Unit, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF334155)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    val date = Date(session.startTime)
                    val format = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
                    Text(
                        text = format.format(date), 
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    
                    if (!isPlaying) {
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        val duration = (session.endTime - session.startTime) / 1000
                        Text(
                            text = "Duration: ${DateUtils.formatElapsedTime(duration)}", 
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                        
                        Text(
                            text = "Score: ${"%.1f".format(session.snoreScore)}", 
                            style = MaterialTheme.typography.labelLarge,
                            color = if(session.snoreScore > 500) Color(0xFFFCA5A5) else Color(0xFF86EFAC)
                        )
                    }
                }
                
                // Action Buttons
                Row {
                     IconButton(onClick = onDeleteClick) {
                        Icon(
                            imageVector = TrashIcon,
                            contentDescription = "Delete",
                            tint = Color.White.copy(alpha = 0.5f)
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    IconButton(
                        onClick = onPlayClick,
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.1f), CircleShape)
                    ) {
                       Icon(
                           if (isPlaying) StopIcon else Icons.Default.PlayArrow, 
                           contentDescription = "Play",
                           tint = Color.White
                       )
                    }
                }
            }
            
            // Visualization Graph
            AnimatedVisibility(visible = isPlaying) {
                Spacer(modifier = Modifier.height(16.dp))
                WaveformGraph(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp) // Slightly taller
                )
            }
        }
    }
}

@Composable
fun WaveformGraph(modifier: Modifier = Modifier, viewModel: MainViewModel = viewModel()) {
    val amplitude by viewModel.playbackAmplitude.collectAsState()
    
    // Sliding window logic
    val points = remember { mutableStateListOf<Float>() }
    
    LaunchedEffect(amplitude) {
        points.add(amplitude)
        if (points.size > 50) { // Keep last 50 points
            points.removeAt(0)
        }
    }
    
    androidx.compose.foundation.Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val stepX = width / 50f
        
        val path = androidx.compose.ui.graphics.Path()
        path.moveTo(0f, height) 
        
        // Smoothen the visual
        points.forEachIndexed { index, value ->
            // Scale: 30000 is a reasonable max for 16-bit PCM
            val normalizedH = (value / 15000f) * height 
            val y = height - normalizedH.coerceAtMost(height)
            path.lineTo(index * stepX, y)
        }
        
        // 1. Draw Fill (Translucent)
        val fillPath = androidx.compose.ui.graphics.Path()
        fillPath.addPath(path)
        fillPath.lineTo((points.size - 1) * stepX, height)
        fillPath.close()
        
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF8B5CF6).copy(alpha = 0.5f), Color.Transparent)
            )
        )
        
        // 2. Draw Stroke (Solid Line for visibility)
        drawPath(
            path = path,
            color = Color(0xFFC4B5FD), // Light Purple
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f)
        )
    }
}
