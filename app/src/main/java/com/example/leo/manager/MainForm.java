package com.example.leo.manager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainForm extends AppCompatActivity {
    /**
     *联系人部分加上基本的界面初始化部分
     */
    private DrawerLayout drawerLayout;
    private NavigationView nav;
    private AlertDialog.Builder al;
    private List<ContactItem> contactItemList=new ArrayList<>();
    private RecyclerView contactsRec;
    private ContactsAdapter contactsAdapter;
    private String NUMBER=null;
    private String NAME=null;
    private FrameLayout contacts_layout;
    private FrameLayout msg_layout;
    private FrameLayout call_log_layout;
    private MsgAdapter msgAdapter;
    private Button settingButton;
    private FloatingActionButton add_a_contract;
    /**
     * 初始化标题栏以及其他组件
    * */
    private void initDraw(){
        callogRV=(RecyclerView)findViewById(R.id.call_record_recycler_view);
        callogRV.setLayoutManager(new LinearLayoutManager(MainForm.this));
        callogRV.setAdapter(callLogAdapter);
        callogRV.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        add_a_contract=(FloatingActionButton)findViewById(R.id.add_a_contact_button);
        settingButton=(Button)findViewById(R.id.setting_button);
        Toolbar toolbar=(Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.menuicon);

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainForm.this,InfoActivity.class);
                startActivity(intent);
            }
        });
        /**
         * RecyclerView项目的监听器(预留)
         */
        contactsAdapter.setOnRVClickListener(new ContactsAdapter.OnRVClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ContactItem contactItem=contactItemList.get(position);
                NAME=contactItem.getName();
                NUMBER=contactItem.getPhoneNumber();
                Intent intent=new Intent(MainForm.this,ContactDetail.class);
                intent.putExtra("NAME",NAME);
                intent.putExtra("NUMBER",NUMBER);
                startActivity(intent);
            }
        });
        contactsAdapter.setOnImageClickListener(new ContactsAdapter.OnImageClickListener() {
            @Override
            public void onImageClick(View view, int position) {
            }
        });

        /***
         * Navigation的菜单项目的监听器，分别设置了布局的可见性
         */
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.contacts:
                        contacts_layout.setVisibility(View.VISIBLE);
                        call_log_layout.setVisibility(View.GONE);
                        msg_layout.setVisibility(View.GONE);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.call_record:
                        call_log_layout.setVisibility(View.VISIBLE);
                        contacts_layout.setVisibility(View.GONE);
                        msg_layout.setVisibility(View.GONE);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.msg:
                        call_log_layout.setVisibility(View.GONE);
                        contacts_layout.setVisibility(View.GONE);
                        msg_layout.setVisibility(View.VISIBLE);
                        drawerLayout.closeDrawers();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        /**
         * 新增一个联系人
         */
        add_a_contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainForm.this,AddAContract.class);
                startActivityForResult(intent,2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 2:
                if (resultCode==RESULT_OK){
                    getContacts();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 为标题栏的菜单按钮添加监听器
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 16908332:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.refresh:
                getContacts();
                getMsg();
                getCallog();
                break;
            default:
                break;
        }
        return true;
    }

    /***
     *申请权限
     */
    private void call_permissions(){
        /**
         * 分别申请读取联系人、读取短信、读取通话记录的权限
         */
        if (checkSelfPermission(Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_CALL_LOG)!= PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.WRITE_CONTACTS)!=PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.WRITE_CALL_LOG)!=PackageManager.PERMISSION_GRANTED){
            al=new AlertDialog.Builder(MainForm.this);
            al.setTitle("请授予应用权限,否则应用无法正常运行.");
            al.setCancelable(false);
            alertForPermissions(al);
        }else{
            getContacts();
            getCallog();
            getMsg();
        }
    }

    /**
     *刷新联系人按钮
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_refresh,menu);
        return true;
    }

    /**
     *暗示用户授予权限
     */
    private void alertForPermissions(AlertDialog.Builder a){
        a.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] per=new String[]{
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.WRITE_CALL_LOG};
                ActivityCompat.requestPermissions(MainForm.this,per,1);
            }
        });
        a.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainForm.this, "没有权限应用无法正常运行", Toast.LENGTH_SHORT).show();
                MainForm.this.finish();
            }
        });
        a.show();
    }

    /**
     * 获取联系人方法
     */
    private void getContacts(){
        contactItemList.clear();
        contactsAdapter.notifyDataSetChanged();
        ContactItem c=null;
        Cursor contactCursor=null;
        contactCursor=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        if (contactCursor!=null){
            while(contactCursor.moveToNext()){
                String name=contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number=contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                ContactItem contactItem=new ContactItem(name,number);
                if (name==null || number==null)continue;
                contactItemList.add(contactItem);
            }
        }
        contactsAdapter.notifyDataSetChanged();
        contactCursor.close();
    }


    /**
     *以上是联系人的全部初始化方法
     * 若需要更改或添加新的内容仅限在
     * 上面的部分添加或者更改
     * 以代码的阅读
     * 接下来是短信部分的代码
     */
    private List<MsgItem> msgItemList=new ArrayList<>();
    private FloatingActionButton writeAMsg;
    RecyclerView MsgRV;

    /**
     * 初始化信息界面布局
     */
    private void InitMsgPart(){
        msgAdapter=new MsgAdapter(msgItemList);
        writeAMsg=(FloatingActionButton)findViewById(R.id.write_a_msg);
        MsgRV=(RecyclerView)findViewById(R.id.msg_recycler_view);
        MsgRV.setLayoutManager(new LinearLayoutManager(MainForm.this));
        MsgRV.setAdapter(msgAdapter);
        MsgRV.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        if (checkSelfPermission(Manifest.permission.READ_SMS)!=PackageManager.PERMISSION_GRANTED){} else getMsg();
        writeAMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainForm.this,EmptySendMsg.class);
                intent.putExtra("NUMBER","");
                startActivity(intent);
            }
        });
    }
    /**
     * 获取短信
     */
    private static Map<String,MsgItem> msgItemsHashMap=null;
    private void getMsg(){
        msgItemsHashMap=new HashMap<>();
        msgItemList.clear();
        MsgItem msgItem=null;
        Cursor msgCursor=null;
        Uri uri=Uri.parse("content://sms/");
        ContentResolver contentResolver=null;
        contentResolver=getContentResolver();
        msgCursor=contentResolver.query(uri,null,null,null,null);
       if (msgCursor!=null){
            while (msgCursor.moveToNext()){
                String threadId=msgCursor.getString(msgCursor.getColumnIndex("thread_id"));
                if(!msgItemsHashMap.containsKey(threadId)){
                    String number=msgCursor.getString(msgCursor.getColumnIndex("address"));
                    String content=msgCursor.getString(msgCursor.getColumnIndex("body"));
                    String name=msgCursor.getString(msgCursor.getColumnIndex("person"));
                    long date=msgCursor.getLong(msgCursor.getColumnIndex("date"));
                    int type=msgCursor.getInt(msgCursor.getColumnIndex("type"));
                    if (name!=null)
                        msgItem=new MsgItem(content,name,date,R.drawable.contracticon,type);
                    else
                        msgItem=new MsgItem(content,number,date,R.drawable.contracticon,type);
                    msgItemsHashMap.put(number,msgItem);
                }
            }
        }
        Iterator iterator=msgItemsHashMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry=(Map.Entry)iterator.next();
            msgItemList.add((MsgItem) entry.getValue());
        }
        msgAdapter.notifyDataSetChanged();
        msgCursor.close();
        /**
         * 设置短信项目的点击事件
         */
        msgAdapter.setOnItemClickListener(new MsgAdapter.OnClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent switchToMsg=new Intent(MainForm.this,MsgItemDetail.class);
                MsgItem msgItem1=msgItemList.get(position);
                switchToMsg.putExtra("NUMBER",msgItem1.getMsgSender());
                startActivity(switchToMsg);
            }
        });
    }

    /**
     * 判断短信中是否存在了某个号码
     */
    public static boolean exitNumber(String num){
        if (msgItemsHashMap!=null){
            if (msgItemsHashMap.containsKey(num))
                return true;
            else
                return false;
        }
        return false;
    }

    /**
     *初始化通讯记录的界面和获取通讯录
     */
    private RecyclerView callogRV;
    private List<CallLogItem> callLogItemList=new ArrayList<>();
    private CallLogAdapter callLogAdapter=new CallLogAdapter(callLogItemList);
    private void getCallog(){
        Uri uri;
        callLogItemList.clear();
        Cursor calllogCursor;
        ContentResolver contentResolver;
        CallLogItem callLogItem;
        try{
            uri= CallLog.Calls.CONTENT_URI;
            calllogCursor=null;
            contentResolver=getContentResolver();
            calllogCursor=contentResolver.query(uri,null,null,null,null);
            if (calllogCursor!=null){
                while (calllogCursor.moveToNext()){
                    String number=calllogCursor.getString(calllogCursor.getColumnIndex(CallLog.Calls.NUMBER));
                    long date=calllogCursor.getLong(calllogCursor.getColumnIndex(CallLog.Calls.DATE));
                    int type=calllogCursor.getInt(calllogCursor.getColumnIndex(CallLog.Calls.TYPE));
                    String name=calllogCursor.getString(calllogCursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                    String address=calllogCursor.getString(calllogCursor.getColumnIndex(CallLog.Calls.GEOCODED_LOCATION));
                    callLogItem=new CallLogItem(number,name,date,type,address);
                    callLogItemList.add(callLogItem);
                }
            }
            callLogAdapter.notifyDataSetChanged();
            calllogCursor.close();
        }catch (Exception e){
            e.printStackTrace();

        }
        /**
         * 通讯记录的点击事件
         */
        callLogAdapter.setOnItemClickListener(new CallLogAdapter.OnClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CallLogItem callLogItem=callLogItemList.get(position);
                final String number=callLogItem.getNumber();
                try{
                    AlertDialog.Builder al=new AlertDialog.Builder(MainForm.this);
                    al.setMessage("确定拨打以下电话:\n"+number);
                    al.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent callIntent=new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:"+number));
                            startActivity(callIntent);
                        }
                    });
                    al.setCancelable(true);
                    al.show();
                }catch(SecurityException e){
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_form);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawerLayout);
        msg_layout=(FrameLayout)findViewById(R.id.msg_layout);
        msg_layout.setVisibility(View.GONE);
        contacts_layout=(FrameLayout)findViewById(R.id.contacts_layout);
        contacts_layout.setVisibility(View.VISIBLE);
        call_log_layout=(FrameLayout)findViewById(R.id.call_record_layout);
        call_log_layout.setVisibility(View.GONE);
        nav=(NavigationView)findViewById(R.id.nav_view);
        contactsRec=(RecyclerView)findViewById(R.id.contacts_recycler_view);
        contactsRec.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        contactsAdapter=new ContactsAdapter(contactItemList);
        contactsRec.setAdapter(contactsAdapter);
        contactsRec.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        initDraw();
        InitMsgPart();
        call_permissions();
    }
}
