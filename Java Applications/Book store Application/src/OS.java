public class OS 
{
	private String mobileOs;
	private int version;	
	
	// Constructor to initialize new OS Object
	public OS(String mobileOs, int version)
	{
		this.setMoblieOs(mobileOs);
		this.setVersion(version);
	}
	
	
	// Getter Methods
	public String getMoblieOs()
	{
		return this.mobileOs;
	}
	
	
	public int getVersion()
	{
		return this.version;
	}
	
	
	// Setter Methods
	public void setMoblieOs(String mobileOs)
	{
		this.mobileOs = mobileOs;
	}
	
	
	public void setVersion(int version)
	{
		this.version = version;
	}
}
