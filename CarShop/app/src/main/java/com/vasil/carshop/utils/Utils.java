package com.vasil.carshop.utils;

import static com.vasil.carshop.utils.Constants.SHARED_PREF_USER;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.vasil.carshop.responses.users.UserGetResponse;

public class Utils {

    public static String getStringResource(Context context, String resource) {

        try {
            int resId = context.getResources().getIdentifier(resource, "string", context.getPackageName());

            return context.getString(resId);

        } catch (Exception ignored) {
//            Log.e("ERROR", "Some error: ", e);
        }
        return "";
    }

    public static Boolean isLoggedIn(SharedPreferences pref) {

        UserGetResponse user = getLoggedUser(pref);

        if (user == null) {
            return false;
        }

        return user.getUserId() != null;
    }

    public static UserGetResponse getLoggedUser(SharedPreferences pref) {

        Gson gson = new Gson();
        return gson.fromJson(pref.getString(SHARED_PREF_USER, null), UserGetResponse.class);
    }
}
