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

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

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


    }


    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        SharedPreferencesHelper.initializeInstance(this);
        SharedPreferencesHelper.getInstance().putString(SharedPreferencesKeys.FirebaseTokenKey, token);
        SharedPreferencesHelper.getInstance().putBoolean(SharedPreferencesKeys.IsFirebaseTokenUpdatedKey, true);

    }

}
