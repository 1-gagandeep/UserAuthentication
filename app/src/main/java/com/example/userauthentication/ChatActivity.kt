//package com.example.userauthentication // Replace with your package name
//
//import android.os.Bundle
//import android.webkit.WebView
//import android.webkit.WebViewClient
//import android.widget.Button
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//
//class ChatActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_chat)
//
//        // Initialize WebView
//        val webView = findViewById<WebView>(R.id.webview)
//        webView.settings.javaScriptEnabled = true // Required for tawk.to
//        webView.webViewClient = object : WebViewClient() {
//            override fun onPageFinished(view: WebView?, url: String?) {
//                // Optional: Log when the page is fully loaded
//                super.onPageFinished(view, url)
//            }
//        }
//
//        // tawk.to widget HTML content
//        val htmlContent = """
//            <!DOCTYPE html>
//            <html>
//            <head>
//                <title>Chat</title>
//            </head>
//            <body>
//                <h1>Chat with us!</h1>
//                <script type="text/javascript">
//                  var Tawk_API=Tawk_API||{}, Tawk_LoadStart=new Date();
//                  (function(){
//                    var s1=document.createElement("script"),s0=document.getElementsByTagName("script")[0];
//                    s1.async=true;
//                    s1.src='https://embed.tawk.to/[1gtr9fc12]/default'; // Replace with your ID
//                    s1.charset='UTF-8';
//                    s1.setAttribute('crossorigin','*');
//                    s0.parentNode.insertBefore(s1,s0);
//                    s1.onload = function() {
//                        console.log("tawk.to script loaded");
//                    };
//                  })();
//                </script>
//            </body>
//            </html>
//        """.trimIndent()
//
//        // Load the HTML into WebView
//        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
//
//        // Button to maximize the chat widget
//        val chatButton = findViewById<Button>(R.id.chat_button)
//        chatButton.setOnClickListener {
//            // Check if Tawk_API is ready before calling maximize
//            webView.evaluateJavascript("""
//                if (typeof Tawk_API !== 'undefined' && typeof Tawk_API.maximize === 'function') {
//                    Tawk_API.maximize();
//                } else {
//                    console.log('Tawk_API not ready yet');
//                }
//            """.trimIndent()) { result ->
//                // Optional: Show a toast if the widget isn't ready
//                if (result == null) {
//                    Toast.makeText(this, "Chat is still loading, try again in a moment", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//}


package com.example.userauthentication

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.userauthentication.R

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)

        val webView : WebView = findViewById(R.id.webview)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("https://tawk.to/chat/6436ef8731ebfa0fe7f7f086/1gtr9fc12")

    }
}