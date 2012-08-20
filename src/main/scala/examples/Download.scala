package examples

import unfiltered.request.{Path, GET}
import unfiltered.response.{ResponseHeader, ResponseStreamer}
import java.io.OutputStream

object Download extends App {

  val download = unfiltered.filter.Planify {
    case GET(Path("/download")) =>
      ResponseHeader("Content-Disposition", List("attachment; filename=\"download.pdf\"")) ~> new ResponseStreamer {
        def stream(os: OutputStream) {
          val is = getClass.getResourceAsStream("/download.this")
          val buffer = new Array[Byte](1024 * 4)
          Stream.continually(is.read(buffer)).takeWhile(-1 !=).foreach(_ => os.write(buffer))
        }
      }

  }

  unfiltered.jetty.Http(8080).plan(download).run()
}
