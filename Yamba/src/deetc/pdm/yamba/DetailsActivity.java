package deetc.pdm.yamba;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailsActivity extends BaseActivity{

	TextView text;
	Button btnEmail;
	UserInfo info;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_activity);
		
		Intent intent = this.getIntent();	
		Bundle bundle = intent.getExtras();
		
		if(bundle != null)
		{
			info = bundle.getParcelable("Parametro");
			text = (TextView)findViewById(R.id.editText4);
			text.setText(info.toString());
		}
		
		//Botão email
        btnEmail = (Button)findViewById(R.id.btnEmail);
        btnEmail.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Log.v("DetailActivity", "sendEmail");
        		
        		//String to = "teste@teste.pt";
        		String subject = String.format(getText(R.string.subjectEmailClient).toString(), info.getName(), info.getTime());
        		
        		Intent email = new Intent(Intent.ACTION_SEND);
        		//email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to });
        		email.putExtra(Intent.EXTRA_SUBJECT, subject);
        		email.putExtra(Intent.EXTRA_TEXT, info.getText());        		
        		email.setType("message/rfc822");
        		
        		startActivity(Intent.createChooser(email, getText(R.string.chooseEmailClient).toString()));
        	}
		});
	}
}
