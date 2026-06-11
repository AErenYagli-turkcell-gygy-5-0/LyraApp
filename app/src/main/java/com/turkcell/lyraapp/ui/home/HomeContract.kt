package com.turkcell.lyraapp.ui.home

import com.turkcell.lyraapp.data.home.PlaylistItem
import com.turkcell.lyraapp.data.home.QuickSelectItem
import com.turkcell.lyraapp.data.home.TrackItem

/**
 * Home ekranının MVI sözleşmesi: State, Intent ve Effect tek dosyada toplanmıştır.
 */

/**
 * Ekranın gözlemlenebilir tüm durumu. Tek immutable kaynak (single source of truth).
 *
 * [greetingLabel] günün saatine göre ViewModel tarafından türetilir; doğrudan set edilmez.
 */
data class HomeUiState(
    val isLoading: Boolean = false,
    val greetingLabel: String = "İyi akşamlar",
    val userInitials: String = "ZK",
    val quickSelects: List<QuickSelectItem> = emptyList(),
    val recentTracks: List<TrackItem> = emptyList(),
    val suggestedPlaylists: List<PlaylistItem> = emptyList(),
    val currentTrack: TrackItem? = null,
    val isPlaying: Boolean = false,
    val isFavorite: Boolean = false,
    val selectedTab: HomeTab = HomeTab.Home,
)

enum class HomeTab { Home, Search, Library, Favorites, Profile }

/**
 * Kullanıcıdan gelen niyetler. UI yalnızca bu tipleri yayımlar; iş mantığını çalıştırmaz.
 */
sealed interface HomeIntent {
    data object LoadData : HomeIntent
    data class QuickSelectClicked(val id: String) : HomeIntent
    data class TrackClicked(val id: String) : HomeIntent
    data class PlaylistClicked(val id: String) : HomeIntent
    data object PlayPauseClicked : HomeIntent
    data object SkipNextClicked : HomeIntent
    data object FavoriteClicked : HomeIntent
    data object ThemeToggleClicked : HomeIntent
    data class TabSelected(val tab: HomeTab) : HomeIntent
    data object SeeAllRecentClicked : HomeIntent
    data object ProfileClicked : HomeIntent
}

/**
 * Tek seferlik olaylar: navigasyon, snackbar. State içinde tutulmaz;
 * konfigürasyon değişiminde tekrar tetiklenmez.
 */
sealed interface HomeEffect {
    data class ShowError(val message: String) : HomeEffect
    data object ToggleTheme : HomeEffect
    data object NavigateToSearch : HomeEffect
    data object NavigateToLibrary : HomeEffect
    data object NavigateToFavorites : HomeEffect
    data object NavigateToProfile : HomeEffect
}
