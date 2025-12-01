package com.zaro.pronosticapp.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zaro.pronosticapp.R;
import com.zaro.pronosticapp.adapters.MatchAdapter;
import com.zaro.pronosticapp.models.Match;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zaro.pronosticapp.R;
import com.zaro.pronosticapp.models.Match;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MatchAdapter adapter;
    private FloatingActionButton fabAddMatch;
    private DatabaseReference matchesRef;
    private List<Match> matchList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard2);

        // Initialiser Firebase
        matchesRef = FirebaseDatabase.getInstance().getReference("matches");

        // Initialiser les vues
        recyclerView = findViewById(R.id.recyclerViewMatches);
        fabAddMatch = findViewById(R.id.fabAddMatch);

        // Configurer RecyclerView
        adapter = new MatchAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Bouton d'ajout de match
        fabAddMatch.setOnClickListener(v -> showAddMatchDialog());

        // Charger les matchs
        loadMatches();
    }

    private void loadMatches() {
        matchesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                matchList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Match match = data.getValue(Match.class);
                    if (match != null) {
                        matchList.add(match);
                    }
                }
                adapter.setMatches(matchList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminDashboardActivity.this,
                        "Erreur : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddMatchDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_match, null);

        // Récupérer les vues du dialogue
        EditText etCountry = dialogView.findViewById(R.id.etCountry);
        EditText etLeague = dialogView.findViewById(R.id.etLeague);
        EditText etTeam1 = dialogView.findViewById(R.id.etTeam1);
        EditText etTeam2 = dialogView.findViewById(R.id.etTeam2);
        Button btnSelectDateTime = dialogView.findViewById(R.id.btnSelectDateTime);
        EditText etPronostic = dialogView.findViewById(R.id.etPronostic);
        CheckBox cbVisible = dialogView.findViewById(R.id.cbVisible);
        EditText etVisibleMinutes = dialogView.findViewById(R.id.etVisibleMinutes);

        final long[] selectedMatchTime = {0};
        final long[] selectedVisibleTime = {0};

        // Sélecteur de date et heure
        btnSelectDateTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();

            new DatePickerDialog(this, (view, year, month, day) -> {
                new TimePickerDialog(this, (view1, hour, minute) -> {
                    calendar.set(year, month, day, hour, minute, 0);
                    selectedMatchTime[0] = calendar.getTimeInMillis();

                    // Calculer le temps de visibilité (X minutes avant le match)
                    String minutesStr = etVisibleMinutes.getText().toString();
                    int minutesBefore = minutesStr.isEmpty() ? 15 : Integer.parseInt(minutesStr);
                    selectedVisibleTime[0] = selectedMatchTime[0] - (minutesBefore * 60 * 1000);

                    btnSelectDateTime.setText(String.format("%02d/%02d/%d %02d:%02d",
                            day, month + 1, year, hour, minute));
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Créer le dialogue
        new AlertDialog.Builder(this)
                .setTitle("Ajouter un pronostic")
                .setView(dialogView)
                .setPositiveButton("Ajouter", (dialog, which) -> {
                    // Validation
                    if (etCountry.getText().toString().trim().isEmpty() ||
                            etLeague.getText().toString().trim().isEmpty() ||
                            etTeam1.getText().toString().trim().isEmpty() ||
                            etTeam2.getText().toString().trim().isEmpty() ||
                            etPronostic.getText().toString().trim().isEmpty() ||
                            selectedMatchTime[0] == 0) {
                        Toast.makeText(this, "Veuillez remplir tous les champs",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Créer le match
                    String matchId = matchesRef.push().getKey();
                    Match newMatch = new Match(
                            matchId,
                            etCountry.getText().toString().trim(),
                            etLeague.getText().toString().trim(),
                            etTeam1.getText().toString().trim(),
                            etTeam2.getText().toString().trim(),
                            selectedMatchTime[0],
                            etPronostic.getText().toString().trim(),
                            "pending",
                            cbVisible.isChecked(),
                            selectedVisibleTime[0]
                    );

                    // Sauvegarder dans Firebase
                    matchesRef.child(matchId).setValue(newMatch)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Pronostic ajouté avec succès",
                                        Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Erreur : " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    // Méthode pour mettre à jour le résultat d'un match
    public void updateMatchResult(String matchId, String status) {
        matchesRef.child(matchId).child("status").setValue(status)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Résultat mis à jour", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erreur : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}