package es.hol.connectier.madapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity implements View.OnClickListener {

    private TextView haveacc;
    private EditText mail,name,password,cpassword;
    private CheckBox terms;
    private Button reg_btn;
    private ProgressDialog loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Casting
        haveacc = (TextView)findViewById(R.id.haveaccount);
        mail=(EditText)findViewById(R.id.email);
        name=(EditText)findViewById(R.id.fullname);
        password=(EditText)findViewById(R.id.password);
        cpassword=(EditText)findViewById(R.id.cpassword);
        reg_btn=(Button)findViewById(R.id.register_btn);
        terms=(CheckBox)findViewById(R.id.terms);

        //Progress Dialog
        loader=new ProgressDialog(this);
        loader.setMessage("Registering User.....");

        //Listener
        haveacc.setOnClickListener(this);
        reg_btn.setOnClickListener(this);
    }

    public void register()
      {
          final String email= mail.getText().toString().trim();
          final String fullname=name.getText().toString().trim();
          final String pass=password.getText().toString().trim();
          loader.show();
          StringRequest webrequest=new StringRequest(Request.Method.POST, "http://clayofficial.com/mad/v1/registerUser.php", new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                  loader.dismiss();
                  try {
                      JSONObject jsonObject = new JSONObject(response);
                      Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                  } catch (JSONException e) {
                      e.printStackTrace();
                  }
              }
          }, new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
                   loader.hide();
                   Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
              }
          }){
              @Override
              protected Map<String, String> getParams() throws AuthFailureError {
                  Map<String,String> params=new HashMap<>();
                  params.put("email",email);
                  params.put("password",pass);
                  params.put("name",fullname);
                  return params;
          }
      };
          RequestHandler.getInstance(this).addToRequestQueue(webrequest);
      }

    @Override
    public void onClick(View v) {
        if (v == haveacc){
            finish();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        if(v==reg_btn)
        {
             register();
        }
    }
}
