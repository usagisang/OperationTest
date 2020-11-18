package top.gochiusa.operationtest.util

object Constant {
    const val ADD_MODE: Int = 0b0001
    const val MINUS_MODE = 0b0010
    const val MULTIPLICATION_MODE = 0b0100
    const val DIVISION_MODE = 0b1000
    const val MIXED_OPERATION_MODE = 0b1111

    const val DEFAULT_OPERATION_MODE = MIXED_OPERATION_MODE

    const val SETTING_FILE_NAME = "settings"
    const val MAX_NUMBER_SETTING_KEY = "max_number"
    const val MIN_NUMBER_SETTING_KEY = "min_number"
    const val NUMBER_COUNT_SETTING_KEY = "count"
    const val OPERATION_MODE_SETTING_KEY = "operation_mode"

    const val DEFAULT_MAX_NUMBER = 99
    const val DEFAULT_MIN_NUMBER = -99
    const val DEFAULT_NUMBER_COUNT = 2

    const val EQUAL_SIGN = "="

    const val PACKAGE_NAME = "top.gochiusa.operationtest"

    /**
     * 安装应用的会话的唯一标识
     */
    const val INSTALL_SESSION_NAME: String = PACKAGE_NAME
}