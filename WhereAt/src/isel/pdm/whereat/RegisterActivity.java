package isel.pdm.whereat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends Activity{

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy); 
		
		Parse.initialize(this, "FfEsBy1dJIdyQqRigOWfwHVtJw185nv2eJjry1yT",
				"IzGMVtc1KuKglyYW7KWLc8bJ1ZzFTiykPv39nb1b");
		
		final EditText number = (EditText)this.findViewById(R.id.editTextauth);
		final EditText pass = (EditText)this.findViewById(R.id.editText2);
		
		Button register = (Button)this.findViewById(R.id.buttonregister);
		register.setClickable(true);
		register.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {				
				ParseUser user = new ParseUser();
				user.setUsername("a"+number.getText().toString());
				user.setPassword(pass.getText().toString());
				
				user.signUpInBackground(new SignUpCallback() {
					  public void done(com.parse.ParseException e) {
					    if (e == null) {
					    	ParseQuery<ParseObject> query = ParseQuery.getQuery("a"+ParseUser.getCurrentUser().getUsername());
					    	try {
								if(query.count()==0){					    	
									ParseObject g = new ParseObject(ParseUser.getCurrentUser().getUsername());				
									g.saveEventually();    	
								}
							} catch (ParseException e1) {
								return;
							}
					    	Intent intent = new Intent(RegisterActivity.this ,MainActivity.class);
							//intent.putExtra(_tagcourse, _onList.get(arg2));
							startActivity(intent);
					    	
					    } else {
					      Toast.makeText(RegisterActivity.this, "try again",Toast.LENGTH_SHORT).show();
					      
					    }
					  }
					});				
				
			}
		});
		Button login = (Button)this.findViewById(R.id.buttonlogin);
		login.setClickable(true);
		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("cenas", Thread.currentThread()+"");
				 new Thread(new Runnable() {
				        public void run() {
				        	Log.d("cenas1", Thread.currentThread()+"");
				         	ParseUser.logInInBackground("a"+number.getText().toString(), pass.getText().toString(), new LogInCallback() {
							  public void done(ParseUser user, com.parse.ParseException e) {
							    if (e == null) {
							    	
							   	         RegisterActivity.this.runOnUiThread(new Runnable(){

											@Override
											public void run() {
												Intent intent = new Intent(RegisterActivity.this ,MainActivity.class);
												//intent.putExtra(_tagcourse, _onList.get(arg2));
										    	Log.d("cenas2", Thread.currentThread()+"");
												startActivity(intent);
											}
							   	        	 
							   	         }); 	
								    	
							    } else {
							    	 Toast.makeText(RegisterActivity.this, "try again",Toast.LENGTH_SHORT).show();
							    }
							  }
							});
				        }
				        }).start();
		            }
		        });
	}
}
