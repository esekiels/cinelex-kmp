//
//  Shimmer.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 20/09/26.
//

import SwiftUI

struct Shimmer: ViewModifier {

    @State private var startPoint: UnitPoint = .init(x: -1.8, y: -1.2)
    @State private var endPoint: UnitPoint = .init(x: 0, y: -0.2)

    private static let animationDuration: Double = 1.0
    private static let gradientColors: [Color] = [
        Color.textSecondary.opacity(0.15),
        Color.textSecondary.opacity(0.35),
        Color.textSecondary.opacity(0.15)
    ]

    func body(content: Content) -> some View {
        content
            .redacted(reason: .placeholder)
            .overlay(shimmerGradient(content: content))
            .onAppear {
                startAnimation()
            }
    }

    private func shimmerGradient(content: Content) -> some View {
        LinearGradient(
            colors: Self.gradientColors,
            startPoint: startPoint,
            endPoint: endPoint
        )
        .mask(content)
    }

    private func startAnimation() {
        guard !ProcessInfo.processInfo.arguments.contains("-UITestStubs") else {
            return
        }

        withAnimation(.easeInOut(duration: Self.animationDuration).repeatForever(autoreverses: false)) {
            startPoint = .init(x: 1, y: 1)
            endPoint = .init(x: 2.2, y: 2.2)
        }
    }
}

#Preview {
    RoundedRectangle(cornerRadius: 8)
        .fill(.gray.opacity(0.3))
        .frame(width: 250, height: 250)
        .shimmerEffect()
}
