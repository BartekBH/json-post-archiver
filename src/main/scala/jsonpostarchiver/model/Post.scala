package jsonpostarchiver.model

import io.circe.generic.semiauto.*
import io.circe.{Decoder, Encoder}

case class Post(
  id: Int,
  title: String,
  body: String,
  userId: Int)

object Post {
  implicit val decoder: Decoder[Post] = deriveDecoder[Post]
  implicit val encoder: Encoder[Post] = deriveEncoder[Post]
}
