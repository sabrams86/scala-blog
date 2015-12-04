package controllers

import play.api._
import play.api.mvc._
import models.forms._
import models._

class Comments extends Controller {

  def create(postId: Long) = Action { implicit response =>
    FormComment.get.bindFromRequest.fold(
      errors => {
        BadRequest("Boo")
      },
      results => {
        Comment.insertComment(postId, results)
        Redirect("/posts/"+postId)
      }
    )
  }

  def edit(postId: Long, id: Long) = Action { implicit response =>
    val comment = Comment.getComment(id)
    Ok(views.html.comments.edit(comment(0)))
  }

  def update(postId: Long, id: Long) = Action { implicit response =>
    FormComment.get.bindFromRequest.fold(
      errors => {
        BadRequest("Boo")
      },
      results => {
        Comment.updateComment(id, results)
        Redirect("/posts/"+postId)
      }
    )
  }

  def destroy(postId: Long, id: Long) = Action { implicit response =>
    Comment.deleteComment(id)
    Redirect("/posts/"+postId)
  }

}
