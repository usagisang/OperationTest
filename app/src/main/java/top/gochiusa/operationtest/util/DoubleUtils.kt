package top.gochiusa.operationtest.util

import top.gochiusa.operationtest.entity.Operator
import top.gochiusa.operationtest.entity.UserSetting
import java.math.BigDecimal
import java.util.*
import kotlin.random.Random

object DoubleUtils {

    private val DEFAULT_BIG_DECIMAL = BigDecimal(-1.0)

    fun generateExpression(): List<Any>  {
        val count = UserSetting.numberCount
        val expression = mutableListOf<Any>()
        var number: Double
        for (i in 0 until count) {
            number = getRandomDouble(UserSetting.minNumber, UserSetting.maxNumber, 2)
            if (number > 0) {
                expression.add(number)
            } else {
                expression.add(Operator.Left)
                expression.add(number)
                expression.add(Operator.Right)
            }
            if (i < count - 1) {
                expression.add(generateRandomOperator(UserSetting.operationMode))
            }
        }
        return expression
    }

    /**
     * 计算中缀表达式的运算结果
     */
    fun calculateWithBigDecimal(list: List<Any>): BigDecimal {
        return calculateReversePolishNotation(generateReversePolishNotation(list))
    }

    /**
     * 生成指定区间的随机数，除了0之外
     */
    private fun getRandomDouble(minNumber: Int, maxNumber: Int, decimal: Int): Double {
        if (minNumber > maxNumber) {
            return -1.0
        }
        if (minNumber == maxNumber && minNumber == 0) {
            return -1.0
        }
        var randomDouble = 0.0
        var randomInt = randomInt(minNumber, maxNumber)
        while (randomDouble == 0.0) {
            randomDouble = Random.Default.nextDouble()
        }
        if (decimal == 0) {
            randomDouble = if (randomInt == 0) {
                0.5
            } else {
                0.1
            }
        }
        return roundingDouble(randomDouble + randomInt, decimal)
    }

    /**
     * 根据操作符计算结果
     * @param numberOne 操作数
     * @param numberTwo 被操作数(被除数)
     */
    private fun calculate(numberOne: BigDecimal,
                          numberTwo: BigDecimal, operator: Operator): BigDecimal {
        return when(operator) {
            Operator.Plus -> {
                numberTwo + numberOne
            }
            Operator.Minus -> {
                numberTwo - numberOne
            }
            Operator.Multiply -> {
                numberTwo * numberOne
            }
            Operator.Divide -> {
                numberTwo / numberOne
            }
            else -> DEFAULT_BIG_DECIMAL
        }
    }

    /**
     * 计算逆波兰表达式的运算结果
     * @param expressionQueue 包含逆波兰表达式的集合，其中的操作数与运算符已经按照逆波兰表达式排列
     */
    private fun calculateReversePolishNotation(expressionQueue: Queue<Any>): BigDecimal {
        val calculateStack: Stack<BigDecimal> = Stack()
        var obj: Any?
        while (expressionQueue.poll().also { obj = it } != null) {
            if (obj is Double) {
                calculateStack.push(BigDecimal.valueOf(obj as Double))
            } else if (obj is Operator) {
                val numberOne = calculateStack.pop()
                val numberTwo = calculateStack.pop()
                calculateStack.push(calculate(numberOne, numberTwo, obj as Operator))
            }
        }
        return calculateStack.pop()
    }
}