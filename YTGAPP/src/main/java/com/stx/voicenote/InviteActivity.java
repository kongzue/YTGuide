package com.stx.voicenote;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidtestproject.testapp.R;
import cn.myzchh.YTGuide.util.BaseActivity;
import cn.myzchh.YTGuide.util.MyAdapter;
import cn.myzchh.YTGuide.util.RoomUtil;
import cn.myzchh.YTGuide.util.localUser;
import cn.myzchh.YTGuide.util.sqlliteDB.DBManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class InviteActivity extends BaseActivity {

    // 定义计时器
    Timer timer=new Timer();

    private List<Map<String, Object>> datas;
    private long exitTime;
    //切换布局
    private HorizontalScrollView m_tabScroll = null; // 标签滚动条
    private LinearLayout m_tabBar = null; // 标签栏
    private ArrayList<TextView> m_tabList = new ArrayList<TextView>(); // 标签列表
    private TextView m_currentTab;// 当前标签
    private ViewPager m_contentPager = null;// 内容页面
    private ArrayList<View> m_contentViewList = new ArrayList<View>(); // 内容视图列表
    private LayoutInflater m_Inflater = null;// 视图加载器

    private View view_hall;
    private View view_friends;
    private View view_setting;

    //InviteActivity
    private ListView list_invite;
    private ListView list_friends;
    private RelativeLayout layout_room;
    private MyAdapter sa;
    private MyAdapter se;
    private TextView text_room;

    private ImageView image_face1;
    private ImageView image_face2;
    private ImageView image_face3;
    private ImageView image_face4;
    private ImageView image_face5;
    private ImageView tab_line1;
    private ImageView tab_line2;
    private ImageView tab_line3;
    private TextView btn_hall;
    private TextView btn_friends;
    private TextView btn_setting;
    private Button btn_editData;
    private Button button_messageSetting;
    private Button button_Exit;
    private Button button_About;
    private TextView setting_text_Name;
    private ImageView btn_relink;

    private ListView areaCheckListView;
    String[] areas = new String[]{"显示通知","开启声音", "开启震动" };
    boolean[] areaState=new boolean[]{localUser.isGETMSG(), localUser.isOPENBEEP(), localUser.isOPENSHOCK()};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        tab_line1=(ImageView)findViewById(R.id.tab_line1);
        tab_line2=(ImageView)findViewById(R.id.tab_line2);
        tab_line3=(ImageView)findViewById(R.id.tab_line3);
        btn_hall=(TextView)findViewById(R.id.btn_hall);
        btn_friends=(TextView)findViewById(R.id.btn_friends);
        btn_setting=(TextView)findViewById(R.id.btn_setting);

        doUserSetOnline();

        //切换布局
        m_tabScroll = (HorizontalScrollView)findViewById(R.id.tab_scroll);
        m_tabBar = (LinearLayout)findViewById(R.id.tab_bar);
        addTab();
        m_contentPager = (ViewPager)findViewById(R.id.tab_content_pager);
        m_contentPager.setAdapter(new TabContentViewPagerAdapter());
        m_contentPager.setOnPageChangeListener(new TabContentPager_OnPageChangeListener());

        CheckTab(m_tabList.get(0)); // 开始在第一个标签上
        //InviteActivity
        list_invite=(ListView)view_hall.findViewById(R.id.list_invite);
        list_friends=(ListView)view_friends.findViewById(R.id.list_friends);

        layout_room=(RelativeLayout)findViewById(R.id.layout_room);
        text_room=(TextView)findViewById(R.id.text_room);
        image_face1=(ImageView)findViewById(R.id.image_face1);
        image_face2=(ImageView)findViewById(R.id.image_face2);
        image_face3=(ImageView)findViewById(R.id.image_face3);
        image_face4=(ImageView)findViewById(R.id.image_face4);
        image_face5=(ImageView)findViewById(R.id.image_face5);
        btn_editData=(Button)view_setting.findViewById(R.id.button_editData);
        button_messageSetting=(Button)view_setting.findViewById(R.id.button_messageSetting);
        button_Exit=(Button)view_setting.findViewById(R.id.button_Exit);
        button_About=(Button)view_setting.findViewById(R.id.button_about);
        setting_text_Name=(TextView)view_setting.findViewById(R.id.text_Name);
        btn_relink=(ImageView)findViewById(R.id.btn_relink);

        setting_text_Name.setText(localUser.getUSERNAME());

        //loadSomeItem();
        loadOnlineUser();

        Intent intent=getIntent();
        String roomname="";
        roomname=intent.getStringExtra("roomname");
        String roomid=intent.getStringExtra("roomid");
        if (!(roomname==null) && !roomname.equals("")){
            RoomUtil.addInvite(roomname);
        }
        if (RoomUtil.getInviteCount()>0){
            layout_room.setVisibility(View.VISIBLE);
            text_room.setText(RoomUtil.getInviteCount() + "人已被邀请");
            if (RoomUtil.getInviteCount()>1){image_face2.setVisibility(View.VISIBLE);}
            if (RoomUtil.getInviteCount()>2){image_face3.setVisibility(View.VISIBLE);}
            if (RoomUtil.getInviteCount()>3){image_face4.setVisibility(View.VISIBLE);}
            if (RoomUtil.getInviteCount()>4){image_face5.setVisibility(View.VISIBLE);}
            if (RoomUtil.getInviteCount()>5){text_room.setText("等"+RoomUtil.getInviteCount() + "人已被邀请");}
            ArrayList<String> roomP=RoomUtil.getInvite();
            if (!(roomP==null)){
                for(int i=0;i<roomP.size();i++){
                    String iName=roomP.get(i);
                    switch (i){
                        case 0:
                            image_face1.setImageResource(localUser.getUserFaceByRES(iName));
                            break;
                        case 1:
                            image_face2.setImageResource(localUser.getUserFaceByRES(iName));
                            break;
                        case 2:
                            image_face3.setImageResource(localUser.getUserFaceByRES(iName));
                            break;
                        case 3:
                            image_face4.setImageResource(localUser.getUserFaceByRES(iName));
                            break;
                        case 4:
                            image_face5.setImageResource(localUser.getUserFaceByRES(iName));
                            break;
                        default:
                            break;
                    }
                }
            }
        }


        btn_relink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loadOnlineUser();
                btn_relink.setVisibility(View.GONE);
            }
        });

        list_invite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //测试用例
                localUser.setROOMID("0");

                Intent intent = new Intent(InviteActivity.this, RoomActivity.class);
                String inviteName = ((TextView) view.findViewById(R.id.text_NickName)).getText().toString();
                intent.putExtra("inviteName", inviteName);

                showMessageByToast("正在邀请" + inviteName + "进入你的房间");

                try {
                    Thread.sleep(2000);
                    showMessageByToast("" + inviteName + "接受了您的邀请");
                    timer.cancel();
                    timer = null;
                    startActivity(intent);
                } catch (Exception e) {

                }

                int version = Integer.valueOf(android.os.Build.VERSION.SDK);
                if (version > 5) {
                    overridePendingTransition(R.anim.fade, R.anim.hold);
                }
                finish();

                //连接用例
//                TextView text_status=(TextView)view.findViewById(R.id.text_status);
//                if(!text_status.getText().toString().equals("[可以邀请]")){
//                    showMessageByToast("该用户目前无法接受邀请");
//                }else {
//                    final TextView text_NickName = (TextView) view.findViewById(R.id.text_NickName);
//                    final TextView text_UUID = (TextView) view.findViewById(R.id.text_UUID);
//                    showMessageByToast("click:"+text_NickName.getText().toString());
//                    if (localUser.getROOMID().equals("")) {
//                        //还没有房间的情况
//                        new AsyncTask<String, Void, Object>() {
//                            @Override
//                            protected Object doInBackground(String... params) {
//                                String url = "http://sanjin6035.vicp.cc/VoiceRecord/room/createRoom";
//                                String result = "";
//                                try {
//                                    HttpClient httpClient = new DefaultHttpClient();
//                                    HttpPost post = new HttpPost(url);
//                                    List<NameValuePair> paramList = new ArrayList<NameValuePair>();
//
//                                    BasicNameValuePair param = new BasicNameValuePair("riuuid", localUser.getUUID());
//                                    paramList.add(param);
//
//                                    post.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
//                                    HttpResponse httpResponse = httpClient.execute(post);
//                                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
//                                        result = EntityUtils.toString(httpResponse.getEntity());
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                                return result;
//                            }
//
//                            protected void onPostExecute(Object result) {
//                                super.onPostExecute(result);
//                                try {
//                                    JSONTokener jsonParser = new JSONTokener(result + "");
//                                    JSONObject loginmsg = (JSONObject) jsonParser.nextValue();
//                                    if (loginmsg.getBoolean("status")) {
//                                        localUser.setROOMID(loginmsg.getString("roomid"));
//                                        showMessageByToast(localUser.getROOMID());
//                                        String inviteName = text_NickName.getText().toString();
//                                        String inviteUUID = text_UUID.getText().toString();
//                                        showMessageByToast(inviteUUID);
//                                        doInvite(inviteName, inviteUUID);
//                                    } else {
//                                    }
//                                } catch (Exception e) {
//                                }
//                            }
//                        }.execute();
//                    } else {
//                        //已经有房间id的情况
//                        String inviteName = ((TextView) view.findViewById(R.id.text_NickName)).getText().toString();
//                        String inviteUUID = ((TextView) view.findViewById(R.id.text_UUID)).getText().toString();
//                        doInvite(inviteName, inviteUUID);
//                    }
//                }
            }
        });

        layout_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InviteActivity.this, RoomActivity.class);
                timer.cancel();
                timer = null;
                startActivity(intent);
                int version = Integer.valueOf(android.os.Build.VERSION.SDK);
                if (version > 5) {
                    overridePendingTransition(R.anim.fade, R.anim.hold);
                }
                finish();
            }
        });

        btn_editData.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn_editData.setBackgroundColor(getResources().getColor(R.color.btn_setting_off));
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    btn_editData.setBackgroundColor(getResources().getColor(R.color.btn_setting_off));
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn_editData.setBackgroundColor(getResources().getColor(R.color.btn_setting_down));
                }
                return false;
            }
        });

        button_messageSetting.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_UP){
                    button_messageSetting.setBackgroundColor(getResources().getColor(R.color.btn_setting_off));
                }
                if (event.getAction()==MotionEvent.ACTION_MOVE){
                    button_messageSetting.setBackgroundColor(getResources().getColor(R.color.btn_setting_off));
                }
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    button_messageSetting.setBackgroundColor(getResources().getColor(R.color.btn_setting_down));
                }
                return false;
            }
        });

        button_Exit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    button_Exit.setBackgroundColor(getResources().getColor(R.color.btn_setting_off));
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    button_Exit.setBackgroundColor(getResources().getColor(R.color.btn_setting_off));
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    button_Exit.setBackgroundColor(getResources().getColor(R.color.btn_setting_down));
                }
                return false;
            }
        });

        button_About.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    button_About.setBackgroundColor(getResources().getColor(R.color.btn_setting_off));
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    button_About.setBackgroundColor(getResources().getColor(R.color.btn_setting_off));
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    button_About.setBackgroundColor(getResources().getColor(R.color.btn_setting_down));
                }
                return false;
            }
        });

        button_Exit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessageByToast("您已成功注销登录！");
                DBManager mgr = new DBManager(InviteActivity.this);
                mgr.setLocalUser("", "");
                localUser.doUserOffline();
                finish();
            }
        });

        button_About.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new AlertDialog.Builder(InviteActivity.this).setTitle("关于我们").setMessage(
                        "享说 v1.0.0\n服务端开发：张鑫、刘帅 \n客户端开发：张超 \n本软件是一款语音转换文字的聊天软件，界面操作简单，设计美观，支持安卓手机，用户下载应用无需太多学习即可轻松使用。 ")
                        .setPositiveButton("检查更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showMessageByToast("当前没有更新！");
                            }
                        })
                        .setNegativeButton("支持我们！", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showMessageByToast("感谢您的支持，我们会继续加油！");
                            }
                        })
                        .setNeutralButton("关闭", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();

                dialog.show();
            }
        });

        btn_editData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(InviteActivity.this);
                builder.setItems(getResources().getStringArray(R.array.EditDataSettingArray), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int ItemIndex) {
                        switch (ItemIndex) {
                            case 0:
                                showMessageByToast("该功能努力开发中...");
                                break;
                            case 1:
                                showMessageByToast("该功能努力开发中...");
                                break;
                            case 2:
                                showMessageByToast("该功能努力开发中...");
                                break;
                            default:
                                break;
                        }
                        arg0.dismiss();
                    }
                });
                builder.show();
            }
        });

        button_messageSetting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog ad = new AlertDialog.Builder(InviteActivity.this)
                    .setTitle("")
                    .setMultiChoiceItems(areas, areaState, new DialogInterface.OnMultiChoiceClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton, boolean isChecked) {
                            //点击某个区域
                        }
                    }).setPositiveButton("确定",new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog,int whichButton){
                            for (int i = 0; i < areas.length; i++) {
                                if (i == 0) {
                                    localUser.setGETMSG(areaCheckListView.getCheckedItemPositions().get(i));
                                }
                                if (i == 1) {
                                    localUser.setOPENBEEP(areaCheckListView.getCheckedItemPositions().get(i));
                                }
                                if (i == 2) {
                                    localUser.setOPENSHOCK(areaCheckListView.getCheckedItemPositions().get(i));
                                }
//                                if (areaCheckListView.getCheckedItemPositions().get(i)){
//                                    s += i + ":"+ areaCheckListView.getAdapter().getItem(i)+ "  ";
//                                }else{
//                                    areaCheckListView.getCheckedItemPositions().get(i,false);
//                                }
                            }
                            dialog.dismiss();
                        }
                    }).setNegativeButton("取消", null).create();
                areaCheckListView = ad.getListView();
                ad.show();
            }
        });

        btn_hall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TabClick(0);
            }
        });
        btn_friends.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TabClick(1);
            }
        });
        btn_setting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TabClick(2);
            }
        });

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
//                if (msg.what > 0) {
//                    tv.setText(" " + msg.what);
//                } else {
//                    //在handler里可以更改UI组件
//                    tv.setText("开始点火");
//                    timer.cancel();
//                }
                loadOnlineUser();
            }
        };

        timer.schedule(new TimerTask() {
            int i = 10;
            // TimerTask 是个抽象类,实现的是Runable类
            @Override
            public void run() {
                Message message = new Message();
                message.what = i++;
                handler.sendMessage(message);
            }
        }, 100, 4000);

    }

    public void doInvite(String inviteName,String inviteUUID){
        final String inviteNameStr=inviteName;
        final String inviteUUIDStr=inviteUUID;
        new AsyncTask<String, Void, Object>() {
            @Override
            protected Object doInBackground(String... params) {
                String url = "http://sanjin6035.vicp.cc/VoiceRecord/room/invitePeopleToRoom";
                String result = "";
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost post = new HttpPost(url);
                    List<NameValuePair> paramList = new ArrayList<NameValuePair>();

                    //BasicNameValuePair param = new BasicNameValuePair("riuuid", localUser.getUUID());

                    BasicNameValuePair param = new BasicNameValuePair("riusername", localUser.getUSERNAME());
                    BasicNameValuePair param1 = new BasicNameValuePair("rbiuuid", inviteUUIDStr);
                    BasicNameValuePair param2 = new BasicNameValuePair("rroomid", localUser.getROOMID());
                    paramList.add(param);
                    paramList.add(param1);
                    paramList.add(param2);

                    post.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
                    HttpResponse httpResponse = httpClient.execute(post);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) { result = EntityUtils.toString(httpResponse.getEntity());}
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }

            protected void onPostExecute(Object result) {
                super.onPostExecute(result);
                try {
                    JSONTokener jsonParser = new JSONTokener(result + "");
                    JSONObject loginmsg = (JSONObject) jsonParser.nextValue();
                    if (loginmsg.getBoolean("status")) {
                        showMessageByToast("邀请成员" + inviteNameStr + "成功！");
                    } else {
                    }
                } catch (Exception e) {
                }
            }
        }.execute();
        //修改状态用
        new AsyncTask<String, Void, Object>() {
            @Override
            protected Object doInBackground(String... params) {
                String url = "http://sanjin6035.vicp.cc/VoiceRecord/user/changeUserBeInviteInfo";
                String result = "";
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost post = new HttpPost(url);
                    List<NameValuePair> paramList = new ArrayList<NameValuePair>();

                    BasicNameValuePair param = new BasicNameValuePair("riusername", localUser.getUSERNAME());
                    BasicNameValuePair param1 = new BasicNameValuePair("rbiuuid", inviteUUIDStr);
                    BasicNameValuePair param2 = new BasicNameValuePair("rroomid", localUser.getROOMID());
                    paramList.add(param);
                    paramList.add(param1);
                    paramList.add(param2);

                    post.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
                    HttpResponse httpResponse = httpClient.execute(post);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) { result = EntityUtils.toString(httpResponse.getEntity());}
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }

            protected void onPostExecute(Object result) {
                super.onPostExecute(result);
                try {
                    JSONTokener jsonParser = new JSONTokener(result + "");
                    JSONObject loginmsg = (JSONObject) jsonParser.nextValue();
                    if (loginmsg.getBoolean("status")) {
                        //showMessageByToast("邀请成员" + inviteNameStr + "成功！");
                    } else {
                    }
                } catch (Exception e) {
                }
            }
        }.execute();
    }

    public Map<String, Object> getMap(String jsonString) {
        JSONObject jsonObject;
        try {
            boolean Nullflag=false;
            String getName="";

            jsonObject = new JSONObject(jsonString);   @SuppressWarnings("unchecked")
            Iterator<String> keyIter = jsonObject.keys();
            String key;
            Object value;
            Map<String, Object> valueMap = new HashMap<String, Object>();
            while (keyIter.hasNext())
            {
                key = (String) keyIter.next();
                value = jsonObject.get(key);

                if (key.equals("uusername")) {
                    getName=(String)value;
                    if (getName.equals(localUser.getUSERNAME())){
                        Nullflag=true;
                    }
                }

                if (key.equals("ucanbeinvite")) {
                    if ((Integer) value == 1) {
                        valueMap.put("status", "[可以邀请]");
                    } else {
                        valueMap.put("status", "[忙]");
                    }
                }else{

                }
                if (key.equals("ubeinginvite")) {
                    if ((Integer) value == 1) {
                        valueMap.put("status", "[邀请中...]");
                        if (getName.equals(localUser.getUSERNAME())){
                            getInviteMessage();
                        }
                    }
                }


                valueMap.put(key, value);

            }
            valueMap.put("face",localUser.getUserFaceByRES(getName));
            valueMap.put("btnico",R.drawable.btn_invite);
            if (Nullflag){
                return null;
            }else {
                return valueMap;
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Map<String, Object>> getList(String jsonString) {
        ArrayList<Map<String, Object>> list = null;
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject jsonObject;
            list = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < jsonArray.length(); i++)
            {
                jsonObject = jsonArray.getJSONObject(i);
                Map<String, Object> m =getMap(jsonObject.toString());
                if (m!=null){
                    list.add(m);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void loadOnlineUser(){

        new AsyncTask<String, Void, Object>() {
            @Override
            protected Object doInBackground(String... params) {
                String url = "http://sanjin6035.vicp.cc/VoiceRecord/user/getOnlineUsers";
                String result = "";
                try {
                    //创建连接
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost post = new HttpPost(url);
                    //设置参数，仿html表单提交
                    List<NameValuePair> paramList = new ArrayList<NameValuePair>();

                    post.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
                    //发送HttpPost请求，并返回HttpResponse对象
                    HttpResponse httpResponse = httpClient.execute(post);
                    // 判断请求响应状态码，状态码为200表示服务端成功响应了客户端的请求
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        //获取返回结果
                        result = EntityUtils.toString(httpResponse.getEntity());
                        //showMessageByToast(result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //showMessageByToast("错误!");
                }
                return result;
            }

            protected void onPostExecute(Object result) {
                super.onPostExecute(result);
                if (!localUser.getListInviteJSONCache().equals(result+"")){
                    try {
                        JSONTokener jsonParser = new JSONTokener(result + "");
                        JSONObject loginmsg = (JSONObject) jsonParser.nextValue();
                        //if (loginmsg.getBoolean("status")) {
                            List<Map<String, Object>> datas=null;
                            MyAdapter se=null;
                            JSONArray blogInfo = loginmsg.getJSONArray("users");
                            datas=getList(blogInfo.toString());
                            sa=new MyAdapter(InviteActivity.this,datas,R.layout.invite_listitem,
                                    new String[]{"uusername","uemail","btnico","status","uuid","face"},
                                    new int[]{R.id.text_NickName,R.id.text_mail,R.id.btn_ico,R.id.text_status,R.id.text_UUID,R.id.image_face});
                            list_invite.setAdapter(sa);
                        //} else {
                            //showMessageByToast("Error1001,Username & Password is not true");
                        //}

                    } catch (Exception e) {
                        //showMessageByToast("Error0000,Exception @JSON");
                        e.printStackTrace();
                        btn_relink.setVisibility(View.VISIBLE);
                    }
                    localUser.setListInviteJSONCache(result+"");
                }else{
                    try {
                        JSONTokener jsonParser = new JSONTokener(localUser.getListInviteJSONCache());
                        JSONObject loginmsg = (JSONObject) jsonParser.nextValue();
                        //if (loginmsg.getBoolean("status")) {
                        List<Map<String, Object>> datas = null;
                        MyAdapter se = null;
                        JSONArray blogInfo = loginmsg.getJSONArray("users");
                        datas = getList(blogInfo.toString());
                        sa = new MyAdapter(InviteActivity.this, datas, R.layout.invite_listitem,
                                new String[]{"uusername", "uemail", "btnico", "status", "uuid", "face"},
                                new int[]{R.id.text_NickName, R.id.text_mail, R.id.btn_ico, R.id.text_status, R.id.text_UUID, R.id.image_face});
                        list_invite.setAdapter(sa);
                    }catch (Exception e){}
                }
                //showMessageByToast("" + result);
            }
        }.execute();

        List<Map<String, Object>> friends = new ArrayList<>();
        Map<String,Object> friend= new HashMap<String,Object>();
        friend.put("nickname", "myzchh");
        friend.put("mail", "myzchh@qq.com");
        friend.put("btnico",R.drawable.btn_message);
        friends.add(friend);
        se=new MyAdapter(this,friends,R.layout.invite_listitem,new String[]{"nickname","mail","btnico"},
                new int[]{R.id.text_NickName,R.id.text_mail,R.id.btn_ico});
        list_friends.setAdapter(se);
    }

    private void addTab() {
        if (m_Inflater == null)
            m_Inflater =getLayoutInflater();
        view_hall = m_Inflater.inflate(R.layout.invite_hall, null);
        view_friends = m_Inflater.inflate(R.layout.invite_friends, null);
        view_setting = m_Inflater.inflate(R.layout.invite_setting, null);

        m_contentViewList.add(view_hall);
        m_contentViewList.add(view_friends);
        m_contentViewList.add(view_setting);

        //TextView tv =(TextView) v.findViewById(R.id.content_tv);
        //tv.setText("这是标签" + m_contentViewList.size() + "的页面！");

        TextView tab= new TextView(this);
        m_tabList.add(tab);
        tab.setId(m_tabList.size());
        tab.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT));
        tab.setGravity(Gravity.CENTER);
        tab.setText("标签" + m_tabList.size());
        tab.setTag(m_tabList.size() - 1);// 通过tag保存与内容视图关联的序号，从0开始计数
        tab.setOnClickListener(new TabOnClickListener());
        m_tabBar.addView(tab);

        TextView tab1= new TextView(this);
        m_tabList.add(tab1);
        tab1.setId(m_tabList.size());
        tab1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT));
        tab1.setGravity(Gravity.CENTER);
        tab1.setText("标签" + m_tabList.size());
        tab1.setTag(m_tabList.size() - 1);// 通过tag保存与内容视图关联的序号，从0开始计数
        tab1.setOnClickListener(new TabOnClickListener());
        m_tabBar.addView(tab1);

        TextView tab2= new TextView(this);
        m_tabList.add(tab2);
        tab2.setId(m_tabList.size());
        tab2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT));
        tab2.setGravity(Gravity.CENTER);
        tab2.setText("标签" + m_tabList.size());
        tab2.setTag(m_tabList.size() - 1);// 通过tag保存与内容视图关联的序号，从0开始计数
        tab2.setOnClickListener(new TabOnClickListener());
        m_tabBar.addView(tab2);
    }



    /**
     * ViewPager适配器
     */
    class TabContentViewPagerAdapter extends PagerAdapter {
        public TabContentViewPagerAdapter() {}

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2){
            ((ViewPager)arg0).removeView(m_contentViewList.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {}

        @Override
        public int getCount() {
            return m_contentViewList.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager)arg0).addView(m_contentViewList.get(arg1), 0);
            return m_contentViewList.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {}

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {  }

    }

    /**
     * 页面切换监听器
     */
    class TabContentPager_OnPageChangeListener implements OnPageChangeListener {
        @Override

        public void onPageSelected(int arg0) {
            // arg0参数传入的是滑动到第几个页面，从0开始计数
            CheckTab(m_tabList.get(arg0));
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO自动生成的方法存根
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO自动生成的方法存根
        }
    }

    class TabOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO自动生成的方法存根
            // 切换到用户点击的标签
            CheckTab(v);
        }
    }

    /**
     *
     * 切换标签方法
     */
    private void TabClick(int tabIndex){
        m_contentPager.setCurrentItem(tabIndex, true);

        switch (tabIndex){
            case 0:
                tab_line1.setVisibility(View.VISIBLE);
                tab_line2.setVisibility(View.GONE);
                tab_line3.setVisibility(View.GONE);
                btn_hall.setTextColor(getResources().getColor(R.color.tab_title_on));
                btn_friends.setTextColor(getResources().getColor(R.color.tab_title_off));
                btn_setting.setTextColor(getResources().getColor(R.color.tab_title_off));
                break;
            case 1:
                tab_line1.setVisibility(View.GONE);
                tab_line2.setVisibility(View.VISIBLE);
                tab_line3.setVisibility(View.GONE);
                btn_hall.setTextColor(getResources().getColor(R.color.tab_title_off));
                btn_friends.setTextColor(getResources().getColor(R.color.tab_title_on));
                btn_setting.setTextColor(getResources().getColor(R.color.tab_title_off));

                break;
            case 2:
                tab_line1.setVisibility(View.GONE);
                tab_line2.setVisibility(View.GONE);
                tab_line3.setVisibility(View.VISIBLE);
                btn_hall.setTextColor(getResources().getColor(R.color.tab_title_off));
                btn_friends.setTextColor(getResources().getColor(R.color.tab_title_off));
                btn_setting.setTextColor(getResources().getColor(R.color.tab_title_on));

                break;
            default:
                break;
        }
    }

    private void CheckTab(View v) {
        if (m_currentTab != null) {
            //m_currentTab.setBackgroundResource(R.drawable.tab_normal_shape);
        }

        m_currentTab = (TextView) v;
        //m_currentTab.setBackgroundResource(R.drawable.tab_selected_shape);
        m_contentPager.setCurrentItem((Integer)m_currentTab.getTag(), true);

        switch ((Integer)m_currentTab.getTag()){
            case 0:
                tab_line1.setVisibility(View.VISIBLE);
                tab_line2.setVisibility(View.GONE);
                tab_line3.setVisibility(View.GONE);
                btn_hall.setTextColor(getResources().getColor(R.color.tab_title_on));
                btn_friends.setTextColor(getResources().getColor(R.color.tab_title_off));
                btn_setting.setTextColor(getResources().getColor(R.color.tab_title_off));
                break;
            case 1:
                tab_line1.setVisibility(View.GONE);
                tab_line2.setVisibility(View.VISIBLE);
                tab_line3.setVisibility(View.GONE);
                btn_hall.setTextColor(getResources().getColor(R.color.tab_title_off));
                btn_friends.setTextColor(getResources().getColor(R.color.tab_title_on));
                btn_setting.setTextColor(getResources().getColor(R.color.tab_title_off));

                break;
            case 2:
                tab_line1.setVisibility(View.GONE);
                tab_line2.setVisibility(View.GONE);
                tab_line3.setVisibility(View.VISIBLE);
                btn_hall.setTextColor(getResources().getColor(R.color.tab_title_off));
                btn_friends.setTextColor(getResources().getColor(R.color.tab_title_off));
                btn_setting.setTextColor(getResources().getColor(R.color.tab_title_on));

                break;
            default:
                break;
        }

        int viewLeft =v.getLeft();
        int viewRight =v.getRight();
        int scrollLeft = m_tabScroll.getScrollX();
        int scrollRight =scrollLeft + m_tabScroll.getWidth();

        // 如果切换到的标签被隐在屏幕的左边或右边而看不到，就通过滑动让它显示出来
        if (viewLeft <scrollLeft) {
            m_tabScroll.scrollTo(viewLeft,0);
        } else if (viewRight >scrollRight) {
            m_tabScroll.scrollBy(viewRight- scrollRight, 0);
        }
    }

    public void doUserSetOnline(){
        //开始登录
        localUser.doUserOnLine();
    }

    public void showMessageByToast(String Msg) {
        Toast.makeText(InviteActivity.this, Msg, Toast.LENGTH_LONG).show();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                localUser.doUserOffline();
                finish();
                //System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showMessageInTaskBar(String Title,String message,String Tip){
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        Intent resultIntent=new Intent(this, MainActivity.class);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT );

        builder.setContentTitle(Title);
        builder.setContentText(message);
        builder.setTicker(Tip);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setAutoCancel(true);    //设置点击后自动删除
        //builder.setOngoing(true);       //设置不可删除

        builder.setContentIntent(resultPendingIntent);
        NotificationManager me=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        me.notify(1, builder.build());
    }

    public void getInviteMessage(){
        new AsyncTask<String, Void, Object>() {
            @Override
            protected Object doInBackground(String... params) {
                String url = "http://sanjin6035.vicp.cc/VoiceRecord/user/showUser";
                String result = "";
                try {
                    //创建连接
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost post = new HttpPost(url);
                    //设置参数，仿html表单提交
                    List<NameValuePair> paramList = new ArrayList<NameValuePair>();

                    BasicNameValuePair param = new BasicNameValuePair("uuid", localUser.getUUID());
                    paramList.add(param);

                    post.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
                    //发送HttpPost请求，并返回HttpResponse对象
                    HttpResponse httpResponse = httpClient.execute(post);
                    // 判断请求响应状态码，状态码为200表示服务端成功响应了客户端的请求
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        //获取返回结果
                        result = EntityUtils.toString(httpResponse.getEntity());
                        //showMessageByToast(result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //showMessageByToast("错误!");
                }
                return result;
            }

            protected void onPostExecute(Object result) {
                super.onPostExecute(result);
                try {
                    JSONTokener jsonParser = new JSONTokener(result + "");
                    JSONObject loginmsg = (JSONObject) jsonParser.nextValue();
                    if (loginmsg.getBoolean("status")) {
                        JSONObject userInfo = loginmsg.getJSONObject("user");
                        String inviterName= "";
                        String inviterRoom= "";
                        inviterName= userInfo.getString("uiusername");
                        inviterRoom= userInfo.getString("uroomid");
                        if (!inviterName.equals("") && !inviterRoom.equals("")){
                            showMessageByToast(inviterName+"邀请你加入房间"+inviterRoom);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}