package jp.co.map;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Page2Activity extends Activity{
  
	
	double latitude;
	double longitude;
	DatabaseHelper DTH;
	EditText ed;
	EditText ed2;
	TextView tx1;
	GregorianCalendar cal = new GregorianCalendar(); 
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitysub1);
		//DBの作成
		DTH = new DatabaseHelper(this);
		Button button1 = (Button)findViewById(R.id.button1);
		Button button2 = (Button)findViewById(R.id.button2);
		Button button3 = (Button)findViewById(R.id.button3);
		tx1 = (TextView)findViewById(R.id.textView3);
		ed = (EditText)findViewById(R.id.editText1);
		ed2 = (EditText)findViewById(R.id.editText3);
		
		// INTENTでデータ渡し
		  Intent intent = getIntent();
		  
		  latitude = intent.getDoubleExtra("latitude",1.1);
		  longitude = intent.getDoubleExtra("longitude",1.1);
	      
		button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO メインのページに画面遷移
				Intent intent = new Intent(Page2Activity.this, MainActivity.class);
				Page2Activity.this.startActivity(intent);
				
			}
		});
		
		button2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO ページ3に画面遷移
				
				Intent intent = new Intent(Page2Activity.this, Page3Activity.class);
				Page2Activity.this.startActivity(intent);
	
			}
		});
		
		button3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 書き込み
				write();

			}

		});		
	}
	
	//WRITEメソッド
	private void write() {
		// TODO 自動生成されたメソッド・スタブ
		SQLiteDatabase writedb = DTH.getWritableDatabase();
    	String query = "SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='diary';";
    	Cursor c = writedb.rawQuery(query, null); 
    	c.moveToFirst();
    	String result = c.getString(0);
    	
    	if(Integer.parseInt(result)==0){
    		tx1.setText("diarytable ないから作ったよ！");
    		String table_Create = "CREATE  TABLE diary (" +
    				"id INTEGER PRIMARY KEY  AUTOINCREMENT," +
    				 "date TEXT," +
    				 "latitude real," +
    				 "longitude  real," +
    				 "title TEXT," +
    				 "diary TEXT" + ")";
    		
    		writedb.execSQL(table_Create);
    		}else{tx1.setText("diarytable あるから　insert　したよ");
    		
    		// 年月日の取得
    		String year = Integer.toString(cal.get(Calendar.YEAR));
    		int month = cal.get(Calendar.MONTH);
    		month++;
    		String Month = Integer.toString(month);
    		String day = Integer.toString(cal.get(Calendar.DATE));
    		String sql = String.format("insert into diary (date, latitude, longitude, title, diary)values('%s','%.7f','%.7f','%s','%s')",
    		    									year + "年" + Month + "月" + day + "日",
    									latitude,
    									longitude,
    									ed.getText().toString(),
    									ed2.getText().toString()
    									
    									
    									);
    		writedb.execSQL(sql);
    		
    		//　ページ3に画面遷移
			Intent intent = new Intent(Page2Activity.this, Page3Activity.class);
			Page2Activity.this.startActivity(intent);
    		}
	}
	
	//DBを操作するためのクラス
public class DatabaseHelper extends SQLiteOpenHelper	{
	//DBの作成
	private static final String DATABASE_NAME = "diary_db";
	String table_Create = "CREATE  TABLE diary (" +
			"id INTEGER PRIMARY KEY  AUTOINCREMENT," +
			 "date TEXT," +
			 "latitude real," +
			 "longitude real," +
			 "title TEXT," +
			 "diary TEXT" + ")";
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 自動生成されたメソッド・スタブ
		
		db.execSQL(table_Create);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 自動生成されたメソッド・スタブ
        db.execSQL("DROP TABLE IF EXISTS diary");
        onCreate(db);
		
	}
	
}
}

