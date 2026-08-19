package chat.stoat.composables.screens.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import chat.stoat.R
import chat.stoat.persistence.KVStorage
import chat.stoat.settings.ServerConfigRepository
import kotlinx.coroutines.launch

@Composable
fun ServerSettingsPanel(
    kvStorage: KVStorage,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    var serverUrl by remember { mutableStateOf(ServerConfigRepository.currentOrigin()) }
    var error by remember { mutableStateOf<String?>(null) }
    var savedMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(kvStorage) {
        ServerConfigRepository.load(kvStorage)
        serverUrl = ServerConfigRepository.currentOrigin()
    }

    Column(modifier = modifier.fillMaxWidth()) {
        TextButton(onClick = { expanded = !expanded }) {
            Text(
                text = if (expanded) {
                    stringResource(R.string.server_settings_hide)
                } else {
                    stringResource(R.string.server_settings_show)
                }
            )
        }

        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = serverUrl,
                    onValueChange = {
                        serverUrl = it
                        error = null
                        savedMessage = null
                    },
                    label = { Text(stringResource(R.string.server_settings_url_label)) },
                    placeholder = { Text(ServerConfigRepository.DEFAULT_ORIGIN) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        scope.launch {
                            val result = ServerConfigRepository.save(kvStorage, serverUrl)
                            result.onSuccess { saved ->
                                serverUrl = saved
                                savedMessage = "saved"
                                error = null
                            }.onFailure { throwable ->
                                savedMessage = null
                                error = throwable.message ?: "invalid"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(stringResource(R.string.server_settings_save))
                }

                error?.let {
                    Text(
                        text = if (it == "invalid") {
                            stringResource(R.string.server_settings_invalid)
                        } else {
                            it
                        },
                        color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }

                savedMessage?.let {
                    Text(
                        text = stringResource(R.string.server_settings_saved),
                        color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }

                Text(
                    text = stringResource(R.string.server_settings_hint),
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }
}
