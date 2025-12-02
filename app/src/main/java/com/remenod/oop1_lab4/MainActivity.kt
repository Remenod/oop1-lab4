package com.remenod.oop1_lab4

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.remenod.oop1_lab4.game.GameObject
import com.remenod.oop1_lab4.game.GameView
import com.remenod.oop1_lab4.game.figures.Circle
import com.remenod.oop1_lab4.game.physics.PhysicsBody

object PhysicsData {
    var ax = 0f
    var ay = 0f
}

class MainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var gameView: GameView
    private val objects = mutableListOf<GameObject>()
    private var running = false

    private lateinit var sensorManager: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gameView = GameView(this, objects)
        setContentView(gameView)

        objects += GameObject(
            Circle(200f, 200f, 50f),
            PhysicsBody(0f, 0f, 1f, 0.8f)
        )

        objects += GameObject(
            Circle(500f, 400f, 70f),
            PhysicsBody(0f, 0f, 1f, 0.8f)
        )

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
                    GameObject.resolveCollision(objects[i], objects[j])
                }
            }
        }

    }

}
