package ma.ensa.volley;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ma.ensa.volley.lists.ListFiliere;
import ma.ensa.volley.lists.ListRole;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText code, name;
    private Button bnAdd,btgstf;
    RequestQueue requestQueue;
    String insertUrl = "http://192.168.0.92:8088/api/v1/filieres";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        code = findViewById(R.id.code);
        name = findViewById(R.id.name);
        bnAdd = findViewById(R.id.bnAdd);
        btgstf=findViewById(R.id.gstf);



        btgstf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListFiliere.class);
                startActivity(intent);
            }
        });

        bnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {


        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("code", code.getText().toString() );
            jsonBody.put("name", name.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                insertUrl, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("resultat", response+"");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Erreur", error.toString());
            }
        });
        requestQueue.add(request);

    }
}