package com.jawon.han.googleloginwithrestapi

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.gson.GsonBuilder
import com.jawon.han.googleloginwithrestapi.ui.theme.GoogleLoginWithRestApiTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.loginBtn)
        button.setOnClickListener { requestLogin() }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK){
            Log.d("jay", "success")
            val redirectUri = intent.data

            Log.d("jay", "${redirectUri}")
            Log.d("jay", "${redirectUri?.getQueryParameter("code")}")
        } else {
            Log.d("jay", "fail")
        }
    }

    private fun requestLogin(){
        val config = AuthorizationServiceConfiguration(
            Uri.parse("https://accounts.google.com/o/oauth2/v2/auth"),
            Uri.parse("https://www.googleapis.com/oauth2/v4/token"),
            null
        )

        val redirectUri = Uri.parse("com.jawon.han.googleloginwithrestapi:/oauth2callback")

        val request = AuthorizationRequest.Builder(
            config,
            "815911049800-hhs9l91v4f31in31sr5pt5ei94v0usst.apps.googleusercontent.com",
            ResponseTypeValues.CODE,
            redirectUri
        ).setScope("https://www.googleapis.com/auth/youtubepartner")
            .build()

        val authService = AuthorizationService(this)
        val authIntent = authService.getAuthorizationRequestIntent(request)

        launcher.launch(authIntent)
    }

}




interface GoogleAPiRequest{
    @POST("/o/oauth2/v2/auth")
    suspend fun getAuthToken(
        @Body request: AuthRequestModel
    ): Response<Unit>

    companion object{
        private val gson = GsonBuilder().setLenient().create()

        fun apiRetrofit(): GoogleAPiRequest {
            return Retrofit.Builder()
                .baseUrl("https://accounts.google.com")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(GoogleAPiRequest::class.java)
        }
    }
}

data class AuthRequestModel(
    val client_id: String = "815911049800-hhs9l91v4f31in31sr5pt5ei94v0usst.apps.googleusercontent.com",
    val response_type: String = "code",
    val scope: String = "email",
    val redirect_uri: String = ""
)

