package cn.myzchh.YTGuide;

import android.content.Intent;
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
import android.widget.ImageView;

import com.example.androidtestproject.testapp.R;

import cn.myzchh.YTGuide.util.BaseActivity;

public class MapActivity extends BaseActivity {

    private ImageView btn_rec;
    private ImageView btn_rec_shadow;
    private ImageView img_rec_bkg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        btn_rec=(ImageView)findViewById(R.id.btn_rec);
        btn_rec_shadow=(ImageView)findViewById(R.id.btn_rec_shadow);
        img_rec_bkg=(ImageView)findViewById(R.id.img_rec_bkg);

//        img_rec_bkg.clearAnimation();
//        img_rec_bkg.setVisibility(View.GONE);

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
                        finish();
                    }
                    public void onAnimationRepeat(Animation animation) {}
                });
            }
        });
    }

    private void loadAnim() {
        Animation aIn = new AlphaAnimation(0f, 0f);
        aIn.setDuration(1);
        aIn.setFillAfter(true);
        btn_rec_shadow.startAnimation(aIn);
    }

}
