import SwiftUI
import Shared

struct ContentView: View {
    private let platformName = CinelexSDK.shared.platformName

    var body: some View {
        VStack(spacing: 12) {
            Image(systemName: "film")
                .font(.system(size: 64))
                .foregroundStyle(.tint)
            Text("SwiftUI is talking to:")
            Text(platformName).bold()
            Text("shared v\(CinelexSDK.shared.VERSION)")
                .font(.footnote)
                .foregroundStyle(.secondary)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .padding()
    }
}

#Preview {
    ContentView()
}
