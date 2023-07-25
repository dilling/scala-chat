import zio.ZIOAppDefault
import zio.ZIO
import zio.Console._

object App extends ZIOAppDefault:
  def run = program

  val program = 
    for {
      _ <- printLine("Hello, world!")
    } yield ()