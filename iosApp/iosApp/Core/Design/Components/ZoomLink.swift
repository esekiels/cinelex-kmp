//
//  ZoomLink.swift
//  iosApp
//

import SwiftUI

struct ZoomRoute<Value: Hashable>: Hashable {
    let value: Value
    let source: String
}

extension EnvironmentValues {
    @Entry var zoomNamespace: Namespace.ID?
}

struct ZoomLink<Value: Hashable, Label: View>: View {

    @Environment(\.zoomNamespace) private var namespace
    private let route: ZoomRoute<Value>
    private let label: Label

    init(_ value: Value, source: String, @ViewBuilder label: () -> Label) {
        self.route = ZoomRoute(value: value, source: source)
        self.label = label()
    }

    var body: some View {
        NavigationLink(value: route) { label }
            .buttonStyle(.plain)
            .modifier(TransitionSource(id: route, namespace: namespace))
    }
}

private struct TransitionSource<ID: Hashable>: ViewModifier {
    let id: ID
    let namespace: Namespace.ID?

    func body(content: Content) -> some View {
        if let namespace {
            content.matchedTransitionSource(id: id, in: namespace)
        } else {
            content
        }
    }
}
