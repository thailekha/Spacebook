package models;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.util.Date;
import javax.persistence.ManyToOne;
import play.db.jpa.Model;

@Entity
@Table(name="`Comment`")
public class Comment extends Model {

	public String date;
	
	@ManyToOne
	public User commenter;
	
	@ManyToOne
	public Post postHost;
	
	public String commentText;
	
	public Comment(User commenter, Post postHost, String commentText, String date)
	{
		this.commenter = commenter;
		this.postHost = postHost;
		this.commentText = commentText;
		this.date = date;
	}
	
}
