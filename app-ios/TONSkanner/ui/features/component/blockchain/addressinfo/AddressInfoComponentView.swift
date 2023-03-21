import SwiftUI
import RootFramework

// View of block with info about account address.
// Base component protocol: AddressInfoFeature.kt
//
// Android + Desktop implementation (common/compose-ui): fun AddressInfoView
struct AddressInfoComponentView: View {
    private let feature: AddressInfoFeature
    
    @ObservedObject
    private var viewState: ObservableMappedValue<Stateable, NativeStateable<AddressInfoFeatureModel, ErrorDesc>>
    
    init(_ feature: AddressInfoFeature) {
        self.feature = feature
        viewState = ObservableMappedValue<Stateable, NativeStateable<AddressInfoFeatureModel, ErrorDesc>>(
            feature.state,
            mapper: { value in
                mapStateable(stateable: value)
            }
        )
    }
    
    var body: some View {
        // TODO
        Text("AddressInfoFeature")
    }
}
