package com.example.evagnelyrics.ui.theme.component

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization

@Composable
fun EvText(
    @StringRes resource: Int,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onPrimary,
    style: EvTextStyle = EvTextStyle.Body
) {
    Text(
        text = stringResource(id = resource),
        color = color,
        style = when (style) {
            EvTextStyle.Subtitle -> MaterialTheme.typography.subtitle1
            EvTextStyle.Body -> MaterialTheme.typography.body1
            EvTextStyle.Head -> MaterialTheme.typography.h1
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EvTextField(
    value: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onBackground,
    style: EvTextStyle = EvTextStyle.Body,
    @StringRes hint: Int,
    keyboardAction: EvKeyboardAction,
    onGo: (String) -> Unit = {},
    onTextChange: (String) -> Unit,
) {
    val keyBoardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onTextChange,
        textStyle = when (style) {
            EvTextStyle.Subtitle -> MaterialTheme.typography.subtitle1
            EvTextStyle.Body -> MaterialTheme.typography.body1
            EvTextStyle.Head -> MaterialTheme.typography.h1
        },
        placeholder = { EvText(resource = hint, color = MaterialTheme.colors.onBackground) },
        colors = TextFieldDefaults.textFieldColors(
            textColor = color,
            backgroundColor = MaterialTheme.colors.background
        ),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Characters,
            imeAction = if (keyboardAction == EvKeyboardAction.Go) ImeAction.Go else ImeAction.Next,
        ), keyboardActions = KeyboardActions(onGo = {
//            keyBoardController?.hide()
            focusManager.clearFocus()
            onGo(value)
        })
    )
}

enum class EvTextStyle { Subtitle, Body, Head }

enum class EvKeyboardAction { Next, Go }