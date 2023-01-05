package kr.esei.library.module

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel

/**
 * ViewModel의 공통 사항을 정의한다.
 * ViewModel Class 대신 이 클래스를 상속할 것.
 */
public abstract class BaseViewModel: ViewModel() {

    protected open val TAG: String = javaClass.simpleName

    protected abstract val navigator: ActivityNavigator

    protected fun toast(res: Int) { navigator.toast(res) }

    protected fun toast(string: String) { navigator.toast(string) }

    protected fun startActivity(intent: Intent) { navigator.startActivity(intent) }

}