import java.util.ArrayList;

public abstract class Content 
{	 
	private String ID;
	private String applicationName;
	private int noOfDownloads;
	private int price;
	private ArrayList<Comment> reviews;
	
	
	// Constructor to initialize a new content object
	public Content(String ID, String applicationName, int price)
	{
		this.setID(ID);
		this.setApplicationName(applicationName);
		this.setPrice(price);
		
		// Initializing with preset or empty objects
		this.setNoOfDownloads(0);
		this.setReviews(new ArrayList<Comment>());;
	}

	
	// Getter methods
	public String getID() 
	{
		return this.ID;
	}
	
	
	public String getApplicationName()
	{
		return this.applicationName;
	}
	
	
	public int getNoOfDownloads() 
	{
		return this.noOfDownloads;
	}
	
	
	public int getPrice()
	{
		return this.price;
	}
	
	
	public ArrayList<Comment> getReviews()
	{
		return this.reviews;
	}

	
	// Setter Methods
	public void setID(String ID) 
	{
		this.ID = ID;
	}


	public void setApplicationName(String applicationName) 
	{
		this.applicationName = applicationName;
	}


	public void setNoOfDownloads(int noOfDownloads) 
	{
		this.noOfDownloads = noOfDownloads;
	}


	public void setPrice(int price) 
	{
		this.price = price;
	}


	public void setReviews(ArrayList<Comment> reviews) 
	{
		this.reviews = reviews;
	}	
	
	
	// Mutator methods
	
	// Increase the number of downloads by 1 
	public void increaseDownload()
	{
		this.noOfDownloads = this.noOfDownloads + 1;
	}
	
	
	// Adding a new Comment Object to Reviews List
	public void addReview(Comment comment)
	{
		this.reviews.add(comment);
	}
	
	
	// Shows all the reviews and subsequent replies for all of the reviews associated with the content object
	public void showReviews()
	{
		System.out.println("Showing Reviews for: "+this.getApplicationName()+"("+this.getClass().getName()+")");
		System.out.println("----------------------------------------------------------------------");
		
		int counter = 1;
		
		// For each comment object in Reviews list, parse the comment by initiating a recursive call 
		for(Comment comment : this.getReviews())
		{
			System.out.print(counter++ + ". ");
			parseComment(comment, 1);
		}
		
		// If there are no reviews, print out appropriate message
		if(counter == 1)
		{
			System.out.println(String.format("%5s%5s%35s%5s", " ", "-------", "No reviews yet!! Be first to write a review...", "-------" ));
		}
		
		System.out.println("----------------------------------------------------------------------");
		System.out.println("");
	}


	// Method recursively calls itself to print out Reviews and their Replies
	// Parameter level suggests the depth of recursive call, for first call - 1, second recursive call - 2 ...
	// level also tracks the number of tabs to leave before printing the comment text
	private void parseComment(Comment comment,int level)
	{
		// Recursion 
		// Base Case - If there are no replies to the current comment object, return comment text
		// Recursive Case - if comment has replies, call the method again for each of the reply 
		if(comment.getReplies().size() == 0)
		{
			System.out.println( this.buildCommnetText(comment) + "\n\t");
		}
		else
		{
			System.out.println( this.buildCommnetText(comment) + "\n\t");
			
			for(Comment reply : comment.getReplies())
			{
				// Print tabs according to level of recursion
				for(int counter = 0; counter<level; counter++)
					{
						System.out.print("\t");	
					}
				// Call the method recursively for each of the subsequent reply
				parseComment(reply, level + 1);
			}
		}
	}
	
	
	// Method returns a formatted string in format <username> (<userid>): <commentText>
	private String buildCommnetText(Comment comment)
	{
		String result = "";
		return result + comment.getUser().getUserName() + " (" + comment.getUser().getUserId() + "): " + comment.getCommentText();
	}

}
