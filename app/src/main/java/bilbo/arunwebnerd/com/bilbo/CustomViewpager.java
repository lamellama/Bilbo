package bilbo.arunwebnerd.com.bilbo;


	

	import android.content.Context;
	import android.support.v4.view.ViewPager;
	import android.util.AttributeSet;
	import android.view.MotionEvent;

	public class CustomViewpager extends ViewPager {

		private boolean swipeEnabled;

		public CustomViewpager(Context context, AttributeSet attrs) {
			super(context, attrs);
			this.swipeEnabled = true;
		}

		public void setSwipeEnabled(boolean swipeEnabled)
		{
			this.swipeEnabled = swipeEnabled;
		}

		public boolean isSwipeEnabled()
		{
			return swipeEnabled;
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (this.swipeEnabled) {
				return super.onTouchEvent(event);
			}

			return false;
		}

		@Override
		public boolean onInterceptTouchEvent(MotionEvent event) {
			if (this.swipeEnabled) {
				return super.onInterceptTouchEvent(event);
			}

			return false;
		}

		public void setPagingEnabled(boolean enabled) {
			this.swipeEnabled = enabled;
		}
	
}
