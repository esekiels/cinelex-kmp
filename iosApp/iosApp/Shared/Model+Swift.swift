//
//  Model+Swift.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 20/09/26.
//

import Foundation
import Shared

extension Movie: @retroactive Identifiable {}
extension Video: @retroactive Identifiable {}

extension Movie {
    var posterURL: URL? { stubbableURL(posterUrl) }
    var backdropURL: URL? { stubbableURL(backdropUrl) }
}

extension MovieDetails {
    var backdropURL: URL? { stubbableURL(backdropUrl) }
}

extension Cast {
    var profileURL: URL? { stubbableURL(profileUrl) }
}

extension Video {
    var youtubeURL: URL? { youtubeUrl.flatMap(URL.init(string:)) }
}

#if DEBUG
private let isStubbed = UITestScenario.current != nil
#endif

private func stubbableURL(_ path: String?) -> URL? {
    #if DEBUG
    if isStubbed {
        return nil
    }
    #endif
    return path.flatMap(URL.init(string:))
}
