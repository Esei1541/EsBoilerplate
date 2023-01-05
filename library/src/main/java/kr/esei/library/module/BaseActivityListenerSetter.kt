package kr.esei.library.module

/**
 * BaseActivity 및 BaseActivityForViewBinding에서 사용할 Listener를 외부에서 설정하기 위한 interface
 */
internal interface BaseActivityListenerSetter {

    // Activity의 onRequestPermissionsResult에서 실행시킬 listener를 설정한다
    fun setOnRequestPermissionsResultListener(listener: (requestCode: Int, permissions: Array<out String>, grantResults: IntArray) -> Unit)

}