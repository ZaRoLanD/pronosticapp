package com.zaro.pronosticapp.fragments;

import android.os.Bundle;
import android.util.Log;
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
import com.zaro.pronosticapp.adapters.MatchAdapter;
import com.zaro.pronosticapp.models.Match;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MatchListFragment extends Fragment {

    private static final String TAG = "MatchListFragment";
    private RecyclerView recyclerView;
    private MatchAdapter adapter;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private DatabaseReference matchesRef;
    private List<Match> matchList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_list, container, false);

        // Initialiser Firebase
        matchesRef = FirebaseDatabase.getInstance().getReference("matches");

        // Initialiser les vues
        recyclerView = view.findViewById(R.id.recyclerViewMatches);
        progressBar = view.findViewById(R.id.progressBar);
        tvEmpty = view.findViewById(R.id.tvEmpty);

        // Configurer RecyclerView
        adapter = new MatchAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Charger les matchs
        loadUpcomingMatches();

        return view;
    }

    private void loadUpcomingMatches() {
        progressBar.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);

        // Écouter TOUS les matchs
        matchesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "=== DÉBUT CHARGEMENT ===");
                Log.d(TAG, "Nombre de matchs dans Firebase: " + snapshot.getChildrenCount());

                matchList.clear();
                long currentTime = System.currentTimeMillis();

                for (DataSnapshot data : snapshot.getChildren()) {
                    Match match = data.getValue(Match.class);

                    if (match != null) {
                        Log.d(TAG, "Match: " + match.getTeamsDisplay());
                        Log.d(TAG, "  Status: '" + match.getStatus() + "'");
                        Log.d(TAG, "  isVisible: " + match.isVisible());
                        Log.d(TAG, "  visibleFrom: " + match.getVisibleFrom());
                        Log.d(TAG, "  currentTime: " + currentTime);

                        // CONDITION SIMPLIFIÉE : On vérifie juste 3 choses
                        boolean isPending = "pending".equalsIgnoreCase(match.getStatus());
                        boolean isVisibleFlag = match.isVisible();
                        boolean isTimeToShow = currentTime >= match.getVisibleFrom();

                        Log.d(TAG, "  isPending: " + isPending);
                        Log.d(TAG, "  isVisibleFlag: " + isVisibleFlag);
                        Log.d(TAG, "  isTimeToShow: " + isTimeToShow);

                        if (isPending && isVisibleFlag && isTimeToShow) {
                            matchList.add(match);
                            Log.d(TAG, "  ✅ AJOUTÉ À LA LISTE");
                        } else {
                            Log.d(TAG, "  ❌ NON AJOUTÉ");
                        }
                    }
                }

                Log.d(TAG, "Total matchs ajoutés: " + matchList.size());
                Log.d(TAG, "=== FIN CHARGEMENT ===");

                // Trier les matchs par date (du plus proche au plus éloigné)
                Collections.sort(matchList, new Comparator<Match>() {
                    @Override
                    public int compare(Match m1, Match m2) {
                        return Long.compare(m1.getMatchTime(), m2.getMatchTime());
                    }
                });

                // Mettre à jour l'adapter
                adapter.setMatches(matchList);

                // Gérer l'affichage vide
                progressBar.setVisibility(View.GONE);
                if (matchList.isEmpty()) {
                    tvEmpty.setVisibility(View.VISIBLE);
                    tvEmpty.setText("Aucun pronostic disponible pour le moment");
                } else {
                    tvEmpty.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Erreur Firebase: " + error.getMessage());
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