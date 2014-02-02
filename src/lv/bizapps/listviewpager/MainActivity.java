package lv.bizapps.listviewpager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {
	public ViewPager vp;
	protected TitlesListFragment titlesList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		FragmentManager fm = getSupportFragmentManager();
		this.titlesList = (TitlesListFragment)fm.findFragmentById(R.id.titlesFragment);

		this.vp = (ViewPager)findViewById(R.id.viewPager1);
		this.vp.setAdapter(new MyPageAdapter(getSupportFragmentManager()));
		this.vp.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int id) {
			}

			@Override
			public void onPageScrolled(int pos, float posOffset, int posOffetPixs) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				if(state == ViewPager.SCROLL_STATE_IDLE) titlesList.setSelection(vp.getCurrentItem());
			}
		});

		ActionBar ab = getSupportActionBar();
		ab.show();
	}
	
	@SuppressLint("NewApi")
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch(item.getItemId()) {
			case R.id.action_search:	
										View searchView = ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
																				.inflate(R.layout.search_line, null);
			
										((Button)searchView.findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												Log.e("AAA", "ON SEARCH");
											}
										});
										item.setActionView(searchView);
										
										//item.setActionView(R.layout.search_line);
										return true;

			case R.id.action_refresh:	item.setActionView(R.layout.progress_bar);

										new Thread(new IconChanger(this, item)).start();

										return true;
			/*
			case R.id.search:	onSearchRequested();
								return true;
			*/
			default:			return false;
		}
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		/*
		if(Build.VERSION.SDK_INT >= 11) {
	        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
	        SearchView searchView = (SearchView)menu.findItem(R.id.search).getActionView();
	        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	        searchView.setIconifiedByDefault(false);
	    }
		*/
		return true;
	}
}

class IconChanger implements Runnable {
	protected Activity act;
	protected MenuItem item;

	public IconChanger(Activity act, MenuItem item) {
		this.item = item;
		this.act = act;
	}

	@Override
	public void run() {
		//StringBuilder builder = new StringBuilder();

		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet("http://rutube.ru/api/search/video/?format=json&page=1&query="+Uri.encode("хб"));

			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if(statusCode == 200) {
				HttpEntity entity = response.getEntity();
				/*
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while((line = reader.readLine()) != null) {
					builder.append(line);
				}
				*/
				JSONObject json = new JSONObject(EntityUtils.toString(entity));
									//new JSONObject(builder.toString());

				JSONArray res = json.getJSONArray("results");

				Log.e("AAA", res.length()+"");

				for(int i=0; i<res.length(); i++) {
					JSONObject obj = res.getJSONObject(i);
					
					Log.e("AAA", obj.getString("thumbnail_url"));//"ID: "+obj.getString("id")
				}
			}
			else {
				Log.e("AAA", "Failed to download JSON");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		this.act.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				item.setActionView(null);
				item.setIcon(R.drawable.ic_action_refresh);
			}
		});
	}
}

/*
class ListReloader extends AsyncTask<TitlesListFragment, Void, Void> {
	protected TitlesListFragment tlf;

	@Override
	protected Void doInBackground(TitlesListFragment... params) {
		this.tlf = params[0];

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		this.tlf.getListView().invalidateViews();

		super.onPostExecute(result);
	}
}
*/
class MyPageAdapter extends FragmentStatePagerAdapter {
	public MyPageAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int id) {
		return new DetailsFragment(id);
	}

	@Override
	public int getCount() {
		return 30;
	}
}
