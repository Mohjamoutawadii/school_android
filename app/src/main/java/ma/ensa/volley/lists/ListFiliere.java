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
import ma.ensa.volley.adapters.FiliereAdapter;
import ma.ensa.volley.beans.Filiere;


public class ListFiliere extends AppCompatActivity {

    private static final String URL_LOAD = "http://192.168.0.92:8088/api/v1/filieres";


    private ListView listView;
    private List<Filiere>FiliereList;

    private FiliereAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_filiere);

        listView = findViewById(R.id.list);
        adapter = new FiliereAdapter(this, R.layout.item, new ArrayList<>());
        listView.setAdapter(adapter);
        loadStudents();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Filiere selectedFiliere = FiliereList.get(position);

                new AlertDialog.Builder(ListFiliere.this)
                        .setTitle("Choisir une operation")
                        .setItems(new CharSequence[]{"Modifier", "Supprimer"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    showEditDialog(selectedFiliere);
                                } else {
                                    showDeleteConfirmation(selectedFiliere);
                                }
                            }
                        })
                        .show();
            }
        });
    }






    private void loadStudents() {
        FiliereList = new ArrayList<>();
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
                                FiliereList.add(Filiere);
                                adapter.add(Filiere);
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
    private void showEditDialog(final Filiere Filiere) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modifier l'étudiant");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText nomInput = new EditText(this);
        nomInput.setHint("Nom");
        nomInput.setText(Filiere.getname());
        layout.addView(nomInput);

        final EditText codeInput = new EditText(this);
        codeInput.setHint("Code");
        codeInput.setText(Filiere.getcode());
        layout.addView(codeInput);

        builder.setView(layout);

        builder.setPositiveButton("Enregistrer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newNom = nomInput.getText().toString();
                String newCode = codeInput.getText().toString();


                Filiere updatedFiliere = new Filiere(
                        Filiere.getId(),
                        newCode,
                        newNom

                );

                int position = FiliereList.indexOf(Filiere);

                FiliereList.set(position, updatedFiliere);

                adapter.notifyDataSetChanged();
                sendUpdateRequest(updatedFiliere,Filiere);
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
    private void showDeleteConfirmation(final Filiere Filiere) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Êtes-vous sûr de vouloir supprimer cet filiere?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        sendDeleteRequest(Filiere);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    private void sendDeleteRequest(Filiere Filiere) {
        String deleteUrl = "http://192.168.0.92:8088/api/v1/filieres/" + Filiere.getId();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.DELETE, deleteUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Supression réussie !", Toast.LENGTH_SHORT).show();
                FiliereList.remove(Filiere);
                adapter.remove(Filiere);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error+"");
            }
        });

        requestQueue.add(request);
    }
    private void sendUpdateRequest(Filiere filiereToUpdate,Filiere oldfiliere) {
        String updateUrl = "http://192.168.0.92:8088/api/v1/filieres/" + filiereToUpdate.getId();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest updateRequest = new StringRequest(Request.Method.PUT, updateUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        FiliereList.remove(oldfiliere);
                        adapter.remove(oldfiliere);
                        FiliereList.add(filiereToUpdate);
                        adapter.add(filiereToUpdate);
                        Toast.makeText(getApplicationContext(), "Mise à jour effectuée !", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Gérer les erreurs lors de la requête
                        Log.d("error", error.toString());
                        Toast.makeText(getApplicationContext(), "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("name", filiereToUpdate.getname());
                    jsonObject.put("code", filiereToUpdate.getcode());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonObject.toString().getBytes();
            }
        };

        requestQueue.add(updateRequest);
    }


}


