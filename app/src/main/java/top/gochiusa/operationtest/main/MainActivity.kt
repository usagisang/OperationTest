package top.gochiusa.operationtest.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import top.gochiusa.operationtest.R
import top.gochiusa.operationtest.entity.Operator
import top.gochiusa.operationtest.entity.UserSetting
import top.gochiusa.operationtest.main.setting.SettingActivity
import top.gochiusa.operationtest.util.Constant
import top.gochiusa.operationtest.util.FractionUtils
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var tipTextView: TextView
    private lateinit var expressionTextView: TextView
    private lateinit var editText: EditText
    private lateinit var showAnswerButton: Button
    private lateinit var checkAnswerButton: Button
    private lateinit var turnNextButton: Button

    /**
     * 当前显示的表达式
     */
    private lateinit var expressionList: List<Any>

    private val regex = Regex("^[-+]?([0-9]\\d*)$|^[-+]?[0-9]+[./]?[0-9]+$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initChildView()
    }


    private fun initChildView() {
        tipTextView = findViewById(R.id.tv_main_tips)
        expressionTextView = findViewById(R.id.tv_main_expression)
        editText = findViewById(R.id.edt_main_answer)
        showAnswerButton = findViewById(R.id.btn_main_show_answer)
        checkAnswerButton = findViewById(R.id.btn_main_check_answer)
        turnNextButton = findViewById(R.id.btn_main_turn_next)

        showAnswerButton.setOnClickListener {
            it.isEnabled = false
            checkAnswerButton.isEnabled = false
            tipTextView.setText(R.string.show_answer_tip)
            val resultFraction = FractionUtils.calculateWithFraction(expressionList)
            // 确定需要显示的算数结果
            expressionTextView.text = "${expressionTextView.text}$resultFraction"
        }

        checkAnswerButton.setOnClickListener {
            val result = FractionUtils.calculateWithFraction(expressionList)
            var correct = false
            if (editText.text.matches(regex)) {
                correct = (result.toString() == editText.text.toString())
            }
            if (correct) {
                tipTextView.setText(R.string.answer_correct)
            } else {
                tipTextView.setText(R.string.answer_wrong)
            }
        }
        turnNextButton.setOnClickListener {
            showAnswerButton.isEnabled = true
            checkAnswerButton.isEnabled = true
            editText.text.clear()
            refreshExpression()
            tipTextView.setText(R.string.ask_question)
        }
        refreshExpression()
    }

    private fun refreshExpression() {
        // 生成算式列表
        expressionList = FractionUtils.generateExpression()
        // 字符拼接
        val builder = StringBuilder()
        for (any in expressionList) {
            when (any) {
                is Double -> {
                    builder.append(any.toDouble())
                }
                is Operator -> {
                    builder.append(any.toString())
                }
                is Int -> {
                    builder.append(any.toInt())
                }
            }
        }
        builder.append(Constant.EQUAL_SIGN)
        expressionTextView.text = builder.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.main_setting -> {
                SettingActivity.startThisActivity(this)
            }
        }
        return true
    }
}