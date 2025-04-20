package jsonpostarchiver

import cats.effect.*
import cats.syntax.all.*
import jsonpostarchiver.model.Post
import jsonpostarchiver.service.*
import org.http4s.*
import org.http4s.ember.client.*
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jFactory
import pureconfig.*
import pureconfig.module.catseffect.syntax.*

import config.AppConfig

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    EmberClientBuilder.default[IO].build.use { client =>
      given Logger[IO] = Slf4jFactory.create[IO].getLogger
      val program: IO[Unit] = for {
        _                <- Logger[IO].info("Starting application")
        config           <- ConfigSource.default.loadF[IO, AppConfig]()
        postFetchService <- PostFetchService.make(client, config)
        fileWriteService <- FileWriteService.make(config)
        posts            <- postFetchService.fetchAllPosts()
        _                <- posts.traverse(fileWriteService.writePost)
        _                <- Logger[IO].info("Job completed successfully. All posts have been fetched and saved.")
      } yield ()

      program
        .as(ExitCode.Success)
        .handleErrorWith { err =>
          Logger[IO].error(err)(s"Application failed: ${err.getMessage}").as(ExitCode.Error)
        }
    }
  }
}
