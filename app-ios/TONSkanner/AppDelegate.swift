import Foundation
import SwiftUI
import RootFramework

class AppDelegate: NSObject, UIApplicationDelegate {
    private var rootHolder: RootHolder?

    func application(_ application: UIApplication, shouldRestoreSecureApplicationState coder: NSCoder) -> Bool {
        rootHolder = getRootHolder()
        return true
    }
    
    func getRootHolder() -> RootHolder {
        if (rootHolder == nil) {
            rootHolder = RootHolder()
        }
        
        return rootHolder!
    }
}
