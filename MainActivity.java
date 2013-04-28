package jp.co.map;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;
 
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
// import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
// import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
// import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

 
public class MainActivity extends FragmentActivity {
 

  
	DatabaseHelper DTH;
	TextView tx;
	public static String data;
	public static String data2;
    private static final String TAG_MAP_FRAGMENT = "MAP_FRAGMENT";
    private static LatLng TOKYO = new LatLng(35.681382, 139.766084);
    private static LatLng tmpPosition = new LatLng(35.681382, 139.766084);
    ArrayList<LatLng>location = new ArrayList<LatLng>();

    private SupportMapFragment mMapFragment;
    public GoogleMap mMap;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        //データベースの作成
        DTH = new DatabaseHelper(this);
        // 登録したタグから MapFragment を取得する（デバイス回転などの再生成対策）
        mMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentByTag(TAG_MAP_FRAGMENT);
        if (mMapFragment == null) {
            // MapFragment がなければ作成する
            mMapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, mMapFragment, TAG_MAP_FRAGMENT)
                    .commit();
            
            
        }
    }
 
    @Override
    protected void onResume() {
        super.onResume();
 
        if (mMap == null) {
            // MapFragment から GoogleMap を取得する
            mMap = mMapFragment.getMap();
            if (mMap != null) {
                // マップをハイブリッド表示にする
      //          mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
 
                // 屋内マップ表示を無効にする（標準は true）
                mMap.setIndoorEnabled(true);
 
                // 現在地表示ボタンを有効にする
                mMap.setMyLocationEnabled(true);
                // UiSettings にボタン表示設定があるが標準は true なので設定不要
                 mMap.getUiSettings().setMyLocationButtonEnabled(true);
                
                 //　READメソッドの呼び出し
                read();
                
                // カメラの位置を東京駅に変える
                this.moveCameraToTokyo(false);
 
                // 地図の長押しでカメラを東京駅まで移動する
               /* mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng point) {
                        moveCameraToTokyo(true);
                    }
                });*/
            }
        }
    }
 
    /**
     * カメラを東京駅に移動する
     * 
     * @param isAnimation
     *            アニメーション移動するかの判定。true でアニメーション移動。
     */
    
    private void moveCameraToTokyo(boolean isAnimation) {

    	
        // TODO 起動時の位置の設定の変更
    	
    	//TOKYO = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
        CameraUpdate camera = CameraUpdateFactory
                .newCameraPosition(new CameraPosition.Builder()
                        .target(TOKYO)
                        .zoom(10.0f).build());
        if (isAnimation) {
            // アニメーション移動する
            mMap.animateCamera(camera);
        } else {
            // 瞬間移動する
            mMap.moveCamera(camera);
        }
        
     // リスナーをセット
        mMap.setOnMapClickListener(new OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                String text = "latitude=" + point.latitude + ", longitude=" + point.longitude;
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        }); 
        mMap.setOnMapLongClickListener(new OnMapLongClickListener() {
        	
        	
            @Override
            // 長押しのイベントハンドラ
            public void onMapLongClick(LatLng point) {
            	
            	// タップした場所の緯度と経度の情報をMyLocationに入れる
            	final LatLng MyLocation = new LatLng(point.latitude, point.longitude);
                final String text = "latitude（緯度）=" + point.latitude + ", longitude（経度）=" + point.longitude;
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                
            
                // Pinを立てる
/*                mMap.addMarker(new MarkerOptions()
                .position(MyLocation)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
  */              
				Intent intent = new Intent(MainActivity.this, Page2Activity.class);
				// 値を遷移先に渡す
				intent.putExtra("latitude", MyLocation.latitude);
				intent.putExtra("longitude", MyLocation.longitude);
				MainActivity.this.startActivity(intent);
				

                //Pinをタッチしたら画面遷移
				/*
                mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
        			
        			@Override
        			public boolean onMarkerClick(Marker marker) {
        			
        				return false;
        			}
        		});*/
                
            }
        });
        
      
      /*  mMap.setOnCameraChangeListener(new OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
                LatLng point = position.target;
                String text = "latitude=" + point.latitude + ", longitude=" + point.longitude;
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        });*/
    }
    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	boolean ret = super.onCreateOptionsMenu(menu);
    	menu.add(0 , Menu.FIRST , Menu.NONE , "メニュー1");
    	menu.add(0 , Menu.FIRST + 1 ,Menu.NONE , "メニュー2");
    	return ret;
    	}
    
    //指定ミリ秒実行を止めるメソッド
    public synchronized void sleep(long msec)
      {	
      	try
      	{
      		wait(msec);
      	}catch(InterruptedException e){}
      }
    
	private void read() {
		//tx = (TextView)findViewById(R.id.textView1);
		SQLiteDatabase ReadDb = DTH.getReadableDatabase();
    	String query = "SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='diary';";
    	Cursor c = ReadDb.rawQuery(query, null); 
    	c.moveToFirst();
    	String result = c.getString(0);
    	
    	// テーブルがあったら
    	if(Integer.parseInt(result)!=0){

    		Cursor cursor = ReadDb.query("diary", new String[] { "title","diary","id","latitude","longitude","date"}, null, null, null, null,
    		                null);
    		cursor.moveToFirst();
    		// StringBuilder sb = new StringBuilder();
    	        for (int i = 0; i < cursor.getCount(); i++) {
  
    	        	tmpPosition = new  LatLng(cursor.getDouble(3), cursor.getDouble(4));
        	        // DBからの情報でマーカーの作成
    	        	mMap.addMarker(new MarkerOptions()
    	            	.position(tmpPosition)
    	            	.title("TITLE:" + cursor.getString(0))
    	            	.snippet("内容:" + cursor.getString(1))
    	            	.icon(BitmapDescriptorFactory
    	                  .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

    	            cursor.moveToNext();
    	        }
    	        cursor.close();

   //tx.setText(sb.toString());
    		}else{
    			// TABLEがなかったらTABLEの作成
    		String table_Create = "CREATE  TABLE diary (" +
    				"id INTEGER PRIMARY KEY  AUTOINCREMENT," +
    				 "date TEXT," +
    				 "latitude real," +
    				 "longitude  real," +
    				 "title TEXT," +
    				 "diary TEXT" + ")";
    		ReadDb.execSQL(table_Create);
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
			
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// 自動生成されたメソッド・スタブ
			
			db.execSQL(table_Create);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// 自動生成されたメソッド・スタブ
	        db.execSQL("DROP TABLE IF EXISTS diary");
	        onCreate(db);
			
		}
		
	}
	}





