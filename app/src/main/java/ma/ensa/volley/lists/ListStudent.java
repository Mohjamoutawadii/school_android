package ma.ensa.volley.lists;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.ensa.volley.R;
import ma.ensa.volley.adapters.StudentAdapter;
import ma.ensa.volley.beans.Student;

public class ListStudent extends AppCompatActivity {

    private static final String URL_LOAD = "http://192.168.0.92:8088/api/v1/students";

    private ListView listView;
    private List<Student> studentList;
    private StudentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_student);

        listView = findViewById(R.id.list);
        adapter = new StudentAdapter(this, R.layout.item, new ArrayList<>());
        listView.setAdapter(adapter);
        loadStudents();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student selectedStudent = studentList.get(position);

                new AlertDialog.Builder(ListStudent.this)
                        .setTitle("Choisir une opération")
                        .setItems(new CharSequence[]{"Modifier", "Supprimer"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    showEditDialog(selectedStudent);
                                } else {
                                    showDeleteConfirmation(selectedStudent);
                                }
                            }
                        })
                        .show();
            }
        });
    }

    private void loadStudents() {
        studentList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_LOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String firstName = jsonObject.getString("firstName");
                                String lastName = jsonObject.getString("lastName");
                                String telephone = jsonObject.getString("telephone");

                                Student student = new Student(id,firstName, lastName, telephone);
                                studentList.add(student);
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
                        // Handle error
                    }
                }
        );

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void showEditDialog(final Student student) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modifier l'étudiant");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText firstNameInput = new EditText(this);
        firstNameInput.setHint("Prénom");
        firstNameInput.setText(student.getFirstName());
        layout.addView(firstNameInput);

        final EditText lastNameInput = new EditText(this);
        lastNameInput.setHint("Nom");
        lastNameInput.setText(student.getLastName());
        layout.addView(lastNameInput);

        final EditText telephoneInput = new EditText(this);
        telephoneInput.setHint("Téléphone");
        telephoneInput.setText(student.getTelephone());
        layout.addView(telephoneInput);

        builder.setView(layout);

        builder.setPositiveButton("Enregistrer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newFirstName = firstNameInput.getText().toString();
                String newLastName = lastNameInput.getText().toString();
                String newTelephone = telephoneInput.getText().toString();

                Student updatedStudent = new Student(
                        student.getId(),
                        newFirstName,
                        newLastName,
                        newTelephone
                );

                int position = studentList.indexOf(student);
                studentList.set(position, updatedStudent);
                sendUpdateRequest(updatedStudent,student);
            }
        });

        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void showDeleteConfirmation(final Student student) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Êtes-vous sûr de vouloir supprimer cet étudiant?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sendDeleteRequest(student);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void sendDeleteRequest(Student student) {
        String deleteUrl = "http://192.168.0.92:8088/api/v1/students/" + student.getId();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.DELETE, deleteUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Suppression réussie !", Toast.LENGTH_SHORT).show();
                studentList.remove(student);
                adapter.remove(student);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error + "");
            }
        });

        requestQueue.add(request);
    }

    private void sendUpdateRequest(Student student,Student oldstudent) {
        String updateUrl = "http://192.168.0.92:8088/api/v1/students";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest updateRequest = new StringRequest(Request.Method.POST, updateUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getApplicationContext(), "Mise à jour effectuée !", Toast.LENGTH_SHORT).show();
                        studentList.remove(oldstudent);
                        adapter.remove(oldstudent);
                        studentList.add(student);
                        adapter.add(student);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error", error + "");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(student.getId()));
                params.put("firstName", student.getFirstName());
                params.put("lastName", student.getLastName());
                params.put("telephone", student.getTelephone());

                return params;
            }
        };

        requestQueue.add(updateRequest);
    }
}
