package controllers;
import java.util.List;
import models.Message;
import models.Post;
import models.Comment;
import models.User;
import play.Logger;
import play.mvc.Controller;
import java.util.Date;

public class Blog  extends Controller
{
  public static void index(Long id)
  {
    User user = User.findById(id);
    render(user);
  }
  
  public static void indexPost(Long id)
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
    postOwner.posts.add(0, post);
    postOwner.save();
    
    Logger.info ("title:" + title + " content:" + content);
    index(postOwner.id);
  }
  
  public static void deletePost(Long postid)
  {    
    User user = Accounts.getLoggedInUser(); 

    Post post = Post.findById(postid);
    user.posts.remove(post);

    user.save();
    post.delete();

    index(user.id);
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
	  
	  Logger.info("Number of comments: " + postHost.comments.size() 
			  + ", New comment from " + comment.commenter.firstName + " : " + comment.commentText);
	  indexPost(id);
  }
  
  public static void deleteComment(Long id)
  {
	  Comment comment = Comment.findById(id);
	  User user = Accounts.getLoggedInUser(); 
	  Post postHost = comment.postHost;
	  
	  user.comments.remove(comment);
	  postHost.comments.remove(comment);
	  
	  user.save();
	  postHost.save();
	  
	  comment.delete();
	  indexPost(postHost.id);
  }
}