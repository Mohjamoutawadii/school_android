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
import ma.ensa.volley.adapters.RoleAdapter;
import ma.ensa.volley.beans.Role;

public class ListRole extends AppCompatActivity {

    private static final String URL_LOAD = "http://192.168.0.92:8088/api/v1/roles";

    private ListView listView;
    private List<Role> roleList;
    private RoleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_role);

        listView = findViewById(R.id.list);
        adapter = new RoleAdapter(this, R.layout.item, new ArrayList<>());
        listView.setAdapter(adapter);
        loadRoles();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Role selectedRole = roleList.get(position);

                new AlertDialog.Builder(ListRole.this)
                        .setTitle("Choisir une opération")
                        .setItems(new CharSequence[]{"Modifier", "Supprimer"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    showEditDialog(selectedRole);
                                } else {
                                    showDeleteConfirmation(selectedRole);
                                }
                            }
                        })
                        .show();
            }
        });
    }

    private void loadRoles() {
        roleList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_LOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String name = jsonObject.getString("name");
                                Role role = new Role(id, name);
                                roleList.add(role);
                                adapter.add(role);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Gérer les erreurs de chargement ici
                    }
                }
        );

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void showEditDialog(final Role role) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modifier le rôle");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText nameInput = new EditText(this);
        nameInput.setHint("Nom du rôle");
        nameInput.setText(role.getName());
        layout.addView(nameInput);

        builder.setView(layout);

        builder.setPositiveButton("Enregistrer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = nameInput.getText().toString();

                Role updatedRole = new Role(role.getId(), newName);

                int position = roleList.indexOf(role);
                roleList.set(position, updatedRole);

                sendUpdateRequest(updatedRole,role);
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

    private void showDeleteConfirmation(final Role role) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Êtes-vous sûr de vouloir supprimer ce rôle?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        sendDeleteRequest(role);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void sendDeleteRequest(Role role) {
        String deleteUrl = "http://192.168.0.92:8088/api/v1/roles/" + role.getId();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.DELETE, deleteUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                roleList.remove(role);
                adapter.remove(role);
                Toast.makeText(getApplicationContext(), "Suppression réussie !", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error + "");
                Toast.makeText(getApplicationContext(), "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(request);
    }

    private void sendUpdateRequest(Role roleToUpdate,Role oldrole) {
        String updateUrl = "http://192.168.0.92:8088/api/v1/roles/" + roleToUpdate.getId();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest updateRequest = new StringRequest(Request.Method.PUT, updateUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        roleList.remove(oldrole);
                        adapter.remove(oldrole);
                       roleList.add(roleToUpdate);
                        adapter.add(roleToUpdate);
                        // Gérer la réponse en cas de succès de la requête
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
                    jsonObject.put("name", roleToUpdate.getName());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonObject.toString().getBytes();
            }
        };

        requestQueue.add(updateRequest);
    }
}
