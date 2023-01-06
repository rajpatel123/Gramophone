package agstack.gramophone.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import agstack.gramophone.R;

import java.util.Locale;

public class LocaleManagerClass {
    public static final String AppLangIsoCodeKey = "appLangIso";
    private static final String TAG = LocaleManagerClass.class.getSimpleName();

    public static void updateLangIsoCodeInPreferences(String languageIsoCode, Context context) {
        SharedPreferencesHelper.initializeInstance(context);
        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance();
        sharedPreferencesHelper.putString(AppLangIsoCodeKey, languageIsoCode);

    }


    public static String getLangCodeAsPerAppLocale(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        } else {
            //noinspection deprecation
            return context.getResources().getConfiguration().locale.getLanguage();
        }
    }

    public static String getLangCodeFromPreferences(Context context) {
        SharedPreferencesHelper.initializeInstance(context);
        SharedPreferencesHelper preferencesManager = SharedPreferencesHelper.getInstance();
        String appLangIsoCode = Constants.DefaultLangISOCode;
        if (preferencesManager != null) {
            appLangIsoCode = preferencesManager.getString(AppLangIsoCodeKey);
            if (TextUtils.isEmpty(appLangIsoCode) || (appLangIsoCode == null)) {
                appLangIsoCode = Constants.DefaultLangISOCode;
                updateLangIsoCodeInPreferences(appLangIsoCode, context);
            }
        }
        return appLangIsoCode;
    }

    public static String languageForCode(String langCode) {
        if (langCode.equals(Constants.HindiISOCode)) {
            return Constants.Hindi;
        } else {
            return Constants.English;
        }
    }

    public static String localizedLanguageForCode(String langCode, Context context) {
        if (langCode.equals(Constants.HindiISOCode)) {
            return context.getString(R.string.select_language_hi);
        } else if (langCode.equals(Constants.MarathiISOCode)) {
            return context.getString(R.string.select_language_marathi);
        } else {
            return context.getString(R.string.select_language_eng);
        }
    }

    public static void updateLocale(String languageIsoCode, Resources res) {
        if (languageIsoCode != null && languageIsoCode.equalsIgnoreCase("")) {
            return;
        }
        Locale locale = new Locale(languageIsoCode);
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();

        conf.locale = locale;


        res.updateConfiguration(conf, dm);
    }

}
