package top.gochiusa.operationtest.main.setting

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import top.gochiusa.operationtest.R
import top.gochiusa.operationtest.adapter.SettingAdapter
import top.gochiusa.operationtest.entity.UpdateInformation
import top.gochiusa.operationtest.entity.UserSetting
import top.gochiusa.operationtest.main.MyApplication
import top.gochiusa.operationtest.util.Constant
import top.gochiusa.operationtest.util.VersionUtil
import top.gochiusa.operationtest.util.shortBitToBoolean
import top.gochiusa.operationtest.widget.MyDividerItemDecoration
import java.util.*

class SettingActivity: AppCompatActivity(),
    SettingContract.View, SettingAdapter.OnItemClickListener {

    private val presenter: SettingContract.Presenter = SettingPresenter(this)

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SettingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        supportActionBar?.setTitle(R.string.settings)
        // 初始化RecyclerView
        recyclerView = findViewById(R.id.rv_setting)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(MyDividerItemDecoration(this,
                DividerItemDecoration.VERTICAL))
        adapter = SettingAdapter()
        adapter.listener = this
        recyclerView.adapter = adapter
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        when (requestCode) {
            STORE_PERMISSION_UPDATE_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    presenter.downloadUpdate()
                    showToast(DOWNLOAD_START_TIP)
                } else {
                    showToast(REFUSE_STORE_PERMISSION_TIP)
                }
            }
        }
    }

    override fun canUpdate(updateInformation: UpdateInformation) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(ALERT_TITLE)
        val versionName = VersionUtil.getVersionName()
        alertDialogBuilder.setMessage(
                "发现新版本${updateInformation.versionName}，当前版本为${versionName}\n" +
                        "新版本的特性：\n${updateInformation.versionDescription}")
                .setCancelable(true)
                .setPositiveButton("确认更新") { _: DialogInterface, _: Int ->
                    // 如果已经有了存储权限
                    if (ContextCompat.checkSelfPermission(
                                    MyApplication.context,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ) == PackageManager.PERMISSION_GRANTED) {
                        presenter.downloadUpdate()
                        showToast(DOWNLOAD_START_TIP)
                    } else {
                        // 否则尝试申请权限
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                    STORE_PERMISSION_UPDATE_REQUEST_CODE)
                        }
                    }
                }.setNegativeButton("取消") { _, _ -> }.show()
    }

    override fun notUpdate() {
        showToast(NOT_UPDATE_TIP)
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(position: Int) {
        when(position) {
            Constant.OPERATION_MODE_ITEM -> {
                var modeCache = UserSetting.operationMode
                AlertDialog.Builder(this)
                        .setTitle(R.string.setting_operation_mode)
                        .setCancelable(true)
                        .setMultiChoiceItems(
                                R.array.operator_array,
                                booleanArrayOf(
                                        UserSetting.operationMode.shortBitToBoolean(),
                                        (UserSetting.operationMode shr 1).shortBitToBoolean(),
                                        (UserSetting.operationMode shr 2).shortBitToBoolean(),
                                        (UserSetting.operationMode shr 3).shortBitToBoolean()
                                )) { _, which, isChecked ->
                            modeCache = if (isChecked) {
                                // 如果需要选中
                                // 通过1 shl which 生成诸如0100的独热码
                                // 然后与modeCache进行位或运算就可以得到选中后的状态
                                modeCache or (1 shl which)
                            } else {
                                // 如果不需要选中
                                // 生成状态0100的反码1011，与modeCache进行位与运算，就能去除0位的状态
                                modeCache and ((1 shl which).inv())
                            }
                        }.setPositiveButton(R.string.positive_button) { _, _ ->
                            if (modeCache == 0) {
                                // 弹出失败提示
                                showToast(SET_MODE_ERROR_TIP)
                            } else {
                                UserSetting.operationMode = modeCache
                            }
                        }.setNegativeButton(R.string.negative_button) {_, _ ->}
                        .create().show()
            }
            Constant.MINIMUM_NUMBER_ITEM -> {
                SettingRangeItemFragment(
                        String.format(Locale.CHINA, MINIMUM_ITEM_TITLE, UserSetting.maxNumber),
                        -999..UserSetting.maxNumber) {
                    UserSetting.minNumber = it
                    adapter.notifyItemChanged(position)
                }.show(supportFragmentManager, null)
            }
            Constant.MAXIMUM_NUMBER_ITEM -> {
                SettingRangeItemFragment(
                        String.format(Locale.CHINA, MAXIMUM_ITEM_TITLE, UserSetting.minNumber),
                        UserSetting.minNumber..999) {
                    UserSetting.maxNumber = it
                    adapter.notifyItemChanged(position)
                }.show(supportFragmentManager, null)
            }
            Constant.NUMBER_COUNT_ITEM -> {
                SettingRangeItemFragment(
                        String.format(Locale.CHINA, NUMBER_COUNT_ITEM_TITLE, 2, 6),
                        2..6) {
                    UserSetting.numberCount = it
                    adapter.notifyItemChanged(position)
                }.show(supportFragmentManager, null)
            }
            Constant.CHECK_UPDATE_ITEM -> {
                presenter.requestUpdate()
            }
        }
    }

    companion object {
        fun startThisActivity(context: Context) {
            context.startActivity(Intent(context, SettingActivity::class.java))
        }
        private const val ALERT_TITLE = "是否确认更新？"
        private const val DOWNLOAD_START_TIP = "新版本下载中......"
        private const val NOT_UPDATE_TIP = "当前已经是最新版本"
        private const val REFUSE_STORE_PERMISSION_TIP = "拒绝存储权限则无法进行更新"
        private const val STORE_PERMISSION_UPDATE_REQUEST_CODE = 1

        private const val SET_MODE_ERROR_TIP = "设置失败：至少需要启用一项运算符"

        private const val MAXIMUM_ITEM_TITLE = "最大值（%d到999）"
        private const val MINIMUM_ITEM_TITLE = "最小值（-999到%d）"
        private const val NUMBER_COUNT_ITEM_TITLE = "数字个数（%d至%d）"
    }

}