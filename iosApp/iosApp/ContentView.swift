import SwiftUI
import Shared

//
extension Movie: @retroactive Identifiable {}

struct ContentView: View {
    private let platformName = CinelexSDK.shared.platformName

    private let movie = Movie(
        id: 278,
        title: "The Shawshank Redemption",
        backdropPath: nil,
        posterPath: "/poster.jpeg",
        releaseDate: "1994-09-23",
        voteAverage: 8.71,
        voteCount: 28_000,
        genreIds: [18, 80]
    )

    var body: some View {
        VStack(spacing: 12) {
            Text(movie.title).bold()
            Text("rating \(movie.rating) · \(movie.posterUrl ?? "no poster")")
                            .font(.footnote)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .padding()
    }
}

#Preview {
    ContentView()
}
