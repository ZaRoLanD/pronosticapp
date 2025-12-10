# 🏆 Pronostic App - Application de Pronostics Sportifs

Une application Android moderne pour gérer et tracker vos pronostics sportifs avec une interface intuitive et une synchronisation cloud en temps réel via Firebase.

## 📋 Table des matières

- [Fonctionnalités](#-fonctionnalités)
- [Prérequis](#-prérequis)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [Structure du projet](#-structure-du-projet)
- [Technologies utilisées](#-technologies-utilisées)
- [Architecture](#-architecture)
- [Utilisation](#-utilisation)
- [Contribution](#-contribution)
- [Licence](#-licence)

## ✨ Fonctionnalités

- ✅ **Authentification utilisateur** - Connexion sécurisée avec Firebase Authentication
- 📊 **Gestion des pronostics** - Créer, modifier, supprimer et tracker vos pronostics
- 🔄 **Synchronisation cloud** - Synchronisation en temps réel avec Firebase Realtime Database
- 👤 **Profil utilisateur** - Gestion personnalisée de votre compte
- 📱 **Interface moderne** - Design Material Design avec support responsive
- 🎯 **Statistiques** - Suivi de vos performances et historique de pronostics
- 🔐 **Sécurité** - Authentification robuste et données chiffrées

## 🔧 Prérequis

Avant de commencer, assurez-vous d'avoir :

- **Android Studio** (version Flamingo ou supérieure)
- **JDK 17** ou supérieur
- **Gradle 8.0** ou supérieur
- **Un compte Firebase** (configuration gratuite)
- **Android 7.0 (API 24)** minimum sur votre appareil

## 📦 Installation

### 1. Cloner le repository

```bash
git clone https://github.com/ZaRoLanD/pronosticapp.git
cd pronosticapp
```

### 2. Ouvrir dans Android Studio

- Ouvrez Android Studio
- Sélectionnez **Open an existing project**
- Naviguez jusqu'au dossier `pronosticapp`
- Attendez la synchronisation Gradle

### 3. Configurer les dépendances

Android Studio téléchargera automatiquement toutes les dépendances définies dans `build.gradle`.

## 🔐 Configuration

### Configuration Firebase

1. **Créer un projet Firebase** :
   - Allez sur [Firebase Console](https://console.firebase.google.com/)
   - Cliquez sur "Créer un projet"
   - Suivez les étapes de configuration

2. **Télécharger `google-services.json`** :
   - Dans la console Firebase, allez à **Paramètres du projet**
   - Téléchargez le fichier `google-services.json`
   - Placez-le dans le dossier `app/`

3. **Activer les services Firebase** :
   - **Authentication** : Email/Mot de passe
   - **Realtime Database** : Mode test ou avec règles de sécurité appropriées
   - **Storage** : Pour les images utilisateur (optionnel)

### Configuration locale

Créez un fichier `local.properties` à la racine du projet :

```properties
sdk.dir=/path/to/android/sdk
```

## 📁 Structure du projet

```
pronosticapp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/zaro/pronosticapp/
│   │   │   │   ├── activities/          # Activités principales
│   │   │   │   ├── fragments/           # Fragments réutilisables
│   │   │   │   ├── adapters/            # Adaptateurs RecyclerView
│   │   │   │   ├── models/              # Classes de données
│   │   │   │   ├── utilis/              # Utilitaires et helpers
│   │   │   │   └── MainActivity.java    # Point d'entrée
│   │   │   ├── res/                     # Ressources (layout, drawable, values)
│   │   │   └── AndroidManifest.xml      # Manifest de l'application
│   │   ├── test/                        # Tests unitaires
│   │   └── androidTest/                 # Tests instrumentés
│   ├── build.gradle                     # Configuration Gradle
│   └── google-services.json             # Configuration Firebase
├── gradle/                              # Configuration Gradle Wrapper
├── build.gradle                         # Script de build principal
├── settings.gradle                      # Configuration des modules
└── README.md                            # Ce fichier
```

## 🛠 Technologies utilisées

### Framework & SDK

- **Android SDK** : API 24 - 34
- **Java 17**
- **Kotlin** (optionnel)

### Dépendances principales

- **AndroidX** : AppCompat, ConstraintLayout, RecyclerView, CardView
- **Material Design** : Google Material Components
- **Firebase** : 
  - Authentication (gestion des utilisateurs)
  - Realtime Database (synchronisation données)
  - Storage (stockage fichiers)
  - Analytics (suivi événements)
- **GSON** : Sérialisation JSON
- **JUnit** & **Espresso** : Tests

## 🏗 Architecture

L'application suit l'architecture **MVC (Model-View-Controller)** avec les bonnes pratiques Android :

```
┌─────────────────────────────────────────┐
│         Activities/Fragments            │ ← Presentation Layer
│        (UI Controllers)                 │
└────────────────┬────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────┐
│          Adapters & Utils               │ ← Business Logic
│        (Data Processing)                │
└────────────────┬────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────┐
│     Models & Firebase Services          │ ← Data Layer
│        (Data Management)                │
└─────────────────────────────────────────┘
```

## 💻 Utilisation

### Lancer l'application

#### Avec un appareil physique :
1. Connectez votre appareil Android
2. Activez le **USB Debugging** dans les paramètres développeur
3. Cliquez sur **Run** (Shift + F10) dans Android Studio

#### Avec l'émulateur :
1. Ouvrez **AVD Manager** dans Android Studio
2. Créez ou lancez un appareil virtuel
3. Cliquez sur **Run**

### Build

#### Build de debug :
```bash
./gradlew assembleDebug
```

#### Build de release :
```bash
./gradlew assembleRelease
```

#### Générer un APK :
```bash
./gradlew build
```

L'APK se trouvera dans `app/build/outputs/apk/`

## 🧪 Tests

### Exécuter les tests unitaires :
```bash
./gradlew test
```

### Exécuter les tests instrumentés :
```bash
./gradlew connectedAndroidTest
```

## 📝 Contribution

Les contributions sont bienvenues ! Voici comment contribuer :

1. **Fork** le repository
2. **Créez une branche** pour votre fonctionnalité (`git checkout -b feature/AmazingFeature`)
3. **Commitez vos changements** (`git commit -m 'Add some AmazingFeature'`)
4. **Poussez vers la branche** (`git push origin feature/AmazingFeature`)
5. **Ouvrez une Pull Request**

### Standards de code

- Suivre les conventions de nommage Android
- Commenter le code complexe
- Tester vos changements
- Respecter la structure existante du projet

## 🐛 Signaler un bug

Si vous trouvez un bug, veuillez :
1. Vérifier que le bug n'existe pas déjà dans les **Issues**
2. Créer une nouvelle issue avec :
   - Une description claire du problème
   - Les étapes pour reproduire
   - Les logs d'erreur si applicable
   - Votre version d'Android et d'Android Studio

## 📈 Feuille de route

- [ ] Intégration des notifications push
- [ ] Mode hors ligne
- [ ] Partage de pronostics
- [ ] Système de ligues
- [ ] Dashboard avancée avec graphiques
- [ ] Support du mode sombre
- [ ] Application web (companion)

## 📞 Support

Pour obtenir de l'aide :
- 📧 Email : yroland320@gmail.com


## 📄 Licence

Ce projet est sous licence **MIT**. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

---

## 👨‍💻 Auteur

**ZaRoLanD**
- GitHub: [@ZaRoLanD](https://github.com/ZaRoLanD)
- Projet: [Pronostic App](https://github.com/ZaRoLanD/pronosticapp)

---

## ⭐ N'oubliez pas !

Si ce projet vous a été utile, pensez à lui laisser une ⭐ star sur GitHub !

---

**Dernière mise à jour** : Décembre 2025
