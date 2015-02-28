package controllers;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import models.Message;
import models.Post;
import models.Comment;
import models.User;
import play.Logger;
import play.mvc.Controller;
import java.util.Date;
 
public class Blog  extends Controller
{
  public static void index()
  {
    User user = Accounts.getLoggedInUser();
    Collections.reverse(user.posts);
    render(user);
  }
  
  public static void indexpost(Long id)
  {
	 Post post = Post.findById(id);
	 User user = null;
	 boolean fullVersion = true;
	 if(session.isEmpty())
	 {
		 fullVersion = false;
		 user = post.postOwner;
	 }
	 else
	 {
		 user = Accounts.getLoggedInUser();
	 }
	 render(post, user, fullVersion);
  }
  
  public static void newPost(String title, String content)
  {
    User postOwner = Accounts.getLoggedInUser();
    
    Post post = new Post (title, content, postOwner);
    post.save();
    postOwner.posts.add(post);
    postOwner.save();
    
    Logger.info ("title:" + title + " content:" + content + ", position: " + postOwner.posts.indexOf(post));
    index();
  }
  
  public static void deletePost(Long id)
  {    
    Post post = Post.findById(id);
    List<Comment> commentsInDatabase = Comment.find("byPostHost", post).fetch();
    for(Comment comment: commentsInDatabase)
    {
  	  User commenter = comment.commenter; 
  	  Post postHost = comment.postHost;
  	  
  	  commenter.commentsUser.remove(comment);
  	  commenter.save();
  	  postHost.comments.remove(comment);
  	  postHost.save();
  	 
  	  comment.delete();
    }
    
    User user = Accounts.getLoggedInUser();
    user.posts.remove(post);
    user.save();
    post.delete();

    index();
  }
  
  public static void comment(Long id, String commentText)
  {
	  User commenter = Accounts.getLoggedInUser();
	  
	  Post postHost = Post.findById(id);
	  
	  Date date = new Date();
	  String dateString = date.toString();
	  
	  Comment comment = new Comment(commenter, postHost, commentText, dateString);
	  comment.save();
	  postHost.comments.add(comment);	  
	  postHost.save();
	  
	  boolean commentExisted = false;
	  for(Comment cmt: commenter.commentsUser)
	  {
		  if(cmt.equals(comment))
		  {
			  commentExisted = true;
		  }
	  }
	  if(!commentExisted)
	  {
		  commenter.commentsUser.add(comment);
		  commenter.save();
	  }
	  
	  Logger.info("Number of comments: " + postHost.comments.size() 
			  + ", New comment from " + comment.commenter.firstName + " : " + comment.commentText);
	  indexpost(id);
  }
  
  public static void deleteComment(Long id)
  {
	  User deleter = Accounts.getLoggedInUser();
	  Comment comment = Comment.findById(id);
	  User commenter = comment.commenter; 
	  Post postHost = comment.postHost;
	  
	  if(deleter.equals(commenter) || (deleter.equals(postHost.postOwner)))
	  {
		  commenter.commentsUser.remove(comment);
		  commenter.save();
		  postHost.comments.remove(comment);
		  postHost.save();
	 
		  comment.delete();
	  }
	  indexpost(postHost.id);
  }
}