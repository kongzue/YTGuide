package cn.myzchh.YTGuide.util;

import android.os.AsyncTask;

import com.example.androidtestproject.testapp.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chao on 2015/5/19.
 */
public class localUser {
    private static  String USERNAME;
    private static  String UUID;

    private static boolean isUserOffline=false;
    private static boolean isMailSend=false;

    public static String getListInviteJSONCache() {
        return listInviteJSONCache;
    }

    public static void setListInviteJSONCache(String listInviteJSONCache) {
        localUser.listInviteJSONCache = listInviteJSONCache;
    }

    public static String listInviteJSONCache="-1";

    private static boolean GETMSG=true;
    private static boolean OPENBEEP=false;
    private static boolean OPENSHOCK=false;

    private static String ROOMID="";

    public static String getROOMID() {
        return ROOMID;
    }

    public static void setROOMID(String ROOMID) {
        localUser.ROOMID = ROOMID;
    }

    public static boolean isGETMSG() {
        return GETMSG;
    }

    public static void setGETMSG(boolean GETMSG) {
        localUser.GETMSG = GETMSG;
    }

    public static boolean isOPENBEEP() {
        return OPENBEEP;
    }

    public static void setOPENBEEP(boolean OPENBEEP) {
        localUser.OPENBEEP = OPENBEEP;
    }

    public static boolean isOPENSHOCK() {
        return OPENSHOCK;
    }

    public static void setOPENSHOCK(boolean OPENSHOCK) {
        localUser.OPENSHOCK = OPENSHOCK;
    }

    public static  String getUSERNAME(){
        return USERNAME;
    }
    public static  String getUUID(){
        return UUID;
    }
    public static  void setUSERNAME(String username){
        USERNAME= username;
    }
    public static void setUUID(String uuid){
        UUID= uuid;
    }

    public static void doUserOnLine(){
        new AsyncTask<String, Void, Object>() {
            @Override
            protected Object doInBackground(String... params) {
                String url = "http://sanjin6035.vicp.cc/VoiceRecord/user/changeUserStatusOnline";
                String result = "";
                try {
                    //创建连接
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost post = new HttpPost(url);
                    //设置参数，仿html表单提交
                    List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                    BasicNameValuePair param = new BasicNameValuePair("uusername", getUSERNAME());
                    BasicNameValuePair param1 = new BasicNameValuePair("uuid",getUUID());
                    paramList.add(param);
                    paramList.add(param1);

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
            }
        }.execute();
    }

    public static void doUserOffline(){
        new AsyncTask<String, Void, Object>() {
            @Override
            //发送邮件
            protected Object doInBackground(String... params) {
                String url = "http://sanjin6035.vicp.cc/VoiceRecord/voicerecord/exit";
                String result = "";
                try {
                    //创建连接
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost post = new HttpPost(url);
                    //设置参数，仿html表单提交
                    List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                    BasicNameValuePair param = new BasicNameValuePair("roomid",getROOMID());
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
                isMailSend=true;
                if (isUserOffline) {
                    ROOMID = "";
                    System.exit(0);
                    super.onPostExecute(result);
                }
            }
        }.execute();
        //让自己下线
        new AsyncTask<String, Void, Object>() {
            @Override

            protected Object doInBackground(String... params) {
                String url = "http://sanjin6035.vicp.cc/VoiceRecord/user/changeUserStatusOffline";
                String result = "";
                try {
                    //创建连接
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost post = new HttpPost(url);
                    //设置参数，仿html表单提交
                    List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                    BasicNameValuePair param = new BasicNameValuePair("uusername",getUSERNAME());
                    BasicNameValuePair param1 = new BasicNameValuePair("uuid",getUUID());
                    paramList.add(param);
                    paramList.add(param1);

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
                isUserOffline=true;
                if (isMailSend) {
                    ROOMID = "";
                    System.exit(0);
                    super.onPostExecute(result);
                }
            }
        }.execute();
    }

    public static int getUserFaceByRES(String name){
        int resID=0;
        if (name==null){return 0;}
        switch (name){
            case "zhangchao":
                resID= R.drawable.face_chao;
                break;
            case "sanjin6035":
                resID= R.drawable.face_xin;
                break;
            case "testA":
                resID= R.drawable.face_test;
                break;
            case "dddd123":
                resID= R.drawable.face_shuai;
                break;
            default:
                resID= R.drawable.face_default;
                break;
        }

        return resID;
    }
}