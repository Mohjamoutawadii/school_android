package ma.ensa.volley;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion);

        Button btnGestionEtudiants = findViewById(R.id.btnGestionEtudiants);
        Button btnGestionFilieres = findViewById(R.id.btnGestionFilieres);
        Button btnGestionRoles = findViewById(R.id.btnGestionRoles);
        Button btnEtudiantsParFiliere = findViewById(R.id.btnEtudiantsParFiliere);

        btnGestionEtudiants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestionActivity.this, StudentActivity.class);
                startActivity(intent);
            }
        });

        btnGestionFilieres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestionActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnGestionRoles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestionActivity.this, RoleActivity.class);
                startActivity(intent);
            }
        });

        btnEtudiantsParFiliere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestionActivity.this, StudentByFiliereActivity.class);
                startActivity(intent);
            }
        });
    }
}
