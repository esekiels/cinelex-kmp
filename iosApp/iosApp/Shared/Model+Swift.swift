//
//  Model+Swift.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 20/09/26.
//

import Foundation
import Shared

extension Movie: @retroactive Identifiable {}

extension Movie {
    var posterURL: URL? { imageURL(posterUrl) }
    var backdropURL: URL? { imageURL(backdropUrl) }

    private func imageURL(_ path: String?) -> URL? {
        #if DEBUG
        if UITestScenario.current != nil {
            return nil
        }
        #endif
        return path.flatMap(URL.init(string:))
    }
}
