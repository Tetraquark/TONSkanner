import SwiftUI
import RootFramework

// View of block with a latest user requests.
// Base component protocol: QueryHistoryFeature.kt
//
// Android + Desktop implementation (common/compose-ui): fun QueryHistoryView
struct QueryHistoryComponentView: View {
    private let feature: QueryHistoryFeature
    
    init(_ feature: QueryHistoryFeature) {
        self.feature = feature
        // feature.addressesHistory - history list with String
    }
    
    var body: some View {
        // TODO
        Text("QueryHistoryComponentView")
    }
}
