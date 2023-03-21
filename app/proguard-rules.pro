# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class agstack.gramophone.ui.articles.JavaScriptInterface {
   public *;
}

 -keep class agstack.gramophone.ui.home.subcategory.SubCategoryViewModel {
     void goToMyFarm();
 }
-keepclasseswithmembers class * extends androidx.appcompat.app.AppCompatActivity {
       public void *(android.view.View);
}

-keep class agstack.gramophone.ui.home.subcategory.* { *;}
-keep class agstack.gramophone.ui.splash.model.language.*{*;}
-keep class agstack.gramophone.base.* { *;}
-keep class agstack.gramophone.data.model.* { *;}
-keep class agstack.gramophone.ui.splash.* { *;}
-keep class agstack.gramophone.ui.address.model.addressdetails.*{*;}
-keep class agstack.gramophone.ui.address.model.googleapiresponse.*{*;}
-keep class agstack.gramophone.ui.address.model.*{*;}
-keep class agstack.gramophone.ui.advisory.models.advisory.*{*;}
-keep class agstack.gramophone.ui.advisory.models.cropproblems.*{*;}
-keep class agstack.gramophone.ui.advisory.models.recomondedproducts.*{*;}
-keep class agstack.gramophone.ui.articles.JavaScriptInterface.* {*;}
-keep class agstack.gramophone.ui.cart.model.* {*;}
-keep class agstack.gramophone.ui.comments.model.* {*;}
-keep class agstack.gramophone.ui.comments.model.sendcomment.* {*;}
-keep class agstack.gramophone.ui.createnewpost.model.* {*;}
-keep class agstack.gramophone.ui.createnewpost.model.create* {*;}
-keep class agstack.gramophone.ui.createnewpost.model.hashtags* {*;}
-keep class agstack.gramophone.ui.createnewpost.model.problems* {*;}
-keep class agstack.gramophone.ui.dialog.filter.* {*;}
-keep class agstack.gramophone.ui.faq.model.* {*;}
-keep class agstack.gramophone.ui.farm.model.* {*;}
-keep class agstack.gramophone.ui.farm.model.addfarm.* {*;}
-keep class agstack.gramophone.ui.farm.model.unit.* {*;}
-keep class agstack.gramophone.ui.favourite.model.* {*;}
-keep class agstack.gramophone.ui.favourite.model.favouritecount.* {*;}
-keep class agstack.gramophone.ui.followings.model.* {*;}
-keep class agstack.gramophone.ui.home.model.* {*;}
-keep class agstack.gramophone.ui.home.featured.ratingeligibility.* {*;}
-keep class agstack.gramophone.ui.home.product.model.* {*;}
-keep class agstack.gramophone.ui.home.product.test.* {*;}
-keep class agstack.gramophone.ui.home.subcategory.model.* {*;}
-keep class agstack.gramophone.ui.language.model.* {*;}
-keep class agstack.gramophone.ui.language.model.languagelist.* {*;}
-keep class agstack.gramophone.ui.login.model.* {*;}
-keep class agstack.gramophone.ui.notification.model.* {*;}
-keep class agstack.gramophone.ui.notification.model.cropdetails.* {*;}
-keep class agstack.gramophone.ui.notification.model.cropproblem.* {*;}
-keep class agstack.gramophone.ui.notification.model.shopbycat.* {*;}
-keep class agstack.gramophone.ui.notification.model.shopbystore.* {*;}
-keep class agstack.gramophone.ui.offerslist.model.* {*;}
-keep class agstack.gramophone.ui.order.model.* {*;}
-keep class agstack.gramophone.ui.orderdetails.model.* {*;}
-keep class agstack.gramophone.ui.othersporfile.model.* {*;}
-keep class agstack.gramophone.ui.postdetails.model.* {*;}
-keep class agstack.gramophone.ui.profile.model.* {*;}
-keep class agstack.gramophone.ui.profileselection.model.* {*;}
-keep class agstack.gramophone.ui.referandearn.model.* {*;}
-keep class agstack.gramophone.ui.search.model.* {*;}
-keep class agstack.gramophone.ui.settings.model.* {*;}
-keep class agstack.gramophone.ui.settings.model.blockedusers.* {*;}
-keep class agstack.gramophone.ui.settings.model.blockmodels.* {*;}
-keep class agstack.gramophone.ui.tagandmention.* {*;}
-keep class agstack.gramophone.ui.tv.model.* {*;}
-keep class agstack.gramophone.ui.userprofile.model.* {*;}
-keep class agstack.gramophone.ui.userprofile.verifyotp.model.* {*;}
-keep class agstack.gramophone.ui.verifyotp.model.* {*;}
-keep class agstack.gramophone.ui.weather.model.* {*;}
-keep class agstack.gramophone.ui.home.view.model.* {*;}
-keep class agstack.gramophone.ui.home.view.fragments.market.model.* {*;}
-keep class agstack.gramophone.ui.home.view.fragments.market.model.sku.* {*;}
-keep class agstack.gramophone.ui.home.view.fragments.gramophone.model.* {*;}
-keep class agstack.gramophone.ui.home.view.fragments.community.model.* {*;}
-keep class agstack.gramophone.ui.home.view.fragments.community.model.likes.* {*;}
-keep class agstack.gramophone.ui.home.view.fragments.community.model.pin.* {*;}
-keep class agstack.gramophone.ui.home.view.fragments.community.model.quiz.* {*;}
-keep class agstack.gramophone.ui.home.view.fragments.community.model.reportpost.* {*;}
-keep class agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.* {*;}
-keep class agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.block.* {*;}
-keep class agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.follow.* {*;}
-keep class agstack.gramophone.utils.ApiResponse.* {*;}
-keep class agstack.gramophone.utils.* {*;}




-keep class android.animation.Animator { *; }
-keep class android.animation.AnimatorListenerAdapter { *; }
-keep class android.animation.AnimatorSet { *; }
-keep class android.animation.ObjectAnimator { *; }
-keep class android.animation.ValueAnimator { *; }
-keep class android.annotation.SuppressLint { *; }
-keep class android.annotation.TargetApi { *; }
-keep class android.app.Activity { *; }
-keep class android.app.ActivityOptions { *; }
-keep class android.app.AlertDialog { *; }
-keep class android.app.Application { *; }
-keep class android.app.Dialog { *; }
-keep class android.app.DialogFragment { *; }
-keep class android.app.Fragment { *; }
-keep class android.app.FragmentManager { *; }
-keep class android.app.NotificationChannel { *; }
-keep class android.app.NotificationManager { *; }
-keep class android.app.PendingIntent { *; }
-keep class android.app.ProgressDialog { *; }
-keep class android.app.SearchManager { *; }
-keep class android.app.Service { *; }

-keep class android.arch.lifecycle.Lifecycle { *; }
-keep class android.arch.lifecycle.LifecycleOwner { *; }
-keep class android.arch.lifecycle.MutableLiveData { *; }
-keep class android.arch.lifecycle.Observer { *; }
-keep class android.arch.lifecycle.ViewModel { *; }
-keep class android.arch.lifecycle.ViewModelProviders { *; }
-keep class android.content.BroadcastReceiver { *; }
-keep class android.content.ClipData { *; }
-keep class android.content.ComponentName { *; }
-keep class android.content.ContentResolver { *; }
-keep class android.content.Context { *; }
-keep class android.content.DialogInterface { *; }
-keep class android.content.Intent { *; }
-keep class android.content.IntentFilter { *; }
-keep class android.content.pm.PackageManager { *; }
-keep class android.content.res.Configuration { *; }
-keep class android.content.res.Resources { *; }
-keep class android.content.res.TypedArray { *; }
-keep class android.content.ServiceConnection { *; }
-keep class android.content.SharedPreferences { *; }
-keep class android.database.Cursor { *; }
-keep class android.graphics.Bitmap { *; }
-keep class android.graphics.BitmapFactory { *; }
-keep class android.graphics.BitmapShader { *; }
-keep class android.graphics.Canvas { *; }
-keep class android.graphics.Color { *; }
-keep class android.graphics.drawable.AnimationDrawable { *; }
-keep class android.graphics.drawable.BitmapDrawable { *; }
-keep class android.graphics.drawable.ColorDrawable { *; }
-keep class android.graphics.drawable.Drawable { *; }
-keep class android.graphics.drawable.VectorDrawable { *; }
-keep class android.graphics.Matrix { *; }
-keep class android.graphics.Paint { *; }
-keep class android.graphics.Path { *; }
-keep class android.graphics.Point { *; }
-keep class android.graphics.PointF { *; }
-keep class android.graphics.PorterDuff { *; }
-keep class android.graphics.Rect { *; }
-keep class android.graphics.RectF { *; }
-keep class android.graphics.Shader { *; }
-keep class android.graphics.Typeface { *; }
-keep class android.location.Address { *; }
-keep class android.location.Geocoder { *; }
-keep class android.location.Location { *; }
-keep class android.location.LocationListener { *; }
-keep class android.location.LocationManager { *; }
-keep class android.Manifest { *; }
-keep class android.Manifest.permission { *; }

-keep class android.media.RingtoneManager { *; }
-keep class android.net.ConnectivityManager { *; }
-keep class android.net.NetworkInfo { *; }
-keep class android.net.Uri { *; }
-keep class android.os.AsyncTask { *; }
-keep class android.os.Binder { *; }
-keep class android.os.Build { *; }
-keep class android.os.Build.VERSION { *; }
-keep class android.os.Build.VERSION_CODES { *; }
-keep class android.os.Bundle { *; }
-keep class android.os.CountDownTimer { *; }
-keep class android.os.DeadObjectException { *; }
-keep class android.os.Environment { *; }
-keep class android.os.Handler { *; }
-keep class android.os.IBinder { *; }
-keep class android.os.Parcel { *; }
-keep class android.os.Parcelable { *; }
-keep class android.provider.ContactsContract { *; }
-keep class android.provider.MediaStore { *; }
-keep class android.provider.Settings { *; }

-keep class android.text.Editable { *; }
-keep class android.text.format.DateFormat { *; }
-keep class android.text.format.DateUtils { *; }
-keep class android.text.Html { *; }
-keep class android.text.Spannable { *; }
-keep class android.text.SpannableStringBuilder { *; }
-keep class android.text.Spanned { *; }
-keep class android.text.style.ForegroundColorSpan { *; }
-keep class android.text.style.TextAppearanceSpan { *; }
-keep class android.text.TextUtils { *; }
-keep class android.text.TextWatcher { *; }
-keep class android.transition.Transition { *; }
-keep class android.util.AttributeSet { *; }
-keep class android.util.Base64 { *; }
-keep class android.util.DisplayMetrics { *; }
-keep class android.util.Log { *; }
-keep class android.util.Pair { *; }
-keep class android.util.SparseBooleanArray { *; }
-keep class android.view.animation.AccelerateDecelerateInterpolator { *; }
-keep class android.view.animation.AccelerateInterpolator { *; }
-keep class android.view.animation.AlphaAnimation { *; }
-keep class android.view.animation.Animation { *; }
-keep class android.view.animation.AnimationUtils { *; }
-keep class android.view.animation.BounceInterpolator { *; }
-keep class android.view.animation.Transformation { *; }
-keep class android.view.Display { *; }
-keep class android.view.GestureDetector { *; }
-keep class android.view.inputmethod.EditorInfo { *; }
-keep class android.view.inputmethod.InputMethodManager { *; }
-keep class android.view.KeyEvent { *; }
-keep class android.view.LayoutInflater { *; }
-keep class android.view.Menu { *; }
-keep class android.view.MenuInflater { *; }
-keep class android.view.MenuItem { *; }
-keep class android.view.MotionEvent { *; }
-keep class android.view.ScaleGestureDetector { *; }
-keep class android.view.View { *; }
-keep class android.view.View.OnClickListener { *; }
-keep class android.view.ViewGroup { *; }
-keep class android.view.ViewPropertyAnimator { *; }
-keep class android.view.ViewTreeObserver { *; }
-keep class android.view.Window { *; }
-keep class android.view.WindowManager { *; }
-keep class android.webkit.JavascriptInterface { *; }
-keep class android.webkit.WebResourceError { *; }
-keep class android.webkit.WebResourceRequest { *; }
-keep class android.webkit.WebSettings { *; }
-keep class android.webkit.WebView { *; }
-keep class android.webkit.WebViewClient { *; }
-keep class android.widget.* { *; }
-keep class android.widget.AdapterView { *; }
-keep class android.widget.ArrayAdapter { *; }
-keep class android.widget.BaseExpandableListAdapter { *; }
-keep class android.widget.Button { *; }
-keep class android.widget.CheckBox { *; }
-keep class android.widget.CheckedTextView { *; }
-keep class android.widget.CompoundButton { *; }
-keep class android.widget.EditText { *; }
-keep class android.widget.ExpandableListView { *; }
-keep class android.widget.FrameLayout { *; }
-keep class android.widget.ImageButton { *; }
-keep class android.widget.ImageView { *; }
-keep class android.widget.LinearLayout { *; }
-keep class android.widget.ListView { *; }
-keep class android.widget.OverScroller { *; }
-keep class android.widget.PopupMenu { *; }
-keep class android.widget.ProgressBar { *; }
-keep class android.widget.RadioButton { *; }
-keep class android.widget.RadioGroup { *; }
-keep class android.widget.RelativeLayout { *; }
-keep class android.widget.Scroller { *; }
-keep class android.widget.ScrollView { *; }
-keep class android.widget.SearchView { *; }
-keep class android.widget.Spinner { *; }
-keep class android.widget.TextView { *; }
-keep class android.widget.Toast { *; }
-keep class android.widget.Toolbar { *; }

-keep class org.jetbrains.annotations.NotNull { *; }
-keep class org.jetbrains.annotations.Nullable { *; }
-keep class org.json.JSONArray { *; }
-keep class org.json.JSONException { *; }
-keep class org.json.JSONObject { *; }
-keep class org.w3c.dom.Text { *; }

-keep class retrofit2.Call { *; }
-keep class retrofit2.Callback { *; }
-keep class retrofit2.converter.gson.GsonConverterFactory { *; }
-keep class retrofit2.http.Body { *; }
-keep class retrofit2.http.POST { *; }
-keep class retrofit2.http.Url { *; }
-keep class retrofit2.Response { *; }
-keep class retrofit2.Retrofit { *; }



# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile