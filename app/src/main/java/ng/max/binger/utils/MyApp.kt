package ng.max.binger.utils

import android.app.Application
import android.os.SystemClock

import java.util.concurrent.TimeUnit

import ng.max.binger.R
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

/**
 * Created by Olayinka_Peter on 12/18/2017.
 */

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gothic.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )

        SystemClock.sleep(TimeUnit.SECONDS.toMillis(2))
    }
}
