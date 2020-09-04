@SuppressWarnings("serial")
class InsufficientFundsException extends Exception 
{	
	// Reason of the exception
	private String reason;	
	// Available Balance
	private double availableBalance;  	
   
	
	// Constructor to set the reason  
	public InsufficientFundsException(String reason) 
	{
		this.reason = reason;
	}	
	   
		
	// Constructor to set the reason with balance
	public InsufficientFundsException(String reason,  double availableBalance) 
	{
		this.reason = reason;		
		this.availableBalance = availableBalance;
	}
   

	// Get reason of the exception
	public String getReason() 
	{	
		return reason; 
	}
	   

	// The available balance    
	public double availableBalance() 
	{ 
		return availableBalance;
	}	
}	   			


@SuppressWarnings("serial")
class AlreadyExistingException extends Exception
{
	// Reason of the exception
	private String reason;
	
	
	// Constructor to set the reason
	public AlreadyExistingException(String reason)
	{
		this.reason = reason;
	}
	
	
	// Get reason of the exception
	public String getReason() 
	{	
		return reason; 
	}
}


@SuppressWarnings("serial")
class OsMismatchException extends Exception
{
	// Reason of the exception
	private String reason;
	private String requiredOs;
	private String availableOs;
	
	
	// Constructor to initialize the class attributes
	public OsMismatchException(String reason, String requiredOs, String availableOs)
	{
		this.reason = reason;
		this.requiredOs = requiredOs;
		this.availableOs = availableOs;
	}
	
	// Get reason of the exception
	public String getReason() 
	{	
		return reason; 
	}
	

	// Get required version
	public String getRequiredOs() 
	{	
		return this.requiredOs; 
	}
	
	
	// Get available version
	public String getAvailableOs() 
	{	
		return this.availableOs; 
	}
}


@SuppressWarnings("serial")
class VersionMismatchException extends Exception
{
	// Reason of the exception
	private String reason;
	private int requiredVersion;
	private int availableVersion;
	
	
	// Constructor to initialize the class attributes
	public VersionMismatchException(String reason,int requiredVersion, int availableVersion)
	{
		this.reason = reason;
		this.requiredVersion = requiredVersion;
		this.availableVersion = availableVersion;
	}
	
	
	// Get reason of the exception
	public String getReason() 
	{	
		return reason; 
	}

	
	// Get required version
	public int getRequiredVersion() 
	{	
		return this.requiredVersion; 
	}
	
	
	// Get available version
	public int getAvailableVersion() 
	{	
		return this.availableVersion; 
	}
}