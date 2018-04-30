package sdp01.sdp.com.lockemulation;

import android.app.Application;
import android.content.Context;

import com.androidnetworking.AndroidNetworking;

/**
 * Created by ibrahimaleidan on 20/02/2018.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
