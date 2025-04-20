package jsonpostarchiver.service

import cats.effect.IO
import cats.syntax.all.*
import io.circe.syntax._
import fs2.*
import fs2.io.file.{Files, Path}
import jsonpostarchiver.config.AppConfig
import jsonpostarchiver.model.Post
import org.typelevel.log4cats.Logger

trait FileWriteService {
  def writePost(post: Post): IO[Unit]
}

object FileWriteService {
  def make(config: AppConfig)(using Logger[IO]): IO[FileWriteService] = new FileWriteService {
    override def writePost(post: Post): IO[Unit] =
      val filePath: Path = Path(config.filesTargetPath)
      val fileName       = s"${post.id}.json"
      val path           = filePath.resolve(fileName)

      for {
        _ <- Files[IO].createDirectories(filePath)
        _ <- Stream
               .emit(post.asJson.spaces2)
               .through(Files[IO].writeUtf8(path))
               .compile
               .drain
               .flatTap(_ => Logger[IO].info(s"[FileWriteService] Wrote post ${post.id} to $path"))
               .handleErrorWith { err =>
                 Logger[IO].error(s"[FileWriteService] Failed to write post ${post.id}")
                 IO.raiseError(err)
               }
      } yield ()
  }.pure[IO]
}
