public class Book extends Reading 
{
	private String[] authors;

	
	// Constructor to initialize new object of Book
	public Book(String id, String applicationName, int price, String publisher, String genre, int noOfPages, String[] authors) 
	{
		// Call constructor fo class Reading(Superclass)
		super(id, applicationName, price, publisher, genre, noOfPages);
		
		this.setAuthors(authors);
	}
	
	
	// Getter Method
	public String[] getAuthors()
	{
		return this.authors;
	}
	
	
	// Setter Method
	public void setAuthors(String[] authors)
	{
		this.authors = authors;
	}
}
