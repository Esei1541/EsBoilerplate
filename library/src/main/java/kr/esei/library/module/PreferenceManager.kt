package kr.esei.library.module

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import java.io.IOException

/**
 * Class for managing SharedPreference.
 * Uses EncryptedSharedPreference.
 * @param masterKey Master key to be used for encryption.
 * @param fileName Name of the SharedPreference file to be stored on the device. If null, stored as packagename.preference.
 */
public class PreferenceManager(
    context: Context,
    private val masterKey: String,
    private val fileName: String? = null
) {

    private companion object {
        private const val TAG = "EncryptedPreferenceManager"
    }

    private var mContext: Context = context
    private var mFileName: String = fileName ?: "${mContext.packageName}.preference"
    private val preferences: SharedPreferences by lazy {
        try {
            createEncryptedPreferences()
        } catch (e: IOException) {
            Log.e(TAG, "Error with EncryptedSharedPreferences, resetting...", e)
            resetEncryptedPreferences()
            createEncryptedPreferences()
        }
    }

    private var editor: SharedPreferences.Editor = preferences.edit()

    private fun createEncryptedPreferences(): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            mFileName,
            masterKeyAlias,
            mContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun resetEncryptedPreferences() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mContext.deleteSharedPreferences(mFileName)
        } else {
            preferences.edit().clear().apply()
        }
    }

    //region public functions

    /**
     * Save String value in SharedPreference
     */
    public fun put(key: String, value: String) {
        Log.i(TAG, "Insert String Data -> key: $key, value: $value")
        editor.putString(key, value).commit()
    }

    /**
     * Save Int value in SharedPreference
     */
    public fun put(key: String, value: Int) {
        Log.i(TAG, "Insert Int Data -> key: $key, value: $value")
        editor.putInt(key, value).commit()
    }

    /**
     * Save Boolean value in SharedPreference
     */
    public fun put(key: String, value: Boolean) {
        Log.i(TAG, "Insert Boolean Data -> key: $key, value: $value")
        editor.putBoolean(key, value).commit()
    }

    /**
     * Save Long value in SharedPreference
     */
    public fun put(key: String, value: Long) {
        Log.i(TAG, "Insert Long Data -> key: $key, value: $value")
        editor.putLong(key, value).commit()
    }

    /**
     * Save Float value in SharedPreference
     */
    public fun put(key: String, value: Float) {
        Log.i(TAG, "Insert Float Data -> key: $key, value: $value")
        editor.putFloat(key, value).commit()
    }

    /**
     * Get stored String value in SharedPreference.
     * If default value is not set, return null.
     */
    public fun getString(key: String, default: String? = null): String? {
        return preferences.getString(key, default)
    }

    /**
     * Get stored Int value in SharedPreference.
     * If default value is not set, return -1.
     */
    public fun getInt(key: String, default: Int = -1): Int {
        return preferences.getInt(key, default)
    }

    /**
     * Get stored Boolean value in SharedPreference.
     * If default value is not set, return false.
     */
    public fun getBoolean(key: String, default: Boolean = false): Boolean {
        return preferences.getBoolean(key, default)
    }

    /**
     * Get stored Long value in SharedPreference.
     * If default value is not set, return -1L.
     */
    public fun getLong(key: String, default: Long = -1L): Long {
        return preferences.getLong(key, default)
    }

    /**
     * Get stored Float value in SharedPreference.
     * If default value is not set, return -1f.
     */
    public fun getFloat(key: String, default: Float = -1f): Float {
        return preferences.getFloat(key, default)
    }

    /**
     * Delete stored value in SharedPreference.
     */
    public fun delete(key: String) {
        Log.i(TAG, "Delete data -> key: $key")
        editor.remove(key).commit()
    }

    /**
     * Delete all stored value in SharedPreference.
     */
    public fun clear() {
        Log.i(TAG, "Clear all data")
        editor.clear().commit()
    }
    //endregion
}