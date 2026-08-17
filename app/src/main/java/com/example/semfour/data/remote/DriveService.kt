package com.example.semfour.data.remote

import android.util.Log
import kotlinx.serialization.Serializable
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Servicio de sincronización con Google Drive API v3.
 *
 * Usa la carpeta especial [appDataFolder] de Drive — es privada para la app,
 * invisible para el usuario en la interfaz de Drive, y se elimina automáticamente
 * si el usuario desinstala la app.
 */
@Singleton
class DriveService @Inject constructor() {

    companion object {
        const val BACKUP_FILENAME = "semfour_backup.json"
        const val DRIVE_API_BASE = "https://www.googleapis.com/drive/v3"
        const val DRIVE_UPLOAD_BASE = "https://www.googleapis.com/upload/drive/v3"
        const val APP_DATA_FOLDER = "appDataFolder"
        private const val TAG = "DriveService"
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    /**
     * Sube (o actualiza) el backup JSON a Google Drive appDataFolder.
     *
     * @param accessToken Token de acceso OAuth 2.0 con scope drive.appdata
     * @param jsonContent Contenido completo del backup serializado
     * @return [DriveResult.Success] con el ID del archivo en Drive, o [DriveResult.Error]
     */
    suspend fun uploadBackup(accessToken: String, jsonContent: String): DriveResult {
        return try {
            val existingFileId = findBackupFileId(accessToken)

            val metadataJson = if (existingFileId == null) {
                """{"name":"$BACKUP_FILENAME","parents":["$APP_DATA_FOLDER"]}"""
            } else {
                """{"name":"$BACKUP_FILENAME"}"""
            }

            val metadataPart = metadataJson.toRequestBody("application/json; charset=UTF-8".toMediaType())
            val contentPart = jsonContent.toRequestBody("application/json".toMediaType())

            val multipart = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("metadata", null, metadataPart)
                .addFormDataPart("file", BACKUP_FILENAME, contentPart)
                .build()

            val url = if (existingFileId == null) {
                "$DRIVE_UPLOAD_BASE/files?uploadType=multipart&fields=id"
            } else {
                "$DRIVE_UPLOAD_BASE/files/$existingFileId?uploadType=multipart&fields=id"
            }

            val request = Request.Builder()
                .url(url)
                .header("Authorization", "Bearer $accessToken")
                .apply {
                    if (existingFileId == null) post(multipart) else patch(multipart)
                }
                .build()

            val response = client.newCall(request).execute()
            val bodyString = response.body?.string() ?: ""

            if (response.isSuccessful) {
                val fileId = JSONObject(bodyString).optString("id", "")
                DriveResult.Success(fileId)
            } else {
                val detailedMessage = parseGoogleApiError(response.code, bodyString)
                Log.e(TAG, "Drive upload failed: $detailedMessage")
                DriveResult.Error(detailedMessage)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Drive upload exception", e)
            DriveResult.Error("Excepción al subir a Drive: ${e.message}")
        }
    }

    /**
     * Descarga el backup JSON desde Google Drive appDataFolder.
     *
     * @param accessToken Token de acceso OAuth 2.0
     * @return [DriveResult.Success] con el JSON, o [DriveResult.Error] / [DriveResult.NotFound]
     */
    suspend fun downloadBackup(accessToken: String): DriveResult {
        return try {
            val fileId = findBackupFileId(accessToken)
                ?: return DriveResult.NotFound

            val request = Request.Builder()
                .url("$DRIVE_API_BASE/files/$fileId?alt=media")
                .header("Authorization", "Bearer $accessToken")
                .get()
                .build()

            val response = client.newCall(request).execute()
            val bodyString = response.body?.string() ?: ""

            if (response.isSuccessful) {
                DriveResult.Success(bodyString)
            } else {
                val detailedMessage = parseGoogleApiError(response.code, bodyString)
                Log.e(TAG, "Drive download failed: $detailedMessage")
                DriveResult.Error(detailedMessage)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Drive download exception", e)
            DriveResult.Error("Excepción al descargar de Drive: ${e.message}")
        }
    }

    /** Busca el archivo de backup en appDataFolder y devuelve su ID si existe */
    private fun findBackupFileId(accessToken: String): String? {
        return try {
            val url = "$DRIVE_API_BASE/files?spaces=$APP_DATA_FOLDER" +
                    "&q=name='$BACKUP_FILENAME'&fields=files(id,name,modifiedTime)"
            val request = Request.Builder()
                .url(url)
                .header("Authorization", "Bearer $accessToken")
                .get()
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) return null

            val body = response.body?.string() ?: return null
            val files = JSONObject(body).optJSONArray("files") ?: return null
            if (files.length() > 0) files.getJSONObject(0).optString("id") else null
        } catch (e: Exception) {
            Log.w(TAG, "Error buscando archivo previo en Drive", e)
            null
        }
    }

    /** Extrae el mensaje descriptivo exacto del JSON de error de Google */
    private fun parseGoogleApiError(code: Int, body: String): String {
        return try {
            val json = JSONObject(body)
            val errorObj = json.optJSONObject("error")
            val message = errorObj?.optString("message")
            if (!message.isNullOrBlank()) {
                "Error $code: $message"
            } else {
                "Error $code (${body.take(120)})"
            }
        } catch (_: Exception) {
            "Error $code: $body"
        }
    }
}

// ── Resultados ────────────────────────────────────────────────────────────────

sealed class DriveResult {
    data class Success(val data: String) : DriveResult()
    data class Error(val message: String) : DriveResult()
    data object NotFound : DriveResult()
}

@Serializable
data class DriveBackup(
    val version: Int = 1,
    val exportedAt: Long = System.currentTimeMillis(),
    val packageName: String = "com.example.semfour"
)
