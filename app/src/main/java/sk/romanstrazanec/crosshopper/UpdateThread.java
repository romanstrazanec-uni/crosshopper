package sk.romanstrazanec.crosshopper;

import android.os.Handler;

public class UpdateThread extends Thread {
    Handler updatingHandler;

    public UpdateThread(Handler uh) {
        super();
        updatingHandler = uh;
    }

    @Override
    public void run() {
        while (true) {
            try {
                this.sleep(50);
            } catch (Exception ex) {
            }
            updatingHandler.sendEmptyMessage(0);
        }
    }
}
