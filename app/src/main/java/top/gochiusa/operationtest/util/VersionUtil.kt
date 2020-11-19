package top.gochiusa.operationtest.util

import android.os.Build
import top.gochiusa.operationtest.main.MyApplication

object VersionUtil {
    fun getVersionCode(): Int {
        val info = MyApplication.context.packageManager.getPackageInfo(
                MyApplication.context.packageName, 0)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            info.longVersionCode.toInt()
        } else {
            info.versionCode
        }
    }

    fun getVersionName(): String {
        return MyApplication.context.packageManager.getPackageInfo(
            MyApplication.context.packageName, 0
        ).versionName
    }
}