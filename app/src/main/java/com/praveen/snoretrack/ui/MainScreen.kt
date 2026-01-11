package com.praveen.snoretrack.ui

import android.text.format.DateUtils
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        // Decorative background circles
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-100).dp, y = (-50).dp)
                .background(
                    Color(0xFF8B5CF6).copy(alpha = 0.05f),
                    CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.TopEnd)
                .offset(x = 50.dp, y = 100.dp)
                .background(
                    Color(0xFF06B6D4).copy(alpha = 0.05f),
                    CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(48.dp))

            // App Title with Icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.animateContentSize()
            ) {
                Text(
                    text = "💤",
                    fontSize = 40.sp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "SnoreTrack",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = if (isRecording) "Monitoring..." else "Ready to Sleep",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(64.dp))

            // Big Recording Button with Enhanced Pulse and Glow
            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
            val scale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = if (isRecording) 1.08f else 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1500, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulse_scale"
            )

            val glowAlpha by infiniteTransition.animateFloat(
                initialValue = 0.3f,
                targetValue = if (isRecording) 0.6f else 0.3f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1500, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "glow_alpha"
            )

            Box(contentAlignment = Alignment.Center) {
                // Outer glow ring
                if (isRecording) {
                    Box(
                        modifier = Modifier
                            .size(240.dp)
                            .scale(scale)
                            .background(
                                Color(0xFFEF4444).copy(alpha = glowAlpha * 0.3f),
                                CircleShape
                            )
                    )
                }

                // Main button
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .scale(scale)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = if (isRecording)
                                    listOf(
                                        Color(0xFFEF4444),
                                        Color(0xFFDC2626),
                                        Color(0xFF991B1B)
                                    )
                                else
                                    listOf(
                                        Color(0xFFA78BFA),
                                        accentPurple,
                                        Color(0xFF6D28D9)
                                    )
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
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                    ) {
                        Icon(
                            imageVector = if (isRecording) StopIcon else Icons.Default.PlayArrow,
                            contentDescription = if (isRecording) "Stop" else "Start",
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isRecording) "STOP" else "START",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        if (isRecording) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${analysis?.amplitude?.toInt() ?: 0} dB",
                                color = Color.White.copy(alpha = 0.9f),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        
            Spacer(modifier = Modifier.height(32.dp))

            // Snore Detection Alert with Animation
            AnimatedVisibility(
                visible = analysis?.isSnore == true,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFBBF24).copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⚠️",
                            fontSize = 24.sp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Snore Detected!",
                                color = Color(0xFFFDBA74),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Recording snore event...",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Session Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Sessions",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                if (sessions.isNotEmpty()) {
                    Text(
                        text = "${sessions.size}",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier
                            .background(
                                Color.White.copy(alpha = 0.1f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sessions List
            if (sessions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "🌙",
                            fontSize = 64.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No sessions yet",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "Tap START to begin tracking",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.4f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
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
    }
}

@Composable
fun SessionItem(session: Session, isPlaying: Boolean, onPlayClick: () -> Unit, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF334155).copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isPlaying) 8.dp else 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .animateContentSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Date Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            Color(0xFF8B5CF6).copy(alpha = 0.15f),
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "📅",
                        fontSize = 24.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Session Info
                Column(modifier = Modifier.weight(1f)) {
                    val date = Date(session.startTime)
                    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

                    Text(
                        text = dateFormat.format(date),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = timeFormat.format(date),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }

                // Action Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = TrashIcon,
                            contentDescription = "Delete",
                            tint = Color(0xFFFCA5A5).copy(alpha = 0.7f),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = onPlayClick,
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                if (isPlaying)
                                    Color(0xFFEF4444).copy(alpha = 0.2f)
                                else
                                    Color(0xFF8B5CF6).copy(alpha = 0.2f),
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = if (isPlaying) StopIcon else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Stop" else "Play",
                            tint = if (isPlaying) Color(0xFFEF4444) else Color(0xFFA78BFA),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // Session Stats
            AnimatedVisibility(visible = !isPlaying) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Stats Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Duration Stat
                        StatChip(
                            icon = "⏱️",
                            label = "Duration",
                            value = DateUtils.formatElapsedTime((session.endTime - session.startTime) / 1000),
                            modifier = Modifier.weight(1f)
                        )

                        // Score Stat
                        StatChip(
                            icon = if (session.snoreScore > 500) "⚠️" else "✅",
                            label = "Score",
                            value = "%.1f".format(session.snoreScore),
                            valueColor = if (session.snoreScore > 500)
                                Color(0xFFFCA5A5)
                            else
                                Color(0xFF86EFAC),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Waveform Visualization
            AnimatedVisibility(
                visible = isPlaying,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Now Playing Header
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "🎵",
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Playing Audio",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color(0xFFA78BFA),
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Waveform
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(
                                Color(0xFF1E293B).copy(alpha = 0.5f),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(8.dp)
                    ) {
                        WaveformGraph(
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatChip(
    icon: String,
    label: String,
    value: String,
    valueColor: Color = Color.White,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                Color(0xFF1E293B).copy(alpha = 0.5f),
                RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = icon,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.labelLarge,
                color = valueColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun WaveformGraph(modifier: Modifier = Modifier, viewModel: MainViewModel = viewModel()) {
    val amplitude by viewModel.playbackAmplitude.collectAsState()

    // Sliding window logic with more points for smoother visualization
    val points = remember { mutableStateListOf<Float>() }

    LaunchedEffect(amplitude) {
        points.add(amplitude)
        if (points.size > 60) { // Keep last 60 points for smoother curves
            points.removeAt(0)
        }
    }

    androidx.compose.foundation.Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerY = height / 2f
        val stepX = width / 60f

        if (points.isEmpty()) return@Canvas

        // Create smooth path using cubic curves
        val path = androidx.compose.ui.graphics.Path()

        var prevY = centerY
        points.forEachIndexed { index, value ->
            // Normalize amplitude with improved scaling
            val normalizedH = (value / 10000f) * (height / 2f)
            val y = centerY - normalizedH.coerceIn(-height / 2f, height / 2f)
            val x = index * stepX

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                // Use quadratic curve for smoother lines
                val prevX = (index - 1) * stepX
                val controlX = (prevX + x) / 2f
                path.quadraticBezierTo(controlX, prevY, x, y)
            }
            prevY = y
        }

        // Create fill path
        val fillPath = androidx.compose.ui.graphics.Path()
        fillPath.addPath(path)

        // Add bottom mirror
        val bottomPath = androidx.compose.ui.graphics.Path()
        var prevBottomY = centerY
        points.reversed().forEachIndexed { index, value ->
            val normalizedH = (value / 10000f) * (height / 2f)
            val y = centerY + normalizedH.coerceIn(-height / 2f, height / 2f)
            val x = (points.size - 1 - index) * stepX

            if (index == 0) {
                bottomPath.lineTo(x, y)
            } else {
                val nextX = (points.size - index) * stepX
                val controlX = (x + nextX) / 2f
                bottomPath.quadraticBezierTo(controlX, prevBottomY, x, y)
            }
            prevBottomY = y
        }

        fillPath.addPath(bottomPath)
        fillPath.close()

        // Draw gradient fill
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF8B5CF6).copy(alpha = 0.4f),
                    Color(0xFFA78BFA).copy(alpha = 0.2f),
                    Color(0xFF8B5CF6).copy(alpha = 0.4f)
                ),
                startY = 0f,
                endY = height
            )
        )

        // Draw center line
        drawLine(
            color = Color.White.copy(alpha = 0.1f),
            start = androidx.compose.ui.geometry.Offset(0f, centerY),
            end = androidx.compose.ui.geometry.Offset(width, centerY),
            strokeWidth = 1f
        )

        // Draw top stroke with glow effect
        drawPath(
            path = path,
            color = Color(0xFFA78BFA).copy(alpha = 0.3f),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 6f)
        )
        drawPath(
            path = path,
            color = Color(0xFFC4B5FD),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3f)
        )

        // Draw bottom mirror stroke
        drawPath(
            path = bottomPath,
            color = Color(0xFFA78BFA).copy(alpha = 0.3f),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 6f)
        )
        drawPath(
            path = bottomPath,
            color = Color(0xFFC4B5FD),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3f)
        )

        // Draw grid lines for reference
        for (i in 1..3) {
            val y = (height / 4f) * i
            drawLine(
                color = Color.White.copy(alpha = 0.05f),
                start = androidx.compose.ui.geometry.Offset(0f, y),
                end = androidx.compose.ui.geometry.Offset(width, y),
                strokeWidth = 1f
            )
        }
    }
}
