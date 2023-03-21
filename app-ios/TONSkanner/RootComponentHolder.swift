import Foundation
import RootFramework

class RootHolder : ObservableObject {
    let lifecycle: LifecycleRegistry
    let root: RootComponent

    init() {
        lifecycle = LifecycleRegistryKt.LifecycleRegistry()

        root = RootComponent(
            componentContext: DefaultComponentContext(lifecycle: lifecycle),
            settings: NSUserDefaultsSettings(delegate: UserDefaults.standard),
            tonConfig: TonConfigUrl(url: "https://ton.org/global-config.json"),
            appConfig: AppConfig(tonDnsRootCollectionAddress: "EQC3dNlesgVD8YbAazcauIrXBPfiVhMMr5YYk2in0Mtsz0Bz"),
            deepLink: RootComponentDeepLinkNone()
        )

        lifecycle.onCreate()
    }

    deinit {
        lifecycle.onDestroy()
    }
}
