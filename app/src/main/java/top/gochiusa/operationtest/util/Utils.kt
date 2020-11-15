package top.gochiusa.operationtest.util

import top.gochiusa.operationtest.entity.Operator
import top.gochiusa.operationtest.entity.UserSetting
import java.math.BigDecimal
import java.util.*
import kotlin.random.Random


/**
 * 生成包含中缀表达式的集合
 */
fun generateExpression(): List<Any> {
    val count = UserSetting.numberCount
    val expression = mutableListOf<Any>()
    var number: Double
    for (i in 0 until count) {
        number = getRandomNumber(UserSetting.minNumber, UserSetting.maxNumber, 0)
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
fun calculateExpression(list: List<Any>): BigDecimal {
    return calculateReversePolishNotation(generateReversePolishNotation(list))
}

/**
 * 按照指定的保留小数的位数，舍入Double
 */
fun roundingDouble(double: Double, decimal: Int) =
        String.format("%.${decimal}f", double).toDouble()


/**
 * 生成指定区间的随机数，除了0之外
 */
private fun getRandomNumber(minNumber: Int, maxNumber: Int, decimal: Int): Double {
    if (minNumber > maxNumber) {
        return -1.0
    }
    if (minNumber == maxNumber && minNumber == 0) {
        return -1.0
    }
    var randomInt = 0
    var randomDouble = 0.0
    while (randomInt == 0 && randomDouble == 0.0) {
        randomInt = Random.Default.nextInt(minNumber, maxNumber)
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
 * 生成指定区间内的随机整数，包含0
 */
private fun getRandomInt(minNumber: Int, maxNumber: Int): Int {
    return if (minNumber > maxNumber) {
        -1
    } else {
        (minNumber..maxNumber).random()
    }
}

/**
 * 随机生成运算符
 */
private fun generateRandomOperator(operationMode: String): Operator {
    return when(operationMode) {
        Constant.ADD_AND_REDUCE_MODE -> Operator.matching(getRandomInt(0, 1))
        Constant.MULTIPLICATION_AND_DIVISION_MODE -> Operator.matching(getRandomInt(2, 3))
        Constant.MIXED_OPERATION_MODE-> Operator.matching(getRandomInt(0, 3))
        else -> Operator.matching(getRandomInt(0, 3))
    }
}

/**
 * 将含有中缀表达式的集合转换为含有逆波兰表达式的集合
 */
private fun generateReversePolishNotation(list: List<Any>): Queue<Any> {
    val operationStack: Stack<Operator> = Stack()
    val deque: ArrayDeque<Any> = ArrayDeque()
    for (obj in list) {
        if (obj is Double) {
            // 操作数进队列
            deque.offerLast(obj)
        } else if (obj is Operator) {
            val operator: Operator = obj
            when (obj) {
                Operator.Left -> {
                    // 左括号直接进栈
                    operationStack.push(operator)
                }
                Operator.Right -> {
                    // 遇到右括号，持续弹出运算符，直到遇到左括号
                    while (operationStack.peek() != Operator.Left) {
                        deque.offerLast(operationStack.pop())
                    }
                    // 弹出左括号
                    operationStack.pop()
                }
                else -> {
                    // 如果栈不为空，栈顶不是左括号，栈顶运算符的优先级更高
                    // 将栈内的运算符弹出到队列，直到不满足前面的条件
                    while (!operationStack.isEmpty() && operationStack.peek() != Operator.Left
                            && operator.priority <= operationStack.peek().priority) {
                        deque.offerLast(operationStack.pop())
                    }
                    // 不满足上述条件，将运算符压入栈内
                    operationStack.push(operator)
                }
            }
        }
    }
    // 将剩余的运算符全部弹出
    while (!operationStack.isEmpty()) {
        deque.offerLast(operationStack.pop())
    }
    return deque
}


/**
 * 根据操作符计算结果
 * @param numberOne 操作数
 * @param numberTwo 被操作数(被除数)
 */
private fun calculate(numberOne: BigDecimal,
                      numberTwo: BigDecimal, operator: Operator): BigDecimal? {
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
        else -> null
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


