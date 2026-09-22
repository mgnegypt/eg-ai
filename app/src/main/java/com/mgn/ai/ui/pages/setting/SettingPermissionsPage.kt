package com.mgn.ai.ui.pages.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.flow.first
import me.rerere.hugeicons.HugeIcons
import me.rerere.hugeicons.stroke.Download01
import me.rerere.hugeicons.stroke.Folder01
import me.rerere.hugeicons.stroke.Notification01
import me.rerere.hugeicons.stroke.Zap
import com.mgn.ai.R
import com.mgn.ai.data.datastore.SettingsStore
import com.mgn.ai.ui.components.nav.BackButton
import com.mgn.ai.ui.components.ui.CardGroup
import com.mgn.ai.ui.theme.CustomColors
import com.mgn.ai.utils.SystemPermissions
import com.mgn.ai.utils.plus
import org.koin.compose.koinInject

/**
 */
@Composable
fun SettingPermissionsPage() {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val settingsStore: SettingsStore = koinInject()

    var refreshKey by remember { mutableStateOf(0) }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) refreshKey++
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val notificationEnabled = remember(refreshKey) {
        SystemPermissions.isNotificationEnabled(context)
    }
    val batteryExempt = remember(refreshKey) {
        SystemPermissions.isIgnoringBatteryOptimizations(context)
    }
    val storageEnabled = remember(refreshKey) {
        SystemPermissions.hasAllFilesAccess()
    }
    val installUnknownAppsEnabled = remember(refreshKey) {
        SystemPermissions.canInstallUnknownApps(context)
    }
    LaunchedEffect(batteryExempt) {
        if (batteryExempt != settingsStore.settingsFlowRaw.first().keepAwakeEnabled) {
            settingsStore.update { it.copy(keepAwakeEnabled = batteryExempt) }
        }
    }
    val backgroundEnabled = batteryExempt && notificationEnabled

    Scaffold(
        topBar = {
            LargeFlexibleTopAppBar(
                title = { Text(stringResource(R.string.setting_permissions_title)) },
                navigationIcon = { BackButton() },
                scrollBehavior = scrollBehavior,
                colors = CustomColors.topBarColors,
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = CustomColors.topBarColors.containerColor,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding + PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                CardGroup(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    title = { Text(stringResource(R.string.setting_permissions_title)) },
                ) {
                    item(
                        leadingContent = { Icon(HugeIcons.Notification01, null) },
                        headlineContent = {
                            Text(stringResource(R.string.setting_permissions_notification))
                        },
                        supportingContent = {
                            Text(stringResource(R.string.setting_permissions_notification_desc))
                        },
                        trailingContent = {
                            PermissionBadge(enabled = notificationEnabled)
                        },
                        onClick = {
                            SystemPermissions.openSettings(
                                context,
                                SystemPermissions.notificationSettingsIntent(context),
                            )
                        },
                    )
                    item(
                        leadingContent = { Icon(HugeIcons.Zap, null) },
                        headlineContent = {
                            Text(stringResource(R.string.setting_permissions_battery_optimization))
                        },
                        supportingContent = {
                            Text(stringResource(R.string.setting_permissions_battery_optimization_desc))
                        },
                        trailingContent = {
                            PermissionBadge(enabled = batteryExempt)
                        },
                        onClick = {
                            SystemPermissions.openSettings(
                                context,
                                SystemPermissions.batteryOptimizationIntent(context),
                            )
                        },
                    )
                    item(
                        leadingContent = { Icon(HugeIcons.Zap, null) },
                        headlineContent = {
                            Text(stringResource(R.string.setting_permissions_background))
                        },
                        supportingContent = {
                            Text(
                                text = stringResource(R.string.setting_permissions_background_desc),
                                maxLines = 2,
                            )
                        },
                        trailingContent = {
                            PermissionBadge(enabled = backgroundEnabled)
                        },
                        onClick = {
                            SystemPermissions.openSettings(
                                context,
                                SystemPermissions.batteryOptimizationIntent(context),
                            )
                        },
                    )
                    item(
                        leadingContent = { Icon(HugeIcons.Folder01, null) },
                        headlineContent = {
                            Text(stringResource(R.string.setting_permissions_storage))
                        },
                        supportingContent = {
                            Text(stringResource(R.string.setting_permissions_storage_desc))
                        },
                        trailingContent = {
                            PermissionBadge(enabled = storageEnabled)
                        },
                        onClick = {
                            SystemPermissions.openSettings(
                                context,
                                SystemPermissions.allFilesAccessIntent(context),
                            )
                        },
                    )
                    item(
                        leadingContent = { Icon(HugeIcons.Download01, null) },
                        headlineContent = {
                            Text(stringResource(R.string.setting_permissions_install_unknown_apps))
                        },
                        supportingContent = {
                            Text(stringResource(R.string.setting_permissions_install_unknown_apps_desc))
                        },
                        trailingContent = {
                            PermissionBadge(enabled = installUnknownAppsEnabled)
                        },
                        onClick = {
                            SystemPermissions.openSettings(
                                context,
                                SystemPermissions.installUnknownAppsIntent(context),
                            )
                        },
                    )
                }
            }

            item {
                Text(
                    text = stringResource(R.string.setting_permissions_footer),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 24.dp),
                )
            }
        }
    }
}

@Composable
private fun PermissionBadge(enabled: Boolean) {
    val container = if (enabled) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.errorContainer
    }
    val content = if (enabled) {
        MaterialTheme.colorScheme.onSecondaryContainer
    } else {
        MaterialTheme.colorScheme.onErrorContainer
    }
    Text(
        text = stringResource(
            if (enabled) R.string.setting_permissions_enabled else R.string.setting_permissions_disabled,
        ),
        style = MaterialTheme.typography.labelSmall,
        color = content,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .size(
                width = 68.dp,
                height = 24.dp,
            )
            .background(container, RoundedCornerShape(12.dp))
            .padding(horizontal = 6.dp, vertical = 3.dp),
    )
}
