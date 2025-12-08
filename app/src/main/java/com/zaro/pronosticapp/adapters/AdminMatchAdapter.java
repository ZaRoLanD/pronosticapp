package com.zaro.pronosticapp.adapters;

import android.app.AlertDialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.zaro.pronosticapp.R;
import com.zaro.pronosticapp.models.Match;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminMatchAdapter extends RecyclerView.Adapter<AdminMatchAdapter.AdminMatchViewHolder> {

    private List<Match> matches = new ArrayList<>();
    private DatabaseReference matchesRef;

    public AdminMatchAdapter() {
        matchesRef = FirebaseDatabase.getInstance().getReference("matches");
    }

    @NonNull
    @Override
    public AdminMatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_match, parent, false);
        return new AdminMatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminMatchViewHolder holder, int position) {
        Match match = matches.get(position);
        holder.bind(match);
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
        notifyDataSetChanged();
    }

    class AdminMatchViewHolder extends RecyclerView.ViewHolder {
        TextView tvCountry, tvLeague, tvTeams, tvMatchTime, tvPronostic, tvStatus;
        Button btnWon, btnLost, btnDelete;
        LinearLayout layoutActions;

        public AdminMatchViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCountry = itemView.findViewById(R.id.tvCountry);
            tvLeague = itemView.findViewById(R.id.tvLeague);
            tvTeams = itemView.findViewById(R.id.tvTeams);
            tvMatchTime = itemView.findViewById(R.id.tvMatchTime);
            tvPronostic = itemView.findViewById(R.id.tvPronostic);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnWon = itemView.findViewById(R.id.btnWon);
            btnLost = itemView.findViewById(R.id.btnLost);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            layoutActions = itemView.findViewById(R.id.layoutActions);
        }

        public void bind(Match match) {
            // Afficher les informations de base
            tvCountry.setText(match.getCountry());
            tvLeague.setText(match.getLeague());
            tvTeams.setText(match.getTeamsDisplay());
            tvPronostic.setText(match.getPronostic());

            // Formater l'heure
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            tvMatchTime.setText(sdf.format(new Date(match.getMatchTime())));

            // Afficher le statut
            switch (match.getStatus()) {
                case "pending":
                    tvStatus.setText("En attente");
                    tvStatus.setTextColor(Color.parseColor("#9E9E9E"));
                    layoutActions.setVisibility(View.VISIBLE);
                    break;
                case "won":
                    tvStatus.setText("✅ GAGNÉ");
                    tvStatus.setTextColor(Color.parseColor("#4CAF50"));
                    layoutActions.setVisibility(View.GONE);
                    break;
                case "lost":
                    tvStatus.setText("❌ PERDU");
                    tvStatus.setTextColor(Color.parseColor("#F44336"));
                    layoutActions.setVisibility(View.GONE);
                    break;
            }

            // Bouton "Gagné"
            btnWon.setOnClickListener(v -> {
                new AlertDialog.Builder(itemView.getContext())
                        .setTitle("Confirmer le résultat")
                        .setMessage("Marquer ce pronostic comme GAGNÉ ?")
                        .setPositiveButton("Oui", (dialog, which) -> {
                            updateMatchStatus(match.getId(), "won");
                        })
                        .setNegativeButton("Non", null)
                        .show();
            });

            // Bouton "Perdu"
            btnLost.setOnClickListener(v -> {
                new AlertDialog.Builder(itemView.getContext())
                        .setTitle("Confirmer le résultat")
                        .setMessage("Marquer ce pronostic comme PERDU ?")
                        .setPositiveButton("Oui", (dialog, which) -> {
                            updateMatchStatus(match.getId(), "lost");
                        })
                        .setNegativeButton("Non", null)
                        .show();
            });

            // Bouton "Supprimer"
            btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(itemView.getContext())
                        .setTitle("Supprimer le pronostic")
                        .setMessage("Êtes-vous sûr de vouloir supprimer ce pronostic ?")
                        .setPositiveButton("Supprimer", (dialog, which) -> {
                            deleteMatch(match.getId());
                        })
                        .setNegativeButton("Annuler", null)
                        .show();
            });
        }

        private void updateMatchStatus(String matchId, String status) {
            matchesRef.child(matchId).child("status").setValue(status)
                    .addOnSuccessListener(aVoid -> {
                        // Mettre à jour le timestamp
                        matchesRef.child(matchId).child("updatedAt")
                                .setValue(System.currentTimeMillis());
                    });
        }

        private void deleteMatch(String matchId) {
            matchesRef.child(matchId).removeValue();
        }
    }
}