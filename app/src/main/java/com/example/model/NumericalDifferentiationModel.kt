package com.example.model

import java.util.Locale
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Numerical Differentiation Methods / Finite Difference Stencils
 */
enum class DifferentiationMethod(
    val displayName: String,
    val orderOfAccuracy: String,
    val formulaDescription: String
) {
    FIVE_POINT_CENTRAL(
        displayName = "5-Point Central Difference",
        orderOfAccuracy = "O(h⁴) High Precision",
        formulaDescription = "(-f(x+2h) + 8f(x+h) - 8f(x-h) + f(x-2h)) / (12h)"
    ),
    THREE_POINT_CENTRAL(
        displayName = "3-Point Central Difference",
        orderOfAccuracy = "O(h²) Standard Symmetric",
        formulaDescription = "(f(x+h) - f(x-h)) / (2h)"
    ),
    FORWARD_DIFFERENCE(
        displayName = "Forward Difference (Euler)",
        orderOfAccuracy = "O(h) First Order",
        formulaDescription = "(f(x+h) - f(x)) / h"
    ),
    BACKWARD_DIFFERENCE(
        displayName = "Backward Difference",
        orderOfAccuracy = "O(h) First Order",
        formulaDescription = "(f(x) - f(x-h)) / h"
    )
}

/**
 * Curvature / Concavity classification at a point
 */
enum class ConcavityType(val displayName: String, val badgeColorHex: Long) {
    CONCAVE_UP("Concave Up (f'' > 0)", 0xFF16A34A),
    CONCAVE_DOWN("Concave Down (f'' < 0)", 0xFFDC2626),
    INFLECTION_POINT("Inflection / Flat (f'' ≈ 0)", 0xFFD97706)
}

/**
 * Real-time Calculus Point Diagnostics at a specific x0 coordinate
 */
data class CalculusPointDiagnostics(
    val x0: Double,
    val fx0: Double,
    val fPrime0: Double,
    val fDoublePrime0: Double,
    val slopeAngleDeg: Double,
    val concavity: ConcavityType,
    val tangentSlopeM: Double,
    val tangentInterceptB: Double,
    val tangentEquation: String,
    val normalEquation: String,
    val radiusOfCurvature: Double?
) {
    val formattedX0: String get() = String.format(Locale.US, "%+.4f", x0)
    val formattedFx0: String get() = String.format(Locale.US, "%+.4f", fx0)
    val formattedFPrime0: String get() = String.format(Locale.US, "%+.4f", fPrime0)
    val formattedFDoublePrime0: String get() = String.format(Locale.US, "%+.4f", fDoublePrime0)
    val formattedSlopeAngle: String get() = String.format(Locale.US, "%.1f°", slopeAngleDeg)
    val formattedRadiusOfCurvature: String
        get() = radiusOfCurvature?.let { if (it > 1000.0) "∞ (Flat)" else String.format(Locale.US, "%.3f", it) } ?: "Undefined"
}

/**
 * Educational & Classical Hyperbolic Differentiation Presets
 */
data class DifferentiationPreset(
    val title: String,
    val expression: String,
    val analyticalDerivative: String,
    val description: String,
    val defaultDomainMin: Double = -5.0,
    val defaultDomainMax: Double = 5.0
)

object NumericalDifferentiationPresets {
    val list: List<DifferentiationPreset> = listOf(
        DifferentiationPreset(
            title = "cosh(x) → sinh(x)",
            expression = "cosh(x)",
            analyticalDerivative = "sinh(x)",
            description = "The derivative of the catenary hyperbolic cosine is exactly the hyperbolic sine."
        ),
        DifferentiationPreset(
            title = "sinh(x) → cosh(x)",
            expression = "sinh(x)",
            analyticalDerivative = "cosh(x)",
            description = "Hyperbolic sine differentiates directly into hyperbolic cosine without a negative sign."
        ),
        DifferentiationPreset(
            title = "tanh(x) → sech²(x)",
            expression = "tanh(x)",
            analyticalDerivative = "sech(x)^2",
            description = "The derivative of hyperbolic tangent is hyperbolic secant squared (1 - tanh²(x))."
        ),
        DifferentiationPreset(
            title = "sech(x) → -sech(x)·tanh(x)",
            expression = "sech(x)",
            analyticalDerivative = "-sech(x)*tanh(x)",
            description = "Soliton envelope curve: represents solitary wave packets in optical fibers."
        ),
        DifferentiationPreset(
            title = "Gaussian Damped Catenary",
            expression = "exp(-x^2) * cosh(x)",
            analyticalDerivative = "exp(-x^2)*(sinh(x) - 2*x*cosh(x))",
            description = "Product rule demonstration of a localized Gaussian envelope modulating a catenary curve."
        ),
        DifferentiationPreset(
            title = "Soliton Pulse sech²(x)",
            expression = "sech(x)^2",
            analyticalDerivative = "-2*sech(x)^2*tanh(x)",
            description = "Korteweg-de Vries (KdV) solitary wave equation fundamental solution profile."
        ),
        DifferentiationPreset(
            title = "Inverse sinh: asinh(x)",
            expression = "asinh(x)",
            analyticalDerivative = "1 / sqrt(x^2 + 1)",
            description = "Algebraic derivative of the inverse hyperbolic sine function."
        ),
        DifferentiationPreset(
            title = "Product Rule: x² · sinh(x)",
            expression = "x^2 * sinh(x)",
            analyticalDerivative = "2*x*sinh(x) + x^2*cosh(x)",
            description = "Polynomial and hyperbolic product rule calculus test."
        )
    )
}

/**
 * Numerical Differentiation Computation Engine
 */
object NumericalDifferentiationEngine {

    fun evalFunction(
        expr: Expression,
        x: Double,
        paramA: Double = 2.0,
        shiftC: Double = 0.0
    ): Double? {
        return try {
            val v = expr.evaluate(x, paramA, shiftC)
            if (v.isNaN() || v.isInfinite()) null else v
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Compute 1st derivative f'(x) numerically using specified finite difference stencil
     */
    fun evalFirstDerivative(
        expr: Expression,
        x: Double,
        h: Double = 1e-4,
        method: DifferentiationMethod = DifferentiationMethod.FIVE_POINT_CENTRAL,
        paramA: Double = 2.0,
        shiftC: Double = 0.0
    ): Double? {
        val step = h.coerceIn(1e-6, 0.5)
        return try {
            when (method) {
                DifferentiationMethod.FIVE_POINT_CENTRAL -> {
                    val f_minus_2 = evalFunction(expr, x - 2 * step, paramA, shiftC) ?: return null
                    val f_minus_1 = evalFunction(expr, x - step, paramA, shiftC) ?: return null
                    val f_plus_1 = evalFunction(expr, x + step, paramA, shiftC) ?: return null
                    val f_plus_2 = evalFunction(expr, x + 2 * step, paramA, shiftC) ?: return null
                    val derivative = (-f_plus_2 + 8.0 * f_plus_1 - 8.0 * f_minus_1 + f_minus_2) / (12.0 * step)
                    if (derivative.isNaN() || derivative.isInfinite()) null else derivative
                }
                DifferentiationMethod.THREE_POINT_CENTRAL -> {
                    val f_plus = evalFunction(expr, x + step, paramA, shiftC) ?: return null
                    val f_minus = evalFunction(expr, x - step, paramA, shiftC) ?: return null
                    val derivative = (f_plus - f_minus) / (2.0 * step)
                    if (derivative.isNaN() || derivative.isInfinite()) null else derivative
                }
                DifferentiationMethod.FORWARD_DIFFERENCE -> {
                    val f_curr = evalFunction(expr, x, paramA, shiftC) ?: return null
                    val f_plus = evalFunction(expr, x + step, paramA, shiftC) ?: return null
                    val derivative = (f_plus - f_curr) / step
                    if (derivative.isNaN() || derivative.isInfinite()) null else derivative
                }
                DifferentiationMethod.BACKWARD_DIFFERENCE -> {
                    val f_curr = evalFunction(expr, x, paramA, shiftC) ?: return null
                    val f_minus = evalFunction(expr, x - step, paramA, shiftC) ?: return null
                    val derivative = (f_curr - f_minus) / step
                    if (derivative.isNaN() || derivative.isInfinite()) null else derivative
                }
            }
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Compute 2nd derivative f''(x) numerically (Concavity and Curvature)
     */
    fun evalSecondDerivative(
        expr: Expression,
        x: Double,
        h: Double = 1e-4,
        paramA: Double = 2.0,
        shiftC: Double = 0.0
    ): Double? {
        val step = h.coerceIn(1e-6, 0.5)
        return try {
            // High precision 5-point central stencil for 2nd derivative:
            // f''(x) ≈ (-f(x+2h) + 16f(x+h) - 30f(x) + 16f(x-h) - f(x-2h)) / (12h²)
            val f_minus_2 = evalFunction(expr, x - 2 * step, paramA, shiftC)
            val f_minus_1 = evalFunction(expr, x - step, paramA, shiftC)
            val f_center = evalFunction(expr, x, paramA, shiftC)
            val f_plus_1 = evalFunction(expr, x + step, paramA, shiftC)
            val f_plus_2 = evalFunction(expr, x + 2 * step, paramA, shiftC)

            if (f_minus_2 != null && f_minus_1 != null && f_center != null && f_plus_1 != null && f_plus_2 != null) {
                val d2 = (-f_plus_2 + 16.0 * f_plus_1 - 30.0 * f_center + 16.0 * f_minus_1 - f_minus_2) / (12.0 * step * step)
                if (!d2.isNaN() && !d2.isInfinite()) return d2
            }

            // Fallback to standard 3-point central stencil: (f(x+h) - 2f(x) + f(x-h)) / h²
            if (f_minus_1 != null && f_center != null && f_plus_1 != null) {
                val d2 = (f_plus_1 - 2.0 * f_center + f_minus_1) / (step * step)
                if (!d2.isNaN() && !d2.isInfinite()) return d2
            }
            null
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Compute comprehensive calculus point diagnostics at coordinate x0
     */
    fun computeCalculusDiagnostics(
        expr: Expression,
        x0: Double,
        h: Double = 1e-4,
        method: DifferentiationMethod = DifferentiationMethod.FIVE_POINT_CENTRAL,
        paramA: Double = 2.0,
        shiftC: Double = 0.0
    ): CalculusPointDiagnostics? {
        val fx0 = evalFunction(expr, x0, paramA, shiftC) ?: return null
        val fPrime0 = evalFirstDerivative(expr, x0, h, method, paramA, shiftC) ?: return null
        val fDoublePrime0 = evalSecondDerivative(expr, x0, h, paramA, shiftC) ?: 0.0

        val slopeAngleDeg = atan(fPrime0) * (180.0 / Math.PI)

        val concavity = when {
            fDoublePrime0 > 1e-4 -> ConcavityType.CONCAVE_UP
            fDoublePrime0 < -1e-4 -> ConcavityType.CONCAVE_DOWN
            else -> ConcavityType.INFLECTION_POINT
        }

        // Tangent line: y - fx0 = fPrime0 * (x - x0) => y = fPrime0 * x + (fx0 - fPrime0 * x0)
        val tangentSlopeM = fPrime0
        val tangentInterceptB = fx0 - fPrime0 * x0
        val tangentEquation = "y = ${String.format(Locale.US, "%.3f", tangentSlopeM)}x ${if (tangentInterceptB >= 0) "+ " + String.format(Locale.US, "%.3f", tangentInterceptB) else "- " + String.format(Locale.US, "%.3f", abs(tangentInterceptB))}"

        // Normal line: slope = -1 / fPrime0 (if fPrime0 != 0)
        val normalEquation = if (abs(fPrime0) > 1e-6) {
            val normalM = -1.0 / fPrime0
            val normalB = fx0 - normalM * x0
            "y = ${String.format(Locale.US, "%.3f", normalM)}x ${if (normalB >= 0) "+ " + String.format(Locale.US, "%.3f", normalB) else "- " + String.format(Locale.US, "%.3f", abs(normalB))}"
        } else {
            "x = ${String.format(Locale.US, "%.3f", x0)} (Vertical)"
        }

        // Radius of Curvature: R = (1 + (f')²)^(3/2) / |f''|
        val radiusOfCurvature = if (abs(fDoublePrime0) > 1e-7) {
            (1.0 + fPrime0 * fPrime0).pow(1.5) / abs(fDoublePrime0)
        } else {
            null
        }

        return CalculusPointDiagnostics(
            x0 = x0,
            fx0 = fx0,
            fPrime0 = fPrime0,
            fDoublePrime0 = fDoublePrime0,
            slopeAngleDeg = slopeAngleDeg,
            concavity = concavity,
            tangentSlopeM = tangentSlopeM,
            tangentInterceptB = tangentInterceptB,
            tangentEquation = tangentEquation,
            normalEquation = normalEquation,
            radiusOfCurvature = radiusOfCurvature
        )
    }
}
