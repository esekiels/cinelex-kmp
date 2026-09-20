//
//  SkeletonView.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 20/09/26.
//

import SwiftUI

struct SkeletonView: View {

    let style: CarouselStyle

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            titlePlaceholder

            ScrollView(.horizontal, showsIndicators: false) {
                HStack(spacing: 12) {
                    ForEach(0..<5, id: \.self) { _ in
                        skeletonCard
                    }
                }
                .padding(.horizontal, 16)
            }
        }
        .accessibilityElement(children: .ignore)
        .accessibilityIdentifier(AccessibilityID.homeSkeleton)
    }
}

private extension SkeletonView {
    
    var titlePlaceholder: some View {
        skeletonRect(width: 150, height: 30)
            .padding(.leading, 16)
    }

    @ViewBuilder
    var skeletonCard: some View {
        switch style {
        case .poster:
            let width = UIScreen.main.bounds.width * 0.4
            skeletonRect(width: width, height: width * 3 / 2)
        case .backdrop:
            let width = UIScreen.main.bounds.width * 0.75
            skeletonRect(width: width, height: width * 9 / 16)
        }
    }

    func skeletonRect(width: CGFloat, height: CGFloat) -> some View {
        RoundedRectangle(cornerRadius: 8)
            .fill(.gray.opacity(0.3))
            .frame(width: width, height: height)
            .shimmerEffect()
    }
}

#Preview("Backdrop") {
    SkeletonView(style: .backdrop)
}

#Preview("Poster") {
    SkeletonView(style: .poster)
}
