package com.turkcell.lyraapp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.turkcell.lyraapp.data.home.ArtworkVariant
import com.turkcell.lyraapp.data.home.PlaylistItem
import com.turkcell.lyraapp.data.home.QuickSelectItem
import com.turkcell.lyraapp.data.home.TrackItem
import com.turkcell.lyraapp.ui.icons.LyraIcons
import com.turkcell.lyraapp.ui.theme.LyraAppTheme

/**
 * Home akışının durumlu (stateful) giriş noktası.
 *
 * [HomeViewModel]'i Hilt'ten alır, durumu yaşam döngüsüne duyarlı şekilde toplar ve
 * tek seferlik [HomeEffect]'leri tüketir. UI ile iş mantığı arasındaki tek köprü burasıdır.
 */
@Composable
fun HomeRoute(
    onToggleTheme: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToLibrary: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.onIntent(HomeIntent.LoadData)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
                HomeEffect.ToggleTheme -> onToggleTheme()
                HomeEffect.NavigateToSearch -> onNavigateToSearch()
                HomeEffect.NavigateToLibrary -> onNavigateToLibrary()
                HomeEffect.NavigateToFavorites -> onNavigateToFavorites()
                HomeEffect.NavigateToProfile -> onNavigateToProfile()
            }
        }
    }

    HomeScreen(
        state = uiState,
        onIntent = viewModel::onIntent,
        snackbarHostState = snackbarHostState,
        modifier = modifier,
    )
}

/**
 * Home ("Ana Sayfa") ekranı.
 *
 * Tamamen durumsuzdur (stateless): durumu [state] üzerinden alır, kullanıcı etkileşimlerini
 * [onIntent] ile yukarı yayımlar. İş mantığı veya state sahipliği bu katmanda bulunmaz.
 */
@Composable
fun HomeScreen(
    state: HomeUiState,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Column {
                if (state.currentTrack != null) {
                    MiniPlayer(
                        track = state.currentTrack,
                        isPlaying = state.isPlaying,
                        isFavorite = state.isFavorite,
                        onPlayPause = { onIntent(HomeIntent.PlayPauseClicked) },
                        onSkipNext = { onIntent(HomeIntent.SkipNextClicked) },
                        onFavorite = { onIntent(HomeIntent.FavoriteClicked) },
                    )
                }
                HomeNavigationBar(
                    selectedTab = state.selectedTab,
                    onTabSelected = { onIntent(HomeIntent.TabSelected(it)) },
                )
            }
        },
    ) { innerPadding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
            ) {
                GreetingHeader(
                    greetingLabel = state.greetingLabel,
                    userInitials = state.userInitials,
                    onThemeToggle = { onIntent(HomeIntent.ThemeToggleClicked) },
                )

                if (state.quickSelects.isNotEmpty()) {
                    Spacer(Modifier.height(20.dp))
                    QuickSelectsSection(
                        items = state.quickSelects,
                        onItemClick = { onIntent(HomeIntent.QuickSelectClicked(it)) },
                    )
                }

                if (state.recentTracks.isNotEmpty()) {
                    Spacer(Modifier.height(28.dp))
                    RecentTracksSection(
                        tracks = state.recentTracks,
                        onTrackClick = { onIntent(HomeIntent.TrackClicked(it)) },
                        onSeeAll = { onIntent(HomeIntent.SeeAllRecentClicked) },
                    )
                }

                if (state.suggestedPlaylists.isNotEmpty()) {
                    Spacer(Modifier.height(28.dp))
                    SuggestedPlaylistsSection(
                        playlists = state.suggestedPlaylists,
                        onPlaylistClick = { onIntent(HomeIntent.PlaylistClicked(it)) },
                    )
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

// ── Selamlama başlığı ────────────────────────────────────────────────────────

@Composable
private fun GreetingHeader(
    greetingLabel: String,
    userInitials: String,
    onThemeToggle: () -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = greetingLabel,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            IconButton(onClick = onThemeToggle) {
                Icon(
                    imageVector = LyraIcons.WbSunny,
                    contentDescription = "Tema değiştir",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(22.dp),
                )
            }
            Spacer(Modifier.width(4.dp))
            UserAvatar(initials = userInitials)
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Ne dinlemek istersin?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun UserAvatar(initials: String) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initials,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}

// ── Hızlı seçimler ──────────────────────────────────────────────────────────

@Composable
private fun QuickSelectsSection(
    items: List<QuickSelectItem>,
    onItemClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                row.forEach { item ->
                    QuickSelectCard(
                        item = item,
                        onClick = { onItemClick(item.id) },
                        modifier = Modifier.weight(1f),
                    )
                }
                if (row.size < 2) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun QuickSelectCard(
    item: QuickSelectItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor = item.variant.containerColor()
    val onContainerColor = item.variant.onContainerColor()

    Box(
        modifier = modifier
            .height(64.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(containerColor)
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .width(56.dp)
                    .fillMaxSize()
                    .background(onContainerColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = LyraIcons.Waveform,
                    contentDescription = null,
                    tint = onContainerColor.copy(alpha = 0.7f),
                    modifier = Modifier.size(28.dp),
                )
            }
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = onContainerColor,
                modifier = Modifier.padding(horizontal = 12.dp),
                maxLines = 1,
            )
        }
    }
}

// ── Son çalınanlar ──────────────────────────────────────────────────────────

@Composable
private fun RecentTracksSection(
    tracks: List<TrackItem>,
    onTrackClick: (String) -> Unit,
    onSeeAll: () -> Unit,
) {
    Column {
        SectionHeader(
            title = "Son çalınanlar",
            actionLabel = "Tümü",
            onAction = onSeeAll,
        )
        Spacer(Modifier.height(12.dp))
        LazyRow(
            state = rememberLazyListState(),
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(tracks, key = { it.id }) { track ->
                RecentTrackCard(
                    track = track,
                    onClick = { onTrackClick(track.id) },
                )
            }
        }
    }
}

@Composable
private fun RecentTrackCard(
    track: TrackItem,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .width(140.dp)
            .clickable(onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(track.variant.containerColor()),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = LyraIcons.Waveform,
                contentDescription = null,
                tint = track.variant.onContainerColor().copy(alpha = 0.6f),
                modifier = Modifier.size(48.dp),
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = track.title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
        )
        Text(
            text = track.artist,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
        )
    }
}

// ── Senin için çalma listeleri ───────────────────────────────────────────────

@Composable
private fun SuggestedPlaylistsSection(
    playlists: List<PlaylistItem>,
    onPlaylistClick: (String) -> Unit,
) {
    Column {
        SectionHeader(title = "Senin için çalma listeleri")
        Spacer(Modifier.height(12.dp))
        LazyRow(
            state = rememberLazyListState(),
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(playlists, key = { it.id }) { playlist ->
                PlaylistCard(
                    playlist = playlist,
                    onClick = { onPlaylistClick(playlist.id) },
                )
            }
        }
    }
}

@Composable
private fun PlaylistCard(
    playlist: PlaylistItem,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .clickable(onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(playlist.variant.containerColor()),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = LyraIcons.LibraryMusic,
                contentDescription = null,
                tint = playlist.variant.onContainerColor().copy(alpha = 0.6f),
                modifier = Modifier.size(40.dp),
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = playlist.title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
        )
    }
}

// ── Yardımcı bileşenler ──────────────────────────────────────────────────────

@Composable
private fun SectionHeader(
    title: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        if (actionLabel != null && onAction != null) {
            Text(
                text = actionLabel,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(onClick = onAction),
            )
        }
    }
}

// ── Mini player ──────────────────────────────────────────────────────────────

@Composable
private fun MiniPlayer(
    track: TrackItem,
    isPlaying: Boolean,
    isFavorite: Boolean,
    onPlayPause: () -> Unit,
    onSkipNext: () -> Unit,
    onFavorite: () -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.35f)
                        .height(2.dp)
                        .background(MaterialTheme.colorScheme.primary),
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(track.variant.containerColor()),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = LyraIcons.Waveform,
                        contentDescription = null,
                        tint = track.variant.onContainerColor().copy(alpha = 0.7f),
                        modifier = Modifier.size(22.dp),
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = track.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                    )
                    Text(
                        text = track.artist,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                    )
                }
                PlayerIconButton(
                    icon = if (isFavorite) LyraIcons.FavoriteFilled else LyraIcons.FavoriteBorder,
                    contentDescription = if (isFavorite) "Favorilerden çıkar" else "Favorilere ekle",
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    onClick = onFavorite,
                )
                PlayerIconButton(
                    icon = if (isPlaying) LyraIcons.Pause else LyraIcons.PlayArrow,
                    contentDescription = if (isPlaying) "Duraklat" else "Oynat",
                    tint = MaterialTheme.colorScheme.onSurface,
                    onClick = onPlayPause,
                )
                PlayerIconButton(
                    icon = LyraIcons.SkipNext,
                    contentDescription = "Sonraki parça",
                    tint = MaterialTheme.colorScheme.onSurface,
                    onClick = onSkipNext,
                )
            }
        }
    }
}

@Composable
private fun PlayerIconButton(
    icon: ImageVector,
    contentDescription: String,
    tint: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(24.dp),
        )
    }
}

// ── Alt navigasyon çubuğu ────────────────────────────────────────────────────

private data class NavItem(
    val tab: HomeTab,
    val label: String,
    val icon: ImageVector,
)

private val navItems = listOf(
    NavItem(HomeTab.Home, "Ana sayfa", LyraIcons.Home),
    NavItem(HomeTab.Search, "Ara", LyraIcons.Search),
    NavItem(HomeTab.Library, "Kütüphane", LyraIcons.LibraryMusic),
    NavItem(HomeTab.Favorites, "Favoriler", LyraIcons.FavoriteBorder),
    NavItem(HomeTab.Profile, "Profil", LyraIcons.Person),
)

@Composable
private fun HomeNavigationBar(
    selectedTab: HomeTab,
    onTabSelected: (HomeTab) -> Unit,
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        navItems.forEach { item ->
            NavigationBarItem(
                selected = selectedTab == item.tab,
                onClick = { onTabSelected(item.tab) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall,
                    )
                },
            )
        }
    }
}

// ── ArtworkVariant → ColorScheme eşlemesi ────────────────────────────────────

@Composable
private fun ArtworkVariant.containerColor() = when (this) {
    ArtworkVariant.Primary -> MaterialTheme.colorScheme.primaryContainer
    ArtworkVariant.Secondary -> MaterialTheme.colorScheme.secondaryContainer
    ArtworkVariant.Tertiary -> MaterialTheme.colorScheme.tertiaryContainer
}

@Composable
private fun ArtworkVariant.onContainerColor() = when (this) {
    ArtworkVariant.Primary -> MaterialTheme.colorScheme.onPrimaryContainer
    ArtworkVariant.Secondary -> MaterialTheme.colorScheme.onSecondaryContainer
    ArtworkVariant.Tertiary -> MaterialTheme.colorScheme.onTertiaryContainer
}

// ── Preview'lar ──────────────────────────────────────────────────────────────

private val previewQuickSelects = listOf(
    QuickSelectItem("qs1", "Gece Sürüşü", ArtworkVariant.Tertiary),
    QuickSelectItem("qs2", "Sabah Kahvesi", ArtworkVariant.Primary),
    QuickSelectItem("qs3", "Neon Sokaklar", ArtworkVariant.Tertiary),
    QuickSelectItem("qs4", "Odaklan", ArtworkVariant.Secondary),
    QuickSelectItem("qs5", "Derin Mavi", ArtworkVariant.Primary),
    QuickSelectItem("qs6", "Yaz Anıları", ArtworkVariant.Secondary),
)

private val previewTracks = listOf(
    TrackItem("t1", "Neon Sokaklar", "Şehir Işıkları", ArtworkVariant.Tertiary),
    TrackItem("t2", "Derin Mavi", "Okyanus", ArtworkVariant.Primary),
    TrackItem("t3", "Yıldız Tozu", "Polaris", ArtworkVariant.Secondary),
)

private val previewPlaylists = listOf(
    PlaylistItem("p1", "Gece Sürüşü", ArtworkVariant.Tertiary),
    PlaylistItem("p2", "Sabah Keyfi", ArtworkVariant.Secondary),
    PlaylistItem("p3", "Odak Modu", ArtworkVariant.Primary),
)

@Preview(name = "Home - Dark", showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenDarkPreview() {
    LyraAppTheme(darkTheme = true) {
        HomeScreen(
            state = HomeUiState(
                greetingLabel = "İyi akşamlar",
                userInitials = "ZK",
                quickSelects = previewQuickSelects,
                recentTracks = previewTracks,
                suggestedPlaylists = previewPlaylists,
                currentTrack = previewTracks.first(),
                isPlaying = true,
                isFavorite = true,
                selectedTab = HomeTab.Home,
            ),
            onIntent = {},
        )
    }
}

@Preview(name = "Home - Light", showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenLightPreview() {
    LyraAppTheme(darkTheme = false) {
        HomeScreen(
            state = HomeUiState(
                greetingLabel = "İyi günler",
                userInitials = "ZK",
                quickSelects = previewQuickSelects,
                recentTracks = previewTracks,
                suggestedPlaylists = previewPlaylists,
                currentTrack = previewTracks.first(),
                isPlaying = false,
                isFavorite = false,
                selectedTab = HomeTab.Home,
            ),
            onIntent = {},
        )
    }
}

@Preview(name = "Home - Loading", showBackground = true)
@Composable
private fun HomeScreenLoadingPreview() {
    LyraAppTheme(darkTheme = true) {
        HomeScreen(
            state = HomeUiState(isLoading = true),
            onIntent = {},
        )
    }
}
