package com.nghiemduong1809.googledrvieapi

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.FileContent
import com.google.api.client.json.gson.GsonFactory.getDefaultInstance
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class MainViewModel(private val app: Application) : AndroidViewModel(app) {

    private var instanceDrive: Drive? = null

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

    fun backupDrive(uri: Uri) = GlobalScope.launch(Dispatchers.IO) {
        try {
            val file = File(uri.toString())
            val fileMetaData = com.google.api.services.drive.model.File()
            fileMetaData.name = file.name
            fileMetaData.parents = Collections.singletonList("appDataFolder")
            val mediaContent = FileContent("text/csv", file)
            getDrive()?.files()?.create(fileMetaData, mediaContent)?.setFields("id, parents")
                ?.execute()
        } catch (e: Exception) {
            Log.e("TAG123", "backupDrive: $e")
        }
    }

    private fun getDrive(): Drive? {
        GoogleSignIn.getLastSignedInAccount(app)?.let { googleSignInAccount ->
            val credential = GoogleAccountCredential.usingOAuth2(
                app, Collections.singletonList(DriveScopes.DRIVE_APPDATA)
            )
            credential.selectedAccount = googleSignInAccount.account
            instanceDrive = Drive.Builder(
                AndroidHttp.newCompatibleTransport(), getDefaultInstance(), credential
            ).setApplicationName(app.getString(R.string.app_name)).build()
        }
        return instanceDrive
    }
}