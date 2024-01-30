package card.core;

import java.time.Period;

import account.core.EAccount;
import card.model.Card;

public enum ECard {
	DEBIT {
		@Override
		public Period getPeriodOfType() 	{	return Period.ofYears(1);}
	},
	CREDIT{
		@Override
		public Period getPeriodOfType() 	{	return Period.ofMonths(8);}
	};
	
	public abstract Period getPeriodOfType();
}
