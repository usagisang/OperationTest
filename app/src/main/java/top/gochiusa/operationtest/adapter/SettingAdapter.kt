package top.gochiusa.operationtest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import top.gochiusa.operationtest.R
import top.gochiusa.operationtest.entity.UserSetting
import top.gochiusa.operationtest.util.Constant.CHECK_UPDATE_ITEM
import top.gochiusa.operationtest.util.Constant.MAXIMUM_NUMBER_ITEM
import top.gochiusa.operationtest.util.Constant.MINIMUM_NUMBER_ITEM
import top.gochiusa.operationtest.util.Constant.NUMBER_COUNT_ITEM
import top.gochiusa.operationtest.util.Constant.OPERATION_MODE_ITEM
import top.gochiusa.operationtest.util.VersionUtil

class SettingAdapter: RecyclerView.Adapter<SettingAdapter.ViewHolder>() {

    var listener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_setting_normal_style, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var resourceId: Int
        var descriptionText = ""
        when(position) {
            OPERATION_MODE_ITEM -> {
                resourceId = R.string.setting_operation_mode
                descriptionText = "允许或禁止某些运算符生成"
            }
            MINIMUM_NUMBER_ITEM -> {
                resourceId = R.string.setting_minimum_number
                descriptionText = UserSetting.minNumber.toString()
            }
            MAXIMUM_NUMBER_ITEM -> {
                resourceId = R.string.setting_maximum_number
                descriptionText = UserSetting.maxNumber.toString()
            }
            NUMBER_COUNT_ITEM -> {
                resourceId = R.string.setting_number_count
                descriptionText = UserSetting.numberCount.toString()
            }
            CHECK_UPDATE_ITEM -> {
                resourceId = R.string.setting_check_update
                descriptionText = "当前版本：${VersionUtil.getVersionName()}"
            }
            else -> {
                resourceId = R.string.setting_default_title
            }
        }
        holder.titleView.setText(resourceId)
        holder.descriptionView.text = descriptionText
        holder.itemView.setOnClickListener {
            listener?.onClick(position)
        }
    }

    override fun getItemCount() = 5

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView: TextView = itemView.findViewById(R.id.tv_item_normal_title)
        val descriptionView: TextView = itemView.findViewById(R.id.tv_item_normal_content)
    }
    interface OnItemClickListener {
        fun onClick(position: Int)
    }
}