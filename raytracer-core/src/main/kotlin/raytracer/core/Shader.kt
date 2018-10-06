package raytracer.core

sealed class Shader {
    abstract fun calculateColor(viewRay: Ray, light: Light, t: T, shapes: List<Shape>): Color
}

class BlinnPhong(val values: List<Double>) : Shader() {
    override fun calculateColor(viewRay: Ray, light: Light, t: T, shapes: List<Shape>): Color {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}

class Lambertion(val values: List<Double>) : Shader() {
    override fun calculateColor(viewRay: Ray, light: Light, t: T, shapes: List<Shape>): Color {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
