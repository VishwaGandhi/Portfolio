package model;
import util.DateTime;

public class RentalRecord implements Comparable<Object>{

	private String recordId;
	private DateTime rentDate;
	private DateTime estimatedReturnDate;
	private DateTime actualReturnDate;
	private double rentalFee;
	private double lateFee;
	
	public RentalRecord() {
	}
	public RentalRecord(String recordId, DateTime rentDate, DateTime estimatedReturnDate) {
		this.setRecordId(recordId);
		this.setRentDate(rentDate);
		this.setEstimatedReturnDate(estimatedReturnDate);
	}

	public String toString()
	{
		//recordId:rentDate:estimatedReturnDate:actualReturnDate:rentalFee:lateFee
		String formated_record = recordId + ":" + rentDate + ":" + estimatedReturnDate + ":" + actualReturnDate + ":" + rentalFee + ":" + lateFee;
		
		return formated_record;
	}
	
	// Rental record details in one line format
	public String getDetails() {
		String result = "";
		String recordId = "Record ID:\t\t"+this.getRecordId() + "\n";
		String rentDate = "Rent Date:\t\t"+this.getRentDate()+ "\n";
		String estimatedReturnDate = "Estimated Return Date:\t"+this.getEstimatedReturnDate()+ "\n";
		result = recordId + rentDate + estimatedReturnDate;
		if(this.getActualReturnDate() != null) {
			String actualReturnDate = "Actual Return Date:\t"+this.getActualReturnDate()+"\n";
			String rentalFee ="RentalFee:\t\t"+ Double.toString(this.getRentalFee())+"\n";
			String lateFee = "LateFee:\t\t"+this.getLateFee()+"\n";
			result = result + actualReturnDate + rentalFee + lateFee;
		}
		return result;
	}
	public String getRecordId() {
		return recordId;
	}
	public DateTime getRentDate() {
		return rentDate;
	}
	public DateTime getEstimatedReturnDate() {
		return estimatedReturnDate;
	}
	public DateTime getActualReturnDate() {
		return actualReturnDate;
	}
	public double getRentalFee() {
		return rentalFee;
	}
	public double getLateFee() {
		return lateFee;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public void setRentDate(DateTime rentDate) {
		this.rentDate = rentDate;
	}
	public void setEstimatedReturnDate(DateTime estimatedReturnDate) {
		this.estimatedReturnDate = estimatedReturnDate;
	}
	public void setActualReturnDate(DateTime actualReturnDate) {
		this.actualReturnDate = actualReturnDate;
	}
	public void setRentalFee(double rentalFee) {
		this.rentalFee = rentalFee;
	}
	public void setLateFee(double lateFee) {
		this.lateFee = lateFee;
	}
	
	@Override
	public int compareTo(Object ob) {
		DateTime compareTo = ((RentalRecord)ob).getRentDate();
		
		return DateTime.diffDays(compareTo, this.getRentDate());
	}
	
	
}