/*
 *  Copyright 2017 Rozdoum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package agstack.gramophone.pushnotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

import agstack.gramophone.R;
import agstack.gramophone.ui.home.view.HomeActivity;
import agstack.gramophone.ui.splash.view.SplashActivity;
import agstack.gramophone.utils.Constants;
import agstack.gramophone.utils.IntentKeys;
import agstack.gramophone.utils.SharedPreferencesHelper;
import agstack.gramophone.utils.SharedPreferencesKeys;

/**
 * Created by Raj
 */


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private static int notificationId = 0;

    private static final String POST_ID_KEY = "postId";
    private static final String AUTHOR_ID_KEY = "authorId";
    private static final String ACTION_TYPE_KEY = "actionType";
    private static final String TITLE_KEY = "title";
    private static final String BODY_KEY = "body";
    private static final String ICON_KEY = "icon";
    private static final String ACTION_TYPE_NEW_LIKE = "new_like";
    private static final String ACTION_TYPE_NEW_COMMENT = "new_comment";
    private static final String ACTION_TYPE_NEW_POST = "new_post";
    private static final String ACTION_TYPE_DEEP_LINK = "deep_link";
    private HashMap<String, String> hashMap = new HashMap<>();


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        handleRemoteMessage(remoteMessage);
    }


    private void handleRemoteMessage(RemoteMessage remoteMessage) {
        String receivedActionType = remoteMessage.getData().get(ACTION_TYPE_KEY);

        if (receivedActionType != null) {
            switch (receivedActionType) {
                case ACTION_TYPE_NEW_LIKE:
                    parseCommentOrLike(remoteMessage);
                    break;
                case ACTION_TYPE_NEW_COMMENT:
                    parseCommentOrLike(remoteMessage);
                    break;
                case ACTION_TYPE_NEW_POST:
                    handleNewPostCreatedAction(remoteMessage);
                    break;
                case ACTION_TYPE_DEEP_LINK:
                    parseDeepLink(remoteMessage);
                    break;
            }
        }
    }

    private void handleNewPostCreatedAction(RemoteMessage remoteMessage) {
//        String postAuthorId = remoteMessage.getData().get(AUTHOR_ID_KEY);
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        //Send notification for each users except author of post.
//        if (firebaseUser != null && !firebaseUser.getUid().equals(postAuthorId)) {
//            PostManager.getInstance(this.getApplicationContext()).incrementNewPostsCounter();
//        }
    }

    private void parseDeepLink(RemoteMessage remoteMessage) {
        String notificationTitle = remoteMessage.getData().get(TITLE_KEY);
        String notificationBody = remoteMessage.getData().get(BODY_KEY);
        String notificationImageUrl = remoteMessage.getData().get(ICON_KEY);
        String deep_link = remoteMessage.getData().get(Constants.PushNotificationDeepLinkKey);

        Intent backIntent = new Intent(this, HomeActivity.class);
        Intent intent = new Intent(this, SplashActivity.class);
        intent.setData(Uri.parse(deep_link));
        Bitmap bitmap = getBitmapFromUrl(notificationImageUrl);
        sendNotification(notificationTitle, notificationBody, bitmap, intent, backIntent);
    }

    private void parseCommentOrLike(RemoteMessage remoteMessage) {
        String notificationTitle = remoteMessage.getData().get(TITLE_KEY);
        String notificationBody = remoteMessage.getData().get(BODY_KEY);
        String notificationImageUrl = remoteMessage.getData().get(ICON_KEY);
        String postId = remoteMessage.getData().get(POST_ID_KEY);
        Intent backIntent = new Intent(this, HomeActivity.class);
        Intent intent = new Intent(this, SplashActivity.class);
        intent.putExtra(IntentKeys.PostIdKey, postId);
        Bitmap bitmap = getBitmapFromUrl(notificationImageUrl);

        sendNotification(notificationTitle, notificationBody, bitmap, intent, backIntent);

    }

    public Bitmap getBitmapFromUrl(String imageUrl) {
        try {
            return Glide.with(this)
                    .asBitmap()
                    .load(imageUrl)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(Constants.PushNotification.LARGE_ICONE_SIZE, Constants.PushNotification.LARGE_ICONE_SIZE)
                    .get();

        } catch (Exception e) {
            return null;
        }
    }


    private void sendNotification(String notificationTitle, String notificationBody, Bitmap bitmap, Intent intent, Intent backIntent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent;

        if (backIntent != null) {
            backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Intent[] intents = new Intent[]{backIntent, intent};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                pendingIntent = PendingIntent.getActivities(this, notificationId++, intents, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            } else
                pendingIntent = PendingIntent.getActivities(this, notificationId++, intents, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                pendingIntent = PendingIntent.getActivity(this, notificationId++, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            } else {
                pendingIntent = PendingIntent.getActivity(this, notificationId++, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            }
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder;


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        String name = "gramophone_channel";
        String id = "gramophone_channel_1"; // The user-visible name of the channel.
        String description = "gramophone_first_channel"; // The user-visible description of the channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = null;
            if (notificationManager != null) {
                mChannel = notificationManager.getNotificationChannel(id);
            }
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, name, importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(mChannel);
                }
            }

            notificationBuilder = new NotificationCompat.Builder(this, id)
                    .setAutoCancel(true)   //Automatically delete the notification
                    .setSmallIcon(R.drawable.ic_gramophone_leaf) //Notification icon
                    .setContentIntent(pendingIntent)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationBody)
                    .setLargeIcon(bitmap)
                    .setSound(defaultSoundUri);

        } else {
            notificationBuilder = new NotificationCompat.Builder(this)
                    .setAutoCancel(true)   //Automatically delete the notification
                    .setSmallIcon(R.drawable.ic_gramophone_leaf) //Notification icon
                    .setContentIntent(pendingIntent)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationBody)
                    .setLargeIcon(bitmap)
                    .setSound(defaultSoundUri);

        }
        if (notificationManager != null) {
            notificationManager.notify(notificationId++ /* ID of notification */, notificationBuilder.build());
        }
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d("Token----", token);

        SharedPreferencesHelper.initializeInstance(this);
        SharedPreferencesHelper.getInstance().putString(SharedPreferencesKeys.FirebaseTokenKey, token);
        SharedPreferencesHelper.getInstance().putBoolean(SharedPreferencesKeys.IsFirebaseTokenUpdatedKey, true);

    }

}
