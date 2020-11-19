package top.gochiusa.operationtest.util

object Constant {
    /**
     * 操作符的模式
     */
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

    /**
     * 设置列表中，各个设置项的位置
     */
    const val OPERATION_MODE_ITEM = 0
    const val MINIMUM_NUMBER_ITEM = 1
    const val MAXIMUM_NUMBER_ITEM = 2
    const val NUMBER_COUNT_ITEM = 3
    const val CHECK_UPDATE_ITEM = 4

    const val DEFAULT_MAX_NUMBER = 99
    const val DEFAULT_MIN_NUMBER = -99
    const val DEFAULT_NUMBER_COUNT = 2

    const val EQUAL_SIGN = "="

    const val PACKAGE_NAME = "top.gochiusa.operationtest"

    /**
     * 安装应用的会话的唯一标识
     */
    const val INSTALL_SESSION_NAME: String = PACKAGE_NAME

    /**
     * JSON数据的键值
     */
    const val VERSION_CODE = "versionCode"
    const val VERSION_NAME = "versionName"
    const val APPLICATION_ID = "applicationId"
    const val VARIANT_NAME = "variantName"
    const val OUTPUT_FILE = "outputFile"
    const val VERSION_DESCRIPTION = "versionDescription"
}