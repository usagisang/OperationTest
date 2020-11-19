package top.gochiusa.operationtest.entity

import kotlin.math.max
import kotlin.math.min

class Fraction(var numerator: Int, var denominator: Int) {

    constructor(numerator: Int): this(numerator, 1)

    fun add(int: Int) {
        numerator += (int * denominator)
    }

    fun minus(int: Int) {
        numerator -= (int * denominator)
    }

    fun multiply(int: Int) {
        numerator *= int
    }

    fun divide(int: Int) {
        if (int == 0) {
            throw ArithmeticException()
        }
        denominator *= int
    }


    fun toInt() = numerator / denominator

    fun toDouble() = numerator.toDouble() / denominator.toDouble()

    fun isInt(): Boolean = numerator % denominator == 0

    /**
     * 对分数进行约分
     */
    fun reduce() {
        var i = max(numerator, denominator)
        var j = min(numerator, denominator)
        var cache: Int
        while (j != 0 && i != 0) {
            // 缓存当前除数
            cache = j
            // 余数作为下一个除数
            j = i % j
            // 除数作为下一个被除数
            i = cache
        }
        numerator /= i
        denominator /= i
    }

    /**
     * 规范化分数可能带有的负号，如90/-74，要求负号始终在前面，比如-90/74
     */
    fun standard() {
        if (denominator < 0) {
            denominator = -denominator
            numerator = -numerator
        }
    }

    override fun toString(): String {
        return if (isInt()) {
            standard()
            "${toInt()}"
        } else {
            reduce()
            standard()
            "$numerator/$denominator"
        }
    }

    override fun equals(other: Any?): Boolean {
        when(other) {
            is Double -> {
                return other == this.toDouble()
            }
            is Int -> {
                return if (isInt()) {
                    other == (numerator / denominator)
                } else {
                    false
                }
            }
            is Fraction -> {
                other.reduce()
                this.reduce()
                return (other.numerator == this.numerator) &&
                        (other.denominator == this.denominator)
            }
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = numerator
        result = 31 * result + denominator
        return result
    }

}