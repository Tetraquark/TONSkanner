import SwiftUI
import RootFramework

// Screen of an account transactions list.
// Base component protocol: TransactionListScreen.kt
// Contains components: TransactionsHistoryFeature
//
// Android implementation (android-app): fun TransactionListScreen
// Desktop implementation (desktop-app): fun TransactionListScreen
struct TransactionListScreenView: View {
    private let screenFeature: TransactionListScreen
    
    @ObservedObject
    private var screenErrorsState: ObservableValue<TransactionListScreenScreenState>
    
    init(_ feature: TransactionListScreen) {
        screenFeature = feature
        screenErrorsState = ObservableValue(feature.screenState)
    }
    
    var body: some View {
        Text("TransactionListScreenView")

        // TODO TransactionHistoryComponentView(screenFeature.transactionsHistoryFeature)
    }
}
