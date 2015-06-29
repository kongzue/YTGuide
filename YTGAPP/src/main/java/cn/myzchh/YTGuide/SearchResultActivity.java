package cn.myzchh.YTGuide;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.androidtestproject.testapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.myzchh.YTGuide.util.BaseActivity;
import cn.myzchh.YTGuide.util.localUser;

public class SearchResultActivity extends BaseActivity {

    private EditText edit_searchbox;
    private ImageView btn_back;
    private Button btn_search;
    private ListView list_result;

    private List<Map<String,Object>> resultDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        edit_searchbox=(EditText)findViewById(R.id.edit_searchbox);
        btn_back=(ImageView)findViewById(R.id.btn_back);
        btn_search=(Button)findViewById(R.id.btn_search);
        list_result=(ListView)findViewById(R.id.list_result);

        Intent i=getIntent();
        String searchP=i.getStringExtra("searchStr");
        if (searchP!=null){
            edit_searchbox.setText(searchP);
            doSearch();
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMe();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch();
            }
        });

        list_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (resultDatas!=null){
                    Map m=resultDatas.get(position);
                    localUser.setGoWhereX((double)m.get("x"));
                    localUser.setGoWhereY((double) m.get("y"));
                    closeMe();
                }
            }
        });
    }

    private void doSearch() {
        localUser.getLocal_path();
        List<Map> l= localUser.searchPath(edit_searchbox.getText().toString());
        if (resultDatas==null){
            resultDatas=new ArrayList<Map<String,Object>>();
        }
        for(int i = 0;i < l.size(); i ++) {
            Map map = l.get(i);
            double x=(double)map.get("x");
            double y=(double)map.get("y");
            double distance=Distance(localUser.getGps_WD(),localUser.getGps_JD(),x,y);
            map.put("distance","距离"+(int)distance+"米");
            resultDatas.add(map);
        }
        list_result.setAdapter(new SimpleAdapter(SearchResultActivity.this,resultDatas,R.layout.item_search_result,
                new String[]{"name","distance"},
                new int[]{R.id.text_name,R.id.text_dis}));
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

    private void closeMe() {
        finish();
        overridePendingTransition(R.anim.fade, R.anim.back);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            closeMe();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
