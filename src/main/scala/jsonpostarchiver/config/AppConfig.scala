package jsonpostarchiver.config

import pureconfig.ConfigReader

case class AppConfig(getPostsUrl: String, filesTargetPath: String) derives ConfigReader
