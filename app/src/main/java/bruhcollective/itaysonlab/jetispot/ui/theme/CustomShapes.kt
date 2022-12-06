package bruhcollective.itaysonlab.jetispot.ui.theme

import android.graphics.Matrix
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class CustomShapes {
  val BlobShape: Shape = object: Shape {
    override fun createOutline(
      size: Size,
      layoutDirection: LayoutDirection,
      density: Density
    ): Outline {
      val baseWidth = 184f
      val baseHeight = 184f

      val path = Path().apply {
        moveTo(92f, 12f)
        cubicTo(80.568f, 12f, 69.9033f, 6.6281f, 59.1725f, 2.686f)
        cubicTo(54.4428f, 0.9485f, 49.3322f, 0f, 44f, 0f)
        cubicTo(19.6995f, 0f, 0f, 19.6995f, 0f, 44f)
        cubicTo(0f, 49.3322f, 0.9485f, 54.4428f, 2.686f, 59.1725f)
        cubicTo(6.6281f, 69.9033f, 12f, 80.568f, 12f, 92f)
        cubicTo(12f, 103.432f, 6.6281f, 114.0967f, 2.686f, 124.8275f)
        cubicTo(0.9485f, 129.5572f, 0f, 134.6678f, 0f, 140f)
        cubicTo(0f, 164.3005f, 19.6995f, 184f, 44f, 184f)
        cubicTo(49.3322f, 184f, 54.4428f, 183.0515f, 59.1724f, 181.314f)
        cubicTo(69.9033f, 177.372f, 80.568f, 172f, 92f, 172f)
        cubicTo(103.432f, 172f, 114.0967f, 177.372f, 124.8276f, 181.314f)
        cubicTo(129.5572f, 183.0515f, 134.6678f, 184f, 140f, 184f)
        cubicTo(164.3005f, 184f, 184f, 164.3005f, 184f, 140f)
        cubicTo(184f, 134.6678f, 183.0515f, 129.5572f, 181.314f, 124.8276f)
        cubicTo(177.372f, 114.0967f, 172f, 103.432f, 172f, 92f)
        cubicTo(172f, 80.568f, 177.372f, 69.9033f, 181.314f, 59.1724f)
        cubicTo(183.0515f, 54.4428f, 184f, 49.3322f, 184f, 44f)
        cubicTo(184f, 19.6995f, 164.3005f, 0f, 140f, 0f)
        cubicTo(134.6678f, 0f, 129.5572f, 0.9485f, 124.8275f, 2.686f)
        cubicTo(114.0967f, 6.6281f, 103.432f, 12f, 92f, 12f)
        close()
      }
      return Outline.Generic(
        path
          .asAndroidPath()
          .apply {
            transform(Matrix().apply {
              setScale(size.width / baseWidth, size.height / baseHeight)
            })
          }
          .asComposePath()
      )
    }
  }
}