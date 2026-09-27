//
//  DetailView.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 27/09/26.
//

import SwiftUI
import Shared

struct DetailView: View {

    let movie: Movie

    var body: some View {
        Text("detail.placeholder")
            .foregroundStyle(.textPrimary)
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .background(Color.background)
            .navigationTitle(movie.title)
            .navigationBarTitleDisplayMode(.inline)
    }
}

#if DEBUG
#Preview {
    NavigationStack {
        DetailView(movie: MovieStubs.shared.all[0])
    }
}
#endif
