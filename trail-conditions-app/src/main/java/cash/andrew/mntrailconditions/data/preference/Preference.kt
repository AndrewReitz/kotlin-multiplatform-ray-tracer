package cash.andrew.mntrailconditions.data.preference

import android.content.SharedPreferences
import androidx.core.content.edit

fun SharedPreferences.stringPreference(key: String, defaultValue: String?) = Preference(
        getFun = { getString(key, defaultValue) },
        setFun = { edit { putString(key, it) } }
)

fun SharedPreferences.stringSetPreference(
        key: String,
        defaultValue: Set<String> = emptySet()
) = Preference(
        getFun = { getStringSet(key, defaultValue) },
        setFun = { edit { putStringSet(key, it) } }
)

fun SharedPreferences.longPreference(key: String, defaultValue: Long) = Preference(
        getFun = { getLong(key, defaultValue) },
        setFun = { edit { putLong(key, it) } }
)

fun SharedPreferences.intPreference(key: String, defaultValue: Int) = Preference(
        getFun = { getInt(key, defaultValue) },
        setFun = { edit { putInt(key, it) } }
)

fun SharedPreferences.booleanPreference(key: String, defaultValue: Boolean) = Preference(
        getFun = { getBoolean(key, defaultValue) },
        setFun = { edit { putBoolean(key, it) } }
)

data class Preference<T : Any>(
        val getFun: () -> T?,
        val setFun: (T) -> Unit
) {
    fun getOrNull(): T? = getFun()
    fun get(): T = requireNotNull(getFun())
    fun set(value: T) = setFun(value)
}