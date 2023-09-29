import zio.ZIOAppDefault
import zio.ZIO
import zio._
import zio.Console._
import sttp.client3._
import sttp.client3.httpclient.zio.HttpClientZioBackend
import sttp.client3.SttpBackend

object Cli extends ZIOAppDefault {
  def run = program.provide(
    SttpClient.layer,
    HttpClientZioBackend.layer(),
)

  def chatRequest(message: String) = basicRequest
    .body(message)
    .post(uri"localhost:8080/chat")

  val sendMessage = for {
    _ <- printLine("User: ")
    message <- readLine
    _ <- printLine("Bot: ")
    response <- Client.chatRequest(message)
    responseMessage <- ZIO.fromEither(response.body)
    _ <- printLine(responseMessage)
  } yield ()

  val program = sendMessage.forever
}

trait Client {
    def chatRequest(message: String): Task[Response[Either[String, String]]]
}

object Client {
    def chatRequest(message: String) = ZIO.serviceWithZIO[Client](_.chatRequest(message))
}

case class SttpClient(sttpBackend: SttpBackend[Task, Any]) extends Client {
    def chatRequest(message: String) = basicRequest
        .body(message)
        .post(uri"http://localhost:8080/chat")
        .send(sttpBackend)
}

object SttpClient {
    val layer: ZLayer[SttpBackend[Task, Any], Any, Client] = ZLayer.fromFunction(SttpClient(_))
}
