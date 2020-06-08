
import java.util.ArrayList;
import java.util.List;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Likes;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.Photo;
import com.restfb.types.Post;
import com.restfb.types.ProfilePictureSource;
import com.restfb.types.User;
import com.restfb.types.WorkExperience;
import com.restfb.types.User.Education;
import com.restfb.types.User.Experience;

/**
 * @author Michal Motylinski
 * @version v1.0
 * 
 * @description This class gets the actual data from users Facebook profile and
 *              passes them to the main class which prints the results out.
 */

public class DataGetter {
	/* Access token passed from the main class */
	private static String accessToken = FacebookCrawler.Token();
	@SuppressWarnings("deprecation")
	private static FacebookClient fbClient = new DefaultFacebookClient(accessToken);
	/* Permissions used for extraction of data */
	private static User me = fbClient.fetchObject("me", User.class,
			Parameter.with("fields",
					"name, id, about, email, picture, birthday, location, hometown, likes, currency,"
							+ " education, work, gender, link, locale, languages, political, quotes, relationship_status,"
							+ " religion, favorite_teams, favorite_athletes"));

	/* Short objects below gets single data types */
	protected String Name() {
		String name = me.getName();
		return name;
	}

	protected String About() {
		String name = me.getAbout();
		return name;
	}

	protected String Gender() {
		String gender = me.getGender();
		return gender;
	}

	protected String Email() {
		String email = me.getEmail();
		return email;
	}

	protected String Birthday() {
		String birthday = me.getBirthday();
		return birthday;
	}

	protected String RelationshipStatus() {
		String relationship = me.getRelationshipStatus();
		return relationship;
	}

	protected String Hometown() {
		String hometown = me.getHometownName();
		return hometown;
	}

	protected String Locale() {
		String locale = me.getLocale();
		return locale;
	}

	protected String Political() {
		String political = me.getPolitical();
		return political;
	}

	protected String Religion() {
		String religion = me.getReligion();
		return religion;
	}

	protected String Quotes() {
		String quotes = me.getQuotes();
		return quotes;
	}

	protected String Link() {
		String link = me.getLink();
		return link;
	}

	/* Get and clear non list data */
	protected String Picture() {
		String picture;
		ProfilePictureSource pictureList = me.getPicture();
		String pictureString = String.valueOf(pictureList);
		picture = pictureString.replaceAll(".*url=", "").replaceAll("width.*", "");
		return picture;
	}

	/* Get and clear non list data */
	protected String Location() {
		String location;
		NamedFacebookType locationList = me.getLocation();
		String locationString = String.valueOf(locationList);
		location = locationString.replace("NamedFacebookType[", "").replaceAll("metadata.*?name", "name")
				.replaceAll(" type.*?]", "").replace("=", " = ");
		return location;
	}

	/* Get and clear non list data */
	protected String Currency() {
		String currency;
		com.restfb.types.User.Currency currencyList = me.getCurrency();
		String currencyString = String.valueOf(currencyList);
		currency = currencyString.replaceAll(".*userCurrency=", "").replace("]", "");
		return currency;
	}

	/* Get and clear non list data */
	protected Long Friends() {
		Long totalCount = null;
		Connection<User> myFriends = fbClient.fetchConnection("me/friends", User.class,
				Parameter.with("fields", "id,name"));
		totalCount = myFriends.getTotalCount();
		return totalCount;
	}

	/*
	 * From that point every method gets and clears the lists of data which
	 * objects will be passed to the main class for printing
	 */
	protected ArrayList<String> Photos() {
		ArrayList<String> photosArrayList = new ArrayList<String>();
		Connection<Photo> photos = fbClient.fetchConnection("me/photos/uploaded", Photo.class);
		for (List<Photo> allPhotos : photos) {
			for (Photo photo : allPhotos) {
				photosArrayList.add("id = " + photo.getId() + " Creation Time = " + photo.getCreatedTime());
			}
		}
		return photosArrayList;
	}

	protected ArrayList<String> Posts() {
		ArrayList<String> postsArrayList = new ArrayList<String>();
		Connection<Post> posts = fbClient.fetchConnection("me/feed", Post.class);
		for (List<Post> myFeedPage : posts) {
			for (Post post : myFeedPage) {
				postsArrayList.add("id = " + post.getId() + " Creation Time = " + post.getCreatedTime() + " Message = "
						+ post.getMessage() + " Story = " + post.getStory());
			}
		}
		return postsArrayList;
	}

	protected String[] Education() {

		List<Education> educationList = me.getEducation();
		String educationString = String.valueOf(educationList);
		String[] educationLocalArray = educationString.split("], ");
		for (int i = 0; i < educationLocalArray.length; i++) {
			educationLocalArray[i] = educationLocalArray[i].replaceAll(".*id=", "id=").replaceAll(" with.*", "")
					.replace("type=null] ", "").replace("metadata=null ", "").replace("=", " = ");
		}
		return educationLocalArray;
	}

	protected String[] Languages() {
		List<Experience> languagesList = me.getLanguages();
		String languagesString = String.valueOf(languagesList);
		String[] languagesLocalArray = languagesString.split("], ");
		for (int i = 0; i < languagesLocalArray.length; i++) {
			languagesLocalArray[i] = languagesLocalArray[i].replaceAll(".*id=", "id=").replaceAll(" type.*", "")
					.replace("metadata=null ", "").replace("=", " = ");
		}
		return languagesLocalArray;
	}

	protected String[] Teams() {
		List<Experience> teamsList = me.getFavoriteTeams();
		String teamsString = String.valueOf(teamsList);
		String[] teamsLocalArray = teamsString.split("], ");
		for (int i = 0; i < teamsLocalArray.length; i++) {
			teamsLocalArray[i] = teamsLocalArray[i].replaceAll(".*id=", "id=").replaceAll(" type.*", "")
					.replace("metadata=null ", "").replace("=", " = ");
		}
		return teamsLocalArray;
	}

	protected String[] Athletes() {
		List<Experience> athletesList = me.getFavoriteAthletes();
		String athletesString = String.valueOf(athletesList);
		String[] athletesLocalArray = athletesString.split("], ");
		for (int i = 0; i < athletesLocalArray.length; i++) {
			athletesLocalArray[i] = athletesLocalArray[i].replaceAll(".*id=", "id=").replaceAll(" type.*", "")
					.replace("metadata=null ", "").replace("=", " = ");
		}
		return athletesLocalArray;
	}

	protected String[] Work() {
		List<WorkExperience> workList = me.getWork();
		String workString = String.valueOf(workList);
		String[] workLocalArray = workString.split("], ");
		for (int i = 0; i < workLocalArray.length; i++) {
			workLocalArray[i] = workLocalArray[i].replaceAll(".*employer", "").replaceAll("=Page.*?id=", "id=")
					.replaceAll("impressum.*?name=", "Company name=").replaceAll("nameW.*?endDate=", "endDate=")
					.replaceAll("from.*?positionid=", "positionid=").replaceAll("nameW.*", "").replaceAll("=", " = ");
		}
		return workLocalArray;
	}

	protected String[] Likes() {
		Likes listOfLikes = me.getLikes();
		String stringOfLikes = String.valueOf(listOfLikes);
		stringOfLikes = stringOfLikes.replace("Likes[canLike=null data=[", "");
		String[] likesLocalArray = stringOfLikes.split("],");
		for (int i = 0; i < likesLocalArray.length; i++) {
			likesLocalArray[i] = likesLocalArray[i].replace("LikeItem[", "")
					.replace("]] hasLiked=null totalCount=0]", "").replace("metadata=null ", "")
					.replace(" type=null", "").replace("=", " = ").trim();
		}
		return likesLocalArray;
	}
}