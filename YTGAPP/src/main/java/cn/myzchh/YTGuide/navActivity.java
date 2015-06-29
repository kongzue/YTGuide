package cn.myzchh.YTGuide;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidtestproject.testapp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.myzchh.YTGuide.util.BaseActivity;
import cn.myzchh.YTGuide.util.localUser;

public class navActivity extends BaseActivity {

    //摄像头
    private static final String TAG = "CameravedioActivity";
    private Camera camera;
    private boolean preview = false;
    private SeekBar mSeekBar;

    //指南针传感器
    private SensorManager manager;
    private SensorListener listener = new SensorListener();

    private TextView text_compass;
    private TextView text_ori;
    private TextView text_Tip;

    private boolean back_flag;
    private TextView text_title;
    private TextView text_body;
    private RelativeLayout box_info;
    private boolean flag_box_anim;

    //GPS
    private LocationManager locationManager;
    private double latitude = 0.0;
    private double longitude = 0.0;

    private List<Map> mNearlydatas=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        text_compass=(TextView)findViewById(R.id.text_compass);
        text_ori=(TextView)findViewById(R.id.text_ori);
        text_Tip=(TextView)findViewById(R.id.text_Tip);
        text_title=(TextView)findViewById(R.id.text_title);
        text_body=(TextView)findViewById(R.id.text_body);
        box_info=(RelativeLayout)findViewById(R.id.box_info);

        //指南针获取系统服务（SENSOR_SERVICE)返回一个SensorManager 对象
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //摄像头
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.cp);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().setFixedSize(200, 200);
        surfaceView.getHolder().addCallback(new SurfaceViewCallback());

        text_ori.setText("x="+ localUser.getGps_JD()+"\ny="+localUser.getGps_WD()+
                "\n和图书馆距离="+Distance(localUser.getGps_WD(),localUser.getGps_JD(),30.0043950000,106.2454910000));

        getPathRotateAngle();
    }

    public void getPathRotateAngle() {
        mNearlydatas=new ArrayList<Map>();
        List<Map> datas= getNearlyPath();

        String debugStr="";

        for(int i = 0;i < datas.size(); i ++){
            Map map=datas.get(i);

            //System.out.println(">>"+localUser.getGps_WD()+","+(double)map.get("x")+","+localUser.getGps_JD()+","+(double)map.get("y"));
            //System.out.println("c="+localUser.);
            double c=degree(localUser.getGps_JD(),localUser.getGps_WD(),(double)map.get("y"),(double)map.get("x"));
            debugStr=debugStr+("name="+map.get("name")+";  c="+c)+"\n";
            map.put("degree",c);

            mNearlydatas.add(map);
        }

        text_ori.setText(text_ori.getText()+"\n"+debugStr);
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
        System.out.println(result);
        return result;
    }

//
//    private double CalculateRotateAngle(double x,double y){
//        double PI=Math.PI;
//        System.out.println("y="+y+";JD="+localUser.getGps_JD()+"\nx="+x+";WD="+localUser.getGps_WD());
//        double dRotateAngle = Math.atan(((y-localUser.getGps_JD())/(x-localUser.getGps_WD())));
//
//        //如果下一点的横坐标大于前一点(在第一和第四象限)
//        if (x>=localUser.getGps_WD())
//        {
//            //在第一象限(0<=dRotateAngle<=90)
//            if (y>=localUser.getGps_JD())
//            {
//
//            }
//            else
//            {
//                dRotateAngle=PI-dRotateAngle;
//            }
//        }
//        else//(在第二和第三象限)
//        {
//            //第二象限
//            if (y>=localUser.getGps_JD())
//            {
//                dRotateAngle=2*PI-dRotateAngle;
//            }
//            else//第三象限
//            {
//                dRotateAngle=PI+dRotateAngle;
//            }
//        }
//        return dRotateAngle;
//    }
    
    public List<Map> getNearlyPath(){
        List<Map> returnDatas=new ArrayList<Map>();
        List<Map> datas= localUser.getLocal_path();

        for(int i = 0;i < datas.size(); i ++){
            Map map=datas.get(i);
            double x=(double)map.get("x");
            double y=(double)map.get("y");
            double distance=Distance(localUser.getGps_WD(),localUser.getGps_JD(),x,y);
            //System.out.println("getDatas:   name="+map.get("name")+"    ;distance="+distance);
            if (distance<=50){
                returnDatas.add(map);
            }
        }
        return returnDatas;
    }

    public double Distance(double n1, double e1, double n2, double e2)
    {
        double DEF_PI = 3.14159265359; // PI
        double DEF_2PI= 6.28318530712; // 2*PI
        double DEF_PI180= 0.01745329252; // PI/180.0
        double DEF_R =6370693.5; // radius of earth

        double ew1, ns1, ew2, ns2;
        double dx, dy, dew;
        double distance;
        // 角度转换为弧度
        ew1 = n1 * DEF_PI180;
        ns1 = e1 * DEF_PI180;
        ew2 = n2 * DEF_PI180;
        ns2 = e2 * DEF_PI180;
        // 经度差
        dew = ew1 - ew2;
        // 若跨东经和西经180 度，进行调整
        if (dew > DEF_PI)
            dew = DEF_2PI - dew;
        else if (dew < -DEF_PI)
            dew = DEF_2PI + dew;
        dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
        dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
        // 勾股定理求斜边长
        distance = Math.sqrt(dx * dx + dy * dy);
        return distance;
    }

    public void showMessageByToast(String Msg) {
        Toast.makeText(navActivity.this, Msg, Toast.LENGTH_LONG).show();
    }

    protected void onResume() {
        super.onResume();
        /**
         * 获取方向传感器
         * 通过SensorManager对象获取相应的Sensor类型的对象
         */
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        //应用在前台时候注册监听器
        manager.registerListener(listener, sensor,
                SensorManager.SENSOR_DELAY_GAME);
    }

    protected void onPause() {
        super.onPause();
        //指南针传感器应用不在前台时候销毁掉监听器
        manager.unregisterListener(listener);
    }

    //指南针传感器
    private final class SensorListener implements SensorEventListener {
        private float predegree = 0;
        private boolean ori_flag=false;

        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;// 存放了方向值
            int x=(int)values[0];
            text_compass.setText(x+"°");
            if (x>337 || x<22 ){
                text_Tip.setText("北");
            }
            if (x>=22 && x<76 ){
                text_Tip.setText("东北");
            }
            if (x>=76 && x<112 ){
                text_Tip.setText("东");
            }
            if (x>=112 && x<157 ){
                text_Tip.setText("东南");
            }
            if (x>=157 && x<202 ){
                text_Tip.setText("南");
            }
            if (x>=202 && x<247 ){
                text_Tip.setText("西南");
            }
            if (x>=247 && x<292 ){
                text_Tip.setText("西");
            }
            if (x>=292 && x<337 ){
                text_Tip.setText("西北");
            }

            if (mNearlydatas!=null){
                String strName="";
                String strInfo="";
                for(int k = 0;k <= mNearlydatas.size(); k ++){
                    if (k!=mNearlydatas.size()) {
                        Map map = mNearlydatas.get(k);
                        double c = ((double) map.get("degree"));
                        if (c > x - 5 && c < x + 5) {
                            strName = (String) map.get("name");
                            strInfo = (String) map.get("info");
                            break;
                        }
                    }else{
                        strName = "";
                        strInfo = "";
                    }
                }
                if (!strName.equals("")){
                    text_title.setText(strName);
                    text_body.setText(strInfo);
                    if (!flag_box_anim) {
                        box_info.setVisibility(View.VISIBLE);
                        Animation aIn1 = new AlphaAnimation(0f, 1f);
                        aIn1.setDuration(400);
                        aIn1.setFillAfter(true);
                        box_info.startAnimation(aIn1);
                        flag_box_anim = true;
                    }
                }else {
                    if (flag_box_anim) {
                        box_info.setVisibility(View.GONE);
                        Animation aIn1 = new AlphaAnimation(1f, 0f);
                        aIn1.setDuration(400);
                        aIn1.setFillAfter(true);
                        box_info.startAnimation(aIn1);
                        flag_box_anim = false;
                    }
                }
            }
            // 获取与Y轴的夹角
            float y = values[1];
            //text_ori.setText("y="+y);
                if (y > -45) {
                    if (!back_flag) {
                        finish();
                        overridePendingTransition(R.anim.fade, R.anim.back);
                        back_flag=true;
                    }
                }else{
                    back_flag=false;
                }
            predegree = -values[0];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }


    }

    private final class SurfaceViewCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            camera = Camera.open();
            try {
                camera.setPreviewDisplay(holder);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Camera.Parameters parameters = camera.getParameters();
            /* 设置预览照片的大小，此处设置为全屏 */
//          WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE); // 获取当前屏幕管理器对象
//          Display display = wm.getDefaultDisplay();                        // 获取屏幕信息的描述类
//          parameters.setPreviewSize(display.getWidth(), display.getHeight());          // 设置
            parameters.setPreviewSize(200, 200);
            /* 每秒从摄像头捕获5帧画面， */
            parameters.setPreviewFrameRate(5);
            /* 设置照片的输出格式:jpg */
            parameters.setPictureFormat(PixelFormat.JPEG);
            /* 照片质量 */
            parameters.set("jpeg-quality", 85);
            /* 设置照片的大小：此处照片大小等于屏幕大小 */
//          parameters.setPictureSize(display.getWidth(), display.getHeight());
            parameters.setPictureSize(200, 200);
            /* 将参数对象赋予到 camera 对象上 */
//          camera.setParameters(parameters);
            camera.startPreview();
            camera.setPreviewCallback(new Camera.PreviewCallback(){
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    // TODO Auto-generated method stub
                    //在视频聊天中，这里传送本地frame数据给remote端
                }
            });
            camera.setDisplayOrientation(90);
            preview = true;
        }
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
            Log.d(TAG,"surfaceChanged");
        }
        /**
         * SurfaceView 被销毁时释放掉 摄像头
         */
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if(camera != null) {
                /* 若摄像头正在工作，先停止它 */
                if(preview) {
                    camera.stopPreview();
                    preview = false;
                }
                //如果注册了此回调，在release之前调用，否则release之后还回调，crash
                camera.setPreviewCallback(null);
                camera.release();
                camera=null;
            }
        }

    }
    /**
     * 处理照片被拍摄之后的事件
     */
    private final class TakePictureCallback implements Camera.PictureCallback {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
            overridePendingTransition(R.anim.fade, R.anim.back);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
