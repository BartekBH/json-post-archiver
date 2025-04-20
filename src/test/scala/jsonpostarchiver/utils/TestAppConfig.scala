package jsonpostarchiver.utils

import jsonpostarchiver.config.AppConfig

object TestAppConfig {
  val default: AppConfig = AppConfig(
    getPostsUrl = "http://localhost/posts",
    filesTargetPath = "test_output"
  )

  val invalid: AppConfig = AppConfig(
    getPostsUrl = "invalid-url",
    filesTargetPath = "/root/test_output"
  )
}
