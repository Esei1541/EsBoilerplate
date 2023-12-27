package kr.esei.library.model

/**
 * 데이터를 담기 위한 HashMap Class
 * 데이터를 기본 자료형으로 가져올 수 있도록 get function이 구현되어 있다.
 */
public class EventDataBundle: HashMap<String, Any>() {

    public fun getString(key: String): String? {
        return this[key] as String?
    }

    public fun getInt(key: String): Int? {
        val value = this[key] as Double?
        return value?.toInt()
    }

    public fun getBoolean(key: String): Boolean {
        val value = this[key] as Boolean?
        return value ?: false
    }

    public fun getLong(key: String): Long? {
        val value = this[key] as Double?
        return value?.toLong()
    }

    public fun getDouble(key: String): Double? {
        return this[key] as Double?
    }

    public fun getFloat(key: String): Float? {
        val value = this[key] as Double?
        return value?.toFloat()
    }

}