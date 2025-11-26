package com.mrboomdev.uust.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mrboomdev.uust.data.University
import com.mrboomdev.uust.ui.UustTheme
import com.mrboomdev.uust.ui.components.OutlinedTextPicker
import com.mrboomdev.uust.ui.components.TextPickerItem
import com.mrboomdev.uust.utils.permissions.Permission
import com.mrboomdev.uust.utils.permissions.PermissionRequestResult
import com.mrboomdev.uust.utils.permissions.rememberPermissionController
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import uust.composeapp.generated.resources.Res
import uust.composeapp.generated.resources.ic_close

object LocationDialogDefaults {
    val LIVE_LOCATION = -1.0 to -1.0
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDialog(
    onDismissRequest: () -> Unit,
    onSelected: (Pair<Double, Double>) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val permissionsController = rememberPermissionController()
    var error by rememberSaveable { mutableStateOf<String?>(null) }

    var selectedUniversity by remember { mutableStateOf<University?>(null) }
    var selectedCampus by remember(selectedUniversity) { mutableStateOf<Int?>(null) }
    var selectedFloor by remember(selectedCampus) { mutableStateOf<Int?>(null) }
    var selectedRoom by remember(selectedFloor) { mutableStateOf<Int?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        dragHandle = {}
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),

            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f),

                style = MaterialTheme.typography.titleLarge,
                fontFamily = UustTheme.fonts.golos,
                text = "Выбор местоположения"
            )

            IconButton(
                modifier = Modifier.padding(end = 8.dp),
                onClick = onDismissRequest
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(Res.drawable.ic_close),
                    contentDescription = null
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                var isExpanded by rememberSaveable { mutableStateOf(false) }

                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = UustTheme.fonts.golos,
                    color = MaterialTheme.colorScheme.secondary,
                    text = "Адрес"
                )

                OutlinedTextPicker(
                    modifier = Modifier.fillMaxWidth(),
                    text = selectedUniversity?.address ?: "-",
                    isExpanded = isExpanded,
                    onExpand = { isExpanded = it }
                ) {
                    University.entries.forEach { place ->
                        TextPickerItem(
                            text = {
                                Text(
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontFamily = UustTheme.fonts.golos,
                                    text = place.address
                                )
                            },

                            onClick = {
                                selectedUniversity = place
                                isExpanded = false
                            }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                var isExpanded by rememberSaveable { mutableStateOf(false) }

                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = UustTheme.fonts.golos,
                    color = MaterialTheme.colorScheme.secondary,
                    text = "Корпус"
                )

                OutlinedTextPicker(
                    modifier = Modifier.fillMaxWidth(),
                    text = selectedCampus?.let { it + 1 }?.toString() ?: "-",
                    isExpanded = isExpanded,
                    onExpand = { isExpanded = it }
                ) {
                    selectedUniversity?.campuses?.forEachIndexed { index, _ ->
                        TextPickerItem(
                            text = {
                                Text(
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontFamily = UustTheme.fonts.golos,
                                    text = (index + 1).toString()
                                )
                            },

                            onClick = {
                                selectedCampus = index
                                isExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                var isExpanded by rememberSaveable { mutableStateOf(false) }

                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = UustTheme.fonts.golos,
                    color = MaterialTheme.colorScheme.secondary,
                    text = "Этаж"
                )

                OutlinedTextPicker(
                    modifier = Modifier.fillMaxWidth(),
                    text = selectedFloor?.let { it + 1 }?.toString() ?: "-",
                    isExpanded = isExpanded,
                    onExpand = { isExpanded = it }
                ) {
                    selectedCampus?.also { selectedCampus ->
                        selectedUniversity?.campuses?.get(selectedCampus)?.floors?.forEachIndexed { index, _ ->
                            TextPickerItem(
                                text = {
                                    Text(
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontFamily = UustTheme.fonts.golos,
                                        text = (index + 1).toString()
                                    )
                                },

                                onClick = {
                                    selectedFloor = index
                                    isExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                var isExpanded by rememberSaveable { mutableStateOf(false) }

                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = UustTheme.fonts.golos,
                    color = MaterialTheme.colorScheme.secondary,
                    text = "Аудитория"
                )

                OutlinedTextPicker(
                    modifier = Modifier.fillMaxWidth(),
                    text = selectedRoom?.let { "${(selectedCampus ?: 0) + 1}${(selectedFloor ?: 0) + 1}${it + 1}" }
                        ?: "-",
                    isExpanded = isExpanded,
                    onExpand = { isExpanded = it }
                ) {
                    selectedCampus?.also { selectedCampus ->
                        selectedFloor?.also { selectedFloor ->
                            selectedUniversity?.campuses?.get(selectedCampus)?.floors?.get(selectedFloor)?.roomsCount?.let { roomsCount ->
                                repeat(roomsCount) { room ->
                                    TextPickerItem(
                                        text = {
                                            Text(
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontFamily = UustTheme.fonts.golos,
                                                text = "${selectedCampus + 1}${selectedFloor + 1}${room + 1}"
                                            )
                                        },

                                        onClick = {
                                            selectedRoom = room
                                            isExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 4.dp),

            contentPadding = PaddingValues(16.dp),

            onClick = {
                coroutineScope.launch {
                    when(permissionsController.requestPermission(
                        Permission.LOCATION
                    )) {
                        PermissionRequestResult.GRANTED -> {
                            onSelected(LocationDialogDefaults.LIVE_LOCATION)
                            onDismissRequest()
                        }

                        PermissionRequestResult.DENIED -> {
                            error =
                                "Отказано в разрешении. Мы не можем предоставить вам требуемый функционал без необходимых разрешений!"
                        }

                        PermissionRequestResult.FUCKED -> {
                            error =
                                "Для получения разрешения необходимо перейти в настройки приложения и дать его в пункте разрешений."
                        }

                        PermissionRequestResult.CANCELLED -> {}
                    }
                }
            }
        ) {
            Text(
                style = MaterialTheme.typography.labelMedium,
                fontFamily = UustTheme.fonts.golos,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                text = "Использовать текущую геолокацию"
            )
        }

        FilledTonalButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),

            contentPadding = PaddingValues(16.dp),
            enabled = selectedUniversity != null,

            onClick = {
                // TODO: Get selected place address and navigate to it
            }
        ) {
            Text(
                fontFamily = UustTheme.fonts.golos,
                text = "Перейти"
            )
        }
    }

    error?.also { theError ->
        AlertDialog(
            onDismissRequest = { error = null },
            title = { Text("Разрешение не получено") },
            text = { Text(theError) },

            dismissButton = {
                TextButton(
                    onClick = {
                        permissionsController.requestPermissionFucked(Permission.LOCATION)
                    }
                ) {
                    Text("Открыть настройки приложения")
                }
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        error = null
                    }
                ) {
                    Text("Ок")
                }
            }
        )
    }
}