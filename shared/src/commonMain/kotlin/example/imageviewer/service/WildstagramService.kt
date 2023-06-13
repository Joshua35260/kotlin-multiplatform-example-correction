
package example.imageviewer.service

import androidx.compose.ui.graphics.ImageBitmap
import example.imageviewer.LocalImageProvider
import example.imageviewer.loadPicture
import example.imageviewer.model.PictureData
import example.imageviewer.readFile
import example.imageviewer.toByteArray
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.onUpload
import io.ktor.client.plugins.timeout
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeFully
import kotlinx.serialization.json.Json

class WildstagramService {
    private val url = "https://wildstagram.nausicaa.wilders.dev"
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 10_000
        }
    }


    suspend fun getAll(): List<PictureData.WildstagramPicture> {
        val pictureNames: List<String> = httpClient.get(url + "/list") {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token}")
            }
        }.body()
        println("pictureNames =>  " + pictureNames)

        return pictureNames.map { pictureName -> PictureData.WildstagramPicture(pictureName, "Wilder image: $pictureName", url + "/" + pictureName) }
    }

    val token = "bfd28f71-a745-4a48-9d59-248498b39dfb"
    suspend fun pushImage(imageBitmap: ImageBitmap) {
        println("PokemonService: will POST  on " + url)

        try {
            val bytes:ByteArray = imageBitmap.toByteArray();
        // val bytes:ByteArray = readFile("/tmp/my.jpg")

        println("SEND UPLOAD")
        val response: HttpResponse = httpClient.post(url + "/upload") {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token}")
            }
            //timeout ( 1000 )
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("name", "louis")
                        append("fileData", bytes, Headers.build {
                            append(HttpHeaders.ContentType, "image/jpeg")
                            append(HttpHeaders.ContentDisposition, "filename=\"louis.jpg\"")
                            append(HttpHeaders.ContentLength, bytes.size)
                        })
                    },
                    boundary = "WCSWebAppBoundary"
                )
            )
            onUpload { bytesSentTotal, contentLength ->
                println("Sent $bytesSentTotal bytes from $contentLength")
            }
        }

        println("SENT UPLOAD - CODE=" + response.status)
        } catch (e: Exception) {
            println("EXCEPTION " + e)
            e.printStackTrace()
        }

    }
}