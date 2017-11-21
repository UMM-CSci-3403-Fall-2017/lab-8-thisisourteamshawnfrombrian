package mpd;

public class ThreadedMinimumPairwiseDistance implements MinimumPairwiseDistance {
	public int finalResult = Integer.MAX_VALUE;


	public synchronized void setResult(int diff){
		if(diff < finalResult){
			finalResult = diff;
		}
	}

	@Override
	public int minimumPairwiseDistance(int[] values){ 

		Thread[] threads = {new Thread(new Calculate("Lower_Left", values)), new Thread(new Calculate("Bottom_Right", values)), new Thread(new Calculate("Top_Right", values)), new Thread(new Calculate("Center", values))};
		for(int i = 0; i < threads.length; i++){
			threads[i].start();
		}
		try{
		for(int i = 0; i < threads.length; i++){
			threads[i].join();
		}
		}catch (InterruptedException e){
			e.printStackTrace();
		}
		return finalResult;
	}

	private class Calculate implements Runnable{
		String section = "";
		int[] values;

		public Calculate(String section, int[] values){
			this.section = section;
			this.values = values;
		}

		public void run(){
			int diff = calculate(section, values);
			setResult(diff);
		} 

		private int calculate(String section, int[] values){
			int N  = values.length;
			int halfOfN = N/2;
			int result = Integer.MAX_VALUE;

			if(section == "Lower_Left"){
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
				for(int i = halfOfN; i < N; i++){
					for(int j = 0; j + halfOfN < N; j++){
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
				System.out.println("Error");
				return -1;
			}
		}
	}
}
