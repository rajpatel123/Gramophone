package agstack.gramophone.ui.profile.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.profile.ProfileActivityNavigator
import agstack.gramophone.ui.settings.view.SettingsActivity
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
) : BaseViewModel<ProfileActivityNavigator>() {

    fun logout(v: View){
        SharedPreferencesHelper.instance?.putBoolean(
            SharedPreferencesKeys.logged_in,
            false
        )
        getNavigator()?.logout()
    }

    fun openSettings(){
        getNavigator()?.openActivity(SettingsActivity::class.java,null)
    }

    fun shareApp(){
    getNavigator()?.shareApp(Intent(Intent.ACTION_SEND).apply {
        type = "text/plain";
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        putExtra(Intent.EXTRA_SUBJECT, getNavigator()?.getMessage(R.string.share_app));
        putExtra(Intent.EXTRA_TEXT, getNavigator()?.getMessage(R.string.app_url));

    })
    }


}