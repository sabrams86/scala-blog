package models.forms

import play.api.data._
import play.api.data.Forms._

case class FormNewPost(
  title: String,
  content: String
)

object FormNewPost {
  val get = Form(
    mapping(
      "title" -> text,
      "content" -> text
    )(FormNewPost.apply)(FormNewPost.unapply)
  )
}
