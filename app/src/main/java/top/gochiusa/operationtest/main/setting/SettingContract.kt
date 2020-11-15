package top.gochiusa.operationtest.main.setting

import top.gochiusa.operationtest.base.BasePresenter
import top.gochiusa.operationtest.base.BaseView
import top.gochiusa.operationtest.entity.UpdateInformation

interface SettingContract {

    interface View: BaseView {
        /**
         * 可以升级时回调
         */
        fun canUpdate(updateInformation: UpdateInformation)

        /**
         * 无法升级时回调
         */
        fun notUpdate()
    }

    interface Presenter: BasePresenter {
        /**
         * 请求新版本数据
         */
        fun requestUpdate()

        /**
         * 请求下载新版本apk
         */
        fun downloadUpdate()
    }
}