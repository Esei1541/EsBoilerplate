package kr.esei.library.constant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.esei.library.util.Event

public typealias ActivityInflater<T> = (LayoutInflater) -> T
public typealias FragmentInflater<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

// Event Wrapper 사용 시 가독성을 위한 Type Alias
public typealias MutableLiveEvent<T> = MutableLiveData<Event<T>>
public typealias LiveEvent<T> = LiveData<Event<T>>
