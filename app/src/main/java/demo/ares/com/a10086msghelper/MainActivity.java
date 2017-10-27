package demo.ares.com.a10086msghelper;

import android.Manifest;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import demo.ares.com.a10086msghelper.rxbus.MsgEvent;
import demo.ares.com.a10086msghelper.rxbus.RxBus;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static demo.ares.com.a10086msghelper.SMSSendResultReceiver.MSG_CONTENT;
import static demo.ares.com.a10086msghelper.SMSSendResultReceiver.PHONE_NUM;
import static demo.ares.com.a10086msghelper.SMSSendResultReceiver.SENT_SMS_ACTION;

public class MainActivity extends AppCompatActivity {
    SMSSendResultReceiver receiver;

    EditText commandEt;
    Button sendBtn;
    Button clearBtn;
    EditText targetEt;
    ListView historyLV;
    HistoryAdapter historyAdapter;


//    private String[] msgList = {"CXYE", "CXZD 201706", "CXZE", "CXJF", "CXSJLL", "KTWLAN", "QXWLAN",
//            "KTSJLL5", "KTYU", "QXYU","10086","0000"};

    private String[] msgList = {"CXYE", "CXTC", "CXZE", "CXJF", "CXSJLL"};

    private  String commandList[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        commandEt = (EditText) findViewById(R.id.commandEt);
        targetEt = (EditText) findViewById(R.id.targetEt);
        targetEt.setSelection(targetEt.length());
        sendBtn = (Button) findViewById(R.id.sendBtn);
        clearBtn = (Button) findViewById(R.id.clearBtn);
        historyLV=(ListView)findViewById(R.id.historyListView);
        historyAdapter=new HistoryAdapter();
        historyLV.setAdapter(historyAdapter);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < msgList.length; i++) {
            String c = msgList[i];
            if (i == msgList.length - 1) {
                stringBuilder.append(c);

            } else {
                stringBuilder.append(c + ",");
            }

        }
        commandEt.setText(stringBuilder);
        commandEt.setSelection(stringBuilder.length());


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String phone =targetEt.getText().toString();
                String content = commandEt.getText().toString();

                if(TextUtils.isEmpty(content)){

                    showDialog("短信内容不能为空");
                    return;
                }
                if(TextUtils.isEmpty(phone)){

                    showDialog("号码不能为空");
                    return;
                }
                historyAdapter.clear();
                sendBtn.setText("指令列表正在发送中...");
                sendBtn.setEnabled(false);
                String commandList[] = content.split(",");
                MainActivity.this.commandList= commandList;
                Observable.fromArray(commandList).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(@NonNull String msg) throws Exception {

                                new RxPermissions(MainActivity.this).request(Manifest.permission.SEND_SMS).subscribe(new Consumer<Boolean>() {
                                    @Override
                                    public void accept(@io.reactivex.annotations.NonNull Boolean aBoolean) throws Exception {


                                    }
                                });
                                try{
                                    sendSMS(phone,msg);

                                }catch (SecurityException e){
                                    Toast.makeText(MainActivity.this,"没有发送短信的权限",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyAdapter.clear();
            }
        });


        receiver = new SMSSendResultReceiver();
        IntentFilter filter = new IntentFilter(SENT_SMS_ACTION);
        registerReceiver(receiver, filter);

        new RxPermissions(this).request(Manifest.permission.SEND_SMS).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Boolean aBoolean) throws Exception {


            }
        });
        RxBus.getInstance().register(MsgEvent.class).subscribe(new Consumer<MsgEvent>() {
            @Override
            public void accept(@NonNull MsgEvent msgEvent) throws Exception {



                if(msgEvent.getData() instanceof Message10086){
                    Message10086 msg = (Message10086)msgEvent.getData();
                    historyAdapter.addMsg(msg);
                    if( historyAdapter.getCount()==MainActivity.this.commandList.length){

                        sendBtn.setText("开始发送");
                        sendBtn.setEnabled(true);
                        showDialog("指令发送完毕");
                    }
                }


            }
        });
    }


    public void sendSMS(String phoneNumber, String message) {
        //获取短信管理器
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        //拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(message);
        Intent itSend = new Intent(SENT_SMS_ACTION);
        itSend.putExtra(PHONE_NUM, phoneNumber);
        itSend.putExtra(MSG_CONTENT, message);
        PendingIntent sentPI = PendingIntent.getBroadcast(getApplicationContext(), 0, itSend, PendingIntent.FLAG_UPDATE_CURRENT);
        for (String text : divideContents) {
            smsManager.sendTextMessage(phoneNumber, null, text, sentPI, null);
        }

    }

    public void showDialog(String msg){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();

    }

}
