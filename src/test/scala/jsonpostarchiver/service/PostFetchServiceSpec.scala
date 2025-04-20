package jsonpostarchiver.service

import cats.effect.IO
import jsonpostarchiver.model.Post
import jsonpostarchiver.utils.TestAppConfig
import munit.CatsEffectSuite
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.implicits.*
import org.http4s.client.Client
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jFactory

class PostFetchServiceSpec extends CatsEffectSuite {
  given Logger[IO] = Slf4jFactory.create[IO].getLogger

  val config = TestAppConfig.default

  test("fetchAllPosts should return parsed list of posts") {
    val testPosts = List(Post(1, "Test title 1", "Test body 1", 1), Post(2, "Test title 2", "Test body 2", 2))

    val mockHttpApp = HttpRoutes
      .of[IO] { case GET -> Root / "posts" =>
        Ok(testPosts)
      }
      .orNotFound

    val client = Client.fromHttpApp(mockHttpApp)

    for {
      service <- PostFetchService.make(client, config)
      posts   <- service.fetchAllPosts()
    } yield assertEquals(posts, testPosts)
  }

  test("fetchAllPosts should handle errors") {
    val mockHttpApp = HttpRoutes
      .of[IO] { case GET -> Root / "posts" =>
        InternalServerError()
      }
      .orNotFound

    val client = Client.fromHttpApp(mockHttpApp)

    for {
      service <- PostFetchService.make(client, config)
      result  <- service.fetchAllPosts().attempt
    } yield assert(result.isLeft)
  }
}
