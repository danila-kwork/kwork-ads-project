package ru.maproject.qrcode.ui.screens

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.camera.core.CameraSelector
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.common.Barcode
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFCreationHelper
import org.apache.poi.xssf.usermodel.XSSFDrawing
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import ru.maproject.qrcode.ui.view.camera.BarcodeScanner
import ru.maproject.qrcode.ui.view.camera.CameraControlsView
import ru.maproject.qrcode.ui.view.camera.switchLens
import ru.maproject.qrcode.utils.generateBarCode.generateBarCode
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BarcodeCodeScanner(
    navController: NavController
) {
    val context = LocalContext.current

    val cameraPermission = rememberPermissionState(permission = Manifest.permission.CAMERA)
    var barcodesList by remember { mutableStateOf(listOf<Barcode>()) }

    LaunchedEffect(key1 = Unit, block = {
        cameraPermission.launchPermissionRequest()
    })

    BackHandler {
        if(barcodesList.isNotEmpty())
            barcodesList = emptyList()
        else
            navController.navigateUp()
    }

    if(barcodesList.isNotEmpty()){
        LazyColumn {
            items(barcodesList){ barcode ->
                val rawValue = barcode.rawValue.toString()

                if(barcodesList.size == 1){
                    Spacer(modifier = Modifier.height(50.dp))
                }

                Card(
                    modifier = Modifier.padding(10.dp),
                    shape = AbsoluteRoundedCornerShape(15.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = rawValue,
                            modifier = Modifier.padding(5.dp),
                            fontWeight = FontWeight.W900
                        )

                        generateBarCode(rawValue)?.let { bitmap ->
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(400.dp)
                                    .padding(5.dp)
                            )

                            Button(onClick = {
                                saveQrCodeFile(
                                    context = context,
                                    number = rawValue,
                                    image = bitmap
                                )
                            }) {
                                Text(text = "Save")
                            }
                        }
                    }
                }
            }
        }
    }else {
        if (cameraPermission.hasPermission){
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
                    onLensChange = { lens = switchLens(lens) }
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
    val xssfWorkbook = XSSFWorkbook()
    val xssfSheet = xssfWorkbook.createSheet()

    val stream = ByteArrayOutputStream()
    image.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    val imageByteArray = stream.toByteArray()

    val pictureIdx: Int = xssfWorkbook.addPicture(imageByteArray, Workbook.PICTURE_TYPE_PNG)

    val drawing: XSSFDrawing = xssfSheet.createDrawingPatriarch()
    val helper: XSSFCreationHelper = xssfWorkbook.creationHelper
    val anchor = helper.createClientAnchor()
    anchor.setCol1(1)
    anchor.row1 = 1

    val picture = drawing.createPicture(anchor, pictureIdx)
    picture.resize()
    val excelFile = File(context.filesDir, "test.xlsx")

    xssfSheet.createRow(5).createCell(5).setCellValue(number)

    if (!excelFile.exists()) {
        excelFile.createNewFile();
    }else {
        excelFile.delete()
        excelFile.createNewFile()
    }

    val ops = FileOutputStream(excelFile)
    xssfWorkbook.write(ops)
}