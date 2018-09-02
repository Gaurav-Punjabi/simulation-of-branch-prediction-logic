import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.File;
import java.util.Map;
import java.util.HashMap;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.io.IOException;


/**
 * This class is used to calculated the accuracy of the branch prediction logic of Pentium Processor for the given source code of C.
 */
public class PredictionCalculator {

	private static final String PRINTF_REGEX = "printf\\(.+\\);";
	private static final String FOR_REGEX = "for\\(.+\\)\\s*\\{";
	private static final String IF_REGEX = "if\\(.*\\)\\s*\\{";
	private static final String ELSE_IF_REGEX = "else if\\s*\\(.*\\)\\s*\\{";
	private static final String ELSE_REGEX = "else\\s*\\{";
	private static final String WHILE_REGEX = "while\\(.*\\)\\s*\\{";

	private String path;

	private List<HistoryBit> historyBits;
	
	public PredictionCalculator(final String path) {
		this.path = path;
	}

	public double getPredictionAccuracy()throws Exception{
		String sourceCode = readFileContents(this.path);
		
		this.historyBits = new ArrayList<>();

		if(sourceCode == null) 
			return -1;
		sourceCode = configureCode(sourceCode);

		writeCode("Temp.c", sourceCode);

		ProcessBuilder compileProcess = new ProcessBuilder("clang","Temp.c","-o","Test");
		Process compile = compileProcess.start();
		compile.waitFor();
		
		ProcessBuilder runProcess = new ProcessBuilder("./Test");
		Process run = runProcess.start();
		run.waitFor();

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(run.getInputStream()));
		String temp = "";
		Iterator<HistoryBit> iterator = this.historyBits.iterator();
		int lastId;
		while((temp = bufferedReader.readLine()) != null) {
			System.out.println("temp = " + temp);
			HistoryBit historyBit = iterator.next();

			while(historyBit.getId() != Integer.parseInt(temp)) {
				historyBit.updateBit(false);
				if(!iterator.hasNext())
					iterator = this.historyBits.iterator();
				historyBit = iterator.next();
			}
			historyBit.updateBit(true);
			if(!iterator.hasNext())
					iterator = this.historyBits.iterator();
		}
		for(HistoryBit historyBit : historyBits) {
			System.out.println("historyBit.getBit() = " + historyBit.getBit());
		}
		return this.getPercentage();
	}

	private double getPercentage() {
		int size = this.historyBits.size() * 3;
		int score = 0;
		for(HistoryBit historyBit : this.historyBits) {
			score += historyBit.getBit();
		}
		return (((double)score/(double)size) * 100.00);
	}

	private void writeCode(final String fileName,
						   final String code) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(fileName);
			fileOutputStream.write(code.getBytes());
			fileOutputStream.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	private String configureCode(final String source) {
		String updatedCode = source.replaceAll(PRINTF_REGEX, "");
		ArrayList<Matcher> matchers = new ArrayList<>();
		matchers.add(Pattern.compile(IF_REGEX).matcher(updatedCode));
		matchers.add(Pattern.compile(ELSE_IF_REGEX).matcher(updatedCode));
		matchers.add(Pattern.compile(ELSE_REGEX).matcher(updatedCode));
		matchers.add(Pattern.compile(FOR_REGEX).matcher(updatedCode));
		matchers.add(Pattern.compile(WHILE_REGEX).matcher(updatedCode));

		int id = 0;

		for(Matcher matcher : matchers) {
			int offset = 0;
			while(matcher.find()) {
				String stringToInsert = "\nprintf(\"" +  id++ + "\\n\");";
				int start = matcher.start(0) + offset;
				int end = matcher.end(0) + offset;
				updatedCode = updatedCode.substring(0, start) + matcher.group(0) + stringToInsert + updatedCode.substring(end);
				offset += stringToInsert.length();
			}
		}
		for(int i=0;i<id;i++) {
			this.historyBits.add(new HistoryBit(i));
		}
		return updatedCode;
	}
	
	private String readFileContents(final String path) {
		try {
			String sourceCode = new String(Files.readAllBytes(new File(path).toPath()));
			return sourceCode;
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args)throws Exception {
		if(args.length == 0) {
			System.out.println("Please pass the path to the program that needs to be tested..");
			return;
		}
		String path = arg[0];
		if(!new File(path).isFile()) {
			System.out.println("The given path is incorrect please check the path.");	
			return;
		}
		PredictionCalculator predictionCalculator = new PredictionCalculator(path);
		double percentage = predictionCalculator.getPredictionAccuracy();
		System.out.println("The accuracy of the branch prediction logic for the given code is " + percentage);
	}
}
