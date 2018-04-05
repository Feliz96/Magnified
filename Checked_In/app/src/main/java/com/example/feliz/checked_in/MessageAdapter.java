package com.example.feliz.checked_in;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;



import java.util.List;

/**
 * Created by Feliz on 2017/10/03.
 */

public class MessageAdapter extends BaseAdapter
{
    private List<MessageModel> msg;
    private Context context;

    public MessageAdapter(List<MessageModel> msg, Context context) {
        this.msg = msg;
        this.context = context;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private LayoutInflater layoutInflater;


    @Override
    public int getCount() {
        return msg.size();
    }

    @Override
    public Object getItem(int position) {
        return msg.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       View view = convertView;
        if(view ==null)
        {
            if(msg.get(position).send)
            {
                view = layoutInflater.inflate(R.layout.list_item_message,null,false);
            }

//            BubbleTextView textmsg = (BubbleTextView)view.findViewById(R.id.inbox);
  //          textmsg.setText(msg.get(position).message);
        }
    return view;
    }
}
