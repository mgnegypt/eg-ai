package com.mgn.ai.ui.pages.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mgn.ai.R
import com.mgn.ai.data.donation.DEFAULT_DONATIONS_CONFIG
import com.mgn.ai.data.donation.DonationNetwork
import com.mgn.ai.data.donation.DonationRepository
import com.mgn.ai.data.donation.enabledNetworks
import com.mgn.ai.data.donation.hasRealDestination
import com.mgn.ai.ui.components.nav.BackButton
import com.mgn.ai.ui.components.ui.CardGroup
import com.mgn.ai.ui.theme.CustomColors
import com.mgn.ai.utils.openUrl
import com.composables.icons.lucide.ExternalLink
import com.composables.icons.lucide.Lucide
import me.rerere.hugeicons.HugeIcons
import me.rerere.hugeicons.stroke.Github
import me.rerere.hugeicons.stroke.InLove
import org.koin.compose.koinInject

@Composable
fun SettingDonatePage() {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            LargeFlexibleTopAppBar(
                title = {
                    Text(text = stringResource(R.string.donate_page_title))
                },
                navigationIcon = {
                    BackButton()
                },
                scrollBehavior = scrollBehavior,
                colors = CustomColors.topBarColors,
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = CustomColors.topBarColors.containerColor,
    ) { paddings ->
        Column(
            modifier = Modifier
                .padding(paddings)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.donate_page_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            DonateMethodsCardGroup()
            SupportCardGroup()
        }
    }
}

@Composable
private fun DonateMethodsCardGroup() {
    val context = LocalContext.current
    val repository = koinInject<DonationRepository>()
    val config by produceState(initialValue = DEFAULT_DONATIONS_CONFIG) {
        value = repository.load()
    }
    var pendingNetwork by remember { mutableStateOf<DonationNetwork?>(null) }
    var showComingSoon by remember { mutableStateOf(false) }

    CardGroup(
        modifier = Modifier.fillMaxWidth(),
        title = { Text(stringResource(R.string.donate_page_donation_methods)) },
    ) {
        val networks = config.enabledNetworks()
        if (networks.isEmpty()) {
            item(
                leadingContent = { Icon(HugeIcons.InLove, null) },
                headlineContent = { Text(stringResource(R.string.donate_page_coming_soon_title)) },
                supportingContent = { Text(stringResource(R.string.donate_page_coming_soon_text)) },
            )
        }
        networks.forEach { network ->
            item(
                onClick = {
                    if (network.hasRealDestination()) {
                        context.openUrl(network.url)
                    } else {
                        pendingNetwork = network
                        showComingSoon = true
                    }
                },
                leadingContent = { Icon(HugeIcons.InLove, null) },
                headlineContent = { Text(network.label) },
                supportingContent = { Text(stringResource(R.string.donate_page_tap_to_open)) },
                trailingContent = { Icon(Lucide.ExternalLink, null) },
            )
        }
    }

    if (showComingSoon) {
        AlertDialog(
            onDismissRequest = { showComingSoon = false },
            title = { Text(stringResource(R.string.donate_page_coming_soon_title)) },
            text = {
                Text(
                    stringResource(
                        R.string.donate_page_coming_soon_text_with_network,
                        pendingNetwork?.label ?: ""
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = { showComingSoon = false }) {
                    Text(stringResource(android.R.string.ok))
                }
            },
        )
    }
}

@Composable
private fun SupportCardGroup() {
    val context = LocalContext.current
    CardGroup(
        modifier = Modifier.fillMaxWidth(),
    ) {
        item(
            onClick = { context.openUrl("https://github.com/mgnegypt/eg-ai") },
            leadingContent = {
                Icon(
                    imageVector = HugeIcons.Github,
                    contentDescription = null,
                )
            },
            supportingContent = { Text("https://github.com/mgnegypt/eg-ai") },
            headlineContent = { Text("MGN AI") },
        )
    }
}
