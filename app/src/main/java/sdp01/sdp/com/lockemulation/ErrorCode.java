package sdp01.sdp.com.lockemulation;

/**
 * Created by ibrahimaleidan on 11/03/2018.
 */

public class ErrorCode {

    public int code;
    public String info;
    public String comment;

    public ErrorCode(int code, String info, String comment){
        this.code = code;
        this.info = info;
        this.comment = comment;
    }

}
