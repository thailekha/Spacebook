import java.util.ArrayList;
import java.util.List;

import models.Message;
import models.Post;
import models.User;

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
}