package kr.esei.library.util

/**
 * MVVM 환경에서, ViewModel과 View 사이의 이벤트를 처리하기 위한 클래스
 * 단일 이벤트를 한 번만 처리하여, Lifecycle에 의한 중복 실행 등을 방지한다.
 * 해당 이벤트를 Observing하기 위해서는 EventObserver Class를 사용해야 한다.
 */
public open class Event<out T>(private val content: T) {

    // 단일 이벤트가 이미 처리되었는지를 판단한다.
    private var hasBeenHandled = false
    public val isHandled: Boolean get() = hasBeenHandled

    /**
     * 이벤트의 처리 여부에 따라 값을 반환한다.
     * 이미 처리된 이벤트라면 null을, 그렇지 않다면 이벤트의 값을 반환하고 hasBeenHandled를 true로 바꿔 이벤트를 처리했음을 표시한다.
     */
    public fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * 이벤트의 처리 여부와 상관 없이 값을 반환한다.
     */
    public fun peekContent(): T = content
}