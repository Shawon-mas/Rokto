package com.app.roktoDorkar.utilites;

import java.util.HashMap;

public class Constants {
    public static final String KEY_COLLECTION_USERS="users";
    public static final String KEY_NAME="name";
    public static final String KEY_NUMBER="number";
    public static final String KEY_EMAIL="email";
    public static final String KEY_DOB="date of birth";
    public static final String KEY_AGE="age";
    public static final String KEY_UID="uId";
    public static final String KEY_DIVISION="division";
    public static final String KEY_DISTRICT="district";
    public static final String KEY_UPZILA="upazila";
    public static final String KEY_ABOUT="about";
    public static final String KEY_BLOODTYPE="bloodType";

    public static final String KEY_IMAGE_URI="imageUri";
    public static final String KEY_IS_SIGNED_IN="isSignedId";
    public static final String KEY_IS_FIRST_TIME="isFirstTime";
    public static final String KEY_USER_ID="userID";
    public static final String KEY_PREFERENCE_NAME="AppPreference";
    public static final String KEY_FCM_TOKEN="fcmToken";
    public static final String KEY_COLLECTION_CHAT="chat";
    public static final String KEY_SENDER_ID="senderId";
    public static final String KEY_RECEIVER_ID="receiverId";
    public static final String KEY_MESSAGE="message";
    public static final String KEY_TIMESTAMP="timestamp";
    public static final String KEY_AVAILABILITY="availability";
    public static final String KEY_SERVER_KEY="AAAA_d-ovxo:APA91bH9bJ5DdSa7baKIIYerLr5Zj3n0xy9Oqv9vMoVHtyqeHgAGEKn1zvdZlvHrGqd_lAFzbj12nb9a-I8ENnN7Sl_qafdU38qP3xTb-y7KLuD3fDFO7DK-sQGwOwIIfRW4Cu1_hoep";
    public static final String REMOTE_MESSAGE_AUTHORIZATION="Authorization";
    public static final String REMOTE_MESSAGE_CONTENT_TYPE="Content-Type";
    public static final String REMOTE_MESSAGE_DATA="data";
    public static final String REMOTE_MESSAGE_REGISTRATION_IDS="registration_ids";

    public static HashMap<String,String> remoteMsgHeaders=null;
    public static HashMap<String,String> getRemoteMsgHeaders()
        {
        if (remoteMsgHeaders==null){
            remoteMsgHeaders=new HashMap<>();
            remoteMsgHeaders.put(
                    REMOTE_MESSAGE_AUTHORIZATION,
                    "key=AAAA_d-ovxo:APA91bH9bJ5DdSa7baKIIYerLr5Zj3n0xy9Oqv9vMoVHtyqeHgAGEKn1zvdZlvHrGqd_lAFzbj12nb9a-I8ENnN7Sl_qafdU38qP3xTb-y7KLuD3fDFO7DK-sQGwOwIIfRW4Cu1_hoep"
            );
            remoteMsgHeaders.put(
                    REMOTE_MESSAGE_CONTENT_TYPE,
                    "application/json"
            );
        }
        return remoteMsgHeaders;
    }


}
