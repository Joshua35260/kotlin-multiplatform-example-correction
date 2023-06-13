package example.imageviewer

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.utils.io.core.use
import kotlinx.coroutines.Dispatchers
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.URL
import java.util.UUID


actual fun Modifier.notchPadding(): Modifier = this.displayCutoutPadding().statusBarsPadding()

class AndroidStorableImage(
    val imageBitmap: ImageBitmap
)

actual typealias PlatformStorableImage = AndroidStorableImage

actual fun createUUID(): String = UUID.randomUUID().toString()

actual val ioDispatcher = Dispatchers.IO

actual val isShareFeatureSupported: Boolean = true

actual val shareIcon: ImageVector = Icons.Filled.Share

actual suspend fun loadPicture(url: String): ImageBitmap {
    try {
        val httpClient = HttpClient()
        val image: ByteArray = httpClient.use { client ->
            client.get(url).body()
        }
        return image.toImageBitmap()
    } catch (e: Exception) {
        println("error: cannot load picture: $url")
        e.printStackTrace()
        return ImageBitmap(1, 1)
    }
}
actual suspend fun readFile(path: String): ByteArray {
    return File(path).readBytes()
}

actual fun ImageBitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    this.asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 90, stream)
    val bytes = stream.toByteArray()
    return bytes
}
