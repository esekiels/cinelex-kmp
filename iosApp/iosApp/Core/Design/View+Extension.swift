//
//  View+Extension.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 20/09/26.
//

import SwiftUI

extension View {

    func shimmerEffect() -> some View {
        modifier(Shimmer())
    }
}
