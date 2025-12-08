package com.zaro.pronosticapp.adapters;

import android.graphics.Color;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<Match> matches = new ArrayList<>();

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_match, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
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

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCountry, tvLeague, tvTeams, tvMatchTime, tvPronostic;
        TextView tvResultText;
        ImageView ivResultIcon;
        LinearLayout layoutCountdown, layoutResult;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCountry = itemView.findViewById(R.id.tvCountry);
            tvLeague = itemView.findViewById(R.id.tvLeague);
            tvTeams = itemView.findViewById(R.id.tvTeams);
            tvMatchTime = itemView.findViewById(R.id.tvMatchTime);
            tvPronostic = itemView.findViewById(R.id.tvPronostic);
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
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            tvMatchTime.setText(sdf.format(new Date(match.getMatchTime())));

            // Toujours masquer le countdown dans l'historique
            layoutCountdown.setVisibility(View.GONE);
            layoutResult.setVisibility(View.VISIBLE);

            // Afficher le résultat
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