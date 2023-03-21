import SwiftUI
import RootFramework

@main
struct TONSkannerApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self)
    var appDelegate
    
    private var rootHolder: RootHolder { appDelegate.getRootHolder() }
    
    var body: some Scene {
        WindowGroup {
            RootComponentView(rootHolder.root)
                .onAppear { LifecycleRegistryExtKt.resume(self.rootHolder.lifecycle) }
                .onDisappear { LifecycleRegistryExtKt.stop(self.rootHolder.lifecycle) }
        }
    }
}
