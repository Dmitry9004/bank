package account.core;


public enum ERate {
	
	MAIN_RATE(16);
	
	private double rate;
	ERate(double rate){
		this.rate = rate;
	}
	
	public double getRate() { return rate; }
}
