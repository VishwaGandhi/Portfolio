public class Magazine extends Reading
{
	private String mainFeatureTitle;
	
	
	// Constructor to initialize new Magazine object
	public Magazine(String ID, String applicationName, int price, String publisher, String genre, int noOfPages, String mainFeatureTitle) 
	{
		// Call to Reading (Superclass) Constructor
		super(ID, applicationName, price, publisher, genre, noOfPages);
		
		this.setMainFeatureTitle(mainFeatureTitle);
	}

	
	//Getter Method
	public String getMainFeatureTitle()
	{
		return this.mainFeatureTitle;
	}
	
	
	//Setter Method
	public void setMainFeatureTitle(String mainFeatureTitle)
	{
		this.mainFeatureTitle = mainFeatureTitle;
	}
	
}
