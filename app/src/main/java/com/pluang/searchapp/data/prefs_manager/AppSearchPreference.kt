package com.pluang.searchapp.data.prefs_manager

import android.content.Context
import android.content.SharedPreferences
import com.pluang.searchapp.data.prefs_manager.AppSearchPreference

class AppSearchPreference(var mContext: Context) {
    var preferences: SharedPreferences
    var editor: SharedPreferences.Editor
    var searchHistory: Set<String>?
        get() = preferences.getStringSet(KEY_USER_SEARCH_HISTORY, null)
        set(searchHistory) {
            editor.putStringSet(KEY_USER_SEARCH_HISTORY, searchHistory)
            editor.apply()
        }
/*
    fun setSearchKey(key: Set<String>) {
        editor.putStringSet(KEY_USER_SEARCH_HISTORY, key)
        editor.apply()
    }

    fun getSearchKey(): Set<String> {
        return preferences.getStringSet(KEY_USER_SEARCH_HISTORY, null)!!
    }*/

    companion object {
        private const val DB_NAME = "search_user_db"
        private const val KEY_USER_SEARCH_HISTORY = "user_search_history"
    }

    init {
        preferences = mContext.getSharedPreferences(DB_NAME, 0)
        editor = preferences.edit()
    }
}


