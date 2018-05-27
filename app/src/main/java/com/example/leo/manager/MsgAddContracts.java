package com.example.leo.manager;

import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MsgAddContracts extends AppCompatActivity {
    private Button back_button;
    private FloatingActionButton conform_button;
    private List<ContactItem> contactItemList=new ArrayList<>();
    private AddContractsAdapter adapter;
    private RecyclerView AddRV;
    private Map<Integer,Boolean> map;
    private ArrayList<String> numberList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_add_contracts);
        back_button=(Button)findViewById(R.id.add_a_contact_back_button);
        conform_button=(FloatingActionButton)findViewById(R.id.conform_contract);
        adapter=new AddContractsAdapter(contactItemList);
        AddRV=(RecyclerView)findViewById(R.id.add_a_contact_rec_view);
        AddRV.setLayoutManager(new LinearLayoutManager(this));
        AddRV.setAdapter(adapter);
        getContacts();
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MsgAddContracts.this.finish();
            }
        });
        conform_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map=adapter.getMap();
                ContactItem contactItem=null;
                for (int i=0;i<map.size();i++){
                    if (map.get(i)){
                        numberList.add(contactItemList.get(i).getPhoneNumber());
                    }
                }
                Intent intent=new Intent();
                intent.putStringArrayListExtra("NUMBERS",numberList);
                setResult(RESULT_OK,intent);
                MsgAddContracts.this.finish();
            }
        });
    }
    /**
     * 获取联系人方法
     */
    private void getContacts(){
        contactItemList.clear();
        adapter.notifyDataSetChanged();
        ContactItem c=null;
        Cursor contactCursor=null;
        contactCursor=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        if (contactCursor!=null){
            while(contactCursor.moveToNext()){
                String name=contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number=contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                ContactItem contactItem=new ContactItem(name,number);
                contactItemList.add(contactItem);
            }
        }
        adapter.notifyDataSetChanged();
        contactCursor.close();
    }
}