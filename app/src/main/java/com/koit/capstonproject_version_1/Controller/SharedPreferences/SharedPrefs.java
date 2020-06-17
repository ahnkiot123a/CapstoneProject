package com.koit.capstonproject_version_1.Controller.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.koit.capstonproject_version_1.Model.User;


public class SharedPrefs {

    private static final String PREFS_NAME = "share_prefs";
    private static SharedPrefs mInstance;
    private SharedPreferences mSharedPreferences;

    private SharedPrefs() {
        mSharedPreferences = App.self().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPrefs getInstance() {
        if (mInstance == null) {
            mInstance = new SharedPrefs();
        }
        return mInstance;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> anonymousClass) {
        if (anonymousClass == String.class) {
            return (T) mSharedPreferences.getString(key, "");
        } else if (anonymousClass == Boolean.class) {
            return (T) Boolean.valueOf(mSharedPreferences.getBoolean(key, false));
        } else if (anonymousClass == Float.class) {
            return (T) Float.valueOf(mSharedPreferences.getFloat(key, 0));
        } else if (anonymousClass == Integer.class) {
            return (T) Integer.valueOf(mSharedPreferences.getInt(key, 0));
        } else if (anonymousClass == Long.class) {
            return (T) Long.valueOf(mSharedPreferences.getLong(key, 0));
        }
        return null;
    }


    public <T> void put(String key, T data) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if (data instanceof String) {
            editor.putString(key, (String) data);
        } else if (data instanceof Boolean) {
            editor.putBoolean(key, (Boolean) data);
        } else if (data instanceof Float) {
            editor.putFloat(key, (Float) data);
        } else if (data instanceof Integer) {
            editor.putInt(key, (Integer) data);
        } else if (data instanceof Long) {
            editor.putLong(key, (Long) data);
        }
        editor.apply();
    }

    public void putCurrentUser(String key, User user) {
        String sObject = convertUserToString(user);
        put(key, sObject);
    }

    public User getCurrentUser(String key) {

        String getStringUser = get(key, String.class);
        User user = convertStringToUser(getStringUser);
        return user;
    }

    private User convertStringToUser(String getStringUser) {
        String[] result = getStringUser.split("[|]");
        Log.i("result1", result[0]);
        User user = null;
        if (result.length == 10) {
            user = new User();
            user.setFullName(result[0]);
            user.setAddress(result[1]);
            user.setEmail(result[2]);
            user.setStoreName(result[3]);
            user.setDateOfBirth(result[4]);
            user.setPhoneNumber(result[5]);
            user.setPassword(result[6]);
            user.setRoleID(result[7]);
            if (result[8].equalsIgnoreCase("true")) {
                user.setHasFingerprint(true);
            } else {
                user.setHasFingerprint(false);
            }
            if (result[9].equalsIgnoreCase("true")) {
                user.setGender(true);
            } else {
                user.setGender(false);
            }
        }

        return user;
    }

    private String convertUserToString(User user) {
        String result = user.getFullName() + "|" +
                user.getAddress() + "|" +
                user.getEmail() + " |" +
                user.getStoreName() + "|" +
                user.getDateOfBirth() + "|" +
                user.getPhoneNumber() + "|" +
                user.getPassword() + "|" +
                user.getRoleID() + "|" +
                user.isHasFingerprint() + "|" +
                user.isGender();
        Log.i("result", result);
        return result;
    }


    public void clear() {
        mSharedPreferences.edit().clear().commit();
    }
}
