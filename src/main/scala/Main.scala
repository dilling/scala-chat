import zio.ZIOAppDefault
import zio.ZIO
import zio.Console._
import dev.maxmelnyk.openaiscala.client.OpenAIClient
import sttp.client3.httpclient.zio.HttpClientZioBackend
import zio.interop.catz._
import dev.maxmelnyk.openaiscala.models.text.completions.chat.ChatCompletion.Message
import dev.maxmelnyk.openaiscala.models.text.completions.chat.ChatCompletion.Message.Role

object App extends ZIOAppDefault:
  def run = program

  val openAIClient = for {
    config <- Config.get
    backend <- HttpClientZioBackend.scoped()
  } yield OpenAIClient(config.apiKey, None)(backend)

  val messages = Seq(
    Message(
      Role.User,
      "Hello, how are you?"
    )
  )

  val program =
    for {
      client <- openAIClient
      response <- client.createChatCompletion(messages)
      message <- ZIO.fromOption(response.choices.headOption.map(_.message.content))
      _ <- printLine(message)
    } yield ()
