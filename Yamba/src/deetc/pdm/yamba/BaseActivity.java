package deetc.pdm.yamba;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends Activity{
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.time_line, menu);
		return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
			case R.id.itemTimeLine:
				startActivity(new Intent(this,deetc.pdm.yamba.TimeLineActivity.class));
				break;
			case R.id.itemTimeLineExplicit:
				startService(new Intent(BaseActivity.this, deetc.pdm.yamba.TimelinePull.class));
				break;
			case R.id.itemPreference:
				startActivity(new Intent(this , deetc.pdm.yamba.PrefsActivity.class));
				break;
			case R.id.itemStatus:
				startActivity(new Intent(this , deetc.pdm.yamba.StatusActivity.class));
				break;
			case R.id.itemUserInfo:
				startActivity(new Intent(this , deetc.pdm.yamba.UserInfoActivity.class));
				break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	
}
