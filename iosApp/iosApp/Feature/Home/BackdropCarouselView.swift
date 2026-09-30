//
//  BackdropCarousel.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 20/09/26.
//

import SwiftUI
import Shared
import Kingfisher

struct BackdropCarouselView: View {
    
    let title: String
    let data: [Movie]
    
    var body: some View {
        GeometryReader { geomtry in
            content(geomtry)
        }
        .frame(height: UIScreen.main.bounds.width * 0.75 * (9 / 16) + 70)
    }
}

private extension BackdropCarouselView {
    
    func content(_ geometry: GeometryProxy) -> some View {
        VStack(alignment: .leading, spacing: geometry.size.height * 0.02) {
            Text(title)
                .font(.title)
                .fontWeight(.bold)
                .foregroundStyle(.textPrimary)
            carousel(geometry)
        }
        .padding(.leading, geometry.size.width * 0.04)
    }
    
    func carousel(_ geometry: GeometryProxy) -> some View {
        ScrollView(.horizontal, showsIndicators: false) {
            LazyHStack {
                ForEach(data) { movie in
                    backdropCard(movie, geometry: geometry)
                }
            }
        }
        .scrollTargetLayout()
        .accessibilityIdentifier(AccessibilityID.carousel(title))
    }
    
    func backdropCard(_ movie: Movie, geometry: GeometryProxy) -> some View {
        let cardWidth = geometry.size.width * 0.75
        let cardHeight = cardWidth * (9 / 16)
        
        return NavigationLink(value: movie) {
            VStack(alignment: .leading, spacing: 8) {
                KFImage.url(movie.backdropURL)
                    .placeholder { ImagePlaceholder(16 / 9) }
                    .resizable()
                    .cacheOriginalImage()
                    .scaleFactor(UIScreen.main.scale)
                    .fade(duration: 0.2)
                    .aspectRatio(16 / 9, contentMode: .fill)
                    .clipShape(RoundedRectangle(cornerRadius: 8))
                
                Text(movie.title)
                    .font(.subheadline)
                    .fontWeight(.medium)
                    .foregroundStyle(.textPrimary)
                    .lineLimit(1)
            }
            .frame(width: cardWidth, height: cardHeight)
        }
        .buttonStyle(.plain)
        .accessibilityIdentifier(AccessibilityID.movieCard)
        .accessibilityLabel(movie.title)
    }
}
