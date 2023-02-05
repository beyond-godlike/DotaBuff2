package com.unava.dia.dotabuff.presentation.features.players

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import com.unava.dia.dotabuff.presentation.ui.theme.Dimens.Padding
import com.unava.dia.dotabuff.presentation.ui.theme.Dimens.ShimmerHeight
import com.unava.dia.dotabuff.presentation.ui.theme.Dimens.ShimmerWidth
import com.unava.dia.dotabuff.presentation.ui.theme.Dimens.ShimmerWidthName

@Composable
fun ShimmerListItem(
    isLoading: Boolean,
    contentAfterLoading: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isLoading) {
        Row(modifier = modifier) {
            Box(
                modifier = Modifier
                    .size(ShimmerWidth)
                    .clip(CircleShape)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.width(Padding))
            Box(
                modifier = Modifier
                    .width(ShimmerWidthName)
                    .height(ShimmerHeight)
                    .clip(CircleShape)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.width(Padding))
            Box(
                modifier = Modifier
                    .width(ShimmerWidth)
                    .height(ShimmerHeight)
                    .clip(CircleShape)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.width(Padding))
            Box(
                modifier = Modifier
                    .width(ShimmerWidth)
                    .height(ShimmerHeight)
                    .clip(CircleShape)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.width(Padding))
            Box(
                modifier = Modifier
                    .width(ShimmerWidth)
                    .height(ShimmerHeight)
                    .clip(CircleShape)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.width(Padding))
            Box(
                modifier = Modifier
                    .width(ShimmerWidth)
                    .height(ShimmerHeight)
                    .clip(CircleShape)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.width(Padding))
            Box(
                modifier = Modifier
                    .size(ShimmerHeight)
                    .clip(CircleShape)
                    .shimmerEffect()
            )
        }
    } else {
        contentAfterLoading()
    }
}

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition()
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        )
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFB8B5B5),
                Color(0xFF8F8B8B),
                Color(0xFFB8B5B5),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}