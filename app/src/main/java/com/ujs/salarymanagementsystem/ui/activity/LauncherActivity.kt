package com.ujs.salarymanagementsystem.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ujs.salarymanagementsystem.R

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0) {
            finish()
            return
        }
        // val intent = Intent(this, MainActivity().javaClass)    // 主页
        val intent = Intent(this, MainActivity().javaClass)    // 主页
        startActivity(intent)
        finish()
    }
}