//
//  ImagePlaceholder.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 20/09/26.
//

import SwiftUI

struct ImagePlaceholder: View {

    private let aspectRatio: CGFloat

    init(_ aspectRatio: CGFloat) {
        self.aspectRatio = aspectRatio
    }

    var body: some View {
        Color.gray.opacity(0.5)
            .aspectRatio(aspectRatio, contentMode: .fit)
            .cornerRadius(8)
            .shimmerEffect()
    }
}
