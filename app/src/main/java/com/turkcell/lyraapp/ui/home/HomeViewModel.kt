package com.turkcell.lyraapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.lyraapp.data.home.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

/**
 * Home ekranının MVI ViewModel'i.
 *
 * Tek giriş noktası [onIntent]'tir. Durum [uiState] üzerinden gözlemlenir;
 * tek seferlik olaylar [effect] kanalından akar.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _effect = Channel<HomeEffect>(Channel.BUFFERED)
    val effect: Flow<HomeEffect> = _effect.receiveAsFlow()

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.LoadData -> loadData()
            is HomeIntent.QuickSelectClicked -> {} // detay ekranı eklenince bağlanacak
            is HomeIntent.TrackClicked -> onTrackClicked(intent.id)
            is HomeIntent.PlaylistClicked -> {} // detay ekranı eklenince bağlanacak
            is HomeIntent.PlayPauseClicked -> togglePlayPause()
            is HomeIntent.SkipNextClicked -> skipToNext()
            is HomeIntent.FavoriteClicked -> toggleFavorite()
            is HomeIntent.ThemeToggleClicked -> viewModelScope.launch {
                _effect.send(HomeEffect.ToggleTheme)
            }
            is HomeIntent.TabSelected -> onTabSelected(intent.tab)
            is HomeIntent.SeeAllRecentClicked -> {} // liste ekranı eklenince bağlanacak
            is HomeIntent.ProfileClicked -> viewModelScope.launch {
                _effect.send(HomeEffect.NavigateToProfile)
            }
        }
    }

    private fun loadData() {
        if (_uiState.value.isLoading) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, greetingLabel = computeGreeting()) }

            val quickSelectsResult = homeRepository.getQuickSelects()
            val recentTracksResult = homeRepository.getRecentTracks()
            val playlistsResult = homeRepository.getSuggestedPlaylists()

            quickSelectsResult.onFailure {
                _effect.send(HomeEffect.ShowError(it.message ?: "Veri yüklenemedi."))
            }
            recentTracksResult.onFailure {
                _effect.send(HomeEffect.ShowError(it.message ?: "Veri yüklenemedi."))
            }
            playlistsResult.onFailure {
                _effect.send(HomeEffect.ShowError(it.message ?: "Veri yüklenemedi."))
            }

            val tracks = recentTracksResult.getOrDefault(emptyList())
            _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    quickSelects = quickSelectsResult.getOrDefault(emptyList()),
                    recentTracks = tracks,
                    suggestedPlaylists = playlistsResult.getOrDefault(emptyList()),
                    currentTrack = tracks.firstOrNull(),
                    isPlaying = tracks.isNotEmpty(),
                )
            }
        }
    }

    private fun onTrackClicked(id: String) {
        val track = _uiState.value.recentTracks.firstOrNull { it.id == id } ?: return
        _uiState.update { it.copy(currentTrack = track, isPlaying = true, isFavorite = false) }
    }

    private fun togglePlayPause() {
        _uiState.update { it.copy(isPlaying = !it.isPlaying) }
    }

    private fun skipToNext() {
        val tracks = _uiState.value.recentTracks
        val currentId = _uiState.value.currentTrack?.id ?: return
        val currentIndex = tracks.indexOfFirst { it.id == currentId }
        val nextTrack = tracks.getOrNull((currentIndex + 1) % tracks.size) ?: return
        _uiState.update { it.copy(currentTrack = nextTrack, isPlaying = true, isFavorite = false) }
    }

    private fun toggleFavorite() {
        _uiState.update { it.copy(isFavorite = !it.isFavorite) }
    }

    private fun onTabSelected(tab: HomeTab) {
        _uiState.update { it.copy(selectedTab = tab) }
        viewModelScope.launch {
            when (tab) {
                HomeTab.Home -> Unit
                HomeTab.Search -> _effect.send(HomeEffect.NavigateToSearch)
                HomeTab.Library -> _effect.send(HomeEffect.NavigateToLibrary)
                HomeTab.Favorites -> _effect.send(HomeEffect.NavigateToFavorites)
                HomeTab.Profile -> _effect.send(HomeEffect.NavigateToProfile)
            }
        }
    }

    /** Günün saatine göre türetilen selamlama; Android/Compose bağımlılığı içermez. */
    private fun computeGreeting(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when {
            hour < 12 -> "Günaydın"
            hour < 18 -> "İyi günler"
            else -> "İyi akşamlar"
        }
    }
}
