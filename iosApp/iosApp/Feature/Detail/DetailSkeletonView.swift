//
//  DetailSkeletonView.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 27/09/26.
//

import SwiftUI

struct DetailSkeletonView: View {

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            skeleton()
                .aspectRatio(16 / 9, contentMode: .fit)

            VStack(alignment: .leading, spacing: 20) {
                VStack(alignment: .leading, spacing: 8) {
                    skeleton().frame(width: 200, height: 24)
                    skeleton().frame(width: 160, height: 14)
                }

                VStack(alignment: .leading, spacing: 6) {
                    ForEach(0..<3, id: \.self) { _ in
                        skeleton().frame(height: 14)
                    }
                    skeleton().frame(width: 200, height: 14)
                }

                skeleton().frame(width: 180, height: 14)

                HStack(spacing: 12) {
                    ForEach(0..<4, id: \.self) { _ in
                        VStack(spacing: 6) {
                            Circle()
                                .fill(Color.gray.opacity(0.3))
                                .frame(width: 56, height: 56)
                            skeleton().frame(width: 60, height: 10)
                        }
                    }
                }
            }
            .padding(16)
        }
        .frame(maxHeight: .infinity, alignment: .top)
        .shimmerEffect()
        .accessibilityElement(children: .ignore)
        .accessibilityIdentifier(AccessibilityID.detailSkeleton)
    }
}

private extension DetailSkeletonView {

    func skeleton() -> some View {
        RoundedRectangle(cornerRadius: 4)
            .fill(Color.gray.opacity(0.3))
    }
}

#Preview {
    DetailSkeletonView()
}
