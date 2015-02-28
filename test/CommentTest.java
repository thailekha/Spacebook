import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;
import models.User;
import models.Comment;
import models.Post;

public class CommentTest extends UnitTest {
	
	private User demo, semi;
	private Post demopo;
	
	@BeforeClass
	  public static void loadDB()
	  {
	    Fixtures.deleteAllModels();
	  }
	
	@Before
	public void setUp()
	{
		demo = new User("fname", "lname", "demo@email.com", "secret", 100, "unknown");
		demo.save();
		semi = new User("first", "last", "semi@email.com", "secret", 100, "unknown");
		semi.save();
		demopo = new Post("random", "random", demo);
		demopo.save();
		
	}
	
	@After
	public void tearDown()
	{
		demopo.delete();
		semi.delete();
		demo.delete();
	}
	
	@Test
	public void testCreateComment()
	{
		Comment cmt1 = new Comment(semi, demopo, "text1", "randomDate");
		cmt1.save();
		
		List<Comment> cmtsUser = Comment.find("byCommenter", semi).fetch();
		List<Comment> cmtsPost = Comment.find("byPostHost", demopo).fetch();
		assertEquals(cmtsUser.size(), 1);
		assertEquals(cmtsPost.size(), 1);
		assertEquals(cmtsUser.get(0), cmtsPost.get(0));
		
		Comment comment1 = cmtsUser.get(0);
		assertEquals("text1", comment1.commentText);
		
		//demopo.comments.remove(cmt1);
		//demo.comments.remove(cmt1);
		cmt1.delete();
	}
	
	@Test
	public void testMultipleComments()
	{
		Comment cmt2 = new Comment(demo, demopo, "text2", "randomDate");
		cmt2.save();
		Comment cmt3 = new Comment(semi, demopo, "text3", "randomDate");
		cmt3.save();
		
		List<Comment> cmtsDemo = Comment.find("byCommenter", demo).fetch();
		List<Comment> cmtsSemi = Comment.find("byCommenter", semi).fetch();
		List<Comment> cmtsPost = Comment.find("byPostHost", demopo).fetch();
		assertEquals(cmtsDemo.size(), 1);
		assertEquals(cmtsSemi.size(), 1);
		assertEquals(cmtsPost.size(), 2);
		assertEquals(cmtsDemo.get(0), cmtsPost.get(0));
		assertEquals(cmtsSemi.get(0), cmtsPost.get(1));
		
		Comment comment2 = cmtsDemo.get(0);
		assertEquals("text2", comment2.commentText);
		
		Comment comment3 = cmtsSemi.get(0);				
		assertEquals("text3", comment3.commentText);
		
		cmt2.delete();
		cmt3.delete();
	}
	
	@Test
	public void testDeleteComment()
	{
		Comment cmt4 = new Comment(semi, demopo, "text4", "randomDate");
		cmt4.save();
		
		List<Comment> cmtsUser = Comment.find("byCommenter", semi).fetch();
		Comment comment4 = cmtsUser.get(0);
		demopo.comments.remove(comment4);
		demopo.save();
		cmt4.delete();
		
		User semiUser = User.findByEmail("demo@email.com");
		Post demopost = Post.find("byPostOwner", semiUser).first();
		assertEquals(demopost.comments.size(), 0);
	}
	
}
