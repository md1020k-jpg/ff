package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CalculusPointDiagnostics
import com.example.model.ConcavityType
import com.example.model.DifferentiationMethod
import com.example.model.DifferentiationPreset
import com.example.model.Expression
import com.example.model.NumericalDifferentiationEngine
import com.example.model.NumericalDifferentiationPresets
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun NumericalDifferentiationCard(
    functionExpr: String,
    parsedExpression: Expression?,
    parseError: String?,
    stepSizeH: Double,
    method: DifferentiationMethod,
    plotFunction: Boolean,
    plotFirstDerivative: Boolean,
    plotSecondDerivative: Boolean,
    showTangentLine: Boolean,
    showNormalLine: Boolean,
    scrubX: Double,
    paramA: Double,
    shiftC: Double,
    plotXMin: Double,
    plotXMax: Double,
    onFunctionExprChange: (String) -> Unit,
    onStepSizeHChange: (Double) -> Unit,
    onMethodChange: (DifferentiationMethod) -> Unit,
    onTogglePlotFunction: (Boolean) -> Unit,
    onTogglePlotFirstDerivative: (Boolean) -> Unit,
    onTogglePlotSecondDerivative: (Boolean) -> Unit,
    onToggleShowTangentLine: (Boolean) -> Unit,
    onToggleShowNormalLine: (Boolean) -> Unit,
    onScrubXChange: (Double) -> Unit,
    onApplyPreset: (DifferentiationPreset) -> Unit,
    modifier: Modifier = Modifier
) {
    var methodExpanded by remember { mutableStateOf(false) }

    val diagnostics = remember(parsedExpression, scrubX, stepSizeH, method, paramA, shiftC) {
        if (parsedExpression != null) {
            NumericalDifferentiationEngine.computeCalculusDiagnostics(
                expr = parsedExpression,
                x0 = scrubX,
                h = stepSizeH,
                method = method,
                paramA = paramA,
                shiftC = shiftC
            )
        } else null
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("numerical_differentiation_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timeline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Numerical Differentiation",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Calculate & plot derivatives f'(x) and f''(x)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = method.orderOfAccuracy,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Function Input Field f(x)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Input Function f(x)",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedTextField(
                    value = functionExpr,
                    onValueChange = onFunctionExprChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("diff_function_input"),
                    leadingIcon = {
                        Text(
                            text = "f(x) =",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    },
                    trailingIcon = {
                        if (functionExpr.isNotEmpty()) {
                            IconButton(
                                onClick = { onFunctionExprChange("") },
                                modifier = Modifier.testTag("clear_diff_input_button")
                            ) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear input")
                            }
                        }
                    },
                    isError = parseError != null,
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                if (parseError != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Icon(
                            Icons.Default.ErrorOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = parseError,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            // Quick Math Symbol Insertion Chips
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Quick Math Inserts",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(
                        "cosh(x)" to "cosh(x)",
                        "sinh(x)" to "sinh(x)",
                        "tanh(x)" to "tanh(x)",
                        "sech(x)" to "sech(x)",
                        "asinh(x)" to "asinh(x)",
                        "x²" to "x^2",
                        "e⁻ˣ²" to "exp(-x^2)",
                        "√(x²+1)" to "sqrt(x^2+1)",
                        "ln(x)" to "ln(x)"
                    ).forEach { (label, insertValue) ->
                        Surface(
                            onClick = {
                                if (functionExpr.isEmpty() || functionExpr == "cosh(x)") {
                                    onFunctionExprChange(insertValue)
                                } else {
                                    onFunctionExprChange("$functionExpr * $insertValue")
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 1.dp,
                            modifier = Modifier.testTag("quick_insert_$label")
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelMedium,
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Canvas Plotting Toggles & Styling
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Canvas Graphing Layers",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = plotFunction,
                        onClick = { onTogglePlotFunction(!plotFunction) },
                        label = { Text("f(x) Function Curve") },
                        leadingIcon = {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF6366F1)) // Indigo
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF6366F1).copy(alpha = 0.18f),
                            selectedLabelColor = Color(0xFF4338CA)
                        ),
                        modifier = Modifier.testTag("toggle_plot_fx")
                    )

                    FilterChip(
                        selected = plotFirstDerivative,
                        onClick = { onTogglePlotFirstDerivative(!plotFirstDerivative) },
                        label = { Text("f'(x) 1st Derivative") },
                        leadingIcon = {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF06B6D4)) // Cyan
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF06B6D4).copy(alpha = 0.18f),
                            selectedLabelColor = Color(0xFF0E7490)
                        ),
                        modifier = Modifier.testTag("toggle_plot_fprime")
                    )

                    FilterChip(
                        selected = plotSecondDerivative,
                        onClick = { onTogglePlotSecondDerivative(!plotSecondDerivative) },
                        label = { Text("f''(x) 2nd Derivative (Concavity)") },
                        leadingIcon = {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFF59E0B)) // Amber
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFF59E0B).copy(alpha = 0.18f),
                            selectedLabelColor = Color(0xFFB45309)
                        ),
                        modifier = Modifier.testTag("toggle_plot_fdoubleprime")
                    )

                    FilterChip(
                        selected = showTangentLine,
                        onClick = { onToggleShowTangentLine(!showTangentLine) },
                        label = { Text("Tangent Line at x₀") },
                        leadingIcon = {
                            Icon(Icons.Default.TrendingUp, contentDescription = null, modifier = Modifier.size(14.dp))
                        },
                        modifier = Modifier.testTag("toggle_show_tangent_line")
                    )

                    FilterChip(
                        selected = showNormalLine,
                        onClick = { onToggleShowNormalLine(!showNormalLine) },
                        label = { Text("Normal Line (Orthogonal)") },
                        modifier = Modifier.testTag("toggle_show_normal_line")
                    )
                }
            }

            // Numerical Method & Finite Difference Step Size (h)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Method Selector
                ExposedDropdownMenuBox(
                    expanded = methodExpanded,
                    onExpandedChange = { methodExpanded = it },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = method.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Finite Difference Stencil") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = methodExpanded) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .testTag("diff_method_dropdown")
                    )
                    ExposedDropdownMenu(
                        expanded = methodExpanded,
                        onDismissRequest = { methodExpanded = false }
                    ) {
                        DifferentiationMethod.values().forEach { m ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(m.displayName, fontWeight = FontWeight.SemiBold)
                                        Text(
                                            m.orderOfAccuracy + " • " + m.formulaDescription,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                },
                                onClick = {
                                    onMethodChange(m)
                                    methodExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Step Size (h) Control Slider
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Finite Difference Step Size (h)",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "h = ${String.format(Locale.US, "%.5f", stepSizeH)}",
                        style = MaterialTheme.typography.labelMedium,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Slider(
                    value = stepSizeH.toFloat(),
                    onValueChange = { onStepSizeHChange(it.toDouble()) },
                    valueRange = 0.0001f..0.05f,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("diff_step_size_slider")
                )
                Text(
                    text = "Smaller h reduces truncation error ($method), balancing floating-point roundoff.",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Real-time Calculus Point Inspector at Scrub Coordinate x0
            if (diagnostics != null) {
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🎯 Live Derivative Inspector (x₀ = ${diagnostics.formattedX0})",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(diagnostics.concavity.badgeColorHex).copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = diagnostics.concavity.displayName,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(diagnostics.concavity.badgeColorHex),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        // Inspection Slider for x0
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Inspect X Position",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "x₀ = ${diagnostics.formattedX0}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Slider(
                                value = scrubX.toFloat().coerceIn(plotXMin.toFloat(), plotXMax.toFloat()),
                                onValueChange = { onScrubXChange(it.toDouble()) },
                                valueRange = plotXMin.toFloat()..plotXMax.toFloat(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("diff_scrub_x_slider")
                            )
                        }

                        // Grid of Calculus Properties
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Function Value f(x0)
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "f(x₀)",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = diagnostics.formattedFx0,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF6366F1)
                                    )
                                }
                            }

                            // 1st Derivative f'(x0) (Slope)
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "f'(x₀) [Slope / Velocity]",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "${diagnostics.formattedFPrime0} (${diagnostics.formattedSlopeAngle})",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF06B6D4)
                                    )
                                }
                            }

                            // 2nd Derivative f''(x0) (Curvature)
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "f''(x₀) [Concavity]",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = diagnostics.formattedFDoublePrime0,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFF59E0B)
                                    )
                                }
                            }
                        }

                        // Tangent and Normal Line Equations
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Tangent Line at x₀:",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = diagnostics.tangentEquation,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Normal Line at x₀:",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = diagnostics.normalEquation,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontFamily = FontFamily.Monospace,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Radius of Curvature (R):",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = diagnostics.formattedRadiusOfCurvature,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Differentiation Presets Carousel
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Classic Hyperbolic Differentiation Presets",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    NumericalDifferentiationPresets.list.forEach { preset ->
                        Surface(
                            onClick = { onApplyPreset(preset) },
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 2.dp,
                            modifier = Modifier.testTag("diff_preset_${preset.title}")
                        ) {
                            Text(
                                text = preset.title,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
