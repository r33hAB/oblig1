# DAT153 Oblig 2 Boilerplate

This repository contains the Oblig 1 Android app together with a non-invasive
boilerplate for DAT153 Oblig 2.

The new files are skeletons only. They are meant to help a group of three
students divide the work without replacing the existing Oblig 1 solution.

## Suggested Work Split

- Student 1: Room database and repository
- Student 2: ViewModels and screen state
- Student 3: ContentProvider and Espresso tests

## Required For Oblig 2

- TODO: Connect Room so gallery metadata is persisted.
- TODO: Add ViewModels for gallery and quiz screen state.
- TODO: Implement the 3 required Espresso tests.
- TODO: Register and test the ContentProvider with `adb shell content`.

## Nice To Have

- TODO: Improve repository structure if the team wants a cleaner data flow.
- TODO: Add a dedicated test database or fake data source for instrumentation tests.
- TODO: Improve quiz question selection beyond the minimum required behavior.
- TODO: Keep the suggested work split if parallel work helps the group.

## TODO: Test 1 Description

- TODO: Describe the first required Espresso test from Oblig 2.
- TODO: State what screen is tested, what the user action is, and what should happen.

## TODO: Test 2 Description

- TODO: Describe the second required Espresso test from Oblig 2.
- TODO: State what screen is tested, what the user action is, and what should happen.

## TODO: Test 3 Description

- TODO: Describe the third required Espresso test from Oblig 2.
- TODO: State what screen is tested, what the user action is, and what should happen.

## TODO: Expected Results

- TODO: Document the expected result for each required test.
- TODO: Keep the expected result short and easy to check during demo or grading.

## TODO: Pass/Fail

- TODO: Describe how the team will decide if each test passes or fails.
- TODO: Add one pass/fail statement per required test.

## TODO: ADB Testing Of The ContentProvider

- TODO: Add example `adb shell content query` command.
- TODO: Add example `adb shell content insert` command if required.
- TODO: Add example `adb shell content delete` command if required.
- TODO: Document the expected provider output.
- TODO: Note which provider URI should be used for collection and single item access.

## Notes

- The existing Oblig 1 app logic has not been replaced.
- QuizApplication is still part of the project and should remain until the
  team intentionally migrates responsibilities.
- External image permission handling in GalleriActivity should be preserved.

## Implementation Checklist

### 1. Room Database (Victoria)

- [x] Create gallery metadata `Entity`
- [x] Define primary key
- [x] Add fields for image URI
- [x] Add fields for title or label if needed
- [x] Create DAO interface
- [x] Add insert function
- [x] Add query-all function
- [x] Add delete or replace strategy
- [x] Create Room `Database` class
- [x] Register entity in database
- [x] Expose DAO from database
- [x] Create repository
- [x] Connect repository to DAO
- [x] Keep database access out of activities

### 2. ViewModels (Andreas)

- [x] Create `GalleryViewModel`
- [x] Handle gallery screen state
- [x] Load saved gallery metadata
- [x] Expose gallery list to UI
- [x] Add function to save new gallery item
- [x] Add function to refresh data
- [x] Create `QuizViewModel`
- [x] Handle quiz screen state
- [x] Keep track of current question
- [x] Keep track of score
- [x] Keep track of finished quiz state
- [x] Add function to submit answer
- [x] Add function to move to next question
- [x] Add function to restart quiz

### 3. Data Handling (Victoria)

- [ ] Persist gallery metadata in Room
- [ ] Load stored data on app startup
- [ ] Restore gallery list after app restart
- [ ] Store URI as text value
- [ ] Do not store full image files in Room
- [ ] Keep URI handling consistent in repository and ViewModel

### 4. ContentProvider (Andreas)

- [ ] Create provider class
- [ ] Define authority constant
- [ ] Define content URI constant
- [ ] Define table or path constants
- [ ] Implement `query()`
- [ ] Implement `insert()`
- [ ] Implement `delete()`
- [ ] Implement `update()` if required
- [ ] Implement `getType()`
- [ ] Register provider in `AndroidManifest.xml`
- [ ] Verify exported settings match assignment needs

### 5. ContentProvider Testing With adb (Kristoffer)

- [ ] Add `adb shell content query` example
- [ ] Add `adb shell content insert` example
- [ ] Add `adb shell content delete` example
- [ ] Test collection URI
- [ ] Test single-item URI if supported
- [ ] Verify returned data matches Room content

### 6. Espresso UI Tests (Kristoffer)

- [ ] Create navigation test
- [ ] Verify screen changes are correct
- [ ] Create quiz score test
- [ ] Verify score updates after answers
- [ ] Create gallery test
- [ ] Verify gallery item is shown after load or insert
- [ ] Stub intents when external actions are triggered
- [ ] Keep tests stable and repeatable

### 7. Test Structure (Kristoffer)

- [ ] Put instrumentation tests in the correct test folder
- [ ] Use clear test class names
- [ ] Use clear test method names
- [ ] Keep one main purpose per test
- [ ] Reset test data between runs if needed
- [ ] Document what each test checks

### 8. README Final Documentation

- [ ] Replace placeholder TODO text
- [ ] Document final Room setup
- [ ] Document final ViewModel setup
- [ ] Document final ContentProvider URI values
- [ ] Document final adb test commands
- [ ] Document the 3 required Espresso tests
- [ ] Keep README short and easy to grade

### 9. Git Repository Checklist

- [ ] Create a simple branch plan
- [ ] Assign one main area to each student
- [ ] Use pull requests or clean merge steps
- [ ] Review each other’s code before merge
- [ ] Avoid direct edits to the same files at the same time
- [ ] Make sure final main branch builds

### 10. Lab Presentation Topics

- [ ] Show Room entity, DAO, database, and repository
- [ ] Explain `GalleryViewModel`
- [ ] Explain `QuizViewModel`
- [ ] Demo persisted gallery data after restart
- [ ] Demo ContentProvider with `adb`
- [ ] Demo Espresso navigation test
- [ ] Demo Espresso quiz score test
- [ ] Demo Espresso gallery test
- [ ] Be ready to explain team work split
