package sdp01.sdp.com.lockemulation;

import android.location.Location;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class Networking {

    private final static String URL = "https://scala-sdp.herokuapp.com/";

    public static void sendLock(final String status, final DataSourceRequestListener listener) {
        String device_id = AuthInfo.getDevice();

        if (device_id.isEmpty()) {
            listener.onError(new ErrorCode(-1, "NOT_AUTHENTICATED", ""));
            return;
        }


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_id", device_id);
            jsonObject.put("status", status);
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onError(new ErrorCode(-143, "Error Sending Status", e.getMessage()));
            return;
        }


        AndroidNetworking.post(URL + "lock/status")
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onResponse(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        JSONObject error;
                        String code;

                        try {
                            error = new JSONObject(anError.getErrorBody());

                            code = error.getString("message");

                            if (code.equals("NOT_AUTHENTICATED")) {
                                listener.onError(new ErrorCode(-1, "NOT_AUTHENTICATED", ""));
                            } else {
                                listener.onError(new ErrorCode(-143, "Error Sending Status", ""));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onError(new ErrorCode(-143, "Error Sending Status", anError.getErrorBody().toString()));
                        }
                    }
                });
    }

    public static void sendLocation(final String status, final Location location, final DataSourceRequestListener listener) {
        String device_id = AuthInfo.getDevice();

        if (device_id.isEmpty()) {
            listener.onError(new ErrorCode(-1, "NOT_AUTHENTICATED", ""));
            return;
        }


        JSONObject locationJSON = new JSONObject();
        try {
            locationJSON.put("latitude", Double.toString(location.getLatitude()));
            locationJSON.put("longitude", Double.toString(location.getLongitude()));
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onError(new ErrorCode(-103, "Error Sending Request", e.getMessage()));
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_id", device_id);
            jsonObject.put("status", status);
            jsonObject.put("location", locationJSON);
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onError(new ErrorCode(-143, "Error Sending Status", e.getMessage()));
            return;
        }


        AndroidNetworking.post(URL + "lock/location")
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onResponse(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        JSONObject error;
                        String code;

                        try {
                            error = new JSONObject(anError.getErrorBody());

                            code = error.getString("message");

                            if (code.equals("NOT_AUTHENTICATED")) {
                                listener.onError(new ErrorCode(-1, "NOT_AUTHENTICATED", anError.getErrorBody().toString()));
                            } else {
                                listener.onError(new ErrorCode(-143, "Error Sending Status", anError.getErrorBody().toString()));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onError(new ErrorCode(-143, "Error Sending Status", e.getMessage()));
                        }
                    }
                });
    }

    public static void endService(final String status, final DataSourceRequestListener listener) {
        String device_id = AuthInfo.getDevice();

        if (device_id.isEmpty()) {
            listener.onError(new ErrorCode(-1, "NOT_AUTHENTICATED", ""));
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_id", device_id);
            jsonObject.put("status", status);
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onError(new ErrorCode(-143, "Error Sending Status", e.getMessage()));
            return;
        }


        AndroidNetworking.post(URL + "lock/finish")
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onResponse(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        JSONObject error;
                        String code;

                        try {
                            error = new JSONObject(anError.getErrorBody());

                            code = error.getString("message");

                            if (code.equals("NOT_AUTHENTICATED")) {
                                listener.onError(new ErrorCode(-1, "NOT_AUTHENTICATED", ""));
                            } else {
                                listener.onError(new ErrorCode(-143, "Error Sending Status", ""));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onError(new ErrorCode(-143, "Error Sending Status", e.getMessage()));
                        }
                    }
                });
    }



}
