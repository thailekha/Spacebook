package models;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class Post extends Model
{	
  public String title;
  
  @ManyToOne
  public User postOwner;
  
  @OneToMany (cascade=CascadeType.ALL) 
  public List<Comment> comments = new ArrayList<Comment>();
  
  @Lob
  public String content;

  public Post(String title, String content, User postOwner)
  {
	this.postOwner = postOwner;
    this.title = title;
    this.content = content;
  }

  public void cleanComments()
  {
	  comments.clear();
  }
  
  public String toString()
  {
    return title;
  } 
}