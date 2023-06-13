package example.imageviewer

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.CoroutineDispatcher
import org.jetbrains.skia.Image

expect fun Modifier.notchPadding(): Modifier

expect suspend fun loadPicture(url: String): ImageBitmap
expect suspend fun readFile(path: String): ByteArray

expect class PlatformStorableImage

expect fun createUUID(): String

expect val ioDispatcher: CoroutineDispatcher

expect val isShareFeatureSupported: Boolean

expect fun ImageBitmap.toByteArray(): ByteArray

expect val shareIcon: ImageVector
/*
override fun onCreate(savedInstanceState: Bundle?) {

}*/