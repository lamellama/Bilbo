package bilbo.arunwebnerd.com.bilbo;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

import android.widget.TextView;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;

public class MainActivity extends AppCompatActivity implements FirstFragment.OnInputUpdateListener{
	
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
  //  private SectionsPagerAdapter mSectionsPagerAdapter;
	private MyPagerAdapter mPageAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private CustomViewpager mViewPager;
	
	private static final String TAG = "MainActivity";
	
	private Bundle secondFragBundle;
	
	private int numPeople;
	private int tipPercent;
	private float billTotal;
	
	SecondFragment secondFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
		
		secondFragBundle = new Bundle();
		secondFragment = SecondFragment.newInstance("SecondFragment, Instance 1");
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mPageAdapter = new MyPagerAdapter(getSupportFragmentManager(), secondFragment);
		
        // Set up the ViewPager with the sections adapter.
        mViewPager = (CustomViewpager) findViewById(R.id.container);
        mViewPager.setAdapter(mPageAdapter);
		mViewPager.setSwipeEnabled(false);
		
		
		tipPercent = getResources().getInteger(R.integer.tip_default);
		billTotal = Float.parseFloat( getResources().getString(R.string.total_default));
		numPeople = getResources().getInteger(R.integer.numpeople_default);
		// Initialise bundle before second fragment is created
		initSecondFrag(numPeople, tipPercent, billTotal);
		

    }
	private class MyPagerAdapter extends FragmentPagerAdapter {
		private Fragment secondFragment;
        public MyPagerAdapter(FragmentManager fm, Fragment frag) {
			
            super(fm);
			secondFragment = frag;
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

				case 0: return FirstFragment.newInstance("FirstFragment, Instance 1");
				case 1://SecondFragment newFragment = SecondFragment.newInstance("SecondFragment, Instance 1");
				//	Log.d(TAG, "secondFragBundled");
					
					//secondFragBundle = getSupportFragmentManager().findFragmentById(R.id.pageview)
					secondFragment.setArguments(secondFragBundle); 
					return secondFragment;
				default: return FirstFragment.newInstance("ThirdFragment, Default");
            }
        }

        @Override
        public int getCount() {
            return 2;
        }       
    }

	
	public void initSecondFrag(int numPeeps, int tip, float total)
	{
		Log.d(TAG, "onInputUpdate - bill: " + total + " - tip: " + tip + " - Peeps: " + numPeeps);
		secondFragBundle.putInt("tip", tip);
		secondFragBundle.putInt("people", numPeeps);
		secondFragBundle.putFloat("total", total);

		//secondFragment.setArguments(secondFragBundle);

		if(secondFragment == null){}
		else{
			Bundle bun = new Bundle();
			bun.putInt("tip", tip);
			bun.putInt("people", numPeeps);
			bun.putFloat("total", total);
			secondFragment.updateArgs(bun);
			}
	}
	

	//boolean backPressHandled = false;
	//Are you sure? dialog box
	public  void areyousureDialog(Activity a, String title, String message){
		AlertDialog.Builder dialog = new AlertDialog.Builder(a);
		dialog.setTitle(title);
		dialog.setMessage(message);
		LayoutInflater inflater = getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		dialog.setView(inflater.inflate(R.layout.warning_dialog, null));
		dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					mViewPager.setCurrentItem(mViewPager.getCurrentItem()-1 );
				}
			})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User cancelled the dialog
				
				}
			});
		dialog.create().show();     

	}

	@Override
	public void onBackPressed()
	{
		if(mViewPager.getCurrentItem() == 1){
			areyousureDialog(this, "Are you sure you want to return?", "Your input on this page will be lost");
		}else
			super.onBackPressed();
	}
	

	
	
	
	@Override
	public void onInputUpdate(int numPeeps, int tip, float total)
	{
		initSecondFrag(numPeeps, tip, total);
		
		mViewPager.setCurrentItem(1, true);
	}


	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       
		switch (item.getItemId()) {
			case R.id.action_settings:
				// User chose the "Settings" item, show the app settings UI...
				return true;

			case R.id.action_group:
				//((SecondFragment)getSupportFragmentManager().findFragmentById(R.id.second_frag)).groupItems();
				secondFragment.groupItems();
				return true;
			case R.id.action_ungroup:
				secondFragment.unGroupItems();
				//((SecondFragment)getSupportFragmentManager().findFragmentById(R.id.second_frag)).unGroupItems();
				return true;
			default:
			//No action
				return super.onOptionsItemSelected(item);

		}
	
    }

}
