package ma.ensa.volley;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ma.ensa.volley.adapters.StudentAdapter;
import ma.ensa.volley.beans.Filiere;
import ma.ensa.volley.beans.Student;


public class StudentByFiliereActivity extends AppCompatActivity {
    private static final String URL_LOAD = "http://192.168.0.92:8088/api/v1/filieres";

    Spinner spinner;
    ListView listView;
    List<Student> studentsList;
    List<Filiere> filiers;
    List<String> filiereList;
    StudentAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_by_filiere);

        spinner = findViewById(R.id.spinnerFiliere);
        listView = findViewById(R.id.listView);


        loadFiliereList();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               String selectedFiliere = (String) spinner.getSelectedItem();

                for(Filiere f:filiers){
                    if(f.getname().equals(selectedFiliere)){
                        loadStudentsByFiliere(f.getId());
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        adapter = new StudentAdapter(this, R.layout.itemstudent, new ArrayList<>());
        listView.setAdapter(adapter);
    }


    private void loadFiliereList() {
        filiereList = new ArrayList<>();
        filiers=new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_LOAD,
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
                                Filiere Filiere = new Filiere(id, code,nom);
                                filiers.add(Filiere);
                                filiereList.add(nom);
                                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(StudentByFiliereActivity.this, android.R.layout.simple_spinner_item, filiereList);
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


    private void loadStudentsByFiliere(int id) {
        String URL="http://192.168.0.92:8088/api/v1/students/filieres/" +id;
        studentsList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,URL ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                                studentsList.clear();
                                adapter.clear();
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int id = jsonObject.getInt("id");
                                    String firstName= jsonObject.getString("firstName");
                                    String lastName = jsonObject.getString("lastName");
                                    String telephone = jsonObject.getString("telephone");
                                    Student student=new Student(id,firstName,lastName,telephone);
                                    studentsList.add(student);
                                    if (studentsList.isEmpty()) {
                                        Toast.makeText(getApplicationContext(), "Aucun étudiant dans cette filière", Toast.LENGTH_LONG).show();
                                    }
                                    adapter.add(student);




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
