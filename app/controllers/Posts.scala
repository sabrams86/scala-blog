package controllers

import play.api._
import play.api.mvc._
import models.forms._
import models._

class Posts extends Controller {

  def index = Action { implicit response =>
    val posts = Post.getPosts
    Ok(views.html.posts.index(posts))
  }

  def newPost = Action { implicit response =>
    Ok(views.html.posts.newform())
  }

  def create = Action { implicit response =>
    FormNewPost.get.bindFromRequest.fold(
      errors => {
        BadRequest("Boo")
      },
      results => {
        Post.insertPost(results)
        Redirect("/posts")
      }
    )
  }

  def show(id: Long) = Action { implicit response =>
    val post = Post.getPost(id)
    val comments = Comment.getComments(id)
    Ok(views.html.posts.show(post, comments))
  }

  def edit(id: Long) = Action { implicit response =>
    val post = Post.getPost(id)
    Ok(views.html.posts.edit(post))
  }

  def update(id: Long) = Action { implicit response =>
    FormNewPost.get.bindFromRequest.fold(
      errors => {
        BadRequest("Boo")
      },
      results => {
        Post.updatePost(id, results)
        Redirect("/posts/"+id)
      }
    )
  }

  def destroy(id: Long) = Action { implicit response =>
    Post.deletePost(id)
    Comment.deleteComments(id)
    Redirect("/posts")
  }

}
