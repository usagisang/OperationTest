package top.gochiusa.operationtest.util

import org.json.JSONObject
import top.gochiusa.operationtest.entity.UpdateInformation

object JSONUtils {
    fun parseUpdateInformation(jsonData: String): UpdateInformation {
        val obj = JSONObject(jsonData)
        return UpdateInformation(
            obj.getInt(Constant.VERSION_CODE),
            obj.getString(Constant.VERSION_NAME),
            obj.getString(Constant.APPLICATION_ID),
            obj.getString(Constant.VARIANT_NAME),
            obj.getString(Constant.OUTPUT_FILE),
            obj.getString(Constant.VERSION_DESCRIPTION))
    }
}