@file:OptIn(ExperimentalFoundationApi::class)

package com.example1.weatherapplication.core.utils

import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import kotlinx.coroutines.coroutineScope

fun Modifier.noRippleClickable(
    debounceInterval: Long = 500L,
    enableClick: Boolean = true,
    onClick: () -> Unit
): Modifier = composed {
    val context = LocalContext.current
    var lastClickTime by remember { mutableLongStateOf(0L) }
    this.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        val currentTime = System.currentTimeMillis()
        if ((currentTime - lastClickTime) < debounceInterval) return@clickable
        lastClickTime = currentTime
        if (enableClick == true) {
            onClick()
        } else {
            Toast.makeText(context, "Action is not allowed", Toast.LENGTH_SHORT).show()
        }
    }
}

fun Modifier.disableSplitMotionEvents() =
    this.pointerInput(Unit) {
        coroutineScope {
            var currentId: Long = -1L
            awaitPointerEventScope {
                while (true) {
                    awaitPointerEvent(PointerEventPass.Initial).changes.forEach { pointerInfo ->
                        when {
                            pointerInfo.pressed && currentId == -1L -> currentId =
                                pointerInfo.id.value

                            pointerInfo.pressed.not() && currentId == pointerInfo.id.value -> currentId =
                                -1

                            pointerInfo.id.value != currentId && currentId != -1L -> pointerInfo.consume()
                            else -> Unit
                        }
                    }
                }
            }
        }
    }


fun Modifier.bouncingClickable(
    enabled: Boolean = true,
    onLongClick: () -> Unit = {},
    onClick: () -> Unit = {},
) = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val haptic = LocalHapticFeedback.current

    val animationTransition = updateTransition(isPressed, label = "BouncingClickableTransition")
    val scaleFactor by animationTransition.animateFloat(
        targetValueByState = { pressed -> if (pressed) 0.94f else 1f },
        label = "BouncingClickableScaleFactorTransition",
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        }
    )
    /*val opacity by animationTransition.animateFloat(
        targetValueByState = { pressed -> if (pressed) 0.7f else 1f },
        label = "BouncingClickableOpacityTransition"
    )*/

    this
        .graphicsLayer {
            this.scaleX = scaleFactor
            this.scaleY = scaleFactor
            /*this.alpha = 1f*/
        }
        .combinedClickable(
            interactionSource = interactionSource,
            indication = null,
            enabled = enabled,
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onClick()
            },
            onLongClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onLongClick()
            }
        )
}