package kr.esei.library.util

import androidx.lifecycle.Observer

/**
 * Event Wrapper를 Observe하기 위한 Observer Class
 * Event Class의 getContentIfNotHandled()를 사용하여 기존에 단일 이벤트가 실행되지 않은 경우에만 이벤트를 처리한다.
 */
public class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(value: Event<T>) {
        value.getContentIfNotHandled()?.let { data ->
            onEventUnhandledContent(data)
        }
    }
}