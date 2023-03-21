import SwiftUI
import RootFramework

public class ObservableMappedValue<T : AnyObject, M : AnyObject> : ObservableObject {
    private let observableValue: Value<T>

    @Published
    var value: M

    private var observer: ((T) -> Void)?
    private var mapper: ((T) -> M)?
    
    init(_ value: Value<T>, mapper: @escaping (T) -> M) {
        observableValue = value
        self.mapper = mapper
        self.value = mapper(observableValue.value)
        observer = { [weak self] value in self?.value = mapper(value) }
        observableValue.subscribe(observer: observer!)
    }

    deinit {
        observableValue.unsubscribe(observer: self.observer!)
    }
}
