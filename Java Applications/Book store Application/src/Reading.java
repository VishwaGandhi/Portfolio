public abstract class Reading extends Content 
{
	private String publisher;
	private String genre; 	
	private int noOfPages;

	
	// Constructor to initialize new Reading objects
	public Reading(String ID, String applicationName, int price, String publisher, String genre, int noOfPages) 
	{
		super(ID, applicationName, price);
		
		this.setPublisher(publisher);
		this.setGenre(genre);
		this.setNoOfPages(noOfPages);
	}


	//Getter Methods
	public String getPublisher() 
	{
		return publisher;
	}


	public String getGenre() 
	{
		return genre;
	}
	
	
	public int getNoOfPages() 
	{
		return noOfPages;
	}
	
	
	//Setter Methods
	public void setPublisher(String publisher) 
	{
		this.publisher = publisher;
	}


	public void setGenre(String genre) 
	{
		this.genre = genre;
	}
	

	public void setNoOfPages(int noOfPages) 
	{
		this.noOfPages = noOfPages;
	}
	
}
