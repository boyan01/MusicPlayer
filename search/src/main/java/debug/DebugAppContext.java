package debug;

import android.app.Application;

import tech.summerly.quiet.commonlib.base.BaseModule;

public class DebugAppContext extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BaseModule.init(this);
    }
}
