package com.example.evagnelyrics.ui.theme.component

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
    color: Color = MaterialTheme.colorScheme.onPrimary,
    style: EvTextStyle = EvTextStyle.Body
) {
    Text(
        text = stringResource(id = resource),
        color = color,
        style = when (style) {
            EvTextStyle.Subtitle -> MaterialTheme.typography.labelMedium
            EvTextStyle.Body -> MaterialTheme.typography.bodyMedium
            EvTextStyle.Head -> MaterialTheme.typography.headlineMedium
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EvTextField(
    value: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
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
            EvTextStyle.Subtitle -> MaterialTheme.typography.labelMedium
            EvTextStyle.Body -> MaterialTheme.typography.bodyMedium
            EvTextStyle.Head -> MaterialTheme.typography.headlineMedium
        },
        placeholder = { EvText(resource = hint, color = MaterialTheme.colorScheme.onBackground) },
        colors = TextFieldDefaults.textFieldColors(
            textColor = color,
            containerColor = MaterialTheme.colorScheme.background
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