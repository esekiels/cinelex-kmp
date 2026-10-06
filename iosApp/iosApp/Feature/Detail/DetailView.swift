//
//  DetailView.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 27/09/26.
//

import Kingfisher
import Shared
import SwiftUI

struct DetailView: View {

    @State private var viewModel: DetailViewModel

    init(viewModel: DetailViewModel) {
        _viewModel = State(initialValue: viewModel)
    }

    var body: some View {
        content
            .task { await viewModel.onAppear() }
            .task { await viewModel.observeWatchlist() }
            .toolbar {
                if isLoaded {
                    ToolbarItem(placement: .topBarTrailing) { watchlistToggle }
                }
            }
            .navigationBarTitleDisplayMode(.inline)
            .background(Color.background)
    }
}

private extension DetailView {

    var isLoaded: Bool {
        if case .loaded = viewModel.state {
            return true
        }
        return false
    }

    var watchlistToggle: some View {
        Button {
            viewModel.toggleWatchlist()
        } label: {
            Image(systemName: viewModel.isSaved ? "bookmark.fill" : "bookmark")
                .accessibilityLabel(Text(viewModel.isSaved ? LocalizedStringKey("watchlist.remove") : "watchlist.add"))
        }
        .accessibilityIdentifier(AccessibilityID.watchlistToggle)
    }

    @ViewBuilder
    var content: some View {
        switch viewModel.state {
        case .loaded(let movie):
            details(movie)
        case .loading, .empty:
            DetailSkeletonView()
        case .error(let code, _):
            ContentUnavailableView {
                Label("detail.error", systemImage: "exclamationmark.triangle")
            } description: {
                Text(.error(code))
            } actions: {
                Button("action.retry") { viewModel.onRefresh() }
                    .buttonStyle(.bordered)
            }
            .accessibilityIdentifier(AccessibilityID.detailError)
        }
    }

    func details(_ movie: MovieDetails) -> some View {
        ScrollView(showsIndicators: false) {
            VStack(alignment: .leading, spacing: 0) {
                backdrop(movie)
                VStack(alignment: .leading, spacing: 20) {
                    info(movie)
                    Text(movie.overview)
                        .font(.body)
                        .foregroundStyle(.textPrimary)
                    rating(movie)
                    castSection(movie.cast)
                    crewSection(movie)
                    trailerSection(movie.youtubeTrailers)
                }
                .padding(16)
            }
        }
        .accessibilityIdentifier(AccessibilityID.detailScreen)
    }

    // MARK: - Header

    func backdrop(_ movie: MovieDetails) -> some View {
        KFImage.url(movie.backdropURL)
            .placeholder { ImagePlaceholder(16 / 9) }
            .resizable()
            .loadDiskFileSynchronously()
            .cacheOriginalImage()
            .scaleFactor(UIScreen.main.scale)
            .fade(duration: 0.2)
            .aspectRatio(16 / 9, contentMode: .fill)
    }

    func info(_ movie: MovieDetails) -> some View {
        VStack(alignment: .leading, spacing: 8) {
            Text(movie.title)
                .font(.title2)
                .fontWeight(.bold)
                .foregroundStyle(.textPrimary)

            (part(movie.genreNames) + separator + part(movie.releaseYear) + separator + length(movie.length))
                .font(.subheadline)
                .foregroundStyle(.textSecondary)
        }
    }

    var separator: Text { Text(verbatim: " · ") }

    func part(_ value: String?) -> Text {
        value.map { Text(verbatim: $0) } ?? Text("detail.notAvailable")
    }

    func length(_ length: Length?) -> Text {
        guard let length else {
            return Text("detail.notAvailable")
        }
        return Text("detail.runtime \(Int(length.hours)) \(Int(length.minutes))")
    }

    func rating(_ movie: MovieDetails) -> some View {
        HStack(spacing: 4) {
            HStack(spacing: 2) {
                ForEach(0..<10, id: \.self) { index in
                    let filled = movie.isStarFilled(index: Int32(index))
                    Image(systemName: filled ? "star.fill" : "star")
                        .font(.caption2)
                        .foregroundStyle(filled ? .yellow : .gray)
                }
            }
            Text(verbatim: movie.scoreRating)
                .font(.subheadline)
                .foregroundStyle(.textSecondary)
        }
        .accessibilityElement(children: .ignore)
        .accessibilityLabel(Text("detail.rating \(movie.scoreRating)"))
    }

    // MARK: - Cast

    @ViewBuilder
    func castSection(_ cast: [Cast]) -> some View {
        if !cast.isEmpty {
            VStack(alignment: .leading, spacing: 8) {
                Text("detail.cast")
                    .font(.headline)
                    .foregroundStyle(.textPrimary)

                ScrollView(.horizontal, showsIndicators: false) {
                    LazyHStack(alignment: .top, spacing: 12) {
                        ForEach(Array(cast.prefix(10).enumerated()), id: \.offset) { _, member in
                            castCard(member)
                        }
                    }
                }
            }
        }
    }

    func castCard(_ member: Cast) -> some View {
        VStack(spacing: 6) {
            KFImage.url(member.profileURL)
                .placeholder {
                    Image(systemName: "person.circle.fill")
                        .resizable()
                        .foregroundStyle(.textSecondary)
                        .accessibilityHidden(true)
                }
                .resizable()
                .scaleFactor(UIScreen.main.scale)
                .fade(duration: 0.2)
                .aspectRatio(contentMode: .fill)
                .frame(width: 56, height: 56)
                .clipShape(Circle())

            Text(member.name)
                .font(.caption)
                .fontWeight(.medium)
                .foregroundStyle(.textPrimary)
                .lineLimit(1)

            Text(member.character)
                .font(.caption2)
                .foregroundStyle(.textSecondary)
                .lineLimit(1)
        }
        .frame(width: 80)
        .accessibilityElement(children: .combine)
        .accessibilityIdentifier(AccessibilityID.castCard)
    }

    // MARK: - Crew

    @ViewBuilder
    func crewSection(_ movie: MovieDetails) -> some View {
        let rows: [(LocalizedStringKey, [Crew])] = [
            ("detail.director", movie.directors),
            ("detail.producer", movie.producers),
            ("detail.writer", movie.screenwriters)
        ]
        if rows.contains(where: { !$0.1.isEmpty }) {
            VStack(alignment: .leading, spacing: 12) {
                Text("detail.crew")
                    .font(.headline)
                    .foregroundStyle(.textPrimary)

                ForEach(rows.indices, id: \.self) { index in
                    crewRow(rows[index].0, crew: rows[index].1)
                }
            }
        }
    }

    @ViewBuilder
    func crewRow(_ title: LocalizedStringKey, crew: [Crew]) -> some View {
        if !crew.isEmpty {
            VStack(alignment: .leading, spacing: 2) {
                Text(title)
                    .font(.subheadline)
                    .foregroundStyle(.textSecondary)

                Text(crew.map(\.name).joined(separator: ", "))
                    .font(.subheadline)
                    .foregroundStyle(.textPrimary)
            }
        }
    }

    // MARK: - Trailers

    @ViewBuilder
    func trailerSection(_ trailers: [Video]) -> some View {
        if !trailers.isEmpty {
            VStack(alignment: .leading, spacing: 8) {
                Text("detail.trailers")
                    .font(.headline)
                    .foregroundStyle(.textPrimary)

                ForEach(trailers) { trailer in
                    if let url = trailer.youtubeURL {
                        trailerRow(trailer.name, url: url)
                    }
                }
            }
        }
    }

    func trailerRow(_ name: String, url: URL) -> some View {
        Link(destination: url) {
            HStack {
                Image(systemName: "play.circle.fill")
                    .font(.title3)
                    .foregroundStyle(Color.colorPrimary)
                    .accessibilityHidden(true)
                Text(name)
                    .font(.subheadline)
                    .foregroundStyle(.textPrimary)
                    .lineLimit(1)
                Spacer()
            }
            .padding(.vertical, 4)
        }
        .accessibilityIdentifier(AccessibilityID.trailerButton)
    }
}

#if DEBUG
#Preview("Loaded") {
    NavigationStack {
        DetailView(
            viewModel: DetailViewModel(
                repository: FakeMovieRepository(nowPlaying: MovieStubs.shared.all),
                watchlist: FakeWatchlistRepository(),
                movieId: 278
            )
        )
    }
}

#Preview("Error") {
    let repository = FakeMovieRepository(nowPlaying: MovieStubs.shared.all)
    repository.detailsFailure = CinelexException(code: ErrorConstants.shared.NETWORK_ERROR, message: "Server unreachable")
    return NavigationStack {
        DetailView(viewModel: DetailViewModel(repository: repository, watchlist: FakeWatchlistRepository(), movieId: 278))
    }
}
#endif
