import SwiftUI
import RootFramework

struct RootComponentView: View {
    private let rootComponent: RootComponent
    
    @ObservedObject
    private var childStack: ObservableValue<ChildStack<AnyObject, RootFeatureChild>>
    private var activeChild: RootFeatureChild { childStack.value.active.instance }
    
    init(_ rootComponent: RootComponent) {
        self.rootComponent = rootComponent
        childStack = ObservableValue(rootComponent.childStack)
    }
    
    var body: some View {
        VStack {
            ChildView(child: activeChild)
                .frame(maxHeight: .infinity)
        }
    }
}

private struct ChildView: View {
    let child: RootFeatureChild

    var body: some View {
        switch child {
        case let child as RootFeatureChild.StartExplorer: StartExplorerScreenView(child.screen)
        case let child as RootFeatureChild.ExplorerMain: ExplorerMainScreenView(child.screen)
        case let child as RootFeatureChild.TransactionList: TransactionListScreenView(child.screen)
        default: EmptyView()
        }
    }
}

//struct RootComponentView_Previews: PreviewProvider {
//    static var previews: some View {
//        RootComponentView()
//    }
//}
