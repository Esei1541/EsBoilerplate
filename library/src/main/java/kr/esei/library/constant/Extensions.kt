package kr.esei.library.constant

import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.regex.Pattern
import kotlin.math.roundToInt

/**
 * 유틸성 확장 함수를 정의한다.
 */

/**
 * Show toast message by String resource
 */
public fun Context.toast(@StringRes res: Int) {
    Toast.makeText(this, getString(res), Toast.LENGTH_SHORT).show()
}

/**
 * Show toast message by String
 */
public fun Context.toast(string: String) {
    Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
}

/**
 * Dp를 Px 단위로 변경
 */
public fun Context.dpToPx(dp: Int): Int {
    val destiny = resources.displayMetrics.density

    return (dp * destiny).roundToInt()
}

/**
 * Set status bar color
 */
public fun AppCompatActivity.setStatusBarColor(color: Int, lightMode: Boolean) {
    val window = window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = color

    val controllerCompat = WindowInsetsControllerCompat(window, window.decorView)
    controllerCompat.isAppearanceLightStatusBars = lightMode
}


/**
 * status bar의 높이를 가져온다
 */
public fun AppCompatActivity.getStatusBarHeight(): Int {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else {
        0
    }
}

/**
 * navigation bar의 높이를 가져온다
 */
public fun AppCompatActivity.getNavigationBarHeight(): Int {
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else {
        0
    }
}

/**
 * 현재 디바이스의 navigation bar가 소프트 키라면 true를 반환, 물리 키라면 false를 반환한다.
 */
public fun AppCompatActivity.isSoftNavigationBar(): Boolean {
    val resourceId = resources.getIdentifier("config_showNavigationBar", "bool", "android")
    return if (resourceId > 0) {
        resources.getBoolean(resourceId)
    } else {
        false
    }
}

/**
 * 디바이스 스크린의 최대 넓이를 구한다
 */
@Suppress("DEPRECATION")
public fun Context.getScreenMaxWidth(): Int{
    val result: Int
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowInsets = windowManager.currentWindowMetrics.windowInsets
        val insets = windowInsets.getInsetsIgnoringVisibility(
            WindowInsets.Type.navigationBars() or WindowInsets.Type.displayCutout()
        )
        val insetWidth = insets.right + insets.left
        val bound = windowManager.currentWindowMetrics.bounds

        result = bound.width() - insetWidth
    } else {
        val size = Point()
        val display = windowManager.defaultDisplay
        display.getSize(size)

        result = size.x
    }

    return result
}

/**
 * Status Bar를 투명화하기 위한 Flag를 추가한다.
 */
public fun AppCompatActivity.addTransparentStatusBarFlag() {
    val window = window
    window.apply {
        setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
}

/**
 * 상단 스테이터스 바 영역을 제거한다.
 */
@Suppress("DEPRECATION")
public fun AppCompatActivity.removeStatusBar(rootLayout: ViewGroup) {
    val window = window
    window.apply {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            // 동작 확인 필요
            decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                    )
        } else {
            WindowCompat.setDecorFitsSystemWindows(this, false)

            WindowInsetsControllerCompat(window, window.decorView).apply {
                hide(WindowInsetsCompat.Type.statusBars())
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }

            ViewCompat.setOnApplyWindowInsetsListener(
                rootLayout
            ) { view: View, windowInsets: WindowInsetsCompat ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
                view.layoutParams = (view.layoutParams as FrameLayout.LayoutParams).apply {
                    bottomMargin = insets.bottom
                }
                WindowInsetsCompat.CONSUMED
            }
        }
    }

}

/**
 * 레이아웃을 컷아웃 영역으로 배치
 */
public fun AppCompatActivity.setLayoutInDisplayCutout() {
    val window = window
    window.apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
        }
    }
}


/**
 * 특정 Activity를 실행시키고, 해당 Activity가 종료되었을 때 callback을 받는다.
 * Deprecated된 startActivityForResult 대용
 */
public fun AppCompatActivity.activityResultLauncher(onResultActivity: (ActivityResult) -> Unit): ActivityResultLauncher<Intent> {
    return registerForActivityResult(ActivityResultContracts.StartActivityForResult(), onResultActivity)
}

/**
 * Uri에서 실제 파일 경로를 가져온다.
 */
public fun Uri.getRealPath(context: Context): String? {
    return try {
        val cursor = context.contentResolver.query(this, null, null, null, null)
        var result: String? = path
        cursor?.let {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex(MediaStore.Images.Media.DATA)
                result = it.getString(index)
            }
        }
        cursor?.close()
        result
    } catch (e: Exception) {
        null
    }
}

/**
 * MilliSecond to LocalDate
 */
public fun Long.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
}

/**
 * MilliSecond to LocalDateTime
 */
public fun Long.toLocalDateTime(): LocalDateTime {
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDateTime()
}

/**
 * LocalDate to MilliSecond
 */
public fun LocalDate.toMilliSecond(): Long {
    val time = this.atStartOfDay(ZoneId.systemDefault())

    return time.toInstant().toEpochMilli()
}

/**
 * LocalDateTime to MilliSecond
 */
public fun LocalDateTime.toMilliSecond(): Long {
    val time = this.atZone(ZoneId.systemDefault())

    return time.toInstant().toEpochMilli()
}

/**
 * Email 양식 체크
 */
public fun String.emailValidation(): Boolean {
    val regex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    return Pattern.matches(regex, this.trim())
}

/**
 * String의 공백문자를 줄바꿈되지 않는 특수문자로 변경
 */
public fun String.toNoBreakString(): String {
    return this.replace(" ", "\u00A0")
}

/**
 * TypeToken 객체를 통한 Gson의 fromJson 호출
 * List 등 TypeToken을 정의하지 않으면 Exception이 발생하는 타입을 변환할 때 사용하면 됨
 */
public inline fun <reified T> Gson.fromJson(json: String): T = fromJson<T>(json, object : TypeToken<T>() {}.type)
