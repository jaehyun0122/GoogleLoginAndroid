package com.jawon.han.googleloginwithrestapi

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse

class RedirectActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.response_layout)

        val resp = AuthorizationResponse.fromIntent(intent)
        val ex = AuthorizationException.fromIntent(intent)
        val redirectUri = intent.data

        Log.d("jay", "redirect ${redirectUri?.getQueryParameter("code")}")

        finish()
    }
}