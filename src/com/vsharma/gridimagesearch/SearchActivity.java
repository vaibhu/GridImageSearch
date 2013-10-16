package com.vsharma.gridimagesearch;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity implements OnScrollListener{

	EditText etQuery;
	Button btnSearch;
	GridView gvResults;
	List<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultArrayAdapter imageAdapter;
	
	String size = "icon";
	String type = "face";
	int start = 0;
	boolean isLoading = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setUpViews();
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvResults.setAdapter(imageAdapter);
		gvResults.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View parent, int position,
					long rowId) {
				Intent intent = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				intent.putExtra("result", imageResult);
				startActivity(intent);
			}
			
		});
		gvResults.setOnScrollListener(this);
	}

	private void setUpViews() {
		etQuery = (EditText) findViewById(R.id.etQuery);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		gvResults = (GridView) findViewById(R.id.gvResults);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}
	
	public void onImageSearch(View v) {
		String query = etQuery.getText().toString();
		Toast.makeText(this, "Searching for "+query, Toast.LENGTH_SHORT).show();
		start = 0;
		String api = "http://ajax.googleapis.com/ajax/services/search/images?rsz=8&" +
				"start="+start+"&v=1.0&q="+Uri.encode(query)+"&imgsz="+size+"&imgtype="+type;
		Log.d("DEBUG", api);
		imageResults.clear();
		search(api);
	}
	
	public void loadMore(View v) {
		isLoading = true;
		String query = etQuery.getText().toString();
		start = start+8;
		String api = "http://ajax.googleapis.com/ajax/services/search/images?rsz=8&" +
				"start="+start+"&v=1.0&q="+Uri.encode(query)+"&imgsz="+size+"&imgtype="+type;
		Log.d("DEBUG", api);
		search(api);
	}
	
	private void search(String api) {
		isLoading = true;
		AsyncHttpClient client = new AsyncHttpClient();
		//https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=android
		
		client.get(api, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject response){
				Log.d("DEBUG", "success");
				JSONArray imageJsonResults = null;
				try {
					imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
					//imageResults.clear();
					imageAdapter.addAll(ImageResult.fromJsonArray(imageJsonResults));
					Log.d("DEBUG", imageJsonResults.toString());
					
				} catch(JSONException ex) {
					Log.d("DEBUG", "exception");
					ex.printStackTrace();
				}
			}
			
			/**
		     * Fired when a request returns successfully and contains a json array
		     * at the base of the response string. Override to handle in your
		     * own code.
		     * @param response the parsed json array found in the server response (if any)
		     */
		    public void onSuccess(JSONArray response) {
		    	Log.d("DEBUG", "2");
		    }

		    /**
		     * Fired when a request returns successfully and contains a json object
		     * at the base of the response string. Override to handle in your
		     * own code.
		     * @param statusCode the status code of the response
		     * @param response the parsed json object found in the server response (if any)
		     */
		    public void onSuccess(int statusCode, JSONObject response) {
		    	Log.d("DEBUG", "3");
		        onSuccess(response);
		    }


		    /**
		     * Fired when a request returns successfully and contains a json array
		     * at the base of the response string. Override to handle in your
		     * own code.
		     * @param statusCode the status code of the response
		     * @param response the parsed json array found in the server response (if any)
		     */
		    public void onSuccess(int statusCode, JSONArray response) {
		    	Log.d("DEBUG", "4");
		        onSuccess(response);
		    }

		    public void onFailure(Throwable e, JSONObject errorResponse) {
		    	Log.d("DEBUG", "5");
		    }
		    public void onFailure(Throwable e, JSONArray errorResponse) {
		    	Log.d("DEBUG", "6");
		    }


		    //
		    // Pre-processing of messages (executes in background threadpool thread)
		    //

		    @Override
		    protected void sendSuccessMessage(int statusCode, String responseBody) {
		    	Log.d("DEBUG", "sendSuccessMessage "+responseBody);
		        try {
		            Object jsonResponse = parseResponse(responseBody);
		            sendMessage(obtainMessage(SUCCESS_JSON_MESSAGE, new Object[]{statusCode, jsonResponse}));
		        } catch(JSONException e) {
		            sendFailureMessage(e, responseBody);
		        }
		    }
			
			@Override
		    protected void handleMessage(Message msg) {
				Log.d("DEBUG", "handleMessage "+msg);
		        switch(msg.what){
		            case SUCCESS_JSON_MESSAGE:
		                Object[] response = (Object[]) msg.obj;
		                handleSuccessJsonMessage(((Integer) response[0]).intValue(), response[1]);
		                break;
		            default:
		                super.handleMessage(msg);
		        }
		    }

		    protected void handleSuccessJsonMessage(int statusCode, Object jsonResponse) {
		    	Log.d("DEBUG", "handleSuccessJsonMessage "+jsonResponse);
		        if(jsonResponse instanceof JSONObject) {
		            onSuccess(statusCode, (JSONObject)jsonResponse);
		        } else if(jsonResponse instanceof JSONArray) {
		            onSuccess(statusCode, (JSONArray)jsonResponse);
		        } else {
		            onFailure(new JSONException("Unexpected type " + jsonResponse.getClass().getName()), (JSONObject)null);
		        }
		    }

		    protected Object parseResponse(String responseBody) throws JSONException {
		    	Log.d("DEBUG", "parseResponse "+responseBody);
		        Object result = null;
		        //trim the string to prevent start with blank, and test if the string is valid JSON, because the parser don't do this :(. If Json is not valid this will return null
		                responseBody = responseBody.trim();
		                if(responseBody.startsWith("{") || responseBody.startsWith("[")) {
		                        result = new JSONTokener(responseBody).nextValue();
		                }
		                if (result == null) {
		                        result = responseBody;
		                }
		                return result;
		    }

		    @Override
		    protected void handleFailureMessage(Throwable e, String responseBody) {
		    	Log.d("DEBUG", "error messgae "+e.getLocalizedMessage());
		        try {
		            if (responseBody != null) {
		                Object jsonResponse = parseResponse(responseBody);
		                if(jsonResponse instanceof JSONObject) {
		                    onFailure(e, (JSONObject)jsonResponse);
		                } else if(jsonResponse instanceof JSONArray) {
		                    onFailure(e, (JSONArray)jsonResponse);
		                } else {
		                    onFailure(e, responseBody);
		                }
		            }else {
		                onFailure(e, "");
		            }
		        }catch(JSONException ex) {
		            onFailure(e, responseBody);
		        }
		    }
		});
		
		isLoading = false;
	}
	
	public void setSettings(MenuItem mi) {
		Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
		startActivityForResult(intent, 100);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 100) {
			if(resultCode==RESULT_OK) {
				size = data.getExtras().getString("size");
				type = data.getExtras().getString("type");
				Toast.makeText(this, data.getExtras().getString("engine"), Toast.LENGTH_SHORT).show();
			}
		}
	}

	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		Log.d("DEBUG", "firstVisibleItem:"+firstVisibleItem+"visibleItemCount:"+visibleItemCount+"totalItemCount:"+totalItemCount);
		//loadMore(view);
		if(isLoading && totalItemCount==0)
			return;
		String query = etQuery.getText().toString();
		start = totalItemCount;
		String api = "http://ajax.googleapis.com/ajax/services/search/images?rsz=8&" +
				"start="+start+"&v=1.0&q="+Uri.encode(query)+"&imgsz="+size+"&imgtype="+type;
		Log.d("DEBUG", api);
		
	
		if(firstVisibleItem==0 && totalItemCount==visibleItemCount)
			search(api);
		else if((totalItemCount - visibleItemCount) <= (firstVisibleItem + 8))
			search(api);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		Log.d("DEBUG", "onScrollStateChanged");
		
	}
	
}
