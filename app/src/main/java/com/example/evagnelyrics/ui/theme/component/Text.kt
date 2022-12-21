package com.example.evagnelyrics.ui.theme.component

import androidx.annotation.StringRes
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp

@Composable
fun EvText(
    @StringRes resource: Int,
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    style: EvTextStyle = EvTextStyle.Body
) {
    Text(
        text = stringResource(id = resource),
        color = color,
        style = when(style){
            EvTextStyle.Subtitle -> MaterialTheme.typography.subtitle1
            EvTextStyle.Body -> MaterialTheme.typography.body1
            EvTextStyle.Head -> MaterialTheme.typography.h1
        },
        modifier = modifier
    )
}

enum class EvTextStyle { Subtitle, Body, Head }