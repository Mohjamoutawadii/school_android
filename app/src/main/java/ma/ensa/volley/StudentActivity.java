package ma.ensa.volley;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;



import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ma.ensa.volley.adapters.StudentAdapter;
import ma.ensa.volley.beans.Filiere;
import ma.ensa.volley.beans.Role;
import ma.ensa.volley.lists.ListStudent;

public class StudentActivity extends AppCompatActivity implements View.OnClickListener{


    String selectedRole;

    String selectedfiliere;
    Spinner spinner,spinnerfiliere;

    Role role;
    Filiere filiere;


    List<String> rolesList = new ArrayList<>();
    List<String> filiereList = new ArrayList<>();

    List<Role> roles = new ArrayList<>();
    List<Filiere> filieres = new ArrayList<>();

    private EditText login,password,firstName,lastName,telephone;
    private Button bnAdd,btgst;
    RequestQueue requestQueue;
    String insertUrl = "http://192.168.0.92:8088/api/v1/filieres";
    String studentUrl="http://192.168.0.92:8088/api/v1/students";
    String getroles = "http://192.168.0.92:8088/api/v1/roles";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        spinner = findViewById(R.id.spinner);
        spinnerfiliere = findViewById(R.id.spinner_filiere);
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        firstName=findViewById(R.id.firstName);
        lastName=findViewById(R.id.lastName);
        telephone=findViewById(R.id.telephone);
        bnAdd = findViewById(R.id.Add);
        btgst=findViewById(R.id.gst);

        bnAdd.setOnClickListener(this);
        loadroles();
        loadfilieres();

        btgst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentActivity.this, ListStudent.class);
                startActivity(intent);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRole = (String) spinner.getSelectedItem();
                for (Role r : roles) {
                    if (r.getName().equals(selectedRole)) {
                        role =new Role(r.getId(),r.getName());
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinnerfiliere.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                 selectedfiliere = (String) spinnerfiliere.getSelectedItem();
                for (Filiere f : filieres) {
                    if (f.getname().equals(selectedfiliere)) {
                        filiere=new Filiere(f.getId(),f.getcode(),f.getname());
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("login", login.getText().toString());
                jsonBody.put("password", password.getText().toString());
                JSONArray rolesArray = new JSONArray();
                JSONObject roleObj = new JSONObject();
                roleObj.put("id", role.getId());
                roleObj.put("name", role.getName());
                rolesArray.put(roleObj);
                jsonBody.put("roles", rolesArray);
                jsonBody.put("firstName", firstName.getText().toString());
                jsonBody.put("lastName", lastName.getText().toString());
                jsonBody.put("telephone", telephone.getText().toString());
                JSONObject filiereObj = new JSONObject();
                filiereObj.put("id", filiere.getId());
                filiereObj.put("code", filiere.getcode());
                filiereObj.put("name", filiere.getname());
                jsonBody.put("filiere", filiereObj);




            } catch (JSONException e) {
                e.printStackTrace();
            }

    requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                studentUrl, jsonBody, new Response.Listener<JSONObject>() {
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
    private void loadroles() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getroles,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String nom = jsonObject.getString("name");
                                roles.add(new Role(id,nom));
                                rolesList.add(nom);
                                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(StudentActivity.this, android.R.layout.simple_spinner_item, rolesList);
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(spinnerAdapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        Volley.newRequestQueue(this).add(stringRequest);
    }
    private void loadfilieres() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, insertUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String code = jsonObject.getString("code");
                                String nom = jsonObject.getString("name");
                                filiereList.add(nom);
                                filieres.add(new Filiere(id,code,nom));
                                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(StudentActivity.this, android.R.layout.simple_spinner_item,filiereList);
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerfiliere.setAdapter(spinnerAdapter);


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        Volley.newRequestQueue(this).add(stringRequest);
    }


}