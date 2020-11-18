package top.gochiusa.operationtest.entity

import android.content.Context
import android.content.SharedPreferences
import top.gochiusa.operationtest.main.MyApplication
import top.gochiusa.operationtest.util.Constant

object UserSetting {

    private val preferences: SharedPreferences = MyApplication.context.getSharedPreferences(
            Constant.SETTING_FILE_NAME, Context.MODE_PRIVATE
    )
    var maxNumber: Int = preferences.getInt(Constant.MAX_NUMBER_SETTING_KEY,
            Constant.DEFAULT_MAX_NUMBER)
        set(value) {
            field = value
            preferences.edit().putInt(Constant.MAX_NUMBER_SETTING_KEY, value).apply()
        }
    var minNumber: Int = preferences.getInt(Constant.MIN_NUMBER_SETTING_KEY,
            Constant.DEFAULT_MIN_NUMBER)
        set(value) {
            field = value
            preferences.edit().putInt(Constant.MIN_NUMBER_SETTING_KEY, value).apply()
        }
    var numberCount: Int = preferences.getInt(Constant.NUMBER_COUNT_SETTING_KEY,
            Constant.DEFAULT_NUMBER_COUNT)
        set(value) {
            field = value
            preferences.edit().putInt(Constant.NUMBER_COUNT_SETTING_KEY, value).apply()
        }
    var operationMode: Int = preferences.getInt(Constant.OPERATION_MODE_SETTING_KEY,
            Constant.DEFAULT_OPERATION_MODE)
        set(value) {
            field = value
            preferences.edit().putInt(Constant.OPERATION_MODE_SETTING_KEY, value).apply()
        }
}