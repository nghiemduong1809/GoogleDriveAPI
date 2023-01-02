package com.nghiemduong1809.googledrvieapi

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes

class MainViewModel(private val app: Application) : AndroidViewModel(app) {

    fun isUserSignIn(): Boolean {
        return GoogleSignIn.getLastSignedInAccount(app) != null
    }

    fun getIntentSignInGoogleAccount(): Intent {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail().requestScopes(Scope(DriveScopes.DRIVE_APPDATA)).build()
        val googleSignInClient = GoogleSignIn.getClient(app, gso)
        return googleSignInClient.signInIntent
    }

    fun getSelectFileIntent(): Intent {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "text/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        return Intent.createChooser(intent, "")
    }
}