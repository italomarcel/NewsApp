package com.company.newsapp

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.company.newsapp.auth.BiometricAuthHelper
import com.company.newsapp.ui.ArticleDetailScreen
import com.company.newsapp.ui.HeadlinesScreen
import com.google.gson.Gson
import java.net.URLDecoder
import java.net.URLEncoder

@Composable
@Preview
fun App() {
    MaterialTheme {
        val context = LocalContext.current
        var isAppAuthenticated by remember { mutableStateOf(false) }
        var authError by remember { mutableStateOf<String?>(null) }
        var biometricHelper by remember { mutableStateOf<BiometricAuthHelper?>(null) }

        LaunchedEffect(context) {
            if (context is FragmentActivity) {
                biometricHelper = BiometricAuthHelper(context)
            }
        }

        if (!isAppAuthenticated) {
            AppAuthScreen(
                biometricHelper = biometricHelper,
                authError = authError,
                onAuthSuccess = { isAppAuthenticated = true },
                onAuthError = { authError = it }
            )
            return@MaterialTheme
        }

        val navController = rememberNavController()
        val gson = remember { Gson() }

        NavHost(
            navController = navController,
            startDestination = "headlines"
        ) {
            composable("headlines") {
                HeadlinesScreen(
                    onArticleClick = { article ->
                        val articleJson = gson.toJson(article)
                        val encodedJson = URLEncoder.encode(articleJson, "UTF-8")
                        navController.navigate("article/$encodedJson")
                    }
                )
            }

            composable("article/{articleData}") { backStackEntry ->
                val encodedArticleData = backStackEntry.arguments?.getString("articleData") ?: ""
                val article = try {
                    val decodedJson = URLDecoder.decode(encodedArticleData, "UTF-8")
                    gson.fromJson(decodedJson, com.company.newsapp.models.Article::class.java)
                } catch (e: Exception) {
                    null
                }
                ArticleDetailScreen(
                    article = article,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppAuthScreen(
    biometricHelper: BiometricAuthHelper?,
    authError: String?,
    onAuthSuccess: () -> Unit,
    onAuthError: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "NewsApp",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Security",
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Secure Access Required",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Please authenticate to access the news app.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = {
                    biometricHelper?.let { helper ->
                        if (helper.canUseBiometricAuth()) {
                            helper.authenticateUser(
                                onSuccess = onAuthSuccess,
                                onError = onAuthError
                            )
                        } else {
                            onAuthError("Biometric authentication not available")
                        }
                    } ?: onAuthError("Authentication not available")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Authenticate")
            }
            
            authError?.let { error ->
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = error,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}
