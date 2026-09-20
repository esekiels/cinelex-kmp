//
//  KotlinError.swift
//  iosApp
//
//  Created by Esekiel Surbakti on 20/09/26.
//

import Foundation
import Shared

extension Error {
    
    /// A kotlin exception doesn't conform to Swift's Error
    /// A throwing `suspend` function arrives as an `NSError`
    /// it's carrying the real throwable in `userInfo`
    /// `catch let e as CinelexException` never matches
    var cinelexException: CinelexException? {
        (self as NSError).userInfo["KotlinException"] as? CinelexException
    }
}
