

import java.io.File;

public final class UtilsTestWordNet {
	private static final String FILE_DIR = "wordnet";
	private static final String SYN_1 = "synsets_1.txt";
	private static final String HYP_1 = "hypernyms_1.txt";
	private static final String REAL_SYN = "real_synsets.txt";
	private static final String REAL_HYP = "real_hypernyms.txt";
	
	private UtilsTestWordNet(){
	}

	public static String getSyn1(){
		return getFullFileName(SYN_1);
	}
	
	public static String getHyp1(){
		return getFullFileName(HYP_1);
	}
	
	public static String getRealSyn(){
		return getFullFileName(REAL_SYN);
	}
	
	public static String getRealHyp(){
		return getFullFileName(REAL_HYP);
	}
	
	public static String getFullFileName(String name){
		String fullFileName = FILE_DIR + File.separator + name;
		return UtilsTestWordNet.class.getClassLoader().getResource(fullFileName).getFile();
	}

}
