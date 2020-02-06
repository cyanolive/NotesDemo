package com.example.notesdemo.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notesdemo.R

private const val LOGIN_VIEW = "LOGIN_VIEW"

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val view = supportFragmentManager.findFragmentByTag(LOGIN_VIEW) as LoginView?
            ?: LoginView()

        supportFragmentManager.beginTransaction()
            .replace(R.id.root_activity_login, view, LOGIN_VIEW)
            .commitNowAllowingStateLoss()
    }
}