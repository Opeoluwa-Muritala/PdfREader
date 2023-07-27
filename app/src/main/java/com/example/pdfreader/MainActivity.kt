package com.example.pdfreader


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import android.graphics.Paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.pdfreader.ui.theme.PDFReaderTheme
import android.graphics.*
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.*
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

private const val STORAGE_PERMISSIONS = 101

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PDFReaderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
        storagePermissions()
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun storagePermissions() {
        if (
            hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ) {
            Toast.makeText(this,"Permission Denied...",Toast.LENGTH_LONG).show()

        } else {
            Toast.makeText(this, "Permission Granted...",Toast.LENGTH_SHORT).show()
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSIONS)
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (
            requestCode == STORAGE_PERMISSIONS &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {

        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val filename = "doc ${System.currentTimeMillis()}.pdf"
    //val pdfactivityIntent = Intent(context, PdfActivity::class.java)
    val filecontent = "This is a blank Pdf"
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.CreateDocument()){
            uri: Uri? ->
        if (uri != null){
            savefiletodocuments(context,uri,filecontent)
        }
    }
    val  activity = LocalContext.current as Activity



    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.Cyan)
            .fillMaxSize()
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(red = 63, green = 81, blue = 181, alpha = 255),
                disabledContainerColor = Color.Gray
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 30.dp),
            modifier = Modifier
                .size(200.dp, 250.dp)
                .clickable {
                    /*val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "application/pdf"
                        putExtra(Intent.EXTRA_TITLE,filename)
                    }
                    //context.startActivity(pdfactivityIntent)*/
                    saveFileToDocumentsAndShare(
                        context, "This is A Blank Test Pdf File"
                    )
                    Toast
                        .makeText(context, "It Works", Toast.LENGTH_LONG)
                        .show()
                   // activity.startActivityForResult(intent, 1)
                }
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Cyan
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 150.dp),
                    shape = CircleShape
                ) {
            Image(
                painter = painterResource(id = R.drawable.images),
                contentDescription = null,
                alignment = Alignment.TopCenter,
                modifier = Modifier
                    .size(100.dp),
                contentScale = ContentScale.FillBounds
            )}
                Spacer(modifier = Modifier.height(20.dp))
            Text(
                "Create Pdf",
                fontSize = 30.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = Color.White
            )
            }
        }
    }
}




fun savefiletodocuments(context: Context,uri: Uri,content: String){
    try {
        val outputStream = context.contentResolver.openOutputStream(uri)
        outputStream?.use {stream ->
        stream.write(content.toByteArray())
        }
        Toast.makeText(context,"File Saved Successfully!", Toast.LENGTH_SHORT).show()

    } catch (e: Exception){
        Toast.makeText(context,"Error Saving file: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

fun Context.hasPermission(permission: String): Boolean =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun saveFileToDocumentsAndShare(context: Context, content: String) {
    try {
        // Create a new PDF document
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size in points (1 point = 1/72 inch)
        val page = pdfDocument.startPage(pageInfo)

        // Create a canvas to draw the text
        val canvas = page.canvas
        val paint = Paint()
        paint.color = android.graphics.Color.BLACK
        paint.textSize = 24f

        // Adjust text positioning
        val x = 50f
        val y = 100f

        // Draw the text on the page
        canvas.drawText(content, x, y, paint)

        pdfDocument.finishPage(page)

        // Save the PDF to a temporary file
        val pdfFile = File(context.cacheDir, "Blank.pdf")
        val outputStream = FileOutputStream(pdfFile)
        pdfDocument.writeTo(outputStream)

        pdfDocument.close()

        // Share the PDF using ACTION_SEND intent
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", pdfFile)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "application/pdf"
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        context.startActivity(Intent.createChooser(shareIntent, "Share PDF"))

        // Optionally notify the user about the successful save.
        Toast.makeText(context, "File saved and shared successfully!", Toast.LENGTH_SHORT).show()
    } catch (e: IOException) {
        // Handle any errors that may occur during the save process.
        Toast.makeText(context, "Error saving and sharing file: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@Preview(showBackground = true)
@Composable
fun MainPreview() {
    PDFReaderTheme {
        MainScreen()
    }
}

