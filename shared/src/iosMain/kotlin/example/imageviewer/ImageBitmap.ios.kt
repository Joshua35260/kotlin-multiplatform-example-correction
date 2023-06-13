package example.imageviewer

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.utils.io.core.use
import org.jetbrains.skia.Image

actual fun ByteArray.toImageBitmap(): ImageBitmap =
    Image.makeFromEncoded(this).toComposeImageBitmap()

actual suspend fun loadPicture(url: String): ImageBitmap {
    val httpClient = HttpClient()
    val image: ByteArray = httpClient.use { client ->
        client.get(url).body()
    }
    return Image.makeFromEncoded(image).toComposeImageBitmap()
}
