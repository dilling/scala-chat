import zio.config.ConfigSource
import zio.config._, ConfigDescriptor._, ConfigSource._
import zio.config.magnolia.descriptor

case class Config(apiKey: String)

object Config:
    private val desc = (string("OPENAI_API_KEY")).to[Config]
    private val source = ConfigSource.fromSystemEnv(None, None)

    val get = read(desc from source)
