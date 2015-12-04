package models

import play.api.Play.current
import models.forms._
import anorm._
import play.api.db.DB
import anorm.SqlParser._
import scala.language.postfixOps
import play.api.libs.json.{JsNull,Json,JsString,JsValue}

case class Post(
  title: String,
  content: String
)

object Post {
  def insertPost(data: FormNewPost): Boolean = {
    val addedRows: Int = DB.withConnection { implicit connection =>
      SQL(
        """
        insert into posts (title, content) values ({title}, {content});
        """
      ).on(
        "title" -> data.title,
        "content" -> data.content
      ).executeUpdate()
    }
    addedRows == 1
  }

  def getPosts: List[(Int, String, String)] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
        select * from posts
        """
      ).as( int("idposts") ~ str("title") ~ str("content") map(flatten) * )
    }
  }

  def getPost(id: Long): List[(Int, String, String)] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
        select * from posts where idposts = {id}
        """
      ).on(
        "id" -> id
      ).as( int("idposts") ~ str("title") ~ str("content") map(flatten) * )
    }
  }

  def updatePost(id: Long, data: FormNewPost): Boolean = {
    val updatedRows = DB.withConnection { implicit connection =>
      SQL(
        """
        update posts
        set title={title}, content={content}
        where idposts={id}
        """
      ).on(
        "id" -> id,
        "title" -> data.title,
        "content" -> data.content
      ).executeUpdate()
    }
    updatedRows == 1
  }

  def deletePost(id: Long): Boolean = {
    val deletedRows = DB.withConnection { implicit connection =>
      SQL(
        """
        delete from posts
        where idposts={id}
        """
      ).on(
        "id" -> id
      ).executeUpdate()
    }
    deletedRows == 1
  }

}
