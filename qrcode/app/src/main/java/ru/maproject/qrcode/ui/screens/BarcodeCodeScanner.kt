package ru.maproject.qrcode.ui.screens

import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.camera.core.CameraSelector
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.mlkit.vision.barcode.common.Barcode
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Workbook
import org.joda.time.Period
import ru.maproject.qrcode.common.openBrowser
import ru.maproject.qrcode.data.qrCode.qrCodeApi
import ru.maproject.qrcode.data.user.UserDataStore
import ru.maproject.qrcode.data.user.model.User
import ru.maproject.qrcode.navigation.Screen
import ru.maproject.qrcode.ui.theme.tintColor
import ru.maproject.qrcode.ui.view.camera.BarcodeScanner
import ru.maproject.qrcode.ui.view.camera.CameraControlsView
import ru.maproject.qrcode.ui.view.camera.switchLens
import ru.maproject.qrcode.utils.generateBarCode.generateBarCode
import ru.maproject.qrcode.yandexAds.InterstitialYandexAds
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Date


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BarcodeCodeScanner(
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val permissions = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.CAMERA,
            WRITE_EXTERNAL_STORAGE
        )
    )
    var barcodesList by remember { mutableStateOf(listOf<Barcode>()) }
    var loadingSaveFile by remember { mutableStateOf(false) }
    var qrCodeApi = remember { qrCodeApi }

    val userDataStore = remember(::UserDataStore)
    var user by remember { mutableStateOf<User?>(null) }

    val interstitialYandexAds = remember {
        InterstitialYandexAds(context, onDismissed = { adClickedDate, returnedToDate ->
            user ?: return@InterstitialYandexAds

            if(adClickedDate != null && returnedToDate != null){
                if((Period(adClickedDate, returnedToDate)).seconds >= 10){
                    userDataStore.updateCountInterstitialAdsClick(user!!.countInterstitialAdsClick + 1)
                }else {
                    userDataStore.updateCountInterstitialAds(user!!.countInterstitialAds + 1)
                }
            } else {
                userDataStore.updateCountInterstitialAds(user!!.countInterstitialAds + 1)
            }
        })
    }

    LaunchedEffect(key1 = Unit, block = {
        permissions.launchMultiplePermissionRequest()
        userDataStore.get { user = it }
    })

    BackHandler {
        if(barcodesList.isNotEmpty())
            barcodesList = emptyList()
        else
            navController.navigateUp()
    }

    if(barcodesList.isNotEmpty()){

        val barcode = remember(barcodesList::last)
        val rawValue = barcode.rawValue.toString()
        var qrCodeUrl by remember { mutableStateOf("") }

        LaunchedEffect(key1 = Unit, block = {
            interstitialYandexAds.show()

            qrCodeUrl = qrCodeApi.get(rawValue).url
        })

        if(barcodesList.size == 1){
            Spacer(modifier = Modifier.height(50.dp))
        }

        Card(
            modifier = Modifier.padding(10.dp),
            shape = AbsoluteRoundedCornerShape(15.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {

                Image(
                    painter = rememberAsyncImagePainter(model = qrCodeUrl),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp).padding(5.dp)
                )

                Text(
                    text = rawValue,
                    modifier = Modifier.padding(5.dp),
                    fontWeight = FontWeight.W900
                )

                Button(
                    modifier = Modifier.padding(5.dp),
                    shape = AbsoluteRoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = tintColor
                    ),
                    onClick = {
                        if(qrCodeUrl.isNotEmpty())
                            context.openBrowser(qrCodeUrl)
                    }
                ) {
                    Text(text = "Открыть qrcode")
                }

//                generateBarCode(rawValue)?.let { bitmap ->
//                    Image(
//                        bitmap = bitmap.asImageBitmap(),
//                        contentDescription = null,
//                        modifier = Modifier
//                            .size(400.dp)
//                            .padding(5.dp)
//                    )
//
//                    AnimatedVisibility(visible = loadingSaveFile) {
//                        CircularProgressIndicator(
//                            modifier = Modifier.padding(5.dp),
//                            color = tintColor
//                        )
//                    }
//
//                    AnimatedVisibility(visible = !loadingSaveFile) {
//                        Button(
//                            shape = AbsoluteRoundedCornerShape(15.dp),
//                            colors = ButtonDefaults.buttonColors(
//                                backgroundColor = tintColor
//                            ),
//                            onClick = {
//                                scope.launch {
//                                    saveQrCodeFile(
//                                        context = context,
//                                        number = rawValue,
//                                        image = bitmap
//                                    )
//                                    loadingSaveFile = true
//                                    delay(2000L)
//                                    loadingSaveFile = false
//                                    Toast.makeText(context, "Успешно !", Toast.LENGTH_SHORT).show()
//                                }
//                            }
//                        ) {
//                            Text(text = "Скачать файл")
//                        }
//                    }
//                }
            }
        }
    }else {
        if (permissions.allPermissionsGranted){
            Box(modifier = Modifier.fillMaxSize()) {
                var lens by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
                BarcodeScanner(
                    cameraLens = lens,
                    barcodeScanner = true
                ){ barcodes, sourceInfo ->
                    barcodes?.let {

                        if(barcodes.isNotEmpty()){
                            barcodesList = it
                        }

                        Canvas(
                            modifier = Modifier.fillMaxSize()
                        ){
                            for (barcode in barcodes){
                                val needToMirror = sourceInfo.isImageFlipped
                                val corners = barcode.cornerPoints

                                corners?.let {
                                    drawPath(
                                        path = Path().apply {
                                            corners.forEachIndexed { index, point ->
                                                if (index == 0) {
                                                    if (needToMirror){
                                                        moveTo(size.width - point.x, point.y.toFloat())
                                                    }else {
                                                        moveTo(point.x.toFloat(), point.y.toFloat())
                                                    }
                                                } else {
                                                    if (needToMirror){
                                                        lineTo(size.width - point.x, point.y.toFloat())
                                                    }else{
                                                        lineTo(point.x.toFloat(), point.y.toFloat())
                                                    }
                                                }
                                            }
                                        },
                                        color = Color.Red,
                                        style = Stroke(5f)
                                    )
                                }
                            }
                        }
                    }
                }

                CameraControlsView(
                    onLensChange = { lens = switchLens(lens) },
                    onLongClick = {
                        navController.navigate(Screen.Main.route)
                    }
                )
            }
        }
    }
}

fun saveQrCodeFile(
    context: Context,
    number: String,
    image: Bitmap
) {
    val workbook = HSSFWorkbook()
    val sheet = workbook.createSheet("main")

    sheet.createRow(5)
        .createCell(0)
        .setCellValue(number)

    val stream = ByteArrayOutputStream()
    image.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    val imageByteArray = stream.toByteArray()

    val pictureIdx: Int = workbook.addPicture(imageByteArray, Workbook.PICTURE_TYPE_PNG)

    val drawing = sheet.createDrawingPatriarch()
    val helper = workbook.creationHelper
    val anchor = helper.createClientAnchor()
    anchor.setCol1(1)
    anchor.row1 = 1

    val picture = drawing.createPicture(anchor, pictureIdx)
//    picture.resize()

    val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path

    val excelFile = File("$path/${Date().time}.xls")
    excelFile.createNewFile()
    val ops = FileOutputStream(excelFile)
    workbook.write(ops)
    ops.close()
}