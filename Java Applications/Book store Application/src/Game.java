public class Game extends Content 
{
	private boolean isMultiPlayer;
	private OS minSystemRequirement;
	
	// Constructor to initialize the Game objects
	public Game(String ID, String applicationName, int price, boolean isMultiPlayer, OS minSystemRequirement) 
	{	
		// Call to (Content) superclass constructor
	 	super(ID, applicationName, price);
	 	
	 	this.setMultiPlayer(isMultiPlayer);
	 	this.setMinSystemRequirement(minSystemRequirement);
	}

	// Getter Methods
	public boolean isMultiPlayer()
	{
		return isMultiPlayer;
	}
	

	public OS getMinSystemRequirement() 
	{
		return minSystemRequirement;
	}
	
	
	// Setter Methods
	public void setMultiPlayer(boolean isMultiPlayer) 
	{
		this.isMultiPlayer = isMultiPlayer;
	}


	public void setMinSystemRequirement(OS minSystemRequirement) 
	{
		this.minSystemRequirement = minSystemRequirement;
	}
}
