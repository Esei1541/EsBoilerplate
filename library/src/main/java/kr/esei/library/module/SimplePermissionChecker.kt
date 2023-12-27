package kr.esei.library.module

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kr.esei.library.constant.activityResultLauncher

/**
 * 기본적인 권한 체크 flow를 간편하게 사용할 수 있도록 정의한 유틸리티 클래스
 * 커스텀 하지 않은 기본적인 상태의 flow는 다음과 같다.
 * 1. 생성자로 받은 권한 체크
 * 2. 권한이 승인되었을 경우 onAllPermissionsGranted() callback 후 종료 / 권한 중 하나 이상이 거부되었을 경우 onDenied()를 통해 다이얼로그 출력
 * 3. 다이얼로그에서 확인을 누르면 설정 화면으로 이동, 설정 화면에서 돌아올 경우 1번으로 돌아가 체크 flow 실행
 * 4. 다이얼로그에서 취소를 눌렀을 경우 onDialogNegative() callback 후 종료
 *
 * 해당 Class는 registerForActivityResult를 사용하기 때문에 반드시 Activity의 onCreate()에서 초기화해야 한다.
 *
 * @param parentActivity (필수) 권한 체크를 실행할 부모 activity
 * @param onAllPermissionsGranted (선택) 권한이 모두 승인되었을 경우 실행할 callback
 * @param onDenied (선택) 권한이 하나 이상 거부되었을 경우 실행할 callback / 기본적으로 showDialog()를 실행하도록 되어 있음
 * @param showDialog (선택) 권한 거부 시 설정창으로 이동을 유도하는 dialog를 출력 / 기본값은 AlertDialog로 설정되어있지만, function을 통해 커스텀 가능
 * @param onDialogPositive (선택) dialog에서 확인을 눌렀을 경우 실행할 callback / 기본적으로 설정 화면으로 이동하도록 되어 있음
 * @param onDialogNegative (필수) 설정 이동 다이얼로그에서 취소를 눌렀을 경우 실행할 callback
 * @param permissions (필수) 권한 체크를 실행할 권한 목록
 */
@Deprecated("This class is deprecated.")
public class SimplePermissionChecker(
    private val parentActivity: AppCompatActivity,
    private var onAllPermissionsGranted: (() -> Unit)? = null,
    private var onDenied: (() -> Unit)? = null,
    private var showDialog: (() -> Unit)? = null,
    private var onDialogPositive: (() -> Unit)? = null,
    private var onDialogNegative: () -> Unit,
    private var permissions: Array<String>
) {

    private val TAG = javaClass.simpleName
    private var _dialogTitle = "앱 권한 거부됨"
    private var _dialogMessage = "해당기능을 실행하기 위해선 권한이 필요합니다.\n설정으로 이동하시겠습니까?"
    private val configLauncher = parentActivity.activityResultLauncher { _ ->
        onDialogPositive?.invoke()
    }

    public val dialogTitle: String
        get() = _dialogTitle

    public val dialogMessage: String
        get() = _dialogMessage

    init {
        initDefaultFunctions()
    }

    /**
     * 선택 항목이 생성자에서 전달되지 않았을 경우 기본 동작으로 초기화한다.
     */
    private fun initDefaultFunctions() {
        if (onAllPermissionsGranted == null) {
            onAllPermissionsGranted = {}
        }
        if (showDialog == null) {
            showDialog = { showPermissionAlertDialog() }
        }
        if (onDenied == null) {
            onDenied = { showDialog?.invoke() }
        }
        if (onDialogPositive == null) {
            onDialogPositive = { check() }
        }
    }

    /**
     * 특정 권한을 체크하고 결과에 따른 callback을 실행
     */
    public fun check() {
        val permissionList = permissions.toList()

        Dexter.withContext(parentActivity)
            .withPermissions(permissionList)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report == null) return

                    if (report.areAllPermissionsGranted()) {
                        onAllPermissionsGranted?.invoke()
                    } else {
                        onDenied?.invoke()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: MutableList<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    /**
     * 설정 화면으로 이동을 유도하는 기본 AlertDialog를 정의한다.
     */
    private fun showPermissionAlertDialog() {
        AlertDialog.Builder(parentActivity)
            .setTitle(_dialogTitle)
            .setMessage(_dialogMessage)
            .setPositiveButton("확인") { dialog, _ ->
                Log.e(TAG, "showPermissionAlertDialog: on Submit")
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + parentActivity.packageName)
                )
                configLauncher.launch(intent)

                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                onDialogNegative.invoke()
                dialog.dismiss()
            }
            .show()
    }

    //region Setter
    public fun setDefaultDialogTitle(title: String) {
        this._dialogTitle = title
    }

    public fun setDefaultDialogMessage(message: String) {
        this._dialogMessage = message
    }

    public fun setOnAllPermissionsGrantedListener(listener: () -> Unit) {
        this.onAllPermissionsGranted = listener
    }

    public fun setOnDeniedListener(listener: () -> Unit) {
        this.onDenied = listener
    }

    public fun setOnShowDialogListener(listener: () -> Unit) {
        this.showDialog = listener
    }

    public fun setPermissions(permissions: Array<String>) {
        this.permissions = permissions
    }
    //endregion
}