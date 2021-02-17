package sk.romanstrazanec.crosshopper

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import java.util.*

class GameCanvas(context: Context) : View(context) {
    private var paint: Paint = Paint()
    private var pic: Bitmap? = null
    private var bunny: Bunny
    private var carrot: Carrot
    private var cars: ArrayList<Car>
    private var carWidth: Int
    private var maxX = 0f
    private var maxY = 0f

    // autá z ľavej strany
    private var leftRoads: FloatArray

    // autá z pravej strany
    private var rightRoads: FloatArray

    // aby sa negenerovali autá na tej istej ceste
    private var previousLeftRoad = -1
    private var previousRightRoad = -1
    private var previousCarrotLayer = 0 // kde bol pred tým carrot
    private var layers: FloatArray // y-nové súradnice, kam môže bunny ísť

    private var bunnyLayer: Int // kde je bunny

    private var score: Int

    private fun setbg(context: Context) {
        val pozadie = Bitmap.createBitmap(BitmapFactory.decodeResource(this.resources, R.drawable.background_image))
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        maxX = size.x.toFloat()
        maxY = size.y.toFloat()
        pic = Bitmap.createScaledBitmap(pozadie, size.x, size.y, false)
    }

    fun update() {
        // ak bunny zasiahnutý
        if (crash()) {
            score = 0
            bunnyLayer = layers.size - 1
            previousCarrotLayer = 0
            carrot.y = layers[previousCarrotLayer]
            bunny.y = layers[bunnyLayer]
        }

        // ak zoberie mrkvu
        if (bunny.y == carrot.y) {
            score++
            carrot.y = randomCarrotPosition()
        }
        if (cars.size < 7) {
            if (Math.random() > 0.9) {
                cars.add(Car((0 - carWidth).toFloat(), randomLeftRoad(), carWidth, (maxY / 35.52).toInt(),
                        Color.rgb(100 + (Math.random() * 156).toInt(), 100 + (Math.random() * 156).toInt(), 100 + (Math.random() * 156).toInt()),
                        30 + (Math.random() * 21).toInt(), 1))
            }
            if (Math.random() > 0.9) {
                cars.add(Car(maxX, randomRightRoad(), carWidth, (maxY / 35.52).toInt(),
                        Color.rgb(100 + (Math.random() * 156).toInt(), 100 + (Math.random() * 156).toInt(), 100 + (Math.random() * 156).toInt()),
                        30 + (Math.random() * 21).toInt(), -1))
            }
        }
        if (cars.size > 0) {
            var c: Car
            for (i in cars.indices.reversed()) {
                c = cars[i]
                c.move()
                if (c.x > maxX || c.x + c.width < 0) cars.removeAt(i)
            }
        }
    }

    private fun randomLeftRoad(): Float {
        var i: Int
        do {
            i = (Math.random() * leftRoads.size).toInt()
        } while (i == previousLeftRoad)
        previousLeftRoad = i
        return leftRoads[i]
    }

    private fun randomRightRoad(): Float {
        var i: Int
        do {
            i = (Math.random() * rightRoads.size).toInt()
        } while (i == previousRightRoad)
        previousRightRoad = i
        return rightRoads[i]
    }

    private fun randomCarrotPosition(): Float {
        var i: Int
        do {
            i = (Math.random() * layers.size).toInt()
        } while (i == previousCarrotLayer)
        previousCarrotLayer = i
        return layers[i]
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val e = event.action
        if (e == MotionEvent.ACTION_DOWN) {
            if (event.y > bunny.y) {
                moveBunnyDown()
            } else {
                moveBunnyUp()
            }
            invalidate()
        }
        return true
    }

    private fun moveBunnyUp() {
        if (bunnyLayer - 1 > -1) {
            bunnyLayer--
            bunny.y = layers[bunnyLayer]
        }
    }

    private fun moveBunnyDown() {
        if (bunnyLayer + 1 < layers.size) {
            bunnyLayer++
            bunny.y = layers[bunnyLayer]
        }
    }

    private fun crash(): Boolean {
        for (i in cars.indices) {
            if (isNear(cars[i])) return true
        }
        return false
    }

    private fun isNear(c: Car): Boolean {
        return bunny.y < c.y + c.height && bunny.y > c.y && bunny.x + bunny.r > c.x && bunny.x - bunny.r < c.x + c.width
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(pic!!, 0f, 0f, paint)
        drawBunny(canvas)
        drawCarrot(canvas)
        drawScoreCounter(canvas)
        for (i in cars.indices) {
            drawCar(canvas, cars[i])
        }
    }

    private fun drawBunny(canvas: Canvas) {
        paint.color = bunny.color
        canvas.drawCircle(bunny.x, bunny.y, bunny.r.toFloat(), paint)
    }

    private fun drawCar(canvas: Canvas, c: Car) {
        paint.color = c.color
        canvas.drawRect(c.x, c.y, c.x + c.width, c.y + c.height, paint)
    }

    private fun drawCarrot(canvas: Canvas) {
        paint.color = carrot.color
        canvas.drawCircle(carrot.x, carrot.y, maxY / 59.2f, paint)
    }

    private fun drawScoreCounter(canvas: Canvas) {
        paint.color = Color.WHITE
        canvas.drawRect(maxY / 177.6f, maxY / 177.6f, maxY / 14.8f, maxY / 25.37f, paint)
        paint.color = Color.BLACK
        paint.textSize = maxY / 25.37f
        canvas.drawText("" + score, maxY / 118.4f, maxY / 27.32f, paint)
    }

    init {
        setbg(context)
        leftRoads = floatArrayOf(.269144f * maxY, .347973f * maxY, .573198f * maxY, .797254f * maxY) //478.0f, 618.0f, 1018.0f, 1418.0f
        rightRoads = floatArrayOf(.094595f * maxY, .184685f * maxY, .488739f * maxY, .713964f * maxY) // 168.0f, 328.0f, 868.0f, 1268.0f
        layers = floatArrayOf(.038288f * maxY, .111486f * maxY, .195946f * maxY, .280405f * maxY, .359234f * maxY, .432432f * maxY, .5f * maxY, .584459f * maxY, .657658f * maxY, .725225f * maxY, .809685f * maxY, .882883f * maxY)
        carWidth = maxX.toInt() / 9
        bunnyLayer = layers.size - 1
        bunny = Bunny(maxX / 2, layers[bunnyLayer], (maxY / 35.52f).toInt(), Color.WHITE)
        carrot = Carrot(maxX / 2, layers[previousCarrotLayer], Color.rgb(255, 155, 0))
        cars = ArrayList()
        score = 0
    }
}