package ng.max.binger.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import ng.max.binger.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
