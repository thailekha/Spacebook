import java.util.ArrayList;
import java.util.List;

import models.Message;
import models.Post;
import models.User;
import models.Comment;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class BlogTest extends UnitTest
{
  private User bob;
  private Post post1, post2;

  @BeforeClass
  public static void loadDB()
  {
    Fixtures.deleteAllModels();
  }

  @Before
  public void setup()
  {
    bob   = new User("bob", "jones", "bob@jones.com", "secret", 20, "irish");
    //post1 = new Post("Post Title 1", "This is the first post content", bob);
    //post2 = new Post("Post Title 2", "This is the second post content", bob);
    bob.save();
    //post1.save();
    //post2.save();
  }

  @After
  public void teardown()
  {
    bob.delete();
    //post1.delete();
    //post2.delete();
  }

  @Test
  public void testCreatePost()
  {
	post1 = new Post("Post Title 1", "This is the first post content", bob);
	post1.save();
	
	List<Post> bobPosts = Post.find("byPostOwner", bob).fetch();

    assertEquals(1, bobPosts.size());
    Post post = bobPosts.get(0);
    assertEquals(post.title, "Post Title 1");
    assertEquals(post.content, "This is the first post content");
    
    post1.delete();
  }

  @Test
  public void testUpdatePost()
  {
	  Post post5 = new Post("Post Title 5", "This is the fifth post content", bob);
	  post5.save();
	 
	  List<Post> posts = Post.find("byPostOwner", bob).fetch();
	  Post postFive = posts.get(0);
	  postFive.title = "New post title 5";
	  postFive.content = "New fifth post content";
	  postFive.save();
	  
	  assertNotSame(post5.title, "Post Title 5");
	  assertNotSame(post5.content, "This is the fifth post content");
	  
	  post5.delete();
  }
  
  @Test
  public void testCreateMultiplePosts()
  {
	post1 = new Post("Post Title 1", "This is the first post content", bob);
	post2 = new Post("Post Title 2", "This is the second post content", bob);
    post1.save();
    post2.save();

    List<Post> bobPosts = Post.find("byPostOwner", bob).fetch();
    assertEquals(2, bobPosts.size());

    Post postOne = bobPosts.get(0);
    Post postTwo = bobPosts.get(1);
    assertEquals(postOne.title, "Post Title 1");
    assertEquals(postOne.content, "This is the first post content");
    assertEquals(postTwo.title, "Post Title 2");
    assertEquals(postTwo.content, "This is the second post content");
    
    post1.delete();
    post2.delete();
  }
  
  @Test
  public void testDeletePost()
  {
    Post post3 = new Post("Post Title 3", "This is the third post content", bob);
    post3.save();

    List<Post> bobPosts = Post.find("byPostOwner", bob).fetch();
    //assertEquals(1, bobPosts.size());
    Post postThree = bobPosts.get(0);
    bob.posts.remove(postThree);
    bob.save();
    post3.delete();

    User anotherUser = User.findByEmail("bob@jones.com");
    assertEquals(0, anotherUser.posts.size());   
   }
  
  @Test
  public void testDeletePostThatHasComments()
  {
	Post postToDel = new Post("Post To Delete", "The content", bob); 
	postToDel.save();
	User mary = new User("mary", "colllins", "mary@collins.com", "secret", 20, "irish");
    mary.save();
	Comment comment1 = new Comment(bob, postToDel, "text1", "randomDate");
	comment1.save();
	Comment comment2 = new Comment(mary, postToDel, "text2", "randomDate");
	comment2.save();
	
	List<Comment> cmts1 = Comment.find("byCommenter", bob).fetch();
	Comment cmt1 = cmts1.get(0);
	List<Comment> cmts2 = Comment.find("byCommenter", mary).fetch();
	Comment cmt2 = cmts2.get(0);
	List<Post> bobPosts = Post.find("byPostOwner", bob).fetch();
    Post postToDelete = bobPosts.get(0);
    
    postToDelete.comments.remove(cmt1);
    postToDelete.comments.remove(cmt2);
    postToDelete.save();
    bob.commentsUser.remove(cmt1);
    bob.save();
    mary.commentsUser.remove(cmt2);
    mary.save();
    
    cmt1.delete();
    cmt2.delete();
    
    bobPosts.remove(postToDelete);
    postToDelete.delete();
	
    User mirrorBob = User.findByEmail("bob@jones.com");
    assertEquals(0, mirrorBob.commentsUser.size());
    User mirrorMary = User.findByEmail("mary@collins.com");
    assertEquals(0, mirrorMary.commentsUser.size());
    List<Post> postsOfBob = Post.find("byPostOwner", mirrorBob).fetch();
    assertEquals(0, postsOfBob.size());
    
    mary.delete();
  }
}