import zio.ZIOAppDefault
import zio.ZIO
import zio.Console._
import dev.maxmelnyk.openaiscala.client.OpenAIClient
import sttp.client3.httpclient.zio.HttpClientZioBackend
import zio.interop.catz._
import dev.maxmelnyk.openaiscala.models.text.completions.chat.ChatCompletion.Message
import dev.maxmelnyk.openaiscala.models.text.completions.chat.ChatCompletion.Message.Role
import zio.http._
import zio.Scope

object App extends ZIOAppDefault {
  def run = program

  val openAIClient = for {
    config <- Config.get
    backend <- HttpClientZioBackend()
  } yield OpenAIClient(config.apiKey, None)(backend)

  val messages = Seq(
    Message(
      Role.User,
      "Hello, how are you?"
    )
  )

  val sendMessages =
    for {
      client <- openAIClient
      response <- client.createChatCompletion(messages)
      message <- ZIO.fromEither(response.choices.headOption.map(_.message.content).toRight(new Exception("No Messages")))
    } yield message

  val app: HttpApp[Any, Throwable] =
    Http.collectZIO[Request] {
      case Method.POST -> Root / "chat" => sendMessages.map(Response.text(_))
    }

  val server = app.catchAllZIO {
    case e: Throwable => ZIO.succeed(Response.fromHttpError(HttpError.InternalServerError(e.getMessage)))
  }

  val program = Server.serve(server).provide(Server.default)
}