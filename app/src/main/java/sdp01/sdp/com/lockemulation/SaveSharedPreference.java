package sdp01.sdp.com.lockemulation;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class SaveSharedPreference extends Application {

    private static Context context = MyApplication.getAppContext();

    private static final String PREF= "sdp01.sdp.com.sdp01";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return ctx.getSharedPreferences(
                PREF, Context.MODE_PRIVATE);
    }

    public static void setString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setBool(String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static String getString(String key) {
        return getSharedPreferences(context).getString(key, "");
    }

    public static boolean getBool(String key) {
        return getSharedPreferences(context).getBoolean(key, false);
    }
}
