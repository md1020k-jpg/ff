package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ChatMessage
import com.example.data.GeminiClient
import com.example.data.GeminiModelChoice
import com.example.data.MessageSender
import com.example.model.CablePreset
import com.example.model.CatenaryCalculation
import com.example.model.CatenaryCurvePoint
import com.example.model.GraphBounds
import com.example.model.GraphPreset
import com.example.model.HyperbolicFunc
import com.example.model.ParabolaMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.cosh
import kotlin.math.sinh

enum class AppViewTab(val title: String) {
    PLOT("Plot"),
    INSPECTOR("Inspector"),
    DIFFERENTIATION("Derivatives"),
    PARABOLA("Parabola"),
    ENGINEERING("Cable Eng"),
    PHYSICS("Physics"),
    IDENTITIES("Identities"),
    NUMERICAL_SOLVER("Solver"),
    AI_CHAT("Gemini AI")
}

enum class SidebarSectionTab(val title: String) {
    POINT_INSPECTOR("Point Inspector"),
    DIFFERENTIATION("Numerical Differentiation"),
    NUMERICAL_SOLVER("Numerical Solver"),
    CATENARY_SIMULATOR("Catenary Physics Simulator"),
    CALCULUS_REFERENCE("Calculus Reference"),
    GEMINI_AI("Gemini Chat & Search")
}

data class HyperbolicUiState(
    val activeFunctions: Set<HyperbolicFunc> = GraphPreset.SHOW_ALL.defaultFunctions,
    val bounds: GraphBounds = GraphPreset.SHOW_ALL.bounds,
    val spanL: Double = 6.0,   // Tower Span (L)
    val paramA: Double = 2.0,  // Parameter / Amplitude (A)
    val shiftC: Double = 0.0,  // Horizontal Shift (c)
    val scrubX: Double = 0.0,  // Default inspect coordinate
    val selectedPreset: GraphPreset = GraphPreset.SHOW_ALL,
    val selectedTab: AppViewTab = AppViewTab.PLOT,
    val selectedSidebarTab: SidebarSectionTab = SidebarSectionTab.POINT_INSPECTOR,
    val showGrid: Boolean = true,
    val showAsymptotes: Boolean = true,
    val showYEqualsX: Boolean = false,
    val showTangentLine: Boolean = false,
    val showParabolaComparison: Boolean = false,
    val parabolaMode: ParabolaMode = ParabolaMode.STANDARD_X_SQUARED,
    val morphBlend: Float = 1.0f, // 0.0 = exact cosh(x), 1.0 = target parabola
    val isAutoMorphing: Boolean = false,
    val isPanZoomMode: Boolean = false,
    val isHapticsEnabled: Boolean = true,
    val showTheoryDialog: Boolean = false,
    val isDarkTheme: Boolean = false,
    // Numerical Differentiation State
    val isDiffToolActive: Boolean = false,
    val diffFunctionExpr: String = "cosh(x)",
    val diffParsedExpression: com.example.model.Expression? = try { com.example.model.Expression("cosh(x)") } catch (_: Exception) { null },
    val diffParseError: String? = null,
    val diffStepSizeH: Double = 0.001,
    val diffMethod: com.example.model.DifferentiationMethod = com.example.model.DifferentiationMethod.FIVE_POINT_CENTRAL,
    val plotDiffFunction: Boolean = true,
    val plotFirstDerivative: Boolean = true,
    val plotSecondDerivative: Boolean = false,
    val showDiffTangentLine: Boolean = true,
    val showDiffNormalLine: Boolean = false,
    // Numerical Equation & Root Solver State
    val solverMode: com.example.model.SolverMode = com.example.model.SolverMode.ROOT_FINDING,
    val solverFx: String = "cosh(x) - 3",
    val solverGx: String = "2*x",
    val solverMethod: com.example.model.NumericalMethod = com.example.model.NumericalMethod.HYBRID_BRENT_NEWTON,
    val solverDomainMin: Double = -5.0,
    val solverDomainMax: Double = 5.0,
    val solverTolerance: Double = 1e-7,
    val solverResult: com.example.model.SolverResultState = com.example.model.SolverResultState.Idle,
    // Physics Catenary Simulation State (y = a * cosh(x / a) where a = T₀ / (μ * g))
    val catenaryCalculation: CatenaryCalculation = CatenaryCalculation(
        horizontalTensionN = 25000.0,
        spanM = 200.0,
        linearMassDensityKgPerM = 1.5
    ),
    val selectedCablePreset: CablePreset? = CablePreset.HIGH_VOLTAGE_POWER_LINE,
    // Gemini Chatbot & Search Grounding State
    val chatMessages: List<ChatMessage> = listOf(
        ChatMessage(
            sender = MessageSender.AI,
            text = "Welcome to Hyperbolic & Catenary AI Studio! I am your mathematical reasoning, calculus proof, and catenary physics specialist.\n\nAsk me any question about hyperbolic identities, derivatives, definite integrals, or use real-time Google Search Grounding to explore architectural structures like the Gateway Arch and suspension bridges.",
            modelName = "Gemini 3.5 Flash",
            isSearchGrounded = false
        )
    ),
    val isGeneratingChatResponse: Boolean = false,
    val selectedGeminiModel: GeminiModelChoice = GeminiModelChoice.FLASH_SEARCH,
    val isSearchGroundingEnabled: Boolean = true
) {
    // S = 2 * A * sinh(L / (2 * A))
    val arcLength: Double get() = 2.0 * paramA * sinh(spanL / (2.0 * paramA))
    
    // h = A * (cosh(L / (2 * A)) - 1)
    val maxSag: Double get() = paramA * (cosh(spanL / (2.0 * paramA)) - 1.0)
    
    // Slack % = ((S - L) / L) * 100
    val slackPercent: Double get() = ((arcLength - spanL) / spanL) * 100.0
    
    // Tower coordinate properties
    val towerLeftX: Double get() = -spanL / 2.0 + shiftC
    val towerRightX: Double get() = spanL / 2.0 + shiftC
    val towerY: Double get() = paramA * cosh(spanL / (2.0 * paramA))
}

class HyperbolicViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HyperbolicUiState())
    val uiState: StateFlow<HyperbolicUiState> = _uiState.asStateFlow()

    fun toggleFunction(func: HyperbolicFunc) {
        _uiState.update { current ->
            val updated = if (current.activeFunctions.contains(func)) {
                if (current.activeFunctions.size > 1) current.activeFunctions - func else current.activeFunctions
            } else {
                current.activeFunctions + func
            }
            current.copy(activeFunctions = updated)
        }
    }

    fun setScrubX(x: Double?) {
        if (x != null) {
            _uiState.update { it.copy(scrubX = x) }
        }
    }

    fun updateBounds(newBounds: GraphBounds) {
        _uiState.update { it.copy(bounds = newBounds) }
    }

    fun selectPreset(preset: GraphPreset) {
        _uiState.update {
            it.copy(
                selectedPreset = preset,
                bounds = preset.bounds,
                activeFunctions = preset.defaultFunctions,
                scrubX = 0.0
            )
        }
    }

    fun resetBounds() {
        selectPreset(GraphPreset.SHOW_ALL)
    }

    fun setSpanL(newL: Double) {
        _uiState.update { it.copy(spanL = newL) }
    }

    fun setParamA(newA: Double) {
        _uiState.update { it.copy(paramA = newA) }
    }

    fun setShiftC(newShift: Double) {
        _uiState.update { it.copy(shiftC = newShift) }
    }

    fun resetTransformations() {
        _uiState.update { it.copy(spanL = 6.0, paramA = 2.0, shiftC = 0.0) }
    }

    fun toggleYEqualsX() {
        _uiState.update { it.copy(showYEqualsX = !it.showYEqualsX) }
    }

    fun toggleTangentLine() {
        _uiState.update { it.copy(showTangentLine = !it.showTangentLine) }
    }

    fun setTangentLine(show: Boolean) {
        _uiState.update { it.copy(showTangentLine = show) }
    }

    fun toggleParabolaComparison() {
        _uiState.update { current ->
            val newState = !current.showParabolaComparison
            // If turning on parabola comparison, ensure COSH is also active for direct comparison
            val updatedFunctions = if (newState && !current.activeFunctions.contains(HyperbolicFunc.COSH)) {
                current.activeFunctions + HyperbolicFunc.COSH
            } else {
                current.activeFunctions
            }
            current.copy(
                showParabolaComparison = newState,
                activeFunctions = updatedFunctions
            )
        }
    }

    fun setParabolaComparison(show: Boolean) {
        _uiState.update { current ->
            val updatedFunctions = if (show && !current.activeFunctions.contains(HyperbolicFunc.COSH)) {
                current.activeFunctions + HyperbolicFunc.COSH
            } else {
                current.activeFunctions
            }
            current.copy(
                showParabolaComparison = show,
                activeFunctions = updatedFunctions
            )
        }
    }

    fun setParabolaMode(mode: ParabolaMode) {
        _uiState.update { it.copy(parabolaMode = mode) }
    }

    fun setMorphBlend(blend: Float) {
        _uiState.update { it.copy(morphBlend = blend.coerceIn(0f, 1f), isAutoMorphing = false) }
    }

    fun toggleAutoMorph() {
        _uiState.update { it.copy(isAutoMorphing = !it.isAutoMorphing) }
    }

    fun setAutoMorphing(auto: Boolean) {
        _uiState.update { it.copy(isAutoMorphing = auto) }
    }

    /**
     * Evaluates the parabola value at coordinate x according to current mode and transformation parameters.
     */
    fun evaluateParabola(x: Double, mode: ParabolaMode = _uiState.value.parabolaMode): Double {
        val a = _uiState.value.paramA
        val c = _uiState.value.shiftC
        return when (mode) {
            ParabolaMode.STANDARD_X_SQUARED -> x * x
            ParabolaMode.TAYLOR_SERIES -> 1.0 + (x * x) / 2.0
            ParabolaMode.MATCHED_CATENARY_PARABOLA -> {
                val dx = x - c
                a + (dx * dx) / (2.0 * a)
            }
        }
    }

    fun setSelectedTab(tab: AppViewTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun setSelectedSidebarTab(tab: SidebarSectionTab) {
        _uiState.update { it.copy(selectedSidebarTab = tab) }
    }

    fun togglePanZoomMode() {
        _uiState.update { it.copy(isPanZoomMode = !it.isPanZoomMode) }
    }

    fun toggleHaptics() {
        _uiState.update { it.copy(isHapticsEnabled = !it.isHapticsEnabled) }
    }

    fun setHapticsEnabled(enabled: Boolean) {
        _uiState.update { it.copy(isHapticsEnabled = enabled) }
    }

    fun toggleGrid() {
        _uiState.update { it.copy(showGrid = !it.showGrid) }
    }

    fun toggleAsymptotes() {
        _uiState.update { it.copy(showAsymptotes = !it.showAsymptotes) }
    }

    fun setTheoryDialogVisible(visible: Boolean) {
        _uiState.update { it.copy(showTheoryDialog = visible) }
    }

    fun toggleTheme() {
        _uiState.update { it.copy(isDarkTheme = !it.isDarkTheme) }
    }

    fun setDarkTheme(isDark: Boolean) {
        _uiState.update { it.copy(isDarkTheme = isDark) }
    }

    // =========================================================================
    // Catenary Cable Physics Calculation Engine (y = a * cosh(x / a))
    // =========================================================================

    /**
     * Updates the horizontal tension T₀ (in Newtons).
     * Recomputes catenary scaling parameter a = T₀ / (μ * g).
     */
    fun updateCableTension(tensionN: Double) {
        _uiState.update { current ->
            val updatedCalc = current.catenaryCalculation.copy(
                horizontalTensionN = tensionN.coerceAtLeast(10.0)
            )
            current.copy(
                catenaryCalculation = updatedCalc,
                selectedCablePreset = null
            )
        }
    }

    /**
     * Updates the span L between cable supports/pylons (in meters).
     */
    fun updateCableSpan(spanM: Double) {
        _uiState.update { current ->
            val updatedCalc = current.catenaryCalculation.copy(
                spanM = spanM.coerceAtLeast(1.0)
            )
            current.copy(
                catenaryCalculation = updatedCalc,
                selectedCablePreset = null
            )
        }
    }

    /**
     * Updates the cable linear mass density μ (in kg/m).
     * Recomputes linear weight density w = μ * g and a = T₀ / w.
     */
    fun updateCableMassDensity(massKgPerM: Double) {
        _uiState.update { current ->
            val updatedCalc = current.catenaryCalculation.copy(
                linearMassDensityKgPerM = massKgPerM.coerceAtLeast(0.01)
            )
            current.copy(
                catenaryCalculation = updatedCalc,
                selectedCablePreset = null
            )
        }
    }

    /**
     * Applies a predefined real-world engineering cable preset.
     */
    fun applyCablePreset(preset: CablePreset) {
        _uiState.update { current ->
            val updatedCalc = CatenaryCalculation(
                horizontalTensionN = preset.tensionN,
                spanM = preset.spanM,
                linearMassDensityKgPerM = preset.massKgPerM
            )
            current.copy(
                catenaryCalculation = updatedCalc,
                selectedCablePreset = preset
            )
        }
    }

    /**
     * Calculates the catenary elevation y, relative sag, slope, and local tension at position x.
     * Uses equation: y(x) = a * cosh(x / a).
     */
    fun calculateCatenaryPoint(x: Double): CatenaryCurvePoint {
        val calc = _uiState.value.catenaryCalculation
        val a = calc.parameterA
        val y = calc.evaluateY(x)
        val sag = y - a
        val slope = calc.evaluateSlope(x)
        val tension = calc.evaluateTensionN(x)
        val halfSpan = calc.halfSpanM
        val fraction = if (calc.spanM > 0.0) ((x + halfSpan) / calc.spanM).toFloat().coerceIn(0f, 1f) else 0.5f
        val normSag = if (calc.maxSagM > 0.0) (sag / calc.maxSagM).toFloat() else 0f
        return CatenaryCurvePoint(
            x = x,
            y = y,
            sag = sag,
            slope = slope,
            tensionN = tension,
            normalizedX = fraction,
            normalizedSag = normSag
        )
    }

    /**
     * Generates evenly sampled points along the catenary curve between x = -L/2 and x = +L/2.
     */
    fun getSampledCatenaryCurve(stepCount: Int = 100): List<CatenaryCurvePoint> {
        return _uiState.value.catenaryCalculation.sampleCurvePoints(stepCount)
    }

    // =========================================================================
    // Gemini Chatbot & Google Search Grounding Engine
    // =========================================================================

    fun selectGeminiModel(choice: GeminiModelChoice) {
        _uiState.update { it.copy(selectedGeminiModel = choice) }
    }

    fun toggleSearchGrounding(enabled: Boolean) {
        _uiState.update { it.copy(isSearchGroundingEnabled = enabled) }
    }

    fun clearChat() {
        _uiState.update {
            it.copy(
                chatMessages = listOf(
                    ChatMessage(
                        sender = MessageSender.AI,
                        text = "Chat history cleared. How can I assist you with hyperbolic math, calculus, or catenary physics today?",
                        modelName = it.selectedGeminiModel.displayName,
                        isSearchGrounded = false
                    )
                )
            )
        }
    }

    fun sendChatMessage(promptText: String) {
        val trimmed = promptText.trim()
        if (trimmed.isBlank() || _uiState.value.isGeneratingChatResponse) return

        val userMessage = ChatMessage(
            sender = MessageSender.USER,
            text = trimmed
        )

        _uiState.update { current ->
            current.copy(
                chatMessages = current.chatMessages + userMessage,
                isGeneratingChatResponse = true
            )
        }

        viewModelScope.launch {
            val currentState = _uiState.value
            val history = currentState.chatMessages.dropLast(1) // exclude current user message from history argument since sendChatMessage adds it
            val result = GeminiClient.sendChatMessage(
                conversationHistory = history,
                userMessage = trimmed,
                modelChoice = currentState.selectedGeminiModel,
                enableSearchGrounding = currentState.isSearchGroundingEnabled
            )

            result.onSuccess { aiMsg ->
                _uiState.update { current ->
                    current.copy(
                        chatMessages = current.chatMessages + aiMsg,
                        isGeneratingChatResponse = false
                    )
                }
            }.onFailure { error ->
                val errorMsg = ChatMessage(
                    sender = MessageSender.AI,
                    text = "⚠️ Gemini Request Notice: ${error.message ?: "Unable to complete request"}\n\nTip: You can configure your Gemini API Key in the AI Studio Secrets panel.",
                    modelName = currentState.selectedGeminiModel.displayName,
                    isError = true
                )
                _uiState.update { current ->
                    current.copy(
                        chatMessages = current.chatMessages + errorMsg,
                        isGeneratingChatResponse = false
                    )
                }
            }
        }
    }

    fun sendCurrentAppContextToChat() {
        val state = _uiState.value
        val activeFuncNames = state.activeFunctions.joinToString(", ") { it.displayName }
        val scrubY = state.activeFunctions.firstOrNull()?.evaluate(state.scrubX, state.paramA, state.shiftC) ?: 0.0
        val catenary = state.catenaryCalculation

        val contextPrompt = """
            Please analyze my current workspace in Hyperbolic & Catenary Studio:
            - Active Hyperbolic Functions: $activeFuncNames
            - Parameters: Amplitude A = ${state.paramA}, Shift c = ${state.shiftC}, Span L = ${state.spanL}
            - Inspected Coordinate: x = ${String.format(java.util.Locale.US, "%.2f", state.scrubX)}, f(x) = ${String.format(java.util.Locale.US, "%.3f", scrubY)}
            - Real-World Catenary Cable Setup: Span L = ${catenary.spanM} m, Tension T₀ = ${catenary.horizontalTensionN} N, Linear density μ = ${catenary.linearMassDensityKgPerM} kg/m, Parameter a = ${String.format(java.util.Locale.US, "%.2f", catenary.parameterA)}, Max Sag = ${String.format(java.util.Locale.US, "%.2f", catenary.maxSagM)} m, Arc Length S = ${String.format(java.util.Locale.US, "%.2f", catenary.arcLengthM)} m.
            
            Can you provide an intuitive mathematical summary of these values, verify the tension and sag physics, and cite any real-world architectural or power-transmission structures with comparable dimensions?
        """.trimIndent()

        sendChatMessage(contextPrompt)
    }

    // ==========================================
    // Numerical Solver Actions
    // ==========================================

    fun setSolverMode(mode: com.example.model.SolverMode) {
        _uiState.update { it.copy(solverMode = mode, solverResult = com.example.model.SolverResultState.Idle) }
    }

    fun setSolverFx(fx: String) {
        _uiState.update { it.copy(solverFx = fx) }
    }

    fun setSolverGx(gx: String) {
        _uiState.update { it.copy(solverGx = gx) }
    }

    fun setSolverMethod(method: com.example.model.NumericalMethod) {
        _uiState.update { it.copy(solverMethod = method) }
    }

    fun setSolverDomain(min: Double, max: Double) {
        _uiState.update { it.copy(solverDomainMin = min, solverDomainMax = max) }
    }

    fun setSolverTolerance(tol: Double) {
        _uiState.update { it.copy(solverTolerance = tol) }
    }

    fun applySolverPreset(preset: com.example.model.EquationSolverPreset) {
        _uiState.update {
            it.copy(
                solverMode = preset.mode,
                solverFx = preset.fx,
                solverGx = preset.gx,
                solverDomainMin = preset.domainMin,
                solverDomainMax = preset.domainMax,
                solverResult = com.example.model.SolverResultState.Idle
            )
        }
        solveEquation()
    }

    fun solveEquation() {
        val state = _uiState.value
        _uiState.update { it.copy(solverResult = com.example.model.SolverResultState.Solving) }

        viewModelScope.launch {
            val result = com.example.model.NumericalEquationSolver.solve(
                mode = state.solverMode,
                fStr = state.solverFx,
                gStr = state.solverGx,
                domainMin = state.solverDomainMin,
                domainMax = state.solverDomainMax,
                tolerance = state.solverTolerance,
                method = state.solverMethod,
                paramA = state.paramA,
                shiftC = state.shiftC
            )
            _uiState.update { it.copy(solverResult = result) }
        }
    }

    fun inspectSolvedRoot(rootX: Double) {
        setScrubX(rootX)
        // Ensure user can see the point on plot
        val currentBounds = _uiState.value.bounds
        if (rootX < currentBounds.xMin || rootX > currentBounds.xMax) {
            val padding = 2.0
            val newMin = kotlin.math.min(currentBounds.xMin, (rootX - padding).toFloat())
            val newMax = kotlin.math.max(currentBounds.xMax, (rootX + padding).toFloat())
            _uiState.update { it.copy(bounds = currentBounds.copy(xMin = newMin, xMax = newMax)) }
        }
    }

    fun askGeminiAboutSolution(equationDesc: String, roots: List<com.example.model.SolvedPoint>) {
        val rootsSummary = if (roots.isEmpty()) {
            "No real roots found in domain."
        } else {
            roots.mapIndexed { idx, pt ->
                "#${idx + 1}: x* = ${pt.formattedX}, y* = ${pt.formattedY} (residual |f(x*)| = ${pt.formattedResidual}, converged in ${pt.iterationsCount} iterations via ${pt.methodUsed.displayName})"
            }.joinToString("\n")
        }

        val prompt = """
            I just used the Numerical Solver to solve the following equation:
            $equationDesc
            
            Discovered Numerical Solutions:
            $rootsSummary
            
            Can you provide:
            1. An analytical or symbolic solution/derivation (e.g. using logarithmic forms of inverse hyperbolic functions like arcosh, arsinh, artanh).
            2. An intuitive geometric explanation of why the roots/intersections occur at these exact coordinates.
            3. Any relevant physical or engineering implications of this equation (e.g., in catenary sag, cable tension, or structural arches).
        """.trimIndent()

        setSelectedTab(AppViewTab.AI_CHAT)
        setSelectedSidebarTab(SidebarSectionTab.GEMINI_AI)
        sendChatMessage(prompt)
    }

    fun setDiffFunctionExpr(expr: String) {
        val (parsed, error) = try {
            val p = com.example.model.Expression(expr)
            Pair(p, null)
        } catch (e: Exception) {
            Pair(null, e.message ?: "Invalid mathematical expression syntax")
        }
        _uiState.update {
            it.copy(
                diffFunctionExpr = expr,
                diffParsedExpression = parsed,
                diffParseError = error
            )
        }
    }

    fun setDiffStepSizeH(h: Double) {
        _uiState.update { it.copy(diffStepSizeH = h.coerceIn(1e-6, 0.5)) }
    }

    fun setDiffMethod(method: com.example.model.DifferentiationMethod) {
        _uiState.update { it.copy(diffMethod = method) }
    }

    fun togglePlotDiffFunction(enabled: Boolean) {
        _uiState.update { it.copy(plotDiffFunction = enabled) }
    }

    fun togglePlotFirstDerivative(enabled: Boolean) {
        _uiState.update { it.copy(plotFirstDerivative = enabled) }
    }

    fun togglePlotSecondDerivative(enabled: Boolean) {
        _uiState.update { it.copy(plotSecondDerivative = enabled) }
    }

    fun toggleShowDiffTangentLine(enabled: Boolean) {
        _uiState.update { it.copy(showDiffTangentLine = enabled) }
    }

    fun toggleShowDiffNormalLine(enabled: Boolean) {
        _uiState.update { it.copy(showDiffNormalLine = enabled) }
    }

    fun toggleDiffToolActive(active: Boolean) {
        _uiState.update { it.copy(isDiffToolActive = active) }
    }

    fun applyDiffPreset(preset: com.example.model.DifferentiationPreset) {
        setDiffFunctionExpr(preset.expression)
        _uiState.update {
            it.copy(
                isDiffToolActive = true,
                plotDiffFunction = true,
                plotFirstDerivative = true
            )
        }
    }

    fun applyDifferentiationPreset(preset: com.example.model.DifferentiationPreset) {
        applyDiffPreset(preset)
    }

    fun askGeminiAboutDerivative(expr: String, diagnostics: com.example.model.CalculusPointDiagnostics?) {
        val diagSummary = if (diagnostics != null) {
            """
            Current Live Evaluation at x₀ = ${diagnostics.formattedX0}:
            - Function value f(x₀) = ${diagnostics.formattedFx0}
            - 1st Derivative f'(x₀) = ${diagnostics.formattedFPrime0} (Slope angle: ${diagnostics.formattedSlopeAngle})
            - 2nd Derivative f''(x₀) = ${diagnostics.formattedFDoublePrime0} (${diagnostics.concavity.displayName})
            - Tangent Line Equation: ${diagnostics.tangentEquation}
            - Normal Line Equation: ${diagnostics.normalEquation}
            - Radius of Curvature: ${diagnostics.formattedRadiusOfCurvature}
            """.trimIndent()
        } else {
            "Numerical diagnostics unavailable for this expression."
        }

        val prompt = """
            I am analyzing the function f(x) = $expr using the Numerical Differentiation tool.
            
            $diagSummary
            
            Can you provide:
            1. The exact symbolic derivative f'(x) = d/dx[$expr] with step-by-step calculus derivation (showing chain rule, product rule, quotient rule, or hyperbolic derivative identities).
            2. The second derivative f''(x) = d²/dx²[$expr] and an analysis of its concavity and inflection points where f''(x) = 0.
            3. Comparison between the analytical limit definition lim_{h->0} (f(x+h) - f(x))/h and numerical finite difference stencils (such as the 5-point central stencil).
            4. Practical physical or geometric interpretation (such as curve slope, tangent lines, velocity/acceleration, or wave dispersion).
        """.trimIndent()

        setSelectedTab(AppViewTab.AI_CHAT)
        setSelectedSidebarTab(SidebarSectionTab.GEMINI_AI)
        sendChatMessage(prompt)
    }
}

