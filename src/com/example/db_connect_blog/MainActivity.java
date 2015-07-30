package com.example.db_connect_blog;

import java.io.BufferedReader; 
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener{
	Button fetch;
	TextView text;
	EditText et;
	EditText et1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		fetch= (Button) findViewById(R.id.fetch);
		text = (TextView) findViewById(R.id.text);
		et = (EditText) findViewById(R.id.et);
		et1 = (EditText) findViewById(R.id.et1);
		
		fetch.setOnClickListener(this);
	}
	
	class task extends AsyncTask<String, String, Void>
	{
	private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
	    InputStream is = null ;
	    String result = "";
	    protected void onPreExecute() {
	       progressDialog.setMessage("Fetching data...");
	       progressDialog.show();
	       progressDialog.setOnCancelListener(new OnCancelListener() {
		@Override
			public void onCancel(DialogInterface arg0) {
			task.this.cancel(true);
		   }
		});
	     }
	       @Override
		protected Void doInBackground(String... params) {
		  String url_select = "http://www.troski.byethost7.com/troski.php";

		  HttpClient httpClient = new DefaultHttpClient();
		  HttpPost httpPost = new HttpPost(url_select);

	          ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

		    try {
			httpPost.setEntity(new UrlEncodedFormEntity(param));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();

			//read content
			is =  httpEntity.getContent();					

			} catch (Exception e) {

			Log.e("log_tag", "Error in http connection "+e.toString());
			//Toast.makeText(MainActivity.this, "Please Try Again", Toast.LENGTH_LONG).show();
			}
		try {
		    BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = "";
			while((line=br.readLine())!=null)
			{
			   sb.append(line+"\n");
			}
				is.close();
				result=sb.toString();				

					} catch (Exception e) {
						// TODO: handle exception
						Log.e("log_tag", "Error converting result "+e.toString());
					}

				return null;

			}
		protected void onPostExecute(Void v) {

			// ambil data dari Json database
			try {
				JSONArray Jarray = new JSONArray(result);
				for(int i=0;i<Jarray.length();i++)
				{
				JSONObject Jasonobject = null;
				//text_1 = (TextView)findViewById(R.id.txt1);
				Jasonobject = Jarray.getJSONObject(i);

				//get an output on the screen
				//String id = Jasonobject.getString("id");
				String name = Jasonobject.getString("name");
				String name1 = Jasonobject.getString("name1");
				String db_detail="";
				
				if(et.getText().toString().equalsIgnoreCase(name) && et1.getText().toString().equalsIgnoreCase(name1)) {
					db_detail = Jasonobject.getString("detail");
					text.setText(db_detail);
					break;
				
				}else 
	                {
	                   
						Context context = getApplicationContext();
						CharSequence text = "NOT AVAILABLE";
						int duration = Toast.LENGTH_LONG;

						Toast toast = Toast.makeText(context, text, duration);
						toast.show();
	                break;
	                }


				

				}
				this.progressDialog.dismiss();

			} catch (Exception e) {
				// TODO: handle exception
				Log.e("log_tag", "Error parsing data "+e.toString());
			}
		}
	    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.fetch : new task().execute();break;
		}
		
	}

}
