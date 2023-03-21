import SwiftUI
import RootFramework

// View with paginated list of a last account transactions.
// Base component protocol: TransactionsHistoryFeature.kt
//
// Android + Desktop implementation (common/compose-ui): fun TransactionsHistory
struct TransactionHistoryComponentView: View {
    private let feature: TransactionsHistoryFeature
    
    @ObservedObject
    private var viewState: ObservableMappedValue<Stateable, NativeStateable<TransactionsHistoryFeatureModel, ErrorDesc>>
    
    init(_ feature: TransactionsHistoryFeature) {
        self.feature = feature
        viewState = ObservableMappedValue<Stateable, NativeStateable<TransactionsHistoryFeatureModel, ErrorDesc>>(
            feature.state,
            mapper: { value in
                mapStateable(stateable: value)
            }
        )
    }
    
    var body: some View {
        // TODO
        Text("TransactionsHistoryFeature")
    }
}
