package agstack.gramophone.ui.createnewpost.view;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import agstack.gramophone.utils.SharedPreferencesKeys;

/**
 * Created by Sarita on 26-02-2018.
 */

public class MySingleton {
    private static MySingleton instance;
    private Boolean isPostListRefreshNeeded = false;
    private Boolean isFlexiUpdate = false;
    private Boolean isFlexiUpdateInProgress = false;
    private boolean isreferralApplied = false;
    private ReentrantLock lock = new ReentrantLock();
    private Bitmap bitmap = null;
    private String whatsAppMessage = null;
    private Boolean isFavoriteVyaparChanged=false;
    private Boolean isFavoriteTraderChanged=false;
    private Boolean updateMySellListing=false;
    private Boolean updateMyBuyListing=false;



    private Intent viewIntent = null;
    private Intent shareIntent = null;
    private Intent splashIntent = null;
    private Bitmap screenshotBitmap = null;
    private boolean offTokenAutoComplete = false;
    private String kbUrl = null;

    public static MySingleton getInstance() {
        if (instance == null)
            instance = new MySingleton();
        return instance;
    }

    private MySingleton() {
    }

    public boolean campaignAvailable = false;
    public Boolean isPageRefreshNeeded = false;
    public Boolean isCurrentLocationUpdated = false;
    public Boolean isMyFarmPageRefreshNeeded = false;
    public Boolean isAllFarmPageRefreshNeeded = false;
    public String sharedImagePath;
    private Boolean isRefreshApiInProgress = false;
    private HashMap<String, List<String>> quizSelectedAnswer = new HashMap<>();


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public HashMap<String, List<String>> getQuizSelectedAnswer() {
        return quizSelectedAnswer;
    }

    public void setQuizSelectedAnswer(HashMap<String, List<String>> quizSelectedAnswer) {
        this.quizSelectedAnswer = quizSelectedAnswer;
    }

    public Boolean getRefreshApiInProgress() {
        return isRefreshApiInProgress;
    }

    public void setRefreshApiInProgress(Boolean refreshApiInProgress) {
        isRefreshApiInProgress = refreshApiInProgress;
    }

    public Boolean getCurrentLocationUpdated() {
        return isCurrentLocationUpdated;
    }

    public void setCurrentLocationUpdated(Boolean currentLocationUpdated) {
        isCurrentLocationUpdated = currentLocationUpdated;
    }

    public Boolean getHomePageRefreshNeeded() {
        return isHomePageRefreshNeeded;
    }

    public void setHomePageRefreshNeeded(Boolean homePageRefreshNeeded) {
        isHomePageRefreshNeeded = homePageRefreshNeeded;
    }

    public Boolean isHomePageRefreshNeeded = false;

    public Boolean getLocationEdited() {
        return isLocationEdited;
    }

    public void setLocationEdited(Boolean locationEdited) {
        isLocationEdited = locationEdited;
    }

    public Boolean getSelectedCropEdited() {
        return isSelectedCropEdited;
    }

    public void setSelectedCropEdited(Boolean selectedCropEdited) {
        isSelectedCropEdited = selectedCropEdited;
    }

    private Boolean isLocationEdited = false;
    private Boolean isSelectedCropEdited = false;

    public Boolean getPageRefreshNeeded() {
        return isPageRefreshNeeded;
    }

    public void setPageRefreshNeeded(Boolean pageRefreshNeeded) {
        isPageRefreshNeeded = pageRefreshNeeded;
    }

    public Boolean getMyFarmPageRefreshNeeded() {
        return isMyFarmPageRefreshNeeded;
    }

    public void setMyFarmPageRefreshNeeded(Boolean pageRefreshNeeded) {
        isMyFarmPageRefreshNeeded = pageRefreshNeeded;
    }

    public Boolean getAllFarmPageRefreshNeeded() {
        return isAllFarmPageRefreshNeeded;
    }

    public void setAllFarmPageRefreshNeeded(Boolean pageRefreshNeeded) {
        isAllFarmPageRefreshNeeded = pageRefreshNeeded;
    }

    public String getIsPageLaunched() {
        return isPageLaunched;
    }

    public void setIsPageLaunched(String isPageLaunched) {
        this.isPageLaunched = isPageLaunched;
    }

    public Boolean getBackToMain() {
        return isBackToMain;
    }

    public void setBackToMain(Boolean backToMain) {
        isBackToMain = backToMain;
    }

    public Boolean getPostListRefreshNeeded() {
        return isPostListRefreshNeeded;
    }

    public void setPostListRefreshNeeded(Boolean postListRefreshNeeded) {
        isPostListRefreshNeeded = postListRefreshNeeded;
    }

    public boolean isCommentListChanged() {
        return isCommentListChanged;
    }

    public void setCommentListChanged(boolean commentListChanged) {
        isCommentListChanged = commentListChanged;
    }

    private boolean isCommentListChanged = false;
    private Boolean isBackToMain = false;
    private String isPageLaunched = SharedPreferencesKeys.IsHomeLaunched;
    private Boolean isNavigationUpdateNeeded = false;

    public Boolean getNavigationUpdateNeeded() {
        return isNavigationUpdateNeeded;
    }

    public void setNavigationUpdateNeeded(Boolean navigationUpdateNeeded) {
        isNavigationUpdateNeeded = navigationUpdateNeeded;
    }

    public String getSharedImagePath() {
        return sharedImagePath;
    }

    public void setSharedImagePath(String sharedImagePath) {
        this.sharedImagePath = sharedImagePath;
    }

    public Boolean getFlexiUpdate() {
        return isFlexiUpdate;
    }

    public void setFlexiUpdate(Boolean flexiUpdate) {
        isFlexiUpdate = flexiUpdate;
    }

    public Boolean getFlexiUpdateInProgress() {
        return isFlexiUpdateInProgress;
    }

    public void setFlexiUpdateInProgress(Boolean flexiUpdateInProgress) {
        isFlexiUpdateInProgress = flexiUpdateInProgress;
    }

    public boolean isIsreferralApplied() {
        return isreferralApplied;
    }

    public void setIsreferralApplied(boolean isreferralApplied) {
        this.isreferralApplied = isreferralApplied;
    }

    public boolean isCampaignAvailable() {
        return campaignAvailable;
    }

    public void setCampaignAvailable(boolean campaignAvailable) {
        this.campaignAvailable = campaignAvailable;
    }

    public Bitmap getScreenshotBitmap() {
        return screenshotBitmap;
    }

    public void setScreenshotBitmap(Bitmap screenshotBitmap) {
        this.screenshotBitmap = screenshotBitmap;
    }

    public Intent getViewIntent() {
        return viewIntent;
    }

    public void setViewIntent(Intent viewIntent) {
        this.viewIntent = viewIntent;
    }

    public Intent getShareIntent() {
        return shareIntent;
    }

    public void setShareIntent(Intent shareIntent) {
        this.shareIntent = shareIntent;
    }

    public boolean isOffTokenAutoComplete() {
        return offTokenAutoComplete;
    }

    public void setOffTokenAutoComplete(boolean offTokenAutoComplete) {
        this.offTokenAutoComplete = offTokenAutoComplete;
    }

    public Intent getSplashIntent() {
        return splashIntent;
    }

    public void setSplashIntent(Intent splashIntent) {
        this.splashIntent = splashIntent;
    }

    public String getWhatsAppMessage() {
        return whatsAppMessage;
    }

    public void setWhatsAppMessage(String whatsAppMessage) {
        this.whatsAppMessage = whatsAppMessage;
    }

    public String getKbUrl() {
        return kbUrl;
    }

    public void setKbUrl(String kbUrl) {
        this.kbUrl = kbUrl;
    }

    public Boolean getFavoriteVyaparChanged() {
        return isFavoriteVyaparChanged;
    }

    public void setFavoriteVyaparChanged(Boolean favoriteVyaparChanged) {
        isFavoriteVyaparChanged = favoriteVyaparChanged;
    }

    public Boolean getFavoriteTraderChanged() {
        return isFavoriteTraderChanged;
    }

    public void setFavoriteTraderChanged(Boolean favoriteTraderChanged) {
        isFavoriteTraderChanged = favoriteTraderChanged;
    }

    public Boolean getUpdateMySellListing() {
        return updateMySellListing;
    }

    public void setUpdateMySellListing(Boolean updateMySellListing) {
        this.updateMySellListing = updateMySellListing;
    }

    public Boolean getUpdateMyBuyListing() {
        return updateMyBuyListing;
    }

    public void setUpdateMyBuyListing(Boolean updateMyBuyListing) {
        this.updateMyBuyListing = updateMyBuyListing;
    }

    public void hideSoftKeyboard(View view, Activity activity){
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
