package lv.bizapps.listviewpager;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.ListFragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class TitlesListFragment extends ListFragment {
	protected TitlesListFragment lf = this;
	protected View footerView;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		final LayoutInflater li = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.footerView = li.inflate(R.layout.cust_view, null);

		Button b = (Button)this.footerView.findViewById(R.id.button1);
		b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.e("AAA", "1111111111111111111111");

				//getListView().invalidateViews();

				getListView().removeFooterView(footerView);
				View loadingItemsView = li.inflate(R.layout.loading_items, null);
				getListView().addFooterView(loadingItemsView);

				new Thread(new ItemsReloader(getActivity(), lf, loadingItemsView)).start();

				//new ItemsAsyncReloader(getActivity()).execute(lf);
			}
		});

		getListView().addFooterView(this.footerView);

		setListAdapter(new TitlesListAdapter(getActivity()));
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		setSelection(position);

		((MainActivity)getActivity()).vp.setCurrentItem(position);
	}
}

class ItemsReloader implements Runnable {
	protected Activity activity;
	protected TitlesListFragment listFragment;
	protected View liv;

	public ItemsReloader(Activity activity, TitlesListFragment listFragment, View liv) {
		this.activity = activity;
		this.listFragment = listFragment;
		this.liv = liv;
	}

	@Override
	public void run() {
		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

		try {
			Thread.sleep(3000);
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		this.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				final LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				listFragment.getListView().removeFooterView(liv);

				final View footerView = inflater.inflate(R.layout.cust_view, null);
				Button b = (Button)footerView.findViewById(R.id.button1);
				b.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.e("AAA", "INTERNAL CLICK");

						listFragment.getListView().removeFooterView(footerView);
						View loadingItemsView = inflater.inflate(R.layout.loading_items, null);
						listFragment.getListView().addFooterView(loadingItemsView);

						new Thread(new ItemsReloader(activity, listFragment, loadingItemsView)).start();
					}
				});

				listFragment.getListView().addFooterView(footerView);
			}
		});
	}
}

class TitlesListAdapter extends BaseAdapter{
	protected Context ctx;
	protected LayoutInflater li;

	public TitlesListAdapter(Context ctx) {
		this.ctx = ctx;

		this.li = (LayoutInflater)this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return 30;
	}

	@Override
	public Object getItem(int id) {
		return id;
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int id, View view, ViewGroup viewGroup) {
		//view = this.li.inflate(android.R.layout.simple_list_item_activated_1, viewGroup, false);
		//((TextView)view.findViewById(android.R.id.text1)).setText(Html.fromHtml("<b>POS:</b> "+id));

		//Log.e("AAA", "CREATE LIST POS: "+id);

		view = this.li.inflate(R.layout.list_row, viewGroup, false);
		((TextView)view.findViewById(R.id.textView1)).setText(Html.fromHtml("<b>POS:</b> "+id));

		return view;
	}

	@Override
	public boolean isEnabled(int position) {
		return super.isEnabled(position);
	}
}
