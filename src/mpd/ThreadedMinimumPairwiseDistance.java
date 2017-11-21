package mpd;

public class ThreadedMinimumPairwiseDistance implements MinimumPairwiseDistance {
	public int finalResult = Integer.MAX_VALUE;


	//Synchronized method to set final result
	public synchronized void setResult(int diff){
		if(diff < finalResult){
			finalResult = diff;
		}
	}


	@Override
	public int minimumPairwiseDistance(int[] values){
		//Initializing four threads for each section in the array
		Thread[] threads = {new Thread(new Calculate("Lower_Left", values)), new Thread(new Calculate("Bottom_Right", values)), new Thread(new Calculate("Top_Right", values)), new Thread(new Calculate("Center", values))};
		//Starts all threads
		for(int i = 0; i < threads.length; i++){
			threads[i].start();
		}

		try{
			//Wait for the threads to finish and join them
			for(int i = 0; i < threads.length; i++){
				threads[i].join();
			}
		}catch (InterruptedException e){
			e.printStackTrace();
		}
		return finalResult;
	}

	//Inner class that implements Runnable
	private class Calculate implements Runnable{
		String section = "";
		int[] values;

		//Constructor that takes in a section and array of value
		public Calculate(String section, int[] values){
			this.section = section;
			this.values = values;
		}

		//Overided run function which calls calculate() and sets the result
		public void run(){
			int diff = calculate(section, values);
			setResult(diff);
		} 

		private int calculate(String section, int[] values){
			int N  = values.length;
			int halfOfN = N/2;
			int result = Integer.MAX_VALUE;

			//Each if statement has a nested for loop for one of the 4 sections
			if(section == "Lower_Left"){
				//This nested loop looks at the range 0 ≤ j < i < N/2
				for(int i = 0; i < halfOfN; i++){
					for(int j = 0; j < i; j++){
						int diff = Math.abs(values[i] - values[j]);
						if (diff < result) {
							result = diff;
						}
					}
				}
				return result; 

			}else if(section == "Bottom_Right"){
				//This nested loop looks at the range N/2 ≤ j + N/2 < i < N
				for(int i = halfOfN; i < N; i++){
					for(int j = 0; j+halfOfN < i; j++){
						int diff = Math.abs(values[i] - values[j]);
						if (diff < result) {
							result = diff;
						}
					}
				}
				return result;

			}else if(section == "Top_Right"){
				//This nested loop looks at the range N/2 ≤ j < i < N
				for(int i = halfOfN; i < N; i++ ){
					for(int j = halfOfN; j < i; j++){
						int diff = Math.abs(values[i] - values[j]);
						if (diff < result) {
							result = diff;
						}
					}
				}
				return result;

			}else if(section == "Center"){
				//This nested loop N/2 ≤ i ≤ j + N/2 < N
				for(int i = halfOfN; i < N; i++){
					for(int j = 0; j + halfOfN < N; j++){
						//If j + N/2 is greater than or equal to i then do operation otherwise continue
						if((j+halfOfN) >= i){
							int diff = Math.abs(values[i] - values[j]);
							if (diff < result) {
								result = diff;
							}
						}
					}
				}
				return result;

			}else{
				//In case none of the cases are found return -1 and prints error message
				System.out.println("Error: none of the cases were found");
				return -1;
			}
		}
	}
}
