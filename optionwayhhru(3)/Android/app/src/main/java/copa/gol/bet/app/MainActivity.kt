package copa.gol.bet.app

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.webkit.ValueCallback
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.web.AccompanistWebChromeClient
import copa.gol.bet.app.data.databse.UtilsDataStore
import copa.gol.bet.app.data.remoteConfig.RemoteConfigDataStore
import copa.gol.bet.app.ui.screens.*
import copa.gol.bet.app.ui.theme.OptionwayhhruTheme
import copa.gol.bet.app.utils.ConnectionInternet
import copa.gol.bet.app.utils.checkIsEmu
import copa.gol.bet.app.utils.isNetworkConnected
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {

    companion object {
        private const val SP_LINK = "SP_LINK"
        private const val SP_LINK_VALUE = "SP_LINK_VALUE"

        private const val INPUT_FILE_REQUEST_CODE = 1
        private const val FILECHOOSER_RESULTCODE = 1
    }

    private val mCameraPhotoPath: MutableState<String?> = mutableStateOf(null)
    private val mFilePathCallback: MutableState<ValueCallback<Array<Uri>>?> = mutableStateOf(null)
    private var mUploadMessage: MutableState<ValueCallback<Uri?>?> = mutableStateOf(null)
    private var mCapturedImageURI: MutableState<Uri?> = mutableStateOf(null)

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OptionwayhhruTheme {
                val navController = rememberNavController()
                val connectionInternet = remember { ConnectionInternet(application) }
                var checkInternet by rememberSaveable { mutableStateOf(isNetworkConnected(this)) }
                var loading by rememberSaveable { mutableStateOf(true) }
                var localUrl by rememberSaveable { mutableStateOf<String?>(null) }
                var firebaseUrl by rememberSaveable { mutableStateOf<String?>(null) }
                val utilsDataStore = remember { UtilsDataStore(this@MainActivity) }
                val remoteConfigDataStore = remember(::RemoteConfigDataStore)
                val permisions = rememberMultiplePermissionsState(permissions = listOf(WRITE_EXTERNAL_STORAGE, CAMERA))

                val startDestination = if(loading && checkInternet)
                    "loading_screen"
                else if (!checkInternet || (firebaseUrl == null && localUrl == null))
                    "no_internet_screen"
                else if(localUrl != null)
                    "web_screen"
                else if(firebaseUrl == "" || checkIsEmu())
                    "main_screen"
                else {
                    utilsDataStore.saveUrl(firebaseUrl)
                    "web_screen"
                }

                connectionInternet.observe(this){
                    checkInternet = it
                }

                LaunchedEffect(key1 = Unit, block = {
                    localUrl = utilsDataStore.getUrl()
                    if(localUrl == null){
                        firebaseUrl = remoteConfigDataStore.getUrl()
                    }
                    loading = false
                })

                LaunchedEffect(key1 = Unit, block = {
                    permisions.launchMultiplePermissionRequest()
                })

//                LaunchedEffect(key1 = checkInternet, block = {
//                    if(checkInternet && localUrl == null && firebaseUrl == null){
//                        firebaseUrl = remoteConfigDataStore.getUrl()
//                    }
//                })

                NavHost(
                    navController = navController,
                    startDestination = startDestination,
                    builder = {
                        composable("loading_screen"){
                            LoadingScreen()
                        }
                        composable("no_internet_screen"){
                            NoInternetConnectionScreen()
                        }
                        composable("main_screen"){
                            MainScreen(navController = navController)
                        }
                        composable("web_screen") {
                            WebViewScreen(
                                url = localUrl ?: firebaseUrl ?: "",
                                chromeClient = remember {
                                    ChromeClient(
                                        activity = this@MainActivity,
                                        mCameraPhotoPath = mCameraPhotoPath,
                                        mFilePathCallback = mFilePathCallback,
                                        mUploadMessage = mUploadMessage,
                                        mCapturedImageURI = mCapturedImageURI
                                    )
                                }
                            )
                        }
                        composable(
                            route = "news_more/{id}",
                            arguments = listOf(
                                navArgument("id"){
                                    type = NavType.StringType
                                }
                            )
                        ){
                            NewsMoreScreen(
                                id = it.arguments!!.getString("id")!!
                            )
                        }
                    }
                )
            }
        }
    }

    class ChromeClient(
        private val activity: Activity,
        private val mCameraPhotoPath: MutableState<String?>,
        private val mFilePathCallback: MutableState<ValueCallback<Array<Uri>>?>,
        private val mUploadMessage: MutableState<ValueCallback<Uri?>?>,
        private val mCapturedImageURI: MutableState<Uri?>
    ): AccompanistWebChromeClient() {
        // For Android 5.0
        @SuppressLint("QueryPermissionsNeeded")
        override fun onShowFileChooser(
            view: WebView,
            filePath: ValueCallback<Array<Uri>>,
            fileChooserParams: FileChooserParams
        ): Boolean {
            // Double check that we don't have any existing callbacks
            if (mFilePathCallback.value != null) {
                mFilePathCallback.value!!.onReceiveValue(null)
            }
            mFilePathCallback.value = filePath
            var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent!!.resolveActivity(this.activity.packageManager) != null) {
                // Create the File where the photo should go
                var photoFile: File? = null
                try {
                    photoFile = createImageFile()
                    takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath.value)
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Log.e("ErrorCreatingFile", "Unable to create Image File", ex)
                }

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    mCameraPhotoPath.value = "file:" + photoFile.absolutePath
                    takePictureIntent.putExtra(
                        MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile)
                    )
                } else {
                    takePictureIntent = null
                }
            }
            val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
            contentSelectionIntent.type = "image/*"
            val intentArray: Array<Intent?> = takePictureIntent?.let { arrayOf(it) } ?: arrayOfNulls(0)
            val chooserIntent = Intent(Intent.ACTION_CHOOSER)
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
            activity.startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE)
            return true
        }

        // openFileChooser for Android 3.0+
        // openFileChooser for Android < 3.0
        fun openFileChooser(uploadMsg: ValueCallback<Uri?>?, acceptType: String? = "") {
            mUploadMessage.value = uploadMsg
            // Create AndroidExampleFolder at sdcard
            val imageStorageDir = File(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES
                ), "AndroidExampleFolder"
            )
            if (!imageStorageDir.exists()) {
                imageStorageDir.mkdirs()
            }

            // Create camera captured image file path and name
            val file = File(
                imageStorageDir.toString() + File.separator + "IMG_"
                        + System.currentTimeMillis().toString() + ".jpg"
            )
            mCapturedImageURI.value = Uri.fromFile(file)

            // Camera capture image intent
            val captureIntent = Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
            )
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI.value)
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.addCategory(Intent.CATEGORY_OPENABLE)
            i.type = "image/*"

            // Create file chooser intent
            val chooserIntent = Intent.createChooser(i, "Image Chooser")

            // Set camera intent to file chooser
            chooserIntent.putExtra(
                Intent.EXTRA_INITIAL_INTENTS, arrayOf<Parcelable>(captureIntent)
            )

            // On select image call onActivityResult method of activity
            activity.startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE)
        }

        //openFileChooser for other Android versions
        fun openFileChooser(
            uploadMsg: ValueCallback<Uri?>?,
            acceptType: String?,
            capture: String?
        ) {
            openFileChooser(uploadMsg, acceptType)
        }

        @SuppressLint("SimpleDateFormat")
        fun createImageFile(): File {
            // Create an image file name
            val timeStamp =
                SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val imageFileName = "JPEG_" + timeStamp + "_"
            val storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            )
            return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir /* directory */
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback.value == null) {
                super.onActivityResult(requestCode, resultCode, data)
                return
            }
            var results: Array<Uri>? = null

            // Check that the response is a good one
            if (resultCode == AppCompatActivity.RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath.value != null) {
                        results = arrayOf(Uri.parse(mCameraPhotoPath.value))
                    }
                } else {
                    val dataString = data.dataString
                    if (dataString != null) {
                        results = arrayOf(Uri.parse(dataString))
                    }
                }
            }
            mFilePathCallback.value!!.onReceiveValue(results)
            mFilePathCallback.value = null
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage.value == null) {
                super.onActivityResult(requestCode, resultCode, data)
                return
            }
            if (requestCode == FILECHOOSER_RESULTCODE) {
                if (null == mUploadMessage.value) {
                    return
                }
                var result: Uri? = null
                try {
                    result = if (resultCode != AppCompatActivity.RESULT_OK) {
                        null
                    } else {

                        // retrieve from the private variable if the intent is null
                        if (data == null) mCapturedImageURI.value else data.data
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        applicationContext, "activity :$e",
                        Toast.LENGTH_LONG
                    ).show()
                }
                mUploadMessage.value!!.onReceiveValue(result)
                mUploadMessage.value = null
            }
        }
        return
    }
}