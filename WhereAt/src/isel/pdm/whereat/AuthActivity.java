package isel.pdm.whereat;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AuthActivity extends Activity{

	 @TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.giveauth);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy); 

		
		Parse.initialize(this, "FfEsBy1dJIdyQqRigOWfwHVtJw185nv2eJjry1yT", "IzGMVtc1KuKglyYW7KWLc8bJ1ZzFTiykPv39nb1b");
		
		final EditText et = (EditText)this.findViewById(R.id.editTextauth);
		Button save = (Button)this.findViewById(R.id.buttongiveauthh);
		save.setClickable(true);
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ParseObject g1 = new ParseObject(ParseUser.getCurrentUser().getUsername());				
				g1.put("user","a"+et.getText().toString());
				g1.put("caniuse", false);
				g1.saveEventually();
				
				
				ParseObject g = new ParseObject("a"+et.getText().toString());				
				g.put("user",ParseUser.getCurrentUser().getUsername());
				g.put("caniuse", false);
				g.saveEventually();
				
			}});
	
		
		
		Button remove = (Button)this.findViewById(R.id.buttonremove);
		remove.setClickable(true);
		remove.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ParseQuery<ParseObject> query = ParseQuery.getQuery("a"+et.getText().toString());
				query.whereEqualTo("user" , ParseUser.getCurrentUser().getUsername());
				query.getFirstInBackground(	new GetCallback<ParseObject>() {
					public void done(ParseObject gameScore,
							com.parse.ParseException e) {
						if (e == null) {
							//gameScore.put("user", ParseUser.getCurrentUser().getUsername());
							gameScore.remove("user");
							gameScore.saveEventually();
						}
					}
				});
//				ParseObject g = new ParseObject("a"+et.getText().toString());				
//				g.remove("user");
//				g.saveEventually();
				
			}});
		
	}
	
}
