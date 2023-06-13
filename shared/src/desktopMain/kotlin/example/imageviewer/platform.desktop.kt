package example.imageviewer

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.utils.io.core.use
import kotlinx.coroutines.Dispatchers
import org.jetbrains.skia.Image
import java.io.File
import java.util.UUID

actual fun Modifier.notchPadding(): Modifier = Modifier.padding(top = 12.dp)

class DesktopStorableImage(
    val imageBitmap: ImageBitmap
)

actual suspend fun loadPicture(url: String): ImageBitmap {
    try {
        val httpClient = HttpClient()
        val image: ByteArray = httpClient.use { client ->
            client.get(url).body()
        }
        return Image.makeFromEncoded(image).toComposeImageBitmap()
    } catch (e: Exception) {
        println("ERROR : cannot load picture $url")
        e.printStackTrace()
        return ImageBitmap(1, 1)
    }
}

actual suspend fun readFile(path: String): ByteArray {
    return File(path).readBytes()
}

actual typealias PlatformStorableImage = DesktopStorableImage

actual fun ImageBitmap.toByteArray(): ByteArray {
    val r = this.asSkiaBitmap().readPixels()
    return r ?: ByteArray(0)
}

actual fun createUUID(): String = UUID.randomUUID().toString()

actual val ioDispatcher = Dispatchers.IO

actual val isShareFeatureSupported: Boolean = false

actual val shareIcon: ImageVector = Icons.Filled.Share
