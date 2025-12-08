package com.zaro.pronosticapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.zaro.pronosticapp.R;
import com.zaro.pronosticapp.activities.AdminDashboardActivity;
import com.zaro.pronosticapp.activities.AdminLoginActivity;
import com.zaro.pronosticapp.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerAdapter adapter;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialiser Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialiser les vues
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        // Configurer la toolbar
        setSupportActionBar(toolbar);

        // Configurer ViewPager2 avec l'adapter
        adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Lier TabLayout avec ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Pronostics");
                            break;
                        case 1:
                            tab.setText("Historique");
                            break;
                    }
                }
        ).attach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        // Afficher/masquer l'option admin selon l'authentification
        FirebaseUser currentUser = mAuth.getCurrentUser();
        MenuItem adminItem = menu.findItem(R.id.action_admin);
        MenuItem logoutItem = menu.findItem(R.id.action_logout);

        if (currentUser != null) {
            adminItem.setVisible(false);
            logoutItem.setVisible(true);
        } else {
            adminItem.setVisible(true);
            logoutItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_admin) {
            // Ouvrir la page de connexion admin
            Intent intent = new Intent(this, AdminLoginActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_dashboard) {
            // Ouvrir le tableau de bord admin
            Intent intent = new Intent(this, AdminDashboardActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_logout) {
            // Déconnexion
            mAuth.signOut();
            invalidateOptionsMenu(); // Rafraîchir le menu
            return true;
        } else if (id == R.id.action_refresh) {
            // Rafraîchir les données
            refreshData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshData() {
        // Notifier les fragments de rafraîchir leurs données
        if (adapter != null) {
            int currentItem = viewPager.getCurrentItem();
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(currentItem, false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Rafraîchir le menu au retour sur l'activité
        invalidateOptionsMenu();
    }
}