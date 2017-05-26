package bilbo.arunwebnerd.com.bilbo;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.*;

public class FirstFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.first_frag, container, false);

        EditText tvTotal = (EditText) v.findViewById(R.id.tvTotal);
        tvTotal.setText(getString(R.string.total_prompt));

        EditText tvNumberPeople = (EditText) v.findViewById(R.id.tvNumPeople);
        tvNumberPeople.setText(getString(R.string.people_prompt));

        Spinner spinTipPercent = (Spinner) v.findViewById(R.id.spinnerTipPercent);

        return v;
    }

    public static FirstFragment newInstance(String text) {

        FirstFragment f = new FirstFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);
		return f;
		}
	}	
