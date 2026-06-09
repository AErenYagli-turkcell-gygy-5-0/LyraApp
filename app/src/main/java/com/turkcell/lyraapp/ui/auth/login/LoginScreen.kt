package com.turkcell.lyraapp.ui.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.turkcell.lyraapp.ui.icons.LyraIcons
import com.turkcell.lyraapp.ui.theme.LyraAppTheme

/**
 * Login ("Tekrar hoş geldin") ekranı.
 *
 * Tamamen durumsuzdur (stateless): herhangi bir state, network veya iş mantığı içermez.
 * Metin alanları salt-görsel olarak (`readOnly`, boş `value`) render edilir; etkileşimli
 * elemanların arka plan davranışı bu katmanda tanımlanmaz.
 */
@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .padding(horizontal = 24.dp),
        ) {
            Spacer(Modifier.weight(0.22f))

            BrandLogo()
            Spacer(Modifier.height(24.dp))

            HeaderTexts()
            Spacer(Modifier.height(28.dp))

            PhoneNumberField()
            Spacer(Modifier.height(14.dp))

            PasswordField()
            Spacer(Modifier.height(10.dp))

            Text(
                text = "Şifremi unuttum",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.End),
            )
            Spacer(Modifier.height(28.dp))

            LoginButton()

            Spacer(Modifier.weight(0.30f))

            RegisterPrompt(modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun BrandLogo() {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = LyraIcons.Waveform,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(28.dp),
        )
    }
}

@Composable
private fun HeaderTexts() {
    Text(
        text = "Tekrar hoş geldin",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
    )
    Spacer(Modifier.height(8.dp))
    Text(
        text = "Hesabına giriş yap, kaldığın yerden dinlemeye devam et.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.secondary,
    )
}

@Composable
private fun PhoneNumberField() {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        readOnly = true,
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        label = { Text("Telefon numarası") },
        prefix = { Text("+90") },
        placeholder = { Text("5XX XXX XX XX") },
        leadingIcon = {
            Icon(
                imageVector = LyraIcons.Smartphone,
                contentDescription = null,
            )
        },
    )
}

@Composable
private fun PasswordField() {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        readOnly = true,
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        placeholder = { Text("Şifre") },
        leadingIcon = {
            Icon(
                imageVector = LyraIcons.Lock,
                contentDescription = null,
            )
        },
        trailingIcon = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = LyraIcons.Visibility,
                    contentDescription = "Şifreyi göster",
                )
            }
        },
    )
}

@Composable
private fun LoginButton() {
    Button(
        onClick = {},
        enabled = false,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
    ) {
        Text(
            text = "Giriş yap",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(Modifier.width(8.dp))
        Icon(
            imageVector = LyraIcons.ArrowForward,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun RegisterPrompt(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Hesabın yok mu?",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = "Kayıt ol",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Preview(name = "Login - Light", showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenLightPreview() {
    LyraAppTheme(darkTheme = false) {
        LoginScreen()
    }
}

@Preview(name = "Login - Dark", showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenDarkPreview() {
    LyraAppTheme(darkTheme = true) {
        LoginScreen()
    }
}