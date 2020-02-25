import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class ReutersParser 
{		
	private String PATH = null;
	private int cntWord;
	private HashMap<String, Integer> mapWord2Id = null;
	
	public ReutersParser(String sPath) {
		PATH = sPath;
		cntWord = 0;
		mapWord2Id = new HashMap<String, Integer>();
	}
	
	public Document[] parse() {
		int nDocId = -1;
		File folder = new File(PATH);
		int nDocs = countFiles(folder);
		System.out.println("Number of documents: "+nDocs);
		Document[] arDocs = new Document[nDocs];
		File[] listOfFolders = folder.listFiles(File::isDirectory);
		for (int i=0 ; i < listOfFolders.length ; i++) {
			File[] listOfFiles = listOfFolders[i].listFiles();
			for (int j=0 ; j < listOfFiles.length ; j++) {
				if (listOfFiles[j].isFile()) {
					nDocId++;
					//System.out.println("Parsing file: "+nDocId+". "+listOfFiles[j].getName());
					Document d = parseFile(listOfFiles[j], nDocId);
					arDocs[nDocId] = d;
					//System.out.println(d);
				}
			}
		}
		return arDocs;
	}

	private Document parseFile(File f, int nDocId) {
		ArrayList<Integer> arTokens = new ArrayList<Integer>();
		String sLine = "", sWord = "";
		try {
			BufferedReader br = new BufferedReader(
					new FileReader(f.getAbsoluteFile()));
			while ((sLine = br.readLine()) != null) {     
				sLine = sLine.trim();
				StringTokenizer st = new StringTokenizer(sLine, " /\t.!,;?\"\'()");
				while (st.hasMoreTokens()) {
					sWord = st.nextToken().toLowerCase();
					int iWord = word2id(sWord);
					//System.out.println(iWord+" "+sWord);
					if (!arTokens.contains(iWord))
						arTokens.add(iWord);
				}
			}
			br.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		Document doc = new Document(
				(Integer[])arTokens.toArray(new Integer[arTokens.size()]),
				f.getAbsoluteFile().getAbsolutePath(),
				nDocId);
		return doc;
	}

	private int countFiles(File directory) {
	    int count = 0;
	    for (File file : directory.listFiles()) {
	        if (file.isDirectory()) {
	            count += countFiles(file); 
	        }
	        else
	        		count++;
	    }
	    return count;
	}
	
	private int word2id(String sWord) {
		if (mapWord2Id != null) {
			if (mapWord2Id.containsKey(sWord))
				return mapWord2Id.get(sWord);
			else {
				mapWord2Id.put(sWord, ++cntWord);
				return cntWord;
			}
		}
		else
			return -1;
	}
	
}
