public class Heap {
	private Document[] heapArray;
	private int maxSize;           // size of array
	private int currentSize;       // number of nodes in array

	public Heap(int mx)            
	{
		maxSize = mx;
		currentSize = 0;
		heapArray = new Document[maxSize];  
	}

	public boolean isEmpty(){
		return currentSize == 0;
	}//end isEmpty

	public int getCurrentSize(){
	return currentSize;		
	}

	public void trickleUp(int index){
		int parent = (index-1)/2;
		Document bottom = heapArray[index];
		bottom = heapArray[index];
		while(index>0 && heapArray[parent].getSim()<bottom.getSim()){
			heapArray[index] = heapArray[parent];
			index = parent;
			parent = (parent-1)/2;
		}
		heapArray[index] = bottom;
	}//end trickleUp


	public boolean insert(Document doc)
	{
		if(currentSize==maxSize){return false;}
			heapArray[currentSize] = doc;
			trickleUp(currentSize++);	
		return true;
	}//end insert  
	
	public Document remove()           
	{                  
		Document root = heapArray[0];
		if(currentSize==0){
		return null;	
		}  
		heapArray[0] = heapArray[--currentSize];
		trickleDown(0);
		return root;
	}//end remove  

	public void trickleDown(int index){
		int largerChild;
		Document top = heapArray[index];
		while(index < currentSize/2){
			int leftChild = 2*index+1;
			int rightChild = leftChild+1;
			if(rightChild < currentSize && heapArray[leftChild].getSim() < heapArray[rightChild].getSim()){
				largerChild = rightChild;
			}else{
				largerChild = leftChild;
			}
			if(top.getSim() >= heapArray[largerChild].getSim()){break;}
			heapArray[index] = heapArray[largerChild];
			index = largerChild;
		}//end while
		heapArray[index] = top;
	}//end trickleDown

	public static void main(String[] args) {
		ReutersParser newParse = new ReutersParser("./data");
		Document[] docs = newParse.parse();//parsed all 8K files
		Document[] articlesForUser = new Document[10];
		Heap tempDocHeap = new Heap(docs.length);		
		Heap finalHeap = new Heap(10);
		Heap docsHeap   = new Heap(docs.length);
		int indexOfProfile = 500;
		int y = 0;
		double cntMinHashBeforeFinal = 0.0;
		double[] threshold = {0.1,0.15,0.2,0.25,0.3};
		int[] numberOfArticlesPerDay ={2,4,6,8,10};
		int cntMinHash = 0;
		int cntJaccard = 0;
		double accuracy = 0;
		for(int i = 0;i<docs.length;i++){
			docs[i].minhash(docs[indexOfProfile]);
			docsHeap.insert(docs[i]);	
			tempDocHeap.insert(docs[i]);		
		}
			for(int t=0;t < threshold.length;t++){
				for(int z=0;z<docs.length;z++){
					if(docs[z].jaccard(docs[indexOfProfile]) > threshold[t]){
						cntJaccard++;
					}//endjaccard
				}	
				for(int n=0;n<numberOfArticlesPerDay.length;n++){
					for(int i = 0;i<docsHeap.heapArray.length;i++){
						if(docsHeap.heapArray[i]!=docs[indexOfProfile]){
							if(tempDocHeap.heapArray[i].minhash(docs[indexOfProfile]) > threshold[t]){								
								for(int z = 0;z<10;z++){
									cntMinHashBeforeFinal = tempDocHeap.heapArray[i].minhash(docs[indexOfProfile]);
								}//επανυπολογισμός για όσες > threshold
							if(cntMinHashBeforeFinal > threshold[t]){
								for(int m=0;m<finalHeap.heapArray.length;m++){
									if(docsHeap.heapArray[i]==finalHeap.heapArray[m]){
										break;
									}
								}														
								finalHeap.insert(docsHeap.remove());
								cntMinHash++;
							}
							}//endminhash
						}
					}//end docs length
					for(int j = 0;j<numberOfArticlesPerDay[n];j++){
						articlesForUser[j] = finalHeap.remove();

					}
					
					y = Math.min(cntJaccard, 8000/numberOfArticlesPerDay[n]);
					accuracy = (double) cntMinHash/y;
					System.out.println("threshold = " +threshold[t]+ " n = " +numberOfArticlesPerDay[n]+ " accuracy = " +accuracy);
					System.out.println("minhash = " +cntMinHash+ " 8000/n = " +8000/numberOfArticlesPerDay[n]+ " cntJaccard = " +cntJaccard+ " y = " +y);
					cntMinHash = 0;
					
				}//end numberOfArticlesPerDay
				System.out.println(".....................................................");
				cntJaccard = 0;
			}//end threshold	
	}//end main
}//end heap