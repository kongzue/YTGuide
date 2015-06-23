package cn.myzchh.YTGuide;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.voicerecognition.android.VoiceRecognitionClient;
import com.baidu.voicerecognition.android.VoiceRecognitionConfig;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.example.androidtestproject.testapp.R;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Arrays;
import java.util.List;

import cn.myzchh.YTGuide.util.BaseActivity;

public class recActivity extends BaseActivity {
    
    private TextView text_tip;
    
    private String yuyinJSON="";
    private String[] yuyinChoose;
    private VoiceRecognitionClient client;
    private BaiduASRDigitalDialog dlgVoiceRecognize;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec);

        text_tip=(TextView)findViewById(R.id.text_tip);
        startRec();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            client.speakFinish();
            Intent intent=new Intent(recActivity.this,MapActivity.class);
            startActivity(intent);
            int version = Integer.valueOf(android.os.Build.VERSION.SDK);
            if (version > 5) {
                overridePendingTransition(R.anim.fade, R.anim.hold);
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void startRec(){
        client= VoiceRecognitionClient.getInstance(recActivity.this);
        client.setTokenApis("x5vVhmsnoEN6OM1w6hbXgc6A", "3d3d824b4dc93c1edd465aa93bb9442a");

        VoiceRecognitionConfig config=new VoiceRecognitionConfig();
        config.setProp(VoiceRecognitionConfig.PROP_SEARCH);
        config.enableNLU();

        client.startVoiceRecognition(new VoiceRecognitionClient.VoiceClientStatusChangeListener() {
            @Override
            public void onClientStatusChange(int status, Object results) {
                switch (status){
                    case VoiceRecognitionClient.CLIENT_STATUS_SPEECH_START:
                        //开始说话
                        text_tip.setText("有什么能帮到您的？");
                        break;
                    case VoiceRecognitionClient.CLIENT_STATUS_START_RECORDING:
                        //录音中
                        text_tip.setText("正在听...");
                        break;
                    case VoiceRecognitionClient.CLIENT_STATUS_SPEECH_END:
                        //说话结束
                        text_tip.setText("让我想一想...");
                        break;
                    case VoiceRecognitionClient.CLIENT_STATUS_FINISH:
                        //成功
                        //text_tip.setText("FINISH");
                        List list=(List)results;
                        String str= Arrays.toString(null == list ? null : list.toArray());
                        //showMessageByToast(str);
                        str= str.substring(1,str.length()-1);
                        yuyinJSON=str;

                        String result="";
                        try {
                            JSONTokener jsonParser = new JSONTokener(str);
                            JSONObject loginmsg = (JSONObject) jsonParser.nextValue();
                            String str1=loginmsg.getString("json_res");
                            JSONTokener jsonParser1 = new JSONTokener(str1);
                            JSONObject loginmsg1 = (JSONObject) jsonParser1.nextValue();

                            result=loginmsg1.getString("raw_text");

                            String itemStr=loginmsg.getString("item");
                            itemStr=itemStr.replace("[\"","");
                            itemStr=itemStr.replace("\"]", "");
                            yuyinChoose=itemStr.split("\",\"");
                        } catch (Exception e){}
                        showMessageByToast(result);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNetworkStatusChange(int i, Object o) {

            }

            @Override
            public void onError(int i, int i1) {
                text_tip.setText("呃...好像出了点问题");
            }
        },config);

    }

    public void showMessageByToast(String Msg){
        Toast.makeText(recActivity.this, Msg, Toast.LENGTH_SHORT).show();
    }
}
