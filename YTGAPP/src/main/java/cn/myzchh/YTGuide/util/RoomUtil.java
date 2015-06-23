package cn.myzchh.YTGuide.util;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by chao on 2015/5/22.
 */
public class RoomUtil {
    private static ArrayList<String> InviteArray;
    private static int InviteCount;

    public static void addInvite(String name){
        Log.i("addname",name);
        if (InviteArray==null){
            InviteArray=new ArrayList<String>();
        }
        for(String tmp:InviteArray){
            if (tmp.equals(name)){
                return;
            }
        }
        InviteArray.add(name);
        InviteCount=InviteArray.size();
        Log.i("count",InviteCount+"");
    }

    public static ArrayList<String> getInvite(){
        return  InviteArray;
    }

    public static int getInviteCount(){
        return InviteCount;
    }

}
