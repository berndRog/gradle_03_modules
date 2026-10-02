# Android-Projekt von GitHub nach GitLab übernehmen

Ziel: Für die Vorlesung eine eigene Projektkopie auf dem GitLab der Hochschule bereitstellen. Die Entwicklung bleibt auf GitHub. Die Vorlesungskopie beginnt mit einer neuen Git-Historie und verwendet lokal und auf GitLab den Branch `main`.

## 1. Projektkopie vorbereiten

1. Kopiere das Android-Projekt in einen separaten Vorlesungsordner.
2. Behalte die Projektdateien und die `.gitignore` bei.
3. Schließe das Projekt in Android Studio, bevor du seine Git-Verwaltung austauschst.
4. Erstelle auf GitLab ein **leeres Repository ohne README, Lizenz und automatisch erzeugte `.gitignore`**.
5. Kopiere dessen SSH- oder HTTPS-Clone-URL.

Der Name des GitLab-Repositories darf mit dem GitHub-Namen übereinstimmen. Entscheidend ist die Remote-URL.

**Wichtig:** Die folgenden Befehle ausschließlich in der Vorlesungskopie ausführen. Das ursprüngliche Entwicklungsprojekt bleibt erhalten.

## 2. Neue Historie erstellen und nach GitLab übertragen

Wechsle im Terminal in den Vorlesungsordner. Ersetze die beiden Beispielpfade durch deinen tatsächlichen Ordner und deine GitLab-URL.

```bash
# Vorlesungskopie öffnen und den aktuellen Ordner kontrollieren
cd /Pfad/zum/Vorlesungsprojekt
pwd

# Alte lokale Git-Verwaltung einschließlich der Historie entfernen
rm -rf .git

# Neues Git-Repository mit dem Branch main initialisieren
git init -b main

# Dateien aufnehmen; die vorhandene .gitignore wird berücksichtigt
git add .

# Vor dem Commit prüfen, welche Dateien aufgenommen wurden
git status

# Ersten Commit der neuen Historie erstellen
git commit -m "Initial course version"

# GitLab verbinden: Beispiel-URL ersetzen
git remote add origin 'https://git-fkb.ostfalia.de/mobilesysteme2627/a2_01_count.git'

# main nach GitLab übertragen und Tracking einrichten
git push -u origin main

# Ergebnis kontrollieren
git status
git branch --show-current
git remote -v
```

`git init -b main` erstellt die neue Git-Verwaltung im Ordner `.git` und legt `main` als Namen des ersten Branches fest. Der erste Commit entsteht erst mit `git commit`; die Verbindung zu GitLab entsteht mit `git remote add origin`.

`rm -rf .git` entfernt die lokale Git-Historie und Remote-Konfiguration der Kopie. Die Android-Quelldateien sowie die Historie auf GitHub bleiben erhalten. Bei einer normalen Projektkopie muss `.git` ein eigener Ordner sein; bei einem Git-Worktree diese Anleitung nicht zum Zurücksetzen verwenden.

Prüfe vor dem Commit insbesondere, dass `local.properties`, Build-Verzeichnisse, Zugangsdaten und private API-Schlüssel nicht aufgenommen werden. Die `.gitignore` muss die entsprechenden lokalen Dateien ausschließen.

## 3. Ergebnis prüfen

Erwartete Ausgabe von `git status`:

```text
On branch main
Your branch is up to date with 'origin/main'.

nothing to commit, working tree clean
```

`git branch --show-current` liefert `main`. Bei `git remote -v` müssen Fetch und Push auf die GitLab-URL zeigen.

Prüfe auf GitLab, dass `main` der Standardbranch ist. Die neue Historie beginnt mit dem Commit `Initial course version`.

## 4. Die richtige Projektkopie in Android Studio öffnen

1. Wähle **File → Close Project**.
2. Öffne über **Open** ausdrücklich den neuen Vorlesungsordner. Verwende bei gleichnamigen Projekten zunächst keinen alten Eintrag aus der Liste zuletzt geöffneter Projekte.
3. Prüfe im Terminal dieses Android-Studio-Fensters:

   ```bash
   pwd
   git status
   git remote -v
   ```

4. Unter **Settings → Version Control → Directory Mappings** muss der aktuelle Projektordner beziehungsweise `<Project>` dem VCS **Git** zugeordnet sein. Entferne veraltete Zuordnungen zu anderen Projektordnern.
5. Unter **Git → Manage Remotes** muss `origin` auf GitLab zeigen.

Falls Android Studio weiterhin `master` anzeigt, obwohl das Terminal `main` meldet, prüfe zuerst den tatsächlich geöffneten Projektpfad und die Directory Mappings. Schließe Android Studio gegebenenfalls vollständig und öffne den richtigen Projektordner erneut ausdrücklich über **Open**. In unserem Fall hat dieses explizite Öffnen die veraltete Anzeige behoben.

### Alter GitHub-Eintrag in workspace.xml

Ein Eintrag wie `GithubPullRequestsUISettings` in `.idea/workspace.xml` speichert die zuletzt ausgewählte Verbindung des GitHub-Pull-Requests-Fensters. Er bestimmt weder den aktuellen Branch noch das Ziel von `git push`.

Dieser Eintrag kann bei vollständig geschlossenem Android Studio entfernt werden, ist aber für die GitLab-Verbindung nicht maßgeblich. Maßgeblich sind die Remote-URL in `.git/config` und die Ausgabe von `git remote -v`. Ein hinterlegtes GitHub-Konto in den IDE-Einstellungen darf ebenfalls erhalten bleiben.

## 5. Sonderfälle

### Vorhandenen lokalen Branch master nur in main umbenennen

Wenn Git bereits korrekt eingerichtet ist und nur der Branchname geändert werden soll:

```bash
git branch -m master main
git push -u origin main
```

Danach gegebenenfalls auf GitLab `main` zum Standardbranch machen. Erst anschließend einen nicht mehr benötigten Remote-Branch `master` löschen:

```bash
git push origin --delete master
git fetch --prune
```

Die Umbenennung erhält die Historie. Für eine neue Historie verwende stattdessen den Ablauf aus Abschnitt 2.

### GitLab enthält bereits eine Historie, die erhalten bleiben soll

Klone das vorhandene GitLab-Repository in einen separaten Ordner:

```bash
git clone 'https://gitlab.example.edu/gruppe/projekt.git' Vorlesungsprojekt
```

Kopiere den gewünschten Android-Projektstand hinein, **ohne die `.git` des GitHub-Projekts**. Die `.git` des GitLab-Clones bleibt erhalten. Entferne dabei bewusst auch veraltete Projektdateien, die im neuen Stand nicht mehr vorkommen; bloßes Überschreiben entfernt sie nicht.

```bash
cd Vorlesungsprojekt
git status
git add .
git commit -m "Update course examples"
git push
```

Die bisherigen GitLab-Commits bleiben erhalten; der neue Projektstand kommt als weiterer Commit hinzu. Nur die Remote-URL eines bestehenden GitHub-Clones zu ändern übernimmt dagegen dessen Historie und ist kein Neustart.
