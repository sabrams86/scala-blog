package models

import play.api.Play.current
import models.forms._
import anorm._
import play.api.db.DB
import anorm.SqlParser._
import scala.language.postfixOps
import play.api.libs.json.{JsNull,Json,JsString,JsValue}

case class Post(
  id: Int,
  title: String,
  content: String
)

object Post {
  // Parsers

  val postParser: RowParser[Post] = {
    int("id") ~
    str("title") ~
    str("content") map {
      case id ~ title ~ content => Post(id, title, content)
    }
  }

  val postsParser: ResultSetParser[List[Post]] = {
    postParser *
  }

  // Database calls

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

  def getPosts: List[Post] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
        select * from posts
        """
      ).as(postsParser)
    }
  }

  def getPost(id: Long): Post = {
    DB.withConnection { implicit connection =>
      SQL(
        """
        select * from posts where id = {id}
        """
      ).on(
        "id" -> id
      ).as(postsParser).head
    }
  }

  def updatePost(id: Long, data: FormNewPost): Boolean = {
    val updatedRows = DB.withConnection { implicit connection =>
      SQL(
        """
        update posts
        set title={title}, content={content}
        where id={id}
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
        where id={id}
        """
      ).on(
        "id" -> id
      ).executeUpdate()
    }
    deletedRows == 1
  }

}
