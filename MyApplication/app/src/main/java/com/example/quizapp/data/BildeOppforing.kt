/**
 * BildeOppforing.kt - Datamodell for bilde-navn-par i Quiz-appen
 *
 * Denne filen definerer datastrukturen som brukes for å lagre informasjon
 * om hvert bilde i galleriet og quizen.
 *
 * DESIGNVALG:
 * - Vi bruker en data class fordi den automatisk gir oss equals(), hashCode(),
 *   toString() og copy() metoder som er nyttige for Collections
 * - Uri brukes for å referere til bilder fordi det fungerer både for:
 *   - Resource-bilder (android.resource://...)
 *   - Bilder valgt fra telefonens galleri (content://...)
 * - erInnebygd flagget hjelper oss å skille mellom forhåndslastede bilder
 *   og bilder lagt til av brukeren (nyttig for fremtidig persistering)
 */

package com.example.quizapp.data

import android.net.Uri

/**
 * Representerer noe lagt til i bildegalleriet.
 *
 * @property id Unik identifikator for oppføringen. Brukes for å identifisere
 *              oppføringer når vi skal slette eller oppdatere dem.
 * @property navn Navnet/etiketten som hører til bildet. Dette er svaret i quizen.
 * @property bildeUri URI som peker til bildet. Kan være:
 *                    - android.resource://pakkenavn/drawable/ressursnavn for innebygde bilder
 *                    - content://... for bilder valgt fra telefonens galleri
 * @property erInnebygd True hvis bildet er en innebygd ressurs, false hvis det er
 *                      lagt til av brukeren. Innebygde bilder lastes på nytt ved appstart.
 */
data class BildeOppforing(
    val id: Long,
    val navn: String,
    val bildeUri: Uri,
    val erInnebygd: Boolean = false
)
