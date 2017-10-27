package demo.ares.com.a10086msghelper;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import demo.ares.com.a10086msghelper.rxbus.MsgEvent;
import demo.ares.com.a10086msghelper.rxbus.RxBus;

/**
 * Created on 2017/10/27 00:22.
 *
 * @author ares
 */

public class SMSSendResultReceiver extends BroadcastReceiver {
    public static final String SENT_SMS_ACTION = "demo_sms_send_action";
    public static final String PHONE_NUM = "PHONE_NUM";
    public static final String MSG_CONTENT = "MSG_CONTENT";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (SENT_SMS_ACTION.equals(intent.getAction())) {
            String phoneNum = intent.getStringExtra(PHONE_NUM);
            String msg = intent.getStringExtra(MSG_CONTENT);
            switch(getResultCode())
            {
                case Activity.RESULT_OK:
                    // 发送成功
                    MsgHistoryRecord.getInstance().addSuccessMsg(msg);
                    MsgEvent<Message10086> event = new MsgEvent<Message10086>();
                    Message10086 obj = new Message10086();
                    obj.setStatus(true);
                    obj.setContent(msg);
                    event.setData(obj);
                    RxBus.getInstance().post(event);
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                case SmsManager.RESULT_ERROR_NULL_PDU:
                default:
                    // 发送失败
                    MsgHistoryRecord.getInstance().addFailMsg(msg);

                    MsgEvent<Message10086> event2 = new MsgEvent<Message10086>();
                    Message10086 obj2 = new Message10086();
                    obj2.setStatus(false);
                    obj2.setContent(msg);
                    event2.setData(obj2);
                    RxBus.getInstance().post(event2);
                    break;
            }
        }
    }
}
