package com.example.pdfreader

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.pdfreader.ui.theme.PDFReaderTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PdfActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PDFReaderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PdfView()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfView(){
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val  context = LocalContext.current
    val mainActivity = Intent(context,MainActivity::class.java).addFlags(
        FLAG_ACTIVITY_NEW_TASK
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text = "PDF Reader",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 50.dp),
                    color = Color(red = 63, green = 81, blue = 181, alpha = 255)
                )},
                navigationIcon = {
                    IconButton(onClick = {

                        onBackPressedDispatcher?.onBackPressed()
                        context.startActivity(mainActivity)
                        Toast.makeText(context,"Going Back...", Toast.LENGTH_LONG).show()
                    }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
                            tint = Color(red = 63, green = 81, blue = 181, alpha = 255)
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(red = 119, green = 232, blue = 247, alpha = 255)
                )

            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    Toast.makeText(context,"Sharing...", Toast.LENGTH_LONG).show()
                },
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 40.dp),
                shape = CircleShape,
                modifier = Modifier.size(80.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Share, contentDescription = null)
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {


    }

}





@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PDFReaderTheme {
    PdfView()
    }
}