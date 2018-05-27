package com.example.leo.manager;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ContactDetail extends AppCompatActivity {
    private String NAME;
    private String NUMBER;
    private List<String> list;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        ListView listView=(ListView)findViewById(R.id.contact_detail_list);
        list=new ArrayList<>();
        adapter=new ArrayAdapter<String>(ContactDetail.this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
        Intent intent=getIntent();
        NAME=intent.getStringExtra("NAME");
        NUMBER=intent.getStringExtra("NUMBER");
        list.add(NAME);
        list.add(NUMBER);
        FloatingActionButton call=(FloatingActionButton)findViewById(R.id.dial);
        FloatingActionButton send_msg=(FloatingActionButton)findViewById(R.id.send_msg);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert=new AlertDialog.Builder(ContactDetail.this);
                if (NUMBER.equals("")){
                    alert.setTitle("不存在号码");
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                }else{
                alert.setTitle("确定拨打以下电话：");
                alert.setMessage(NAME+"\n"+NUMBER);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try{
                            Intent intent=new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:"+NUMBER));
                            startActivity(intent);
                        }catch(SecurityException e){
                            e.printStackTrace();
                        }
                    }
                });
                }
                alert.setCancelable(true);
                alert.show();
            }
        });
        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {       //如果号码已经有过短信记录，则直接跳到该号码的短信界面
                if (MainForm.exitNumber(NUMBER)){
                    Intent intent1=new Intent(ContactDetail.this,MsgItemDetail.class);
                    intent1.putExtra("NUMBER",NUMBER);
                    startActivity(intent1);
                }else{
                Intent intent=new Intent(ContactDetail.this,EmptySendMsg.class);
                intent.putExtra("NUMBER",NUMBER);
                startActivity(intent);
                }
            }
        });
    }
}
