package gui;

public class Shift implements Comparable<Shift> {
	
	private String date;
	private String startTime;
	private String endTime;
	private double earnings;
	private int startMileage;
	private int endMileage;
	
	public Shift(final String date, final String startTime, final String endTime, final double earnings, final int startMileage, final int endMileage) {
		this.date = date;
		this.startTime = startTime.toUpperCase();
		this.endTime = endTime.toUpperCase();
		this.earnings = earnings;
		this.startMileage = startMileage;
		this.endMileage = endMileage;
	}
	
	public void setDate(final String date) {
		this.date = date;
	}
	public void setStartTime(final String startTime) {
		this.startTime = startTime;
	}
	public void setEndTime(final String endTime) {
		this.endTime = endTime;
	}
	public void setEarnings(final double earnings) {
		this.earnings = earnings;
	}
	public void setStartMileage(final int mileage) {
		this.startMileage = mileage;
	}
	public void setEndMileage(final int mileage) {
		this.endMileage = mileage;
	}
	public int getStartMileage() {
		return this.startMileage;
	}
	public int getEndMileage() {
		return this.endMileage;
	}
	public String getDate() {
		return this.date;
	}
	public int getMonth() {
		if(this.date.charAt(1)=='/') {
			return Integer.parseInt(this.date.substring(0,1));
		}
		return Integer.parseInt(this.date.substring(0,2));
	}
	public String getStartTime() {
		return this.startTime;
	}
	public String getEndTime() {
		return this.endTime;
	}
	public int getMileage() {
		return this.endMileage - this.startMileage;
	}
	public double getEarnings() {
		return this.earnings;
	}
	public int getDuration() {
		int startHours, startMins, endHours, endMins;
		String morningEvening;
		String[] data = this.startTime.split(":");
		startHours = Integer.parseInt(data[0]);
		startMins = Integer.parseInt(data[1].substring(0,data[1].length()-2));
		morningEvening = data[1].substring(data[1].length()-2,data[1].length());
		data = this.endTime.split(":");
		endHours = Integer.parseInt(data[0]);
		endMins = Integer.parseInt(data[1].substring(0,data[1].length()-2));
		if(!morningEvening.equalsIgnoreCase(data[1].substring(data[1].length()-2,data[1].length())) && endHours - startHours < 0) endHours += 12;
		else if(endHours == startHours || endHours<startHours)endHours += 24;
		return ((endHours - startHours)*60)+(endMins - startMins);
	}
	
	@Override
	public String toString() {
		return String.format("Date: "+this.date + " Start Time: "+this.startTime + " End Time: "+this.endTime + " Earnings: $%.2f",this.earnings);
	}
	
	@Override
	public int compareTo(Shift o) {
		if(this.date.equals(o.date)&&this.startTime.equals(o.startTime)&&this.endTime.equals(o.endTime)) return 0;
		else {
			String[] date = this.date.split("/");
			String[] date2 = o.date.split("/");
			if(Integer.parseInt(date[0])<Integer.parseInt(date2[0])) {
				return -1;
			}
			else if(Integer.parseInt(date[0])==Integer.parseInt(date2[0])&&Integer.parseInt(date[1])<Integer.parseInt(date2[1])) {
				return -1;
			}
			else if(Integer.parseInt(date[0])==Integer.parseInt(date2[0])&&Integer.parseInt(date[1])==Integer.parseInt(date2[1])) {
				String[] data = this.startTime.split(":");
				String[] data2 = o.startTime.split(":");
				if(data[1].substring(data[1].length()-2,data[1].length()).equals(data2[1].substring(data2[1].length()-2,data2[1].length()))) {
					if(Integer.parseInt(data[0])<Integer.parseInt(data2[0]))return -1;
				}
				else if(data[1].substring(data[1].length()-2,data[1].length()).equals("AM"))return -1;
			}
		}
		return 1;
	}
}
