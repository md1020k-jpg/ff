package com.example.model

import kotlin.math.*

enum class MathGrade(val displayName: String, val level: String) {
    CLASS_6("Class 6", "Middle School Foundation"),
    CLASS_7("Class 7", "Pre-Algebra & Geometry"),
    CLASS_8("Class 8", "Algebra & Solid Mensuration"),
    CLASS_9("Class 9", "Secondary Mathematics I"),
    CLASS_10("Class 10", "Secondary Board Essentials"),
    CLASS_11("Class 11", "Higher Secondary Fundamentals"),
    CLASS_12("Class 12", "Advanced Senior Secondary")
}

enum class MathBranch(val displayName: String) {
    ALGEBRA("Algebra & Numbers"),
    GEOMETRY("Geometry & Euclidean"),
    MENSURATION("Mensuration & Solids"),
    TRIGONOMETRY("Trigonometry"),
    COORDINATE_GEOMETRY("Coordinate Geometry"),
    CALCULUS("Calculus & Limits"),
    VECTORS_MATRICES("Vectors & Matrices"),
    PROBABILITY_STATS("Probability & Statistics"),
    SETS_RELATIONS("Sets, Relations & Logic")
}

data class MathFormula(
    val title: String,
    val formulaLatex: String,
    val quickDescription: String,
    val symbols: Map<String, String> = emptyMap(),
    val canCalculate: Boolean = false,
    val defaultInputs: Map<String, Double> = emptyMap(),
    val calculateFn: ((Map<String, Double>) -> Double)? = null,
    val outputUnit: String = "",
    val outputSymbol: String = "Result"
)

data class MathTopic(
    val id: String,
    val grade: MathGrade,
    val branch: MathBranch,
    val chapterTitle: String,
    val topicTitle: String,
    val summary: String,
    val detailedExplanation: String,
    val keyFormulas: List<MathFormula>,
    val importantIdentitiesAndTheorems: List<String>,
    val realWorldApplications: List<String>
)

object MathHandbookRepository {

    val allTopics: List<MathTopic> = listOf(
        // ==========================================
        // CLASS 6
        // ==========================================
        MathTopic(
            id = "m6_numbers_hcf_lcm",
            grade = MathGrade.CLASS_6,
            branch = MathBranch.ALGEBRA,
            chapterTitle = "Playing with Numbers & Arithmetic",
            topicTitle = "Divisibility, Factors, HCF & LCM",
            summary = "Fundamental concepts of prime/composite numbers, prime factorisation, Highest Common Factor (HCF), Lowest Common Multiple (LCM), and divisibility rules.",
            detailedExplanation = "Factors are numbers that divide a given number exactly with zero remainder. Prime numbers have exactly 2 factors (1 and itself), while composite numbers have more than 2 factors. The Fundamental Theorem of Arithmetic implies that every composite number can be uniquely expressed as a product of prime numbers. For any two positive integers a and b, the product of their HCF and LCM is strictly equal to the product of the numbers: HCF(a,b) × LCM(a,b) = a × b.",
            keyFormulas = listOf(
                MathFormula(
                    title = "HCF and LCM Product Relationship",
                    formulaLatex = "HCF(a, b) \\times LCM(a, b) = a \\times b",
                    quickDescription = "The product of the HCF and LCM of two numbers equals the product of the two numbers.",
                    symbols = mapOf("a, b" to "Two positive integers", "HCF" to "Highest Common Factor", "LCM" to "Least Common Multiple"),
                    canCalculate = true,
                    defaultInputs = mapOf("Number a" to 12.0, "Number b" to 18.0),
                    calculateFn = { inputs ->
                        val a = inputs["Number a"]?.toLong() ?: 12L
                        val b = inputs["Number b"]?.toLong() ?: 18L
                        fun gcd(x: Long, y: Long): Long = if (y == 0L) x else gcd(y, x % y)
                        val g = gcd(a, b)
                        val l = (a * b) / g
                        l.toDouble() // Returns LCM
                    },
                    outputUnit = "",
                    outputSymbol = "LCM(a, b)"
                ),
                MathFormula(
                    title = "BODMAS Order of Operations",
                    formulaLatex = "B \\to O \\to D \\to M \\to A \\to S",
                    quickDescription = "Brackets > Orders (powers/roots) > Division > Multiplication > Addition > Subtraction.",
                    symbols = mapOf("B" to "Brackets (), {}, []", "O" to "Orders / Of", "D/M" to "Division/Multiplication (left to right)", "A/S" to "Addition/Subtraction (left to right)")
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Divisibility rule by 2: Last digit is even (0, 2, 4, 6, 8)",
                "Divisibility rule by 3: Sum of all digits is a multiple of 3",
                "Divisibility rule by 4: Last two digits form a number divisible by 4",
                "Divisibility rule by 9: Sum of all digits is divisible by 9",
                "Divisibility rule by 11: Difference between sum of odd-place digits and even-place digits is 0 or divisible by 11"
            ),
            realWorldApplications = listOf(
                "Scheduling recurring events, bus time synchronization, and gear ratio cycles using LCM",
                "Tile layout dimensions and equal distribution packing using HCF",
                "Computer cryptography encryption keys based on prime factorisation"
            )
        ),
        MathTopic(
            id = "m6_mensuration_basics",
            grade = MathGrade.CLASS_6,
            branch = MathBranch.MENSURATION,
            chapterTitle = "Mensuration & Geometry Fundamentals",
            topicTitle = "Perimeter & Area of 2D Polygons",
            summary = "Calculations of boundaries (perimeters) and enclosed region surfaces (areas) for standard rectangles and regular polygons.",
            detailedExplanation = "Perimeter represents the continuous line forming the boundary of a closed geometric figure. For a rectangle with length l and breadth b, the perimeter is P = 2(l + b). Area measures the amount of space inside the boundary in square units, given by A = l × b for a rectangle and A = s² for a square with side s.",
            keyFormulas = listOf(
                MathFormula(
                    title = "Perimeter of Rectangle",
                    formulaLatex = "P = 2 \\times (l + b)",
                    quickDescription = "Total boundary length of a rectangular boundary.",
                    symbols = mapOf("l" to "Length of rectangle (m)", "b" to "Breadth of rectangle (m)"),
                    canCalculate = true,
                    defaultInputs = mapOf("Length l" to 8.0, "Breadth b" to 5.0),
                    calculateFn = { inputs ->
                        val l = inputs["Length l"] ?: 8.0
                        val b = inputs["Breadth b"] ?: 5.0
                        2.0 * (l + b)
                    },
                    outputUnit = "units",
                    outputSymbol = "Perimeter P"
                ),
                MathFormula(
                    title = "Area of Rectangle",
                    formulaLatex = "A = l \\times b",
                    quickDescription = "Two-dimensional surface enclosed by length and breadth.",
                    symbols = mapOf("l" to "Length", "b" to "Breadth"),
                    canCalculate = true,
                    defaultInputs = mapOf("Length l" to 8.0, "Breadth b" to 5.0),
                    calculateFn = { inputs ->
                        val l = inputs["Length l"] ?: 8.0
                        val b = inputs["Breadth b"] ?: 5.0
                        l * b
                    },
                    outputUnit = "sq units",
                    outputSymbol = "Area A"
                ),
                MathFormula(
                    title = "Perimeter & Area of Square",
                    formulaLatex = "P = 4s, \\quad A = s^2",
                    quickDescription = "Equilateral quadrilateral perimeter and square area.",
                    symbols = mapOf("s" to "Side length of the square")
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Perimeter of any regular polygon with n sides: P = n × s",
                "Conversion: 1 m² = 10,000 cm²",
                "Conversion: 1 hectare = 10,000 m²"
            ),
            realWorldApplications = listOf(
                "Fencing agricultural plots and boundary wall cost estimation",
                "Flooring tiles, wall painting, and room carpet area requirement planning"
            )
        ),
        MathTopic(
            id = "m6_ratio_proportion",
            grade = MathGrade.CLASS_6,
            branch = MathBranch.ALGEBRA,
            chapterTitle = "Ratio, Proportion & Unitary Method",
            topicTitle = "Direct Comparisons & Proportional Equivalence",
            summary = "Comparative magnitude relations between two quantities of the same unit and proportional equality ad = bc.",
            detailedExplanation = "A ratio a:b compares two quantities by division. A proportion is an equality of two ratios: a/b = c/d (written as a:b :: c:d), where a and d are extremes, and b and c are means. The cross-product property states that the product of extremes equals the product of means: a × d = b × c.",
            keyFormulas = listOf(
                MathFormula(
                    title = "Cross-Product Rule of Proportion",
                    formulaLatex = "a : b = c : d \\iff a \\times d = b \\times c",
                    quickDescription = "Product of extremes equals product of means.",
                    symbols = mapOf("a, d" to "Extremes", "b, c" to "Means"),
                    canCalculate = true,
                    defaultInputs = mapOf("Term a" to 2.0, "Term b" to 4.0, "Term c" to 6.0),
                    calculateFn = { inputs ->
                        val a = inputs["Term a"] ?: 2.0
                        val b = inputs["Term b"] ?: 4.0
                        val c = inputs["Term c"] ?: 6.0
                        (b * c) / a // Returns d such that a/b = c/d
                    },
                    outputUnit = "",
                    outputSymbol = "Fourth Proportional d"
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Two ratios are in proportion if and only if cross-products are equal",
                "Unitary method: Calculate the value of 1 unit first by division, then find required units by multiplication"
            ),
            realWorldApplications = listOf(
                "Architectural map scales and blueprint drawing ratios",
                "Chemical solution dilutions, cooking recipes, and currency exchange conversions"
            )
        ),

        // ==========================================
        // CLASS 7
        // ==========================================
        MathTopic(
            id = "m7_exponents_powers",
            grade = MathGrade.CLASS_7,
            branch = MathBranch.ALGEBRA,
            chapterTitle = "Exponents, Powers & Scientific Notation",
            topicTitle = "Laws of Indices and Exponential Arithmetic",
            summary = "Rules governing powers with base numbers: multiplication, division, power of powers, zero exponent, and negative exponent definitions.",
            detailedExplanation = "Exponential notation represents repeated multiplication: a^n = a × a × ... × a (n times). The fundamental laws of indices allow algebraic simplification across scientific, engineering, and astronomical calculations.",
            keyFormulas = listOf(
                MathFormula(
                    title = "Product Law of Exponents",
                    formulaLatex = "a^m \\times a^n = a^{m+n}",
                    quickDescription = "When multiplying powers with identical bases, add the exponents.",
                    symbols = mapOf("a" to "Base number (a ≠ 0)", "m, n" to "Integer exponents")
                ),
                MathFormula(
                    title = "Quotient Law of Exponents",
                    formulaLatex = "\\frac{a^m}{a^n} = a^{m-n}",
                    quickDescription = "When dividing powers with identical bases, subtract the exponents.",
                    symbols = mapOf("a" to "Base number", "m, n" to "Exponents")
                ),
                MathFormula(
                    title = "Power of a Power & Zero Power",
                    formulaLatex = "(a^m)^n = a^{m \\cdot n}, \\quad a^0 = 1, \\quad a^{-n} = \\frac{1}{a^n}",
                    quickDescription = "Power multiplied in nested powers; any non-zero number to power 0 is 1.",
                    symbols = mapOf("a" to "Non-zero base")
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "a^m × b^m = (a × b)^m",
                "a^m / b^m = (a / b)^m",
                "(-1)^n = 1 if n is even, and -1 if n is odd"
            ),
            realWorldApplications = listOf(
                "Scientific notation for interstellar distances (e.g. speed of light 3 × 10⁸ m/s)",
                "Computer memory byte addressing (2¹⁰ = 1024 bytes = 1 KB, 2²⁰ = 1 MB)"
            )
        ),
        MathTopic(
            id = "m7_triangles_properties",
            grade = MathGrade.CLASS_7,
            branch = MathBranch.GEOMETRY,
            chapterTitle = "The Triangle and Its Properties",
            topicTitle = "Angle Sum, Exterior Angles & Pythagoras Theorem",
            summary = "Fundamental angle relations in planar triangles, the exterior angle theorem, triangle inequalities, and Pythagoras theorem for right-angled triangles.",
            detailedExplanation = "The sum of interior angles in any Euclidean triangle is always 180°. An exterior angle is equal to the sum of its two interior opposite angles. In a right-angled triangle, the square on the hypotenuse is equal to the sum of the squares on the other two legs: a² + b² = c².",
            keyFormulas = listOf(
                MathFormula(
                    title = "Pythagoras Theorem",
                    formulaLatex = "c^2 = a^2 + b^2 \\implies c = \\sqrt{a^2 + b^2}",
                    quickDescription = "Hypotenuse length calculated from base and perpendicular legs.",
                    symbols = mapOf("a" to "Perpendicular leg length", "b" to "Base leg length", "c" to "Hypotenuse"),
                    canCalculate = true,
                    defaultInputs = mapOf("Leg a" to 3.0, "Leg b" to 4.0),
                    calculateFn = { inputs ->
                        val a = inputs["Leg a"] ?: 3.0
                        val b = inputs["Leg b"] ?: 4.0
                        sqrt(a * a + b * b)
                    },
                    outputUnit = "units",
                    outputSymbol = "Hypotenuse c"
                ),
                MathFormula(
                    title = "Interior Angle Sum Property",
                    formulaLatex = "\\angle A + \\angle B + \\angle C = 180^\\circ",
                    quickDescription = "Sum of internal angles of any triangle equals two right angles (180 degrees)."
                ),
                MathFormula(
                    title = "Exterior Angle Property",
                    formulaLatex = "\\angle \\text{Ext} = \\angle \\text{Opp}_1 + \\angle \\text{Opp}_2",
                    quickDescription = "Exterior angle equals the sum of interior opposite angles."
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Triangle Inequality: Sum of the lengths of any two sides of a triangle is strictly greater than the length of the third side",
                "Difference of any two sides is strictly less than the third side",
                "SSS, SAS, ASA, and RHS congruence criteria for triangles"
            ),
            realWorldApplications = listOf(
                "Triangulation in GPS navigation and surveying land topography",
                "Structural trusses in bridges, roof frames, and crane cranes for rigidity"
            )
        ),
        MathTopic(
            id = "m7_commercial_math",
            grade = MathGrade.CLASS_7,
            branch = MathBranch.ALGEBRA,
            chapterTitle = "Comparing Quantities & Commercial Math",
            topicTitle = "Percentages, Profit & Loss, Simple Interest",
            summary = "Financial arithmetic covering percentage increase/decrease, profit/loss percentage on cost price, and simple interest calculations.",
            detailedExplanation = "Profit or loss percentage is always calculated on the Cost Price (CP). Simple Interest (SI) is accrued strictly on the principal sum over time at a constant annual rate: SI = (P × R × T) / 100. Total maturity amount is A = P + SI.",
            keyFormulas = listOf(
                MathFormula(
                    title = "Simple Interest Formula",
                    formulaLatex = "SI = \\frac{P \\times R \\times T}{100}, \\quad A = P + SI",
                    quickDescription = "Simple interest generated on principal amount P at rate R% over T years.",
                    symbols = mapOf("P" to "Principal amount ($)", "R" to "Annual interest rate (%)", "T" to "Time period (years)", "A" to "Maturity amount ($)"),
                    canCalculate = true,
                    defaultInputs = mapOf("Principal P" to 5000.0, "Rate R (%)" to 6.5, "Time T (yrs)" to 3.0),
                    calculateFn = { inputs ->
                        val p = inputs["Principal P"] ?: 5000.0
                        val r = inputs["Rate R (%)"] ?: 6.5
                        val t = inputs["Time T (yrs)"] ?: 3.0
                        (p * r * t) / 100.0
                    },
                    outputUnit = "$",
                    outputSymbol = "Simple Interest SI"
                ),
                MathFormula(
                    title = "Profit & Loss Percentages",
                    formulaLatex = "\\text{Profit \\%} = \\frac{\\text{Profit}}{\\text{CP}} \\times 100, \\quad \\text{Loss \\%} = \\frac{\\text{Loss}}{\\text{CP}} \\times 100",
                    quickDescription = "Profit (SP - CP) or Loss (CP - SP) percentage computed relative to Cost Price."
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Selling Price with Profit: SP = CP × (100 + Profit %) / 100",
                "Selling Price with Loss: SP = CP × (100 - Loss %) / 100",
                "Percentage change = (Absolute Change / Original Value) × 100"
            ),
            realWorldApplications = listOf(
                "Short-term bank loans, micro-credit deposits, and fixed savings interest",
                "Retail commerce pricing, wholesale discounts, and margin analysis"
            )
        ),
        MathTopic(
            id = "m7_circle_area_perimeter",
            grade = MathGrade.CLASS_7,
            branch = MathBranch.MENSURATION,
            chapterTitle = "Perimeter and Area of Plane Figures",
            topicTitle = "Circles, Triangles & Parallelograms",
            summary = "Circumference C = 2πr, circle area A = πr², parallelogram area A = bh, and triangle area A = ½bh.",
            detailedExplanation = "The ratio of a circle's circumference to its diameter is the mathematical constant π ≈ 3.14159265... Area of a circle is derived by decomposing it into infinitesimally thin concentric rings or sector slices, yielding A = πr².",
            keyFormulas = listOf(
                MathFormula(
                    title = "Circle Circumference & Area",
                    formulaLatex = "C = 2\\pi r, \\quad A = \\pi r^2",
                    quickDescription = "Perimeter (circumference) and enclosed area of a circle with radius r.",
                    symbols = mapOf("r" to "Radius of circle (m)", "\\pi" to "Pi constant ≈ 3.14159"),
                    canCalculate = true,
                    defaultInputs = mapOf("Radius r" to 7.0),
                    calculateFn = { inputs ->
                        val r = inputs["Radius r"] ?: 7.0
                        PI * r * r
                    },
                    outputUnit = "sq units",
                    outputSymbol = "Circle Area A"
                ),
                MathFormula(
                    title = "Area of Triangle & Parallelogram",
                    formulaLatex = "A_{\\text{triangle}} = \\frac{1}{2} b h, \\quad A_{\\text{parallelogram}} = b h",
                    quickDescription = "Triangle area is half the base times perpendicular altitude."
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Area of path/circular ring between concentric circles: A = π(R² - r²)",
                "Circumference ratio: C₁/C₂ = r₁/r₂; Area ratio: A₁/A₂ = (r₁/r₂)²"
            ),
            realWorldApplications = listOf(
                "Circular racetrack dimensions, pipe flow cross-section, and irrigation radius",
                "Agricultural crop land area calculation and seed quantity budgeting"
            )
        ),

        // ==========================================
        // CLASS 8
        // ==========================================
        MathTopic(
            id = "m8_algebraic_identities",
            grade = MathGrade.CLASS_8,
            branch = MathBranch.ALGEBRA,
            chapterTitle = "Algebraic Expressions & Identities",
            topicTitle = "Fundamental Standard Quadratic Algebraic Identities",
            summary = "Core identities for expanding and factorising binomials: (a+b)², (a-b)², (a+b)(a-b), and (x+a)(x+b).",
            detailedExplanation = "An algebraic identity is an equality that remains true for all possible values of its variables. These 4 standard quadratic identities form the bedrock of polynomial factorisation, mental arithmetic, and algebraic problem-solving.",
            keyFormulas = listOf(
                MathFormula(
                    title = "Square of a Sum (Binomial Expansion)",
                    formulaLatex = "(a + b)^2 = a^2 + 2ab + b^2",
                    quickDescription = "Expansion of sum squared.",
                    symbols = mapOf("a, b" to "Real numbers or algebraic terms"),
                    canCalculate = true,
                    defaultInputs = mapOf("Term a" to 10.0, "Term b" to 3.0),
                    calculateFn = { inputs ->
                        val a = inputs["Term a"] ?: 10.0
                        val b = inputs["Term b"] ?: 3.0
                        (a + b).pow(2)
                    },
                    outputUnit = "",
                    outputSymbol = "(a+b)²"
                ),
                MathFormula(
                    title = "Square of a Difference",
                    formulaLatex = "(a - b)^2 = a^2 - 2ab + b^2",
                    quickDescription = "Expansion of difference squared."
                ),
                MathFormula(
                    title = "Difference of Two Squares",
                    formulaLatex = "a^2 - b^2 = (a + b)(a - b)",
                    quickDescription = "Factorisation of difference between two perfect squares."
                ),
                MathFormula(
                    title = "Product of Binomials with Common Term",
                    formulaLatex = "(x + a)(x + b) = x^2 + (a + b)x + ab",
                    quickDescription = "General product expansion for linear binomials."
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "(a + b)² + (a - b)² = 2(a² + b²)",
                "(a + b)² - (a - b)² = 4ab",
                "(a + 1/a)² = a² + 1/a² + 2"
            ),
            realWorldApplications = listOf(
                "Mental calculation shortcuts (e.g., 103² = (100+3)² = 10000 + 600 + 9 = 10609)",
                "Difference of squares for rapid multiplication (e.g., 47 × 53 = (50-3)(50+3) = 2500 - 9 = 2491)"
            )
        ),
        MathTopic(
            id = "m8_compound_interest",
            grade = MathGrade.CLASS_8,
            branch = MathBranch.ALGEBRA,
            chapterTitle = "Comparing Quantities & Financial Math",
            topicTitle = "Compound Interest & Exponential Growth",
            summary = "Formulas for compound interest compounded annually, semi-annually, and quarterly, plus population depreciation.",
            detailedExplanation = "Unlike simple interest, compound interest earns interest on previously accumulated interest. The maturity amount after n conversion periods with interest rate r% per period is A = P(1 + r/100)^n. Compound Interest CI = A - P.",
            keyFormulas = listOf(
                MathFormula(
                    title = "Compound Interest Amount",
                    formulaLatex = "A = P \\left(1 + \\frac{r}{100}\\right)^n, \\quad CI = A - P",
                    quickDescription = "Maturity amount when interest is compounded annually.",
                    symbols = mapOf("P" to "Principal sum ($)", "r" to "Annual interest rate (%)", "n" to "Number of years / periods"),
                    canCalculate = true,
                    defaultInputs = mapOf("Principal P" to 10000.0, "Annual Rate r (%)" to 8.0, "Years n" to 5.0),
                    calculateFn = { inputs ->
                        val p = inputs["Principal P"] ?: 10000.0
                        val r = inputs["Annual Rate r (%)"] ?: 8.0
                        val n = inputs["Years n"] ?: 5.0
                        p * (1.0 + r / 100.0).pow(n)
                    },
                    outputUnit = "$",
                    outputSymbol = "Total Amount A"
                ),
                MathFormula(
                    title = "Half-Yearly Compounding",
                    formulaLatex = "A = P \\left(1 + \\frac{r/2}{100}\\right)^{2n}",
                    quickDescription = "Interest compounded semi-annually (rate halved, periods doubled)."
                ),
                MathFormula(
                    title = "Depreciation Formula",
                    formulaLatex = "V = V_0 \\left(1 - \\frac{r}{100}\\right)^n",
                    quickDescription = "Decay value of an asset depreciating at r% per annum."
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Continuous compounding limit: A = P · e^(rt/100)",
                "Rule of 72: Approximate years to double money ≈ 72 / Interest Rate (%)"
            ),
            realWorldApplications = listOf(
                "Long-term mutual funds, retirement 401(k) portfolios, and mortgages",
                "Machine depreciation in corporate accounting and vehicle resale valuation"
            )
        ),
        MathTopic(
            id = "m8_solid_mensuration",
            grade = MathGrade.CLASS_8,
            branch = MathBranch.MENSURATION,
            chapterTitle = "Mensuration of 3D Solids",
            topicTitle = "Surface Areas & Volumes of Cuboid, Cube & Cylinder",
            summary = "Total Surface Area (TSA), Lateral/Curved Surface Area (LSA/CSA), and Volume (V) formulas for 3D prisms and cylinders.",
            detailedExplanation = "Surface area represents the sum of the areas of all external faces bounding a solid. Volume measures the three-dimensional capacity enclosed by the surface boundary.",
            keyFormulas = listOf(
                MathFormula(
                    title = "Cuboid Surface Area & Volume",
                    formulaLatex = "TSA = 2(lb + bh + hl), \\quad V = l \\times b \\times h",
                    quickDescription = "Total surface area and volume of a rectangular prism.",
                    symbols = mapOf("l" to "Length", "b" to "Breadth", "h" to "Height"),
                    canCalculate = true,
                    defaultInputs = mapOf("Length l" to 10.0, "Breadth b" to 6.0, "Height h" to 4.0),
                    calculateFn = { inputs ->
                        val l = inputs["Length l"] ?: 10.0
                        val b = inputs["Breadth b"] ?: 6.0
                        val h = inputs["Height h"] ?: 4.0
                        l * b * h
                    },
                    outputUnit = "cubic units",
                    outputSymbol = "Volume V"
                ),
                MathFormula(
                    title = "Right Circular Cylinder",
                    formulaLatex = "CSA = 2\\pi r h, \\quad TSA = 2\\pi r(r + h), \\quad V = \\pi r^2 h",
                    quickDescription = "Curved surface, total surface, and volume of a cylinder with radius r and height h.",
                    symbols = mapOf("r" to "Base radius", "h" to "Height"),
                    canCalculate = true,
                    defaultInputs = mapOf("Radius r" to 3.5, "Height h" to 10.0),
                    calculateFn = { inputs ->
                        val r = inputs["Radius r"] ?: 3.5
                        val h = inputs["Height h"] ?: 10.0
                        PI * r * r * h
                    },
                    outputUnit = "cubic units",
                    outputSymbol = "Cylinder Volume V"
                ),
                MathFormula(
                    title = "Area of Trapezium & Rhombus",
                    formulaLatex = "A_{\\text{trap}} = \\frac{1}{2}(a + b)h, \\quad A_{\\text{rhombus}} = \\frac{1}{2} d_1 d_2",
                    quickDescription = "Trapezium area with parallel sides a, b and rhombus with diagonals d1, d2."
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Diagonal of a cuboid: d = √(l² + b² + h²)",
                "Diagonal of a cube: d = s√3",
                "1 Litre = 1000 cm³ = 10⁻³ m³; 1 m³ = 1000 Litres"
            ),
            realWorldApplications = listOf(
                "Water storage tank capacity design and civil concrete volume estimation",
                "Packaging carton box material optimization and shipping container packing"
            )
        ),
        MathTopic(
            id = "m8_polygons_angles",
            grade = MathGrade.CLASS_8,
            branch = MathBranch.GEOMETRY,
            chapterTitle = "Understanding Quadrilaterals & Polygons",
            topicTitle = "Interior & Exterior Angle Sum Theorems",
            summary = "Formulas for sum of interior angles in n-sided polygon, exterior angle sum, and properties of special quadrilaterals.",
            detailedExplanation = "Any polygon with n sides can be partitioned into (n - 2) non-overlapping triangles from a single vertex, leading directly to the theorem that the sum of interior angles is (n - 2) × 180°. For any convex polygon, the sum of all exterior angles is always constant at 360°.",
            keyFormulas = listOf(
                MathFormula(
                    title = "Polygon Interior Angle Sum",
                    formulaLatex = "S_{\\text{int}} = (n - 2) \\times 180^\\circ",
                    quickDescription = "Total sum of interior angles for any convex polygon of n sides.",
                    symbols = mapOf("n" to "Number of sides (n ≥ 3)"),
                    canCalculate = true,
                    defaultInputs = mapOf("Number of sides n" to 6.0),
                    calculateFn = { inputs ->
                        val n = inputs["Number of sides n"] ?: 6.0
                        (n - 2.0) * 180.0
                    },
                    outputUnit = "degrees",
                    outputSymbol = "Sum S"
                ),
                MathFormula(
                    title = "Regular Polygon Angle Formulas",
                    formulaLatex = "\\text{Each Int} = \\frac{(n - 2) \\times 180^\\circ}{n}, \\quad \\text{Each Ext} = \\frac{360^\\circ}{n}",
                    quickDescription = "Measure of individual interior and exterior angles in an equiangular regular polygon."
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Number of diagonals in an n-sided polygon: N = n(n - 3) / 2",
                "Sum of exterior angles of any convex polygon is always exactly 360°",
                "Opposite angles and sides of a parallelogram are equal; diagonals bisect each other",
                "Diagonals of a rhombus bisect each other at right angles (90°)"
            ),
            realWorldApplications = listOf(
                "Hexagonal honeycomb structures and architectural geodesic dome tessellations",
                "Polygon mesh rendering in 3D computer graphics gaming engines"
            )
        ),

        // ==========================================
        // CLASS 9
        // ==========================================
        MathTopic(
            id = "m9_polynomial_identities",
            grade = MathGrade.CLASS_9,
            branch = MathBranch.ALGEBRA,
            chapterTitle = "Polynomials & Advanced Algebraic Identities",
            topicTitle = "Cubic Expansions, Trinomial Squares & Special Factoring",
            summary = "Complete suite of cubic and trinomial algebraic identities: (a+b+c)², (a±b)³, a³±b³, and a³+b³+c³-3abc.",
            detailedExplanation = "Higher-order algebraic identities are essential for simplifying expressions, evaluating polynomial roots, and proving geometric equalities. The identity a³ + b³ + c³ - 3abc = (a+b+c)(a²+b²+c² - ab - bc - ca) has a remarkable corollary: if a + b + c = 0, then a³ + b³ + c³ = 3abc.",
            keyFormulas = listOf(
                MathFormula(
                    title = "Trinomial Square Identity",
                    formulaLatex = "(a + b + c)^2 = a^2 + b^2 + c^2 + 2ab + 2bc + 2ca",
                    quickDescription = "Expansion of three terms squared.",
                    symbols = mapOf("a, b, c" to "Real numbers or algebraic expressions")
                ),
                MathFormula(
                    title = "Cubes of Binomials",
                    formulaLatex = "(a \\pm b)^3 = a^3 \\pm 3a^2b + 3ab^2 \\pm b^3 = a^3 \\pm b^3 \\pm 3ab(a \\pm b)",
                    quickDescription = "Expansion of sum and difference of two terms cubed."
                ),
                MathFormula(
                    title = "Sum & Difference of Two Cubes",
                    formulaLatex = "a^3 + b^3 = (a + b)(a^2 - ab + b^2), \\quad a^3 - b^3 = (a - b)(a^2 + ab + b^2)",
                    quickDescription = "Factorisation of sum and difference of cubes."
                ),
                MathFormula(
                    title = "Special Three-Variable Cubic Identity",
                    formulaLatex = "a^3 + b^3 + c^3 - 3abc = (a + b + c)(a^2 + b^2 + c^2 - ab - bc - ca)",
                    quickDescription = "Crucial conditional theorem: If a + b + c = 0, then a³ + b³ + c³ = 3abc."
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Remainder Theorem: If polynomial p(x) is divided by (x - a), the remainder is p(a)",
                "Factor Theorem: (x - a) is a factor of p(x) if and only if p(a) = 0",
                "a² + b² + c² - ab - bc - ca = ½[(a - b)² + (b - c)² + (c - a)²]"
            ),
            realWorldApplications = listOf(
                "Rapid computational algebra in DSP filters and cryptography",
                "Spline curve generation in computer-aided design (CAD)"
            )
        ),
        MathTopic(
            id = "m9_herons_formula",
            grade = MathGrade.CLASS_9,
            branch = MathBranch.MENSURATION,
            chapterTitle = "Heron's Formula & Scalene Triangles",
            topicTitle = "Triangle Area via Semi-Perimeter and Side Lengths",
            summary = "Heron's theorem for calculating the exact area of any triangle given solely the lengths of its three sides without knowing the altitude.",
            detailedExplanation = "For any triangle with side lengths a, b, and c, let the semi-perimeter be s = (a + b + c) / 2. Heron's Formula computes the area as A = √(s(s - a)(s - b)(s - c)). This is immensely practical when perpendicular heights cannot be directly measured.",
            keyFormulas = listOf(
                MathFormula(
                    title = "Heron's Formula for Area of Triangle",
                    formulaLatex = "A = \\sqrt{s(s - a)(s - b)(s - c)}, \\quad s = \\frac{a + b + c}{2}",
                    quickDescription = "Area of triangle using three side lengths a, b, c and semi-perimeter s.",
                    symbols = mapOf("a, b, c" to "Side lengths", "s" to "Semi-perimeter"),
                    canCalculate = true,
                    defaultInputs = mapOf("Side a" to 5.0, "Side b" to 6.0, "Side c" to 7.0),
                    calculateFn = { inputs ->
                        val a = inputs["Side a"] ?: 5.0
                        val b = inputs["Side b"] ?: 6.0
                        val c = inputs["Side c"] ?: 7.0
                        val s = (a + b + c) / 2.0
                        val inside = s * (s - a) * (s - b) * (s - c)
                        if (inside > 0) sqrt(inside) else 0.0
                    },
                    outputUnit = "sq units",
                    outputSymbol = "Heron Area A"
                ),
                MathFormula(
                    title = "Area of Equilateral Triangle",
                    formulaLatex = "A = \\frac{\\sqrt{3}}{4} a^2, \\quad h = \\frac{\\sqrt{3}}{2} a",
                    quickDescription = "Special case of Heron's formula for equilateral triangle of side a."
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Area of isosceles triangle with equal sides a and base b: A = (b/4)√(4a² - b²)",
                "Inradius r of triangle: r = A / s",
                "Circumradius R of triangle: R = (abc) / (4A)"
            ),
            realWorldApplications = listOf(
                "Land surveying when uneven triangular boundary perimeters are measured with chains",
                "Triangulated irregular networks (TIN) for 3D terrain elevation mapping"
            )
        ),
        MathTopic(
            id = "m9_curved_solids",
            grade = MathGrade.CLASS_9,
            branch = MathBranch.MENSURATION,
            chapterTitle = "Surface Areas & Volumes of Curved Solids",
            topicTitle = "Cone, Sphere & Hemisphere Formulas",
            summary = "Formulas for slant height, curved surface area, total surface area, and volume of right circular cones, spheres, and hemispheres.",
            detailedExplanation = "Curved solids require integration-based or Archimedean principles. For a cone of base radius r and height h, slant height is l = √(r² + h²), CSA = πrl, and Volume = ⅓πr²h (one-third of cylinder). For a sphere of radius r, surface area is exactly 4πr² and volume is ⁴/₃πr³.",
            keyFormulas = listOf(
                MathFormula(
                    title = "Right Circular Cone Formulas",
                    formulaLatex = "l = \\sqrt{r^2 + h^2}, \\quad CSA = \\pi r l, \\quad V = \\frac{1}{3}\\pi r^2 h",
                    quickDescription = "Slant height, curved surface area, and volume of cone.",
                    symbols = mapOf("r" to "Base radius", "h" to "Vertical height", "l" to "Slant height"),
                    canCalculate = true,
                    defaultInputs = mapOf("Radius r" to 3.0, "Height h" to 4.0),
                    calculateFn = { inputs ->
                        val r = inputs["Radius r"] ?: 3.0
                        val h = inputs["Height h"] ?: 4.0
                        (1.0 / 3.0) * PI * r * r * h
                    },
                    outputUnit = "cubic units",
                    outputSymbol = "Cone Volume V"
                ),
                MathFormula(
                    title = "Sphere Surface Area & Volume",
                    formulaLatex = "SA = 4\\pi r^2, \\quad V = \\frac{4}{3}\\pi r^3",
                    quickDescription = "Surface area and volume of a perfect sphere of radius r.",
                    symbols = mapOf("r" to "Sphere radius"),
                    canCalculate = true,
                    defaultInputs = mapOf("Radius r" to 6.0),
                    calculateFn = { inputs ->
                        val r = inputs["Radius r"] ?: 6.0
                        (4.0 / 3.0) * PI * r.pow(3)
                    },
                    outputUnit = "cubic units",
                    outputSymbol = "Sphere Volume V"
                ),
                MathFormula(
                    title = "Hemisphere Formulas",
                    formulaLatex = "CSA = 2\\pi r^2, \\quad TSA = 3\\pi r^2, \\quad V = \\frac{2}{3}\\pi r^3",
                    quickDescription = "Half-sphere curved surface, solid total surface area, and volume."
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Archimedes Ratio: Cylinder : Sphere : Cone with same radius & height (h=2r) volumes are in ratio 3 : 2 : 1",
                "Volume conservation: When metal solids are melted and recast, total volume remains constant"
            ),
            realWorldApplications = listOf(
                "Astronomical body mass calculations (planetary spheroids)",
                "Silo grain storage volume, ice cream cone capacity, and dome architecture"
            )
        ),
        MathTopic(
            id = "m9_circles_geometry",
            grade = MathGrade.CLASS_9,
            branch = MathBranch.GEOMETRY,
            chapterTitle = "Circle Theorems & Cyclic Quadrilaterals",
            topicTitle = "Angles Subtended by Arcs & Cyclic Quadrilateral Theorems",
            summary = "Theorems on chords, central angle vs circumference angle (2θ rule), angles in the same segment, and opposite angles of cyclic quadrilaterals.",
            detailedExplanation = "A circle is the locus of points equidistant from a center. The angle subtended by an arc at the center is double the angle subtended by it at any point on the remaining circumference. A quadrilateral is cyclic if and only if the sum of either pair of opposite angles is 180°.",
            keyFormulas = listOf(
                MathFormula(
                    title = "Central Angle Theorem",
                    formulaLatex = "\\angle AOB = 2 \\times \\angle APB",
                    quickDescription = "The angle subtended by an arc at the center is double the angle at the circumference."
                ),
                MathFormula(
                    title = "Angle in a Semicircle",
                    formulaLatex = "\\angle \\text{Semicircle} = 90^\\circ",
                    quickDescription = "The angle inscribed in a semicircle is always a right angle."
                ),
                MathFormula(
                    title = "Cyclic Quadrilateral Theorem",
                    formulaLatex = "\\angle A + \\angle C = 180^\\circ, \\quad \\angle B + \\angle D = 180^\\circ",
                    quickDescription = "Opposite interior angles of a cyclic quadrilateral are supplementary."
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Equal chords of a circle subtend equal angles at the center and are equidistant from the center",
                "Perpendicular from the center of a circle to a chord bisects the chord",
                "Angles in the same segment of a circle are equal",
                "Ptolemy's Theorem for cyclic quadrilaterals: AC · BD = AB · CD + BC · AD"
            ),
            realWorldApplications = listOf(
                "Optics lens curvature radius measurements via spherometers",
                "Mechanical gear pitch circles and planetary gear transmission layout"
            )
        ),

        // ==========================================
        // CLASS 10
        // ==========================================
        MathTopic(
            id = "m10_quadratic_equations",
            grade = MathGrade.CLASS_10,
            branch = MathBranch.ALGEBRA,
            chapterTitle = "Quadratic Equations & Discriminant",
            topicTitle = "Quadratic Formula & Nature of Roots",
            summary = "Standard quadratic form ax² + bx + c = 0, the Quadratic Formula, Discriminant D = b² - 4ac, and root nature analysis.",
            detailedExplanation = "A quadratic equation has degree 2. The roots can be solved algebraically using the quadratic formula derived by completing the square: x = (-b ± √(b² - 4ac)) / (2a). The discriminant D = b² - 4ac determines the nature of roots: D > 0 gives two distinct real roots, D = 0 gives two equal real roots (x = -b/2a), and D < 0 gives non-real complex conjugate roots.",
            keyFormulas = listOf(
                MathFormula(
                    title = "The Quadratic Formula",
                    formulaLatex = "x = \\frac{-b \\pm \\sqrt{b^2 - 4ac}}{2a}",
                    quickDescription = "General algebraic solution for roots of ax² + bx + c = 0.",
                    symbols = mapOf("a" to "Coefficient of x² (a ≠ 0)", "b" to "Coefficient of x", "c" to "Constant term"),
                    canCalculate = true,
                    defaultInputs = mapOf("Coeff a" to 1.0, "Coeff b" to -5.0, "Coeff c" to 6.0),
                    calculateFn = { inputs ->
                        val a = inputs["Coeff a"] ?: 1.0
                        val b = inputs["Coeff b"] ?: -5.0
                        val c = inputs["Coeff c"] ?: 6.0
                        val disc = b * b - 4.0 * a * c
                        if (disc >= 0) (-b + sqrt(disc)) / (2.0 * a) else Double.NaN // returns principal root
                    },
                    outputUnit = "",
                    outputSymbol = "Primary Root x₁"
                ),
                MathFormula(
                    title = "Discriminant & Nature of Roots",
                    formulaLatex = "D = b^2 - 4ac \\quad \\begin{cases} D > 0 & \\text{Two distinct real roots} \\\\ D = 0 & \\text{Two equal real roots} \\\\ D < 0 & \\text{No real roots (complex)} \\end{cases}",
                    quickDescription = "Discriminant criterion determining the nature of roots.",
                    symbols = mapOf("D" to "Discriminant")
                ),
                MathFormula(
                    title = "Vieta's Relations for Quadratic",
                    formulaLatex = "\\alpha + \\beta = -\\frac{b}{a}, \\quad \\alpha \\beta = \\frac{c}{a}",
                    quickDescription = "Sum and product of roots expressed in terms of polynomial coefficients."
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Reconstruction of quadratic equation from roots: x² - (α + β)x + αβ = 0",
                "Condition for a common root between two quadratics a₁x²+b₁x+c₁=0 and a₂x²+b₂x+c₂=0: (c₁a₂ - c₂a₁)² = (a₁b₂ - a₂b₁)(b₁c₂ - b₂c₁)"
            ),
            realWorldApplications = listOf(
                "Ballistic projectile trajectory peak and landing distance calculation",
                "Profit maximisation and cost minimisation parabolic vertex optimization in economics"
            )
        ),
        MathTopic(
            id = "m10_arithmetic_progression",
            grade = MathGrade.CLASS_10,
            branch = MathBranch.ALGEBRA,
            chapterTitle = "Arithmetic Progressions (AP)",
            topicTitle = "General Term & Sum of First n Terms",
            summary = "Sequences with constant common difference d: n-th term formula a_n = a + (n-1)d and sum of n terms S_n = n/2 [2a + (n-1)d].",
            detailedExplanation = "An Arithmetic Progression is a sequence where each term after the first is obtained by adding a fixed constant common difference d. The n-th term is a_n = a + (n - 1)d. The sum of the first n terms is S_n = (n/2)[2a + (n - 1)d] = (n/2)(a + l), where l is the last term.",
            keyFormulas = listOf(
                MathFormula(
                    title = "n-th Term of an AP",
                    formulaLatex = "a_n = a + (n - 1)d",
                    quickDescription = "Value of the n-th term in an arithmetic progression.",
                    symbols = mapOf("a" to "First term", "d" to "Common difference", "n" to "Term index"),
                    canCalculate = true,
                    defaultInputs = mapOf("First term a" to 2.0, "Difference d" to 3.0, "Index n" to 10.0),
                    calculateFn = { inputs ->
                        val a = inputs["First term a"] ?: 2.0
                        val d = inputs["Difference d"] ?: 3.0
                        val n = inputs["Index n"] ?: 10.0
                        a + (n - 1.0) * d
                    },
                    outputUnit = "",
                    outputSymbol = "Term aₙ"
                ),
                MathFormula(
                    title = "Sum of First n Terms of an AP",
                    formulaLatex = "S_n = \\frac{n}{2}\\left[2a + (n - 1)d\\right] = \\frac{n}{2}(a + l)",
                    quickDescription = "Cumulative sum of the first n terms.",
                    symbols = mapOf("a" to "First term", "l" to "Last term aₙ", "n" to "Number of terms"),
                    canCalculate = true,
                    defaultInputs = mapOf("First term a" to 2.0, "Difference d" to 3.0, "Terms n" to 10.0),
                    calculateFn = { inputs ->
                        val a = inputs["First term a"] ?: 2.0
                        val d = inputs["Difference d"] ?: 3.0
                        val n = inputs["Terms n"] ?: 10.0
                        (n / 2.0) * (2.0 * a + (n - 1.0) * d)
                    },
                    outputUnit = "",
                    outputSymbol = "Sum Sₙ"
                ),
                MathFormula(
                    title = "Arithmetic Mean",
                    formulaLatex = "AM = \\frac{a + b}{2}",
                    quickDescription = "The arithmetic mean between two numbers a and b."
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Common difference: d = aₙ - aₙ₋₁",
                "Relation between sum and n-th term: aₙ = Sₙ - Sₙ₋₁",
                "Three terms in AP can be conveniently assumed as: (a - d), a, (a + d)",
                "Four terms in AP: (a - 3d), (a - d), (a + d), (a + 3d)"
            ),
            realWorldApplications = listOf(
                "Loan installment schedules with equal step amortization",
                "Seating row capacity layout in stadium auditoriums and tiered theatres"
            )
        ),
        MathTopic(
            id = "m10_trigonometry_foundations",
            grade = MathGrade.CLASS_10,
            branch = MathBranch.TRIGONOMETRY,
            chapterTitle = "Introduction to Trigonometry & Identities",
            topicTitle = "Trigonometric Ratios & Pythagorean Identities",
            summary = "Definitions of sine, cosine, tangent, cotangent, secant, cosecant, exact standard angle values, complementary angles, and the 3 fundamental Pythagorean identities.",
            detailedExplanation = "Trigonometry studies relationships between side lengths and angles of triangles. In a right triangle with acute angle θ, sin θ = Opp/Hyp, cos θ = Adj/Hyp, and tan θ = Opp/Adj. The three fundamental Pythagorean identities link these ratios universally.",
            keyFormulas = listOf(
                MathFormula(
                    title = "Pythagorean Trigonometric Identities",
                    formulaLatex = "\\sin^2\\theta + \\cos^2\\theta = 1, \\quad 1 + \\tan^2\\theta = \\sec^2\\theta, \\quad 1 + \\cot^2\\theta = \\csc^2\\theta",
                    quickDescription = "The 3 fundamental Pythagorean identities of trigonometry."
                ),
                MathFormula(
                    title = "Reciprocal & Quotient Identities",
                    formulaLatex = "\\tan\\theta = \\frac{\\sin\\theta}{\\cos\\theta}, \\quad \\cot\\theta = \\frac{\\cos\\theta}{\\sin\\theta}, \\quad \\sec\\theta = \\frac{1}{\\cos\\theta}, \\quad \\csc\\theta = \\frac{1}{\\sin\\theta}",
                    quickDescription = "Reciprocal and ratio definitions of trigonometric functions."
                ),
                MathFormula(
                    title = "Complementary Angle Relations",
                    formulaLatex = "\\sin(90^\\circ - \\theta) = \\cos\\theta, \\quad \\tan(90^\\circ - \\theta) = \\cot\\theta, \\quad \\sec(90^\\circ - \\theta) = \\csc\\theta",
                    quickDescription = "Trigonometric ratios of complementary angles."
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Standard exact table values at 0°, 30°, 45°, 60°, 90°",
                "sin(0°)=0, sin(30°)=1/2, sin(45°)=1/√2, sin(60°)=√3/2, sin(90°)=1",
                "cos(0°)=1, cos(30°)=√3/2, cos(45°)=1/√2, cos(60°)=1/2, cos(90°)=0",
                "tan(0°)=0, tan(30°)=1/√3, tan(45°)=1, tan(60°)=√3, tan(90°)=undefined",
                "sec²θ - tan²θ = 1 ⟹ (sec θ - tan θ) = 1 / (sec θ + tan θ)"
            ),
            realWorldApplications = listOf(
                "Heights & distances surveying (calculating lighthouse height and tower shadows using angles of elevation/depression)",
                "Radar signal phase processing, satellite tracking, and civil structural engineering"
            )
        ),
        MathTopic(
            id = "m10_coordinate_geometry_2d",
            grade = MathGrade.CLASS_10,
            branch = MathBranch.COORDINATE_GEOMETRY,
            chapterTitle = "Coordinate Geometry (2D)",
            topicTitle = "Distance, Section Formula, Midpoint & Triangle Area",
            summary = "Formulas for Euclidean distance, section formula for internal division, centroid of triangle, and coordinate-based area of polygons.",
            detailedExplanation = "Coordinate geometry unites algebra with geometry on the Cartesian plane. The distance between points P(x₁, y₁) and Q(x₂, y₂) is d = √((x₂ - x₁)² + (y₂ - y₁)²). The section formula finds coordinates dividing a line segment in ratio m₁:m₂.",
            keyFormulas = listOf(
                MathFormula(
                    title = "Distance Formula",
                    formulaLatex = "d = \\sqrt{(x_2 - x_1)^2 + (y_2 - y_1)^2}",
                    quickDescription = "Euclidean distance between two 2D points.",
                    symbols = mapOf("x₁, y₁" to "Coordinates of point 1", "x₂, y₂" to "Coordinates of point 2"),
                    canCalculate = true,
                    defaultInputs = mapOf("x₁" to 0.0, "y₁" to 0.0, "x₂" to 3.0, "y₂" to 4.0),
                    calculateFn = { inputs ->
                        val x1 = inputs["x₁"] ?: 0.0
                        val y1 = inputs["y₁"] ?: 0.0
                        val x2 = inputs["x₂"] ?: 3.0
                        val y2 = inputs["y₂"] ?: 4.0
                        sqrt((x2 - x1).pow(2) + (y2 - y1).pow(2))
                    },
                    outputUnit = "units",
                    outputSymbol = "Distance d"
                ),
                MathFormula(
                    title = "Section Formula (Internal Division)",
                    formulaLatex = "x = \\frac{m_1 x_2 + m_2 x_1}{m_1 + m_2}, \\quad y = \\frac{m_1 y_2 + m_2 y_1}{m_1 + m_2}",
                    quickDescription = "Coordinates of point dividing line segment joining (x1,y1) and (x2,y2) in ratio m1:m2."
                ),
                MathFormula(
                    title = "Centroid & Area of Triangle",
                    formulaLatex = "G = \\left(\\frac{x_1+x_2+x_3}{3}, \\frac{y_1+y_2+y_3}{3}\\right), \\quad A = \\frac{1}{2}|x_1(y_2-y_3) + x_2(y_3-y_1) + x_3(y_1-y_2)|",
                    quickDescription = "Centroid coordinate formula and triangular enclosed area formula from 3 vertices."
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Midpoint Formula: M = ((x₁ + x₂) / 2, (y₁ + y₂) / 2)",
                "Condition for collinearity of three points: Area of triangle formed by the three points equals 0"
            ),
            realWorldApplications = listOf(
                "GPS map routing coordinates, geographic information systems (GIS)",
                "Robotic end-effector position tracking on 2D conveyor belts"
            )
        ),
        MathTopic(
            id = "m10_circle_sectors_statistics",
            grade = MathGrade.CLASS_10,
            branch = MathBranch.MENSURATION,
            chapterTitle = "Areas Related to Circles & Statistics",
            topicTitle = "Sector Area, Arc Length, Empirical Mean-Median-Mode",
            summary = "Formulas for sector area A = (θ/360)πr², arc length l = (θ/360)2πr, grouped mean, median, mode, and empirical relationship Mode = 3 Median - 2 Mean.",
            detailedExplanation = "A sector is a portion of a circle enclosed by two radii and an arc. In statistics, measures of central tendency describe grouped data distributions.",
            keyFormulas = listOf(
                MathFormula(
                    title = "Sector Area & Arc Length",
                    formulaLatex = "A_{\\text{sector}} = \\frac{\\theta}{360^\\circ}\\pi r^2, \\quad l_{\\text{arc}} = \\frac{\\theta}{360^\\circ}2\\pi r",
                    quickDescription = "Area and arc length subtended by central angle θ in degrees.",
                    symbols = mapOf("r" to "Radius", "\\theta" to "Central angle in degrees"),
                    canCalculate = true,
                    defaultInputs = mapOf("Radius r" to 6.0, "Angle θ (deg)" to 60.0),
                    calculateFn = { inputs ->
                        val r = inputs["Radius r"] ?: 6.0
                        val theta = inputs["Angle θ (deg)"] ?: 60.0
                        (theta / 360.0) * PI * r * r
                    },
                    outputUnit = "sq units",
                    outputSymbol = "Sector Area"
                ),
                MathFormula(
                    title = "Empirical Statistical Formula",
                    formulaLatex = "\\text{Mode} = 3\\text{Median} - 2\\text{Mean}",
                    quickDescription = "Empirical relationship connecting the three measures of central tendency in moderately skewed distributions."
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Area of segment of circle = Area of sector - Area of corresponding triangle",
                "Classical probability: P(E) = n(E) / n(S), where 0 ≤ P(E) ≤ 1",
                "Complementary event: P(E) + P(not E) = 1"
            ),
            realWorldApplications = listOf(
                "Windshield wiper wipe sweep area and pie chart visualizer proportions",
                "Census demographic analysis and data science central tendency estimation"
            )
        ),

        // ==========================================
        // CLASS 11
        // ==========================================
        MathTopic(
            id = "m11_sets_relations_functions",
            grade = MathGrade.CLASS_11,
            branch = MathBranch.SETS_RELATIONS,
            chapterTitle = "Sets, Relations and Functions",
            topicTitle = "Set Operations, De Morgan's Laws & Cardinality",
            summary = "Union, intersection, complement, Cartesian product, relation equivalence, and function domain/codomain/range.",
            detailedExplanation = "A set is a well-defined collection of distinct objects. Cardinality formulas count elements across finite sets: n(A ∪ B) = n(A) + n(B) - n(A ∩ B). De Morgan's laws state that the complement of the union of two sets is the intersection of their complements: (A ∪ B)' = A' ∩ B'.",
            keyFormulas = listOf(
                MathFormula(
                    title = "Principle of Inclusion-Exclusion (2 Sets)",
                    formulaLatex = "n(A \\cup B) = n(A) + n(B) - n(A \\cap B)",
                    quickDescription = "Cardinality of the union of two finite sets.",
                    symbols = mapOf("n(A)" to "Elements in A", "n(B)" to "Elements in B", "n(A ∩ B)" to "Intersection elements"),
                    canCalculate = true,
                    defaultInputs = mapOf("n(A)" to 30.0, "n(B)" to 25.0, "n(A ∩ B)" to 10.0),
                    calculateFn = { inputs ->
                        val na = inputs["n(A)"] ?: 30.0
                        val nb = inputs["n(B)"] ?: 25.0
                        val nab = inputs["n(A ∩ B)"] ?: 10.0
                        na + nb - nab
                    },
                    outputUnit = "elements",
                    outputSymbol = "n(A ∪ B)"
                ),
                MathFormula(
                    title = "De Morgan's Laws",
                    formulaLatex = "(A \\cup B)' = A' \\cap B', \\quad (A \\cap B)' = A' \\cup B'",
                    quickDescription = "Complementation laws governing set duality."
                ),
                MathFormula(
                    title = "Three Set Inclusion-Exclusion",
                    formulaLatex = "n(A \\cup B \\cup C) = n(A)+n(B)+n(C) - n(A\\cap B) - n(B\\cap C) - n(C\\cap A) + n(A\\cap B\\cap C)",
                    quickDescription = "Total union elements for 3 overlapping finite sets."
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Number of subsets (Power Set) of a set with n elements is 2ⁿ",
                "Number of relations from set A to set B is 2^(n(A) · n(B))",
                "Equivalence relation is reflexive, symmetric, and transitive"
            ),
            realWorldApplications = listOf(
                "Relational database SQL JOIN and UNION query logic",
                "Digital logic gate boolean circuit design (AND/OR duality)"
            )
        ),
        MathTopic(
            id = "m11_trigonometric_identities_senior",
            grade = MathGrade.CLASS_11,
            branch = MathBranch.TRIGONOMETRY,
            chapterTitle = "Senior Trigonometric Functions & Identities",
            topicTitle = "Compound, Double, Triple Angle & Sum-to-Product Formulas",
            summary = "Complete suite of senior trigonometric identities: sin(A±B), cos(A±B), tan(A±B), double angle, triple angle, product-to-sum, and sum-to-product (C-D) formulas.",
            detailedExplanation = "Senior trigonometry generalises circular functions to all real domain values using radians (π rad = 180°). The compound angle expansions form the basis for wave harmonic analysis, Fourier series, and quantum phase transformations.",
            keyFormulas = listOf(
                MathFormula(
                    title = "Compound Angle Addition Formulas",
                    formulaLatex = "\\sin(A \\pm B) = \\sin A \\cos B \\pm \\cos A \\sin B, \\quad \\cos(A \\pm B) = \\cos A \\cos B \\mp \\sin A \\sin B",
                    quickDescription = "Sine and cosine of sum and difference of two angles."
                ),
                MathFormula(
                    title = "Tangent & Cotangent Compound Angles",
                    formulaLatex = "\\tan(A \\pm B) = \\frac{\\tan A \\pm \\tan B}{1 \\mp \\tan A \\tan B}, \\quad \\cot(A \\pm B) = \\frac{\\cot A \\cot B \\mp 1}{\\cot B \\pm \\cot A}",
                    quickDescription = "Tangent and cotangent sum and difference expansions."
                ),
                MathFormula(
                    title = "Double Angle Formulas",
                    formulaLatex = "\\sin 2A = 2\\sin A\\cos A = \\frac{2\\tan A}{1+\\tan^2 A}, \\quad \\cos 2A = \\cos^2 A - \\sin^2 A = 2\\cos^2 A - 1 = 1 - 2\\sin^2 A = \\frac{1-\\tan^2 A}{1+\\tan^2 A}",
                    quickDescription = "Double angle sine and cosine expressed in trigonometric terms."
                ),
                MathFormula(
                    title = "Triple Angle Formulas",
                    formulaLatex = "\\sin 3A = 3\\sin A - 4\\sin^3 A, \\quad \\cos 3A = 4\\cos^3 A - 3\\cos A, \\quad \\tan 3A = \\frac{3\\tan A - \\tan^3 A}{1 - 3\\tan^2 A}",
                    quickDescription = "Triple angle expansions."
                ),
                MathFormula(
                    title = "Sum-to-Product (C-D Formulas)",
                    formulaLatex = "\\sin C + \\sin D = 2\\sin\\left(\\frac{C+D}{2}\\right)\\cos\\left(\\frac{C-D}{2}\\right), \\quad \\cos C - \\cos D = -2\\sin\\left(\\frac{C+D}{2}\\right)\\sin\\left(\\frac{C-D}{2}\\right)",
                    quickDescription = "Conversion of sum/difference of trigonometric terms into products."
                ),
                MathFormula(
                    title = "Product-to-Sum Formulas",
                    formulaLatex = "2\\sin A\\cos B = \\sin(A+B) + \\sin(A-B), \\quad 2\\cos A\\cos B = \\cos(A+B) + \\cos(A-B)",
                    quickDescription = "Conversion of trigonometric products into sums."
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Arc length formula: l = r · θ (where θ is in radians)",
                "Half angle powers: sin²A = (1 - cos 2A) / 2, cos²A = (1 + cos 2A) / 2",
                "General solution: sin θ = 0 ⟹ θ = nπ; cos θ = 0 ⟹ θ = (2n+1)π/2; tan θ = 0 ⟹ θ = nπ",
                "General solution: sin θ = sin α ⟹ θ = nπ + (-1)ⁿ α; cos θ = cos α ⟹ θ = 2nπ ± α"
            ),
            realWorldApplications = listOf(
                "Radio frequency AM/FM modulation and superheterodyne mixing",
                "Acoustic beat frequencies (interference between two sound waves)",
                "Alternating current active, reactive, and apparent electrical power calculations"
            )
        ),
        MathTopic(
            id = "m11_algebra_comb_binom",
            grade = MathGrade.CLASS_11,
            branch = MathBranch.ALGEBRA,
            chapterTitle = "Permutations, Combinations & Binomial Theorem",
            topicTitle = "Counting Principles, nPr, nCr & General Binomial Expansion",
            summary = "Fundamental counting principle, factorial notation n!, permutations ⁿPᵣ, combinations ⁿCᵣ, Pascal's rule, and Newton's Binomial Theorem.",
            detailedExplanation = "Permutations count ordered arrangements: ⁿPᵣ = n! / (n - r)!. Combinations count unordered selections: ⁿCᵣ = n! / [r!(n - r)!]. Newton's Binomial Theorem states (a + b)ⁿ = ∑ [r=0 to n] ⁿCᵣ aⁿ⁻ʳ bʳ.",
            keyFormulas = listOf(
                MathFormula(
                    title = "Permutations Formula (Ordered Arrangements)",
                    formulaLatex = "^nP_r = \\frac{n!}{(n - r)!}",
                    quickDescription = "Arrangements of r objects chosen from n distinct objects.",
                    symbols = mapOf("n" to "Total items", "r" to "Items chosen"),
                    canCalculate = true,
                    defaultInputs = mapOf("Total n" to 5.0, "Chosen r" to 3.0),
                    calculateFn = { inputs ->
                        val n = inputs["Total n"]?.toInt() ?: 5
                        val r = inputs["Chosen r"]?.toInt() ?: 3
                        fun fact(x: Int): Double = if (x <= 1) 1.0 else (1..x).fold(1.0) { acc, i -> acc * i }
                        if (n >= r && r >= 0) fact(n) / fact(n - r) else 0.0
                    },
                    outputUnit = "ways",
                    outputSymbol = "ⁿPᵣ"
                ),
                MathFormula(
                    title = "Combinations Formula (Unordered Selections)",
                    formulaLatex = "^nC_r = \\binom{n}{r} = \\frac{n!}{r!(n - r)!}",
                    quickDescription = "Number of ways to choose r items from n distinct items.",
                    symbols = mapOf("n" to "Total items", "r" to "Items chosen"),
                    canCalculate = true,
                    defaultInputs = mapOf("Total n" to 6.0, "Chosen r" to 2.0),
                    calculateFn = { inputs ->
                        val n = inputs["Total n"]?.toInt() ?: 6
                        val r = inputs["Chosen r"]?.toInt() ?: 2
                        fun fact(x: Int): Double = if (x <= 1) 1.0 else (1..x).fold(1.0) { acc, i -> acc * i }
                        if (n >= r && r >= 0) fact(n) / (fact(r) * fact(n - r)) else 0.0
                    },
                    outputUnit = "combinations",
                    outputSymbol = "ⁿCᵣ"
                ),
                MathFormula(
                    title = "The Binomial Theorem",
                    formulaLatex = "(a + b)^n = \\sum_{r=0}^n \\binom{n}{r} a^{n-r}b^r, \\quad T_{r+1} = \\binom{n}{r} a^{n-r}b^r",
                    quickDescription = "General term T_{r+1} and expansion for any positive integer exponent n."
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Symmetry property: ⁿCᵣ = ⁿCₙ₋ᵣ",
                "Pascal's Identity: ⁿCᵣ + ⁿCᵣ₋₁ = ⁿ⁺¹Cᵣ",
                "Sum of all binomial coefficients: ⁿC₀ + ⁿC₁ + ... + ⁿCₙ = 2ⁿ",
                "Sum of even coefficients equals sum of odd coefficients: = 2ⁿ⁻¹"
            ),
            realWorldApplications = listOf(
                "Password entropy calculation and cryptographic key combinations",
                "Genetics inheritance probability matrices and clinical trial sampling"
            )
        ),
        MathTopic(
            id = "m11_sequences_series",
            grade = MathGrade.CLASS_11,
            branch = MathBranch.ALGEBRA,
            chapterTitle = "Sequences, Series & Progressions",
            topicTitle = "Geometric Progressions (GP) & Special Infinite Series",
            summary = "General term of GP a_n = arⁿ⁻¹, finite sum S_n = a(1 - rⁿ)/(1 - r), infinite geometric series S_∞ = a/(1 - r), and standard power sums ∑n, ∑n², ∑n³.",
            detailedExplanation = "A Geometric Progression has a constant common ratio r = aₖ₊₁ / aₖ. When |r| < 1, the sum of an infinite geometric series converges to a finite value: S_∞ = a / (1 - r).",
            keyFormulas = listOf(
                MathFormula(
                    title = "n-th Term and Finite Sum of GP",
                    formulaLatex = "a_n = a r^{n-1}, \\quad S_n = \\frac{a(1 - r^n)}{1 - r} \\quad (r \\neq 1)",
                    quickDescription = "General term and sum of first n terms of a geometric progression.",
                    symbols = mapOf("a" to "First term", "r" to "Common ratio", "n" to "Number of terms")
                ),
                MathFormula(
                    title = "Sum to Infinity of GP",
                    formulaLatex = "S_\\infty = \\frac{a}{1 - r} \\quad (|r| < 1)",
                    quickDescription = "Convergent infinite sum of geometric progression when common ratio |r| < 1.",
                    symbols = mapOf("a" to "First term", "r" to "Common ratio (|r| < 1)"),
                    canCalculate = true,
                    defaultInputs = mapOf("First term a" to 1.0, "Common ratio r" to 0.5),
                    calculateFn = { inputs ->
                        val a = inputs["First term a"] ?: 1.0
                        val r = inputs["Common ratio r"] ?: 0.5
                        if (abs(r) < 1.0) a / (1.0 - r) else Double.NaN
                    },
                    outputUnit = "",
                    outputSymbol = "Infinite Sum S_∞"
                ),
                MathFormula(
                    title = "Sum of Natural Numbers & Powers",
                    formulaLatex = "\\sum n = \\frac{n(n+1)}{2}, \\quad \\sum n^2 = \\frac{n(n+1)(2n+1)}{6}, \\quad \\sum n^3 = \\left[\\frac{n(n+1)}{2}\\right]^2",
                    quickDescription = "Closed form formulas for sum of first n naturals, squares, and cubes."
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Geometric Mean: GM = √(ab)",
                "AM-GM-HM Inequality: For positive real numbers, AM ≥ GM ≥ HM",
                "Harmonic Mean: HM = 2ab / (a + b)"
            ),
            realWorldApplications = listOf(
                "Compound interest dividend reinvestment and financial annuity valuation",
                "Fractal geometry perimeter/area convergence (Koch snowflake, Sierpinski triangle)"
            )
        ),
        MathTopic(
            id = "m11_straight_lines_conics",
            grade = MathGrade.CLASS_11,
            branch = MathBranch.COORDINATE_GEOMETRY,
            chapterTitle = "Straight Lines & Conic Sections",
            topicTitle = "Line Forms, Circle, Parabola, Ellipse & Hyperbola",
            summary = "Equations of lines (slope-intercept, point-slope, intercept, normal), perpendicular distance, and standard equations of conics.",
            detailedExplanation = "Conic sections are curves formed by the intersection of a plane with a double right circular cone. The eccentricity e determines the curve: Circle (e = 0), Parabola (e = 1), Ellipse (0 < e < 1), and Hyperbola (e > 1).",
            keyFormulas = listOf(
                MathFormula(
                    title = "Perpendicular Distance from Point to Line",
                    formulaLatex = "d = \\frac{|A x_1 + B y_1 + C|}{\\sqrt{A^2 + B^2}}",
                    quickDescription = "Shortest perpendicular distance from point (x1, y1) to line Ax + By + C = 0.",
                    symbols = mapOf("A, B, C" to "Coefficients of line", "x₁, y₁" to "Point coordinates"),
                    canCalculate = true,
                    defaultInputs = mapOf("A" to 3.0, "B" to 4.0, "C" to -12.0, "x₁" to 0.0, "y₁" to 0.0),
                    calculateFn = { inputs ->
                        val a = inputs["A"] ?: 3.0
                        val b = inputs["B"] ?: 4.0
                        val c = inputs["C"] ?: -12.0
                        val x1 = inputs["x₁"] ?: 0.0
                        val y1 = inputs["y₁"] ?: 0.0
                        abs(a * x1 + b * y1 + c) / sqrt(a * a + b * b)
                    },
                    outputUnit = "units",
                    outputSymbol = "Distance d"
                ),
                MathFormula(
                    title = "Standard Forms of Conic Sections",
                    formulaLatex = "\\text{Parabola: } y^2 = 4ax, \\quad \\text{Ellipse: } \\frac{x^2}{a^2} + \\frac{y^2}{b^2} = 1, \\quad \\text{Hyperbola: } \\frac{x^2}{a^2} - \\frac{y^2}{b^2} = 1",
                    quickDescription = "Standard canonical equations for conics centered at the origin."
                ),
                MathFormula(
                    title = "Ellipse & Hyperbola Eccentricity",
                    formulaLatex = "e_{\\text{ellipse}} = \\sqrt{1 - \\frac{b^2}{a^2}}, \\quad e_{\\text{hyperbola}} = \\sqrt{1 + \\frac{b^2}{a^2}}",
                    quickDescription = "Eccentricity formulas governing orbital shape."
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Slope between two points: m = (y₂ - y₁) / (x₂ - x₁)",
                "Angle between two lines: tan θ = |(m₁ - m₂) / (1 + m₁m₂)|",
                "Parallel lines condition: m₁ = m₂; Perpendicular lines condition: m₁ · m₂ = -1",
                "Distance between parallel lines Ax+By+C₁=0 and Ax+By+C₂=0: d = |C₁ - C₂| / √(A² + B²)"
            ),
            realWorldApplications = listOf(
                "Keplerian planetary orbits (ellipses with the Sun at one focus)",
                "Satellite dish parabolic reflectors and car headlamp beam focus",
                "LORAN hyperbolic navigation radio beacon position fixing"
            )
        ),
        MathTopic(
            id = "m11_limits_derivatives_intro",
            grade = MathGrade.CLASS_11,
            branch = MathBranch.CALCULUS,
            chapterTitle = "Limits and Introduction to Derivatives",
            topicTitle = "Fundamental Limit Theorems & Differentiation from First Principles",
            summary = "Standard limits (lim sin x / x = 1), First Principle definition of derivative f'(x) = lim [f(x+h) - f(x)] / h, power rule, product rule, and quotient rule.",
            detailedExplanation = "Calculus is the mathematical study of continuous change. A limit describes the value a function approaches as the input approaches some value. The derivative represents the instantaneous rate of change or the slope of the tangent line.",
            keyFormulas = listOf(
                MathFormula(
                    title = "Definition of Derivative (First Principle)",
                    formulaLatex = "f'(x) = \\lim_{h \\to 0} \\frac{f(x + h) - f(x)}{h}",
                    quickDescription = "Formal limit definition of the instantaneous derivative."
                ),
                MathFormula(
                    title = "Standard Limits Suite",
                    formulaLatex = "\\lim_{x \\to 0} \\frac{\\sin x}{x} = 1, \\quad \\lim_{x \\to 0} \\frac{\\tan x}{x} = 1, \\quad \\lim_{x \\to a} \\frac{x^n - a^n}{x - a} = n a^{n-1}, \\quad \\lim_{x \\to 0} \\frac{e^x - 1}{x} = 1",
                    quickDescription = "Crucial trigonometric and algebraic standard limits."
                ),
                MathFormula(
                    title = "Product & Quotient Rules",
                    formulaLatex = "\\frac{d}{dx}(uv) = u'v + uv', \\quad \\frac{d}{dx}\\left(\\frac{u}{v}\\right) = \\frac{u'v - uv'}{v^2}",
                    quickDescription = "Differentiation rules for products and quotients of functions."
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Power Rule: d/dx(xⁿ) = n xⁿ⁻¹",
                "Trigonometric derivatives: d/dx(sin x) = cos x, d/dx(cos x) = -sin x, d/dx(tan x) = sec²x",
                "d/dx(sec x) = sec x tan x, d/dx(cot x) = -csc²x, d/dx(csc x) = -csc x cot x"
            ),
            realWorldApplications = listOf(
                "Instantaneous velocity and acceleration calculation in vehicular motion",
                "Marginal cost and revenue optimization in quantitative microeconomics"
            )
        ),

        // ==========================================
        // CLASS 12
        // ==========================================
        MathTopic(
            id = "m12_inverse_trigonometry",
            grade = MathGrade.CLASS_12,
            branch = MathBranch.TRIGONOMETRY,
            chapterTitle = "Inverse Trigonometric Functions",
            topicTitle = "Principal Value Branches & Transformational Identities",
            summary = "Principal domains and ranges for sin⁻¹x, cos⁻¹x, tan⁻¹x, complementary sum π/2 identities, and inverse tangent addition formulas.",
            detailedExplanation = "Inverse trigonometric functions are defined by restricting the domains of standard circular functions to make them bijective. For example, sin⁻¹x : [-1, 1] → [-π/2, π/2], cos⁻¹x : [-1, 1] → [0, π], and tan⁻¹x : ℝ → (-π/2, π/2).",
            keyFormulas = listOf(
                MathFormula(
                    title = "Complementary Inverse Sum Identities",
                    formulaLatex = "\\sin^{-1}x + \\cos^{-1}x = \\frac{\\pi}{2}, \\quad \\tan^{-1}x + \\cot^{-1}x = \\frac{\\pi}{2}, \\quad \\sec^{-1}x + \\csc^{-1}x = \\frac{\\pi}{2}",
                    quickDescription = "Complementary inverse trigonometric angle sum equals π/2 (90 degrees)."
                ),
                MathFormula(
                    title = "Inverse Tangent Addition Formula",
                    formulaLatex = "\\tan^{-1}x + \\tan^{-1}y = \\tan^{-1}\\left(\\frac{x + y}{1 - xy}\\right) \\quad (xy < 1)",
                    quickDescription = "Composition formula for sum of inverse tangents.",
                    symbols = mapOf("x, y" to "Real numbers satisfying xy < 1"),
                    canCalculate = true,
                    defaultInputs = mapOf("Input x" to 0.5, "Input y" to 0.3333333333333333),
                    calculateFn = { inputs ->
                        val x = inputs["Input x"] ?: 0.5
                        val y = inputs["Input y"] ?: (1.0 / 3.0)
                        atan((x + y) / (1.0 - x * y)) * (180.0 / PI) // in degrees
                    },
                    outputUnit = "degrees",
                    outputSymbol = "tan⁻¹(x) + tan⁻¹(y)"
                ),
                MathFormula(
                    title = "Multiple Angle Inverse Transforms",
                    formulaLatex = "2\\tan^{-1}x = \\sin^{-1}\\left(\\frac{2x}{1+x^2}\\right) = \\cos^{-1}\\left(\\frac{1-x^2}{1+x^2}\\right) = \\tan^{-1}\\left(\\frac{2x}{1-x^2}\\right)",
                    quickDescription = "Transformations connecting 2 tan⁻¹x to inverse sine, cosine, and tangent."
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "sin⁻¹(-x) = -sin⁻¹x; tan⁻¹(-x) = -tan⁻¹x; csc⁻¹(-x) = -csc⁻¹x",
                "cos⁻¹(-x) = π - cos⁻¹x; sec⁻¹(-x) = π - sec⁻¹x; cot⁻¹(-x) = π - cot⁻¹x",
                "tan⁻¹(1/x) = cot⁻¹x (for x > 0)"
            ),
            realWorldApplications = listOf(
                "Inverse kinematics in robotic arm joint rotation angle calculations",
                "Computer graphics ray-tracing camera field-of-view projections"
            )
        ),
        MathTopic(
            id = "m12_matrices_determinants",
            grade = MathGrade.CLASS_12,
            branch = MathBranch.VECTORS_MATRICES,
            chapterTitle = "Matrices & Determinants",
            topicTitle = "Matrix Algebra, Inverses, Adjoint & Cramer's Rule",
            summary = "Matrix multiplication, determinant evaluation, adjoint matrix formula, matrix inverse A⁻¹ = adj(A)/|A|, and solving linear systems AX = B.",
            detailedExplanation = "A matrix is a rectangular array of numbers. A square matrix A is invertible if and only if |A| ≠ 0. The inverse is given by A⁻¹ = (1/|A|) adj(A). Systems of linear equations can be solved elegantly using the matrix equation X = A⁻¹B.",
            keyFormulas = listOf(
                MathFormula(
                    title = "2x2 Matrix Determinant & Inverse",
                    formulaLatex = "|A| = ad - bc, \\quad A^{-1} = \\frac{1}{ad - bc}\\begin{pmatrix} d & -b \\\\ -c & a \\end{pmatrix}",
                    quickDescription = "Determinant and analytical inverse formula for a 2x2 matrix.",
                    symbols = mapOf("a, b, c, d" to "Elements of [[a, b], [c, d]]"),
                    canCalculate = true,
                    defaultInputs = mapOf("Element a" to 4.0, "Element b" to 7.0, "Element c" to 2.0, "Element d" to 6.0),
                    calculateFn = { inputs ->
                        val a = inputs["Element a"] ?: 4.0
                        val b = inputs["Element b"] ?: 7.0
                        val c = inputs["Element c"] ?: 2.0
                        val d = inputs["Element d"] ?: 6.0
                        a * d - b * c // Returns determinant |A|
                    },
                    outputUnit = "",
                    outputSymbol = "Determinant |A|"
                ),
                MathFormula(
                    title = "Fundamental Adjoint Theorem",
                    formulaLatex = "A \\cdot (\\text{adj } A) = (\\text{adj } A) \\cdot A = |A| I_n",
                    quickDescription = "Product of any square matrix with its classical adjoint equals determinant times identity."
                ),
                MathFormula(
                    title = "System of Linear Equations (Matrix Method)",
                    formulaLatex = "A X = B \\implies X = A^{-1} B = \\frac{1}{|A|}(\\text{adj } A)B",
                    quickDescription = "Direct algebraic solution to n-variable simultaneous linear systems."
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Transpose product rule: (AB)ᵀ = Bᵀ Aᵀ",
                "Inverse product rule: (AB)⁻¹ = B⁻¹ A⁻¹",
                "Determinant properties: |AB| = |A| |B|; |Aᵀ| = |A|; |kA| = kⁿ |A| for n×n matrix",
                "|adj A| = |A|ⁿ⁻¹; |adj(adj A)| = |A|⁽ⁿ⁻¹⁾²"
            ),
            realWorldApplications = listOf(
                "3D computer graphics 4x4 affine transformation pipelines (scaling, rotation, translation)",
                "Google PageRank eigenvector algorithm, machine learning neural network weights"
            )
        ),
        MathTopic(
            id = "m12_differentiation_calculus",
            grade = MathGrade.CLASS_12,
            branch = MathBranch.CALCULUS,
            chapterTitle = "Continuity & Differentiability",
            topicTitle = "Chain Rule, Implicit, Parametric & Standard Derivatives",
            summary = "Chain rule for composite functions, implicit differentiation, logarithmic differentiation, Mean Value Theorems (Rolle's & LMVT), and senior standard derivative catalog.",
            detailedExplanation = "Continuity requires that the limit as x approaches c from both sides equals f(c). Differentiability implies the existence of a unique tangent line. The chain rule states d/dx[f(g(x))] = f'(g(x)) · g'(x).",
            keyFormulas = listOf(
                MathFormula(
                    title = "Chain Rule of Differentiation",
                    formulaLatex = "\\frac{dy}{dx} = \\frac{dy}{du} \\cdot \\frac{du}{dx}, \\quad \\frac{d}{dx}[f(g(x))] = f'(g(x)) \\cdot g'(x)",
                    quickDescription = "Derivative of nested composite functions."
                ),
                MathFormula(
                    title = "Inverse Trigonometric Derivatives",
                    formulaLatex = "\\frac{d}{dx}(\\sin^{-1}x) = \\frac{1}{\\sqrt{1-x^2}}, \\quad \\frac{d}{dx}(\\cos^{-1}x) = -\\frac{1}{\\sqrt{1-x^2}}, \\quad \\frac{d}{dx}(\\tan^{-1}x) = \\frac{1}{1+x^2}",
                    quickDescription = "Standard derivatives of inverse circular trigonometric functions."
                ),
                MathFormula(
                    title = "Exponential & Logarithmic Derivatives",
                    formulaLatex = "\\frac{d}{dx}(e^x) = e^x, \\quad \\frac{d}{dx}(a^x) = a^x \\ln a, \\quad \\frac{d}{dx}(\\ln x) = \\frac{1}{x}, \\quad \\frac{d}{dx}(\\log_a x) = \\frac{1}{x \\ln a}",
                    quickDescription = "Derivatives of natural and general exponential and logarithmic functions."
                ),
                MathFormula(
                    title = "Lagrange's Mean Value Theorem (LMVT)",
                    formulaLatex = "f'(c) = \\frac{f(b) - f(a)}{b - a} \\quad (c \\in (a, b))",
                    quickDescription = "Guarantees existence of a point where instantaneous rate equals average rate."
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Rolle's Theorem: If f(a)=f(b), continuous on [a,b] & differentiable on (a,b), then ∃ c ∈ (a,b) such that f'(c) = 0",
                "Parametric differentiation: dy/dx = (dy/dt) / (dx/dt); d²y/dx² = [d/dt(dy/dx)] / (dx/dt)",
                "Hyperbolic derivatives: d/dx(sinh x) = cosh x, d/dx(cosh x) = sinh x, d/dx(tanh x) = sech²x"
            ),
            realWorldApplications = listOf(
                "Gradient descent optimization in artificial intelligence and deep neural network training",
                "Aerodynamic lift and drag force derivative modeling over airfoil surfaces"
            )
        ),
        MathTopic(
            id = "m12_integral_calculus",
            grade = MathGrade.CLASS_12,
            branch = MathBranch.CALCULUS,
            chapterTitle = "Integral Calculus (Indefinite & Definite)",
            topicTitle = "Integration Techniques, By Parts, Special Integrals & Properties",
            summary = "Fundamental Theorem of Calculus, Integration by Parts (ILATE rule), special algebraic/trigonometric integral formulas, and definite integral properties (King's rule).",
            detailedExplanation = "Integration is the reverse process of differentiation and computes accumulated areas. Integration by parts states ∫ u v dx = u ∫ v dx - ∫ (u' ∫ v dx) dx. Definite integrals evaluate net area under curves and possess powerful symmetric properties.",
            keyFormulas = listOf(
                MathFormula(
                    title = "Integration by Parts Formula",
                    formulaLatex = "\\int u v \\, dx = u \\int v \\, dx - \\int \\left(u' \\int v \\, dx\\right) dx",
                    quickDescription = "Product rule for integration governed by the ILATE priority order.",
                    symbols = mapOf("u" to "First function (by ILATE)", "v" to "Second function")
                ),
                MathFormula(
                    title = "Special Exponential Form",
                    formulaLatex = "\\int e^x \\left[f(x) + f'(x)\\right] dx = e^x f(x) + C",
                    quickDescription = "Immediate evaluation formula for e^x multiplied by sum of function and its derivative."
                ),
                MathFormula(
                    title = "Standard Inverse & Radical Integrals",
                    formulaLatex = "\\int \\frac{dx}{x^2 + a^2} = \\frac{1}{a}\\tan^{-1}\\left(\\frac{x}{a}\\right) + C, \\quad \\int \\frac{dx}{\\sqrt{a^2 - x^2}} = \\sin^{-1}\\left(\\frac{x}{a}\\right) + C, \\quad \\int \\frac{dx}{\\sqrt{x^2 \\pm a^2}} = \\ln|x + \\sqrt{x^2 \\pm a^2}| + C",
                    quickDescription = "Standard quadratic denominator and radical antiderivatives."
                ),
                MathFormula(
                    title = "Definite Integral Properties (King's Property)",
                    formulaLatex = "\\int_a^b f(x)\\,dx = \\int_a^b f(a + b - x)\\,dx, \\quad \\int_0^a f(x)\\,dx = \\int_0^a f(a - x)\\,dx",
                    quickDescription = "Symmetry transformation for definite integrals."
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Even/Odd property: ∫₋ₐᵃ f(x) dx = 2∫₀ᵃ f(x) dx (if f is even), and = 0 (if f is odd)",
                "Periodic function property: If f(x + T) = f(x), then ∫₀ⁿᵀ f(x) dx = n ∫₀ᵀ f(x) dx",
                "Leibniz Rule for differentiating an integral: d/dx [∫_{u(x)}^{v(x)} f(t) dt] = f(v(x)) v'(x) - f(u(x)) u'(x)"
            ),
            realWorldApplications = listOf(
                "Calculating centers of mass, moments of inertia, and gravitational fields",
                "Cumulative probability density function integration and financial option pricing"
            )
        ),
        MathTopic(
            id = "m12_differential_equations",
            grade = MathGrade.CLASS_12,
            branch = MathBranch.CALCULUS,
            chapterTitle = "Differential Equations & Applications of Integrals",
            topicTitle = "First-Order Linear Differential Equations & Area Under Curve",
            summary = "Order and degree, variable separable method, integrating factor IF = e^(∫P dx), solution y · IF = ∫ (Q · IF) dx + C, and area between two curves.",
            detailedExplanation = "A differential equation relates a function with its derivatives. A linear first-order differential equation has the standard form dy/dx + P(x)y = Q(x), solved by multiplying through by the integrating factor IF = exp(∫ P(x) dx). Area bounded between two curves y = f(x) and y = g(x) from a to b is A = ∫ₐᵇ |f(x) - g(x)| dx.",
            keyFormulas = listOf(
                MathFormula(
                    title = "First-Order Linear Differential Equation",
                    formulaLatex = "\\frac{dy}{dx} + P(x)y = Q(x) \\implies IF = e^{\\int P(x)dx}, \\quad y \\cdot IF = \\int \\left(Q(x) \\cdot IF\\right) dx + C",
                    quickDescription = "Integrating factor method for solving linear ODEs."
                ),
                MathFormula(
                    title = "Area Between Two Curves",
                    formulaLatex = "A = \\int_a^b \\left| f(x) - g(x) \\right| dx",
                    quickDescription = "Enclosed planar area between upper curve f(x) and lower curve g(x)."
                ),
                MathFormula(
                    title = "Exponential Growth and Decay Law",
                    formulaLatex = "\\frac{dy}{dt} = k y \\implies y(t) = y_0 e^{kt}",
                    quickDescription = "Continuous population growth or radioactive decay differential model.",
                    symbols = mapOf("y₀" to "Initial quantity", "k" to "Growth rate constant", "t" to "Elapsed time"),
                    canCalculate = true,
                    defaultInputs = mapOf("Initial y₀" to 100.0, "Rate k" to 0.05, "Time t" to 10.0),
                    calculateFn = { inputs ->
                        val y0 = inputs["Initial y₀"] ?: 100.0
                        val k = inputs["Rate k"] ?: 0.05
                        val t = inputs["Time t"] ?: 10.0
                        y0 * exp(k * t)
                    },
                    outputUnit = "units",
                    outputSymbol = "Quantity y(t)"
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Homogeneous Differential Equations: dy/dx = F(y/x) is solved by substituting y = vx",
                "Separable Variables: dy/g(y) = f(x)dx ⟹ ∫ dy/g(y) = ∫ f(x)dx + C",
                "Newton's Law of Cooling: dT/dt = -k(T - T_ambient)"
            ),
            realWorldApplications = listOf(
                "Radioactive carbon-14 dating and pharmacokinetic drug concentration elimination in bloodstream",
                "RLC circuit transient discharge analysis and mechanical vibration damping"
            )
        ),
        MathTopic(
            id = "m12_vectors_3d",
            grade = MathGrade.CLASS_12,
            branch = MathBranch.VECTORS_MATRICES,
            chapterTitle = "Vector Algebra & 3D Geometry",
            topicTitle = "Dot Product, Cross Product, Skew Lines & Plane Equations",
            summary = "Dot product a · b = |a||b| cos θ, cross product a × b, scalar triple product [a b c], shortest distance between skew lines, and vector equations of planes.",
            detailedExplanation = "Vectors represent magnitude and direction in 3-dimensional space. The scalar dot product measures parallelism and projection, while the vector cross product produces an orthogonal vector with magnitude equal to the parallelogram area. Two non-parallel, non-intersecting lines in 3D are called skew lines.",
            keyFormulas = listOf(
                MathFormula(
                    title = "Dot (Scalar) Product",
                    formulaLatex = "\\vec{a} \\cdot \\vec{b} = |\\vec{a}||\\vec{b}|\\cos\\theta = a_1 b_1 + a_2 b_2 + a_3 b_3",
                    quickDescription = "Scalar product of two 3D vectors.",
                    symbols = mapOf("a₁,a₂,a₃" to "Components of vector a", "b₁,b₂,b₃" to "Components of vector b"),
                    canCalculate = true,
                    defaultInputs = mapOf("a₁" to 1.0, "a₂" to 2.0, "a₃" to 3.0, "b₁" to 4.0, "b₂" to 5.0, "b₃" to 6.0),
                    calculateFn = { inputs ->
                        val a1 = inputs["a₁"] ?: 1.0
                        val a2 = inputs["a₂"] ?: 2.0
                        val a3 = inputs["a₃"] ?: 3.0
                        val b1 = inputs["b₁"] ?: 4.0
                        val b2 = inputs["b₂"] ?: 5.0
                        val b3 = inputs["b₃"] ?: 6.0
                        a1 * b1 + a2 * b2 + a3 * b3
                    },
                    outputUnit = "",
                    outputSymbol = "Dot Product a · b"
                ),
                MathFormula(
                    title = "Cross (Vector) Product",
                    formulaLatex = "\\vec{a} \\times \\vec{b} = |\\vec{a}||\\vec{b}|\\sin\\theta \\hat{n} = \\begin{vmatrix} \\hat{i} & \\hat{j} & \\hat{k} \\\\ a_1 & a_2 & a_3 \\\\ b_1 & b_2 & b_3 \\end{vmatrix}",
                    quickDescription = "Vector orthogonal to both a and b with magnitude equal to parallelogram area."
                ),
                MathFormula(
                    title = "Shortest Distance Between Skew Lines",
                    formulaLatex = "d = \\frac{\\left|(\\vec{a}_2 - \\vec{a}_1) \\cdot (\\vec{b}_1 \\times \\vec{b}_2)\\right|}{|\\vec{b}_1 \\times \\vec{b}_2|}",
                    quickDescription = "Exact minimum perpendicular distance between two non-intersecting non-parallel lines in 3D."
                ),
                MathFormula(
                    title = "Equation of Plane & Distance of Point",
                    formulaLatex = "\\vec{r} \\cdot \\hat{n} = d \\implies Ax + By + Cz + D = 0, \\quad d_{\\text{pt}} = \\frac{|Ax_1 + By_1 + Cz_1 + D|}{\\sqrt{A^2 + B^2 + C^2}}",
                    quickDescription = "Cartesian and vector plane equations with perpendicular point distance."
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Projection of vector a on vector b: proj = (a · b) / |b|",
                "Perpendicular condition: a · b = 0; Parallel condition: a × b = 0",
                "Scalar Triple Product: [a b c] = a · (b × c) = Volume of parallelepiped formed by vectors",
                "Vector Triple Product: a × (b × c) = (a · c)b - (a · b)c (BAC-CAB rule)",
                "Direction cosines: l² + m² + n² = 1, where l = cos α, m = cos β, n = cos γ"
            ),
            realWorldApplications = listOf(
                "Game physics engine rigid body torque (τ = r × F) and angular momentum simulations",
                "Aircraft navigation flight trajectories, missile guidance vectors, and 3D satellite orbits"
            )
        ),
        MathTopic(
            id = "m12_probability_senior",
            grade = MathGrade.CLASS_12,
            branch = MathBranch.PROBABILITY_STATS,
            chapterTitle = "Senior Probability & Random Variables",
            topicTitle = "Bayes' Theorem, Conditional Probability & Binomial Distribution",
            summary = "Conditional probability P(A|B) = P(A∩B)/P(B), Bayes' Theorem for reverse conditional posterior probability, and Bernoulli Binomial distribution P(X=k) = ⁿCₖ pᵏ qⁿ⁻ᵏ.",
            detailedExplanation = "Bayes' Theorem provides a mathematical framework for updating the prior probability of an event based on new acquired evidence. A Bernoulli trial consists of n independent repeated trials with success probability p and failure probability q = 1 - p.",
            keyFormulas = listOf(
                MathFormula(
                    title = "Bayes' Theorem Formula",
                    formulaLatex = "P(E_i | A) = \\frac{P(E_i) P(A | E_i)}{\\sum_{j=1}^n P(E_j) P(A | E_j)}",
                    quickDescription = "Posterior probability of event Ei given observed evidence A."
                ),
                MathFormula(
                    title = "Conditional Probability Definition",
                    formulaLatex = "P(A | B) = \\frac{P(A \\cap B)}{P(B)} \\quad (P(B) > 0)",
                    quickDescription = "Probability of event A occurring given that event B has occurred.",
                    symbols = mapOf("P(A ∩ B)" to "Joint probability", "P(B)" to "Prior probability of condition"),
                    canCalculate = true,
                    defaultInputs = mapOf("Joint P(A∩B)" to 0.2, "Condition P(B)" to 0.5),
                    calculateFn = { inputs ->
                        val joint = inputs["Joint P(A∩B)"] ?: 0.2
                        val pb = inputs["Condition P(B)"] ?: 0.5
                        if (pb > 0) joint / pb else 0.0
                    },
                    outputUnit = "",
                    outputSymbol = "Conditional P(A|B)"
                ),
                MathFormula(
                    title = "Binomial Distribution Formula",
                    formulaLatex = "P(X = k) = \\binom{n}{k} p^k (1 - p)^{n-k}, \\quad \\mu = n p, \\quad \\sigma^2 = n p (1 - p)",
                    quickDescription = "Probability of exactly k successes in n independent Bernoulli trials."
                )
            ),
            importantIdentitiesAndTheorems = listOf(
                "Multiplication Theorem: P(A ∩ B) = P(A) · P(B|A) = P(B) · P(A|B)",
                "Independent Events Condition: P(A ∩ B) = P(A) · P(B) ⟹ P(A|B) = P(A)",
                "Total Probability Theorem: P(A) = ∑ P(Eᵢ) P(A|Eᵢ) for partition E₁, ..., Eₙ",
                "Expectation of Discrete Random Variable: E(X) = ∑ xᵢ pᵢ; Var(X) = E(X²) - [E(X)]²"
            ),
            realWorldApplications = listOf(
                "Medical diagnostic accuracy testing (false positive/negative sensitivity analysis using Bayes' Theorem)",
                "Machine learning spam classifiers (Naïve Bayes) and risk underwriting in insurance"
            )
        )
    )
}
