package invertPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ReadTerm {

	/**
	 * Read the content of cran.txt starting from I. and till it finds next I.
	 * write the document ID in the outFile.txt
	 */

	public void readCran(final File srcPath, String s) throws IOException {
		Scanner input = null;
		PrintWriter out = null;
		String word;
		String stemOut;
		Stemmer stemObj = new Stemmer();
		try {
			input = new Scanner(new FileReader(srcPath + "//input//cran.txt"));
			out = new PrintWriter(new FileWriter(srcPath
					+ "//output//outFile.txt"));
			File stopWordFile = new File(srcPath + "//input//stopWords.txt");
			/********* CREATE ARRAYLIST OF STOPWORDS FROM STOP.TXT START ********/
			if (!stopWordFile.exists()) {
				throw new RuntimeException("File Not Found");
			}
			BufferedReader reader = null;
			StringBuilder stopWords = new StringBuilder();
			try {
				reader = new BufferedReader(new FileReader(stopWordFile));
				String line;
				while ((line = reader.readLine()) != null) {
					stopWords.append(line + " ");
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			String[] arrStopWords = stopWords.toString().split("\\s+");
			HashMap<String, HashMap<Integer, Integer>> allTerms = new HashMap<String,  HashMap<Integer, Integer>>();
			HashMap<Integer, Integer> getDocTerm = new HashMap<Integer, Integer>();
			int docId = 0;
			int prevCountDoc = 0;
			/**
			 *  CREATE ARRAYLIST OF STOPWORDS FROM STOP.TXT STOP *******
			 *  */
			while (input.hasNext()) {
				word = input.next();
				/**	
				 * Content of a document
				 */
				if(!word.equals(s)){
					if (!Arrays.asList(arrStopWords).contains(word)) {
						stemOut = stemObj.steamWord(word); // stem this word
						/**
						 * Outer Hash Start
						 */
						if(!allTerms.containsKey(stemOut)){
							allTerms.put(stemOut, new HashMap<Integer, Integer>());
							allTerms.get(stemOut).put(docId, 1); 
						}else{
							//get value of the current hash map
							if(allTerms.get(stemOut).containsKey(docId)){
								prevCountDoc = allTerms.get(stemOut).get(docId);
							}else{
								prevCountDoc = 0;
							}
							allTerms.get(stemOut).put(docId, prevCountDoc+1);
						}
					}
				}else{
					/**
					 * New Document start with .I
					 */
					docId = input.nextInt();
					prevCountDoc = 0;
					getDocTerm.clear();
					
				}
			}
			Set<String> allTermsHashKey = allTerms.keySet();
			ArrayList<String> termsKeyArray = new ArrayList<String>();
			termsKeyArray.addAll(allTermsHashKey);
			Collections.sort(termsKeyArray);
			for (String term: termsKeyArray){
	            String termName =term.toString();
	            String docDetail = allTerms.get(termName).toString();  
	            out.println(termName+" => "+"Doc Count: "+ allTerms.get(termName).size()+" "+docDetail);
	            
			} 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			input.close();
			out.close();
		}
	}

	/**
	 * This method prints the terms in the file
	 * @param out - Output printwriter stream
	 * @param i - Count for the line
	 * @param termCount - Termcount map to keep track of the terms
	 */
	private void printTerms(PrintWriter out, int i,
			HashMap<String, Integer> termCount) {
		List<String> sortedKeys = new ArrayList<String>(termCount.keySet());
		if (i - 1 > 0) {
			out.println("Doc ID: " + (i - 1) + "\n"
					+ "Total Unique Term Count: " + Term.terms.size()); // Initially
																		// doc-id
																		// = 1
																		// term
																		// count
																		// 0;
		}
		Collections.sort(sortedKeys);
		for (String str : sortedKeys) {
			out.println("Term: " + str + " Count: "
					+ termCount.get(str).intValue());
		}
		Term.terms.clear();
		termCount.clear();
		sortedKeys.clear();
		out.flush();
	}

	/**
	 * it takes the word given by readCran() and checks if it is stopword if
	 * the passed "checkWord" is not stopword it returns the string which was
	 * passed
	 * 
	 * @param srcPath
	 * @param checkWord
	 * @return
	 */
	public String checkStopWord(final File srcPath, String checkWord) {
		File stopWordFile = new File(srcPath + "//input//stopWords.txt");
		if (!stopWordFile.exists()) {
			throw new RuntimeException("File Not Found");
		}
		BufferedReader reader = null;
		StringBuilder stopWords = new StringBuilder();
		try {
			reader = new BufferedReader(new FileReader(stopWordFile));
			String line;
			while ((line = reader.readLine()) != null) {
				stopWords.append(line + " ");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		String notStopWord = "";
		String[] arrStopWords = stopWords.toString().split("\\s+");
		if (!Arrays.asList(arrStopWords).contains(checkWord)) {
			notStopWord = checkWord;
		}
		return notStopWord;
	}

	public static void main(String[] args) throws IOException {
		Path currentRelativePath = Paths.get("");
		String getProjPath = currentRelativePath.toAbsolutePath().toString();
		final File srcPath = new File(getProjPath + "//src");
		ReadTerm readTermObj = new ReadTerm();
		readTermObj.readCran(srcPath, ".I");
	}
}
