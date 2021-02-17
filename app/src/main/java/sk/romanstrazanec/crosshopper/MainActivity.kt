package sk.romanstrazanec.crosshopper

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var myThread: UpdateThread? = null
    private var updateHandler: Handler? = null
    private var myCanvas: GameCanvas? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar!!.hide()
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        myCanvas = GameCanvas(this)
        setContentView(myCanvas)
        createHandler()
        myThread = UpdateThread(updateHandler!!)
        myThread!!.start()
    }

    private fun createHandler() {
        updateHandler = object : Handler() {
            override fun handleMessage(msg: Message) {
                myCanvas!!.update()
                myCanvas!!.invalidate()
                super.handleMessage(msg)
            }
        }
    }
}