package top.gochiusa.operationtest.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import top.gochiusa.operationtest.util.calculateExpression
import top.gochiusa.operationtest.util.generateExpression
import top.gochiusa.operationtest.util.roundingDouble
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

    /**
     * 当前显示的表达式字符串
     */
    private var expressionString = ""

    private val regex = Regex("^[-+]?([0-9]\\d*)$|^[-+]?[0-9]+\\.?[0-9]+$")

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
            val result = calculateExpression(expressionList)
            // TODO 算数结果计算优化
            // 确定需要显示的算数结果
            val text = if (UserSetting.operationMode == Constant.ADD_AND_REDUCE_MODE) {
                result.toInt().toString()
            } else {
                roundingDouble(result.toDouble(), 3).toString()
            }
            expressionTextView.text = "${expressionTextView.text}$text"
        }

        checkAnswerButton.setOnClickListener {
            val result = calculateExpression(expressionList)
            var correct = false
            // TODO 计算结果正确与否判断优化
            if (editText.text.matches(regex)) {
                correct = if (UserSetting.operationMode == Constant.ADD_AND_REDUCE_MODE) {
                    Log.d("this", result.toInt().toString())
                    Log.d("this", editText.text.toString())
                    (result.toInt().toString() == editText.text.toString())
                } else {
                    (roundingDouble(result.toDouble(), 3) ==
                            roundingDouble(editText.text.toString().toDouble(), 3))
                }
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
        expressionList = generateExpression()
        // 字符拼接
        val builder = StringBuilder()
        for (any in expressionList) {
            if (any is Double) {
                builder.append(any.toInt())
            } else if (any is Operator) {
                builder.append(any.toString())
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