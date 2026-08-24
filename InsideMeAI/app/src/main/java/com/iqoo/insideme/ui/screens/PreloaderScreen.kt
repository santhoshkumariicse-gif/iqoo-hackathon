package com.iqoo.insideme.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun PreloaderScreen(onComplete: () -> Unit) {
    var progress by remember { mutableStateOf(0f) }
    
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "pulse_alpha"
    )

    LaunchedEffect(Unit) {
        while (progress < 100f) {
            delay(100)
            progress += (Math.random() * 5 + 1).toFloat()
            if (progress >= 100f) progress = 100f
        }
        delay(500)
        onComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF151515), Color(0xFF000000)),
                    center = Offset.Unspecified,
                    radius = 1500f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // HUD Texts
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(32.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text("DATA ANALYSIS", color = Color(0xFFA0AEC0), fontSize = 10.sp, letterSpacing = 2.sp)
                Text("10/01/2023", color = Color(0x66FFFFFF), fontSize = 10.sp, modifier = Modifier.padding(top = 4.dp))
                Text("2.45", color = Color(0x66FFFFFF), fontSize = 10.sp, modifier = Modifier.padding(top = 4.dp))
            }

            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(32.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text("OPTICAL LENS", color = Color(0xFFA0AEC0), fontSize = 10.sp, letterSpacing = 2.sp)
                Text("MACL NB-5A.3", color = Color(0x66FFFFFF), fontSize = 10.sp, modifier = Modifier.padding(top = 4.dp))
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Central Emblem
            Box(
                modifier = Modifier.size(240.dp),
                contentAlignment = Alignment.Center
            ) {
                // Outer Ring
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = Color(0xFF2D3748),
                        radius = size.width / 2,
                        style = Stroke(width = 16.dp.toPx())
                    )
                    drawCircle(
                        color = Color(0xFFF7A400).copy(alpha = 0.2f),
                        radius = size.width / 2,
                        style = Stroke(width = 24.dp.toPx())
                    )
                }

                // Inner Ring & Brain SVG
                Canvas(modifier = Modifier.size(160.dp)) {
                    drawCircle(
                        color = Color(0xFF4A5568),
                        radius = size.width / 2,
                        style = Stroke(width = 8.dp.toPx())
                    )
                    
                    // Glow
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFF7A400).copy(alpha = 0.3f * pulseAlpha), Color.Transparent)
                        ),
                        radius = size.width / 2
                    )

                    // Draw Brain & M Shape (Simplified representation)
                    translate(left = size.width * 0.15f, top = size.height * 0.15f) {
                        val w = size.width * 0.7f
                        val h = size.height * 0.7f
                        
                        // M Shape
                        val pathM = Path().apply {
                            moveTo(w * 0.2f, h * 0.8f)
                            lineTo(w * 0.2f, h * 0.2f)
                            lineTo(w * 0.5f, h * 0.6f)
                            lineTo(w * 0.8f, h * 0.2f)
                            lineTo(w * 0.8f, h * 0.8f)
                        }
                        
                        drawPath(
                            path = pathM,
                            color = Color(0xFFF7A400),
                            style = Stroke(
                                width = 8.dp.toPx(),
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                        drawPath(
                            path = pathM,
                            color = Color.White,
                            style = Stroke(
                                width = 2.dp.toPx(),
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // INSIDEME Title
            Text(
                text = "INSIDEME",
                style = TextStyle(
                    fontSize = 54.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 6.sp,
                    color = Color.White
                )
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Progress Section
            Text(
                text = "PROGRESS: ${progress.toInt()}%",
                color = Color(0xFFCBD5E0),
                fontSize = 12.sp,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .height(16.dp)
                    .background(Color(0xFF2D3748), shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                    .padding(2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress / 100f)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFFFF5500), Color(0xFFF7A400))
                            ),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(6.dp)
                        )
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "SYSTEM RECALL: ANALYZING TEMPORAL SEGMENTS...",
                color = Color(0x66FFFFFF),
                fontSize = 10.sp,
                letterSpacing = 1.sp
            )
        }
    }
}
