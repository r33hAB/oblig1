# DAT153 Task 3 - Review Report

> **Reviewed repo:** *https://github.com/soukupj00/DAT153-Quiz-App*
> **Our repo:** https://github.com/r33hAB/oblig1
> **Date:** 2026-04-06


## Table of Contents

- [2a) How does the gallery let you pick an image?](#2a-how-does-the-gallery-let-you-pick-an-image)
- [2b) How does the quiz work?](#2b-how-does-the-quiz-work)
- [2c) ContentProvider](#2c-contentprovider)
- [What we learned](#what-we-learned)


## 2a) How does the gallery let you pick an image?

### Their solution

They use `rememberLauncherForActivityResult` with `ActivityResultContracts.OpenDocument()` in `GalleryScreen.kt` (line 97-104). This is the Compose way of launching a file picker. When the user taps the + button (line 137), it opens the system file picker filtered to only show images (`arrayOf("image/*")`).

```kotlin
val pickImageLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.OpenDocument()
) { uri: Uri? ->
    uri?.let {
        newImageUri = it
        showNameDialog = true
    }
}
```

When a user picks an image, the Uri gets saved in a state variable and a dialog pops up asking for a name. The persistable URI permission (so the app can still access the image later) is only requested when the user confirms the name, inside the `onConfirm` callback at line 154:

```kotlin
val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION
context.contentResolver.takePersistableUriPermission(uri, takeFlags)
```

If that fails (SecurityException), the image still gets saved anyway (line 164-171). After that, it inserts a `QuizItem` into Room with the name, URI string, and an `isDrawable = false` flag.

### Our solution

We do pretty much the same thing but with `registerForActivityResult` in `GalleriActivity.kt` (line 108-125). This is an outdated Activity-based way of doing it, and not the Compose-specific one. Same `OpenDocument()` contract, same `arrayOf("image/*")` filter.

Another difference is we request the persistable URI permission right away in the callback, before the naming dialog even shows up (line 113-119). Then we store the URI in `ventedeUri` and show the dialog. The image only gets saved to the database when the user confirms a name.

```kotlin
contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
```

### What's different

- **Launcher:** We use `registerForActivityResult` (Activity-based), they use `rememberLauncherForActivityResult` (Compose-based)
- **When permission is taken:** We take it right away in the callback, they take it later when the user confirms the name
- **isDrawable flag:** We don't use one, they do

Since both projects use Compose for the UI, their `rememberLauncherForActivityResult` is the more correct choice due to it's designed for Compose and ties into the Compose lifecycle. Ours works fine though, but its an older way of doing it.

Taking permission earlier like we do is a bit safer since the permission can't expire between picking the image and confirming the name. But in practice it probably doesn't matter.
-

## 2b) How does the quiz work?

### Their solution

The quiz logic is in `QuizViewModel.kt` and `QuizUtil.kt`.

When the quiz starts, `loadQuiz()` (line 60) grabs all gallery items from the database, shuffles them, and turns each one into a quiz question using `generateQuizEntry()`. Their quiz is finite and every image gets one question, and when you've answered them all, it shows a final score.

Wrong answers are generated in `QuizUtil.kt` (line 14-19):

```kotlin
val wrongNames = gallery.filter { it.name != correctEntry.name }.shuffled().take(3).map { it.name }
val options = (wrongNames + correctEntry.name).shuffled()
```

It filters out the correct answer, shuffles the rest, grabs 3 wrong names, adds the correct one back in, and shuffles again. So you get 4 buttons total.

Answer checking is a simple string comparison in `submitAnswer()` (line 119):

```kotlin
if (option == state.entry.correctName) {
    score++
}
```

They also use `SavedStateHandle` (line 184-193) to save quiz state across process death which is nice

### Edge cases

For an empty gallery, the quiz just sits on a loading spinner forever. The code checks `if (items.isNotEmpty())` at line 63, and if there's nothing, it never moves past `Loading` state. Not great.

For 2 images, `take(3)` only finds 1 wrong answer, so you'd get 2 buttons. For 1 image, you'd get a single button with just the correct answer. It doesn't crash, but the quiz is pretty pointless with that few images.

### Our solution

Our quiz in `QuizViewModel.kt` works differently  it's infinite. Instead of showing every image once, it picks a random image each round and keeps going until you stop. We avoid showing the same image twice in a row by filtering out the previous question (line 92-97).

We generate 2 wrong answers instead of 3, so we show 3 buttons per question (line 99):

```kotlin
val feilSvar = list.filter { it.id != riktigBilde.id }.map { it.navn }.shuffled().take(ANTALL_FEIL_SVAR)
```

Answer checking is also a string comparison (line 50): `chosenAnswer == riktigSvar`.

For edge cases, we check if there are at least 3 images before starting the quiz (line 87-89). If not, we show a "too few images" error screen instead of a broken quiz:

```kotlin
if (list.size < MINIMUM_BILDER) {
    _uiState.update { it.copy(phase = QuizPhase.ForFaaBilder) }
    return@launch
}
```

### What's different

- **Quiz type:** Ours is infinite (keeps going), theirs is finite (one question per image)
- **Answer buttons:** We show 3, they show 4
- **Empty/small gallery:** We show an error message, they show a loading spinner forever / fewer buttons
- **State after process death:** Ours is lost (ViewModel only), theirs is saved (SavedStateHandle)

Our edge case handling is better showing an error is much better than a stuck loading screen. But their `SavedStateHandle` approach is something we should have done. Without it, if Android kills our app in the background, the quiz score resets.

### Can the test be turned into a loop?

Both solutions make this pretty easy. Their `FullQuizRunTest.kt` has a helper `answerQuestion()` (line 83) that reads the correct answer from the ViewModel and picks correct or wrong based on a boolean. You could wrap that in a `repeat` loop with `Random.nextBoolean()`.

Our `QuizTest.kt` does the same thing reads `phase.gjeldendeBilder.navn` from the ViewModel to know which answer is right. Also easy to loop.
-

## 2c) ContentProvider

### Their solution

`QuizProvider.kt` is registered with authority `com.example.quiz_app.provider`. It only matches one URI: `content://com.example.quiz_app.provider/quiz_items`.

The `query()` method (line 44-51) runs a hardcoded SQL string:

```kotlin
val cursor = database.query("SELECT name, uri AS URI FROM quiz_items", null)
```

It returns two columns: `name` and `URI` (aliased from `uri`). The provider is read-only `insert()`, `update()`, and `delete()` all throw `UnsupportedOperationException`.



Their code is in English, ours is in Norwegian. For group projects where others might need to read the code, English is the better choice.
