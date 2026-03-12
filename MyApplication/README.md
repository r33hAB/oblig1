# DAT153 Oblig 2 - Quiz App

Android quiz-app med bildegalleri, quiz-spill, Room-database, ViewModel,
ContentProvider og Espresso UI-tester.

## Arkitektur

- **MVVM** med Repository-mønster
- **Room** for lokal persistering av galleri-metadata
- **ViewModel** (GalleryViewModel, QuizViewModel) for UI-tilstand
- **ContentProvider** for ekstern tilgang til galleri-data
- **Jetpack Compose** for Quiz- og Galleri-skjermer
- **XML-layout** for hovedmenyen (MainActivity)

## ContentProvider

**Authority:** `com.example.quizapp.provider.gallery`
**Base URI:** `content://com.example.quizapp.provider.gallery/gallery_items`

Kolonner: `_id`, `name`, `URI`

### ADB-testing av ContentProvider

**Query alle elementer:**
```bash
adb shell content query --uri content://com.example.quizapp.provider.gallery/gallery_items
```

**Query enkelt element (id=1):**
```bash
adb shell content query --uri content://com.example.quizapp.provider.gallery/gallery_items/1
```

**Sett inn nytt element:**
```bash
adb shell content insert --uri content://com.example.quizapp.provider.gallery/gallery_items --bind name:s:"My Photo" --bind URI:s:"content://media/external/images/media/123"
```

**Slett element (id=1):**
```bash
adb shell content delete --uri content://com.example.quizapp.provider.gallery/gallery_items/1
```

**Forventet output (query):**
```
Row: 0 _id=1, name=Hund, URI=android.resource://com.example.quizapp/2131165269
Row: 1 _id=2, name=Kanin, URI=android.resource://com.example.quizapp/2131165270
Row: 2 _id=3, name=Katt, URI=android.resource://com.example.quizapp/2131165271
```

---

## Espresso UI-tester

Alle tester ligger i `app/src/androidTest/java/com/example/quizapp/`.

### Test 1: Navigasjon fra hovedmenyen

| | |
|--|---|
| **Klasse/metode** | `MainMenuTest.clicking_gallery_button_opens_gallery_activity()` og `MainMenuTest.clicking_quiz_button_opens_quiz_activity()` |
| **Hva testes** | At knappene i hovedmenyen navigerer til riktig aktivitet. |
| **Beskrivelse** | Brukeren er på hovedmenyen og ser to knapper: "Galleri" og "Start Quiz". Når brukeren klikker på "Galleri"-knappen, skal GalleriActivity starte. Når brukeren klikker på "Start Quiz"-knappen, skal QuizActivity starte. |
| **Fremgangsmåte** | 1. MainActivity startes via ActivityScenarioRule. 2. Espresso Intents initialiseres for å fange opp Intents. 3. Espresso klikker på knappen (R.id.knappGalleri eller R.id.knappQuiz). 4. Vi verifiserer at riktig Intent ble sendt med `Intents.intended(hasComponent(...))`. |
| **Forventet resultat** | En Intent med `component = GalleriActivity` (eller `QuizActivity`) ble sendt. |
| **Status** | Bestått / Ikke kjørt ennå (må kjøres på emulator) |

### Test 2: Quiz-score oppdateres korrekt

| | |
|---|---|
| **Klasse/metode** | `QuizTest.quiz_score_updates_correctly_after_right_and_wrong_answer()` |
| **Hva testes** | At scoren oppdateres korrekt etter ett feil og ett riktig svar i quizen. |
| **Beskrivelse** | Brukeren starter quizen. Et bilde vises med tre svaralternativer. Brukeren gir først et feil svar — scoren skal vise 0 riktige av 1 totalt. Deretter går brukeren til neste spørsmål og gir et riktig svar — scoren skal vise 1 riktig av 2 totalt. |
| **Fremgangsmåte** | 1. QuizActivity startes direkte via createAndroidComposeRule (uten å gå via hovedmenyen). 2. Vi venter til quiz-spørsmålet er lastet (VenterPaaSvar-fasen). 3. Vi leser riktig svar fra ViewModel og velger et FEIL alternativ. 4. Vi verifiserer at "Feil!" vises og at scoreRiktige=0, scoreTotalt=1. 5. Vi klikker "Neste spørsmål". 6. Vi leser riktig svar fra ViewModel og velger det RIKTIGE alternativet. 7. Vi verifiserer at "Riktig!" vises og at scoreRiktige=1, scoreTotalt=2. |
| **Forventet resultat** | Etter feil svar: score = 0/1. Etter riktig svar: score = 1/2. |
| **Status** | Bestått / Ikke kjørt ennå (må kjøres på emulator) |

### Test 3: Galleri-antall etter tillegging og sletting

| | |
|---|---|
| **Klasse/metode** | `GalleryTest.gallery_item_count_correct_after_add_and_delete()` |
| **Hva testes** | At antall registrerte bilder er korrekt etter tillegging og sletting av et element. |
| **Beskrivelse** | Brukeren åpner galleriet som inneholder 3 innebygde bilder. Brukeren legger til et nytt bilde ved å klikke "+"-knappen, velge et bilde (simulert med Intent Stubbing) og gi det navnet "TestDyr". Antallet skal nå være 4. Deretter sletter brukeren "TestDyr" via slett-knappen og bekrefter. Antallet skal være tilbake til 3. |
| **Fremgangsmåte** | 1. GalleriActivity startes direkte via createAndroidComposeRule. 2. Vi venter til galleriet er lastet og verifiserer at det er 3 elementer. 3. Vi bruker Intent Stubbing: `Intents.intending(hasAction(ACTION_OPEN_DOCUMENT)).respondWith(result)` for å returnere en resource-URI når bildevelgeren åpnes. 4. Vi klikker FAB (+), skriver "TestDyr" i navne-dialogen og bekrefter. 5. Vi verifiserer at antallet er 4 via ViewModel. 6. Vi klikker slett-knappen (testTag "slett_TestDyr") og bekrefter i dialogen. 7. Vi verifiserer at antallet er tilbake til 3. |
| **Forventet resultat** | Start: 3 bilder. Etter tillegging: 4 bilder. Etter sletting: 3 bilder. |
| **Status** | Bestått / Ikke kjørt ennå (må kjøres på emulator) |

---

