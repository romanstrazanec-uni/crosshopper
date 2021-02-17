package sk.romanstrazanec.crosshopper

import android.os.Handler

class UpdateThread(private var updatingHandler: Handler) : Thread() {
    override fun run() {
        while (true) {
            try {
                sleep(50)
            } catch (ex: Exception) {
            }
            updatingHandler.sendEmptyMessage(0)
        }
    }
}