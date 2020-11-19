package top.gochiusa.operationtest.util

import top.gochiusa.operationtest.entity.Fraction
import top.gochiusa.operationtest.entity.Operator
import top.gochiusa.operationtest.entity.UserSetting
import java.util.*

object FractionUtils {

    private val DEFAULT_FRACTION = Fraction(1, 1)
    /**
     * 生成包含中缀表达式的集合
     */
    fun generateExpression(): List<Any> {
        val count = UserSetting.numberCount
        // 缓存运算表达式
        val expression = mutableListOf<Any>()
        var number: Int
        // 缓存随机生成的运算符
        var operator: Operator
        // 运算符出现次数的统计集合
        val operatorList: MutableList<Int> = mutableListOf(0, 0, 0, 0)
        for (i in 0 until count) {
            number = randomInt(UserSetting.minNumber, UserSetting.maxNumber)
            if (number > 0) {
                expression.add(number)
            } else {
                expression.add(Operator.Left)
                expression.add(number)
                expression.add(Operator.Right)
            }
            if (i < count - 1) {
                // 如果某一个运算符被随机出来的次数过多，继续尝试生成其他运算符
                do {
                    operator = generateRandomOperator(UserSetting.operationMode)
                } while (operatorList[Operator.antiMatch(operator)]++ > 1)
                expression.add(operator)
            }
        }
        return expression
    }

    fun calculateWithFraction(list: List<Any>): Fraction {
        return calculateReversePolishNotation(generateReversePolishNotation(list))
    }

    /**
     * 根据操作符计算结果
     * @param numberOne 操作数
     * @param numberTwo 被操作数(被除数)
     */
    private fun calculate(numberOne: Fraction,
                  numberTwo: Fraction, operator: Operator): Fraction {
        return when(operator) {
            Operator.Plus -> {
                numberTwo add numberOne
            }
            Operator.Minus -> {
                numberTwo minus numberOne
            }
            Operator.Multiply -> {
                numberTwo multiply numberOne
            }
            Operator.Divide -> {
                numberTwo divide numberOne
            }
            else -> DEFAULT_FRACTION
        }
    }

    /**
     * 根据逆波兰表达式计算结果、返回分数。要求序列中所有的数为Int
     */
    private fun calculateReversePolishNotation(expressionQueue: Queue<Any>): Fraction {
        val calculateStack: Stack<Fraction> = Stack()
        var obj: Any?
        while (expressionQueue.poll().also { obj = it } != null) {
            if (obj is Int) {
                calculateStack.push(Fraction(obj as Int))
            } else if (obj is Operator) {
                val numberOne = calculateStack.pop()
                val numberTwo = calculateStack.pop()
                calculateStack.push(calculate(numberOne, numberTwo, obj as Operator))
            }
        }
        return calculateStack.pop()
    }

    infix fun Fraction.add(fraction: Fraction): Fraction {
        this.numerator = (this.numerator * fraction.denominator) +
                (fraction.numerator * this.denominator)
        this.denominator *= fraction.denominator
        return this
    }

    infix fun Fraction.minus(fraction: Fraction): Fraction {
        this.numerator = (this.numerator * fraction.denominator) -
                (fraction.numerator * this.denominator)
        this.denominator *= fraction.denominator
        return this
    }

    infix fun Fraction.multiply(fraction: Fraction): Fraction {
        this.numerator *= fraction.numerator
        this.denominator *= fraction.denominator
        return this
    }

    infix fun Fraction.divide(fraction: Fraction): Fraction {
        this.numerator *= fraction.denominator
        this.denominator *= fraction.numerator
        return this
    }
}