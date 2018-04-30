package sdp01.sdp.com.lockemulation;

import org.json.JSONObject;

/**
 * Created by ibrahimaleidan on 11/03/2018.
 */

public interface DataSourceRequestListener {

    void onResponse(JSONObject response);

    void onError(ErrorCode anError);

}
