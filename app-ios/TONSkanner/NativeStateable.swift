import RootFramework

public class NativeStateable<T : AnyObject, E : AnyObject> {}

class NativeStateableSuccess<T : AnyObject, E : AnyObject> : NativeStateable<T, E> {
    let data: T
    
    init(data: T) {
        self.data = data
    }
}

class NativeStateableFailure<T : AnyObject, E : AnyObject> : NativeStateable<T, E> {
    let failure: E
    
    init(failure: E) {
        self.failure = failure
    }
}

class NativeStateableLoading<T : AnyObject, E : AnyObject> : NativeStateable<T, E> {}

func mapStateable<T : AnyObject, E : AnyObject>(stateable: Stateable) -> NativeStateable<T, E> {
    switch stateable {
    case is StateableSuccess<T>:
        return NativeStateableSuccess<T, E>(data: (stateable as! StateableSuccess<T>).data!)
    case is StateableFailure<E>:
        return NativeStateableFailure<T, E>(failure: (stateable as! StateableFailure<E>).failure!)
    case is StateableLoading:
        return NativeStateableLoading<T, E>()
    default:
        fatalError("Stateable cast error")
    }
}
