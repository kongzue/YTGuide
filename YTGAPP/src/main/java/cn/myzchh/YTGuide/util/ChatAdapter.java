package cn.myzchh.YTGuide.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidtestproject.testapp.R;

import java.util.List;


/**
 * Created by chao on 2015/5/22.
 */
public class ChatAdapter extends BaseAdapter {

    public static final String KEY = "key";
    public static final String VALUE = "value";
    public static final String NAME = "name";
    public static final String FACE = "face";

    public static final int VALUE_NULL = 0;
    public static final int VALUE_LEFT = 1;
    public static final int VALUE_RIGHT = 2;
    private LayoutInflater mInflater;

    private List<Message> myList;

    public ChatAdapter(Context context, List<Message> myList) {
        this.myList = myList;

        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return myList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {

        Message msg = myList.get(position);
        int type = getItemViewType(position);
        ViewHolderRightText holderRightText = null;
        ViewHolderLeftText holderLeftText = null;

        if (convertView == null) {
            switch (type) {
                case VALUE_LEFT:
                    holderLeftText = new ViewHolderLeftText();
                    convertView = mInflater.inflate(R.layout.message_listitem_left,
                            null);
                    holderLeftText.ivLeftIcon = (ImageView) convertView
                            .findViewById(R.id.image_face);
                    holderLeftText.btnLeftText = (TextView) convertView
                            .findViewById(R.id.text_Body);
                    holderLeftText.btnLeftText.setText(msg.getValue());
                    holderLeftText.btnLeftName = (TextView) convertView
                            .findViewById(R.id.text_Name);
                    holderLeftText.btnLeftName.setText(msg.getName());
                    holderLeftText.ivLeftIcon=(ImageView) convertView
                            .findViewById(R.id.image_face);
                    holderLeftText.ivLeftIcon.setImageResource(localUser.getUserFaceByRES(msg.getName()));
                    convertView.setTag(holderLeftText);

                    break;
                case VALUE_RIGHT:
                    holderRightText= new ViewHolderRightText();
                    convertView = mInflater.inflate(R.layout.message_listitem_right,
                            null);
                    holderRightText.ivRightIcon = (ImageView) convertView
                            .findViewById(R.id.image_face);
                    holderRightText.btnRightText = (TextView) convertView
                            .findViewById(R.id.text_Body);
                    holderRightText.btnRightText.setText(msg.getValue());
                    holderRightText.btnRightName = (TextView) convertView
                            .findViewById(R.id.text_Name);
                    holderRightText.btnRightName.setText(msg.getName());
                    holderRightText.ivRightIcon=(ImageView) convertView
                            .findViewById(R.id.image_face);
                    holderRightText.ivRightIcon.setImageResource(localUser.getUserFaceByRES(msg.getName()));
                    convertView.setTag(holderRightText);
                    break;
                case VALUE_NULL:

                    break;
                default:
                    break;
            }

        } else {
            Log.d("baseAdapter", "Adapter_:" + (convertView == null));
            switch (type) {
                case VALUE_LEFT:
                    holderLeftText = (ViewHolderLeftText) convertView.getTag();
                    holderLeftText.btnLeftText.setText(msg.getValue());
                    holderLeftText.btnLeftName.setText(msg.getName());
                    holderLeftText.ivLeftIcon.setImageResource(localUser.getUserFaceByRES(msg.getName()));
                    break;
                case VALUE_RIGHT:
                    holderRightText = (ViewHolderRightText) convertView.getTag();
                    holderRightText.btnRightText.setText(msg.getValue());
                    holderRightText.btnRightName.setText(msg.getName());
                    holderRightText.ivRightIcon.setImageResource(localUser.getUserFaceByRES(msg.getName()));
                    break;
                default:
                    break;
            }

            //holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    /**
     * 根据数据源的position返回需要显示的的layout的type
     * <p>
     * type的值必须从0开始
     */
    @Override
    public int getItemViewType(int position) {

        Message msg = myList.get(position);
        int type = msg.getType();
        return type;
    }

    /**
     * 返回所有的layout的数量
     */
    @Override
    public int getViewTypeCount() {
        return 3;
    }

    class ViewHolderRightText {
        private ImageView ivRightIcon;// 右边的头像
        private TextView btnRightText;// 右边的文本
        private TextView btnRightName;// 右边的文本
    }

    class ViewHolderLeftText {
        private ImageView ivLeftIcon;// 左边的头像
        private TextView btnLeftText;// 左边的文本
        private TextView btnLeftName;// 右边的文本
    }

}