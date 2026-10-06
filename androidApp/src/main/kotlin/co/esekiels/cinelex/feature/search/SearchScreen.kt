/*
 * Cinelex
 * SearchScreen
 *
 * Created by Esekiel Surbakti on 04/10/26
 */

package co.esekiels.cinelex.feature.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.esekiels.cinelex.R
import co.esekiels.cinelex.core.common.UiState
import co.esekiels.cinelex.core.design.TestTag
import co.esekiels.cinelex.core.design.components.MovieRow
import co.esekiels.cinelex.core.design.components.Unavailable
import co.esekiels.cinelex.core.design.errorMessage
import co.esekiels.cinelex.core.design.theme.CinelexTheme
import co.esekiels.cinelex.model.Movie
import kotlinx.coroutines.flow.filter
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
	viewModel: SearchViewModel = koinViewModel(),
	onMovieClick: (movie: Movie, source: String) -> Unit = { _, _ -> },
) {
	val query by viewModel.query.collectAsStateWithLifecycle()
	val state by viewModel.state.collectAsStateWithLifecycle()
	val recommendations by viewModel.recommendations.collectAsStateWithLifecycle()
	val isLoadingMore by viewModel.isLoadingMore.collectAsStateWithLifecycle()
	SearchContent(
		query,
		state,
		recommendations,
		isLoadingMore,
		viewModel::onQueryChange,
		onMovieClick,
		viewModel::loadMore,
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContent(
	query: String,
	state: UiState<List<Movie>>?,
	recommendations: List<Movie>,
	isLoadingMore: Boolean,
	onQueryChange: (String) -> Unit,
	onMovieClick: (movie: Movie, source: String) -> Unit = { _, _ -> },
	onLoadMore: () -> Unit = {},
) {
	Scaffold(topBar = { TopAppBar(title = { Text(stringResource(R.string.search)) }) }) { padding ->
		Column(Modifier.fillMaxSize().padding(padding)) {
			SearchField(query, onQueryChange)
			when (val results = if (query.isBlank()) null else state) {
				null ->
					MovieList(recommendations, TestTag.RECOMMENDATION_ROW, onMovieClick) {
						Text(
							stringResource(R.string.search_recommendations),
							style = MaterialTheme.typography.titleLarge,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(horizontal = 16.dp),
						)
					}

				is UiState.Loaded ->
					MovieList(
						results.data,
						TestTag.SEARCH_RESULT_ROW,
						onMovieClick,
						showsDetails = true,
						isLoadingMore = isLoadingMore,
						onLoadMore = onLoadMore,
					)

				UiState.Loading ->
					Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }

				UiState.Empty ->
					Unavailable(
						Icons.Default.Search,
						stringResource(R.string.search_empty, query.trim()),
						Modifier.fillMaxSize().testTag(TestTag.SEARCH_EMPTY),
					)

				is UiState.Error ->
					Unavailable(
						Icons.Default.Warning,
						stringResource(R.string.search_error),
						Modifier.fillMaxSize().testTag(TestTag.SEARCH_ERROR),
						stringResource(errorMessage(results.code)),
					)
			}
		}
	}
}

@Composable
private fun SearchField(
	query: String,
	onQueryChange: (String) -> Unit,
) {
	val focusManager = LocalFocusManager.current
	OutlinedTextField(
		value = query,
		onValueChange = onQueryChange,
		modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
		placeholder = { Text(stringResource(R.string.search_prompt)) },
		leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
		trailingIcon = {
			if (query.isNotEmpty()) {
				IconButton({ onQueryChange("") }) { Icon(Icons.Default.Close, stringResource(R.string.action_clear)) }
			}
		},
		singleLine = true,
		shape = RoundedCornerShape(12.dp),
		keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
		keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
	)
}

/** `showsDetails` is off for recommendations: the cached list keeps neither rating nor release date. */
@Composable
private fun MovieList(
	movies: List<Movie>,
	rowTag: String,
	onMovieClick: (movie: Movie, source: String) -> Unit,
	showsDetails: Boolean = false,
	isLoadingMore: Boolean = false,
	onLoadMore: () -> Unit = {},
	header: @Composable () -> Unit = {},
) {
	val listState = rememberLazyListState()
	val focusManager = LocalFocusManager.current
	// Match iOS: scrolling the results hides the keyboard so it doesn't cover the list.
	LaunchedEffect(listState) {
		snapshotFlow { listState.isScrollInProgress }.filter { it }.collect { focusManager.clearFocus() }
	}
	LazyColumn(
		Modifier.fillMaxSize(),
		state = listState,
		contentPadding = PaddingValues(vertical = 8.dp),
		verticalArrangement = Arrangement.spacedBy(12.dp),
	) {
		item { header() }
		itemsIndexed(movies, key = { _, movie -> movie.id }) { index, movie ->
			MovieRow(movie, rowTag, showsDetails, onMovieClick)
			// Keyed on the list so a new search ending on the same movie still triggers paging.
			if (index == movies.lastIndex) LaunchedEffect(movies) { onLoadMore() }
		}
		if (isLoadingMore) {
			item {
				Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
			}
		}
	}
}

private val previewMovies =
	listOf(
		Movie(id = 278, title = "The Shawshank Redemption", releaseDate = "1994-09-23", voteAverage = 8.7),
		Movie(id = 238, title = "The Godfather", releaseDate = "1972-03-14", voteAverage = 8.7),
	)

@Preview(showBackground = true)
@Composable
private fun RecommendationsPreview() {
	CinelexTheme {
		SearchContent("", null, previewMovies, isLoadingMore = false, onQueryChange = {})
	}
}

@Preview(showBackground = true)
@Composable
private fun ResultsPreview() {
	CinelexTheme {
		SearchContent("the", UiState.Loaded(previewMovies), emptyList(), isLoadingMore = true, onQueryChange = {})
	}
}

@Preview(showBackground = true)
@Composable
private fun EmptyPreview() {
	CinelexTheme {
		SearchContent("zzz", UiState.Empty, emptyList(), isLoadingMore = false, onQueryChange = {})
	}
}

@Preview(showBackground = true)
@Composable
private fun ErrorPreview() {
	CinelexTheme {
		SearchContent(
			"god",
			UiState.Error("E001", "Server unreachable"),
			emptyList(),
			isLoadingMore = false,
			onQueryChange = {},
		)
	}
}
