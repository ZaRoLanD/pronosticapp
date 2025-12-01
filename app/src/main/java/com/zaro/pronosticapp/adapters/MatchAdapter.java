package com.zaro.pronosticapp.adapters;

import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zaro.pronosticapp.R;
import com.zaro.pronosticapp.models.Match;
import com.zaro.pronosticapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {

    private List<Match> matches = new ArrayList<>();
    private Handler handler = new Handler();
    private List<Runnable> countdownRunnables = new ArrayList<>();

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_match, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        Match match = matches.get(position);
        holder.bind(match);
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public void setMatches(List<Match> matches) {
        // Nettoyer les anciens countdowns
        for (Runnable runnable : countdownRunnables) {
            handler.removeCallbacks(runnable);
        }
        countdownRunnables.clear();

        this.matches = matches;
        notifyDataSetChanged();
    }

    @Override
    public void onViewRecycled(@NonNull MatchViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder.countdownRunnable != null) {
            handler.removeCallbacks(holder.countdownRunnable);
        }
    }

    class MatchViewHolder extends RecyclerView.ViewHolder {
        TextView tvCountry, tvLeague, tvTeams, tvMatchTime, tvPronostic;
        TextView tvCountdown, tvResultText;
        ImageView ivResultIcon;
        LinearLayout layoutCountdown, layoutResult;
        Runnable countdownRunnable;

        public MatchViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCountry = itemView.findViewById(R.id.tvCountry);
            tvLeague = itemView.findViewById(R.id.tvLeague);
            tvTeams = itemView.findViewById(R.id.tvTeams);
            tvMatchTime = itemView.findViewById(R.id.tvMatchTime);
            tvPronostic = itemView.findViewById(R.id.tvPronostic);
            tvCountdown = itemView.findViewById(R.id.tvCountdown);
            tvResultText = itemView.findViewById(R.id.tvResultText);
            ivResultIcon = itemView.findViewById(R.id.ivResultIcon);
            layoutCountdown = itemView.findViewById(R.id.layoutCountdown);
            layoutResult = itemView.findViewById(R.id.layoutResult);
        }

        public void bind(Match match) {
            // Afficher les informations de base
            tvCountry.setText(match.getCountry());
            tvLeague.setText(match.getLeague());
            tvTeams.setText(match.getTeamsDisplay());
            tvPronostic.setText(match.getPronostic());

            // Formater l'heure
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            tvMatchTime.setText(sdf.format(new Date(match.getMatchTime())));

            // Gérer l'affichage selon le statut
            if (match.getStatus().equals("pending")) {
                // Match en attente - afficher le compte à rebours
                layoutCountdown.setVisibility(View.VISIBLE);
                layoutResult.setVisibility(View.GONE);
                startCountdown(match);
            } else {
                // Match terminé - afficher le résultat
                layoutCountdown.setVisibility(View.GONE);
                layoutResult.setVisibility(View.VISIBLE);
                displayResult(match);
            }
        }

        private void startCountdown(Match match) {
            // Nettoyer l'ancien countdown s'il existe
            if (countdownRunnable != null) {
                handler.removeCallbacks(countdownRunnable);
            }

            countdownRunnable = new Runnable() {
                @Override
                public void run() {
                    long timeRemaining = match.getTimeUntilMatch();

                    if (timeRemaining > 0) {
                        // Calculer les jours, heures, minutes, secondes
                        long days = timeRemaining / (1000 * 60 * 60 * 24);
                        long hours = (timeRemaining / (1000 * 60 * 60)) % 24;
                        long minutes = (timeRemaining / (1000 * 60)) % 60;
                        long seconds = (timeRemaining / 1000) % 60;

                        String countdown;
                        if (days > 0) {
                            countdown = String.format(Locale.getDefault(),
                                    "%dd %02d:%02d:%02d", days, hours, minutes, seconds);
                        } else {
                            countdown = String.format(Locale.getDefault(),
                                    "%02d:%02d:%02d", hours, minutes, seconds);
                        }

                        tvCountdown.setText(countdown);

                        // Mettre à jour chaque seconde
                        handler.postDelayed(this, 1000);
                    } else {
                        tvCountdown.setText("Match en cours");
                    }
                }
            };

            countdownRunnables.add(countdownRunnable);
            handler.post(countdownRunnable);
        }

        private void displayResult(Match match) {
            if (match.getStatus().equals("won")) {
                // Pronostic gagné
                ivResultIcon.setImageResource(R.drawable.ic_check);
                ivResultIcon.setColorFilter(Color.parseColor("#4CAF50"));
                tvResultText.setText("GAGNÉ");
                tvResultText.setTextColor(Color.parseColor("#4CAF50"));
                layoutResult.setBackgroundColor(Color.parseColor("#E8F5E9"));
            } else if (match.getStatus().equals("lost")) {
                // Pronostic perdu
                ivResultIcon.setImageResource(R.drawable.ic_close);
                ivResultIcon.setColorFilter(Color.parseColor("#F44336"));
                tvResultText.setText("PERDU");
                tvResultText.setTextColor(Color.parseColor("#F44336"));
                layoutResult.setBackgroundColor(Color.parseColor("#FFEBEE"));
            }
        }
    }
}