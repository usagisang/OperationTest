package top.gochiusa.operationtest.main.setting

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import top.gochiusa.operationtest.R

class SettingRangeItemFragment(private val title: String,
                               private val range: IntRange,
                               private val function: (Int) -> Unit): DialogFragment() {

    private lateinit var editText: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // 创建自定义的内容View
            val view  = requireActivity().layoutInflater.inflate(
                    R.layout.layout_popup_edit_text, null)
            editText = view.findViewById(R.id.edt_setting_content)
            builder.setTitle(title).setCancelable(true).setView(view)
                    .setPositiveButton(R.string.positive_button) { _, _ ->
                        val number: Int = editText.text.toString().toInt()
                        if (range.contains(number)) {
                            function(number)
                        } else {
                            // 弹出失败提示
                            showToast(OUT_OF_RANGE_TIP)
                        }
                    }
                    .setNegativeButton(R.string.negative_button) {_, _ ->}
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val OUT_OF_RANGE_TIP = "设置失败：输入值超出允许范围"
    }
}