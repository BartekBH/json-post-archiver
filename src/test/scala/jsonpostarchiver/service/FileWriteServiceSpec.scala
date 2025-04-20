package jsonpostarchiver.service

import cats.effect.IO
import fs2.io.file.{Files, Path}
import jsonpostarchiver.model.Post
import jsonpostarchiver.utils.TestAppConfig
import munit.CatsEffectSuite
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jFactory

class FileWriteServiceSpec extends CatsEffectSuite {
  given Logger[IO] = Slf4jFactory.create[IO].getLogger

  val post          = Post(1, "Test title", "Test body", 2)
  val config        = TestAppConfig.default
  val configInvalid = TestAppConfig.invalid
  val filePath      = Path(config.filesTargetPath)
  val fileName      = s"${post.id}.json"
  val path          = filePath.resolve(fileName)

  test("writePost should create a JSON file with correct content") {
    for {
      service <- FileWriteService.make(config)
      _       <- service.writePost(post)
      exists  <- Files[IO].exists(path)
      content <- Files[IO].readUtf8(path).compile.string
    } yield {
      assert(exists)
      assert(content.contains("1"))
      assert(content.contains("Test title"))
      assert(content.contains("Test body"))
      assert(content.contains("2"))
    }
  }

  test("writePost should handle errors") {
    for {
      service <- FileWriteService.make(configInvalid)
      result  <- service.writePost(post).attempt
    } yield assert(result.isLeft)
  }

  override def afterAll(): Unit = Files[IO].deleteRecursively(Path(config.filesTargetPath)).unsafeRunSync()
}
