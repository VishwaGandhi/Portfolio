import java.util.ArrayList;

public class User 
{
	private String ID;
	private String name;
	private String phoneNo;
	private double balance;
	private OS currentDeviceOS;
	private boolean isPremium;
	private ArrayList<Content> purchasedContent;
	
	// Constructor to initialize new User objects
	public User(String ID, String name, String phoneNo, double balance, OS currentDeviceOS)
	{
		this.setID(ID);
		this.setName(name);
		this.setPhoneNo(phoneNo);
		this.setBalance(balance);
		this.setCurrentDeviceOS(currentDeviceOS);
		
		// Initialization with preset or null values
		this.setPurchasedContent(new ArrayList<Content>());
		this.setPremium(false);
	}
	
	
	// Getter Methods
	public String getUserId()
	{
		return this.ID;
	}
	
	
	public String getUserName()
	{
		return this.name;
	}
	
	
	public String getUserPhoneNo()
	{
		return this.phoneNo;
	}
	
	
	public double getBalance()
	{
		return this.balance;
	}
	
	
	public OS getUserCurrentDeviceOS()
	{
		return this.currentDeviceOS;
	}
	
	
	public ArrayList<Content> getPurchasedContent()
	{
		return this.purchasedContent;
	}
	
	
	// Setter Methods
	public void setID(String ID) 
	{
		this.ID = ID;
	}

	
	public void setName(String name) 
	{
		this.name = name;
	}

	
	public void setPhoneNo(String phoneNo) 
	{
		this.phoneNo = phoneNo;
	}

	
	public void setBalance(double balance) 
	{
		this.balance = balance;
	}

	
	public void setCurrentDeviceOS(OS currentDeviceOS) 
	{
		this.currentDeviceOS = currentDeviceOS;
	}

	
	public void setPremium(boolean isPremium) 
	{
		this.isPremium = isPremium;
	}

	
	public void setPurchasedContent(ArrayList<Content> purchasedContent) 
	{
		this.purchasedContent = purchasedContent;
	}
	
	
	// Method will make the normal user a premium user, if user has sufficient balance ( >= 100$).
	// If user doesn't have enough balance, method will throw "InsufficientFundsException"
	// If user is already a premium user, method will throw "AlreadyExistingException"
	public void becomePremium()
	{
		try 
		{
			System.out.println("Purchasing premium membership for user "+ this.getUserName());
			if(this.balance >= 100 && !this.isPremium)
			{
				this.setBalance(this.getBalance() - 100);
				this.setPremium(true);
			}
			else if(this.isPremium)
			{
				// Throw an exception to compensate the already existing user
				throw new AlreadyExistingException("Already Existing Exception Occured : User is already a premium user");
			}
			else
			{
				// Throw an exception to compensate the Insufficient balance
				throw new InsufficientFundsException("Insufficient balance to become a premium user for " + this.getUserName() + " Minimum Required Balance - 100.0, Available Balance - " + this.balance, this.balance);
			}
			System.out.println(this.getUserName() + " is now premium user. Enjoy discounts on every purchase.");
		}
		catch(InsufficientFundsException ifex)
		{
			System.out.println("\t" + ifex.getReason());
		}
		catch(AlreadyExistingException aeex)
		{
			System.out.println("\t" + aeex.getReason());
		}
		catch(Exception ex)
		{
			System.out.println("\t" + ex.getMessage());
		}
		System.out.println("-------------------------------------------------------------------------------------\n");
	}

	
	// Method will be called when some user attempts to buy some content from store
	// User must have sufficient balance to purchase the content, if not, "InsufficientFundsException" will occur
	// If user is purchasing a Game, OS & Version requirements must be met, if not, "VersionMismatchException" and "OsMismatchException" will occur
	public void buyContent(Content content)
	{
		try 
		{
			System.out.println("Purchasing "+ content.getApplicationName() +", for user "+this.getUserName());
			double effectivePrice = this.isPremium ? content.getPrice() * 0.80 : content.getPrice();
			if(this.getBalance() >= effectivePrice)	
			{	
				if(content instanceof Game)
				{
					Game tempGameRef = (Game) content;
					if(this.currentDeviceOS.getMoblieOs().matches(tempGameRef.getMinSystemRequirement().getMoblieOs()))
					{
						if( this.currentDeviceOS.getVersion() >= tempGameRef.getMinSystemRequirement().getVersion())
						{
							content.increaseDownload();
							
							this.purchasedContent.add(content);
							this.setBalance(this.getBalance() - effectivePrice);
						}
						else
						{
							// Throw an exception to compensate the version mismatch
							throw new VersionMismatchException("Version mismatch exception occured",((Game) content).getMinSystemRequirement().getVersion(),this.getUserCurrentDeviceOS().getVersion());
						}
					}
					else
					{
						// Throw an exception to compensate the OS mismatch
						throw new OsMismatchException("Os mismatch exception occured", ((Game) content).getMinSystemRequirement().getMoblieOs(),this.getUserCurrentDeviceOS().getMoblieOs());
					}
				}
				else
				{
					content.increaseDownload();
					this.purchasedContent.add(content);
					this.balance =  this.getBalance() - effectivePrice;
				}
				
			}
			else
			{
				// Throw an exception to compensate the Insufficient balance
				throw new InsufficientFundsException("Exception Occured : Insufficient balance to purchase "+ content.getApplicationName()+", price "+content.getPrice()+", available balance "+this.getBalance(),this.balance);
			}	
			System.out.println("Transaction successful, Purchased - "+ content.getApplicationName() +", for user "+this.getUserName());
		}
		catch(VersionMismatchException vrex)
		{
			System.out.println("\t"+vrex.getReason() + " Required Version - "+vrex.getRequiredVersion()+" Current Version - "+ vrex.getAvailableVersion());
		}
		catch(OsMismatchException osex)
		{
			System.out.println("\t"+osex.getReason() + " Required OS - "+osex.getRequiredOs()+" Current OS - "+ osex.getAvailableOs());
		}
		catch(InsufficientFundsException ifex)
		{
			System.out.println("\t"+ifex.getReason());
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		System.out.println("-------------------------------------------------------------------------------------\n");
	}

	
	// Method displays all the content bought by the user, which is stored in object attribute purchasdContent
	public void showContentBought()
	{
		int counter = 1;
		
		// String format to display the details in tabular format
		String format = "%5s%10s%30s%25s";
		
		System.out.println("Content purchased by "+ this.getUserName() +"("+this.getUserId()+")");
		System.out.println("----------------------------------------------------------------------");
		System.out.println(String.format(format,"No.","Type","App Name","Price"));
		System.out.println("----------------------------------------------------------------------");
		
		// Print out the details of all the content that user has bought
		for(Content tempContent : this.purchasedContent)
		{
			System.out.println(String.format(format, counter++, tempContent.getClass().toString().replaceFirst("class",""), tempContent.getApplicationName(), tempContent.getPrice()));
		}
		
		// If user has not bought any content, show appropriate message
		if(counter == 1)
		{
			System.out.println(String.format("%5s%5s%35s%5s"," ", "-------","No content bought","-------" ));
		}
		
		System.out.println("----------------------------------------------------------------------");
		System.out.println("");
	}
}