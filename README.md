
# 🧵 StyleSlick – Verkaufs- & Bestellverwaltungssoftware

**StyleSlick** ist eine vollständig in JavaFX entwickelte Desktop-Anwendung zur Verwaltung von Artikeln, Kategorien, Kunden und Rechnungen.

## ✨ Features

- **Kundenverwaltung**: Erfassen, Bearbeiten, Suchen, Löschen
- **Artikelverwaltung**: Lager, Farben, Qualitäten, Hersteller
- **Kategorisierung**: Artikel werden Kategorien zugeordnet
- **Rechnungsverwaltung**: Erstellung von Bestellungen inkl. Positionen
- **Datenvalidierung**: über eigene `Rules`-Klassen
- **MySQL-Datenbankanbindung** (flexibles Schema)
- **GUI** Mit JavaFX & JFoenix

## 🔧 Technologie-Stack

| Schicht         | Technologie                     |
|-----------------|---------------------------------|
| GUI             | JavaFX + JFoenix                |
| Business-Logic  | Services + Rules                |
| Datenzugriff    | JDBC (über zentrale `Database`) |
| Datenbank       | MySQL                           |
| Build-System    | Maven + JavaFX Plugin           |
| Logging         | SLF4J + Logback                 |


## 🏗️ Projektstruktur (modular)

```plaintext
📁 controller/         // JavaFX Controller für jede Seite
📁 service/            // Singleton-Services mit Business-Logik
📁 model/              // POJOs: Article, Invoice, Customer, ...
📁 rules/              // Validierungen mit Feedback
📁 utils/              // SceneManager
📁 mysql-dump/         // Enthält die Datenbankstruktur (.sql-Dateien)
📄 Main.java           // Einstiegspunkt
📄 pom.xml             // Abhängigkeiten & Build
```

## 🛠️ Setup (lokal ausführen)

1. **MySQL einrichten**  
   Im Ordner `mysql-dump/` befinden sich folgende Dateien:

   - `styleslickdb_article.sql`
   - `styleslickdb_category.sql`
   - `styleslickdb_customer.sql`
   - `styleslickdb_invoice.sql`
   - `styleslickdb_invoice_item.sql`

   Diese definieren die Tabellenstruktur und müssen in der MySQL-Datenbank importiert werden.

2. **`.env` konfigurieren**

   ```env
   DB_URL=jdbc:mysql://localhost:3306/styleslickdb
   DB_USER=root
   DB_PASSWORD=dein_passwort
   ```

3. **Maven Build**

   ```bash
   mvn clean install
   mvn javafx:run
   ```

4. **Login starten**
   - Der Login erfolgt **direkt über die MySQL-Zugangsdaten** (Benutzername und Passwort), die in der `.env`-Datei hinterlegt sind.
   - Es handelt sich **nicht um einen Benutzer aus der Datenbank**, sondern um den **Datenbank-Login selbst** (z. B. `root` mit Passwort).
   - Nur bei erfolgreicher Verbindung zur Datenbank wird die Anwendung gestartet.

## 📦 Abhängigkeiten (Auszug)

- `org.openjfx` – JavaFX UI, FXML, Web, Swing
- `com.jfoenix` – Material UI-Komponenten
- `io.github.cdimascio` – dotenv für Umgebungsvariablen
- `org.slf4j` + `logback-classic` – Logging
- `org.kordamp.bootstrapfx` – UI-Styling
- `eu.hansolo.tilesfx` – Statistische Darstellung

## ✅ Besonderheiten

- **Komplette Trennung** von GUI, Logik und Validierung
- **Datenbankunabhängig erweiterbar**
- **GUI-Navigation mit `SceneManager`**
- **Nutzerführung über `AlertService`**
- **Lagerbestand wird automatisch geprüft & angepasst**

## 📄 Projektstatus & Lizenz

Dieses Projekt wurde **aus eigenem Antrieb und Interesse** entwickelt – ohne Anleitung, ohne Vorlage, komplett selbst erdacht.  
Es befindet sich aktuell **in aktiver Entwicklung** und wird kontinuierlich verbessert.  
Der jetzige Stand ist funktionsfähig, aber noch nicht abgeschlossen – und das ist gewollt: Ich lerne und entwickle gleichzeitig.

Die Software kann **frei verwendet, angepasst und erweitert** werden.

---

📘 Dieses Projekt zeigt, wie viel man schaffen kann, wenn man nicht wartet, sondern anfängt.  
Ohne Anleitung, aber mit Motivation, Disziplin und dem Wunsch, zu verstehen – nicht nur zu kopieren.

👋 Ich bin Anton Grünwald (iTzMagiic) – ich lerne jeden Tag dazu und baue Dinge, die funktionieren.

---

**Entwickelt mit Leidenschaft von [iTzMagiic](https://github.com/iTzMagiic)**

