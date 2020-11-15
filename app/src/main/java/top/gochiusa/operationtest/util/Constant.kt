package top.gochiusa.operationtest.util

object Constant {
    const val ADD_AND_REDUCE_MODE = "加减法"
    const val MULTIPLICATION_AND_DIVISION_MODE = "乘除法"
    const val MIXED_OPERATION_MODE = "混合运算"

    const val SETTING_FILE_NAME = "settings"
    const val MAX_NUMBER_SETTING_KEY = "max_number"
    const val MIN_NUMBER_SETTING_KEY = "min_number"
    const val NUMBER_COUNT_SETTING_KEY = "count"
    const val OPERATION_MODE_SETTING_KEY = "operation_mode"

    const val DEFAULT_MAX_NUMBER = 99
    const val DEFAULT_MIN_NUMBER = -99
    const val DEFAULT_NUMBER_COUNT = 2
    const val DEFAULT_OPERATION_MODE = ADD_AND_REDUCE_MODE

    const val EQUAL_SIGN = "="

    const val PACKAGE_NAME = "top.gochiusa.operationtest"

    /**
     * 安装应用的会话的唯一标识
     */
    const val INSTALL_SESSION_NAME: String = PACKAGE_NAME
}