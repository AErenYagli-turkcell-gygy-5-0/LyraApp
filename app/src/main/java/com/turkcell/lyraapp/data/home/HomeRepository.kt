package com.turkcell.lyraapp.data.home

/**
 * Renk varyantı: UI katmanı bu enum'u MaterialTheme colorScheme slotlarına eşler;
 * doğrudan Color değeri taşımaz.
 */
enum class ArtworkVariant { Primary, Secondary, Tertiary }

data class QuickSelectItem(
    val id: String,
    val title: String,
    val variant: ArtworkVariant,
)

data class TrackItem(
    val id: String,
    val title: String,
    val artist: String,
    val variant: ArtworkVariant,
)

data class PlaylistItem(
    val id: String,
    val title: String,
    val variant: ArtworkVariant,
)

/**
 * Home ekranı veri soyutlaması.
 *
 * Backend henüz hazır olmadığından şu an yalnızca [FakeHomeRepository] ile sağlanır.
 * Gerçek API geldiğinde yalnızca implementasyon ve di/HomeModule.kt bağlaması değişir.
 */
interface HomeRepository {
    suspend fun getQuickSelects(): Result<List<QuickSelectItem>>
    suspend fun getRecentTracks(): Result<List<TrackItem>>
    suspend fun getSuggestedPlaylists(): Result<List<PlaylistItem>>
}
