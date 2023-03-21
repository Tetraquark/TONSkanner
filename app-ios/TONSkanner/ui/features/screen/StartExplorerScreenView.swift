import SwiftUI
import RootFramework

// Start screen of the app.
// Base component protocol: StartExplorerScreen.kt
// Contains components: InputAddressFeature, QueryHistoryFeature.
//
// Android implementation (android-app): fun StartExplorerScreen
// Desktop implementation (desktop-app): fun StartExplorerScreen
struct StartExplorerScreenView: View {
    private let screenFeature: StartExplorerScreen
    
    @ObservedObject
    private var screenErrorsState: ObservableValue<StartExplorerScreenScreenState>
    
    init(_ feature: StartExplorerScreen) {
        screenFeature = feature
        screenErrorsState = ObservableValue(feature.screenState)
    }
    
    var body: some View {
        Text("StartExplorerScreenView")
        
        // TODO
        // InputAddressComponentView(screenFeature.inputAddressFeature)
        // QueryHistoryComponentView(screenFeature.queryHistoryFeature)
    }
}

//struct StartExplorerScreenView_Previews: PreviewProvider {
//    static var previews: some View {
//        StartExplorerScreenView()
//    }
//}
