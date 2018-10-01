package raytracer.core

sealed class Shader {
    abstract fun calculateColor(viewRay: Ray, light: Light, t: T, shapes: List<Shape>): Color
}

class BlinnPhong: Shader() {
    override fun calculateColor(viewRay: Ray, light: Light, t: T, shapes: List<Shape>): Color {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class Lambertion: Shader() {
    override fun calculateColor(viewRay: Ray, light: Light, t: T, shapes: List<Shape>): Color {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
