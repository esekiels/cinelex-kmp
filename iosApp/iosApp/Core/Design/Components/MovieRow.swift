//
//  MovieRow.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 06/10/26.
//

import Kingfisher
import Shared
import SwiftUI

struct MovieRow: View {

    let movie: Movie
    let source: String
    var showsDetails = false

    var body: some View {
        ZoomLink(movie, source: source) {
            HStack(spacing: 12) {
                KFImage.url(movie.posterURL)
                    .placeholder { ImagePlaceholder(2 / 3) }
                    .resizable()
                    .cacheOriginalImage()
                    .scaleFactor(UIScreen.main.scale)
                    .fade(duration: 0.2)
                    .aspectRatio(2 / 3, contentMode: .fit)
                    .frame(width: 60)
                    .clipShape(RoundedRectangle(cornerRadius: 8))
                VStack(alignment: .leading, spacing: 6) {
                    Text(movie.title)
                        .font(.headline)
                        .foregroundStyle(.textPrimary)
                        .lineLimit(2)
                    if showsDetails {
                        if !movie.releaseDate.isEmpty {
                            Text(verbatim: String(movie.releaseDate.prefix(4)))
                                .font(.subheadline)
                                .foregroundStyle(.textSecondary)
                        }
                        Label {
                            Text(verbatim: movie.rating)
                        } icon: {
                            Image(systemName: "star.fill").foregroundStyle(.yellow)
                        }
                        .font(.subheadline)
                        .foregroundStyle(.textSecondary)
                    }
                }
                Spacer(minLength: 0)
            }
            .contentShape(Rectangle())
        }
        .accessibilityLabel(movie.title)
    }
}
