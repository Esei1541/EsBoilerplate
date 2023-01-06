package kr.esei.library.module

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * SharedPreference를 관리하는 class
 * 암호화된 SharedPreference를 사용한다.
 * @param masterKey 암호화에 사용할 masterKey
 * @param fileName 디바이스에 저장될 SharedPreference 파일명. 입력하지 않을 시 packagename.preference로 저장된다.
 */
public class PreferenceManager(
    private val masterKey: String,
    private val fileName: String? = null
) {

    private companion object {
        private const val TAG = "PreferenceManager"
    }

    private lateinit var mFileName: String
    private lateinit var mContext: Context
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    /**
     * 필요한 값을 초기화한다
     * Application Class에서 호출할 것
     */
    public fun init(context: Context) {
        mContext = context
        mFileName = fileName ?: "${mContext.packageName}.preference"

        // SharedPreference 암호화를 위한 masterKey를 생성한다
        val masterKey = MasterKey.Builder(mContext, masterKey)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        preferences = EncryptedSharedPreferences.create(
            context,
            mFileName,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        editor = preferences.edit()
    }

    //region 범용 public function

    /**
     * SharedPreference에 String 값을 저장한다
     */
    public fun put(key: String, value: String) {
        Log.i(TAG, "Insert String Data -> key: $key, value: $value")
        editor.putString(key, value).commit()
    }

    /**
     * SharedPreference에 Int 값을 저장한다
     */
    public fun put(key: String, value: Int) {
        Log.i(TAG, "Insert Int Data -> key: $key, value: $value")
        editor.putInt(key, value).commit()
    }

    /**
     * SharedPreference에 Boolean 값을 저장한다
     */
    public fun put(key: String, value: Boolean) {
        Log.i(TAG, "Insert Boolean Data -> key: $key, value: $value")
        editor.putBoolean(key, value).commit()
    }

    /**
     * SharedPreference에 Long 값을 저장한다
     */
    public fun put(key: String, value: Long) {
        Log.i(TAG, "Insert Long Data -> key: $key, value: $value")
        editor.putLong(key, value).commit()
    }

    /**
     * SharedPreference에 Float 값을 저장한다
     */
    public fun put(key: String, value: Float) {
        Log.i(TAG, "Insert Float Data -> key: $key, value: $value")
        editor.putFloat(key, value).commit()
    }

    /**
     * SharedPreference에 저장된 String 값을 가져온다.
     * 값이 없을 경우 빈 문자열을 return
     */
    public fun getString(key: String): String {
        return preferences.getString(key, null) ?: ""
    }

    /**
     * SharedPreference에 저장된 Int 값을 가져온다.
     * 값이 없을 경우 -1을 return
     */
    public fun getInt(key: String): Int {
        return preferences.getInt(key, -1)
    }

    /**
     * SharedPreference에 저장된 Boolean 값을 가져온다.
     * 값이 없을 경우 false를 return
     */
    public fun getBoolean(key: String): Boolean {
        return preferences.getBoolean(key, false)
    }

    /**
     * SharedPreference에 저장된 Long 값을 가져온다.
     * 값이 없을 경우 -1을 return
     */
    public fun getLong(key: String): Long {
        return preferences.getLong(key, -1)
    }

    /**
     * SharedPreference에 저장된 Float 값을 가져온다.
     * 값이 없을 경우 -1을 return
     */
    public fun getFloat(key: String): Float {
        return preferences.getFloat(key, -1f)
    }

    /**
     * SharedPreference에 저장된 값을 삭제한다
     */
    public fun delete(key: String) {
        Log.i(TAG, "Delete data -> key: $key")
        editor.remove(key).commit()
    }

    /**
     * SharedPreference에 저장된 모든 값을 삭제한다
     */
    public fun clear() {
        Log.i(TAG, "Clear all data")
        editor.clear().commit()
    }
    //endregion
}