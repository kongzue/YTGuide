package cn.myzchh.YTGuide;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.androidtestproject.testapp.R;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import cn.myzchh.YTGuide.util.BaseActivity;
import cn.myzchh.YTGuide.util.localUser;

public class testActivity extends BaseActivity {

    private EditText textbox;
    private Button btn_search;
    private TextView text_result;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        textbox=(EditText)findViewById(R.id.textbox);
        btn_search=(Button)findViewById(R.id.btn_search);
        text_result=(TextView)findViewById(R.id.text_result);

        localUser.getLocal_path();
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Map> l= localUser.searchPath(textbox.getText().toString());
                for(int i = 0;i < l.size(); i ++) {
                    Map map = l.get(i);
                    text_result.setText(text_result.getText().toString()+map.get("name")+"\n");
                }
            }
        });

    }

}