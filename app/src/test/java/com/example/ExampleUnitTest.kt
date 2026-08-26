package com.example

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.model.CablePreset
import com.example.model.CatenaryCalculation
import com.example.model.HyperbolicFunc
import com.example.model.ParabolaMode
import com.example.ui.HyperbolicViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.math.cosh
import kotlin.math.sinh

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleUnitTest {

    @Test
    fun testCatenaryParameterScaling() {
        // Tension T0 = 20,000 N, mass density = 2.0 kg/m, g = 9.80665 m/s²
        // w = 2.0 * 9.80665 = 19.6133 N/m
        // a = T0 / w = 20000 / 19.6133 = 1019.7162 m
        val calc = CatenaryCalculation(
            horizontalTensionN = 20000.0,
            spanM = 200.0,
            linearMassDensityKgPerM = 2.0
        )
        val expectedW = 2.0 * 9.80665
        val expectedA = 20000.0 / expectedW

        assertEquals(expectedW, calc.linearWeightDensityNpm, 1e-4)
        assertEquals(expectedA, calc.parameterA, 1e-4)
    }

    @Test
    fun testCatenaryHyperbolicCosineEquation() {
        val calc = CatenaryCalculation(
            horizontalTensionN = 10000.0,
            spanM = 100.0,
            linearMassDensityKgPerM = 1.0
        )
        val a = calc.parameterA

        // y(x) = a * cosh(x / a)
        // At x = 0 (lowest point / vertex): y(0) = a * cosh(0) = a * 1 = a
        assertEquals(a, calc.evaluateY(0.0), 1e-6)

        // At x = 50 (right support): y(50) = a * cosh(50 / a)
        val expectedY50 = a * cosh(50.0 / a)
        assertEquals(expectedY50, calc.evaluateY(50.0), 1e-6)

        // Symmetry: y(-50) == y(50)
        assertEquals(calc.evaluateY(-50.0), calc.evaluateY(50.0), 1e-6)
    }

    @Test
    fun testCatenarySagAndArcLength() {
        val calc = CatenaryCalculation(
            horizontalTensionN = 25000.0,
            spanM = 300.0,
            linearMassDensityKgPerM = 1.5
        )
        val a = calc.parameterA
        val halfL = 150.0

        // h = a * (cosh(L / 2a) - 1)
        val expectedSag = a * (cosh(halfL / a) - 1.0)
        assertEquals(expectedSag, calc.maxSagM, 1e-5)

        // S = 2 * a * sinh(L / 2a)
        val expectedArcLength = 2.0 * a * sinh(halfL / a)
        assertEquals(expectedArcLength, calc.arcLengthM, 1e-5)

        // Cable arc length S must be strictly greater than horizontal span L
        assertTrue(calc.arcLengthM > calc.spanM)
        assertTrue(calc.slackPercent > 0.0)
    }

    @Test
    fun testCatenaryTensionAndReactions() {
        val calc = CatenaryCalculation(
            horizontalTensionN = 30000.0,
            spanM = 250.0,
            linearMassDensityKgPerM = 2.0
        )
        val a = calc.parameterA
        val halfL = 125.0

        // Maximum tension at support: T_max = T0 * cosh(L / 2a) = T0 + w * h
        val expectedTmax = 30000.0 * cosh(halfL / a)
        val expectedTmaxEnergy = 30000.0 + calc.linearWeightDensityNpm * calc.maxSagM
        assertEquals(expectedTmax, calc.maxTensionN, 1e-4)
        assertEquals(expectedTmaxEnergy, calc.maxTensionN, 1e-4)

        // Vertical reaction: V = T0 * sinh(L / 2a) = (w * S) / 2
        val expectedV = 30000.0 * sinh(halfL / a)
        val expectedVWeight = (calc.linearWeightDensityNpm * calc.arcLengthM) / 2.0
        assertEquals(expectedV, calc.verticalReactionN, 1e-4)
        assertEquals(expectedVWeight, calc.verticalReactionN, 1e-4)
    }

    @Test
    fun testViewModelCatenaryCalculations() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val vm = HyperbolicViewModel(app)

        // Test default state calculation
        val initialCalc = vm.uiState.value.catenaryCalculation
        assertNotNull(initialCalc)
        assertTrue(initialCalc.parameterA > 0.0)

        // Update cable tension
        vm.updateCableTension(50000.0)
        assertEquals(50000.0, vm.uiState.value.catenaryCalculation.horizontalTensionN, 1e-6)

        // Update cable span
        vm.updateCableSpan(400.0)
        assertEquals(400.0, vm.uiState.value.catenaryCalculation.spanM, 1e-6)

        // Update linear mass density
        vm.updateCableMassDensity(3.5)
        assertEquals(3.5, vm.uiState.value.catenaryCalculation.linearMassDensityKgPerM, 1e-6)

        // Test preset application
        vm.applyCablePreset(CablePreset.SUSPENSION_BRIDGE)
        assertEquals(CablePreset.SUSPENSION_BRIDGE.tensionN, vm.uiState.value.catenaryCalculation.horizontalTensionN, 1e-6)
        assertEquals(CablePreset.SUSPENSION_BRIDGE.spanM, vm.uiState.value.catenaryCalculation.spanM, 1e-6)
        assertEquals(CablePreset.SUSPENSION_BRIDGE.massKgPerM, vm.uiState.value.catenaryCalculation.linearMassDensityKgPerM, 1e-6)

        // Test point calculation
        val point = vm.calculateCatenaryPoint(0.0)
        assertEquals(0.0, point.x, 1e-6)
        assertEquals(0.0, point.sag, 1e-6)
        assertEquals(0.0, point.slope, 1e-6)

        // Test curve sample generation
        val curve = vm.getSampledCatenaryCurve(50)
        assertEquals(51, curve.size)
    }

    @Test
    fun testCatenaryExactArcLengthSagInvariant() {
        val calc = CatenaryCalculation(
            horizontalTensionN = 40000.0,
            spanM = 350.0,
            linearMassDensityKgPerM = 2.5
        )
        val a = calc.parameterA
        val h = calc.maxSagM
        val S = calc.arcLengthM

        // Derived invariant: S = 2 * sqrt(2*a*h + h^2)
        val expectedS = 2.0 * kotlin.math.sqrt(2.0 * a * h + h * h)
        assertEquals(expectedS, S, 1e-4)
    }

    @Test
    fun testCalculusDerivatives() {
        // Verify d/dx[sinh(x)] = cosh(x) and d/dx[cosh(x)] = sinh(x) using finite differences
        val x = 1.25
        val dx = 1e-6
        val dSinh = (sinh(x + dx) - sinh(x - dx)) / (2 * dx)
        val dCosh = (cosh(x + dx) - cosh(x - dx)) / (2 * dx)

        assertEquals(cosh(x), dSinh, 1e-5)
        assertEquals(sinh(x), dCosh, 1e-5)
    }

    @Test
    fun testParabolaComparisonAndToggle() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val vm = HyperbolicViewModel(app)
        assertEquals(false, vm.uiState.value.showParabolaComparison)

        vm.toggleParabolaComparison()
        assertEquals(true, vm.uiState.value.showParabolaComparison)
        assertTrue(vm.uiState.value.activeFunctions.contains(HyperbolicFunc.COSH))

        // Test standard parabola y = x²
        vm.setParabolaMode(ParabolaMode.STANDARD_X_SQUARED)
        assertEquals(4.0, vm.evaluateParabola(2.0), 1e-6)
        assertEquals(0.0, vm.evaluateParabola(0.0), 1e-6)

        // Test Taylor series y = 1 + x²/2
        vm.setParabolaMode(ParabolaMode.TAYLOR_SERIES)
        assertEquals(1.0, vm.evaluateParabola(0.0), 1e-6)
        assertEquals(3.0, vm.evaluateParabola(2.0), 1e-6)

        // At x = 0.5, cosh(0.5) is very close to 1 + (0.5)^2/2 = 1.125
        val coshVal = cosh(0.5)
        val taylorVal = vm.evaluateParabola(0.5, ParabolaMode.TAYLOR_SERIES)
        assertTrue(kotlin.math.abs(coshVal - taylorVal) < 0.01)

        // As x becomes larger, cosh(x) diverges exponentially from parabola
        val coshLarge = cosh(4.0)
        val paraLarge = vm.evaluateParabola(4.0, ParabolaMode.STANDARD_X_SQUARED)
        assertTrue(coshLarge > paraLarge)
    }

    @Test
    fun testPhysicsHandbookCurriculumCompleteness() {
        val topics = com.example.model.PhysicsHandbookRepository.allTopics
        assertTrue(topics.isNotEmpty())

        // Check that all grades 6 to 12 are represented
        com.example.model.PhysicsGrade.values().forEach { grade ->
            val gradeTopics = topics.filter { it.grade == grade }
            assertTrue("Class ${grade.displayName} must have topics", gradeTopics.isNotEmpty())
        }

        // Verify formulas and interactive calculators
        val formulasWithCalculators = topics.flatMap { it.keyFormulas }.filter { it.canCalculate && it.calculateFn != null }
        assertTrue("Expected multiple interactive formula calculators", formulasWithCalculators.size >= 10)

        // Test an interactive calculator: Newton's second law F = m * a
        val newtonTopic = topics.find { it.id == "c9_laws_of_motion" }
        assertNotNull(newtonTopic)
        val fEqualsMa = newtonTopic!!.keyFormulas.find { it.title.contains("Second Law") }
        assertNotNull(fEqualsMa)
        val calcFn = fEqualsMa!!.calculateFn
        assertNotNull(calcFn)
        val resultForce = calcFn!!.invoke(mapOf("Mass m (kg)" to 10.0, "Acceleration a (m/s²)" to 9.8))
        assertEquals(98.0, resultForce, 1e-4)

        // Test Ohm's law V = I * R
        val ohmTopic = topics.find { it.id == "c10_electricity" }
        assertNotNull(ohmTopic)
        val ohmFormula = ohmTopic!!.keyFormulas.find { it.title.contains("Ohm") }
        assertNotNull(ohmFormula)
        val voltResult = ohmFormula!!.calculateFn?.invoke(mapOf("Current I (A)" to 2.5, "Resistance R (Ω)" to 10.0))
        assertEquals(25.0, voltResult!!, 1e-4)
    }

    @Test
    fun testMathHandbookCurriculumCompleteness() {
        val topics = com.example.model.MathHandbookRepository.allTopics
        assertTrue("Math handbook topics should not be empty", topics.isNotEmpty())

        // Check that all grades 6 to 12 are represented
        com.example.model.MathGrade.values().forEach { grade ->
            val gradeTopics = topics.filter { it.grade == grade }
            assertTrue("Class ${grade.displayName} must have math topics", gradeTopics.isNotEmpty())
        }

        // Check that key math branches are represented
        com.example.model.MathBranch.values().forEach { branch ->
            val branchTopics = topics.filter { it.branch == branch }
            assertTrue("Math branch ${branch.displayName} must have topics", branchTopics.isNotEmpty())
        }

        // Verify formulas and interactive calculators
        val formulasWithCalculators = topics.flatMap { it.keyFormulas }.filter { it.canCalculate && it.calculateFn != null }
        assertTrue("Expected multiple interactive math formula calculators", formulasWithCalculators.size >= 10)

        // Test an interactive calculator: Pythagoras theorem c = √(a² + b²)
        val pythagorasTopic = topics.find { it.id == "m7_triangles_properties" }
        assertNotNull(pythagorasTopic)
        val pythagorasFormula = pythagorasTopic!!.keyFormulas.find { it.title.contains("Pythagoras") }
        assertNotNull(pythagorasFormula)
        val pythFn = pythagorasFormula!!.calculateFn
        assertNotNull(pythFn)
        val hypotenuse = pythFn!!.invoke(mapOf("Leg a" to 3.0, "Leg b" to 4.0))
        assertEquals(5.0, hypotenuse, 1e-4)

        // Test Heron's formula for a 3-4-5 right triangle: s = 6, area = √(6*3*2*1) = 6
        val heronTopic = topics.find { it.id == "m9_herons_formula" }
        assertNotNull(heronTopic)
        val heronFormula = heronTopic!!.keyFormulas.find { it.title.contains("Heron") }
        assertNotNull(heronFormula)
        val heronArea = heronFormula!!.calculateFn?.invoke(mapOf("Side a" to 3.0, "Side b" to 4.0, "Side c" to 5.0))
        assertEquals(6.0, heronArea!!, 1e-4)

        // Test Simple Interest formula SI = (P * R * T) / 100
        val siTopic = topics.find { it.id == "m7_commercial_math" }
        assertNotNull(siTopic)
        val siFormula = siTopic!!.keyFormulas.find { it.title.contains("Simple Interest") }
        assertNotNull(siFormula)
        val interest = siFormula!!.calculateFn?.invoke(mapOf("Principal P" to 1000.0, "Rate R (%)" to 5.0, "Time T (yrs)" to 2.0))
        assertEquals(100.0, interest!!, 1e-4)
    }
}


