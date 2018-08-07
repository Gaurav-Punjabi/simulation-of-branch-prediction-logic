/**
 * This class is used to represent the historyBit of a individual conditional statement.
 * It also provides a method to update the Bit.
 * There are 4 different states of the Bit : 
 * 		0 = Strongly False
 *		1 = Lightly False
 * 		2 = Lightly True
 *		3 = Strongly True.
 * If the predicted state was true the bit proceeds towards true state otherwise it proceeds towards false state.
 * If the state is at Strongly False or Strongly True then it does not change its state if the predicted state was correct.
 */
public class HistoryBit {
	/**
	 * A Integer to store the bit value of the individual condition.
	 */
	private int bit;
	private int id;

	public HistoryBit(final int id) {
		this.bit = 0;
		this.id = id;
	}
	
	/**
	 * This method is used to update the state of the bit according to the result of the condition.As explained above.
	 */
	public void updateBit(final boolean isTrue) {
		if(isTrue) {
			if(this.bit == 3)
				return;
			this.bit++;
			return;
		}
		if(this.bit == 0)
			return;
		this.bit--;
	}

	public int getBit() {
		return this.bit;
	}
	public int getId() {
		return this.id;
	}
}