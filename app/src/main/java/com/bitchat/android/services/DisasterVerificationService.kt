package com.bitchat.android.services

import android.content.Context
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.util.concurrent.TimeUnit

class DisasterVerificationService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // URL for the backend service.
    // 10.0.2.2 is the special alias to your host loopback interface (i.e., 127.0.0.1 on your development machine)
    private val BACKEND_URL = "http://10.0.2.2:5000/verify"

    suspend fun verifyDisaster(context: Context, description: String, imageUriString: String?): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Starting verification for: $description, image: $imageUriString")

                val builder = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("description", description)

                if (imageUriString != null) {
                    try {
                        val uri = Uri.parse(imageUriString)
                        val contentResolver = context.contentResolver
                        // Get MIME type
                        val type = contentResolver.getType(uri) ?: "application/octet-stream"
                        val mediaType = type.toMediaTypeOrNull()

                        // Read bytes
                        val bytes = contentResolver.openInputStream(uri)?.use { it.readBytes() }

                        if (bytes != null) {
                            builder.addFormDataPart(
                                "image",
                                "upload.jpg", // Generic filename since we can't always get real name easily from URI
                                bytes.toRequestBody(mediaType)
                            )
                        } else {
                             Log.w(TAG, "Failed to read image bytes from: $imageUriString")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error reading image uri", e)
                    }
                }

                val requestBody = builder.build()
                val request = Request.Builder()
                    .url(BACKEND_URL)
                    .post(requestBody)
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (response.isSuccessful && responseBody != null) {
                    val json = JSONObject(responseBody)
                    val verified = json.optBoolean("verified", false)
                    Log.d(TAG, "Verification result: $verified")
                    return@withContext verified
                } else {
                    Log.e(TAG, "Verification failed. Code: ${response.code}, Body: $responseBody")
                    return@withContext false
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error calling verification service", e)
                return@withContext false
            }
        }
    }

    companion object {
        private const val TAG = "DisasterService"
    }
}
