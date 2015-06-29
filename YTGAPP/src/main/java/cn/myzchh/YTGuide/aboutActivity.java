package cn.myzchh.YTGuide;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidtestproject.testapp.R;

import cn.myzchh.YTGuide.util.BaseActivity;

public class aboutActivity extends BaseActivity {

    private ImageView button_Back;
    private TextView text_website;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        text_website=(TextView)findViewById(R.id.text_website);
        button_Back=(ImageView)findViewById(R.id.button_Back);

        button_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(aboutActivity.this,firstActivity.class);
                startActivity(intent);
                int version = Integer.valueOf(android.os.Build.VERSION.SDK);
                if (version > 5) {
                    overridePendingTransition(R.anim.fade, R.anim.hold);
                }
                finish();
            }
        });

        text_website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://ytguide.sinaapp.com/"; // web address
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent=new Intent(aboutActivity.this,firstActivity.class);
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

}
