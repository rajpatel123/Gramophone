package agstack.gramophone.base

import android.os.Bundle
/*


BaseNavigator is the Thin Layer Between View and ViewModel

ViewModel ~~~~~~~View~~~~~~~Model


*/


interface BaseNavigator {

    fun <T> openActivity(cls:Class<T>,extras: Bundle?=null)

    fun <T> openAndFinishActivity(cls:Class<T>,extras: Bundle?=null)

    fun isNetworkAvailable():Boolean

    fun requestPermission(permission:String,callback: (Boolean)->Unit)

    fun getLanguage():String

    fun getMessage(stringResourceId:Int):String

    fun onError(message: String?)

    fun <T> onSuccess(cls:Class<T>)

    fun onLoading()
}