package top.gochiusa.operationtest.main

import android.app.Application
import android.app.DownloadManager
import android.content.Context

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        downloadManager = applicationContext.getSystemService(Context.DOWNLOAD_SERVICE)
                as DownloadManager
    }

    companion object {
        lateinit var context: Context
            private set
        lateinit var downloadManager: DownloadManager
            private set
    }
}