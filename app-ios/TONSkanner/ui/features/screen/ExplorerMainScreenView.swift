import SwiftUI
import RootFramework

// Main screen of the app.
// Base component protocol: ExplorerMainScreen.kt
// Contains components: InputAddressFeature, AddressInfoFeature
//
// Android implementation (android-app): fun ExplorerMainScreen
// Desktop implementation (desktop-app): fun ExplorerMainScreen
struct ExplorerMainScreenView: View {
    private let screenFeature: ExplorerMainScreen
    
    @ObservedObject
    private var screenErrorsState: ObservableValue<ExplorerMainScreenScreenState>
    
    init(_ feature: ExplorerMainScreen) {
        screenFeature = feature
        screenErrorsState = ObservableValue(feature.screenState)
    }
    
    var body: some View {
        Text("ExplorerMainScreenView")
        
        // TODO
        // InputAddressComponentView(screenFeature.inputAddressFeature)
        // AddressInfoComponentView(screenFeature.addressInfoFeature)
    }
}
