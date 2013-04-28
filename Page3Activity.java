package jp.co.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.map.R.id;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;

import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Page3Activity extends Activity{
  	ArrayList<String> list = new ArrayList<String>();

	int j=0;
	TextView tx;
	DatabaseHelper DTH;
	ListView listView;
	List<Map<String, String>> retDataList = new ArrayList<Map<String, String>>();
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitysub2);
		

		  
		 
		
		//DBの作成
		DTH = new DatabaseHelper(this);
		//最新の状態にソート
		update();
		read();

		Button button1 = (Button)findViewById(R.id.Backbutton1);
		Button button2 = (Button)findViewById(R.id.deletebutton1);
        listView = (ListView) findViewById(id.listview);			
		  // リストビューのアイテムがクリックされた時に呼び出されるコールバックリスナーを登録します
        
     listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long Id) {
            	final SQLiteDatabase DeleteDb = DTH.getReadableDatabase();
            
            	Id = Id + 1;
            	
            	final String SelectedDrop = String.format("DELETE FROM diary WHERE id = '%s'",
            										Id); 
            	
                
                AlertDialog.Builder dlg;
                
                dlg = new AlertDialog.Builder(Page3Activity.this);
                dlg.setTitle("項目の削除");
                dlg.setMessage("NO："+Id+"を削除しますか？");
                // アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
                dlg.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            	DeleteDb.execSQL(SelectedDrop);
                                //画面遷移
                    				Intent intent = new Intent(Page3Activity.this, MainActivity.class);
                    				Page3Activity.this.startActivity(intent);

                            }
                        });
                
                dlg.setNeutralButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            	
                            }
                        });
                dlg.show();
                


                return false;
            }
        });
		
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
	//			Intent intent = new Intent(Page3Activity.this, Page2Activity.class);
		//		Page3Activity.this.startActivity(intent);
				// TODO メインのページに画面遷移
				Intent intent = new Intent(Page3Activity.this, MainActivity.class);
				Page3Activity.this.startActivity(intent);
	
			}
		});

		button2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				delete();
			}
		});
	}
	
	private void update() {
		// TODO 自動生成されたメソッド・スタブ
		SQLiteDatabase ReadDb = DTH.getReadableDatabase();
    	String query = "SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='diary';";
    	Cursor c = ReadDb.rawQuery(query, null); 
    	c.moveToFirst();
    	String result = c.getString(0);
    	
    	if(Integer.parseInt(result)==0){
    		//tx.setText("日記の記録はないよ");
    		}else{
    		Cursor cursor = ReadDb.query("diary", new String[] { "id"}, null, null, null, null,
    		                null);
    		cursor.moveToFirst();
    		int change1=100;
    	
    	        for (int i = 0; i < cursor.getCount(); i++) {
    	            ReadDb.execSQL("update diary"+" set id = '"+Integer.toString(change1)+"' WHERE (id="+cursor.getString(0)+")");
    	            change1+=1;
    	            cursor.moveToNext();
    	        }
    
    	            cursor.moveToFirst();
    	            change1=100;
    	            int change2=1;
    	            for (int i = 0; i < cursor.getCount(); i++){
    	                ReadDb.execSQL("update diary set id = '"+Integer.toString(change2)+"' WHERE (id="+Integer.toString(change1)+")");
    	                change1+=1;
    	                change2+=1;
    	                 cursor.moveToNext();
    	            }
    	        }
    
    	        }
		        // アダプターを設定します

	

	private void delete() {
		// TODO 自動生成されたメソッド・スタブ
		SQLiteDatabase DeleteDb = DTH.getReadableDatabase();
        DeleteDb.execSQL("DROP TABLE IF EXISTS diary");
        
	}
	private void read() {
		// TODO 自動生成されたメソッド・スタブ
		SQLiteDatabase ReadDb = DTH.getReadableDatabase();
    	String query = "SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='diary';";
    	Cursor c = ReadDb.rawQuery(query, null); 
    	c.moveToFirst();
    	String result = c.getString(0);
    	
    	if(Integer.parseInt(result)==0){
    		//tx.setText("日記の記録はないよ");
    		}else{
    		Cursor cursor = ReadDb.query("diary", new String[] { "title","diary","id","latitude","longitude","date"}, null, null, null, null,
    		                null);
    		cursor.moveToFirst();
    		
    		//アダプターの準備
    		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

    		 Map<String, String> data = new HashMap<String, String>();
    		 
    		
    	        for (int i = 0; i < cursor.getCount(); i++) {
    	        
    	            data = new HashMap<String, String>();
    	            data.put("no", "NO:" + cursor.getInt(2));
    	            data.put("latitude", "緯度:" + cursor.getString(3));
    	            data.put("longitude", "経度:" + cursor.getString(4));
    	            data.put("date", "日付:" + cursor.getString(5));
    	            data.put("title", "TITLE:" + cursor.getString(0));
    	            data.put("diary", "内容:" + cursor.getString(1));
    	            retDataList.add(data);
    		        cursor.moveToNext();
    
    	        }
    	        cursor.close();
		        // アダプターを設定します

		        // リストビューに渡すアダプタを生成します。
		        SimpleAdapter adapter2 = new SimpleAdapter(this, retDataList,
		          R.layout.low, new String[] { "no", "latitude" ,"longitude","date","title","diary"},
		          new int[] {R.id.textView1, R.id.textView2 , R.id.textView3 , R.id.textView4, R.id.textView5 , R.id.textView6});
		       
		        // アダプタを設定します。
		        listView = (ListView) findViewById(id.listview);
		        listView.setAdapter(adapter2);
		        //listView.setAdapter(adapter);

    	      //  tx.setText(sb.toString());
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
				 "longitude  real," +
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



