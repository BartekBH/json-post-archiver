package jsonpostarchiver.service

import cats.effect.IO
import cats.syntax.all.*
import jsonpostarchiver.config.AppConfig
import jsonpostarchiver.model.Post
import org.http4s.Uri
import org.http4s.circe.CirceEntityCodec.circeEntityDecoder
import org.http4s.client.Client
import org.typelevel.log4cats.Logger

trait PostFetchService {
  def fetchAllPosts(): IO[List[Post]]
}

object PostFetchService {
  def make(client: Client[IO], config: AppConfig)(using Logger[IO]): IO[PostFetchService] = new PostFetchService {
    override def fetchAllPosts(): IO[List[Post]] = {
      client
        .expect[List[Post]](Uri.unsafeFromString(config.getPostsUrl))
        .flatTap { posts =>
          Logger[IO].info(s"[PostFetchService] Successfully fetched ${posts.length} posts")
        }
        .handleErrorWith { err =>
          Logger[IO].error(s"[PostFetchService] Failed to fetch posts from ${config.getPostsUrl}") *>
            IO.raiseError(err)
        }
    }
  }.pure[IO]
}
