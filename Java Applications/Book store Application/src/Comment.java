import java.util.ArrayList;

public class Comment 
{
	private String commentText;
	private ArrayList<Comment> replies;
	private User user;
	
	
	// Constructor to initialize new comment objects
	public Comment(User user,String commentText)
	{
		this.setCommentText(commentText);
		this.setUser(user);
		
		// Initializing with preset or empty objects
		this.setReplies(new ArrayList<Comment>());
	}

	
	// Getter Methods
	public String getCommentText() 
	{
		return commentText;
	}

	
	public ArrayList<Comment> getReplies() 
	{
		return replies;
	}

	
	public User getUser() 
	{
		return user;
	}
	
	
	// Setter Methods
	public void setCommentText(String commentText) 
	{
		this.commentText = commentText;
	}
	
	
	public void setReplies(ArrayList<Comment> replies) 
	{
		this.replies = replies;
	}	

	
	public void setUser(User user) 
	{
		this.user = user;
	}
	
	
	// Mutator Method
	// Method adds a new reply to the list of replies in comment object
	public void addReply(Comment reply) 
	{
		this.replies.add(reply);
	}
	
}
