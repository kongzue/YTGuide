package cn.myzchh.YTGuide.util.sqlliteDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chao on 2015/5/19.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        //假设拥有的用户数据库
        db.execSQL("CREATE TABLE IF NOT EXISTS local_user" +
                "(id INT,local_name VARCHAR, local_uuid VARCHAR)");
        db.execSQL("INSERT INTO local_user VALUES(0,?, ?)", new Object[]{"", ""});
    }

    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("CREATE TABLE IF NOT EXISTS localuser" +
//                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, password VARCHAR,phone VARCHAR)");

    }
}