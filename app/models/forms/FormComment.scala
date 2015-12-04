package models.forms

import play.api.data._
import play.api.data.Forms._

case class FormComment(
  author: String,
  body: String
)

object FormComment {
  val get = Form(
    mapping(
      "author" -> text,
      "body" -> text
    )(FormComment.apply)(FormComment.unapply)
  )
}
