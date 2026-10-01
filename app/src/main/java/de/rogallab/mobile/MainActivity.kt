package de.rogallab.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import de.rogallab.mobile.shared.domain.utilities.Alog
import de.rogallab.mobile.ui.theme.MobileTheme

class MainActivity : ComponentActivity() {

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)

      // Set up logging configuration for the application.
      Alog.set(
         useAndroidLog = true,
         isVerbose = true,
         isDebug = true,
         isInfo = true,
         isComp = true
      )
      Alog.d(TAG, "onCreate()")

      // Enable edge-to-edge display for the activity.
      enableEdgeToEdge()

      // Set the content of the activity to a Composable function.
      setContent {
         MobileTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
               Greeting(
                  name = "Android",
                  modifier = Modifier.padding(innerPadding)
               )
            }
         }
      }
   }

   companion object {
      private const val TAG = "<-MainActivity"
   }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
   Text(
      text = "Hello $name!",
      modifier = modifier
   )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
   MobileTheme {
      Greeting("Android")
   }
}