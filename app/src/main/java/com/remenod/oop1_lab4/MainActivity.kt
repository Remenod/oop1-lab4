package com.remenod.oop1_lab4

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.FloatingActionButton
import com.remenod.oop1_lab4.game.GameObject
import com.remenod.oop1_lab4.game.GameView
import com.remenod.oop1_lab4.game.figures.Circle
import com.remenod.oop1_lab4.game.figures.Rhomb
import com.remenod.oop1_lab4.game.figures.Square
import com.remenod.oop1_lab4.game.physics.PhysicsBody

object PhysicsData {
    var ax = 0f
    var ay = 0f
}

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var gameView: GameView
    private val objects = mutableListOf<GameObject>()
    private var running = false

    private lateinit var sensorManager: SensorManager

    private fun addFloatingButton() {
        val fab = com.google.android.material.floatingactionbutton.FloatingActionButton(this)

        fab.setImageResource(android.R.drawable.ic_input_add)

        fab.size = com.google.android.material.floatingactionbutton.FloatingActionButton.SIZE_NORMAL

        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )

        params.gravity = Gravity.TOP or Gravity.LEFT

        val margin = (16 * resources.displayMetrics.density).toInt()
        params.setMargins(margin, margin, margin, margin)

        addContentView(fab, params)

        fab.setOnClickListener {
            showAddObjectDialog()
        }
    }


    private fun showAddObjectDialog() {
        val types = arrayOf("Circle", "Square", "Rhomb")

        AlertDialog.Builder(this)
            .setTitle("New object")
            .setItems(types) { _, index ->
                val obj = when (index) {
                    0 -> GameObject(Circle(300f, 300f, 70f), PhysicsBody())
                    1 -> GameObject(Square(300f, 300f, 200f), PhysicsBody())
                    2 -> GameObject(Rhomb(300f, 300f, 150f, 220f), PhysicsBody())
                    else -> null
                }
                if (obj != null) objects += obj
            }
            .show()
    }

    private fun openEditDialog(obj: GameObject) {

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        fun field(label: String, value: Float): EditText {
            val t = TextView(this)
            t.text = label
            layout.addView(t)

            val e = EditText(this)
            e.setText(value.toString())
            e.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            layout.addView(e)
            return e
        }

        val massField = field("Mass", obj.physics.mass)
        val bounceField = field("Bounciness", obj.physics.bounce)

        lateinit var size1Field: EditText
        lateinit var size2Field: EditText

        when (val f = obj.figure) {
            is Circle -> {
                size1Field = field("Radius", f.radius)
            }
            is Square -> {
                size1Field = field("Side", f.side)
            }
            is Rhomb -> {
                size1Field = field("Horizontal Diagonal", f.horDiag)
                size2Field = field("Vertical Diagonal", f.vertDiag)
            }
        }

        AlertDialog.Builder(this)
            .setTitle("Object Redacting")
            .setView(layout)
            .setPositiveButton("OK") { _, _ ->

                obj.physics.mass =
                    massField.text.toString().toFloatOrNull() ?: obj.physics.mass

                obj.physics.bounce =
                    bounceField.text.toString().toFloatOrNull() ?: obj.physics.bounce

                when (val f = obj.figure) {
                    is Circle -> {
                        f.radius = size1Field.text.toString().toFloatOrNull() ?: f.radius
                    }
                    is Square -> {
                        f.side = size1Field.text.toString().toFloatOrNull() ?: f.side
                    }
                    is Rhomb -> {
                        f.horDiag = size1Field.text.toString().toFloatOrNull() ?: f.horDiag
                        f.vertDiag = size2Field.text.toString().toFloatOrNull() ?: f.vertDiag
                    }
                }
            }
            .setNeutralButton("Delete") { _, _ ->
                objects.remove(obj)
            }
            .setNegativeButton("Close", null)
            .show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gameView = GameView(this, objects)
        setContentView(gameView)

        addFloatingButton()

        objects += GameObject(
            Circle(200f, 200f, 50f),
            PhysicsBody(0f, 0f, 1f, 0.8f)
        )
        objects += GameObject(
            Circle(500f, 400f, 70f),
            PhysicsBody(0f, 0f, 1f, 0.8f)
        )
        objects += GameObject(
            Square(500f, 400f, 230f),
            PhysicsBody(0f, 0f, 10f, 0.8f)
        )
        objects += GameObject(
            Rhomb(500f, 400f, 130f, 200f),
            PhysicsBody(0f, 0f, 1f, 0.8f)
        )

        gameView.onObjectClick = { obj ->
            openEditDialog(obj)
        }

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accel != null) {
            sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onResume() {
        super.onResume()
        running = true
        startLoop()
    }

    override fun onPause() {
        super.onPause()
        running = false
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return
        PhysicsData.ax = event.values[0]
        PhysicsData.ay = event.values[1]
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun startLoop() {
        Thread {
            var last = System.nanoTime()
            while (running) {
                val now = System.nanoTime()
                val dt = (now - last) / 1_000_000_000f
                last = now

                update(dt)
                gameView.postInvalidate()
                Thread.sleep(16)
            }
        }.start()
    }

    private fun update(dt: Float) {
        val width = gameView.width.toFloat()
        val height = gameView.height.toFloat()

        for (o in objects) {

            o.physics.applyAccelerometer(dt)

            o.physics.update(o.figure, dt)

            o.physics.bounceFromWalls(o.figure, width, height)
        }

        for (i in 0 until objects.size) {
            for (j in i + 1 until objects.size) {
                if (objects[i].figure.intersects(objects[j].figure)) {
                    PhysicsBody.resolveCollision(objects[i], objects[j])
                }
            }
        }

    }

}
