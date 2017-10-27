package demo.ares.com.a10086msghelper.rxbus;

/**
 * Created by ares on 2017/9/12.
 */

public class MsgEvent<T> {

    private T data;

    private String mMsg;
    private int type;
    private int request;

    public MsgEvent() {
    }

    public MsgEvent(T data) {
        this.data = data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getmMsg() {
        return mMsg;
    }

    public void setmMsg(String mMsg) {
        this.mMsg = mMsg;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setRequest(int request) {
        this.request = request;
    }

    public MsgEvent(int request, int type, String msg) {
        this.type = type;
        this.mMsg = msg;

        this.request = request;
    }

    public String getMsg(){
        return mMsg;
    }
    public int getType(){
        return type;
    }
    public int getRequest(){ return request; }

    public T getData(){return data;}
}
