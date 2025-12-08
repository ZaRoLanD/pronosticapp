package com.zaro.pronosticapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zaro.pronosticapp.R;
import com.zaro.pronosticapp.adapters.HistoryAdapter;
import com.zaro.pronosticapp.models.Match;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private DatabaseReference matchesRef;
    private List<Match> matchList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        // Initialiser Firebase
        matchesRef = FirebaseDatabase.getInstance().getReference("matches");

        // Initialiser les vues
        recyclerView = view.findViewById(R.id.recyclerViewHistory);
        progressBar = view.findViewById(R.id.progressBarHistory);
        tvEmpty = view.findViewById(R.id.tvEmptyHistory);

        // Configurer RecyclerView
        adapter = new HistoryAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Charger l'historique
        loadHistory();

        return view;
    }

    private void loadHistory() {
        progressBar.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);

        // Récupérer tous les matchs terminés (won ou lost)
        matchesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                matchList.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    Match match = data.getValue(Match.class);

                    // Filtrer les matchs terminés
                    if (match != null &&
                            (match.getStatus().equals("won") || match.getStatus().equals("lost"))) {

                        // Vérifier la visibilité si nécessaire
                        if (match.shouldBeVisible()) {
                            matchList.add(match);
                        }
                    }
                }

                // Trier par date (du plus récent au plus ancien)
                Collections.sort(matchList, new Comparator<Match>() {
                    @Override
                    public int compare(Match m1, Match m2) {
                        return Long.compare(m2.getMatchTime(), m1.getMatchTime());
                    }
                });

                // Mettre à jour l'adapter
                adapter.setMatches(matchList);

                // Gérer l'affichage vide
                progressBar.setVisibility(View.GONE);
                if (matchList.isEmpty()) {
                    tvEmpty.setVisibility(View.VISIBLE);
                    tvEmpty.setText("Aucun historique disponible");
                } else {
                    tvEmpty.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                tvEmpty.setVisibility(View.VISIBLE);
                tvEmpty.setText("Erreur de chargement : " + error.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Nettoyer les ressources
        if (adapter != null) {
            adapter.setMatches(new ArrayList<>());
        }
    }
}