package cn.myzchh.YTGuide;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.hardware.SensorManager;
import android.view.animation.RotateAnimation;

import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.example.androidtestproject.testapp.R;

import java.io.IOException;

import cn.myzchh.YTGuide.util.BaseActivity;
import cn.myzchh.YTGuide.util.localUser;

public class MapActivity extends BaseActivity {

    //地图
    public MapView mMapView = null;
    public BaiduMap baiduMap = null;
    // 定位相关声明
    public LocationClient locationClient = null;
    // 自定义图标
    BitmapDescriptor mCurrentMarker = null;
    boolean isFirstLoc = true;// 是否首次定位

    //指南针传感器
    private SensorManager manager;
    private SensorListener listener = new SensorListener();

    private ImageView btn_rec;
    private ImageView btn_rec_shadow;
    private ImageView img_rec_bkg;
    private ImageView img_map;
    private Button btn_search;
    private EditText edit_search;


    /** 初始化所有mark坐标 */
    // 第二教学楼
    private LatLng point1 = new LatLng(30.0038520000,106.2465740000);
    private Marker maker1;
    // 第二实验楼
    private LatLng point2 = new LatLng(30.0046610000,106.2466100000);
    private Marker maker2;
    // 第一实验楼
    private LatLng point3 = new LatLng(30.0050600000,106.2466460000);
    private Marker maker3;
    // 第一教学楼
    private LatLng point4 = new LatLng(30.0050560000,106.2472970000);
    private Marker maker4;
    // B1宿舍楼
    private LatLng point5 = new LatLng(30.0058740000,106.2473500000);
    private Marker maker5;
    // B2宿舍楼
    private LatLng point6 = new LatLng(30.0065620000,106.2473640000);
    private Marker maker6;
    // 移通北门
    private LatLng point7 = new LatLng(30.0072620000,106.2474090000);
    private Marker maker7;
    // B区食堂
    private LatLng point8 = new LatLng(30.0073280000,106.2466680000);
    private Marker maker8;
    // A区食堂
    private LatLng point9 = new LatLng(30.0075430000,106.2451490000);
    private Marker maker9;
    // 网球场
    private LatLng point10 = new LatLng(30.0051528775,106.2459762949);
    private Marker maker10;
    // 移通图书馆
    private LatLng point11 = new LatLng(30.0043950000,106.2454910000);
    private Marker maker11;
    // 双子湖
    private LatLng point12 = new LatLng(30.0033318775,106.2449892949);
    private Marker maker12;
    // 下里巴人剧场
    private LatLng point13 = new LatLng(30.0026068775,106.2450962949);
    private Marker maker13;
    // 第六教学楼
    private LatLng point14 = new LatLng(30.0022498775,106.2468822949);
    private Marker maker14;
    // 第七教学楼
    private LatLng point15 = new LatLng(30.0037878775,106.2473462949);
    private Marker maker15;
    // 第五教学楼
    private LatLng point16 = new LatLng(30.0026300000,106.2467190000);
    private Marker maker16;
    // 第四教学楼
    private LatLng point17 = new LatLng(30.0029620000,106.2467010000);
    private Marker maker17;
    // 第三教学楼\
    private LatLng point18 = new LatLng(30.0033690000,106.2466740000);
    private Marker maker18;
    // 行政楼
    private LatLng point19 = new LatLng(30.0022500000,106.2449180000);
    private Marker maker19;
    // 移通正门
    private LatLng point20 = new LatLng(30.0043950000,106.2469690000);
    private Marker maker20;
    // 移通南门
    private LatLng point21 = new LatLng(30.0020770000,106.2455090000);
    private Marker maker21;
    // A区宿舍楼
    private LatLng point22 = new LatLng(30.0057750000,106.2455590000);
    private Marker maker22;
    //花果山香樟书院
    private LatLng point23 = new LatLng(30.0043990000,106.2444090000);
    private Marker maker23;

    //移通操场
    private LatLng point24 = new LatLng(30.0062290000,106.2465740000);
    private Marker maker24;

    //篮球场
    private LatLng point25 = new LatLng(30.0064630000,106.2443190000);
    private Marker maker25;

    //C区天桥
    private LatLng point26 = new LatLng(30.0075740000,106.2461160000);
    private Marker maker26;

    //C区食堂
    private LatLng point27 = new LatLng(30.0087230000,106.2461430000);
    private Marker maker27;

    //移通游泳馆
    private LatLng point28 = new LatLng(30.0096220000,106.2464120000);
    private Marker maker28;

    //C区宿舍楼
    private LatLng point29 = new LatLng(30.0085430000,106.2472030000);
    private Marker maker29;

    //宾果城商业步行街
    private LatLng point30 = new LatLng(30.0082230000,106.2465380000);
    private Marker maker30;

















    // 构建Marker图标
    private BitmapDescriptor bitmap;
    private BitmapDescriptor ground;

    //导航标记
    boolean useDefaultIcon = false;
    private OverlayManager routeOverlay = null;

    private boolean ori_flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        // 注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);

        //绑定控件
        btn_rec=(ImageView)findViewById(R.id.btn_rec);
        btn_rec_shadow=(ImageView)findViewById(R.id.btn_rec_shadow);
        img_rec_bkg=(ImageView)findViewById(R.id.img_rec_bkg);
        img_map=(ImageView)findViewById(R.id.img_map);
        btn_search=(Button)findViewById(R.id.btn_search);
        edit_search=(EditText)findViewById(R.id.edit_search);

        //指南针获取系统服务（SENSOR_SERVICE)返回一个SensorManager 对象
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        loadAnim();

        btn_rec.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    Animation aIn = new AlphaAnimation(0f, 1f);
                    aIn.setDuration(300);
                    aIn.setFillAfter(true);
                    btn_rec_shadow.startAnimation(aIn);
                }
                if (event.getAction()==MotionEvent.ACTION_UP){
                    Animation aIn = new AlphaAnimation(1f, 0f);
                    aIn.setDuration(300);
                    aIn.setFillAfter(true);
                    btn_rec_shadow.startAnimation(aIn);
                }
                return false;
            }
        });

        btn_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (routeOverlay!=null){
                    routeOverlay.removeFromMap();
                }
                img_rec_bkg.setVisibility(View.VISIBLE);
                ScaleAnimation animation =new ScaleAnimation(1f, 20f, 1f, 20f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(500);
                animation.setStartOffset(10);
                animation.setFillAfter(true);
                img_rec_bkg.setAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    public void onAnimationStart(Animation animation) {}
                    public void onAnimationEnd(Animation animation) {
                        Intent intent=new Intent(MapActivity.this,recActivity.class);
                        startActivity(intent);
                        int version = Integer.valueOf(android.os.Build.VERSION.SDK);
                        if (version > 5) {
                            overridePendingTransition(R.anim.fade, R.anim.hold);
                        }
                    }
                    public void onAnimationRepeat(Animation animation) {}
                });
            }
        });

        img_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MapActivity.this,testActivity.class);
                startActivity(intent);
            }
        });

        // 获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        baiduMap=mMapView.getMap();
        //开启定位图层
        baiduMap.setMyLocationEnabled(true);
        locationClient = new LocationClient(getApplicationContext()); // 实例化LocationClient类
        locationClient.registerLocationListener(myListener); // 注册监听函数
        this.setLocationOption();   //设置定位参数
        locationClient.start(); // 开始定位
        locationClient.requestLocation();

        addMapMark();

        baiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }
            @Override
            public void onMapStatusChangeFinish(MapStatus status) {
                if (baiduMap.getMapStatus().zoom <= 17.0) {
                    // 销毁mark
                    baiduMap.clear();
                }
                if (baiduMap.getMapStatus().zoom >= 17.0) {
                    // 重新加载mark
                    initOverlay();
                }
            }
            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

        });

        //去掉标尺
        int childCount = mMapView.getChildCount();
        View zoom = null;
        for(int i = 0;i<childCount;i++){
            View child = mMapView.getChildAt(i);
            if (child instanceof ZoomControls) {
                zoom = child;
                break;
            }
        }
        zoom.setVisibility(View.GONE);
        //去除百度logo
        mMapView.removeViewAt(1);
        BaiduMapOptions options = new BaiduMapOptions()
                .overlookingGesturesEnabled(false).rotateGesturesEnabled(false)
                        // .scaleControlEnabled(false) //是否显示比例尺控件
                .scrollGesturesEnabled(false).zoomGesturesEnabled(false)
                .zoomControlsEnabled(false).compassEnabled(false);
        mMapView = new MapView(MapActivity.this, options);

        ori_flag=false;

        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker mark) {
                //showMessageByToast("进入《"+mark.getTitle()+"》处的评论");
                if (routeOverlay!=null){
                    routeOverlay.removeFromMap();
                }
                Intent intent=new Intent(MapActivity.this,commentActivity.class);
                intent.putExtra("name",mark.getTitle());
                startActivity(intent);
                int version = Integer.valueOf(android.os.Build.VERSION.SDK);
                if (version > 5) {
                    overridePendingTransition(R.anim.fade, R.anim.hold);
                }
                return true;
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (routeOverlay!=null){
                    routeOverlay.removeFromMap();
                }
                Intent intent=new Intent(MapActivity.this,SearchResultActivity.class);
                intent.putExtra("searchStr",edit_search.getText().toString());
                startActivity(intent);
                int version = Integer.valueOf(android.os.Build.VERSION.SDK);
                if (version > 5) {
                    overridePendingTransition(R.anim.fade, R.anim.hold);
                }
            }
        });

    }//end OnCreate




    private void walkToPoint(double x, double y){
        System.out.println("定位到：x="+x+";y="+y+";当前坐标：x="+localUser.getGps_WD()+";y="+localUser.getGps_JD());
        if (x==0 || y==0){
            return;
        }
        try {
            //routeOverlay.removeFromMap();
            //Log.e(TAG, "当前经纬度" + locLatitude + "," locLongitude + "与" + point + "间的距离为：" getPointsDistance(point));
            // 发起算路，既路线规划
            RoutePlanSearch search = RoutePlanSearch.newInstance();
            //需要规划的两点
            PlanNode startPint = PlanNode.withLocation(new LatLng(localUser.getGps_WD(), localUser.getGps_JD()));
            PlanNode endPoint = PlanNode.withLocation(new LatLng(x, y));
            search.walkingSearch(new WalkingRoutePlanOption().from(startPint).to(endPoint));
            search.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
                /**
                 *线路规划结果回调监听器,如果需要实时导航，需要将location方法中的option.setScanSpan(500)，改为1000即可
                 */
                @Override
                public void onGetWalkingRouteResult(WalkingRouteResult result) {
                    //Log.e(TAG,"步行结果监听器");
                    //获取步行规划结果
                    if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                        Toast.makeText(MapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                    }
                    if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                        //Log.e(TAG,"第二个if");
                        WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(baiduMap);
                        routeOverlay = overlay;
                        overlay.setData(result.getRouteLines().get(0));
                        overlay.addToMap();
//                        overlay.zoomToSpan();
                    }
                }

                @Override
                public void onGetTransitRouteResult(TransitRouteResult arg0) {

                }

                @Override
                public void onGetDrivingRouteResult(DrivingRouteResult arg0) {

                }
            });
        } catch (Exception e) {
//            Toast.makeText(MapActivity.this,
//                    e.getMessage() + "", Toast.LENGTH_LONG)
//                    .show();
        }
    }

    /**构建导航线路起终点的图标
     */
    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
            }
            return null;
        }
    }

    public void showMessageByToast(String Msg) {
        Toast.makeText(MapActivity.this, Msg, Toast.LENGTH_SHORT).show();
    }

    private void addMapMark() {

        /** 初始化mark的图标资源 */
        initMarkBitMap();
        /** 为移通添加mark标注 */
        initOverlay();
    }
    /**
     * 初始化mark的图标资源
     */
    private void initMarkBitMap() {
        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
        ground = BitmapDescriptorFactory.fromResource(R.drawable.p_ground);
    }
    /**
     * 添加自定义覆盖物
     */
    private void initOverlay() {
        /** 定义Maker坐标点 */
        maker1 = setPicMark(point1, "第二教学楼");
        maker2 = setPicMark(point2, "第二实验楼");
        maker3 = setPicMark(point3, "第一实验楼");
        maker4 = setPicMark(point4, "第一教学楼");
        maker5 = setPicMark(point5, "B1宿舍楼");
        maker6 = setPicMark(point6, "B2宿舍楼");
        maker7 = setPicMark(point7, "移通北门");
        maker8 = setPicMark(point8, "B区食堂");
        maker9 = setPicMark(point9, "A区食堂");
        maker10 = setPicMark(point10, "网球场");
        maker11 = setPicMark(point11, "移通图书馆");
        maker12 = setPicMark(point12, "双子湖");
        maker13 = setPicMark(point13, "下里巴人剧场");
        maker14 = setPicMark(point14, "第六教学楼");
        maker15 = setPicMark(point15, "第七教学楼");
        maker16 = setPicMark(point16, "第五教学楼");
        maker17 = setPicMark(point17, "第四教学楼");
        maker18 = setPicMark(point18, "第三教学楼");
        maker19 = setPicMark(point19, "行政楼");
        maker20 = setPicMark(point20, "移通正门");
        maker21 = setPicMark(point21, "移通南门");
        maker22 = setPicMark(point22, "A区宿舍楼");
        maker23 = setPicMark(point23, "花果山香樟书院");
        maker24 = setPicMark(point24, "移通操场");
        maker25 = setPicMark(point25, "篮球场");
        maker26 = setPicMark(point26, "C区天桥");
        maker27 = setPicMark(point27, "C区食堂");
        maker28 = setPicMark(point28, "移通游泳馆");
        maker29 = setPicMark(point29, "C区宿舍楼");
        maker30 = setPicMark(point30, "宾果城商业步行街");
    }

    private Overlay setGroundOverlay(LatLng southwest, LatLng northeast,
                                     BitmapDescriptor bdGround) {
        LatLngBounds bounds = new LatLngBounds.Builder().include(northeast)
                .include(southwest).build();
        // 定义Ground覆盖物选项
        OverlayOptions ooGround = new GroundOverlayOptions()
                .positionFromBounds(bounds).image(bdGround).transparency(0.8f);
        // 在地图中添加Ground覆盖物
        baiduMap.addOverlay(ooGround);
        return null;
    }

    /**
     * 设置图形覆盖物
     *
     * @param point
     *            图片覆盖物的坐标
     * @param title
     *            覆盖物的说明文字
     */
    public Marker setPicMark(LatLng point, String title) {
        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(point)
                .icon(bitmap);
        Marker mark = (Marker) baiduMap.addOverlay(option);
        // mark的说明文字
        setTextMark(point, title);
        mark.setTitle(title);
        return mark;
    }

    /**
     * 设置文字覆盖物
     *
     * @param text
     *            设置的文字
     * @param point
     *            设置文字的坐标
     * @return Overlay
     */
    public Overlay setTextMark(LatLng point, String text) {
        // 构建文字Option对象，用于在地图上添加文字
        OverlayOptions textOption = new TextOptions().fontSize(37)
                .fontColor(0xFFFF7313).text(text).position(point);
        // 在地图上添加该文字对象并显示
        Overlay textOverlay = baiduMap.addOverlay(textOption);
        return textOverlay;
    }

    /**
     * 设置文字覆盖物
     *
     * @param text
     *            设置的文字
     * @param point
     *            设置文字的坐标
     * @param rotate
     *            旋转角度大小
     * @return Overlay
     */
    public Overlay setTextMark(LatLng point, String text, int rotate) {
        // 构建文字Option对象，用于在地图上添加文字
        OverlayOptions textOption = new TextOptions().fontSize(37)
                .fontColor(0xFFFF00FF).text(text).position(point)
                .rotate(rotate);
        // 在地图上添加该文字对象并显示
        Overlay textOverlay = baiduMap.addOverlay(textOption);
        return textOverlay;
    }
    
    

    private void loadAnim() {
        Animation aIn = new AlphaAnimation(0f, 0f);
        aIn.setDuration(1);
        aIn.setFillAfter(true);
        btn_rec_shadow.startAnimation(aIn);
    }

    public BDLocationListener myListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;

            //纬度
            localUser.setGps_WD(location.getLatitude());
            //localUser.setGps_WD(30.003583);
            //经度
            localUser.setGps_JD(location.getLongitude());
            //localUser.setGps_JD(106.246693);

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locData); // 设置定位数据
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory
                        .newLatLngZoom(ll, 18); // 设置地图中心点以及缩放级别
                baiduMap.animateMapStatus(u);
            }
        }
    };

    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开GPS
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll"); // 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000); // 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true); // 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true); // 返回的定位结果包含手机机头的方向
        locationClient.setLocOption(option);
    }

    protected void onDestroy() {
        //退出时销毁定位
        locationClient.stop();
        baiduMap.setMyLocationEnabled(false);
        super.onDestroy();
        mMapView.onDestroy();
        mMapView = null;
    }

    protected void onResume() {
        System.out.println("onResume");
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        /**
         * 获取方向传感器
         * 通过SensorManager对象获取相应的Sensor类型的对象
         */
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        //应用在前台时候注册监听器
        manager.registerListener(listener, sensor,
                SensorManager.SENSOR_DELAY_GAME);


        Animation aIn1 = new AlphaAnimation(0f, 0f);
        aIn1.setDuration(1);
        aIn1.setStartOffset(1);
        aIn1.setFillAfter(true);
        img_rec_bkg.startAnimation(aIn1);

        walkToPoint(localUser.getGoWhereX(),localUser.getGoWhereY());
    }

    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
        //指南针传感器应用不在前台时候销毁掉监听器
        manager.unregisterListener(listener);
    }

    //指南针传感器
    private final class SensorListener implements SensorEventListener {
        private float predegree = 0;

        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;// 存放了方向值
            float y = values[1];
            if (y<-45){
                if (!ori_flag) {
                    Intent intent = new Intent(MapActivity.this, navActivity.class);
                    y = 0;
                    startActivity(intent);
                    int version = Integer.valueOf(android.os.Build.VERSION.SDK);
                    if (version > 5) {
                        overridePendingTransition(R.anim.fade, R.anim.hold);
                    }
                    ori_flag=true;
                }
            }else{
                ori_flag=false;
            }

            predegree = -values[0];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

}