package kr.esei.library.model

/**
 * ViewModel에서 다른 Activity를 실행하며 데이터를 전달하기 위한 이벤트 모델
 * 내부에 HashMap을 가지고 있으며, put(key, value)를 통해 데이터를 담을 수 있다.
 * @param activityClass 실행할 Activity의 Class
 */
public class ActivityBundleEventModel(public val activityClass: Class<*>, public val launcherKey: String? = null) {

    private val _bundle = EventDataBundle()
    public val bundle: EventDataBundle get() = _bundle

    //region put data
    public fun put(key: String, value: String) {
        _bundle[key] = value
    }

    public fun put(key: String, value: Int) {
        _bundle[key] = value as Any
    }

    public fun put(key: String, value: Boolean) {
        _bundle[key] = value as Any
    }

    public fun put(key: String, value: Long) {
        _bundle[key] = value as Any
    }

    public fun put(key: String, value: Double) {
        _bundle[key] = value as Any
    }

    public fun put(key: String, value: Float) {
        _bundle[key] = value as Any
    }

    public fun put(key: String, value: Any) {
        _bundle[key] = value
    }
    //endregion
}
