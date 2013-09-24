package deetc.pdm.yamba;

import android.util.Log;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.text.Editable;
import android.text.InputFilter;
import android.widget.Button;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.widget.TextView;
import android.widget.EditText;
import android.text.TextWatcher;
import android.preference.PreferenceManager;

public class StatusActivity extends BaseActivity implements OnSharedPreferenceChangeListener
{
	//private final String APP_TAG = "Yamba";
	private EditText tbStatus;
	private TextView txtCharCount;
	private Button btnSubmit;
	private int maxChars;
	
	
	SharedPreferences prefs;
	
	//Inicialização ecrã e componentes necessárias
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
        
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
        //Máximo de caracteres
        maxChars = Integer.parseInt(prefs.getString("NumberCharacteres", "140"));        
        txtCharCount = (TextView) findViewById(R.id.txtCharCount);
        txtCharCount.setText(String.valueOf(maxChars));
        
        
        //Textbox de status
        tbStatus = (EditText) findViewById(R.id.tbStatus);
        InputFilter [] filter = new InputFilter[1];
        filter[0] = new InputFilter.LengthFilter(maxChars);
        tbStatus.setFilters(filter);
        tbStatus.addTextChangedListener(new TextWatcher()
      	{
       	   public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

       	   public void onTextChanged(CharSequence s, int start, int before, int count) {
       		    		   
       		   txtCharCount.setText(String.valueOf(maxChars - s.length()));
       	   }
       	   
       	   public void afterTextChanged(Editable s) { }
       	});
        
        //Botão submeter
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {       		
        		Log.v("StatusActivity", "startService");
        		Intent intent = new Intent(StatusActivity.this, deetc.pdm.yamba.StatusUpload.class);
        		intent.putExtra("input", tbStatus.getText().toString());
        		startService(intent);
        		tbStatus.setText("");
        		
        	}
		});

      }
    
	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		this.prefs = arg0;
		maxChars = Integer.parseInt(prefs.getString("NumberCharacteres", "140"));
	}
	
	@Override
	protected void onDestroy() {
		Log.d("DEBUG", "STATUS ACTIVITY ONDESTROY THERAD:" + Thread.currentThread().getName());
		super.onDestroy();
	}
}
