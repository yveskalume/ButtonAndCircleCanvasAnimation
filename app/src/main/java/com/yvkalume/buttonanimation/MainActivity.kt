package com.yvkalume.buttonanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.yvkalume.buttonanimation.ui.theme.ButtonAnimationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ButtonAnimationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Content()
                }
            }
        }
    }
}

enum class AnimationState {
    Expanded, Collapsed
}

fun AnimationState.isExpanded() : Boolean {
    return this == AnimationState.Expanded
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Content() {
    var maxDimension by remember { mutableStateOf(0f) }

    var animationState by remember {
        mutableStateOf(AnimationState.Collapsed)
    }

    val dimension by animateFloatAsState(
        animationSpec = tween(durationMillis = if (animationState.isExpanded()) 2000 else 1000),
        targetValue = if (animationState.isExpanded()) {
            maxDimension
        } else {
            0f
        }
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                maxDimension = this.size.maxDimension
                drawCircle(color = Color.Green, radius = dimension, center = this.center)
            }
    ) {
        AnimatedVisibility(
            visible = !animationState.isExpanded() &&  (dimension == 0f),
            enter = scaleIn(tween(300)),
            exit = scaleOut(tween(0)),
        ) {
            FloatingActionButton(
                onClick = { animationState = AnimationState.Expanded },
                containerColor = Color.Green,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }

        AnimatedVisibility(
            visible = animationState.isExpanded() && (dimension > maxDimension/1.8),
            enter = scaleIn(),
            exit = scaleOut(tween(500))
        ) {
            FloatingActionButton(
                onClick = { animationState = AnimationState.Collapsed },
                containerColor = Color.White,
                contentColor = Color.Green,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Remove, contentDescription = null)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ButtonAnimationTheme {
        Content()
    }
}