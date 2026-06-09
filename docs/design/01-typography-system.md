# LyraApp - Tipografi Sistemi

> Bu dosya LyraApp isimli uygulamanın tipografi ölçeği için
> **tek doğruluk kaynağıdır** (single source of truth) ve
> doğrudan bir **Android Jetpack Compose** projesinde kullanılmak
> üzere düzenlenmiştir.


---

## 1. Temel Kural

> Hiçbir `@Composable` içinde ham `TextStyle(...)` veya elle
> `fontSize = 16.sp` yazılmaz.
> Tipografi daima `MaterialTheme.typography.<slot>` üzerinden
> okunmak zorundadır.

Ham `TextStyle(..)` tanımı yalnızca `Type.kt` içinde, `LyraTypography`
sabit değişkeni tanımlanırken kullanılır.

---

## 2. Font Ailesi

> **Roboto** kullanılır.
> Material 3'ün varsayılan font ailesi zaten Roboto olduğundan,
> `FontFamily.Default` doğrudan Roboto'yu sağlar. Ek font dosyası
> (`res/font`) veya downloadable font yapılandırması gerekmez.

```kotlin
import androidx.compose.ui.text.font.FontFamily

val LyraFontFamily = FontFamily.Default // Roboto (M3 varsayılanı)
```

---

## 3. Tipografi Ölçeği (Material 3 Type Scale)

| Token            | Font   | Weight | Size  | Line Height | Letter Spacing |
|------------------|--------|--------|-------|-------------|----------------|
| displayLarge     | Roboto | Normal | 57sp  | 64sp        | -0.25sp        |
| displayMedium    | Roboto | Normal | 45sp  | 52sp        | 0sp            |
| displaySmall     | Roboto | Normal | 36sp  | 44sp        | 0sp            |
| headlineLarge    | Roboto | Normal | 32sp  | 40sp        | 0sp            |
| headlineMedium   | Roboto | Normal | 28sp  | 36sp        | 0sp            |
| headlineSmall    | Roboto | Normal | 24sp  | 32sp        | 0sp            |
| titleLarge       | Roboto | Normal | 22sp  | 28sp        | 0sp            |
| titleMedium      | Roboto | Medium | 16sp  | 24sp        | 0.15sp         |
| titleSmall       | Roboto | Medium | 14sp  | 20sp        | 0.1sp          |
| bodyLarge        | Roboto | Normal | 16sp  | 24sp        | 0.5sp          |
| bodyMedium       | Roboto | Normal | 14sp  | 20sp        | 0.25sp         |
| bodySmall        | Roboto | Normal | 12sp  | 16sp        | 0.4sp          |
| labelLarge       | Roboto | Medium | 14sp  | 20sp        | 0.1sp          |
| labelMedium      | Roboto | Medium | 12sp  | 16sp        | 0.5sp          |
| labelSmall       | Roboto | Medium | 11sp  | 16sp        | 0.5sp          |

---

## 4. `Type.kt` — TextStyle Token Tanımları

```kotlin
package com.turkcell.lyraapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Roboto = Material 3 varsayılan font ailesi
private val LyraFontFamily = FontFamily.Default

val LyraTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = LyraFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
    ),
    displayMedium = TextStyle(
        fontFamily = LyraFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = LyraFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = LyraFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = LyraFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = LyraFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = LyraFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = LyraFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = LyraFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = LyraFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = LyraFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = LyraFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = LyraFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = LyraFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = LyraFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
)
```

---

## 5. `Theme.kt` — Bağlama

> `LyraTypography`, `Theme.kt` içinde `MaterialTheme`'e geçilir.
> (Renk sistemi dosyasındaki `LyraTheme` zaten `typography = LyraTypography`
> satırını içerir.)

```kotlin
MaterialTheme(
    colorScheme = colorScheme,
    typography = LyraTypography, // bu dosya
    content = content,
)
```

---