package top.gochiusa.operationtest.util

import top.gochiusa.operationtest.entity.Operator
import java.util.*

/**
 * 按照指定的保留小数的位数，舍入Double
 */
fun roundingDouble(double: Double, decimal: Int) =
        String.format("%.${decimal}f", double).toDouble()

/**
 * 生成指定区间内的随机整数，不包含0
 */
fun randomInt(minNumber: Int, maxNumber: Int): Int {
    if (minNumber == maxNumber && minNumber == 0) {
        return -1
    }
    var randomInt = 0
    while (randomInt == 0) {
        randomInt = getRandomInt(minNumber, maxNumber)
    }
    return randomInt
}

/**
 * 随机生成运算符
 */
fun generateRandomOperator(operationMode: Int): Operator {
    val operatorList = mutableListOf<Operator>()
    if (operationMode and Constant.ADD_MODE == Constant.ADD_MODE) {
        operatorList.add(Operator.Plus)
    }
    if (operationMode and Constant.MINUS_MODE == Constant.MINUS_MODE) {
        operatorList.add(Operator.Minus)
    }
    if (operationMode and Constant.MULTIPLICATION_MODE == Constant.MULTIPLICATION_MODE) {
        operatorList.add(Operator.Multiply)
    }
    if (operationMode and Constant.DIVISION_MODE == Constant.DIVISION_MODE) {
        operatorList.add(Operator.Divide)
    }
    return operatorList[getRandomInt(0, operatorList.size - 1)]
}

/**
 * 将含有中缀表达式的集合转换为含有逆波兰表达式的集合
 */
fun generateReversePolishNotation(list: List<Any>): Queue<Any> {
    val operationStack: Stack<Operator> = Stack()
    val deque: ArrayDeque<Any> = ArrayDeque()
    for (obj in list) {
        if (obj is Double || obj is Int) {
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
 * 生成指定区间内的随机整数，包含0
 */
private fun getRandomInt(minNumber: Int, maxNumber: Int): Int {
    return if (minNumber > maxNumber) {
        -1
    } else {
        (minNumber..maxNumber).random()
    }
}


