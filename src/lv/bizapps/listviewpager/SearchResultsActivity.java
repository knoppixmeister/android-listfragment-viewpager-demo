package lv.bizapps.listviewpager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SearchResultsActivity extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
    	if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
           // String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
    	}
    }
}
