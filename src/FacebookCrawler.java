
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.User;

/**
 * @author Michal Motylinski
 * @version v1.0
 * 
 * @description This is the main class that prints out and saves to the file the results of crawling
 */

public class FacebookCrawler {
	// static variables
	// Specifies how Facebook Graph API client must operate
	private static FacebookClient fbClient;
	// An array that contains all of the possible permissions
	private static String[] permissionsArray;
	// An array storing output information that will be written in a file
	private static String[] extractionArray;
	// Output txt file
	private static PrintWriter file;
	// Variable storing parameters for facebook client
	private static String accessToken;
	private static User me;
	// Line seperating new output from the old one
	private static String separator = new String(new char[300]).replace("\0", "-");
	// Scanner for users input
	private static Scanner input = new Scanner(System.in);

	public static void main(String[] args) throws IOException {
		System.out.println("Please follow the link below, log on your facebook account, then click 'Get Token' > "
				+ "'Get User Access Token' > check all of the boxes > allow to gather your personal info while pop up windows will come up."
				+ " After that just copy the access token and paste it below in a console");
		System.out.println("https://developers.facebook.com/tools/explorer/145634995501895");

		System.out.println("\nProvide access token:");
		accessToken = input.next();
		// call methods of authentication token, all available permissions
		Token();
		Permissions();
		// below line stores all the permission for current user in variable
		// which will be used later for getting an actual data
		me = fbClient.fetchObject("me", User.class,
				Parameter.with("fields",
						"name, id, about, email, picture, birthday, location, hometown, likes, currency,"
								+ " education, work, gender, link, locale, languages, political, quotes, relationship_status,"
								+ " religion, favorite_teams, favorite_athletes"));
		// call method with output style
		Output();
		String choice = "";
		do {
			System.out.println("--MAIN MENU--");
			System.out.println("1 - Specify what data you would like to retrieve");
			System.out.println("2 - Get all facebook profile data");
			System.out.println("Q - Quit Facebook Crawler Menu");
			System.out.println("Pick: ");
			choice = input.next().toUpperCase();
			// switch statement for menu
			switch (choice) {
			case "1": {
				customizedCrawl();
				break;
			}
			case "2": {
				crawlAll();
				break;
			}
			}
		} while (!choice.equals("Q"));
		System.out.println("GOODBYE");

	}

	// User is asked for getting a token and when he provide it, Token method
	// will provide access to the users account data
	@SuppressWarnings("deprecation")
	static String Token() {
		fbClient = new DefaultFacebookClient(accessToken);
		return accessToken;
	}

	private static void customizedCrawl() throws IOException {
		// list of commands that user can use to print out appropriate results
		System.out.println("*********************************************************************************");
		System.out.println("*\t\t\t\t-=List of commands=-\t\t\t\t*");
		System.out.println("* name about email picture birthday location hometown likes currency education  *");
		System.out.println("* work gender link locale languages political quotes religion relationship \t*");
		System.out.println("* teams athletes photos work posts \t\t\t\t\t\t*");
		System.out.println("*********************************************************************************");
		// User specifies what data program should get
		System.out.println(
				"Please specify what data you would like to extract (Format of an input: object1,object2,object3...)");
		String dataToExtraction = input.next();
		dataToExtraction = dataToExtraction.replace(" ", "");
		// Saving users input in an array in proper format for console output
		String[] userArray = dataToExtraction.split(",");
		for (int i = 0; i < userArray.length; i++) {
			for (int j = 0; j < permissionsArray.length; j++) {
				if (userArray[i].equals(permissionsArray[j])) {
				}
			}
		}

		/*
		 * Saving users input in an array in proper format for file output upper
		 * case is used to compare users input to output lines from Output
		 * method
		 */
		String[] upperCaseUserArray = dataToExtraction.toUpperCase().split(",");
		ArrayList<String> singleResultsArray = new ArrayList<String>();

		for (int i = 0; i < upperCaseUserArray.length; i++) {
			for (int j = 0; j < extractionArray.length; j++) {
				if (extractionArray[j].contains(upperCaseUserArray[i])) {
					/*
					 * print results for single object data in console and save
					 * them to array
					 */
					System.out.println(extractionArray[j]);
					singleResultsArray.add(extractionArray[j]);
				}
			}
		}
		/*
		 * File writer that checks if a file for that user already exist and if
		 * yes then it append results to that file, but if not then program will
		 * create a new file and store results in it. Parameter "true" means
		 * that file is opened in append mode which allows to add data without
		 * overwriting the one that already exist
		 */
		File oldFile = new File("fbCrawler_" + me.getId() + ".txt");
		if (oldFile.exists() && !oldFile.isDirectory()) {
			file = new PrintWriter(new FileWriter(oldFile, true));
			BufferedWriter writetoFile = new BufferedWriter(file);
			Header();
			/* save single object data in a file */
			for (String printArray : singleResultsArray) {
				file.println(printArray);
			}
			/* Below code compares multi object lists of data */
			for (int i = 0; i < upperCaseUserArray.length; i++) {
				if (upperCaseUserArray[i].contains("LIKES")) {
					Likes();
				} else if (upperCaseUserArray[i].contains("EDUCATION")) {
					Education();
				} else if (upperCaseUserArray[i].contains("LANGUAGES")) {
					Languages();
				} else if (upperCaseUserArray[i].contains("TEAMS")) {
					Teams();
				} else if (upperCaseUserArray[i].contains("ATHLETES")) {
					Athletes();
				} else if (upperCaseUserArray[i].contains("PHOTOS")) {
					Photos();
				} else if (upperCaseUserArray[i].contains("WORK")) {
					Work();
				} else if (upperCaseUserArray[i].contains("LIKES")) {
					Likes();
				} else if (upperCaseUserArray[i].contains("POSTS")) {
					Posts();
				}
			}
			writetoFile.write(separator + System.getProperty("line.separator"));
			writetoFile.close();
		} else {
			File newFile = new File("fbCrawler_" + me.getId() + ".txt");
			file = new PrintWriter(new FileWriter(newFile));
			BufferedWriter writetoFile = new BufferedWriter(file);
			Header();
			for (String printArray : singleResultsArray) {
				file.println(printArray);
			}
			for (int i = 0; i < upperCaseUserArray.length; i++) {
				if (upperCaseUserArray[i].contains("LIKES")) {
					Likes();
				} else if (upperCaseUserArray[i].contains("EDUCATION")) {
					Education();
				} else if (upperCaseUserArray[i].contains("LANGUAGES")) {
					Languages();
				} else if (upperCaseUserArray[i].contains("TEAMS")) {
					Teams();
				} else if (upperCaseUserArray[i].contains("ATHLETES")) {
					Athletes();
				} else if (upperCaseUserArray[i].contains("PHOTOS")) {
					Photos();
				} else if (upperCaseUserArray[i].contains("WORK")) {
					Work();
				} else if (upperCaseUserArray[i].contains("LIKES")) {
					Likes();
				} else if (upperCaseUserArray[i].contains("POSTS")) {
					Posts();
				}
			}
			writetoFile.write(separator + System.getProperty("line.separator"));
			writetoFile.close();
		}
	}

	private static void crawlAll() throws IOException {

		/* Print all single object data that can be extracted in console */
		for (int j = 0; j < extractionArray.length; j++) {
			System.out.println(extractionArray[j]);
		}

		File oldFile = new File("fbCrawler_" + me.getId() + ".txt");
		if (oldFile.exists() && !oldFile.isDirectory()) {
			file = new PrintWriter(new FileWriter(oldFile, true));
			BufferedWriter writetoFile = new BufferedWriter(file);

			Header();
			for (String printArray : extractionArray) {
				file.println(printArray);
			}
			Education();
			Languages();
			Teams();
			Athletes();
			Photos();
			Work();
			Likes();
			Posts();
			writetoFile.write(separator + System.getProperty("line.separator"));
			writetoFile.close();
		} else {
			File newFile = new File("fbCrawler_" + me.getId() + ".txt");
			file = new PrintWriter(new FileWriter(newFile));
			BufferedWriter writetoFile = new BufferedWriter(file);
			Header();
			for (String printArray : extractionArray) {
				file.println(printArray);
			}
			Education();
			Languages();
			Teams();
			Athletes();
			Photos();
			Work();
			Likes();
			Posts();
			writetoFile.write(separator + System.getProperty("line.separator"));
			writetoFile.close();
		}
	}

	private static void Permissions() {

		/* An array that stores all the permissions that our program is using */
		permissionsArray = new String[] { "name", "id", "about", "email", "picture", "birthday", "location", "hometown",
				"likes", "currency", "education", "work", "gender", "link", "locale", "languages", "political",
				"quotes", "relationship_status", "religion", "favorite_teams", "favorite_athletes" };
	}

	private static void Output() {

		/*
		 * Program gets single object data. Format of the output is defined
		 * here.
		 */
		String name = "NAME: \t\t\t" + new DataGetter().Name();
		String about = "ABOUT: \t\t\t" + new DataGetter().About();
		String gender = "GENDER: \t\t" + new DataGetter().Gender();
		String email = "EMAIL: \t\t\t" + new DataGetter().Email();
		String picture = "PICTURE: \t\t" + new DataGetter().Picture();
		String birthday = "BIRTHDAY: \t\t" + new DataGetter().Birthday();
		String relationship_status = "RELATIONSHIP STATUS: \t" + me.getRelationshipStatus();
		String hometown = "HOMETOWN: \t\t" + new DataGetter().Hometown();
		String location = "LOCATION: \t\t" + new DataGetter().Location();
		String locale = "LOCALE: \t\t" + new DataGetter().Locale();
		String political = "POLITICAL: \t\t" + new DataGetter().Political();
		String religion = "RELIGION: \t\t" + new DataGetter().Religion();
		String quotes = "QUOTES: \t\t" + new DataGetter().Quotes();
		String currency = "CURRENCY: \t\t" + new DataGetter().Currency();
		String link = "LINK: \t\t\t" + new DataGetter().Link();
		String friends = "FRIENDS COUNT: \t\t" + new DataGetter().Friends();
		/* Output format is stored in the array below */
		extractionArray = new String[] { name, about, gender, email, picture, birthday, relationship_status, hometown,
				location, locale, political, religion, quotes, currency, link, friends };

	}

	/* Layout of the file output header */
	private static void Header() {
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern("HH-mm-ss");
		LocalTime localTime = LocalTime.now();
		String time = pattern.format(localTime);
		String stars = new String(new char[300]).replace("\0", "*");
		file.println(stars);
		file.println("\t\t\t\t\t\t\t\t\t\t\t\t\tUSER ID: " + me.getId() + "");
		file.println("\t\t\t\t\t\t\t\t\t\t\t\t\tTIME: " + LocalDate.now() + " " + time);
		file.println(stars);
	}

	/*
	 * From that point every method print to file data of lists of objects, each
	 * method is a different item and each item can have 0 or more objects that
	 * are printed in an individual line
	 */

	private static void Photos() {
		ArrayList<String> photosArrayList = new DataGetter().Photos();
		System.out.println("UPLOADED PHOTOS: ");
		for (String printArray : photosArrayList) {
			System.out.println(printArray);
		}
		file.println("");
		file.println("UPLOADED PHOTOS:");
		for (String printArray : photosArrayList) {
			file.println(printArray);
		}
	}

	private static void Posts() {
		ArrayList<String> postsArrayList = new DataGetter().Posts();
		System.out.println("POSTS: ");
		for (String printArray : postsArrayList) {
			System.out.println(printArray);
		}
		file.println("");
		file.println("POSTS:");
		for (String printArray : postsArrayList) {
			file.println(printArray);
		}
	}

	private static void Education() {
		String[] crawledData = new DataGetter().Education();
		ArrayList<String> educationArrayList = new ArrayList<String>();
		String[] educationLocalArray = crawledData;
		System.out.println("EDUCATION: ");
		for (int i = 0; i < educationLocalArray.length; i++) {
			System.out.println(educationLocalArray[i]);
		}
		for (int i = 0; i < educationLocalArray.length; i++) {
			educationArrayList.add(educationLocalArray[i]);
		}
		file.println("");
		file.println("EDUCATION:");
		for (String printArray : educationArrayList) {
			file.println(printArray);
		}
	}

	private static void Languages() {
		String[] crawledData = new DataGetter().Languages();
		ArrayList<String> languagesArrayList = new ArrayList<String>();
		String[] languagesLocalArray = crawledData;
		System.out.println("LANGUAGES: ");
		for (int i = 0; i < languagesLocalArray.length; i++) {
			System.out.println(languagesLocalArray[i]);
		}
		for (int i = 0; i < languagesLocalArray.length; i++) {
			languagesArrayList.add(languagesLocalArray[i]);
		}
		file.println("");
		file.println("LANGUAGES:");
		for (String printArray : languagesArrayList) {
			file.println(printArray);
		}
	}

	private static void Teams() {
		String[] crawledData = new DataGetter().Teams();
		ArrayList<String> teamsArrayList = new ArrayList<String>();
		String[] teamsLocalArray = crawledData;
		System.out.println("TEAMS: ");
		for (int i = 0; i < teamsLocalArray.length; i++) {
			System.out.println(teamsLocalArray[i]);
		}
		for (int i = 0; i < teamsLocalArray.length; i++) {
			teamsArrayList.add(teamsLocalArray[i]);
		}
		file.println("");
		file.println("FAVOURITE TEAMS:");
		for (String printArray : teamsArrayList) {
			file.println(printArray);
		}
	}

	private static void Athletes() {
		String[] crawledData = new DataGetter().Athletes();
		ArrayList<String> athletesArrayList = new ArrayList<String>();
		String[] athletesLocalArray = crawledData;
		System.out.println("ATHLETES: ");
		for (int i = 0; i < athletesLocalArray.length; i++) {
			System.out.println(athletesLocalArray[i]);
		}
		for (int i = 0; i < athletesLocalArray.length; i++) {
			athletesArrayList.add(athletesLocalArray[i]);
		}
		file.println("");
		file.println("FAVOURITE ATHLETES:");
		for (String printArray : athletesArrayList) {
			file.println(printArray);
		}
	}

	private static void Work() {
		String[] crawledData = new DataGetter().Work();
		ArrayList<String> workArrayList = new ArrayList<String>();
		String[] workLocalArray = crawledData;
		System.out.println("WORK: ");
		for (int i = 0; i < workLocalArray.length; i++) {
			System.out.println(workLocalArray[i]);
		}
		for (int i = 0; i < workLocalArray.length; i++) {
			workArrayList.add(workLocalArray[i]);
		}
		file.println("");
		file.println("WORK:");
		for (String printArray : workArrayList) {
			file.println(printArray);
		}
	}

	private static void Likes() {
		String[] crawledData = new DataGetter().Likes();
		ArrayList<String> likesArrayList = new ArrayList<String>();
		String[] likesLocalArray = crawledData;
		System.out.println("LIKES: ");
		for (int i = 0; i < likesLocalArray.length; i++) {
			System.out.println(likesLocalArray[i]);
		}
		for (int i = 0; i < likesLocalArray.length; i++) {
			likesArrayList.add(likesLocalArray[i]);
		}
		file.println("");
		file.println("LIKES:");
		for (String printArray : likesArrayList) {
			file.println(printArray);
		}
	}
}