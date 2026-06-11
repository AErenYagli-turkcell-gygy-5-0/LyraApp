package com.turkcell.lyraapp.data.home

import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * [HomeRepository]'nin sahte (stub) implementasyonu.
 *
 * Gerçek bir ağ çağrısı yapmaz; delay ile ağ davranışını taklit eder.
 * Gerçek API geldiğinde bu sınıf değiştirilir; ViewModel ve Contract etkilenmez.
 */
class FakeHomeRepository @Inject constructor() : HomeRepository {

    override suspend fun getQuickSelects(): Result<List<QuickSelectItem>> {
        delay(NETWORK_DELAY_MS)
        return Result.success(
            listOf(
                QuickSelectItem("qs1", "Gece Sürüşü", ArtworkVariant.Tertiary),
                QuickSelectItem("qs2", "Sabah Kahvesi", ArtworkVariant.Primary),
                QuickSelectItem("qs3", "Neon Sokaklar", ArtworkVariant.Tertiary),
                QuickSelectItem("qs4", "Odaklan", ArtworkVariant.Secondary),
                QuickSelectItem("qs5", "Derin Mavi", ArtworkVariant.Primary),
                QuickSelectItem("qs6", "Yaz Anıları", ArtworkVariant.Secondary),
            )
        )
    }

    override suspend fun getRecentTracks(): Result<List<TrackItem>> {
        delay(NETWORK_DELAY_MS)
        return Result.success(
            listOf(
                TrackItem("t1", "Neon Sokaklar", "Şehir Işıkları", ArtworkVariant.Tertiary),
                TrackItem("t2", "Derin Mavi", "Okyanus", ArtworkVariant.Primary),
                TrackItem("t3", "Yıldız Tozu", "Polaris", ArtworkVariant.Secondary),
                TrackItem("t4", "Sabah Işığı", "Aurora", ArtworkVariant.Tertiary),
            )
        )
    }

    override suspend fun getSuggestedPlaylists(): Result<List<PlaylistItem>> {
        delay(NETWORK_DELAY_MS)
        return Result.success(
            listOf(
                PlaylistItem("p1", "Gece Sürüşü", ArtworkVariant.Tertiary),
                PlaylistItem("p2", "Sabah Keyfi", ArtworkVariant.Secondary),
                PlaylistItem("p3", "Odak Modu", ArtworkVariant.Primary),
                PlaylistItem("p4", "Yaz Vibes", ArtworkVariant.Secondary),
            )
        )
    }

    private companion object {
        const val NETWORK_DELAY_MS = 800L
    }
}
