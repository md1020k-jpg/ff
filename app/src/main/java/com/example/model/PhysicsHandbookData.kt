package com.example.model

/**
 * Data models representing Physics concepts, formulas, and explanations
 * across all school curricula from Class 6 to Class 12.
 */

enum class PhysicsGrade(val displayName: String, val classNumber: Int, val description: String) {
    CLASS_6("Class 6", 6, "Foundations of Motion, Light, Electricity & Magnets"),
    CLASS_7("Class 7", 7, "Motion, Heat, Electric Currents, Light & Lenses"),
    CLASS_8("Class 8", 8, "Force, Pressure, Friction, Sound, Light & Electrostatics"),
    CLASS_9("Class 9", 9, "Kinematics, Newton's Laws, Gravitation, Work-Energy & Sound"),
    CLASS_10("Class 10", 10, "Optics, Electricity, Electromagnetism & Sources of Energy"),
    CLASS_11("Class 11", 11, "Mechanics, Fluid Statics, Thermodynamics, SHM & Waves"),
    CLASS_12("Class 12", 12, "Electrostatics, Magnetism, Optics, Modern Physics & Semiconductors")
}

enum class PhysicsBranch(val displayName: String) {
    MECHANICS("Mechanics & Motion"),
    OPTICS("Optics & Light"),
    THERMODYNAMICS("Heat & Thermodynamics"),
    ELECTROMAGNETISM("Electricity & Magnetism"),
    WAVES_SOUND("Waves & Oscillations"),
    MODERN_PHYSICS("Modern & Nuclear Physics"),
    FLUIDS_MATTER("Fluids & Matter Properties")
}

data class PhysicsFormula(
    val title: String,
    val formulaLatex: String,
    val symbols: List<Pair<String, String>>, // Symbol to meaning/unit, e.g. "v" to "Final velocity (m/s)"
    val quickDescription: String,
    val canCalculate: Boolean = false,
    val defaultInputs: Map<String, Double> = emptyMap(),
    val calculateFn: ((Map<String, Double>) -> Double)? = null,
    val outputSymbol: String = "",
    val outputUnit: String = ""
)

data class PhysicsTopic(
    val id: String,
    val grade: PhysicsGrade,
    val branch: PhysicsBranch,
    val chapterTitle: String,
    val topicTitle: String,
    val summary: String,
    val detailedExplanation: String,
    val keyFormulas: List<PhysicsFormula>,
    val importantLawsAndRules: List<String>,
    val realWorldApplications: List<String>
)

object PhysicsHandbookRepository {

    val allTopics: List<PhysicsTopic> = listOf(
        // =========================================================================
        // CLASS 6
        // =========================================================================
        PhysicsTopic(
            id = "c6_motion_measurement",
            grade = PhysicsGrade.CLASS_6,
            branch = PhysicsBranch.MECHANICS,
            chapterTitle = "Motion and Measurement of Distances",
            topicTitle = "Standard Units & Types of Motion",
            summary = "Understanding standard SI units of length and distinguishing between rectilinear, circular, and periodic motions.",
            detailedExplanation = """
                Measurement is the comparison of an unknown quantity with some known fixed quantity (a unit).
                
                • International System of Units (SI): Standardized worldwide. Standard unit of length is the meter (m).
                  - 1 kilometer (km) = 1,000 meters (m)
                  - 1 meter (m) = 100 centimeters (cm)
                  - 1 centimeter (cm) = 10 millimeters (mm)
                
                Types of Motion:
                1. Rectilinear Motion: Motion along a straight line (e.g., marching soldiers, falling stone).
                2. Circular Motion: Motion where distance from a fixed center remains constant (e.g., blades of a fan, hands of a clock).
                3. Periodic Motion: Motion that repeats itself after regular intervals of time (e.g., pendulum of a clock, strings of a guitar, branch of a tree moving back and forth).
                4. Rotational Motion: An object turning around its own internal axis without changing place.
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "Speed Definition",
                    formulaLatex = "Speed = Distance / Time",
                    symbols = listOf("Distance" to "Length traveled (m)", "Time" to "Time taken (s)", "Speed" to "Rate of distance traveled (m/s)"),
                    quickDescription = "Basic rate of covering distance per unit time.",
                    canCalculate = true,
                    defaultInputs = mapOf("Distance (m)" to 100.0, "Time (s)" to 10.0),
                    calculateFn = { inputs -> (inputs["Distance (m)"] ?: 0.0) / (inputs["Time (s)"] ?: 1.0) },
                    outputSymbol = "Speed",
                    outputUnit = "m/s"
                ),
                PhysicsFormula(
                    title = "Metric Length Conversion",
                    formulaLatex = "1 km = 1000 m = 100,000 cm = 1,000,000 mm",
                    symbols = listOf("km" to "Kilometers", "m" to "Meters", "cm" to "Centimeters", "mm" to "Millimeters"),
                    quickDescription = "Fundamental SI unit relations for distance."
                )
            ),
            importantLawsAndRules = listOf(
                "SI Unit of length is Meter (m).",
                "SI Unit of time is Second (s).",
                "An object can exhibit multiple types of motion simultaneously (e.g., a rolling ball has both rotational and rectilinear motion)."
            ),
            realWorldApplications = listOf(
                "Distance calibration on roadways and maps.",
                "Designing pendulum clocks and metronomes based on periodic motion.",
                "Rotational mechanisms in wheels and turbines."
            )
        ),
        PhysicsTopic(
            id = "c6_light_shadows",
            grade = PhysicsGrade.CLASS_6,
            branch = PhysicsBranch.OPTICS,
            chapterTitle = "Light, Shadows and Reflections",
            topicTitle = "Rectilinear Propagation & Shadow Formation",
            summary = "Properties of transparent, translucent, opaque objects, pinhole cameras, and reflection.",
            detailedExplanation = """
                Light is a form of energy that enables us to see objects around us.
                
                Classification of Objects:
                • Luminous: Emit light of their own (e.g., Sun, torch bulb, stars, candle flame).
                • Non-luminous: Do not emit light, visible only by reflecting light (e.g., Moon, book, table).
                • Transparent: Allow light to pass completely (clear glass, clean water, air).
                • Translucent: Allow light to pass partially; objects seen hazily (butter paper, frosted glass).
                • Opaque: Block light completely, casting shadows (wood, metal sheet, cardboard).
                
                Shadows & Pinhole Camera:
                • Shadows require: (1) Source of light, (2) Opaque obstacle, (3) Screen behind the obstacle.
                • Rectilinear Propagation: Light travels in straight lines.
                • Pinhole Camera produces an inverted, real image due to straight-line light travel.
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "Law of Plane Mirror Reflection",
                    formulaLatex = "Angle of Incidence (i) = Angle of Reflection (r)",
                    symbols = listOf("i" to "Angle between incident ray and normal", "r" to "Angle between reflected ray and normal"),
                    quickDescription = "Angle of incident ray equals the angle of the reflected ray."
                )
            ),
            importantLawsAndRules = listOf(
                "Light travels strictly along straight lines (Rectilinear propagation).",
                "Shadows are always dark regardless of the color of the opaque object.",
                "Images in a plane mirror are erect, virtual, same size, and laterally inverted."
            ),
            realWorldApplications = listOf(
                "Pinhole cameras and camera obscura photography.",
                "Periscopes for submarine navigation and rear-view mirrors.",
                "Solar and Lunar eclipse predictions."
            )
        ),
        PhysicsTopic(
            id = "c6_electricity_circuits",
            grade = PhysicsGrade.CLASS_6,
            branch = PhysicsBranch.ELECTROMAGNETISM,
            chapterTitle = "Electricity and Circuits",
            topicTitle = "Electric Cells, Closed Circuits & Conductors",
            summary = "Electric cell terminals, closed vs open circuits, switches, conductors, and insulators.",
            detailedExplanation = """
                Electricity is the flow of electric charge through a conductive path.
                
                Components of an Electric Circuit:
                • Electric Cell: Generates electricity from stored chemical energy. Has two terminals: Positive (+) metal cap and Negative (-) metal disc.
                • Filament of a Bulb: Thin tungsten wire that glows when electric current passes through it.
                • Switch: Device used to close (complete) or open (break) the circuit.
                
                Conductors vs Insulators:
                • Conductors: Materials that allow electric current to pass through them freely (Copper, Aluminum, Iron, Gold, graphite, tap water).
                • Insulators: Materials that do not allow electric current to pass through them (Rubber, Plastic, Wood, Glass, dry air, pure distilled water).
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "Electric Current Direction",
                    formulaLatex = "Conventional Current Direction: Positive (+) to Negative (-)",
                    symbols = listOf("Positive Terminal" to "High potential (+)", "Negative Terminal" to "Low potential (-)"),
                    quickDescription = "Current flows from positive to negative outside the cell."
                )
            ),
            importantLawsAndRules = listOf(
                "Electric current flows only in a closed and complete circuit.",
                "If a bulb filament is broken (fused), the circuit is broken and current ceases.",
                "Human body is a good conductor of electricity; never touch exposed electrical wires."
            ),
            realWorldApplications = listOf(
                "Flashlight torches and battery-operated emergency lighting.",
                "Insulating plastic coating on electrical cables to prevent electric shocks.",
                "Household switches controlling fans and lights."
            )
        ),
        PhysicsTopic(
            id = "c6_magnets",
            grade = PhysicsGrade.CLASS_6,
            branch = PhysicsBranch.ELECTROMAGNETISM,
            chapterTitle = "Fun with Magnets",
            topicTitle = "Magnetic Poles, Attraction & Compass",
            summary = "Magnetic vs non-magnetic materials, North/South poles, magnetic compass navigation.",
            detailedExplanation = """
                A magnet is an object that attracts magnetic materials such as Iron, Nickel, and Cobalt.
                
                Key Properties of Magnets:
                • Two Magnetic Poles: Every magnet has a North Pole (N) and a South Pole (S).
                • Maximum Strength: Magnetic attraction is strongest near the poles.
                • Magnetic Alignment: A freely suspended bar magnet always rests in the North-South geographic direction.
                • Fundamental Law of Magnetism: Like poles repel (N-N, S-S); Opposite poles attract (N-S).
                • Magnetic Compass: A magnetized needle pivoted inside a dial, used by sailors and explorers to find geographic directions.
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "Magnetic Interaction Rule",
                    formulaLatex = "Like Poles Repel (N-N / S-S) | Unlike Poles Attract (N-S)",
                    symbols = listOf("N" to "North magnetic pole", "S" to "South magnetic pole"),
                    quickDescription = "Universal law of magnetic forces between poles."
                )
            ),
            importantLawsAndRules = listOf(
                "Magnetic poles always exist in pairs (monopoles do not exist).",
                "Heating, hammering, or dropping a magnet from a height causes demagnetization.",
                "Magnetic compass needle aligns with Earth's magnetic field."
            ),
            realWorldApplications = listOf(
                "Magnetic compass for maritime and aviation navigation.",
                "Refrigerator door magnetic gaskets.",
                "Magnetic separation in recycling plants to separate scrap iron."
            )
        ),

        // =========================================================================
        // CLASS 7
        // =========================================================================
        PhysicsTopic(
            id = "c7_motion_time",
            grade = PhysicsGrade.CLASS_7,
            branch = PhysicsBranch.MECHANICS,
            chapterTitle = "Motion and Time",
            topicTitle = "Speed, Uniform Motion & Simple Pendulum",
            summary = "Speed calculations, distance-time graphs, simple pendulum time period formula.",
            detailedExplanation = """
                Speed is the distance covered by an object in a unit time.
                
                • Uniform Motion: An object moving along a straight line with a constant speed.
                • Non-Uniform Motion: Speed of the object changes with time.
                
                The Simple Pendulum:
                • Consists of a small metallic ball (bob) suspended from a rigid stand by a light thread.
                • Oscillation: One complete to-and-fro motion (from extreme position A to B and back to A).
                • Time Period (T): Time taken by the pendulum to complete one full oscillation.
                • Galileo Galilei discovered that the time period of a simple pendulum of a given length is constant.
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "Average Speed Formula",
                    formulaLatex = "v = d / t",
                    symbols = listOf("v" to "Speed (m/s or km/h)", "d" to "Total distance (m)", "t" to "Total time taken (s)"),
                    quickDescription = "Speed equals total distance divided by total time.",
                    canCalculate = true,
                    defaultInputs = mapOf("Distance d (m)" to 300.0, "Time t (s)" to 15.0),
                    calculateFn = { inputs -> (inputs["Distance d (m)"] ?: 0.0) / (inputs["Time t (s)"] ?: 1.0) },
                    outputSymbol = "Speed (v)",
                    outputUnit = "m/s"
                ),
                PhysicsFormula(
                    title = "Speed Conversion (km/h to m/s)",
                    formulaLatex = "1 km/h = (5 / 18) m/s  |  1 m/s = (18 / 5) km/h",
                    symbols = listOf("km/h" to "Kilometers per hour", "m/s" to "Meters per second"),
                    quickDescription = "Multiply km/h by 5/18 to get speed in m/s."
                ),
                PhysicsFormula(
                    title = "Simple Pendulum Time Period",
                    formulaLatex = "T = 2π √(L / g)",
                    symbols = listOf("T" to "Time period (s)", "L" to "Length of pendulum (m)", "g" to "Acceleration due to gravity ≈ 9.8 m/s²"),
                    quickDescription = "Time period depends only on length and gravity, independent of bob mass.",
                    canCalculate = true,
                    defaultInputs = mapOf("Length L (m)" to 1.0, "Gravity g (m/s²)" to 9.8),
                    calculateFn = { inputs -> 2.0 * Math.PI * Math.sqrt((inputs["Length L (m)"] ?: 1.0) / (inputs["Gravity g (m/s²)"] ?: 9.8)) },
                    outputSymbol = "Time Period (T)",
                    outputUnit = "seconds"
                )
            ),
            importantLawsAndRules = listOf(
                "Distance-time graph for uniform motion is a straight line sloping upwards.",
                "Slope of distance-time graph represents speed.",
                "Time period of a simple pendulum does not depend on the mass or material of the bob."
            ),
            realWorldApplications = listOf(
                "Automobile speedometers (speed measurement) and odometers (distance recorded).",
                "Grandfather pendulum clocks for timekeeping.",
                "GPS tracking algorithms computing speed and estimated arrival time."
            )
        ),
        PhysicsTopic(
            id = "c7_heat_temperature",
            grade = PhysicsGrade.CLASS_7,
            branch = PhysicsBranch.THERMODYNAMICS,
            chapterTitle = "Heat and Temperature",
            topicTitle = "Temperature Scales & Modes of Heat Transfer",
            summary = "Celsius/Fahrenheit scales, Conduction, Convection, Radiation, Sea/Land breeze.",
            detailedExplanation = """
                Heat is a form of thermal energy that flows spontaneously from a body at higher temperature to one at lower temperature.
                
                Temperature Scales:
                • Celsius (°C): Water freezes at 0°C, boils at 100°C.
                • Fahrenheit (°F): Water freezes at 32°F, boils at 212°F.
                • Kelvin (K): SI unit of temperature. Absolute zero is 0 K (-273.15°C).
                
                Modes of Heat Transfer:
                1. Conduction: Transfer of heat through matter without actual movement of particles (dominant in solids).
                2. Convection: Transfer of heat by actual physical movement of fluid particles (liquids and gases). Drives Sea Breeze (day) and Land Breeze (night).
                3. Radiation: Transfer of heat energy via electromagnetic waves without requiring any material medium (e.g., solar heat reaching Earth across vacuum).
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "Celsius to Fahrenheit Conversion",
                    formulaLatex = "F = (9/5) × C + 32  |  C = (F - 32) × 5/9",
                    symbols = listOf("C" to "Temperature in Celsius (°C)", "F" to "Temperature in Fahrenheit (°F)"),
                    quickDescription = "Standard formula to convert between Celsius and Fahrenheit.",
                    canCalculate = true,
                    defaultInputs = mapOf("Temperature (°C)" to 37.0),
                    calculateFn = { inputs -> (9.0 / 5.0) * (inputs["Temperature (°C)"] ?: 0.0) + 32.0 },
                    outputSymbol = "Temperature (F)",
                    outputUnit = "°F"
                ),
                PhysicsFormula(
                    title = "Celsius to Kelvin Conversion",
                    formulaLatex = "K = C + 273.15",
                    symbols = listOf("K" to "Kelvin (absolute temperature)", "C" to "Celsius (°C)"),
                    quickDescription = "SI scale conversion to absolute Kelvin."
                )
            ),
            importantLawsAndRules = listOf(
                "Heat always transfers from a hotter region to a colder region until thermal equilibrium is reached.",
                "Dark and black surfaces absorb and radiate heat much faster than shiny white surfaces.",
                "Clinical thermometer range: 35°C to 42°F (94°F to 108°F)."
            ),
            realWorldApplications = listOf(
                "Wearing light-colored cotton clothes in summer and dark woolen clothes in winter.",
                "Thermos vacuum flasks preventing conduction, convection, and radiation heat loss.",
                "Cooking utensils made of metal (good conductor) with handles of bakelite/wood (insulators)."
            )
        ),
        PhysicsTopic(
            id = "c7_electric_current_effects",
            grade = PhysicsGrade.CLASS_7,
            branch = PhysicsBranch.ELECTROMAGNETISM,
            chapterTitle = "Electric Current and Its Effects",
            topicTitle = "Heating & Magnetic Effects of Current, Electromagnets",
            summary = "Heating effect (Joule's heat), electric fuse, magnetic effect (Oersted experiment), electromagnets, electric bell.",
            detailedExplanation = """
                1. Heating Effect of Electric Current:
                When electric current flows through a high-resistance wire (such as Nichrome), the wire becomes hot and produces heat.
                • Used in: Electric heaters, toasters, irons, geysers, hair dryers.
                • Electric Fuse: Safety device containing a wire with low melting point that melts and breaks the circuit when excessive current flows.
                • Miniature Circuit Breakers (MCBs): Modern electromagnetic safety switches replacing traditional fuses.
                
                2. Magnetic Effect of Electric Current:
                Hans Christian Oersted (1820) discovered that an electric current carrying wire produces a magnetic field around it, deflecting a nearby compass needle.
                • Electromagnet: A coil of insulated wire wound around a soft iron core that behaves as a strong magnet when current passes through it.
                • Electric Bell: Works on an electromagnet pulling a soft iron armature with a striker.
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "Electric Power Dissipation (Elementary)",
                    formulaLatex = "H = I² × R × t",
                    symbols = listOf("H" to "Heat energy produced (Joules)", "I" to "Electric current (Amperes)", "R" to "Resistance (Ohms)", "t" to "Time duration (seconds)"),
                    quickDescription = "Heat produced is proportional to square of current, resistance, and time."
                )
            ),
            importantLawsAndRules = listOf(
                "An electromagnet's strength increases with: (1) Increasing current, (2) Increasing number of coil turns.",
                "Electric fuse wire must have low melting point and appropriate current rating.",
                "Current produces a magnetic field in concentric circles around the wire."
            ),
            realWorldApplications = listOf(
                "Cranes with large electromagnets to lift and move heavy scrap metal.",
                "Electric bells, magnetic relays, and solenoid valves.",
                "MCBs protecting household appliances from electrical short-circuits and overloads."
            )
        ),
        PhysicsTopic(
            id = "c7_light_reflection_lenses",
            grade = PhysicsGrade.CLASS_7,
            branch = PhysicsBranch.OPTICS,
            chapterTitle = "Light and Lenses",
            topicTitle = "Spherical Mirrors, Convex/Concave Lenses & Prism Dispersion",
            summary = "Concave/convex mirrors and lenses, real vs virtual images, rainbow and Newton's disc.",
            detailedExplanation = """
                Spherical Mirrors:
                • Concave Mirror: Reflecting surface is curved inward. Can form real, inverted images as well as virtual, magnified images (used by dentists and in car headlights).
                • Convex Mirror: Reflecting surface is curved outward. Always forms virtual, erect, and diminished images with a wider field of view (used as vehicle side mirrors).
                
                Lenses:
                • Convex Lens (Converging): Thicker in the middle than edges. Converges light rays. Used as magnifying glass, in cameras, and microscopes.
                • Concave Lens (Diverging): Thinner in the middle than edges. Diverges light rays. Always forms virtual, erect, and smaller images.
                
                Dispersion of Light:
                • Sunlight is white light composed of seven colors: VIBGYOR (Violet, Indigo, Blue, Green, Yellow, Orange, Red).
                • A glass prism splits white light into its spectrum.
                • Newton's Colour Disc: When rotated rapidly, 7 colors mix to appear white.
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "White Light Spectrum",
                    formulaLatex = "White Light = VIBGYOR (Violet to Red)",
                    symbols = listOf("VIBGYOR" to "Violet, Indigo, Blue, Green, Yellow, Orange, Red"),
                    quickDescription = "Dispersion splits white light into seven spectral colors."
                )
            ),
            importantLawsAndRules = listOf(
                "Real images can be captured on a screen; Virtual images cannot be captured on a screen.",
                "Concave mirror converges light; Convex mirror diverges light.",
                "Red light bends least in a prism; Violet light bends most."
            ),
            realWorldApplications = listOf(
                "Dentist inspection mirrors and solar cookers (Concave mirrors).",
                "Rear-view mirrors on cars and motorcycles (Convex mirrors).",
                "Eyeglasses for correcting vision, magnifying lenses, and projectors."
            )
        ),

        // =========================================================================
        // CLASS 8
        // =========================================================================
        PhysicsTopic(
            id = "c8_force_pressure",
            grade = PhysicsGrade.CLASS_8,
            branch = PhysicsBranch.MECHANICS,
            chapterTitle = "Force and Pressure",
            topicTitle = "Types of Forces & Pressure in Fluids and Atmosphere",
            summary = "Contact vs non-contact forces, Pressure = Force/Area, atmospheric and liquid pressure.",
            detailedExplanation = """
                Force is a push or pull upon an object resulting from its interaction with another object.
                • SI Unit of Force: Newton (N).
                
                Classification of Forces:
                1. Contact Forces: Muscular force, Frictional force, Mechanical force, Tension.
                2. Non-Contact Forces: Gravitational force, Electrostatic force, Magnetic force.
                
                Pressure:
                • The force acting perpendicularly on a unit surface area: Pressure = Force / Area.
                • SI Unit: Pascal (Pa), where 1 Pa = 1 N/m².
                • A smaller contact area exerts larger pressure for the same force (e.g., sharp knife cuts better than a blunt one).
                
                Fluid & Atmospheric Pressure:
                • Liquids exert pressure on the walls and base of containers; pressure increases with liquid depth.
                • Atmospheric Pressure: The pressure exerted by the weight of the air column above Earth's surface (≈ 101.3 kPa at sea level).
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "Pressure Formula",
                    formulaLatex = "P = F / A",
                    symbols = listOf("P" to "Pressure (Pascal, Pa)", "F" to "Perpendicular Force (N)", "A" to "Contact Surface Area (m²)"),
                    quickDescription = "Pressure is force distributed per unit area.",
                    canCalculate = true,
                    defaultInputs = mapOf("Force F (N)" to 500.0, "Area A (m²)" to 0.05),
                    calculateFn = { inputs -> (inputs["Force F (N)"] ?: 0.0) / (inputs["Area A (m²)"] ?: 1.0) },
                    outputSymbol = "Pressure (P)",
                    outputUnit = "Pa (N/m²)"
                ),
                PhysicsFormula(
                    title = "Liquid Pressure at Depth",
                    formulaLatex = "P = h × ρ × g",
                    symbols = listOf("h" to "Depth below liquid surface (m)", "ρ" to "Liquid density (kg/m³)", "g" to "Acceleration due to gravity ≈ 9.8 m/s²"),
                    quickDescription = "Hydrostatic pressure increases linearly with depth in a fluid.",
                    canCalculate = true,
                    defaultInputs = mapOf("Depth h (m)" to 10.0, "Density ρ (kg/m³)" to 1000.0, "Gravity g (m/s²)" to 9.8),
                    calculateFn = { inputs -> (inputs["Depth h (m)"] ?: 0.0) * (inputs["Density ρ (kg/m³)"] ?: 1000.0) * (inputs["Gravity g (m/s²)"] ?: 9.8) },
                    outputSymbol = "Hydrostatic Pressure (P)",
                    outputUnit = "Pa"
                )
            ),
            importantLawsAndRules = listOf(
                "Pressure is inversely proportional to the area of contact for a given force.",
                "Liquids exert equal pressure at the same depth in all directions.",
                "Atmospheric pressure decreases with increasing altitude above sea level."
            ),
            realWorldApplications = listOf(
                "Wide straps on school bags to reduce pressure on shoulders.",
                "Dams are built thicker at the base to withstand higher hydrostatic pressure at greater depths.",
                "Suction cups, drinking straws, and syringes operating on atmospheric pressure differential."
            )
        ),
        PhysicsTopic(
            id = "c8_friction",
            grade = PhysicsGrade.CLASS_8,
            branch = PhysicsBranch.MECHANICS,
            chapterTitle = "Friction",
            topicTitle = "Static, Sliding, Rolling Friction & Fluid Drag",
            summary = "Causes of friction, friction comparison (Static > Sliding > Rolling), methods to increase/reduce friction.",
            detailedExplanation = """
                Friction is the opposing force that comes into play when two surfaces in contact move or tend to move relative to each other.
                • Cause of Friction: Microscopic irregularities, interlocking of ridges and valleys on touching surfaces.
                
                Types of Friction:
                1. Static Friction: Opposing force when an object is just at the verge of moving. Maximum static friction is called limiting friction.
                2. Sliding (Kinetic) Friction: Friction when an object slides over another surface. (Sliding < Static).
                3. Rolling Friction: Friction when an object rolls over a surface. (Rolling << Sliding << Static).
                
                Fluid Friction (Drag):
                • Friction exerted by fluids (air, water, oil) on moving objects.
                • Depends on: Object's speed, shape, and nature/viscosity of fluid.
                • Streamlining: Special aerodynamic/hydrodynamic shape (like birds, fish, airplanes) to minimize fluid drag.
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "Law of Limiting Friction",
                    formulaLatex = "f_s ≤ μ_s × N  |  f_k = μ_k × N",
                    symbols = listOf("f_s" to "Static friction force (N)", "μ_s" to "Coefficient of static friction", "N" to "Normal reaction force (N)", "μ_k" to "Coefficient of kinetic friction"),
                    quickDescription = "Frictional force is proportional to the normal contact force."
                ),
                PhysicsFormula(
                    title = "Friction Hierarchy",
                    formulaLatex = "Static Friction (max) > Sliding Friction > Rolling Friction",
                    symbols = listOf("Static" to "Highest threshold", "Rolling" to "Lowest threshold"),
                    quickDescription = "Rolling friction is significantly lower than sliding friction."
                )
            ),
            importantLawsAndRules = listOf(
                "Friction always acts in the direction opposite to the relative motion between surfaces.",
                "Friction is independent of the apparent surface area of contact.",
                "Friction is a necessary evil: essential for walking, writing, braking, but causes wear, tear, and heat loss."
            ),
            realWorldApplications = listOf(
                "Ball bearings in fans, bicycles, and car wheels replacing sliding friction with rolling friction.",
                "Treads on vehicle tires and grooves on shoes to increase grip and prevent slipping.",
                "Lubricants (grease, oil, graphite) forming a thin layer to separate irregular surfaces."
            )
        ),
        PhysicsTopic(
            id = "c8_sound_waves",
            grade = PhysicsGrade.CLASS_8,
            branch = PhysicsBranch.WAVES_SOUND,
            chapterTitle = "Sound",
            topicTitle = "Vibrations, Pitch, Loudness & Audible Frequency",
            summary = "Sound propagation in media, amplitude vs loudness (dB), frequency vs pitch (Hz), human ear anatomy.",
            detailedExplanation = """
                Sound is mechanical energy produced by vibrating objects and propagated as longitudinal pressure waves.
                
                Key Characteristics of Sound:
                • Amplitude: Maximum displacement of vibrating particle from mean position.
                  - Loudness ∝ (Amplitude)²; measured in Decibels (dB).
                • Frequency (f): Number of complete oscillations/vibrations per second.
                  - Unit: Hertz (Hz). 1 Hz = 1 vibration per second.
                  - Pitch (Shrillness) depends directly on frequency: High frequency = High pitch (whistle, female voice); Low frequency = Low pitch / bass (drum, male voice).
                • Speed of Sound: Fastest in Solids > Liquids > Gases (Sound cannot travel in a vacuum).
                  - Speed in dry air at 20°C ≈ 343 m/s.
                
                Audible Range:
                • Human Hearing: 20 Hz to 20,000 Hz (20 kHz).
                • Infrasonic: Frequency < 20 Hz (elephants, earthquakes, whales).
                • Ultrasonic: Frequency > 20,000 Hz (bats, dolphins, medical ultrasound imaging).
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "Wave Speed Formula",
                    formulaLatex = "v = f × λ",
                    symbols = listOf("v" to "Speed of sound (m/s)", "f" to "Frequency (Hz)", "λ" to "Wavelength (m)"),
                    quickDescription = "Speed equals frequency multiplied by wavelength.",
                    canCalculate = true,
                    defaultInputs = mapOf("Frequency f (Hz)" to 440.0, "Wavelength λ (m)" to 0.78),
                    calculateFn = { inputs -> (inputs["Frequency f (Hz)"] ?: 0.0) * (inputs["Wavelength λ (m)"] ?: 0.0) },
                    outputSymbol = "Speed (v)",
                    outputUnit = "m/s"
                ),
                PhysicsFormula(
                    title = "Loudness & Amplitude Relation",
                    formulaLatex = "Loudness ∝ (Amplitude)²",
                    symbols = listOf("Loudness" to "Measured in decibels (dB)", "Amplitude" to "Peak displacement (m)"),
                    quickDescription = "If amplitude doubles, loudness increases by a factor of 4."
                )
            ),
            importantLawsAndRules = listOf(
                "Sound requires a material medium (solid, liquid, or gas) for propagation; it cannot travel through a vacuum.",
                "Loudness above 80 dB becomes physically painful and causes noise pollution.",
                "Eardrum (tympanic membrane) converts pressure variations into mechanical impulses transmitted to the auditory nerve."
            ),
            realWorldApplications = listOf(
                "Ultrasound scanners in prenatal care and non-destructive metal testing.",
                "SONAR navigation used by submarines and echolocation used by bats.",
                "Acoustic damping in concert halls and soundproof studios."
            )
        ),

        // =========================================================================
        // CLASS 9
        // =========================================================================
        PhysicsTopic(
            id = "c9_motion_kinematics",
            grade = PhysicsGrade.CLASS_9,
            branch = PhysicsBranch.MECHANICS,
            chapterTitle = "Motion",
            topicTitle = "Kinematic Equations & Graphical Analysis",
            summary = "Distance vs displacement, speed vs velocity, acceleration, derivations of 3 equations of motion.",
            detailedExplanation = """
                Motion is change in position of an object with respect to time and reference point.
                
                Scalars vs Vectors:
                • Distance (Scalar): Total path length traveled; always ≥ 0.
                • Displacement (Vector): Shortest straight-line distance from initial to final position.
                • Speed = Distance / Time (Scalar); Velocity = Displacement / Time (Vector).
                • Acceleration (a): Rate of change of velocity: a = (v - u) / t.
                
                The Three Equations of Uniformly Accelerated Motion:
                1. v = u + at (Velocity-Time Relation)
                2. s = ut + ½ at² (Position-Time Relation)
                3. v² = u² + 2as (Position-Velocity Relation)
                Where u = initial velocity, v = final velocity, a = constant acceleration, t = time, s = displacement.
                
                Uniform Circular Motion:
                • Motion in a circular path at constant speed: v = 2πr / t.
                • Direction changes continuously, so velocity is constantly changing (centripetally accelerated).
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "First Equation of Motion",
                    formulaLatex = "v = u + a × t",
                    symbols = listOf("v" to "Final velocity (m/s)", "u" to "Initial velocity (m/s)", "a" to "Acceleration (m/s²)", "t" to "Time (s)"),
                    quickDescription = "Computes final velocity after time t.",
                    canCalculate = true,
                    defaultInputs = mapOf("Initial velocity u (m/s)" to 0.0, "Acceleration a (m/s²)" to 9.8, "Time t (s)" to 3.0),
                    calculateFn = { inputs -> (inputs["Initial velocity u (m/s)"] ?: 0.0) + (inputs["Acceleration a (m/s²)"] ?: 0.0) * (inputs["Time t (s)"] ?: 0.0) },
                    outputSymbol = "Final velocity (v)",
                    outputUnit = "m/s"
                ),
                PhysicsFormula(
                    title = "Second Equation of Motion",
                    formulaLatex = "s = u × t + 0.5 × a × t²",
                    symbols = listOf("s" to "Displacement (m)", "u" to "Initial velocity (m/s)", "a" to "Acceleration (m/s²)", "t" to "Time (s)"),
                    quickDescription = "Computes displacement under constant acceleration.",
                    canCalculate = true,
                    defaultInputs = mapOf("Initial velocity u (m/s)" to 10.0, "Acceleration a (m/s²)" to 2.0, "Time t (s)" to 5.0),
                    calculateFn = { inputs ->
                        val u = inputs["Initial velocity u (m/s)"] ?: 0.0
                        val a = inputs["Acceleration a (m/s²)"] ?: 0.0
                        val t = inputs["Time t (s)"] ?: 0.0
                        u * t + 0.5 * a * t * t
                    },
                    outputSymbol = "Displacement (s)",
                    outputUnit = "meters (m)"
                ),
                PhysicsFormula(
                    title = "Third Equation of Motion",
                    formulaLatex = "v² = u² + 2 × a × s",
                    symbols = listOf("v" to "Final velocity (m/s)", "u" to "Initial velocity (m/s)", "a" to "Acceleration (m/s²)", "s" to "Displacement (m)"),
                    quickDescription = "Time-independent relation between velocities and distance.",
                    canCalculate = true,
                    defaultInputs = mapOf("Initial velocity u (m/s)" to 0.0, "Acceleration a (m/s²)" to 9.8, "Displacement s (m)" to 20.0),
                    calculateFn = { inputs ->
                        val u = inputs["Initial velocity u (m/s)"] ?: 0.0
                        val a = inputs["Acceleration a (m/s²)"] ?: 0.0
                        val s = inputs["Displacement s (m)"] ?: 0.0
                        Math.sqrt(u * u + 2 * a * s)
                    },
                    outputSymbol = "Final velocity (v)",
                    outputUnit = "m/s"
                )
            ),
            importantLawsAndRules = listOf(
                "Slope of displacement-time graph gives Velocity.",
                "Slope of velocity-time graph gives Acceleration.",
                "Area under velocity-time graph gives Displacement.",
                "For vertically falling objects under gravity: a = +g (falling) or a = -g (thrown upward), where g ≈ 9.8 m/s²."
            ),
            realWorldApplications = listOf(
                "Vehicle braking distance and stopping time calculations.",
                "Rocket launch trajectory telemetry and free-fall physics.",
                "Speed calibration and track geometry in athletic race planning."
            )
        ),
        PhysicsTopic(
            id = "c9_laws_of_motion",
            grade = PhysicsGrade.CLASS_9,
            branch = PhysicsBranch.MECHANICS,
            chapterTitle = "Force and Laws of Motion",
            topicTitle = "Newton's 3 Laws of Motion & Momentum Conservation",
            summary = "Inertia, F = ma, Action-Reaction pairs, Linear Momentum, Conservation of Momentum.",
            detailedExplanation = """
                Newton's Three Laws of Motion:
                
                1. First Law of Motion (Law of Inertia):
                An object continues in its state of rest or of uniform motion in a straight line unless acted upon by an external unbalanced force.
                • Inertia is the inherent tendency of an object to resist change in state of motion; mass is the measure of inertia.
                
                2. Second Law of Motion:
                The rate of change of momentum of an object is directly proportional to the applied unbalanced force in the direction of force:
                • Momentum: p = m × v (SI unit: kg·m/s).
                • Force: F = dp/dt = m(v - u)/t = m × a.
                • Impulse: Force × time = Change in momentum (J = F × Δt = Δp).
                
                3. Third Law of Motion:
                To every action, there is an equal and opposite reaction (Forces always occur in matched pairs acting on two different bodies).
                
                Law of Conservation of Linear Momentum:
                In an isolated system (no external unbalanced force), total momentum before collision equals total momentum after collision:
                m₁u₁ + m₂u₂ = m₁v₁ + m₂v₂
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "Newton's Second Law",
                    formulaLatex = "F = m × a",
                    symbols = listOf("F" to "Force (N)", "m" to "Mass (kg)", "a" to "Acceleration (m/s²)"),
                    quickDescription = "Net force equals mass times acceleration.",
                    canCalculate = true,
                    defaultInputs = mapOf("Mass m (kg)" to 50.0, "Acceleration a (m/s²)" to 2.5),
                    calculateFn = { inputs -> (inputs["Mass m (kg)"] ?: 0.0) * (inputs["Acceleration a (m/s²)"] ?: 0.0) },
                    outputSymbol = "Force (F)",
                    outputUnit = "Newtons (N)"
                ),
                PhysicsFormula(
                    title = "Linear Momentum",
                    formulaLatex = "p = m × v",
                    symbols = listOf("p" to "Momentum (kg·m/s)", "m" to "Mass (kg)", "v" to "Velocity (m/s)"),
                    quickDescription = "Product of mass and velocity of a moving body.",
                    canCalculate = true,
                    defaultInputs = mapOf("Mass m (kg)" to 1000.0, "Velocity v (m/s)" to 20.0),
                    calculateFn = { inputs -> (inputs["Mass m (kg)"] ?: 0.0) * (inputs["Velocity v (m/s)"] ?: 0.0) },
                    outputSymbol = "Momentum (p)",
                    outputUnit = "kg·m/s"
                ),
                PhysicsFormula(
                    title = "Conservation of Momentum (Collision)",
                    formulaLatex = "m₁u₁ + m₂u₂ = m₁v₁ + m₂v₂",
                    symbols = listOf("m₁, m₂" to "Masses of colliding bodies", "u₁, u₂" to "Initial velocities", "v₁, v₂" to "Final velocities"),
                    quickDescription = "Total linear momentum is conserved in collisions."
                )
            ),
            importantLawsAndRules = listOf(
                "Action and reaction forces never cancel each other because they act on two different bodies.",
                "Cricketer pulls hands backward while catching a ball to increase contact time Δt, thereby reducing the impact force F.",
                "Recoil of a gun: Gun moves backward as bullet fires forward according to conservation of momentum: v_recoil = -(m_bullet / M_gun) × v_bullet."
            ),
            realWorldApplications = listOf(
                "Automotive airbags and seatbelts reducing deceleration impact forces.",
                "Rocket propulsion and jet engine thrust (expelling exhaust gases backward).",
                "Hammer driving nails into wood utilizing large impulse."
            )
        ),
        PhysicsTopic(
            id = "c9_gravitation",
            grade = PhysicsGrade.CLASS_9,
            branch = PhysicsBranch.MECHANICS,
            chapterTitle = "Gravitation",
            topicTitle = "Universal Law of Gravitation, Free Fall, Buoyancy & Archimedes",
            summary = "F = G M m / r², g = G M / R², mass vs weight, thrust, pressure, Archimedes' Principle, relative density.",
            detailedExplanation = """
                Universal Law of Gravitation (Isaac Newton):
                Every particle in the universe attracts every other particle with a force proportional to the product of their masses and inversely proportional to the square of the distance between their centers:
                F = G × (M × m) / r²
                • Universal Gravitational Constant (G) = 6.674 × 10⁻¹¹ N·m²/kg² (measured by Henry Cavendish).
                
                Acceleration Due to Gravity (g):
                • On Earth's surface: g = G·M / R² ≈ 9.8 m/s².
                • On Moon's surface: g_moon ≈ (1/6) g_earth ≈ 1.63 m/s².
                • Mass is constant everywhere (kg); Weight is gravitational force W = m·g (Newtons).
                
                Buoyancy and Archimedes' Principle:
                • Buoyant Force (Upthrust): Upward force exerted by a fluid on a body immersed in it.
                • Archimedes' Principle: When a body is immersed fully or partially in a fluid, it experiences an upward buoyant force equal to the weight of the fluid displaced by it.
                • Relative Density = Density of substance / Density of water at 4°C.
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "Universal Law of Gravitation",
                    formulaLatex = "F = G × (m₁ × m₂) / r²",
                    symbols = listOf("F" to "Gravitational force (N)", "G" to "6.674×10⁻¹¹ N·m²/kg²", "m₁, m₂" to "Masses (kg)", "r" to "Distance between centers (m)"),
                    quickDescription = "Gravitational attraction between any two masses.",
                    canCalculate = true,
                    defaultInputs = mapOf("Mass m1 (kg)" to 5.97e24, "Mass m2 (kg)" to 70.0, "Distance r (m)" to 6.37e6),
                    calculateFn = { inputs ->
                        val G = 6.6743e-11
                        val m1 = inputs["Mass m1 (kg)"] ?: 0.0
                        val m2 = inputs["Mass m2 (kg)"] ?: 0.0
                        val r = inputs["Distance r (m)"] ?: 1.0
                        G * (m1 * m2) / (r * r)
                    },
                    outputSymbol = "Gravitational Force (F)",
                    outputUnit = "Newtons (N)"
                ),
                PhysicsFormula(
                    title = "Weight Formula",
                    formulaLatex = "W = m × g",
                    symbols = listOf("W" to "Weight (N)", "m" to "Mass (kg)", "g" to "Acceleration due to gravity (9.8 m/s²)"),
                    quickDescription = "Weight is the gravitational pull on mass m.",
                    canCalculate = true,
                    defaultInputs = mapOf("Mass m (kg)" to 70.0, "Gravity g (m/s²)" to 9.8),
                    calculateFn = { inputs -> (inputs["Mass m (kg)"] ?: 0.0) * (inputs["Gravity g (m/s²)"] ?: 9.8) },
                    outputSymbol = "Weight (W)",
                    outputUnit = "Newtons (N)"
                ),
                PhysicsFormula(
                    title = "Archimedes Buoyant Force",
                    formulaLatex = "F_b = V_displaced × ρ_fluid × g",
                    symbols = listOf("F_b" to "Buoyant upthrust (N)", "V_displaced" to "Volume of displaced fluid (m³)", "ρ_fluid" to "Density of fluid (kg/m³)", "g" to "Gravity (9.8 m/s²)"),
                    quickDescription = "Upthrust equals weight of displaced fluid.",
                    canCalculate = true,
                    defaultInputs = mapOf("Volume displaced (m³)" to 0.05, "Fluid Density (kg/m³)" to 1000.0, "Gravity g (m/s²)" to 9.8),
                    calculateFn = { inputs ->
                        val v = inputs["Volume displaced (m³)"] ?: 0.0
                        val rho = inputs["Fluid Density (kg/m³)"] ?: 1000.0
                        val g = inputs["Gravity g (m/s²)"] ?: 9.8
                        v * rho * g
                    },
                    outputSymbol = "Buoyant Force (F_b)",
                    outputUnit = "Newtons (N)"
                )
            ),
            importantLawsAndRules = listOf(
                "Value of g is maximum at the Earth's poles and minimum at the equator.",
                "An object will float if its density is less than the fluid density, and sink if its density is greater.",
                "Hydrometers (measuring liquid density) and Lactometers (milk purity) are based on Archimedes' Principle."
            ),
            realWorldApplications = listOf(
                "Design of massive steel ships and submarines floating and submerging safely.",
                "Orbital mechanics of Moon and artificial satellites orbiting Earth.",
                "Hot air balloon ascension governed by buoyant forces."
            )
        ),
        PhysicsTopic(
            id = "c9_work_energy_power",
            grade = PhysicsGrade.CLASS_9,
            branch = PhysicsBranch.MECHANICS,
            chapterTitle = "Work and Energy",
            topicTitle = "Work Done, Kinetic & Potential Energy, Conservation of Energy",
            summary = "W = F·s·cosθ, KE = ½ mv², PE = mgh, Law of Conservation of Energy, Power P = W/t, Commercial unit (kWh).",
            detailedExplanation = """
                Work Done (W):
                • Work is done when a force acts on an object and causes displacement in the direction of force:
                  W = F × s × cos(θ)
                • SI Unit: Joule (J), where 1 J = 1 N·m.
                • Work is positive (θ = 0°), zero (θ = 90°, e.g., coolie carrying load on head on flat road), or negative (θ = 180°, e.g., friction).
                
                Forms of Mechanical Energy:
                1. Kinetic Energy (KE): Energy possessed by a body due to its motion:
                   KE = ½ m v²
                2. Potential Energy (PE): Energy stored in a body due to its position or configuration:
                   Gravitational PE = m × g × h
                   Elastic PE (Spring) = ½ k x²
                
                Law of Conservation of Energy:
                Energy can neither be created nor destroyed; it can only be transformed from one form to another.
                Total Mechanical Energy = KE + PE = Constant (for a freely falling body).
                
                Power (P):
                • Rate of doing work: P = W / t (SI Unit: Watt, 1 W = 1 J/s).
                • Commercial Unit of Electrical Energy: 1 kilowatt-hour (kWh) = 1 Unit = 3.6 × 10⁶ Joules (3.6 MJ).
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "Work Done",
                    formulaLatex = "W = F × s × cos(θ)",
                    symbols = listOf("W" to "Work (Joules)", "F" to "Force (N)", "s" to "Displacement (m)", "θ" to "Angle between force and displacement"),
                    quickDescription = "Mechanical work done by applied force.",
                    canCalculate = true,
                    defaultInputs = mapOf("Force F (N)" to 100.0, "Displacement s (m)" to 15.0),
                    calculateFn = { inputs -> (inputs["Force F (N)"] ?: 0.0) * (inputs["Displacement s (m)"] ?: 0.0) },
                    outputSymbol = "Work (W)",
                    outputUnit = "Joules (J)"
                ),
                PhysicsFormula(
                    title = "Kinetic Energy",
                    formulaLatex = "KE = 0.5 × m × v²",
                    symbols = listOf("KE" to "Kinetic Energy (J)", "m" to "Mass (kg)", "v" to "Velocity (m/s)"),
                    quickDescription = "Energy due to motion.",
                    canCalculate = true,
                    defaultInputs = mapOf("Mass m (kg)" to 1200.0, "Velocity v (m/s)" to 25.0),
                    calculateFn = { inputs ->
                        val m = inputs["Mass m (kg)"] ?: 0.0
                        val v = inputs["Velocity v (m/s)"] ?: 0.0
                        0.5 * m * v * v
                    },
                    outputSymbol = "Kinetic Energy (KE)",
                    outputUnit = "Joules (J)"
                ),
                PhysicsFormula(
                    title = "Gravitational Potential Energy",
                    formulaLatex = "PE = m × g × h",
                    symbols = listOf("PE" to "Potential Energy (J)", "m" to "Mass (kg)", "g" to "Gravity (9.8 m/s²)", "h" to "Height (m)"),
                    quickDescription = "Stored gravitational energy at height h.",
                    canCalculate = true,
                    defaultInputs = mapOf("Mass m (kg)" to 50.0, "Height h (m)" to 10.0, "Gravity g (m/s²)" to 9.8),
                    calculateFn = { inputs -> (inputs["Mass m (kg)"] ?: 0.0) * (inputs["Gravity g (m/s²)"] ?: 9.8) * (inputs["Height h (m)"] ?: 0.0) },
                    outputSymbol = "Potential Energy (PE)",
                    outputUnit = "Joules (J)"
                ),
                PhysicsFormula(
                    title = "Power Formula",
                    formulaLatex = "P = W / t",
                    symbols = listOf("P" to "Power (Watts, W)", "W" to "Work done (Joules)", "t" to "Time taken (s)"),
                    quickDescription = "Rate of doing work.",
                    canCalculate = true,
                    defaultInputs = mapOf("Work W (J)" to 5000.0, "Time t (s)" to 10.0),
                    calculateFn = { inputs -> (inputs["Work W (J)"] ?: 0.0) / (inputs["Time t (s)"] ?: 1.0) },
                    outputSymbol = "Power (P)",
                    outputUnit = "Watts (W)"
                )
            ),
            importantLawsAndRules = listOf(
                "Work done is zero if displacement is zero, or if force is perpendicular to displacement (cos 90° = 0).",
                "1 Horsepower (hp) = 746 Watts.",
                "1 kWh = 1,000 W × 3,600 s = 3.6 × 10⁶ Joules."
            ),
            realWorldApplications = listOf(
                "Hydroelectric dams converting gravitational PE of reservoir water into electrical energy via turbines.",
                "Roller coaster loops converting potential energy to kinetic energy and back.",
                "Electricity billing metering in residential units (kWh)."
            )
        ),

        // =========================================================================
        // CLASS 10
        // =========================================================================
        PhysicsTopic(
            id = "c10_light_optics",
            grade = PhysicsGrade.CLASS_10,
            branch = PhysicsBranch.OPTICS,
            chapterTitle = "Light - Reflection and Refraction",
            topicTitle = "Mirror Formula, Snell's Law, Lens Formula & Power of Lens",
            summary = "Cartesian sign convention, mirror equation, Snell's Law n₁ sin i = n₂ sin r, lens formula, power P = 1/f.",
            detailedExplanation = """
                1. Spherical Mirrors:
                • Mirror Formula: 1/f = 1/v + 1/u
                • Linear Magnification: m = h'/h = -v/u
                • Focal Length: f = R / 2
                
                2. Refraction of Light & Snell's Law:
                When light passes obliquely from one transparent medium to another, it bends due to a change in speed.
                • Snell's Law: (sin i) / (sin r) = n₂ / n₁ = v₁ / v₂ = Constant (Refractive Index n)
                • Absolute Refractive Index: n = c / v (where c = 3 × 10⁸ m/s).
                
                3. Spherical Lenses:
                • Lens Formula: 1/f = 1/v - 1/u
                • Lens Magnification: m = h'/h = +v/u
                • Power of Lens (P): P = 1 / f (in meters). Unit: Dioptre (D).
                  - Convex lens: f > 0, P > 0 (Positive power).
                  - Concave lens: f < 0, P < 0 (Negative power).
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "Mirror Formula",
                    formulaLatex = "1/f = 1/v + 1/u",
                    symbols = listOf("f" to "Focal length (m)", "v" to "Image distance (m)", "u" to "Object distance (m, negative by sign convention)"),
                    quickDescription = "Relates object distance, image distance, and focal length for spherical mirrors.",
                    canCalculate = true,
                    defaultInputs = mapOf("Object distance u (cm)" to -30.0, "Focal length f (cm)" to -15.0),
                    calculateFn = { inputs ->
                        val u = inputs["Object distance u (cm)"] ?: -30.0
                        val f = inputs["Focal length f (cm)"] ?: -15.0
                        (f * u) / (u - f)
                    },
                    outputSymbol = "Image distance (v)",
                    outputUnit = "cm"
                ),
                PhysicsFormula(
                    title = "Snell's Law of Refraction",
                    formulaLatex = "n₁ × sin(i) = n₂ × sin(r)",
                    symbols = listOf("n₁" to "Refractive index of medium 1", "n₂" to "Refractive index of medium 2", "i" to "Angle of incidence", "r" to "Angle of refraction"),
                    quickDescription = "Law governing light bending at optical interfaces."
                ),
                PhysicsFormula(
                    title = "Lens Formula",
                    formulaLatex = "1/f = 1/v - 1/u",
                    symbols = listOf("f" to "Focal length (m)", "v" to "Image distance (m)", "u" to "Object distance (m)"),
                    quickDescription = "Relates object distance, image distance, and focal length for thin lenses.",
                    canCalculate = true,
                    defaultInputs = mapOf("Object distance u (cm)" to -20.0, "Focal length f (cm)" to 10.0),
                    calculateFn = { inputs ->
                        val u = inputs["Object distance u (cm)"] ?: -20.0
                        val f = inputs["Focal length f (cm)"] ?: 10.0
                        (f * u) / (u + f)
                    },
                    outputSymbol = "Image distance (v)",
                    outputUnit = "cm"
                ),
                PhysicsFormula(
                    title = "Power of a Lens",
                    formulaLatex = "P = 1 / f (in meters)",
                    symbols = listOf("P" to "Power of lens (Dioptres, D)", "f" to "Focal length in meters (m)"),
                    quickDescription = "Reciprocal of focal length in meters.",
                    canCalculate = true,
                    defaultInputs = mapOf("Focal length f (m)" to 0.5),
                    calculateFn = { inputs -> 1.0 / (inputs["Focal length f (m)"] ?: 1.0) },
                    outputSymbol = "Power (P)",
                    outputUnit = "Dioptres (D)"
                )
            ),
            importantLawsAndRules = listOf(
                "New Cartesian Sign Convention: Distances measured in the direction of incident light are positive; opposite are negative. Distances above principal axis are positive.",
                "When light travels from rarer to denser medium, it bends TOWARDS the normal (speed decreases).",
                "Total Internal Reflection (TIR) occurs when angle of incidence in denser medium exceeds critical angle (sin θ_c = 1/n)."
            ),
            realWorldApplications = listOf(
                "Optical fiber communications utilizing Total Internal Reflection.",
                "Corrective prescription lenses for Myopia (concave lens) and Hypermetropia (convex lens).",
                "Compound microscopes, digital camera sensors, and astronomical telescopes."
            )
        ),
        PhysicsTopic(
            id = "c10_electricity",
            grade = PhysicsGrade.CLASS_10,
            branch = PhysicsBranch.ELECTROMAGNETISM,
            chapterTitle = "Electricity",
            topicTitle = "Ohm's Law, Resistor Combinations, Joule's Heating & Electric Power",
            summary = "I = Q/t, V = W/Q, V = IR, R = ρL/A, Series R_eq = R₁ + R₂, Parallel 1/R_eq = 1/R₁ + 1/R₂, P = VI = I²R = V²/R.",
            detailedExplanation = """
                Electric Current and Potential Difference:
                • Electric Current (I): Rate of flow of electric charge: I = Q / t (1 Ampere = 1 Coulomb / 1 second).
                • Potential Difference (V): Work done in moving a unit positive charge between two points: V = W / Q (1 Volt = 1 Joule / 1 Coulomb).
                
                Ohm's Law:
                At constant temperature, current flowing through a conductor is directly proportional to the potential difference across its ends:
                V = I × R (where R is electrical resistance in Ohms, Ω).
                
                Factors Affecting Resistance:
                R = ρ × (L / A)
                Where L = length, A = cross-sectional area, ρ = electrical resistivity (characteristic of material, Ω·m).
                
                Resistor Combinations:
                • Series: R_eq = R₁ + R₂ + R₃ (Current I is same through all resistors; V divides).
                • Parallel: 1/R_eq = 1/R₁ + 1/R₂ + 1/R₃ (Potential V is same across all resistors; I divides).
                
                Joule's Law of Heating & Electrical Power:
                • Heat Produced: H = I² R t = V I t = (V² / R) t (Joules).
                • Electric Power: P = V × I = I² × R = V² / R (Watts).
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "Ohm's Law",
                    formulaLatex = "V = I × R",
                    symbols = listOf("V" to "Voltage / Potential difference (Volts, V)", "I" to "Current (Amperes, A)", "R" to "Resistance (Ohms, Ω)"),
                    quickDescription = "Fundamental relationship between voltage, current, and resistance.",
                    canCalculate = true,
                    defaultInputs = mapOf("Current I (A)" to 2.5, "Resistance R (Ω)" to 8.0),
                    calculateFn = { inputs -> (inputs["Current I (A)"] ?: 0.0) * (inputs["Resistance R (Ω)"] ?: 0.0) },
                    outputSymbol = "Voltage (V)",
                    outputUnit = "Volts (V)"
                ),
                PhysicsFormula(
                    title = "Resistance and Resistivity",
                    formulaLatex = "R = ρ × (L / A)",
                    symbols = listOf("R" to "Resistance (Ω)", "ρ" to "Resistivity (Ω·m)", "L" to "Length of wire (m)", "A" to "Cross-sectional area (m²)"),
                    quickDescription = "Resistance depends on material, length, and cross-section.",
                    canCalculate = true,
                    defaultInputs = mapOf("Resistivity ρ (Ω·m)" to 1.7e-8, "Length L (m)" to 10.0, "Area A (m²)" to 1e-6),
                    calculateFn = { inputs ->
                        val rho = inputs["Resistivity ρ (Ω·m)"] ?: 1.7e-8
                        val l = inputs["Length L (m)"] ?: 1.0
                        val a = inputs["Area A (m²)"] ?: 1e-6
                        rho * l / a
                    },
                    outputSymbol = "Resistance (R)",
                    outputUnit = "Ohms (Ω)"
                ),
                PhysicsFormula(
                    title = "Resistors in Parallel (2 Resistors)",
                    formulaLatex = "R_eq = (R₁ × R₂) / (R₁ + R₂)",
                    symbols = listOf("R₁, R₂" to "Individual resistances (Ω)", "R_eq" to "Equivalent parallel resistance (Ω)"),
                    quickDescription = "Calculates total resistance in parallel.",
                    canCalculate = true,
                    defaultInputs = mapOf("Resistor R1 (Ω)" to 10.0, "Resistor R2 (Ω)" to 10.0),
                    calculateFn = { inputs ->
                        val r1 = inputs["Resistor R1 (Ω)"] ?: 10.0
                        val r2 = inputs["Resistor R2 (Ω)"] ?: 10.0
                        (r1 * r2) / (r1 + r2)
                    },
                    outputSymbol = "R_equivalent",
                    outputUnit = "Ohms (Ω)"
                ),
                PhysicsFormula(
                    title = "Electric Power",
                    formulaLatex = "P = V × I = I² × R = V² / R",
                    symbols = listOf("P" to "Power (Watts, W)", "V" to "Voltage (V)", "I" to "Current (A)", "R" to "Resistance (Ω)"),
                    quickDescription = "Rate of electrical energy consumption.",
                    canCalculate = true,
                    defaultInputs = mapOf("Voltage V (V)" to 220.0, "Current I (A)" to 5.0),
                    calculateFn = { inputs -> (inputs["Voltage V (V)"] ?: 0.0) * (inputs["Current I (A)"] ?: 0.0) },
                    outputSymbol = "Electric Power (P)",
                    outputUnit = "Watts (W)"
                )
            ),
            importantLawsAndRules = listOf(
                "Household appliances are connected in PARALLEL so each receives full 220V supply and operates independently.",
                "Alloys (like Nichrome) have higher resistivity and do not oxidize easily at high temperatures compared to pure metals.",
                "Ammeter has very low resistance (connected in series); Voltmeter has very high resistance (connected in parallel)."
            ),
            realWorldApplications = listOf(
                "Domestic power wiring grids (220V AC in India/UK, 110V in US).",
                "Electric heaters, geysers, toasters, and irons utilizing Joule's heating effect.",
                "LED and CFL lighting replacing tungsten filament bulbs for high energy efficiency."
            )
        ),
        PhysicsTopic(
            id = "c10_magnetic_effects",
            grade = PhysicsGrade.CLASS_10,
            branch = PhysicsBranch.ELECTROMAGNETISM,
            chapterTitle = "Magnetic Effects of Electric Current",
            topicTitle = "Right-Hand Thumb Rule, Solenoid, Fleming's Rules & EMI",
            summary = "Magnetic field lines, solenoid B = μ₀ n I, Fleming's Left Hand Rule (Motor), Electromagnetic Induction, Fleming's Right Hand Rule (Generator).",
            detailedExplanation = """
                Magnetic Field & Field Lines:
                • Magnetic field lines emerge from North pole and enter South pole outside a magnet; form continuous closed loops.
                • Maxwell's Right-Hand Thumb Rule: If thumb points in direction of current, curled fingers give direction of magnetic field lines.
                
                Solenoid:
                • A long cylindrical coil of insulated copper wire. Magnetic field inside a solenoid is uniform and strong, mimicking a bar magnet: B = μ₀ n I.
                
                Force on a Current-Carrying Conductor in a Magnetic Field:
                • Force is maximum when conductor is perpendicular to magnetic field: F = B × I × L.
                • Fleming's Left-Hand Rule (Electric Motor):
                  - Forefinger: Magnetic Field (B)
                  - Middle finger: Current (I)
                  - Thumb: Motion / Force (F)
                
                Electromagnetic Induction (EMI - Michael Faraday):
                • Changing magnetic flux linked with a coil induces an electric current in it.
                • Fleming's Right-Hand Rule (Electric Generator):
                  - Forefinger: Magnetic Field (B)
                  - Thumb: Motion of conductor
                  - Middle finger: Direction of Induced Current
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "Magnetic Force on Wire",
                    formulaLatex = "F = B × I × L × sin(θ)",
                    symbols = listOf("F" to "Force (N)", "B" to "Magnetic Field (Tesla, T)", "I" to "Current (A)", "L" to "Length of wire (m)", "θ" to "Angle between wire and magnetic field"),
                    quickDescription = "Lorentz force on a straight current-carrying conductor."
                ),
                PhysicsFormula(
                    title = "Solenoid Magnetic Field",
                    formulaLatex = "B = μ₀ × n × I",
                    symbols = listOf("B" to "Magnetic Field (Tesla, T)", "μ₀" to "4π × 10⁻⁷ T·m/A", "n" to "Number of turns per unit length (N/L)", "I" to "Current (A)"),
                    quickDescription = "Uniform internal magnetic field of an ideal solenoid."
                )
            ),
            importantLawsAndRules = listOf(
                "No two magnetic field lines ever intersect each other (otherwise there would be two directions of field at the intersection point).",
                "Fleming's Left-Hand Rule is for MOTORS (electrical energy to mechanical energy).",
                "Fleming's Right-Hand Rule is for GENERATORS (mechanical energy to electrical energy)."
            ),
            realWorldApplications = listOf(
                "Electric motors in ceiling fans, washing machines, pumps, and electric cars.",
                "AC and DC electrical power generators at hydroelectric and thermal stations.",
                "MRI (Magnetic Resonance Imaging) machines using powerful superconducting electromagnets."
            )
        ),

        // =========================================================================
        // CLASS 11
        // =========================================================================
        PhysicsTopic(
            id = "c11_units_measurements",
            grade = PhysicsGrade.CLASS_11,
            branch = PhysicsBranch.MECHANICS,
            chapterTitle = "Units and Measurements",
            topicTitle = "Dimensional Analysis, SI Base Units & Error Propagation",
            summary = "7 base SI units, dimensional formulas [M^a L^b T^c], principle of homogeneity, absolute/relative/percentage errors.",
            detailedExplanation = """
                Fundamental SI Units:
                1. Length: meter (m) [L]
                2. Mass: kilogram (kg) [M]
                3. Time: second (s) [T]
                4. Electric Current: ampere (A) [A]
                5. Thermodynamic Temperature: kelvin (K) [K]
                6. Amount of Substance: mole (mol) [mol]
                7. Luminous Intensity: candela (cd) [cd]
                
                Dimensional Analysis:
                • Dimensions express a derived physical quantity in terms of fundamental dimensions [M^a L^b T^c I^d ...].
                • Principle of Homogeneity: In any physical equation, dimensions on LHS must match dimensions on RHS.
                • Applications: (1) Checking dimensional consistency of equations, (2) Converting units between systems, (3) Deducing functional relations between physical quantities.
                
                Errors in Measurement:
                • Absolute Error: Δa_i = |a_true - a_i|
                • Relative Error = (Mean Absolute Error Δa_mean) / a_mean
                • Percentage Error = Relative Error × 100%
                • Error Propagation in Multiplication/Division: If Z = A^p · B^q / C^r, then (ΔZ/Z) = p(ΔA/A) + q(ΔB/B) + r(ΔC/C).
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "Percentage Error",
                    formulaLatex = "% Error = (|Measured - True| / True) × 100%",
                    symbols = listOf("Measured" to "Experimental value", "True" to "Standard theoretical value"),
                    quickDescription = "Relative measurement deviation expressed in percent.",
                    canCalculate = true,
                    defaultInputs = mapOf("Measured Value" to 9.6, "True Value" to 9.8),
                    calculateFn = { inputs ->
                        val m = inputs["Measured Value"] ?: 0.0
                        val t = inputs["True Value"] ?: 1.0
                        (Math.abs(m - t) / t) * 100.0
                    },
                    outputSymbol = "Percentage Error",
                    outputUnit = "%"
                ),
                PhysicsFormula(
                    title = "Error Propagation (Power Law)",
                    formulaLatex = "ΔZ/Z = p(ΔA/A) + q(ΔB/B) + r(ΔC/C)",
                    symbols = listOf("Z = A^p B^q / C^r" to "Derived quantity formula", "ΔA, ΔB, ΔC" to "Absolute measurement uncertainties"),
                    quickDescription = "Fractional error summation in power-law products."
                )
            ),
            importantLawsAndRules = listOf(
                "Trigonometric, exponential, and logarithmic functions are dimensionless pure numbers.",
                "Dimensional analysis cannot determine dimensionless proportionality constants (like 2π in T = 2π√(L/g)).",
                "Significant figures: All non-zero digits are significant; zeros between non-zero digits are significant."
            ),
            realWorldApplications = listOf(
                "Aerospace engineering dimensional scaling and wind tunnel model testing.",
                "Quality control calibration of precision digital micrometers and vernier calipers.",
                "Metrology standards maintained by national bureaus (e.g. NIST, NPL)."
            )
        ),
        PhysicsTopic(
            id = "c11_projectile_circular",
            grade = PhysicsGrade.CLASS_11,
            branch = PhysicsBranch.MECHANICS,
            chapterTitle = "Motion in a Plane (2D Kinematics)",
            topicTitle = "Vectors, Projectile Motion & Uniform Circular Motion",
            summary = "Vector dot & cross products, Projectile Trajectory y = x tanθ - gx²/(2u²cos²θ), Time of flight, Max height, Range, Centripetal acceleration a_c = v²/r.",
            detailedExplanation = """
                Vector Algebra:
                • Dot Product: A · B = |A| |B| cos(θ) = A_x B_x + A_y B_y + A_z B_z (Scalar).
                • Cross Product: A × B = |A| |B| sin(θ) n̂ (Vector perpendicular to both A and B).
                
                Projectile Motion:
                An object launched into the air with initial velocity u at an angle θ with horizontal under constant gravity g (neglecting air resistance):
                • Horizontal Component: u_x = u cos(θ), a_x = 0 (Constant horizontal velocity).
                • Vertical Component: u_y = u sin(θ), a_y = -g.
                • Time of Flight (T): T = (2 u sin θ) / g
                • Maximum Height (H): H = (u² sin² θ) / (2 g)
                • Horizontal Range (R): R = (u² sin 2θ) / g (Maximum at θ = 45°: R_max = u² / g).
                • Trajectory Equation: y = x tan(θ) - [g / (2 u² cos² θ)] x² (Parabola).
                
                Uniform Circular Motion:
                • Angular Velocity: ω = v / r = 2π / T = 2πf.
                • Centripetal Acceleration: a_c = v² / r = ω² r (Directed radially inward).
                • Centripetal Force: F_c = m v² / r = m ω² r.
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "Projectile Range Formula",
                    formulaLatex = "R = (u² × sin(2θ)) / g",
                    symbols = listOf("R" to "Horizontal Range (m)", "u" to "Launch speed (m/s)", "θ" to "Launch angle (degrees)", "g" to "Gravity (9.8 m/s²)"),
                    quickDescription = "Total horizontal distance covered by a projectile.",
                    canCalculate = true,
                    defaultInputs = mapOf("Launch speed u (m/s)" to 30.0, "Launch angle θ (deg)" to 45.0, "Gravity g (m/s²)" to 9.8),
                    calculateFn = { inputs ->
                        val u = inputs["Launch speed u (m/s)"] ?: 30.0
                        val deg = inputs["Launch angle θ (deg)"] ?: 45.0
                        val g = inputs["Gravity g (m/s²)"] ?: 9.8
                        val rad2 = Math.toRadians(deg * 2.0)
                        (u * u * Math.sin(rad2)) / g
                    },
                    outputSymbol = "Range (R)",
                    outputUnit = "meters (m)"
                ),
                PhysicsFormula(
                    title = "Maximum Height of Projectile",
                    formulaLatex = "H = (u² × sin²(θ)) / (2 × g)",
                    symbols = listOf("H" to "Max Height (m)", "u" to "Launch velocity (m/s)", "θ" to "Launch angle (deg)", "g" to "Gravity (9.8 m/s²)"),
                    quickDescription = "Peak altitude reached by projectile.",
                    canCalculate = true,
                    defaultInputs = mapOf("Launch speed u (m/s)" to 30.0, "Launch angle θ (deg)" to 45.0, "Gravity g (m/s²)" to 9.8),
                    calculateFn = { inputs ->
                        val u = inputs["Launch speed u (m/s)"] ?: 30.0
                        val deg = inputs["Launch angle θ (deg)"] ?: 45.0
                        val g = inputs["Gravity g (m/s²)"] ?: 9.8
                        val sinVal = Math.sin(Math.toRadians(deg))
                        (u * u * sinVal * sinVal) / (2 * g)
                    },
                    outputSymbol = "Max Height (H)",
                    outputUnit = "meters (m)"
                ),
                PhysicsFormula(
                    title = "Centripetal Acceleration",
                    formulaLatex = "a_c = v² / r = ω² × r",
                    symbols = listOf("a_c" to "Centripetal Acceleration (m/s²)", "v" to "Linear speed (m/s)", "r" to "Radius of circular path (m)", "ω" to "Angular speed (rad/s)"),
                    quickDescription = "Radially inward acceleration for circular motion.",
                    canCalculate = true,
                    defaultInputs = mapOf("Linear speed v (m/s)" to 20.0, "Radius r (m)" to 50.0),
                    calculateFn = { inputs ->
                        val v = inputs["Linear speed v (m/s)"] ?: 20.0
                        val r = inputs["Radius r (m)"] ?: 50.0
                        (v * v) / r
                    },
                    outputSymbol = "Centripetal Accel (a_c)",
                    outputUnit = "m/s²"
                )
            ),
            importantLawsAndRules = listOf(
                "Horizontal velocity of a projectile remains constant throughout flight in the absence of air drag.",
                "Range is identical for complementary launch angles θ and (90° - θ).",
                "Banking of curved roads: Optimum safe speed without friction is v = √(r g tan θ)."
            ),
            realWorldApplications = listOf(
                "Artillery ballistics, missile guidance trajectories, and mortar targeting.",
                "Sports physics: Javelin throw, basketball shooting arcs, and golf ball trajectory.",
                "Banking of highway turns and race tracks to prevent skidding."
            )
        ),
        PhysicsTopic(
            id = "c11_rotational_motion",
            grade = PhysicsGrade.CLASS_11,
            branch = PhysicsBranch.MECHANICS,
            chapterTitle = "System of Particles and Rotational Motion",
            topicTitle = "Center of Mass, Torque, Moment of Inertia & Angular Momentum",
            summary = "Center of mass R_cm, Torque τ = r × F = I α, Moment of inertia I = Σ m_i r_i², Parallel/Perpendicular axis theorems, L = I ω, Conservation of L.",
            detailedExplanation = """
                Center of Mass (CM):
                R_cm = (m₁r₁ + m₂r₂ + ... + m_n r_n) / (m₁ + m₂ + ... + m_n) = (1/M) ∫ r dm
                
                Rotational Dynamics Analogies with Linear Motion:
                • Displacement: Linear x  ↔  Angular θ (radians)
                • Velocity: Linear v  ↔  Angular ω = dθ/dt (rad/s)
                • Acceleration: Linear a  ↔  Angular α = dω/dt (rad/s²)
                • Mass/Inertia: Linear Mass m  ↔  Moment of Inertia I = ∫ r² dm (kg·m²)
                • Force: Linear Force F  ↔  Torque τ = r × F = I × α (N·m)
                • Momentum: Linear p = mv  ↔  Angular Momentum L = r × p = I × ω (kg·m²/s)
                • Kinetic Energy: KE_lin = ½ mv²  ↔  KE_rot = ½ I ω²
                
                Moment of Inertia Theorems:
                1. Parallel Axes Theorem: I = I_cm + M d²
                2. Perpendicular Axes Theorem (planar laminae): I_z = I_x + I_y
                
                Conservation of Angular Momentum:
                When external torque is zero (τ_ext = 0), total angular momentum remains conserved:
                I₁ ω₁ = I₂ ω₂ = Constant
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "Torque Formula",
                    formulaLatex = "τ = r × F × sin(θ) = I × α",
                    symbols = listOf("τ" to "Torque (N·m)", "r" to "Lever arm distance (m)", "F" to "Applied force (N)", "I" to "Moment of inertia (kg·m²)", "α" to "Angular acceleration (rad/s²)"),
                    quickDescription = "Rotational turning effect of a force.",
                    canCalculate = true,
                    defaultInputs = mapOf("Lever arm r (m)" to 0.4, "Force F (N)" to 50.0),
                    calculateFn = { inputs -> (inputs["Lever arm r (m)"] ?: 0.0) * (inputs["Force F (N)"] ?: 0.0) },
                    outputSymbol = "Torque (τ)",
                    outputUnit = "N·m"
                ),
                PhysicsFormula(
                    title = "Rotational Kinetic Energy",
                    formulaLatex = "KE_rot = 0.5 × I × ω²",
                    symbols = listOf("KE_rot" to "Rotational Energy (J)", "I" to "Moment of Inertia (kg·m²)", "ω" to "Angular velocity (rad/s)"),
                    quickDescription = "Kinetic energy stored in rotational motion.",
                    canCalculate = true,
                    defaultInputs = mapOf("Moment of Inertia I (kg·m²)" to 2.5, "Angular velocity ω (rad/s)" to 10.0),
                    calculateFn = { inputs ->
                        val I = inputs["Moment of Inertia I (kg·m²)"] ?: 2.5
                        val w = inputs["Angular velocity ω (rad/s)"] ?: 10.0
                        0.5 * I * w * w
                    },
                    outputSymbol = "Rotational KE",
                    outputUnit = "Joules (J)"
                ),
                PhysicsFormula(
                    title = "Angular Momentum",
                    formulaLatex = "L = I × ω",
                    symbols = listOf("L" to "Angular Momentum (kg·m²/s)", "I" to "Moment of Inertia (kg·m²)", "ω" to "Angular velocity (rad/s)"),
                    quickDescription = "Rotational analog of linear momentum."
                )
            ),
            importantLawsAndRules = listOf(
                "Standard Moments of Inertia: Ring (MR²), Uniform Disc (½ MR²), Solid Sphere (2/5 MR²), Hollow Sphere (2/3 MR²), Rod about center (1/12 ML²).",
                "Figure skaters spin faster when pulling arms inward because decreasing I increases ω (Conservation of Angular Momentum).",
                "Total rolling kinetic energy: KE_total = KE_trans + KE_rot = ½ M v_cm² + ½ I_cm ω²."
            ),
            realWorldApplications = listOf(
                "Flywheels in internal combustion engines smoothing torque delivery.",
                "Gyroscopes used for inertial navigation in spacecraft and smartphones.",
                "Figure skaters and divers controlling rotational spin rates."
            )
        ),
        PhysicsTopic(
            id = "c11_fluid_mechanics",
            grade = PhysicsGrade.CLASS_11,
            branch = PhysicsBranch.FLUIDS_MATTER,
            chapterTitle = "Mechanical Properties of Fluids",
            topicTitle = "Pascal's Law, Continuity Equation, Bernoulli's Theorem & Viscosity",
            summary = "Pascal's principle, Equation of Continuity A₁v₁ = A₂v₂, Bernoulli's equation P + ½ρv² + ρgh = const, Stokes' Law, Terminal velocity.",
            detailedExplanation = """
                Pascal's Law:
                Pressure applied to an enclosed incompressible fluid is transmitted undiminished in all directions throughout the fluid:
                P = F₁ / A₁ = F₂ / A₂  ⟹  F₂ = F₁ × (A₂ / A₁)
                (Foundation of hydraulic lifts and hydraulic brakes).
                
                Equation of Continuity:
                For streamline flow of an incompressible fluid, mass flow rate is constant across all cross-sections:
                A₁ × v₁ = A₂ × v₂ = Constant
                
                Bernoulli's Principle (Conservation of Energy for Ideal Fluid):
                For steady, streamline, non-viscous, and incompressible fluid flow, the total energy per unit volume remains constant along a streamline:
                P + ½ ρ v² + ρ g h = Constant
                Where P = static pressure, ½ ρ v² = dynamic pressure (kinetic energy density), ρ g h = potential energy density.
                
                Viscosity & Stokes' Law:
                • Viscosity (η): Internal friction between adjacent fluid layers: F = -η A (dv/dx).
                • Stokes' Law: Retarding drag on a sphere of radius r moving with velocity v in fluid: F_drag = 6 π η r v.
                • Terminal Velocity (v_t): Constant steady speed reached when gravity equals buoyant force plus viscous drag:
                  v_t = [2 r² (ρ - σ) g] / (9 η)
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "Bernoulli's Equation",
                    formulaLatex = "P + 0.5 × ρ × v² + ρ × g × h = Constant",
                    symbols = listOf("P" to "Static Pressure (Pa)", "ρ" to "Fluid density (kg/m³)", "v" to "Flow velocity (m/s)", "h" to "Elevation (m)", "g" to "9.8 m/s²"),
                    quickDescription = "Conservation of fluid mechanical energy along a streamline."
                ),
                PhysicsFormula(
                    title = "Hydraulic Lift (Pascal's Principle)",
                    formulaLatex = "F₂ = F₁ × (A₂ / A₁)",
                    symbols = listOf("F₂" to "Output load force (N)", "F₁" to "Input applied force (N)", "A₁" to "Input piston area (m²)", "A₂" to "Output piston area (m²)"),
                    quickDescription = "Force multiplication in hydraulic systems.",
                    canCalculate = true,
                    defaultInputs = mapOf("Input Force F1 (N)" to 200.0, "Input Area A1 (m²)" to 0.01, "Output Area A2 (m²)" to 0.5),
                    calculateFn = { inputs ->
                        val f1 = inputs["Input Force F1 (N)"] ?: 200.0
                        val a1 = inputs["Input Area A1 (m²)"] ?: 0.01
                        val a2 = inputs["Output Area A2 (m²)"] ?: 0.5
                        f1 * (a2 / a1)
                    },
                    outputSymbol = "Output Force (F2)",
                    outputUnit = "Newtons (N)"
                ),
                PhysicsFormula(
                    title = "Terminal Velocity Formula",
                    formulaLatex = "v_t = [2 × r² × (ρ_sphere - ρ_fluid) × g] / (9 × η)",
                    symbols = listOf("v_t" to "Terminal velocity (m/s)", "r" to "Radius of sphere (m)", "ρ" to "Densities (kg/m³)", "η" to "Coefficient of viscosity (Pa·s)"),
                    quickDescription = "Terminal falling velocity of a spherical body in viscous fluid."
                )
            ),
            importantLawsAndRules = listOf(
                "Torricelli's Law of Efflux: Speed of liquid leaking from an orifice at depth h is v = √(2gh).",
                "Venturi effect: When fluid speed increases in a constricted pipe, static pressure decreases.",
                "Surface Tension (S = Force/Length = Energy/Area) causes raindrops and soap bubbles to assume spherical shapes."
            ),
            realWorldApplications = listOf(
                "Aerodynamic lift generation on airplane wings (aerofoil shape exploiting Bernoulli's effect).",
                "Hydraulic car jacks, power steering, and automobile hydraulic disc brakes.",
                "Carburetors, paint sprayers, atomizers, and perfume dispensers."
            )
        ),
        PhysicsTopic(
            id = "c11_thermodynamics",
            grade = PhysicsGrade.CLASS_11,
            branch = PhysicsBranch.THERMODYNAMICS,
            chapterTitle = "Thermodynamics & Kinetic Theory",
            topicTitle = "Laws of Thermodynamics, Ideal Gas Law & Carnot Engine",
            summary = "Zeroth Law (temperature), First Law ΔQ = ΔU + ΔW, Ideal gas PV = nRT, Thermodynamic processes, Second Law, Carnot efficiency η = 1 - T_C/T_H.",
            detailedExplanation = """
                Laws of Thermodynamics:
                • Zeroth Law: If systems A and B are in thermal equilibrium with C, then A and B are in thermal equilibrium with each other (Defines Temperature).
                • First Law (Conservation of Energy):
                  ΔQ = ΔU + ΔW = n C_v ΔT + P ΔV
                  Where ΔQ = heat added, ΔU = change in internal energy, ΔW = work done by system.
                • Second Law: Heat cannot spontaneously flow from a colder body to a hotter body without external work (Clausius statement); No heat engine can convert all absorbed heat into work with 100% efficiency (Kelvin-Planck statement).
                
                Thermodynamic Processes:
                1. Isothermal (T = const): PV = const, ΔU = 0, W = n R T ln(V₂/V₁).
                2. Adiabatic (ΔQ = 0): P V^γ = const, T V^(γ-1) = const, W = (P₁V₁ - P₂V₂)/(γ - 1).
                3. Isobaric (P = const): V/T = const, W = P(V₂ - V₁).
                4. Isochoric (V = const): P/T = const, W = 0, ΔQ = ΔU.
                
                Carnot Heat Engine:
                • Ideal reversible engine operating between hot reservoir (T_H) and cold reservoir (T_C).
                • Maximum Theoretical Efficiency: η = 1 - (T_C / T_H) = (T_H - T_C) / T_H (Temperatures in Kelvin).
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "First Law of Thermodynamics",
                    formulaLatex = "ΔQ = ΔU + ΔW",
                    symbols = listOf("ΔQ" to "Heat added to system (J)", "ΔU" to "Change in internal energy (J)", "ΔW" to "Work done by system = P ΔV (J)"),
                    quickDescription = "Conservation of thermal energy."
                ),
                PhysicsFormula(
                    title = "Ideal Gas Law",
                    formulaLatex = "P × V = n × R × T",
                    symbols = listOf("P" to "Pressure (Pa)", "V" to "Volume (m³)", "n" to "Number of moles", "R" to "Universal gas constant = 8.314 J/(mol·K)", "T" to "Absolute temperature (K)"),
                    quickDescription = "Equation of state for an ideal gas.",
                    canCalculate = true,
                    defaultInputs = mapOf("Moles n" to 1.0, "Temperature T (K)" to 300.0, "Volume V (m³)" to 0.025),
                    calculateFn = { inputs ->
                        val n = inputs["Moles n"] ?: 1.0
                        val t = inputs["Temperature T (K)"] ?: 300.0
                        val v = inputs["Volume V (m³)"] ?: 0.025
                        val R = 8.314
                        (n * R * t) / v
                    },
                    outputSymbol = "Pressure (P)",
                    outputUnit = "Pascal (Pa)"
                ),
                PhysicsFormula(
                    title = "Carnot Engine Maximum Efficiency",
                    formulaLatex = "η = 1 - (T_cold / T_hot)",
                    symbols = listOf("η" to "Thermal efficiency (fraction between 0 and 1)", "T_hot" to "Source temperature (Kelvin)", "T_cold" to "Sink temperature (Kelvin)"),
                    quickDescription = "Theoretical upper limit of heat engine efficiency.",
                    canCalculate = true,
                    defaultInputs = mapOf("Hot Reservoir T_hot (K)" to 600.0, "Cold Reservoir T_cold (K)" to 300.0),
                    calculateFn = { inputs ->
                        val th = inputs["Hot Reservoir T_hot (K)"] ?: 600.0
                        val tc = inputs["Cold Reservoir T_cold (K)"] ?: 300.0
                        (1.0 - (tc / th)) * 100.0
                    },
                    outputSymbol = "Carnot Efficiency (η)",
                    outputUnit = "%"
                )
            ),
            importantLawsAndRules = listOf(
                "Molar Heat Capacities Relation (Mayer's Formula): C_p - C_v = R.",
                "Adiabatic index γ = C_p / C_v (Monoatomic = 5/3 ≈ 1.67, Diatomic = 7/5 = 1.4).",
                "RMS speed of gas molecules: v_rms = √(3RT / M) = √(3k_B T / m)."
            ),
            realWorldApplications = listOf(
                "Automobile internal combustion engines (Otto & Diesel cycles).",
                "Refrigerators and air conditioners acting as reverse heat pumps extracting heat.",
                "Steam and gas turbines in thermal and nuclear power plants."
            )
        ),
        PhysicsTopic(
            id = "c11_oscillations_shm",
            grade = PhysicsGrade.CLASS_11,
            branch = PhysicsBranch.WAVES_SOUND,
            chapterTitle = "Oscillations and Waves",
            topicTitle = "Simple Harmonic Motion (SHM), Wave Equation & Doppler Effect",
            summary = "SHM differential equation d²x/dt² + ω²x = 0, Spring-mass T = 2π√(m/k), Wave velocity v = f λ, Standing waves, Doppler effect.",
            detailedExplanation = """
                Simple Harmonic Motion (SHM):
                Periodic motion where restoring force is directly proportional to displacement from equilibrium position and directed towards it:
                F = -k x  ⟹  a = - (k/m) x = - ω² x
                • Displacement: x(t) = A sin(ω t + φ)
                • Velocity: v(t) = dx/dt = A ω cos(ω t + φ) = ± ω √(A² - x²)
                • Acceleration: a(t) = - A ω² sin(ω t + φ) = - ω² x
                • Time Period:
                  - Spring-Mass System: T = 2π √(m / k)
                  - Simple Pendulum: T = 2π √(L / g)
                • Total Energy in SHM: E = KE + PE = ½ k A² = ½ m ω² A² (Constant).
                
                Waves & Doppler Effect:
                • Progressive Wave Equation: y(x, t) = A sin(k x - ω t + φ), where wave number k = 2π / λ.
                • Doppler Effect in Sound: Apparent shift in observed frequency due to relative motion between source and observer:
                  f' = f₀ × [(v ± v_observer) / (v ∓ v_source)]
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "Spring-Mass SHM Time Period",
                    formulaLatex = "T = 2π × √(m / k)",
                    symbols = listOf("T" to "Time period (s)", "m" to "Oscillating mass (kg)", "k" to "Spring constant (N/m)"),
                    quickDescription = "Time period of an ideal spring-mass oscillator.",
                    canCalculate = true,
                    defaultInputs = mapOf("Mass m (kg)" to 0.5, "Spring constant k (N/m)" to 50.0),
                    calculateFn = { inputs ->
                        val m = inputs["Mass m (kg)"] ?: 0.5
                        val k = inputs["Spring constant k (N/m)"] ?: 50.0
                        2.0 * Math.PI * Math.sqrt(m / k)
                    },
                    outputSymbol = "Time Period (T)",
                    outputUnit = "seconds"
                ),
                PhysicsFormula(
                    title = "Total Energy in SHM",
                    formulaLatex = "E = 0.5 × k × A² = 0.5 × m × ω² × A²",
                    symbols = listOf("E" to "Total mechanical energy (J)", "k" to "Spring stiffness (N/m)", "A" to "Amplitude of oscillation (m)"),
                    quickDescription = "Conserved total mechanical energy in harmonic motion.",
                    canCalculate = true,
                    defaultInputs = mapOf("Spring constant k (N/m)" to 100.0, "Amplitude A (m)" to 0.1),
                    calculateFn = { inputs ->
                        val k = inputs["Spring constant k (N/m)"] ?: 100.0
                        val a = inputs["Amplitude A (m)"] ?: 0.1
                        0.5 * k * a * a
                    },
                    outputSymbol = "Total Energy (E)",
                    outputUnit = "Joules (J)"
                ),
                PhysicsFormula(
                    title = "Doppler Shift in Frequency",
                    formulaLatex = "f' = f₀ × [(v + v_o) / (v - v_s)]",
                    symbols = listOf("f'" to "Apparent observed frequency (Hz)", "f₀" to "Source frequency (Hz)", "v" to "Speed of sound (m/s)", "v_o, v_s" to "Observer & Source speeds"),
                    quickDescription = "Frequency perceived when source and observer move towards each other."
                )
            ),
            importantLawsAndRules = listOf(
                "Velocity in SHM is maximum at mean position (x = 0, v_max = Aω) and zero at extreme positions (x = ±A).",
                "Acceleration is maximum at extreme positions (a_max = Aω²) and zero at mean position.",
                "Resonance occurs when driving frequency matches natural frequency, causing massive amplitude amplification."
            ),
            realWorldApplications = listOf(
                "Vehicle suspension shock absorbers dampening harmonic road vibrations.",
                "Acoustic tuning of musical instruments (violins, guitars, flutes) using standing wave harmonics.",
                "Doppler radar in meteorology (storm tracking) and traffic police speed guns."
            )
        ),

        // =========================================================================
        // CLASS 12
        // =========================================================================
        PhysicsTopic(
            id = "c12_electrostatics",
            grade = PhysicsGrade.CLASS_12,
            branch = PhysicsBranch.ELECTROMAGNETISM,
            chapterTitle = "Electrostatics & Capacitance",
            topicTitle = "Coulomb's Law, Gauss's Law, Electric Potential & Capacitors",
            summary = "F = k q₁q₂/r², E = F/q, Gauss's Law ∮E·dA = Q_encl/ε₀, V = k q/r, Capacitance C = Q/V = ε₀A/d, Energy U = ½ CV².",
            detailedExplanation = """
                Coulomb's Law:
                The electrostatic force between two stationary point charges is directly proportional to product of charges and inversely proportional to square of separation distance:
                F = [1 / (4πε₀)] × (|q₁ q₂| / r²)
                Where 1/(4πε₀) ≈ 8.988 × 10⁹ N·m²/C², and ε₀ = 8.854 × 10⁻¹² C²/(N·m²).
                
                Electric Field & Potential:
                • Electric Field (E): Force per unit positive test charge: E = F / q = [1 / (4πε₀)] (q / r²).
                • Electric Potential (V): Work done in bringing a unit positive charge from infinity: V = [1 / (4πε₀)] (q / r).
                • Relation between E and V: E = - dV/dr (Electric field is negative potential gradient).
                
                Gauss's Law:
                The total electric flux through any closed Gaussian surface equals net charge enclosed divided by ε₀:
                Φ_E = ∮ E · dA = Q_enclosed / ε₀
                
                Capacitance & Dielectrics:
                • Capacitance (C): Ratio of charge stored to potential difference: C = Q / V (Farads, F).
                • Parallel Plate Capacitor: C = (ε_r ε₀ A) / d = K C₀ (where K is dielectric constant).
                • Combinations:
                  - Series: 1/C_eq = 1/C₁ + 1/C₂
                  - Parallel: C_eq = C₁ + C₂
                • Energy Stored: U = ½ C V² = ½ Q V = Q² / (2 C).
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "Coulomb's Law",
                    formulaLatex = "F = [1 / (4πε₀)] × (q₁ × q₂) / r²",
                    symbols = listOf("F" to "Electrostatic Force (N)", "q₁, q₂" to "Point charges (Coulombs, C)", "r" to "Distance (m)", "1/4πε₀" to "9 × 10⁹ N·m²/C²"),
                    quickDescription = "Force between two electric charges.",
                    canCalculate = true,
                    defaultInputs = mapOf("Charge q1 (μC)" to 5.0, "Charge q2 (μC)" to -10.0, "Distance r (m)" to 0.2),
                    calculateFn = { inputs ->
                        val k = 8.988e9
                        val q1 = (inputs["Charge q1 (μC)"] ?: 5.0) * 1e-6
                        val q2 = (inputs["Charge q2 (μC)"] ?: -10.0) * 1e-6
                        val r = inputs["Distance r (m)"] ?: 0.2
                        Math.abs(k * q1 * q2 / (r * r))
                    },
                    outputSymbol = "Electrostatic Force |F|",
                    outputUnit = "Newtons (N)"
                ),
                PhysicsFormula(
                    title = "Parallel Plate Capacitance",
                    formulaLatex = "C = (K × ε₀ × A) / d",
                    symbols = listOf("C" to "Capacitance (Farads, F)", "K" to "Dielectric Constant", "ε₀" to "8.854 × 10⁻¹² F/m", "A" to "Plate Area (m²)", "d" to "Separation (m)"),
                    quickDescription = "Capacitance with dielectric slab.",
                    canCalculate = true,
                    defaultInputs = mapOf("Plate Area A (m²)" to 0.05, "Separation d (mm)" to 1.0, "Dielectric Constant K" to 1.0),
                    calculateFn = { inputs ->
                        val eps0 = 8.854e-12
                        val a = inputs["Plate Area A (m²)"] ?: 0.05
                        val d = (inputs["Separation d (mm)"] ?: 1.0) * 1e-3
                        val k = inputs["Dielectric Constant K"] ?: 1.0
                        (k * eps0 * a) / d
                    },
                    outputSymbol = "Capacitance (C)",
                    outputUnit = "Farads (F)"
                ),
                PhysicsFormula(
                    title = "Capacitor Stored Energy",
                    formulaLatex = "U = 0.5 × C × V²",
                    symbols = listOf("U" to "Stored Energy (Joules)", "C" to "Capacitance (F)", "V" to "Voltage (Volts)"),
                    quickDescription = "Electrostatic potential energy stored in electric field.",
                    canCalculate = true,
                    defaultInputs = mapOf("Capacitance C (μF)" to 100.0, "Voltage V (V)" to 12.0),
                    calculateFn = { inputs ->
                        val c = (inputs["Capacitance C (μF)"] ?: 100.0) * 1e-6
                        val v = inputs["Voltage V (V)"] ?: 12.0
                        0.5 * c * v * v
                    },
                    outputSymbol = "Stored Energy (U)",
                    outputUnit = "Joules (J)"
                )
            ),
            importantLawsAndRules = listOf(
                "Electric field is always ZERO inside a hollow charged conductor (Electrostatic Shielding).",
                "Equipotential surfaces are always perpendicular to electric field lines at every point.",
                "Inserting a dielectric slab (K > 1) increases capacitance by factor K: C = K C₀."
            ),
            realWorldApplications = listOf(
                "Capacitors in electronic flash units, defibrillators, and AC power supply filter circuits.",
                "Electrostatic shielding protecting sensitive electronic circuits and Faraday cages.",
                "Touchscreen capacitive sensor matrices in smartphones and tablets."
            )
        ),
        PhysicsTopic(
            id = "c12_current_electricity",
            grade = PhysicsGrade.CLASS_12,
            branch = PhysicsBranch.ELECTROMAGNETISM,
            chapterTitle = "Current Electricity",
            topicTitle = "Drift Velocity, Kirchhoff's Laws, Wheatstone Bridge & Potentiometer",
            summary = "I = n e A v_d, Drift velocity v_d = eEτ/m, Kirchhoff's Junction & Loop Laws, Balanced Wheatstone Bridge P/Q = R/S, EMF vs terminal voltage.",
            detailedExplanation = """
                Microscopic Model of Current:
                • Drift Velocity (v_d): Average velocity acquired by conduction electrons due to applied electric field:
                  v_d = (e E τ) / m
                  Current I = n × e × A × v_d
                  Current Density J = I / A = σ E = n e² τ E / m (where τ is relaxation time).
                
                Kirchhoff's Laws of Electrical Circuits:
                1. Kirchhoff's Current Law (KCL / Junction Rule):
                   Sum of currents entering a junction equals sum of currents leaving: Σ I = 0 (Based on Conservation of Charge).
                2. Kirchhoff's Voltage Law (KVL / Loop Rule):
                   The algebraic sum of changes in potential around any closed circuit loop is zero: Σ ΔV = 0 (Based on Conservation of Energy).
                
                Wheatstone Bridge:
                • Four resistors P, Q, R, S arranged in a bridge network.
                • Condition for balance (galvanometer current I_g = 0):
                  P / Q = R / S
                
                EMF and Internal Resistance:
                • Terminal Voltage: V = E - I r (discharging) or V = E + I r (charging), where E is electromotive force and r is cell internal resistance.
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "Drift Velocity Current Relation",
                    formulaLatex = "I = n × e × A × v_d",
                    symbols = listOf("I" to "Current (A)", "n" to "Free electron number density (m⁻³)", "e" to "1.6 × 10⁻¹⁹ C", "A" to "Wire area (m²)", "v_d" to "Drift velocity (m/s)"),
                    quickDescription = "Microscopic definition of conduction current."
                ),
                PhysicsFormula(
                    title = "Balanced Wheatstone Bridge",
                    formulaLatex = "P / Q = R / S  ⟹  S = (Q × R) / P",
                    symbols = listOf("P, Q" to "Ratio arm resistances (Ω)", "R" to "Known resistance (Ω)", "S" to "Unknown resistance (Ω)"),
                    quickDescription = "Null deflection condition for precision resistance measurement.",
                    canCalculate = true,
                    defaultInputs = mapOf("Resistor P (Ω)" to 100.0, "Resistor Q (Ω)" to 200.0, "Resistor R (Ω)" to 50.0),
                    calculateFn = { inputs ->
                        val p = inputs["Resistor P (Ω)"] ?: 100.0
                        val q = inputs["Resistor Q (Ω)"] ?: 200.0
                        val r = inputs["Resistor R (Ω)"] ?: 50.0
                        (q * r) / p
                    },
                    outputSymbol = "Unknown Resistance (S)",
                    outputUnit = "Ohms (Ω)"
                ),
                PhysicsFormula(
                    title = "Cell Terminal Potential",
                    formulaLatex = "V = E - I × r",
                    symbols = listOf("V" to "Terminal Voltage (V)", "E" to "Cell EMF (V)", "I" to "Circuit Current (A)", "r" to "Internal Resistance (Ω)"),
                    quickDescription = "Terminal voltage drops below EMF during current discharge.",
                    canCalculate = true,
                    defaultInputs = mapOf("EMF E (V)" to 12.0, "Current I (A)" to 2.0, "Internal res r (Ω)" to 0.5),
                    calculateFn = { inputs ->
                        val e = inputs["EMF E (V)"] ?: 12.0
                        val i = inputs["Current I (A)"] ?: 2.0
                        val r = inputs["Internal res r (Ω)"] ?: 0.5
                        e - i * r
                    },
                    outputSymbol = "Terminal Voltage (V)",
                    outputUnit = "Volts (V)"
                )
            ),
            importantLawsAndRules = listOf(
                "KCL represents Conservation of Electric Charge; KVL represents Conservation of Energy.",
                "Meter Bridge is a practical implementation of the Wheatstone Bridge for measuring unknown resistance.",
                "Potentiometer measures potential without drawing any current at null point, acting as an ideal infinite-resistance voltmeter."
            ),
            realWorldApplications = listOf(
                "Strain gauges in load cells using Wheatstone bridge networks to measure mechanical deformation.",
                "Precision battery state-of-charge monitoring in electric vehicles.",
                "Circuit analysis in complex multi-loop electronic hardware systems."
            )
        ),
        PhysicsTopic(
            id = "c12_magnetism_emi_ac",
            grade = PhysicsGrade.CLASS_12,
            branch = PhysicsBranch.ELECTROMAGNETISM,
            chapterTitle = "Magnetism, EMI & Alternating Current",
            topicTitle = "Biot-Savart Law, Ampere's Law, Faraday's Law, Transformers & LCR Series Circuit",
            summary = "dB = (μ₀/4π) (I dl × r̂)/r², Ampere's circuital law ∮B·dl = μ₀I, Faraday's law ε = -dΦ/dt, LCR impedance Z = √(R² + (X_L - X_C)²), Resonant freq f₀ = 1/(2π√LC).",
            detailedExplanation = """
                Biot-Savart Law & Ampere's Circuital Law:
                • Biot-Savart Law: Magnetic field produced by a current element:
                  dB = (μ₀ / 4π) × [I (dl × r̂) / r²]
                • Ampere's Circuital Law: ∮ B · dl = μ₀ I_enclosed
                • Magnetic field at center of circular coil: B = (μ₀ N I) / (2 R).
                
                Faraday's Laws of Electromagnetic Induction:
                1. Whenever magnetic flux linked with a circuit changes, an EMF is induced.
                2. The magnitude of induced EMF is proportional to time rate of change of magnetic flux:
                   ε = - N (dΦ_B / dt) (Lenz's Law gives the negative sign: induced current opposes the change in flux causing it).
                
                Alternating Current & Series LCR Circuit:
                • Instantaneous Voltage: V(t) = V₀ sin(ωt); RMS values: V_rms = V₀ / √2, I_rms = I₀ / √2.
                • Inductive Reactance: X_L = ω L = 2π f L
                • Capacitive Reactance: X_C = 1 / (ω C) = 1 / (2π f C)
                • Impedance (Z): Z = √[R² + (X_L - X_C)²]
                • Electrical Resonance (X_L = X_C): Resonance Frequency f₀ = 1 / (2π √(L C)) (Impedance is minimum Z = R, current is maximum).
                
                Transformer:
                • Step-up or Step-down AC voltage: V_s / V_p = N_s / N_p = I_p / I_s.
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "Faraday's Law of Induction",
                    formulaLatex = "ε = -N × (dΦ / dt)",
                    symbols = listOf("ε" to "Induced EMF (Volts)", "N" to "Number of turns", "Φ" to "Magnetic flux (Weber, Wb = T·m²)", "t" to "Time (s)"),
                    quickDescription = "Magnitude and direction of induced electromagnetic force."
                ),
                PhysicsFormula(
                    title = "LCR Series Circuit Impedance",
                    formulaLatex = "Z = √[R² + (2πfL - 1/(2πfC))²]",
                    symbols = listOf("Z" to "Total Impedance (Ω)", "R" to "Resistance (Ω)", "L" to "Inductance (H)", "C" to "Capacitance (F)", "f" to "AC Frequency (Hz)"),
                    quickDescription = "Total effective opposition to AC current in LCR circuit.",
                    canCalculate = true,
                    defaultInputs = mapOf("Resistance R (Ω)" to 50.0, "Inductance L (mH)" to 100.0, "Capacitance C (μF)" to 10.0, "Frequency f (Hz)" to 50.0),
                    calculateFn = { inputs ->
                        val r = inputs["Resistance R (Ω)"] ?: 50.0
                        val l = (inputs["Inductance L (mH)"] ?: 100.0) * 1e-3
                        val c = (inputs["Capacitance C (μF)"] ?: 10.0) * 1e-6
                        val f = inputs["Frequency f (Hz)"] ?: 50.0
                        val omega = 2 * Math.PI * f
                        val xl = omega * l
                        val xc = 1.0 / (omega * c)
                        Math.sqrt(r * r + (xl - xc) * (xl - xc))
                    },
                    outputSymbol = "Impedance (Z)",
                    outputUnit = "Ohms (Ω)"
                ),
                PhysicsFormula(
                    title = "LCR Resonance Frequency",
                    formulaLatex = "f₀ = 1 / [2π × √(L × C)]",
                    symbols = listOf("f₀" to "Resonance frequency (Hz)", "L" to "Inductance (Henry, H)", "C" to "Capacitance (Farads, F)"),
                    quickDescription = "Frequency where inductive and capacitive reactances cancel.",
                    canCalculate = true,
                    defaultInputs = mapOf("Inductance L (mH)" to 10.0, "Capacitance C (nF)" to 100.0),
                    calculateFn = { inputs ->
                        val l = (inputs["Inductance L (mH)"] ?: 10.0) * 1e-3
                        val c = (inputs["Capacitance C (nF)"] ?: 100.0) * 1e-9
                        1.0 / (2.0 * Math.PI * Math.sqrt(l * c))
                    },
                    outputSymbol = "Resonance Frequency (f₀)",
                    outputUnit = "Hz"
                ),
                PhysicsFormula(
                    title = "Ideal Transformer Transformation Ratio",
                    formulaLatex = "V_s / V_p = N_s / N_p = I_p / I_s",
                    symbols = listOf("V_s, V_p" to "Secondary & Primary voltages", "N_s, N_p" to "Secondary & Primary coil turns", "I_s, I_p" to "Secondary & Primary currents"),
                    quickDescription = "Voltage and current transformation ratio."
                )
            ),
            importantLawsAndRules = listOf(
                "Lenz's Law is a direct consequence of the Law of Conservation of Energy.",
                "Eddy currents induce opposing magnetic damping, minimized using laminated soft-iron cores.",
                "Transformers operate ONLY on Alternating Current (AC); they do not work on constant Direct Current (DC)."
            ),
            realWorldApplications = listOf(
                "High-voltage AC national power grid transmission minimizing I²R heating losses.",
                "Radio tuning receivers selecting specific broadcast frequencies via LC resonance.",
                "Induction cooktops, wireless smartphone chargers, and eddy current electromagnetic train brakes."
            )
        ),
        PhysicsTopic(
            id = "c12_wave_optics",
            grade = PhysicsGrade.CLASS_12,
            branch = PhysicsBranch.OPTICS,
            chapterTitle = "Wave Optics",
            topicTitle = "Huygens' Principle, Interference, YDSE & Diffraction",
            summary = "Wavefronts, Young's Double Slit Experiment fringe width β = λD/d, Single slit diffraction central maximum width 2λD/a, Polarization & Brewster's law.",
            detailedExplanation = """
                Huygens' Wave Theory:
                • Wavefront: The continuous locus of all particles vibrating in the same phase.
                • Secondary Wavelets: Every point on a wavefront behaves as a source of secondary spherical wavelets traveling with the speed of light.
                • Successfully proves Laws of Reflection and Refraction using wave propagation.
                
                Interference of Light & Young's Double Slit Experiment (YDSE):
                Two coherent light sources of wavelength λ separated by distance d casting interference fringes on a screen at distance D (D >> d):
                • Constructive Interference (Bright Fringes): Path difference Δx = n λ (n = 0, 1, 2, ...).
                • Destructive Interference (Dark Fringes): Path difference Δx = (2n - 1) λ / 2.
                • Fringe Width (β): Separation between any two consecutive bright or dark fringes:
                  β = (λ × D) / d
                
                Diffraction of Light (Single Slit):
                • Bending of light around sharp edges and corners into the geometrical shadow.
                • Central Maximum Angular Width: 2θ = 2 λ / a
                • Linear Width of Central Maximum: W = (2 λ D) / a
                
                Polarization:
                • Brewster's Law: Reflected ray is completely plane-polarized when incident at Brewster's angle θ_p:
                  tan(θ_p) = μ (and reflected ray is perpendicular to refracted ray).
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "YDSE Fringe Width",
                    formulaLatex = "β = (λ × D) / d",
                    symbols = listOf("β" to "Fringe Width (m)", "λ" to "Wavelength of light (m)", "D" to "Distance to screen (m)", "d" to "Slit separation (m)"),
                    quickDescription = "Spacing between interference fringes in Young's Double Slit Experiment.",
                    canCalculate = true,
                    defaultInputs = mapOf("Wavelength λ (nm)" to 589.0, "Screen Distance D (m)" to 1.5, "Slit distance d (mm)" to 0.5),
                    calculateFn = { inputs ->
                        val lambda = (inputs["Wavelength λ (nm)"] ?: 589.0) * 1e-9
                        val D = inputs["Screen Distance D (m)"] ?: 1.5
                        val d = (inputs["Slit distance d (mm)"] ?: 0.5) * 1e-3
                        (lambda * D) / d * 1000.0 // output in mm
                    },
                    outputSymbol = "Fringe Width (β)",
                    outputUnit = "mm"
                ),
                PhysicsFormula(
                    title = "Brewster's Angle Law",
                    formulaLatex = "tan(θ_p) = μ",
                    symbols = listOf("θ_p" to "Polarizing Brewster angle", "μ" to "Refractive index of medium"),
                    quickDescription = "Angle of incidence producing completely polarized reflected light."
                )
            ),
            importantLawsAndRules = listOf(
                "Coherent sources have identical frequency and a constant or zero phase difference.",
                "Diffraction central maximum is twice as wide as secondary maxima and contains ~85% of total light intensity.",
                "Transverse nature of light waves is proven uniquely by Polarization (sound waves are longitudinal and cannot be polarized)."
            ),
            realWorldApplications = listOf(
                "Anti-reflective coatings on eyeglasses and camera lenses using destructive thin-film interference.",
                "Polaroid sunglasses eliminating blinding horizontal glare from wet roads and water bodies.",
                "Diffraction gratings in spectrometers analyzing atomic emission spectrum lines."
            )
        ),
        PhysicsTopic(
            id = "c12_modern_physics",
            grade = PhysicsGrade.CLASS_12,
            branch = PhysicsBranch.MODERN_PHYSICS,
            chapterTitle = "Dual Nature, Atoms, Nuclei & Semiconductors",
            topicTitle = "Photoelectric Effect, Bohr Model, Nuclear Binding & p-n Junction Diodes",
            summary = "Einstein's photoelectric equation hν = Φ + KE_max, de Broglie λ = h/p, Bohr radii r_n ∝ n², Energy E_n = -13.6/n² eV, Mass defect ΔE = Δm c², Diodes & Logic gates.",
            detailedExplanation = """
                Dual Nature of Radiation & Photoelectric Effect:
                • Photon Energy: E = h ν = h c / λ (where Planck's constant h = 6.626 × 10⁻³⁴ J·s).
                • Einstein's Photoelectric Equation:
                  h ν = Φ₀ + KE_max = h ν₀ + e V_s
                  Where Φ₀ = work function, ν₀ = threshold frequency, V_s = stopping potential.
                • de Broglie Wavelength: Matter has wave-particle duality: λ = h / p = h / (m v) = h / √(2 m q V).
                
                Bohr Model of the Hydrogen Atom:
                • Quantization of Angular Momentum: L = m v r = n (h / 2π).
                • Orbit Radii: r_n = (0.529 Å) × n² / Z
                • Energy Levels: E_n = - (13.6 eV) × (Z² / n²)
                • Rydberg Spectral Emission Formula: 1/λ = R_H × [1/n₁² - 1/n₂²]
                
                Nuclear Physics:
                • Mass Defect & Binding Energy: E = Δm × c² (1 amu = 931.5 MeV).
                • Nuclear Stability: Peaks near Iron-56 (Fe-56) with ~8.8 MeV per nucleon.
                
                Semiconductors:
                • Intrinsic vs Extrinsic (n-type doped with pentavalent donors, p-type doped with trivalent acceptors).
                • p-n Junction Diode: Forward bias lowers barrier potential (conducts); Reverse bias widens depletion layer (blocks current).
                • Rectifiers (Half-wave & Full-wave converting AC to DC), Zener diode (voltage regulator), LEDs, Solar cells.
            """.trimIndent(),
            keyFormulas = listOf(
                PhysicsFormula(
                    title = "Einstein's Photoelectric Equation",
                    formulaLatex = "KE_max = h × ν - Φ₀ = e × V_stopping",
                    symbols = listOf("KE_max" to "Maximum electron kinetic energy (J)", "h" to "Planck's constant 6.626×10⁻³⁴ J·s", "ν" to "Incident light frequency (Hz)", "Φ₀" to "Work function (J)"),
                    quickDescription = "Conservation of energy in photoelectric photon absorption.",
                    canCalculate = true,
                    defaultInputs = mapOf("Photon Energy hν (eV)" to 4.5, "Work Function Φ₀ (eV)" to 2.2),
                    calculateFn = { inputs ->
                        val e_phot = inputs["Photon Energy hν (eV)"] ?: 4.5
                        val phi = inputs["Work Function Φ₀ (eV)"] ?: 2.2
                        Math.max(0.0, e_phot - phi)
                    },
                    outputSymbol = "Max Kinetic Energy (KE_max)",
                    outputUnit = "eV"
                ),
                PhysicsFormula(
                    title = "de Broglie Matter Wavelength",
                    formulaLatex = "λ = h / p = h / (m × v)",
                    symbols = listOf("λ" to "de Broglie wavelength (m)", "h" to "6.626 × 10⁻³⁴ J·s", "m" to "Mass (kg)", "v" to "Velocity (m/s)"),
                    quickDescription = "Wavelength associated with any moving particle.",
                    canCalculate = true,
                    defaultInputs = mapOf("Mass m (kg)" to 9.11e-31, "Velocity v (m/s)" to 1e6),
                    calculateFn = { inputs ->
                        val h = 6.626e-34
                        val m = inputs["Mass m (kg)"] ?: 9.11e-31
                        val v = inputs["Velocity v (m/s)"] ?: 1e6
                        (h / (m * v)) * 1e9 // output in nm
                    },
                    outputSymbol = "Wavelength (λ)",
                    outputUnit = "nm"
                ),
                PhysicsFormula(
                    title = "Hydrogen Atom Energy Level",
                    formulaLatex = "E_n = -13.6 / n² (eV)",
                    symbols = listOf("E_n" to "Energy of n-th orbital level (eV)", "n" to "Principal quantum number (1, 2, 3, ...)"),
                    quickDescription = "Quantized electron energy in Bohr hydrogen model.",
                    canCalculate = true,
                    defaultInputs = mapOf("Principal Quantum Number n" to 1.0),
                    calculateFn = { inputs ->
                        val n = inputs["Principal Quantum Number n"] ?: 1.0
                        -13.6 / (n * n)
                    },
                    outputSymbol = "Orbital Energy (E_n)",
                    outputUnit = "eV"
                ),
                PhysicsFormula(
                    title = "Mass-Energy Equivalence",
                    formulaLatex = "E = Δm × c²  |  1 amu = 931.5 MeV",
                    symbols = listOf("E" to "Energy released (Joules)", "Δm" to "Mass defect (kg)", "c" to "Speed of light 3×10⁸ m/s"),
                    quickDescription = "Einstein's mass defect nuclear energy relation."
                )
            ),
            importantLawsAndRules = listOf(
                "Photoelectric emission is instantaneous (< 10⁻⁹ s) and occurs only when incident frequency ν exceeds threshold frequency ν₀.",
                "Spectral Series in Hydrogen: Lyman (UV, n₁=1), Balmer (Visible, n₁=2), Paschen (Infrared, n₁=3), Brackett (IR, n₁=4), Pfund (IR, n₁=5).",
                "Zener diode operates under REVERSE BREAKDOWN voltage to provide a stable, constant output voltage."
            ),
            realWorldApplications = listOf(
                "Solar photovoltaic panels generating clean renewable electrical energy from sunlight.",
                "Electron microscopes achieving picometer resolution using ultra-short de Broglie electron wavelengths.",
                "Solid-state silicon microchips, microprocessors, and smartphone semiconductor circuits."
            )
        )
    )
}
