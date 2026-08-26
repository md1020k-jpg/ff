package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.EquationSolverPreset
import com.example.model.NumericalMethod
import com.example.model.SolvedPoint
import com.example.model.SolverMode
import com.example.model.SolverPresets
import com.example.model.SolverResultState
import java.util.Locale
import kotlin.math.log10
import kotlin.math.pow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NumericalSolverCard(
    mode: SolverMode,
    fx: String,
    gx: String,
    method: NumericalMethod,
    domainMin: Double,
    domainMax: Double,
    tolerance: Double,
    solverResult: SolverResultState,
    plotXMin: Double,
    plotXMax: Double,
    onModeChange: (SolverMode) -> Unit,
    onFxChange: (String) -> Unit,
    onGxChange: (String) -> Unit,
    onMethodChange: (NumericalMethod) -> Unit,
    onDomainChange: (Double, Double) -> Unit,
    onToleranceChange: (Double) -> Unit,
    onSolve: () -> Unit,
    onApplyPreset: (EquationSolverPreset) -> Unit,
    onInspectRoot: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showAdvancedSettings by remember { mutableStateOf(false) }
    var showPresetsMenu by remember { mutableStateOf(false) }
    var showMethodMenu by remember { mutableStateOf(false) }

    val quickSymbols = listOf(
        "cosh(x)", "sinh(x)", "tanh(x)", "sech(x)", "coth(x)",
        "x", "x^2", "sqrt(x)", "exp(x)", "ln(x)",
        "+", "-", "*", "/", "^", "pi", "e"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Main Header Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Title and Preset Picker Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Calculate,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "Numerical Equation Solver",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Find roots f(x)=0 or intersections f(x)=g(x)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Presets Dropdown Button
                    Box {
                        OutlinedButton(
                            onClick = { showPresetsMenu = true },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("solver_presets_button")
                        ) {
                            Text("Presets", style = MaterialTheme.typography.labelSmall)
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = showPresetsMenu,
                            onDismissRequest = { showPresetsMenu = false }
                        ) {
                            SolverPresets.PRESETS.forEach { preset ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(
                                                text = preset.title,
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                            Text(
                                                text = preset.description,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    },
                                    onClick = {
                                        onApplyPreset(preset)
                                        showPresetsMenu = false
                                    }
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                    thickness = 0.5.dp
                )

                // Mode Selector: Roots vs Intersections
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SolverMode.values().forEachIndexed { index, solverMode ->
                        SegmentedButton(
                            selected = mode == solverMode,
                            onClick = { onModeChange(solverMode) },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = SolverMode.values().size
                            ),
                            icon = {
                                if (mode == solverMode) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = solverMode.displayName,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (mode == solverMode) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            modifier = Modifier.testTag("solver_mode_${solverMode.name.lowercase(Locale.ROOT)}")
                        )
                    }
                }

                // Equation Inputs
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // f(x) input
                    OutlinedTextField(
                        value = fx,
                        onValueChange = onFxChange,
                        label = {
                            Text(
                                text = if (mode == SolverMode.ROOT_FINDING) "Equation f(x) [= 0]" else "Function f(x)"
                            )
                        },
                        placeholder = {
                            Text(
                                text = if (mode == SolverMode.ROOT_FINDING) "e.g. cosh(x) - 3 or sinh(x) - 2*x" else "e.g. cosh(x)"
                            )
                        },
                        leadingIcon = {
                            Text(
                                text = "f(x) =",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        },
                        trailingIcon = {
                            if (fx.isNotEmpty()) {
                                IconButton(onClick = { onFxChange("") }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Ascii,
                            imeAction = if (mode == SolverMode.INTERSECTION) ImeAction.Next else ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { onSolve() }
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("solver_fx_input")
                    )

                    // g(x) input if in Intersection mode
                    AnimatedVisibility(
                        visible = mode == SolverMode.INTERSECTION,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        OutlinedTextField(
                            value = gx,
                            onValueChange = onGxChange,
                            label = { Text("Intersect with g(x)") },
                            placeholder = { Text("e.g. 2*x or 1 + 0.5*x^2 or cos(x)") },
                            leadingIcon = {
                                Text(
                                    text = "g(x) =",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                            },
                            trailingIcon = {
                                if (gx.isNotEmpty()) {
                                    IconButton(onClick = { onGxChange("") }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Clear",
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Ascii,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { onSolve() }
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("solver_gx_input")
                        )
                    }
                }

                // Quick Math Symbol Chips
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Quick Math Inserts:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        quickSymbols.forEach { sym ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                                border = androidx.compose.foundation.BorderStroke(
                                    0.5.dp,
                                    MaterialTheme.colorScheme.outlineVariant
                                ),
                                onClick = {
                                    val targetText = if (mode == SolverMode.INTERSECTION && gx.isEmpty()) {
                                        if (fx.isEmpty()) {
                                            onFxChange(sym)
                                        } else {
                                            onFxChange("$fx$sym")
                                        }
                                    } else {
                                        onFxChange("$fx$sym")
                                    }
                                }
                            ) {
                                Text(
                                    text = sym,
                                    fontFamily = FontFamily.Monospace,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                // Advanced Settings Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { showAdvancedSettings = !showAdvancedSettings }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Interval & Algorithm Settings",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Icon(
                        imageVector = if (showAdvancedSettings) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Advanced Settings Content
                AnimatedVisibility(
                    visible = showAdvancedSettings,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Method Selector
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Numerical Method:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )

                            Box {
                                OutlinedButton(
                                    onClick = { showMethodMenu = true },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = method.displayName.substringBefore("(").trim(),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }

                                DropdownMenu(
                                    expanded = showMethodMenu,
                                    onDismissRequest = { showMethodMenu = false }
                                ) {
                                    NumericalMethod.values().forEach { m ->
                                        DropdownMenuItem(
                                            text = {
                                                Column {
                                                    Text(
                                                        text = m.displayName,
                                                        fontWeight = if (m == method) FontWeight.Bold else FontWeight.Normal
                                                    )
                                                    Text(
                                                        text = m.shortDescription,
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                            },
                                            onClick = {
                                                onMethodChange(m)
                                                showMethodMenu = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        // Search Interval Domain [xmin, xmax]
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Search Interval [x_min, x_max]:",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )

                                TextButton(
                                    onClick = { onDomainChange(plotXMin, plotXMax) },
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Sync,
                                        contentDescription = null,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Sync with Graph Bounds",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 10.sp
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = String.format(Locale.US, "%.1f", domainMin),
                                    onValueChange = {
                                        val v = it.toDoubleOrNull()
                                        if (v != null) onDomainChange(v, domainMax)
                                    },
                                    label = { Text("x_min") },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = String.format(Locale.US, "%.1f", domainMax),
                                    onValueChange = {
                                        val v = it.toDoubleOrNull()
                                        if (v != null) onDomainChange(domainMin, v)
                                    },
                                    label = { Text("x_max") },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                            }
                        }

                        // Tolerance setting
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Convergence Tolerance (ε):",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = String.format(Locale.US, "%.1e", tolerance),
                                    fontFamily = FontFamily.Monospace,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            val tolExponent = -log10(tolerance).toFloat().coerceIn(4f, 10f)
                            Slider(
                                value = tolExponent,
                                onValueChange = { exp ->
                                    val newTol = 10.0.pow(-exp.toDouble())
                                    onToleranceChange(newTol)
                                },
                                valueRange = 4f..10f,
                                steps = 5,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                // Solve Action Button
                Button(
                    onClick = onSolve,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("solve_equation_button")
                ) {
                    if (solverResult is SolverResultState.Solving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Solving Numerically…", fontWeight = FontWeight.Bold)
                    } else {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (mode == SolverMode.ROOT_FINDING) "Find Equation Roots" else "Calculate Intersections",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Results Section
        when (solverResult) {
            is SolverResultState.Idle -> {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Functions,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = "Ready to Solve",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Enter an equation above (or select a preset) and tap 'Find Equation Roots' to execute numerical root brackets and Newton refinement.",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            is SolverResultState.Solving -> {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Executing numerical solver and bracketing…")
                    }
                }
            }

            is SolverResultState.Error -> {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Solver Error",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            text = solverResult.message,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }

            is SolverResultState.Success -> {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Summary Banner
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (solverResult.roots.isEmpty()) "No Roots Found in Domain" else "Found ${solverResult.roots.size} Root(s)",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Text(
                                    text = "Domain: [${String.format(Locale.US, "%.1f", solverResult.searchInterval.first)}, ${String.format(Locale.US, "%.1f", solverResult.searchInterval.second)}] • ε = ${String.format(Locale.US, "%.1e", solverResult.tolerance)} • ${solverResult.executionTimeMs} ms",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }

                    // Individual Root Result Cards
                    if (solverResult.roots.isEmpty()) {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "No real solutions found within [${String.format(Locale.US, "%.1f", solverResult.searchInterval.first)}, ${String.format(Locale.US, "%.1f", solverResult.searchInterval.second)}].",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Suggestions:\n• Widen the search interval in settings (e.g. [-10, 10]).\n• Verify the equation has real roots (e.g., cosh(x) >= 1 for all real x, so cosh(x) = 0 has no real solutions).\n• Switch numerical method to Hybrid Brent-Newton.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        solverResult.roots.forEachIndexed { index, point ->
                            SolvedRootItemCard(
                                index = index + 1,
                                point = point,
                                mode = mode,
                                onInspect = { onInspectRoot(point.x) },
                                onCopy = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText(
                                        "Solved Root Coordinate",
                                        "x = ${point.formattedX}, y = ${point.formattedY}"
                                    )
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Copied (${point.formattedX}, ${point.formattedY})", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SolvedRootItemCard(
    index: Int,
    point: SolvedPoint,
    mode: SolverMode,
    onInspect: () -> Unit,
    onCopy: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedSteps by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("solved_root_card_$index")
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Top Row: Root badge, Coordinates, Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(26.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "$index",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }

                    Column {
                        SelectionContainer {
                            Text(
                                text = "x* = ${point.formattedX}",
                                fontFamily = FontFamily.Monospace,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        if (mode == SolverMode.INTERSECTION) {
                            SelectionContainer {
                                Text(
                                    text = "y* = ${point.formattedY}",
                                    fontFamily = FontFamily.Monospace,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            }
                        }
                    }
                }

                // Actions: Inspect on Graph & Copy
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = onInspect,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("inspect_root_button_$index")
                    ) {
                        Icon(
                            imageVector = Icons.Default.TouchApp,
                            contentDescription = "Inspect on Graph",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onCopy,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy coordinates",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Convergence stats bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainerHighest,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Residual |f(x*)|: ${point.formattedResidual}",
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Iter: ${point.iterationsCount} steps (${point.methodUsed.name.take(6)})",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Expandable Iteration Steps Details
            if (point.steps.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { expandedSteps = !expandedSteps }
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (expandedSteps) "Hide Iteration Steps" else "Show Convergence History (${point.steps.size} steps)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        imageVector = if (expandedSteps) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }

                AnimatedVisibility(
                    visible = expandedSteps,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Step", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.weight(0.8f))
                            Text("x_n", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.weight(1.6f))
                            Text("f(x_n)", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.weight(1.6f))
                            Text("Δx", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.weight(1.2f))
                        }
                        HorizontalDivider(thickness = 0.5.dp)

                        point.steps.take(15).forEach { step ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("#${step.iteration}", fontFamily = FontFamily.Monospace, fontSize = 10.sp, modifier = Modifier.weight(0.8f))
                                Text(String.format(Locale.US, "%.5f", step.xVal), fontFamily = FontFamily.Monospace, fontSize = 10.sp, modifier = Modifier.weight(1.6f))
                                Text(String.format(Locale.US, "%.2e", step.fxVal), fontFamily = FontFamily.Monospace, fontSize = 10.sp, modifier = Modifier.weight(1.6f))
                                Text(String.format(Locale.US, "%.2e", step.deltaX), fontFamily = FontFamily.Monospace, fontSize = 10.sp, modifier = Modifier.weight(1.2f))
                            }
                        }
                        if (point.steps.size > 15) {
                            Text(
                                text = "… (${point.steps.size - 15} more converged steps)",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 9.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
