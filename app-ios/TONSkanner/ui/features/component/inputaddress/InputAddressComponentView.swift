import SwiftUI
import RootFramework

// View of text field for address input.
// Base component protocol: InputAddressFeature.kt
//
// Android + Desktop implementation (common/compose-ui): fun InputAddressView
struct InputAddressComponentView: View {
    private let feature: InputAddressFeature

    @ObservedObject
    var isExplorerLoading: ObservableValue<KotlinBoolean>
    @ObservedObject
    var fieldValue: ObservableValue<NSString>
    @ObservedObject
    var fieldError: ObservableValue<OptionalValue<StringDesc>>
    @ObservedObject
    var isFieldEnabled: ObservableValue<KotlinBoolean>

    init(_ feature: InputAddressFeature) {
        self.feature = feature
        isExplorerLoading = ObservableValue(feature.isExplorerLoading)
        fieldValue = ObservableValue(feature.addressField.valueValue)
        fieldError = ObservableValue(feature.addressField.failureValue)
        isFieldEnabled = ObservableValue(feature.addressField.isEnabledValue)
    }
    
    var body: some View {
        // TODO
        Text("InputAddressComponentView")
    }
}
