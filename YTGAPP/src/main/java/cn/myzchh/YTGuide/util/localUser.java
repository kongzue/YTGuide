package cn.myzchh.YTGuide.util;

import android.app.Activity;
import android.content.SharedPreferences;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import cn.myzchh.YTGuide.MapActivity;

/**
 * Created by chao on 2015/5/19.
 */

public class localUser{
    private static double gps_JD;
    private static double gps_WD;
    private static List<Map> local_path;
    private static String localUserName="";
    private static String localUserUUID="";
    private static double goWhereX=0;
    private static double goWhereY=0;


    public static void GoWhere(String pointName){
        if (local_path==null){
            getLocal_path();
        }
        for (int i=0;i<local_path.size();i++){
            Map m=local_path.get(i);
            if (m.get("name").equals(pointName)){
                goWhereX=(double)m.get("x");
                goWhereY=(double)m.get("y");
                break;
            }
        }
    }

    public static String getInfoByName(String pointName){
        String result="";
        if (local_path==null){
            getLocal_path();
        }
        for (int i=0;i<local_path.size();i++){
            Map m=local_path.get(i);
            if (m.get("name").equals(pointName)){
                result=(String)m.get("info");
                break;
            }
        }
        return result;
    }

    public static int getPointID(String pointName){
        if (local_path==null){
            getLocal_path();
        }
        int t=-1;
        for (int i=0;i<local_path.size();i++){
            Map m=local_path.get(i);
            if (m.get("name").equals(pointName)){
                t=i;
                break;
            }
        }
        return t;
    }

    public static String getLocalUserUUID() {
        new localUser().loadUserInfo();
        return localUserUUID;
    }

    public static void setLocalUserUUID(String localUserUUID) {
        localUser.localUserUUID = localUserUUID;
        new localUser().saveUserInfo();
    }

    private void saveUserInfo() {
//        SharedPreferences mySharedPreferences = MapActivity.getSharedPreferences("preferences", Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = mySharedPreferences.edit();
//        editor.putString("UserName", localUserName);
//        editor.putString("UserUUID", localUserUUID);
//        editor.commit();
    }

    private void loadUserInfo() {
//        SharedPreferences sharedPreferences = getSharedPreferences("preferences", Activity.MODE_PRIVATE);
//        setLocalUserName(sharedPreferences.getString("UserName", ""));
//        setLocalUserUUID(sharedPreferences.getString("UserUUID", ""));
    }

    public static String getLocalUserName() {
        new localUser().loadUserInfo();
        return localUserName;
    }

    public static void setLocalUserName(String localUserName) {
        localUser.localUserName = localUserName;
        new localUser().saveUserInfo();
    }

    public static double getGoWhereX() {
        double d=goWhereX;
        goWhereX=0;
        return d;
    }

    public static void setGoWhereX(double goWhereX) {
        localUser.goWhereX = goWhereX;
    }

    public static double getGoWhereY() {
        double d=goWhereY;
        goWhereY=0;
        return d;
    }

    public static void setGoWhereY(double goWhereY) {
        localUser.goWhereY = goWhereY;
    }



    public static List<Map> getLocal_path() {
        if (local_path==null){
            local_path=new ArrayList<Map>();
            local_path.add(addNewPath("第二教学楼","这里是第二教学楼，二教二楼是重庆邮电大学移通学院双体系卓越人才教育基地所在地。",30.0038520000,106.2465740000));
            local_path.add(addNewPath("第二实验楼","这里是第二实验楼，上实验课就在这里",30.0046610000,106.2466100000));
            local_path.add(addNewPath("第一实验楼","这里是第一实验楼，上实验课就在这里",30.0050600000,106.2466460000));
            local_path.add(addNewPath("第一教学楼","这里是第一教学楼",30.0050560000,106.2472970000));
            local_path.add(addNewPath("B1宿舍楼","这里是B1宿舍楼，移通宿舍可谓整个重庆最好的宿舍，设施完备，宽敞明亮，上床下书桌，有衣柜书架。寝室有全制冷格力或美的空调，配有标准网线插口，插座板到每个床位。",30.0058740000,106.2473500000));
            local_path.add(addNewPath("B2宿舍楼","这里是B2宿舍楼，移通宿舍可谓整个重庆最好的宿舍，设施完备，宽敞明亮，上床下书桌，有衣柜书架。寝室有全制冷格力或美的空调，配有标准网线插口，插座板到每个床位。",30.0065620000,106.2473640000));
            local_path.add(addNewPath("移通北门","这里是移通北门，北门外是宾果城商业步行街",30.0072620000,106.2474090000));//6
            local_path.add(addNewPath("B区食堂","这里是B区食堂，也称作新食堂",30.0073280000,106.2466680000));
            local_path.add(addNewPath("A区食堂","这里是A区食堂，也称作老食堂",30.0075430000,106.2451490000));
            local_path.add(addNewPath("网球场","这里是网球场",30.0051528775,106.2459762949));
            local_path.add(addNewPath("移通图书馆","这里是移通图书馆，图书馆是学院的文献信息中心，是为教学和科研服务的学术性机构，是学校信息化和社会信息化的重要基地。图书馆的建设和发展应与学校的建设和发展相适应，其水平是学校总体水平的重要标志。",30.0043950000,106.2454910000));//10
            local_path.add(addNewPath("双子湖","这里是双子湖，双子湖是移通著名景点，也是休闲娱乐的好去处。",30.0033318775,106.2449892949));
            local_path.add(addNewPath("下里巴人剧场","这里是下里巴人剧场",30.0026068775,106.2450962949));
            local_path.add(addNewPath("第六教学楼","这里是第六教学楼",30.0022030241,106.2449881059));
            local_path.add(addNewPath("第七教学楼","这里是第七教学楼",30.0037878775,106.2473462949));
            local_path.add(addNewPath("第五教学楼","这里是第五教学楼",30.0026300000,106.2467190000));
            local_path.add(addNewPath("第四教学楼","这里是第四教学楼",30.0029620000,106.2467010000));
            local_path.add(addNewPath("第三教学楼","这里是第三教学楼",30.0033690000,106.2466740000));//17
            local_path.add(addNewPath("行政楼","这里是行政楼，学院领导以及各位老师办公地点就在这里。",30.0021821176,106.2460180741));
            local_path.add(addNewPath("移通正门","这里是移通正门，正门外有许多超市、餐厅，以及购物休闲场所，晚上会有重庆本地特色小吃摊。",30.0043520000,106.2474230000));
            local_path.add(addNewPath("移通南门","这里是移通南门",30.0020770000,106.2455090000));
            local_path.add(addNewPath("A区宿舍楼","这里是A区宿舍楼，移通宿舍可谓整个重庆最好的宿舍，设施完备，宽敞明亮，上床下书桌，有衣柜书架。寝室有全制冷格力或美的空调，配有标准网线插口，插座板到每个床位。",30.0057750000,106.2455590000));
            local_path.add(addNewPath("花果山香樟书院","这里是花果山香樟书院，花果山是移通著名景点之一，也是休息、约会必去之地。",30.0043990000,106.2444090000));
            local_path.add(addNewPath("移通操场","这里是移通操场，体育相关的课程大多在这里举行，同时在这里还会举办各种活动，足球赛。",30.0062290000,106.2465740000));//23
            local_path.add(addNewPath("篮球场","这里是篮球场",30.0064630000,106.2443190000));
            local_path.add(addNewPath("C区天桥","这里是C区天桥，拥有“天梯”之称，通过天桥就可以前往移通北校区以及C区宿舍楼了",30.0075740000,106.2461160000));
            local_path.add(addNewPath("C区食堂","这里是C区食堂",30.0087230000,106.2461430000));//26
            local_path.add(addNewPath("移通游泳馆","这里是移通游泳馆",30.0096220000,106.2464120000));
            local_path.add(addNewPath("C区宿舍楼","这里是C区宿舍楼，远景学院宿舍楼所在地，移通宿舍可谓整个重庆最好的宿舍，设施完备，宽敞明亮，上床下书桌，有衣柜书架。寝室有全制冷格力或美的空调，配有标准网线插口，插座板到每个床位。",30.0085430000,106.2472030000));
            local_path.add(addNewPath("宾果城商业步行街","这里是宾果城商业步行街，在这里可以尽情购物休闲娱乐，宾果城还拥有一所电影院，随时享受最新大片。",30.0082230000,106.2465380000));
//            local_path.add(addNewPath("","",1,1));
//            local_path.add(addNewPath("","",1,1));
//            local_path.add(addNewPath("","",1,1));
//            local_path.add(addNewPath("","",1,1));
        }
        return local_path;
    }

    public static List<Map> searchPath(String keyWord){
        List<Map> m=new ArrayList<Map>();
        if (keyWord.replace("二教","").length()!=keyWord.length() || keyWord.replace("双体","").length()!=keyWord.length()){
            m.add(local_path.get(0));
        }
        if (keyWord.replace("二实验","").length()!=keyWord.length()){
            m.add(local_path.get(1));
        }
        if (keyWord.replace("一实验","").length()!=keyWord.length()){
            m.add(local_path.get(2));
        }
        if (keyWord.replace("一教","").length()!=keyWord.length()){
            m.add(local_path.get(3));
        }
        if (keyWord.replace("B1","").length()!=keyWord.length() || keyWord.replace("宿舍","").length()!=keyWord.length() || keyWord.replace("B区","").length()!=keyWord.length()){
            m.add(local_path.get(4));
        }
        if (keyWord.replace("张超","").length()!=keyWord.length() || keyWord.replace("张鑫","").length()!=keyWord.length()){
            m.add(local_path.get(4));
        }
        if (keyWord.replace("B2","").length()!=keyWord.length() || keyWord.replace("宿舍","").length()!=keyWord.length() || keyWord.replace("B区","").length()!=keyWord.length()){
            m.add(local_path.get(5));
        }
        if (keyWord.replace("北门","").length()!=keyWord.length()){
            m.add(local_path.get(6));
        }
        if (keyWord.replace("B区食堂","").length()!=keyWord.length() || keyWord.replace("新食堂","").length()!=keyWord.length()|| keyWord.replace("食堂","").length()!=keyWord.length()|| keyWord.replace("吃饭","").length()!=keyWord.length()|| keyWord.replace("饿了","").length()!=keyWord.length()){
            m.add(local_path.get(7));
        }
        if (keyWord.replace("A区食堂","").length()!=keyWord.length() || keyWord.replace("老食堂","").length()!=keyWord.length()|| keyWord.replace("食堂","").length()!=keyWord.length()|| keyWord.replace("吃饭","").length()!=keyWord.length()|| keyWord.replace("饿了","").length()!=keyWord.length()){
            m.add(local_path.get(8));
        }
        if (keyWord.replace("网球","").length()!=keyWord.length()){
            m.add(local_path.get(9));
        }
        if (keyWord.replace("图书馆","").length()!=keyWord.length() || keyWord.replace("借书","").length()!=keyWord.length() || keyWord.replace("看书","").length()!=keyWord.length() || keyWord.replace("读书","").length()!=keyWord.length()){
            m.add(local_path.get(10));
        }
        if (keyWord.replace("双子湖","").length()!=keyWord.length() || keyWord.replace("约会","").length()!=keyWord.length() || keyWord.replace("休息","").length()!=keyWord.length() || keyWord.replace("美景","").length()!=keyWord.length()){
            m.add(local_path.get(11));
        }
        if (keyWord.replace("下里巴人","").length()!=keyWord.length() || keyWord.replace("剧院","").length()!=keyWord.length() || keyWord.replace("剧场","").length()!=keyWord.length()){
            m.add(local_path.get(12));
        }
        if (keyWord.replace("六教","").length()!=keyWord.length()){
            m.add(local_path.get(13));
        }
        if (keyWord.replace("七教","").length()!=keyWord.length() || keyWord.replace("停车场","").length()!=keyWord.length()){
            m.add(local_path.get(14));
        }
        if (keyWord.replace("五教","").length()!=keyWord.length() || keyWord.replace("创业","").length()!=keyWord.length()){
            m.add(local_path.get(15));
        }
        if (keyWord.replace("四教","").length()!=keyWord.length() || keyWord.replace("学生组织","").length()!=keyWord.length() || keyWord.replace("办公室","").length()!=keyWord.length()|| keyWord.replace("科联","").length()!=keyWord.length()|| keyWord.replace("科技联合会","").length()!=keyWord.length()|| keyWord.replace("社联","").length()!=keyWord.length()|| keyWord.replace("社团","").length()!=keyWord.length()|| keyWord.replace("团委","").length()!=keyWord.length()){
            m.add(local_path.get(16));
        }
        if (keyWord.replace("三教","").length()!=keyWord.length() || keyWord.replace("阶梯教室","").length()!=keyWord.length() || keyWord.replace("讲堂","").length()!=keyWord.length()){
            m.add(local_path.get(17));
        }
        if (keyWord.replace("行政","").length()!=keyWord.length() || keyWord.replace("办公室","").length()!=keyWord.length() || keyWord.replace("辅导员","").length()!=keyWord.length() || keyWord.replace("院长","").length()!=keyWord.length() || keyWord.replace("校长","").length()!=keyWord.length()){
            m.add(local_path.get(18));
        }

        if (keyWord.replace("正门","").length()!=keyWord.length() || keyWord.replace("大门","").length()!=keyWord.length() || keyWord.replace("超市","").length()!=keyWord.length() || keyWord.replace("买东西","").length()!=keyWord.length() || keyWord.replace("坐车","").length()!=keyWord.length() || keyWord.replace("吃饭","").length()!=keyWord.length() || keyWord.replace("移通","").length()!=keyWord.length() || keyWord.replace("饮料","").length()!=keyWord.length()){
            m.add(local_path.get(19));
        }
        if (keyWord.replace("南门","").length()!=keyWord.length()){
            m.add(local_path.get(20));
        }
        if (keyWord.replace("A区","").length()!=keyWord.length() || keyWord.replace("宿舍","").length()!=keyWord.length() || keyWord.replace("A1","").length()!=keyWord.length() || keyWord.replace("A2","").length()!=keyWord.length() || keyWord.replace("A3","").length()!=keyWord.length() || keyWord.replace("A4","").length()!=keyWord.length() || keyWord.replace("A5","").length()!=keyWord.length() || keyWord.replace("A6","").length()!=keyWord.length() || keyWord.replace("A7","").length()!=keyWord.length() || keyWord.replace("A8","").length()!=keyWord.length() || keyWord.replace("A9","").length()!=keyWord.length() || keyWord.replace("A10","").length()!=keyWord.length() || keyWord.replace("A11","").length()!=keyWord.length() || keyWord.replace("A12","").length()!=keyWord.length() || keyWord.replace("A13","").length()!=keyWord.length()){
            m.add(local_path.get(21));
        }
        if (keyWord.replace("花果山","").length()!=keyWord.length() || keyWord.replace("香樟","").length()!=keyWord.length() || keyWord.replace("美景","").length()!=keyWord.length() || keyWord.replace("约会","").length()!=keyWord.length() || keyWord.replace("休息","").length()!=keyWord.length()){
            m.add(local_path.get(22));
        }
        if (keyWord.replace("操场","").length()!=keyWord.length() || keyWord.replace("足球场","").length()!=keyWord.length()){
            m.add(local_path.get(23));
        }
        if (keyWord.replace("篮球","").length()!=keyWord.length()){
            m.add(local_path.get(24));
        }
        if (keyWord.replace("天桥","").length()!=keyWord.length() || keyWord.replace("天梯","").length()!=keyWord.length()|| keyWord.replace("C区","").length()!=keyWord.length()){
            m.add(local_path.get(25));
        }
        if (keyWord.replace("C区食堂","").length()!=keyWord.length()|| keyWord.replace("食堂","").length()!=keyWord.length() || keyWord.replace("吃饭","").length()!=keyWord.length()|| keyWord.replace("饿了","").length()!=keyWord.length()){
            m.add(local_path.get(26));
        }

        if (keyWord.replace("游泳","").length()!=keyWord.length()){
            m.add(local_path.get(27));
        }
        if (keyWord.replace("C区宿舍","").length()!=keyWord.length()|| keyWord.replace("宿舍","").length()!=keyWord.length() || keyWord.replace("远景","").length()!=keyWord.length()){
            m.add(local_path.get(28));
        }
        if (keyWord.replace("宾果城","").length()!=keyWord.length()|| keyWord.replace("剧院","").length()!=keyWord.length() || keyWord.replace("电影","").length()!=keyWord.length()|| keyWord.replace("购物","").length()!=keyWord.length()|| keyWord.replace("吃饭","").length()!=keyWord.length()){
            m.add(local_path.get(29));
        }
        return m;
    }

    private static Map addNewPath(String pathName,String info,double x,double y){
        Map map=new HashMap();
        map.put("name",pathName);
        map.put("info",info);
        map.put("x",x);
        map.put("y",y);
        return map;
    }

    public static double degree(double x1, double y1, double x2, double y2) {
        double result = 0;
        double r = 0;
        double x;
        double y;
        x = x2 - x1;
        y = y2 - y1;
        if (y > 0) {
            r = Math.atan2(y, x) / Math.PI * 180;
        } else {
            r = Math.atan2(y, x) / Math.PI * 180 + 360;
        }
        if (r >= 360) {
            r -= 360;
        }
        if (r >= 90) {
            result = 450 - r;
        } else {
            result = 90 - r;
        }
        // System.out.println(r);
        if (result >= 360) {
            result -= 360;
        }
        return result;
    }

    public static double getGps_JD() {
        return gps_JD;
    }

    public static void setGps_JD(double gps_JD) {
        localUser.gps_JD = gps_JD;
    }

    public static double getGps_WD() {
        return gps_WD;
    }

    public static void setGps_WD(double gps_WD) {
        localUser.gps_WD = gps_WD;
    }
}