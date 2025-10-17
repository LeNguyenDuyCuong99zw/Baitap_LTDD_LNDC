package vn.kotlinproject.baitap

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvMessage = findViewById<TextView>(R.id.tvMessage)
        val btnSayHi = findViewById<Button>(R.id.btnSayHi)

        btnSayHi.setOnClickListener {
            tvMessage.text = "I’m\nLe Nguyen Duy Cuong"

        }
    }
}
