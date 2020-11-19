package top.gochiusa.operationtest.main.setting

import android.app.DownloadManager
import android.net.Uri
import android.os.Environment
import com.gochiusa.http.HttpClient
import kotlinx.coroutines.*
import top.gochiusa.operationtest.base.BasePresenterImpl
import top.gochiusa.operationtest.main.MyApplication
import top.gochiusa.operationtest.util.JSONUtils
import top.gochiusa.operationtest.util.LogUtil
import top.gochiusa.operationtest.util.VersionUtil
import java.io.IOException

class SettingPresenter(view: SettingContract.View) :
    BasePresenterImpl<SettingContract.View>(view), SettingContract.Presenter {

    override fun requestUpdate() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val updateInformation = getUpdateInformation()
                if (VersionUtil.getVersionCode() < (updateInformation.versionCode ?: 0)) {
                    view?.canUpdate(updateInformation)
                } else {
                    view?.notUpdate()
                }
            } catch (e: IOException) {
                LogUtil.printError(e)
                view?.showToast(UPDATE_ERROR)
            }
        }
    }

    override fun downloadUpdate() {
        // 创建下载请求
        val request = DownloadManager.Request(Uri.parse(UPDATE_DOWNLOAD_URL))
        // 下载中和下载完后都显示通知栏
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        // 使用系统默认的下载路径
        val apkName = "OperationTest.apk"
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, apkName)
        // 通知栏标题
        request.setTitle(apkName)
        // 通知栏描述信息
        request.setDescription(UPDATE_NOTIFICATION_DESCRIBE + apkName)
        // 设置MIME类型
        request.setMimeType("application/vnd.android.package-archive")

        MyApplication.downloadManager.enqueue(request)
    }

    private suspend fun getUpdateInformation() = withContext(Dispatchers.IO) {
        JSONUtils.parseUpdateInformation(
            HttpClient.createDefaultCall(
                UPDATE_INFORMATION_URL
            ).execute().responseBodyString
        )
    }


    companion object {
        private const val UPDATE_ERROR = "检查更新失败"
        private const val UPDATE_DOWNLOAD_URL =
                "http://gochiusa.top:8901/update/download?name=operation_test"
        private const val UPDATE_INFORMATION_URL =
            "http://gochiusa.top:8901/update/checkVersion?name=operation_test"
        private const val UPDATE_NOTIFICATION_DESCRIBE = "正在下载: "
    }
}