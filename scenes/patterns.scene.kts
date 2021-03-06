val floor = Plane(
    material = Material(
        specular = 1,
        color = Color.Red,
        pattern = CheckerPattern(Color.Black, Color.White)
    )
)

val middle = Sphere(
    transform = Matrix.translation(-0.5, 1, 0.5),
    material = Material(
        color = Color(0.1, 1, 0.5),
        diffuse = 0.7F,
        specular = 0.3F,
        pattern = CheckerPattern(Color(0.1, 1, 0.5), Color.White, Matrix.scaling(0.1, 0.1, 1))
    )
)

val right = Sphere(
    transform = Matrix.translation(1.5, 0.5, -0.5) * Matrix.scaling(0.5, 0.5, 0.5),
    material = Material(
        color = Color(0.5, 1, 0.1),
        diffuse = 0.7,
        specular = 0.3
    )
)

val left = Sphere(
    transform = Matrix.translation(-1.5, 0.33, -0.75) * Matrix.scaling(0.33, 0.33, 0.33),
)

val world = World(
    lights = listOf(
        PointLight(
            Point(-10, 10, -10),
            Color(1, 1, 1)
        )
    ),
    objects = listOf(floor, middle, right, left)
)

val camera = Camera(
    hsize = 4 * 500,
    vsize = 4 * 250,
    fieldOfView = PI / 3,
    transform = viewTransform(
        from = Point(0, 1.5, -5),
        to = Point(0, 1, 0),
        up = Vector(0, 1, 0)
    )
)

Scene(camera = camera, world = world)