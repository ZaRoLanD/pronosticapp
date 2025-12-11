package com.zaro.pronosticapp.utilis;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.zaro.pronosticapp.BuildConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Utilitaire pour vérifier les mises à jour via Firebase Remote Config.
 * Remote Config doit définir les clés :
 * - latest_version_code (number)
 * - update_url (string) -> lien vers GitHub Release ou page de téléchargement
 * - force_update (boolean)
 */
public class UpdateChecker {

    public static void check(final Context ctx) {
        FirebaseRemoteConfig rc = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings settings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        rc.setConfigSettingsAsync(settings);

        Map<String, Object> defaults = new HashMap<>();
        defaults.put("latest_version_code", BuildConfig.VERSION_CODE);
        defaults.put("update_url", "");
        defaults.put("force_update", false);
        rc.setDefaultsAsync(defaults);

        rc.fetchAndActivate().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                long latest = rc.getLong("latest_version_code");
                String url = rc.getString("update_url");
                boolean force = rc.getBoolean("force_update");

                if (latest > BuildConfig.VERSION_CODE && url != null && !url.isEmpty()) {
                    showUpdateDialog(ctx, url, force);
                }
            }
        });
    }

    private static void showUpdateDialog(Context ctx, String url, boolean force) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setTitle("Mise à jour disponible");
            builder.setMessage("Une nouvelle version de l'application est disponible. Veuillez mettre à jour.");
            builder.setCancelable(!force);

            builder.setPositiveButton("Mettre à jour", (dialog, which) -> {
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(i);
                } catch (Exception ignored) {
                }
            });

            if (!force) {
                builder.setNegativeButton("Plus tard", (d, w) -> d.dismiss());
            }

            AlertDialog dialog = builder.create();
            if (ctx instanceof android.app.Activity) {
                android.app.Activity act = (android.app.Activity) ctx;
                if (!act.isFinishing()) dialog.show();
            } else {
                dialog.show();
            }
        });
    }
}
