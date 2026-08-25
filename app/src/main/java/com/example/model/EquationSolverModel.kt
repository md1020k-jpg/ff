package com.example.model

import java.util.Locale
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.cosh
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sign
import kotlin.math.sin
import kotlin.math.sinh
import kotlin.math.sqrt
import kotlin.math.tan
import kotlin.math.tanh

/**
 * Modes for the Numerical Equation Solver
 */
enum class SolverMode(val displayName: String, val formulaTemplate: String) {
    ROOT_FINDING("Find Roots", "f(x) = 0"),
    INTERSECTION("Find Intersections", "f(x) = g(x)")
}

/**
 * Numerical Root-Finding Algorithms
 */
enum class NumericalMethod(val displayName: String, val shortDescription: String) {
    HYBRID_BRENT_NEWTON(
        "Hybrid Brent-Newton (Recommended)",
        "Combines interval subdivision, bisection stability & rapid Newton convergence"
    ),
    NEWTON_RAPHSON(
        "Newton-Raphson Method",
        "Fast quadratic convergence using numerical finite difference derivatives"
    ),
    BISECTION(
        "Bisection Method",
        "Guaranteed interval halving; robust for monotonic or discontinuous regions"
    ),
    SECANT(
        "Secant Method",
        "Quasi-Newton linear interpolation between two successive points"
    )
}

/**
 * Step detail during numerical iteration
 */
data class IterationStep(
    val iteration: Int,
    val xVal: Double,
    val fxVal: Double,
    val deltaX: Double
)

/**
 * A single discovered root or intersection point
 */
data class SolvedPoint(
    val x: Double,
    val y: Double,
    val residual: Double,
    val iterationsCount: Int,
    val methodUsed: NumericalMethod,
    val steps: List<IterationStep> = emptyList(),
    val isExact: Boolean = false
) {
    val formattedX: String get() = String.format(Locale.US, "%.6f", x)
    val formattedY: String get() = String.format(Locale.US, "%.6f", y)
    val formattedResidual: String get() = String.format(Locale.US, "%.2e", residual)
}

/**
 * Result state of a numerical equation solving session
 */
sealed class SolverResultState {
    object Idle : SolverResultState()
    object Solving : SolverResultState()
    data class Success(
        val equationDescription: String,
        val roots: List<SolvedPoint>,
        val searchInterval: Pair<Double, Double>,
        val tolerance: Double,
        val executionTimeMs: Long
    ) : SolverResultState()
    data class Error(val message: String) : SolverResultState()
}

/**
 * Presets for interesting Hyperbolic Equation Roots and Intersections
 */
data class EquationSolverPreset(
    val title: String,
    val mode: SolverMode,
    val fx: String,
    val gx: String = "",
    val domainMin: Double = -5.0,
    val domainMax: Double = 5.0,
    val description: String
)

object SolverPresets {
    val PRESETS = listOf(
        EquationSolverPreset(
            title = "Cosh Level: cosh(x) = 3",
            mode = SolverMode.ROOT_FINDING,
            fx = "cosh(x) - 3",
            domainMin = -4.0,
            domainMax = 4.0,
            description = "Roots at x = ±arcosh(3) ≈ ±1.762747"
        ),
        EquationSolverPreset(
            title = "Sinh-Linear Intersection: sinh(x) = 2x",
            mode = SolverMode.INTERSECTION,
            fx = "sinh(x)",
            gx = "2*x",
            domainMin = -4.0,
            domainMax = 4.0,
            description = "Three intersection points: x = 0 and x ≈ ±2.177319"
        ),
        EquationSolverPreset(
            title = "Tanh Bifurcation: tanh(x) = 0.5x",
            mode = SolverMode.INTERSECTION,
            fx = "tanh(x)",
            gx = "0.5*x",
            domainMin = -3.0,
            domainMax = 3.0,
            description = "Three roots: x = 0 and x ≈ ±1.915008"
        ),
        EquationSolverPreset(
            title = "Osculating Parabola: cosh(x) = 1 + 0.5x²",
            mode = SolverMode.INTERSECTION,
            fx = "cosh(x)",
            gx = "1 + 0.5*x^2",
            domainMin = -3.0,
            domainMax = 3.0,
            description = "Higher-order contact at x = 0 where Taylor polynomial matches cosh(x)"
        ),
        EquationSolverPreset(
            title = "Catenary Cable Sag: 2*cosh(x/2) = 5",
            mode = SolverMode.ROOT_FINDING,
            fx = "2*cosh(x/2) - 5",
            domainMin = -6.0,
            domainMax = 6.0,
            description = "Tower span endpoints for catenary parameter a = 2 with sag height"
        ),
        EquationSolverPreset(
            title = "Sech Threshold: sech(x) = 0.5",
            mode = SolverMode.ROOT_FINDING,
            fx = "sech(x) - 0.5",
            domainMin = -4.0,
            domainMax = 4.0,
            description = "Roots at x = ±arsech(0.5) ≈ ±1.316958"
        ),
        EquationSolverPreset(
            title = "Hyperbolic-Trig Cross: sinh(x) = cos(x)",
            mode = SolverMode.INTERSECTION,
            fx = "sinh(x)",
            gx = "cos(x)",
            domainMin = -3.0,
            domainMax = 3.0,
            description = "Unique intersection point around x ≈ 0.703314"
        ),
        EquationSolverPreset(
            title = "Coth Level: coth(x) = 2",
            mode = SolverMode.ROOT_FINDING,
            fx = "coth(x) - 2",
            domainMin = 0.1,
            domainMax = 3.0,
            description = "Inverse coth root at x = 0.5*ln(3) ≈ 0.549306"
        ),
        EquationSolverPreset(
            title = "Tanh vs Exponential: tanh(x) = exp(-x)",
            mode = SolverMode.INTERSECTION,
            fx = "tanh(x)",
            gx = "exp(-x)",
            domainMin = 0.0,
            domainMax = 4.0,
            description = "Transcendental root around x ≈ 0.639154"
        )
    )
}

/**
 * Recursive-Descent Expression Parser and Evaluator supporting Hyperbolic,
 * Trigonometric, Exponential, Logarithmic functions and arbitrary algebraic expressions.
 */
class Expression(val rawString: String) {
    private val normalized: String = preprocess(rawString)

    init {
        // Test compile / parse on dummy value x = 1.0 to validate syntax early
        evaluate(1.0)
    }

    private fun preprocess(input: String): String {
        var str = input.replace(" ", "").lowercase(Locale.ROOT)
        // Normalize = to minus if user entered equation like "cosh(x) = 3"
        if (str.contains("=")) {
            val parts = str.split("=")
            if (parts.size == 2) {
                str = "(${parts[0]})-(${parts[1]})"
            }
        }

        // Insert implicit multiplication: e.g. 2x -> 2*x, 3cosh -> 3*cosh, xcosh -> x*cosh, 2( -> 2*(, )x -> )*x
        val sb = StringBuilder()
        for (i in 0 until str.length) {
            val c = str[i]
            sb.append(c)
            if (i < str.length - 1) {
                val next = str[i + 1]
                if ((c.isDigit() || c == 'x' || c == 'e' || c == ')') &&
                    (next.isLetter() || next == '(') && !(c.isLetter() && next.isLetter())
                ) {
                    // Check if current is a digit or 'x' or ')' and next is '(' or variable
                    if ((c.isDigit() && (next.isLetter() || next == '(')) ||
                        (c == 'x' && (next.isLetter() || next == '(')) ||
                        (c == ')' && (next.isLetter() || next == '(' || next.isDigit()))
                    ) {
                        sb.append('*')
                    }
                }
            }
        }
        return sb.toString()
    }

    fun evaluate(x: Double, a: Double = 2.0, c: Double = 0.0): Double {
        val parser = StringParser(normalized, x, a, c)
        val result = parser.parse()
        if (result.isNaN()) {
            throw ArithmeticException("Result is NaN (Undefined region)")
        }
        return result
    }

    private class StringParser(
        private val str: String,
        private val x: Double,
        private val a: Double,
        private val c: Double
    ) {
        private var pos = -1
        private var ch = ' '

        private fun nextChar() {
            pos++
            ch = if (pos < str.length) str[pos] else '\u0000'
        }

        private fun eat(charToEat: Char): Boolean {
            while (ch == ' ') nextChar()
            if (ch == charToEat) {
                nextChar()
                return true
            }
            return false
        }

        fun parse(): Double {
            nextChar()
            val xVal = parseExpression()
            if (pos < str.length) {
                throw IllegalArgumentException("Unexpected character '${ch}' at position $pos in '$str'")
            }
            return xVal
        }

        // Grammar:
        // Expression = Term | Expression `+` Term | Expression `-` Term
        // Term = Factor | Term `*` Factor | Term `/` Factor
        // Factor = `+` Factor | `-` Factor | Primary `^` Factor
        // Primary = Number | Variable | Function `(` Expression `)` | `(` Expression `)`

        private fun parseExpression(): Double {
            var xVal = parseTerm()
            while (true) {
                when {
                    eat('+') -> xVal += parseTerm()
                    eat('-') -> xVal -= parseTerm()
                    else -> return xVal
                }
            }
        }

        private fun parseTerm(): Double {
            var xVal = parseFactor()
            while (true) {
                when {
                    eat('*') -> xVal *= parseFactor()
                    eat('/') -> {
                        val denom = parseFactor()
                        if (abs(denom) < 1e-15) {
                            return Double.NaN
                        }
                        xVal /= denom
                    }
                    else -> return xVal
                }
            }
        }

        private fun parseFactor(): Double {
            if (eat('+')) return parseFactor()
            if (eat('-')) return -parseFactor()

            var xVal = parsePrimary()

            if (eat('^')) {
                val exponent = parseFactor()
                xVal = xVal.pow(exponent)
            }
            return xVal
        }

        private fun parsePrimary(): Double {
            val startPos = pos

            if (eat('(')) {
                val xVal = parseExpression()
                if (!eat(')')) throw IllegalArgumentException("Missing closing parenthesis in expression")
                return xVal
            }

            // Numbers
            if ((ch in '0'..'9') || ch == '.') {
                while ((ch in '0'..'9') || ch == '.') nextChar()
                val numStr = str.substring(startPos, pos)
                return numStr.toDoubleOrNull() ?: throw IllegalArgumentException("Invalid number: $numStr")
            }

            // Identifiers: variables, constants, functions
            if (ch in 'a'..'z') {
                while (ch in 'a'..'z') nextChar()
                val id = str.substring(startPos, pos)

                // Single letter variables / constants
                when (id) {
                    "x" -> return x
                    "a" -> return a
                    "c" -> return c
                    "pi" -> return Math.PI
                    "e" -> return Math.E
                }

                // Functions require (
                if (!eat('(')) {
                    throw IllegalArgumentException("Expected '(' after function '$id'")
                }
                val arg = parseExpression()
                if (!eat(')')) throw IllegalArgumentException("Missing ')' after function argument in '$id'")

                return when (id) {
                    // Hyperbolic functions
                    "cosh" -> cosh(arg)
                    "sinh" -> sinh(arg)
                    "tanh" -> tanh(arg)
                    "sech" -> {
                        val cVal = cosh(arg)
                        if (abs(cVal) < 1e-15) Double.NaN else 1.0 / cVal
                    }
                    "csch" -> {
                        val sVal = sinh(arg)
                        if (abs(sVal) < 1e-15) Double.NaN else 1.0 / sVal
                    }
                    "coth" -> {
                        val tVal = tanh(arg)
                        if (abs(tVal) < 1e-15) Double.NaN else 1.0 / tVal
                    }
                    // Inverse hyperbolic functions
                    "asinh", "arcsinh" -> ln(arg + sqrt(arg * arg + 1.0))
                    "acosh", "arcosh", "arccosh" -> {
                        if (arg < 1.0) Double.NaN else ln(arg + sqrt(arg * arg - 1.0))
                    }
                    "atanh", "arctanh" -> {
                        if (abs(arg) >= 1.0) Double.NaN else 0.5 * ln((1.0 + arg) / (1.0 - arg))
                    }
                    // Standard trigonometric & calculus
                    "sin" -> sin(arg)
                    "cos" -> cos(arg)
                    "tan" -> tan(arg)
                    "asin", "arcsin" -> if (abs(arg) > 1.0) Double.NaN else asin(arg)
                    "acos", "arccos" -> if (abs(arg) > 1.0) Double.NaN else acos(arg)
                    "atan", "arctan" -> atan(arg)
                    "exp" -> exp(arg)
                    "ln" -> if (arg <= 0.0) Double.NaN else ln(arg)
                    "log", "log10" -> if (arg <= 0.0) Double.NaN else log10(arg)
                    "sqrt" -> if (arg < 0.0) Double.NaN else sqrt(arg)
                    "abs" -> abs(arg)
                    "sign" -> sign(arg)
                    else -> throw IllegalArgumentException("Unknown function: '$id'")
                }
            }

            throw IllegalArgumentException("Unexpected character '${ch}' at position $pos")
        }
    }
}

/**
 * Numerical Solver Engine for Hyperbolic Equations & Intersections
 */
object NumericalEquationSolver {

    /**
     * Solves f(x) = 0 (or f(x) - g(x) = 0) in the search domain [domainMin, domainMax].
     */
    fun solve(
        mode: SolverMode,
        fStr: String,
        gStr: String = "",
        domainMin: Double = -5.0,
        domainMax: Double = 5.0,
        tolerance: Double = 1e-7,
        maxIterations: Int = 100,
        method: NumericalMethod = NumericalMethod.HYBRID_BRENT_NEWTON,
        paramA: Double = 2.0,
        shiftC: Double = 0.0
    ): SolverResultState {
        val startTime = System.currentTimeMillis()

        if (domainMin >= domainMax) {
            return SolverResultState.Error("Interval minimum ($domainMin) must be strictly less than maximum ($domainMax).")
        }

        // Parse expressions
        val fExpr: Expression
        val gExpr: Expression?

        try {
            fExpr = Expression(fStr)
        } catch (e: Exception) {
            return SolverResultState.Error("Invalid equation f(x): ${e.message}")
        }

        try {
            gExpr = if (mode == SolverMode.INTERSECTION && gStr.isNotBlank()) {
                Expression(gStr)
            } else null
        } catch (e: Exception) {
            return SolverResultState.Error("Invalid equation g(x): ${e.message}")
        }

        val targetFunc: (Double) -> Double = { xVal ->
            val fx = fExpr.evaluate(xVal, paramA, shiftC)
            if (gExpr != null) {
                val gx = gExpr.evaluate(xVal, paramA, shiftC)
                fx - gx
            } else {
                fx
            }
        }

        val yValueFunc: (Double) -> Double = { xVal ->
            fExpr.evaluate(xVal, paramA, shiftC)
        }

        // Step 1: Subdivide domain into sub-intervals to find root brackets (sign changes) and local minima of |f(x)|
        val scanSteps = 300
        val stepSize = (domainMax - domainMin) / scanSteps
        val brackets = mutableListOf<Pair<Double, Double>>()
        val discoveredRoots = mutableListOf<SolvedPoint>()

        var prevX = domainMin
        var prevVal = try { targetFunc(prevX) } catch (e: Exception) { Double.NaN }

        for (i in 1..scanSteps) {
            val currX = domainMin + i * stepSize
            val currVal = try { targetFunc(currX) } catch (e: Exception) { Double.NaN }

            if (!prevVal.isNaN() && !currVal.isNaN()) {
                // Exact hit at interval edge
                if (abs(currVal) < tolerance) {
                    val rootY = try { yValueFunc(currX) } catch (e: Exception) { 0.0 }
                    addUniqueRoot(discoveredRoots, SolvedPoint(currX, rootY, abs(currVal), 1, method, isExact = true), tolerance)
                } else if (prevVal * currVal < 0.0) {
                    // Sign change bracket!
                    brackets.add(Pair(prevX, currX))
                }
            }
            prevX = currX
            prevVal = currVal
        }

        // Step 2: For each bracket, run the chosen numerical method to refine root to high precision
        for (bracket in brackets) {
            val root = when (method) {
                NumericalMethod.HYBRID_BRENT_NEWTON -> solveHybridBrentNewton(targetFunc, yValueFunc, bracket.first, bracket.second, tolerance, maxIterations)
                NumericalMethod.NEWTON_RAPHSON -> solveNewtonRaphson(targetFunc, yValueFunc, (bracket.first + bracket.second) / 2.0, tolerance, maxIterations)
                NumericalMethod.BISECTION -> solveBisection(targetFunc, yValueFunc, bracket.first, bracket.second, tolerance, maxIterations)
                NumericalMethod.SECANT -> solveSecant(targetFunc, yValueFunc, bracket.first, bracket.second, tolerance, maxIterations)
            }

            if (root != null && root.residual < tolerance * 100.0) {
                addUniqueRoot(discoveredRoots, root, tolerance * 5.0)
            }
        }

        // Also do a multi-start Newton scan from several seed points to catch tangency / double roots
        val seedCount = 10
        val seedStep = (domainMax - domainMin) / seedCount
        for (k in 0..seedCount) {
            val x0 = domainMin + k * seedStep
            val seedRoot = solveNewtonRaphson(targetFunc, yValueFunc, x0, tolerance, maxIterations)
            if (seedRoot != null && seedRoot.x in domainMin..domainMax && seedRoot.residual < tolerance * 10.0) {
                addUniqueRoot(discoveredRoots, seedRoot, tolerance * 5.0)
            }
        }

        val sortedRoots = discoveredRoots.sortedBy { it.x }
        val executionTime = System.currentTimeMillis() - startTime

        val desc = if (mode == SolverMode.ROOT_FINDING) {
            "Roots for: $fStr = 0"
        } else {
            "Intersections for: $fStr = $gStr"
        }

        return SolverResultState.Success(
            equationDescription = desc,
            roots = sortedRoots,
            searchInterval = Pair(domainMin, domainMax),
            tolerance = tolerance,
            executionTimeMs = executionTime
        )
    }

    private fun addUniqueRoot(list: MutableList<SolvedPoint>, point: SolvedPoint, tol: Double) {
        val alreadyExists = list.any { abs(it.x - point.x) < max(tol, 1e-4) }
        if (!alreadyExists) {
            list.add(point)
        }
    }

    /**
     * Robust Hybrid Brent-Newton Solver combining bisection safeguard with quadratic Newton steps.
     */
    private fun solveHybridBrentNewton(
        f: (Double) -> Double,
        yFunc: (Double) -> Double,
        xA: Double,
        xB: Double,
        tol: Double,
        maxIter: Int
    ): SolvedPoint? {
        var a = xA
        var b = xB
        var fa = try { f(a) } catch (e: Exception) { return null }
        var fb = try { f(b) } catch (e: Exception) { return null }

        if (fa * fb > 0.0) {
            // Fallback to Newton on midpoint
            return solveNewtonRaphson(f, yFunc, (a + b) / 2.0, tol, maxIter)
        }

        val steps = mutableListOf<IterationStep>()
        var c = a
        var fc = fa
        var d = b - a
        var e = d

        for (iter in 1..maxIter) {
            if (fb * fc > 0.0) {
                c = a
                fc = fa
                d = b - a
                e = d
            }
            if (abs(fc) < abs(fb)) {
                a = b
                b = c
                c = a
                fa = fb
                fb = fc
                fc = fa
            }

            val tol1 = 2.0 * 1e-15 * abs(b) + 0.5 * tol
            val xm = 0.5 * (c - b)

            val curStep = IterationStep(iter, b, fb, abs(c - b))
            steps.add(curStep)

            if (abs(xm) <= tol1 || abs(fb) <= tol) {
                val yVal = try { yFunc(b) } catch (e: Exception) { 0.0 }
                return SolvedPoint(b, yVal, abs(fb), iter, NumericalMethod.HYBRID_BRENT_NEWTON, steps)
            }

            if (abs(e) >= tol1 && abs(fa) > abs(fb)) {
                val s = fb / fa
                var p: Double
                var q: Double
                if (a == c) {
                    p = 2.0 * xm * s
                    q = 1.0 - s
                } else {
                    q = fa / fc
                    val r = fb / fc
                    p = s * (2.0 * xm * q * (q - r) - (b - a) * (r - 1.0))
                    q = (q - 1.0) * (r - 1.0) * (s - 1.0)
                }
                if (p > 0.0) q = -q
                p = abs(p)
                if (2.0 * p < min(3.0 * xm * q - abs(tol1 * q), abs(e * q))) {
                    e = d
                    d = p / q
                } else {
                    d = xm
                    e = d
                }
            } else {
                d = xm
                e = d
            }

            a = b
            fa = fb
            if (abs(d) > tol1) {
                b += d
            } else {
                b += if (xm > 0.0) tol1 else -tol1
            }
            fb = try { f(b) } catch (e: Exception) { return null }
        }

        val yVal = try { yFunc(b) } catch (e: Exception) { 0.0 }
        return SolvedPoint(b, yVal, abs(fb), maxIter, NumericalMethod.HYBRID_BRENT_NEWTON, steps)
    }

    /**
     * Newton-Raphson Solver with central difference numerical differentiation:
     * x_{n+1} = x_n - f(x_n) / f'(x_n)
     */
    private fun solveNewtonRaphson(
        f: (Double) -> Double,
        yFunc: (Double) -> Double,
        x0: Double,
        tol: Double,
        maxIter: Int
    ): SolvedPoint? {
        var x = x0
        val steps = mutableListOf<IterationStep>()
        val h = 1e-6

        for (iter in 1..maxIter) {
            val fx = try { f(x) } catch (e: Exception) { return null }
            val fxPlus = try { f(x + h) } catch (e: Exception) { fx }
            val fxMinus = try { f(x - h) } catch (e: Exception) { fx }
            val dfx = (fxPlus - fxMinus) / (2.0 * h)

            val deltaX = if (abs(dfx) > 1e-14) fx / dfx else 0.0
            steps.add(IterationStep(iter, x, fx, abs(deltaX)))

            if (abs(fx) <= tol || abs(deltaX) <= tol) {
                val yVal = try { yFunc(x) } catch (e: Exception) { 0.0 }
                return SolvedPoint(x, yVal, abs(fx), iter, NumericalMethod.NEWTON_RAPHSON, steps)
            }

            if (abs(dfx) < 1e-14) {
                // Zero derivative slope, nudge slightly
                x += 0.1
            } else {
                x -= deltaX
            }
        }

        val finalFx = try { f(x) } catch (e: Exception) { 1.0 }
        if (abs(finalFx) < tol * 100.0) {
            val yVal = try { yFunc(x) } catch (e: Exception) { 0.0 }
            return SolvedPoint(x, yVal, abs(finalFx), maxIter, NumericalMethod.NEWTON_RAPHSON, steps)
        }
        return null
    }

    /**
     * Classic Bisection Method for verified intervals [a, b]
     */
    private fun solveBisection(
        f: (Double) -> Double,
        yFunc: (Double) -> Double,
        xA: Double,
        xB: Double,
        tol: Double,
        maxIter: Int
    ): SolvedPoint? {
        var a = xA
        var b = xB
        var fa = try { f(a) } catch (e: Exception) { return null }
        var fb = try { f(b) } catch (e: Exception) { return null }

        if (fa * fb > 0.0) return null

        val steps = mutableListOf<IterationStep>()
        var mid = (a + b) / 2.0
        var fMid = 0.0

        for (iter in 1..maxIter) {
            mid = (a + b) / 2.0
            fMid = try { f(mid) } catch (e: Exception) { return null }
            val halfWidth = (b - a) / 2.0

            steps.add(IterationStep(iter, mid, fMid, halfWidth))

            if (abs(fMid) <= tol || halfWidth <= tol) {
                val yVal = try { yFunc(mid) } catch (e: Exception) { 0.0 }
                return SolvedPoint(mid, yVal, abs(fMid), iter, NumericalMethod.BISECTION, steps)
            }

            if (fa * fMid < 0.0) {
                b = mid
                fb = fMid
            } else {
                a = mid
                fa = fMid
            }
        }

        val yVal = try { yFunc(mid) } catch (e: Exception) { 0.0 }
        return SolvedPoint(mid, yVal, abs(fMid), maxIter, NumericalMethod.BISECTION, steps)
    }

    /**
     * Secant Method:
     * x_{n+1} = x_n - f(x_n) * (x_n - x_{n-1}) / (f(x_n) - f(x_{n-1}))
     */
    private fun solveSecant(
        f: (Double) -> Double,
        yFunc: (Double) -> Double,
        x0: Double,
        x1: Double,
        tol: Double,
        maxIter: Int
    ): SolvedPoint? {
        var p0 = x0
        var p1 = x1
        var q0 = try { f(p0) } catch (e: Exception) { return null }
        var q1 = try { f(p1) } catch (e: Exception) { return null }

        val steps = mutableListOf<IterationStep>()

        for (iter in 1..maxIter) {
            if (abs(q1 - q0) < 1e-15) break
            val p = p1 - q1 * (p1 - p0) / (q1 - q0)
            val fp = try { f(p) } catch (e: Exception) { return null }
            val delta = abs(p - p1)

            steps.add(IterationStep(iter, p, fp, delta))

            if (abs(fp) <= tol || delta <= tol) {
                val yVal = try { yFunc(p) } catch (e: Exception) { 0.0 }
                return SolvedPoint(p, yVal, abs(fp), iter, NumericalMethod.SECANT, steps)
            }

            p0 = p1
            q0 = q1
            p1 = p
            q1 = fp
        }

        if (abs(q1) < tol * 100.0) {
            val yVal = try { yFunc(p1) } catch (e: Exception) { 0.0 }
            return SolvedPoint(p1, yVal, abs(q1), maxIter, NumericalMethod.SECANT, steps)
        }
        return null
    }
}
