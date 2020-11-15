package top.gochiusa.operationtest.entity

enum class Operator(val value: String, val priority: Int) {
    Plus("+", 1), Minus("-", 1),
    Multiply("x", 2), Divide("/", 2),
    Left("(", 3), Right(")", 3);

    override fun toString(): String {
        return value
    }

    companion object {
        fun matching(value: Int): Operator {
            return when(value) {
                0 -> Plus
                1 -> Minus
                2 -> Multiply
                3 -> Divide
                else -> Plus
            }
        }
    }
}