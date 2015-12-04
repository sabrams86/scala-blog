package models

import play.api.Play.current
import models.forms._
import anorm._
import play.api.db.DB
import anorm.SqlParser._
import scala.language.postfixOps
import play.api.libs.json.{JsNull,Json,JsString,JsValue}

case class Comment(
  id: Int,
  author: String,
  body: String,
  postId: Int
)

object Comment{
  // Parsers

  val commentParser: RowParser[Comment] = {
    int("id") ~
    str("author") ~
    str("body") ~
    int("post_id") map {
      case id ~ author ~ body ~ post_id => Comment(id, author, body, post_id)
    }
  }

  val commentsParser: ResultSetParser[List[Comment]] = {
    commentParser *
  }

  // Database calls

  def insertComment(postId: Long, data: FormComment): Boolean = {
    val addedRows: Int = DB.withConnection { implicit connection =>
      SQL(
        """
        insert into comments
        (post_id, author, body)
        values
        ({postId}, {author}, {body})
        """
      ).on(
        "postId" -> postId,
        "author" -> data.author,
        "body" -> data.body
      ).executeUpdate()
    }
    addedRows == 1
  }

  def getComments(postId: Long): List[Comment] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
        select * from comments
        where post_id = {postId}
        """
      ).on(
        "postId" -> postId
      ).as(commentsParser)
    }
  }

  def getComment(id: Long): Comment = {
    DB.withConnection { implicit connection =>
      SQL(
        """
        select * from comments
        where id = {id}
        """
      ).on(
        "id" -> id
      ).as(commentsParser).head
    }
  }

  def updateComment(id: Long, data: FormComment): Boolean ={
    val updatedRows = DB.withConnection { implicit connection =>
      SQL(
        """
        update comments
        set author={author}, body={body}
        where id={id}
        """
      ).on(
        "author" -> data.author,
        "body" -> data.body,
        "id" -> id
      ).executeUpdate()
    }
    updatedRows == 1
  }

  def deleteComment(id: Long): Boolean = {
    val deletedRows = DB.withConnection { implicit connection =>
      SQL(
        """
        delete from comments
        where id = {id}
        """
      ).on(
        "id" -> id
      ).executeUpdate()
    }
    deletedRows == 1
  }

  def deleteComments(postId: Long): Boolean = {
    val deletedRows = DB.withConnection { implicit connection =>
      SQL(
        """
        delete from comments
        where post_id = {postId}
        """
      ).on(
        "postId" -> postId
      ).executeUpdate()
    }
    deletedRows == 1
  }
}
