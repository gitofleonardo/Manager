package com.example.leo.manager;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MsgItemDetail extends AppCompatActivity {
    private String number;
    private EditText inputText;
    private Button sendButton;
    private RecyclerView recyclerView;
    private MsgInterfaceAdapter msgInterfaceAdapter;
    private List<MsgItem> msgItemList=new ArrayList<>();
    private Button back;
    private Toolbar toolbar;
    private TextView textView_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_item_detail);
        Intent from=getIntent();
        number=from.getStringExtra("NUMBER");
        recyclerView=(RecyclerView)findViewById(R.id.msg_interface_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        msgInterfaceAdapter=new MsgInterfaceAdapter(msgItemList);
        recyclerView.setAdapter(msgInterfaceAdapter);
        inputText=(EditText)findViewById(R.id.msg_interface_edit_text);
        inputText.setText("");
        sendButton=(Button)findViewById(R.id.msg_interface_send);
        back=(Button)findViewById(R.id.msg_interface_back_item);
        toolbar=(Toolbar)findViewById(R.id.msg_interface_toolbar);
        textView_number=(TextView)findViewById(R.id.msg_item_detail_number);
        textView_number.setText(number);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputText.getText().toString().equals("")){

                }
                else{
                Date date=new Date();
                long dateLong=date.getTime();
                new SendAMsg(number,inputText.getText().toString(),MsgItemDetail.this).send();
                MsgItem m=new MsgItem(inputText.getText().toString(),number,dateLong,R.drawable.contracticon,2);
                msgItemList.add(m);
                msgInterfaceAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(msgItemList.size()-1);
                inputText.setText("");
                }
            }
        });
        getMsg();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MsgItemDetail.this.finish();
            }
        });
    }

    /**
     * 获取信息内容
     */
    private void getMsg(){
        MsgItem msgItem=null;
        Cursor msgCursor=null;
        List<MsgItem> list=new ArrayList<>();
        Uri uri=Uri.parse("content://sms/");
        ContentResolver contentResolver=getContentResolver();
        msgCursor=contentResolver.query(uri,null,null,null,null);
        if (msgCursor!=null){
            while (msgCursor.moveToNext()){
                String number_temp=msgCursor.getString(msgCursor.getColumnIndex("address"));
                if (number.equals(number_temp)){
                    String content=msgCursor.getString(msgCursor.getColumnIndex("body"));
                    String name=msgCursor.getString(msgCursor.getColumnIndex("person"));
                    long date=msgCursor.getLong(msgCursor.getColumnIndex("date"));
                    int type=msgCursor.getInt(msgCursor.getColumnIndex("type"));
                    msgItem=new MsgItem(content,number,date,R.drawable.msgicon,type);
                    list.add(msgItem);
                }
            }
        }
        ReverseList<MsgItem> msgItemReverseList=new ReverseList<MsgItem>(list);
        for (MsgItem msgItem1:msgItemReverseList.reversed()){
            msgItemList.add(msgItem1);
        }
        recyclerView.scrollToPosition(msgItemList.size()-1);
        msgInterfaceAdapter.notifyDataSetChanged();
    }
}
