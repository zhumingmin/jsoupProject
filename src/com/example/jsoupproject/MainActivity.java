package com.example.jsoupproject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.example.catchproject.CatchActivity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class MainActivity extends ActionBarActivity {

	// String url = "http://laeudora.com/iwebinfo";
	String url = "http://news.163.com/";

	Button bt1, csdn;
	TextView tv1, tv2, fangwenyuanwen;
	String title;
	String description;
	String link;
	ImageView iv1;
	String imageUrl;
	Bitmap bm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		bt1 = (Button) findViewById(R.id.bttitle);
		csdn = (Button) findViewById(R.id.bt_csdn);
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		iv1 = (ImageView) findViewById(R.id.iv1);

		fangwenyuanwen = (TextView) findViewById(R.id.fangwenyuanwen);
		csdn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, CatchActivity.class);

				startActivity(intent);

			}
		});
		bt1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(runnable).start();
			}

		});
		fangwenyuanwen.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it = new Intent(Intent.ACTION_VIEW);
				it.setData(Uri.parse(link)); // 这里面是需要调转的rul
				it = Intent.createChooser(it, null);
				startActivity(it);
			}
		});

	}

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			try {
				Document doc = Jsoup.connect(url).get();
				Elements imgElements = doc.getElementsByTag("img");
				imageUrl = imgElements.attr("src");
				bm = returnBitMap(imageUrl);

				Elements textElements = doc.getElementsByClass("mod_top_news");
				description = textElements.text();
				// description = doc.text();
				title = doc.title();
				link = doc.baseUri();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handler.sendEmptyMessage(0);
		}
	};

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			tv1.setText(title);
			tv2.setText(description);
			iv1.setImageBitmap(bm);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public Bitmap returnBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}