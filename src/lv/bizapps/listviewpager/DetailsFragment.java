package lv.bizapps.listviewpager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class DetailsFragment extends Fragment {
	protected int id;

	@SuppressLint("ValidFragment")
	public DetailsFragment(int id) {
		super();

		this.id = id;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.details, container, false);

		TextView tv = (TextView)view.findViewById(R.id.textView1);
		tv.setText("DETAILS: "+this.id);

		try {
			InputStream is = getActivity().getAssets().open("file.txt");
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			String line;
			while((line = br.readLine()) != null) {
				tv.append(line);
				tv.append("\n");
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}

		return view;
	}
}
