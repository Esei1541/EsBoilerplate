package kr.esei.library.module

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * ViewModel에서 View Class의 기능에 접근하고 익명으로 데이터 송수신을 하기 위한 기능을 정의하는 interface
 * ViewModel과 View와의 의존성을 방지하기 위해 해당 interface의 구현체를 통해서만 View의 기능에 접근해야 한다.
 * View Class에 상속할 것.
 */
public interface ActivityNavigator {

    public val context: Context

    public val activity: AppCompatActivity

    public fun onBackPressed()

    public fun clearFocus()

    public fun finish()

    public fun finishAffinity()

    public fun toast(res: Int)

    public fun toast(string: String)

    public fun startActivity(intent: Intent)

    public fun startActivity(intent: Intent?, options: Bundle?)

}