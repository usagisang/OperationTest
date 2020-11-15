package top.gochiusa.operationtest.main.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import top.gochiusa.operationtest.R
import top.gochiusa.operationtest.entity.UpdateInformation

class SettingActivity: AppCompatActivity(), SettingContract.View {

    private val presenter: SettingContract.Presenter = SettingPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        actionBar?.setTitle(R.string.settings)
    }

    companion object {
        fun startThisActivity(context: Context) {
            context.startActivity(Intent(context, SettingActivity::class.java))
        }
    }

    override fun canUpdate(updateInformation: UpdateInformation) {
        TODO("Not yet implemented")
    }

    override fun notUpdate() {
        TODO("Not yet implemented")
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}