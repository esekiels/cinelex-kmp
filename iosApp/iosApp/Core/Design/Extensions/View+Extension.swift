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
    
    func zoomDestination<Value: Hashable, Destination: View>(
        for type: Value.Type,
        in namespace: Namespace.ID,
        @ViewBuilder destination: @escaping (Value) -> Destination
    ) -> some View {
        navigationDestination(for: ZoomRoute<Value>.self) { route in
            destination(route.value)
                .navigationTransition(.zoom(sourceID: route, in: namespace))
        }
        .environment(\.zoomNamespace, namespace)
    }
}
