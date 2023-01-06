package kr.esei.library.module

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kr.esei.library.R
import kr.esei.library.view.ProgressDialog

/**
 * Application 클래스에 사용할 수 있는 기본적인 기능을 미리 정의해둔 클래스
 * @param preferenceMasterKey preference 암호화를 위한 마스터 키
 * @param preferenceName preference 파일명. 지정하지 않을 경우 packageName이 들어간다.
 * @param progressTintColor 로딩 다이얼로그의 색상
 */
public class BaseApplication(
    private val preferenceMasterKey: String,
    private val preferenceName: String? = null,
    private val progressTintColor: Int = R.color.esboiler_primary
): Application() {

    protected val TAG: String = javaClass.simpleName

    private var progressDialog: ProgressDialog? = null
    private var preference: PreferenceManager? = null

    /**
     * preferenceManager 객체를 가져온다.
     * 객체가 존재하지 않을 경우 초기화
     */
    public fun getPreferenceManager(): PreferenceManager {
        if (preference == null) {
            preference = PreferenceManager(preferenceMasterKey, preferenceName)
            preference?.init(this)
        }
        return preference!!
    }

    /**
     * 로딩 다이얼로그를 보여주고 유저의 조작을 차단한다.
     * 작업이 끝나면 반드시 dismissProgressDialog()를 호출할 것.
     * @param fragmentManager activity의 FragmentManager를 전달
     */
    public fun showProgressDialog(fragmentManager: FragmentManager) {
        try {
            progressDialog?.dismiss()
            progressDialog = null

            progressDialog = ProgressDialog(
                ContextCompat.getColor(this, R.color.esboiler_white),
                true,
                progressTintColor
            )

            progressDialog?.apply {
                setStyle(DialogFragment.STYLE_NORMAL, R.style.esboiler_fullscreen_dialog)
                // onSaveInstanceState 이후 show() 내부에서 commit() 호출 시 IllegalStateException이 발생하므로...
                fragmentManager.beginTransaction().add(this, "").commitAllowingStateLoss()
//                show(fragmentManager, tag)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "showProgressDialog: Exception occurred on showProgressDialog()")
        }
    }

    /**
     * 로딩 다이얼로그를 보여주고 유저의 조작을 차단한다.
     * 작업이 끝나면 반드시 dismissProgressDialog()를 호출할 것.
     * @param fragmentManager activity의 FragmentManager를 전달
     * @param statusBarColor 다이얼로그 출력 시 상단 상태바의 색상
     * @param isLightStatusBar 다이얼로그 출력 시 상단 상태바의 폰트를 어두운 색상으로 표시할지 여부
     */
    public fun showProgressDialog(fragmentManager: FragmentManager, statusBarColor: Int, isLightStatusBar: Boolean) {
        try {
            progressDialog?.dismiss()
            progressDialog = null

            progressDialog = ProgressDialog(
                ContextCompat.getColor(this, statusBarColor),
                isLightStatusBar,
                progressTintColor
            )

            progressDialog?.apply {
                setStyle(DialogFragment.STYLE_NORMAL, R.style.esboiler_fullscreen_dialog)
                // onSaveInstanceState 이후 show() 내부에서 commit() 호출 시 IllegalStateException이 발생하므로...
                fragmentManager.beginTransaction().add(this, "").commitAllowingStateLoss()
//                show(fragmentManager, tag)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "showProgressDialog: Exception occurred on showProgressDialog()")
        }
    }

    /**
     * 로딩 다이얼로그를 닫는다.
     */
    public fun dismissProgressDialog() {
        try {
            progressDialog?.dismissAllowingStateLoss()
            progressDialog = null
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "dismissProgressDialog: Exception occurred on hideProgressDialog()")
        }
    }

}