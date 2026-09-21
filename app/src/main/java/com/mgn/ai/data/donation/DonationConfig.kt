package com.mgn.ai.data.donation

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.coroutines.resume

/**
 * Firebase Remote Config keys controlling in-app donations.
 *
 * - [RC_KEY_DONATIONS_JSON]: JSON document describing every donation network:
 *   {"networks":[{"id":"binance","label":"Binance","url":"https://...","enabled":true}]}
 *
 * To change the Binance destination remotely, edit the `donations_config`
 * value in the Firebase console (project mgn-ai) -> Remote Config -> publish.
 * To add a future network (Bitcoin, Ethereum, USDT, ...), append another
 * object with a new `id` to the `networks` array. No app update is needed.
 */
const val RC_KEY_DONATIONS_JSON = "donations_config"

/** Placeholder used until the real destination is supplied remotely. */
const val DONATION_URL_TO_BE_PROVIDED = "BINANCE_DONATION_URL_TO_BE_PROVIDED"

private val json = Json { ignoreUnknownKeys = true }

@Serializable
data class DonationNetwork(
    val id: String,
    val label: String,
    val url: String = "",
    val enabled: Boolean = true,
)

@Serializable
data class DonationsConfig(
    val networks: List<DonationNetwork> = listOf(
        DonationNetwork(
            id = "binance",
            label = "Binance",
            url = DONATION_URL_TO_BE_PROVIDED,
            enabled = true,
        )
    ),
)

val DEFAULT_DONATIONS_CONFIG = DonationsConfig()

fun DonationsConfig.enabledNetworks(): List<DonationNetwork> =
    networks.filter { it.enabled }

/** True when [network] points to a real destination instead of the placeholder. */
fun DonationNetwork.hasRealDestination(): Boolean =
    url.isNotBlank() && url != DONATION_URL_TO_BE_PROVIDED

class DonationRepository(
    private val remoteConfig: FirebaseRemoteConfig,
) {
    init {
        val settings = remoteConfigSettings {
            // Donation data changes rarely; avoid hammering the backend.
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(settings)
        remoteConfig.setDefaultsAsync(
            mapOf(RC_KEY_DONATIONS_JSON to json.encodeToString(DonationsConfig.serializer(), DEFAULT_DONATIONS_CONFIG))
        )
    }

    /**
     * Loads the donation configuration. Never throws and never blocks startup:
     * on fetch failure (offline, throttled, ...) the last activated values or
     * the built-in defaults are returned.
     */
    suspend fun load(): DonationsConfig {
        return try {
            suspendCancellableCoroutine { cont ->
                remoteConfig.fetchAndActivate()
                    .addOnCompleteListener { cont.resume(Unit) }
            }
            parse(remoteConfig.getString(RC_KEY_DONATIONS_JSON))
        } catch (e: Exception) {
            parse(remoteConfig.getString(RC_KEY_DONATIONS_JSON))
        }
    }

    private fun parse(raw: String): DonationsConfig {
        if (raw.isBlank()) return DEFAULT_DONATIONS_CONFIG
        return try {
            json.decodeFromString(DonationsConfig.serializer(), raw)
        } catch (e: Exception) {
            DEFAULT_DONATIONS_CONFIG
        }
    }
}
