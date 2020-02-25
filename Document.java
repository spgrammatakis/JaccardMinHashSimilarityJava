import java.util.Random;
	//done
	class IntegerPair {
		private int a;
		private int b;
		
		public IntegerPair(int a, int b) {
			this.a = a; 
			this.b = b;
		}	
		
		public int getA() {
			return a;
		}
		public void setA(int a) {
			this.a = a;
		}
		public int getB() {
			return b;
		}
		public void setB(int b) {
			this.b = b;
		}
	}//end IntegerPair

	public class Document {
		private int id = -1;
		private int[] tokens = null;
		private String filename = "";
		private double sim = 0.0;
		private static Random rnd = new Random(System.currentTimeMillis());

		public Document(Integer[] tokens, String filename, int id) {
			if (tokens != null) {
				if (this.tokens == null)
					this.tokens = new int[tokens.length];
				for (int i=0 ; i < tokens.length ; i++) {
					this.tokens[i] = tokens[i];
				}
			}
			this.filename = filename;
			this.id = id;
		}//done

		public int getKey(){
			return id;
		}

		public void setKey(int key){
			id = key;
		}

		public double getSim(){
			return sim;//επιστροφή ομοιότητα κατά minhash
		}
		public int generateRandomNumber(int lowBound,int upBound){
			int a = rnd.nextInt(upBound) + lowBound;
			return a;		
		}
		
		private IntegerPair hashFuncGen() {	
			int lowBound = 1;
			int upBound = 10000;	
			int a = rnd.nextInt(upBound) + lowBound;
			int b = rnd.nextInt(upBound) + lowBound;
			IntegerPair newPair = new IntegerPair(a,b);
			return newPair;
		}
		
		private long hash(IntegerPair ip, int x) {
			int a = ip.getA();
			int b = ip.getB();
			long h = (a*x + b)%(long)52.981;
			return h;
		} 

		public double minhash(Document doc) {
			int x = 0;
			int k = 100;
			long h1 = 0;
			long h2 = 0;

			long hashValues[][] = new long[k][2];
			for(int i = 0;i<k;i++){
				IntegerPair newPair = hashFuncGen();//100 συναρτήσεις καταρμετισμού
				for(int j = 0;j<tokens.length;j++){
					hashValues[i][0] = hash(newPair, tokens[j]);

					if(j==0){
						h1 = hashValues[i][0];
					}else{
						h1 = Math.min(h1,hashValues[i][0]);//εύρεση ελάχιστης τιμης κατακερματσμού
					}
				}
				for(int j = 0;j<doc.tokens.length;j++){
					hashValues[i][1] = hash(newPair,doc.tokens[j]);
					if(j==0){
						h2=hashValues[i][1];
					}else{
						hashValues[i][1] = Math.min(h2, hashValues[i][1]);//εύρεση ελάχιστης τιμης κατακερματσμού
					}
					
				}
				if(h1==h2){x++;}
			}

			sim = (double) x/k;
			return sim;
		}//end minhash

		public double jaccard(Document doc) {
			int i = 0;
			int j = 0;
			int intersection = 0;
			for(i = 0;i<tokens.length;i++){
				for(j = 0; j < doc.tokens.length; j++){
					if(tokens[i] == doc.tokens[j]){
						intersection++;
					}
				}
			}
			double simJaccard = (double) intersection/(tokens.length + doc.tokens.length - intersection);
			return simJaccard;
		}//end jaccard
	}//end Document