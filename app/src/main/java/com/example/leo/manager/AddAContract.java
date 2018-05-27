package com.example.leo.manager;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

public class AddAContract extends AppCompatActivity {
    private Button back_button;
    private EditText name;
    private EditText number;
    private FloatingActionButton actionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_acontract);
        Toolbar toolbar=(Toolbar)findViewById(R.id.add_a_contact_tool_bar);
        toolbar.setTitle("");
        setActionBar(toolbar);
        back_button=(Button)findViewById(R.id.add_a_contact_back_to_interface);
        name=(EditText)findViewById(R.id.add_a_contact_name);
        number=(EditText)findViewById(R.id.add_a_contact_number);
        actionButton=(FloatingActionButton)findViewById(R.id.add_a_contact_conform_info_button);
        name.setText("");
        number.setText("");
        actionButton.setVisibility(View.GONE);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (name.getText().toString().equals("")){
                    actionButton.setVisibility(View.GONE);
                }else{
                    actionButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAContract.this.finish();
            }
        });
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContract(name.getText().toString(),number.getText().toString());
                Intent intent=new Intent();
                setResult(RESULT_OK,intent);
                AddAContract.this.finish();
            }
        });

    }

    private void addContract(String name,String number){
        ContentValues values = new ContentValues();

        // 向RawContacts.CONTENT_URI空值插入，
        // 先获取Android系统返回的rawContactId
        // 后面要基于此id插入值
        Uri rawContactUri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);
        values.clear();

        values.put(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId);
        // 内容类型
        values.put(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        // 联系人名字
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
        // 向联系人URI添加联系人名字
        getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        values.clear();

        values.put(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        // 联系人的电话号码
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, number);
        // 电话类型
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        // 向联系人电话号码URI添加电话号码
        getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        values.clear();
    }
}
