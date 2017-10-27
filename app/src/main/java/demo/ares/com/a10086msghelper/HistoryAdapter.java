package demo.ares.com.a10086msghelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/10/27 09:08.
 *
 * @author ares
 */

public class HistoryAdapter extends BaseAdapter{

    private List<Message10086> list =new ArrayList<>();

    public HistoryAdapter() {
    }

    public HistoryAdapter(List<Message10086> list) {
        this.list = list;
    }

    public void addMsg(Message10086 msg){

        list.add(msg);
        notifyDataSetChanged();
    }

    public void  clear(){
        list.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message10086 msg = list.get(position);
        Holder holder ;
        if(convertView==null){
            holder = new Holder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_layout,parent,false);
            holder.statusTv=(TextView)convertView.findViewById(R.id.statusTv);
            holder.titleTv=(TextView)convertView.findViewById(R.id.titleTv);
            convertView.setTag(holder);
        }else{
            holder=(Holder)convertView.getTag();
        }
        holder.titleTv.setText(msg.getContent());
        holder.statusTv.setText(msg.isStatus()?"发送成功":"发送失败");
        return convertView;
    }

    public static class Holder{

        public TextView titleTv;
        public TextView statusTv;

    }
}
