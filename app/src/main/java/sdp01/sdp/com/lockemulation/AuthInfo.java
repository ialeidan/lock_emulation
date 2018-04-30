package sdp01.sdp.com.lockemulation;

public class AuthInfo {

    private static final String PREF_DEVICE = "sdp01.sdp.com.sdp01.user_id";
    private static final String PREF_SERVICE = "sdp01.sdp.com.sdp01.type";

    // Setters:
    public static void setDevice(String device) {
        SaveSharedPreference.setString(PREF_DEVICE, device);
    }

    public static void setService(boolean isService) {
        SaveSharedPreference.setBool(PREF_SERVICE, isService);
    }

    public static String getDevice() {
        return SaveSharedPreference.getString(PREF_DEVICE);
    }

    public static boolean getService() {
        return SaveSharedPreference.getBool(PREF_SERVICE);
    }

    public static void clearToken() {
        setDevice(null);
        setService(false);
    }

}
