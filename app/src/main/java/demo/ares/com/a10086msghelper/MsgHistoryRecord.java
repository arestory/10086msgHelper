package demo.ares.com.a10086msghelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/10/27 01:19.
 *
 * @author ares
 */

public class MsgHistoryRecord {


    public  List<String> msgSuccessList = new ArrayList<>();
    public  List<String> msgFailList = new ArrayList<>();
    private static MsgHistoryRecord mMsgHistoryRecord = null;

    private MsgHistoryRecord() {
    }

    public static MsgHistoryRecord getInstance() {

        if (mMsgHistoryRecord == null) {
            synchronized (MsgHistoryRecord.class) {
                if (mMsgHistoryRecord == null) {
                    mMsgHistoryRecord = new MsgHistoryRecord();
                }
            }
        }
        return mMsgHistoryRecord;
    }

    public void addSuccessMsg(String msg){
        msgSuccessList.add(msg);
    }
    public void addFailMsg(String msg){
        msgFailList.add(msg);
    }
    public void removeRecord(){
        msgSuccessList.clear();
        msgFailList.clear();
    }
}
