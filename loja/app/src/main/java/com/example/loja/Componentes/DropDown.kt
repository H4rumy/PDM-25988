package com.example.loja.Componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.loja.classes.User

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MultiSelectDropdown(
    usuarios: List<User>,
    selecionados: Set<String>,
    onSelectionChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Pesquisar users") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Orange,
                focusedLabelColor = Orange,
                cursorColor = Orange,
                unfocusedBorderColor = Color(0xFFE0E0E0)
            ),
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        if (expanded) Icons.Default.KeyboardArrowUp
                        else Icons.Default.KeyboardArrowDown,
                        "Expandir",
                        tint = if (expanded) Orange else Color(0xFF666666)
                    )
                }
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.9f) // Ajustado para corresponder ao TextField
                .heightIn(max = 250.dp) // Altura máxima reduzida
                .background(Color.White)
        ) {
            val filteredUsers = usuarios
                .filter { it.email.contains(searchText, ignoreCase = true) }
                .take(10) // Limita a 10 resultados para melhor performance

            if (filteredUsers.isEmpty()) {
                DropdownMenuItem(
                    text = {
                        Text(
                            "Nenhum user encontrado",
                            color = Color(0xFF666666)
                        )
                    },
                    onClick = { },
                    enabled = false
                )
            }

            filteredUsers.forEach { usuario ->
                DropdownMenuItem(
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                usuario.email,
                                color = Color(0xFF333333)
                            )
                            if (selecionados.contains(usuario.uid)) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Selecionado",
                                    tint = Orange,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    },
                    onClick = {
                        onSelectionChange(usuario.uid)
                        // Não fecha o dropdown ao selecionar
                        searchText = "" // Limpa a pesquisa após seleção
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = Color(0xFF333333)
                    )
                )
            }
        }

        // Chips dos selecionados
        if (selecionados.isNotEmpty()) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                selecionados.forEach { userId ->
                    usuarios.find { it.uid == userId }?.let { usuario ->
                        AssistChip(
                            onClick = { onSelectionChange(userId) },
                            label = { Text(usuario.email) },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Remover",
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = Orange.copy(alpha = 0.1f),
                                labelColor = Orange,
                                trailingIconContentColor = Orange
                            )
                        )
                    }
                }
            }
        }
    }
}