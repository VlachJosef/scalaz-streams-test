package cz.vlach

import scalaz.concurrent.Task
import scalaz.stream.io
import scalaz.stream.process1

object Main extends App {

  val converter: Task[Unit] =
    io.linesR("testdata/fahrenheit.txt")
      .filter(s => !s.trim.isEmpty && !s.startsWith("//"))
      .map(line => fahrenheitToCelsius(line.toDouble).toString)
      .intersperse("\n")
      .pipe(process1.utf8Encode)
      .to(io.fileChunkW("testdata/celsius.txt"))
      .run

  converter.run

  def fahrenheitToCelsius(f: Double): Double = (f - 32.0) * (5.0 / 9.0)
}