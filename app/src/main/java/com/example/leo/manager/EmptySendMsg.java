package com.example.leo.manager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EmptySendMsg extends AppCompatActivity {
    private Button addOneButton;
    private Button backButton;
    private Button sendButton;
    private EditText inputText;
    private RecyclerView msgRV;
    private MsgInterfaceAdapter adapter;
    private List<MsgItem> msgItemList;
    private List<String> intentBackNumbers;
    private EditText number;
    private Map<Integer,Boolean> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_send_msg);
        Intent intent=getIntent();
        msgItemList=new ArrayList<>();
        intentBackNumbers=new ArrayList<>();
        number=(EditText)findViewById(R.id.numbers);
        inputText=(EditText)findViewById(R.id.msg_empty_edit_text);
        inputText.setText("");
        /**
         * 如果界面是在联系人界面开启的话
         * 就读取号码到edit_text控件
         * 如果是在短信界面开启的话
         * 号码是“”类型，照样读取到edit_text控件中
         * 后面会有方法判断是否符合发送消息的条件
         */
        intentBackNumbers.add(intent.getStringExtra("NUMBER"));
        for (String n:intentBackNumbers){
            number.setText(n+"/");
        }
        addOneButton=(Button)findViewById(R.id.add_a_contact);
        backButton=(Button)findViewById(R.id.msg_empty_back_item);
        sendButton=(Button)findViewById(R.id.msg_empty_send);
        msgRV=(RecyclerView)findViewById(R.id.msg_empty_rec_view);
        adapter=new MsgInterfaceAdapter(msgItemList);
        msgRV.setAdapter(adapter);
        msgRV.setLayoutManager(new LinearLayoutManager(this));
        /**
         * 为控件设置监听器
         */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmptySendMsg.this.finish();
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentBackNumbers.add(number.getText().toString());
                if (number.getText().toString().equals("")){
                }else if (inputText.getText().toString().equals("")){
                }
                    else{
                        Date date=new Date();
                        long dateLong=date.getTime();
                        for (String num:number.getText().toString().split("/")){
                            if (!num.equals("")){
                            new SendAMsg(num,inputText.getText().toString(),EmptySendMsg.this).send();
                            MsgItem msgItem=new MsgItem(inputText.getText().toString(),num,dateLong,R.drawable.contracticon,2);
                            msgItemList.add(msgItem);
                            adapter.notifyDataSetChanged();
                            msgRV.scrollToPosition(msgItemList.size()-1);
                            }
                        }
                        inputText.setText("");
                }
            }
        });
        addOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent=new Intent(EmptySendMsg.this,MsgAddContracts.class);
                startActivityForResult(addIntent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode==RESULT_OK){
                    intentBackNumbers=data.getStringArrayListExtra("NUMBERS");
                    for (String backNum:intentBackNumbers){
                        number.append(backNum+"/");
                    }
                }
                break;
            default:
                break;
        }
    }
}
