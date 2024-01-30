package account.core;

public enum EAccount {
	ACCUMULATIVE{
		@Override
		public double getRate()    		{ return ERate.MAIN_RATE.getRate() * 0.86; }
		@Override
		public double getMinLoan() 		{ return 25000; }
		@Override
		public boolean canTakeOrAdd() 	{ return false; }
	},
	SAVING{
		@Override
		public double getRate() 		{ return ERate.MAIN_RATE.getRate()  * 0.1; }
		@Override
		public double getMinLoan()  	{ return 0; }	
		@Override
		public boolean canTakeOrAdd()	{ return true; }
	},
	FOREIGN{
		@Override
		public double getRate()    		{ return ERate.MAIN_RATE.getRate() * 0.01; }
		@Override
		public double getMinLoan() 		{ return 0; }
		@Override
		public boolean canTakeOrAdd() 	{ return true; }
	};
	
	public abstract boolean canTakeOrAdd();
	public abstract double getMinLoan();
	public abstract double getRate();
}
