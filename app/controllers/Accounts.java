package controllers;

import play.*;
import play.mvc.*;
import java.util.*;
import models.*;

public class Accounts extends Controller
{
  public static void signup()
  {
	if (session.contains("logged_in_userid"))
	{
		Home.index();
	}
	else
	{
		render();
	}
  }

  public static void login()
  {
	  if (session.contains("logged_in_userid"))
		{
			Home.index();
		}
		else
		{
			List<User> users = User.findAll();
			boolean noPost = Post.findAll().isEmpty();
			render(users, noPost);
		}
  }

  public static void logout()
  {
	String userId = session.get("logged_in_userid");
	User user = User.findById(Long.parseLong(userId));
	user.online = false;
	//user.fullAccess = false;
	user.save();
    session.clear();
    index();
  }

  public static void index()
  {
	  if (session.contains("logged_in_userid"))
		{
			Home.index();
		}
		else
		{
			render();
		}
  }

  public static User getLoggedInUser()
  {
    User user = null;
    if (session.contains("logged_in_userid"))
    {
      String userId = session.get("logged_in_userid");
      user = User.findById(Long.parseLong(userId));
      //user.fullAccess = true;
      user.online = true;
      user.save();
    }
    else
    {
      login();
    }
    return user;
  }
  
  public static void register(String firstName, String lastName, int age, String nationality, String email, String password, String password2)
  {
    Logger.info(firstName + " " + lastName + " " + email + " " + password);
    User user = new User(firstName, lastName, email, password, age, nationality);
    user.save();
    index();
  }

  public static void authenticate(String email, String password)
  {
    Logger.info("Attempting to authenticate with " + email + ":" + password);

    User user = User.findByEmail(email);
    if ((user != null) && (user.checkPassword(password) == true))
    {
      Logger.info("Authentication successful");
      session.put("logged_in_userid", user.id);
      //user.fullAccess = true;
      user.online = true;
      user.save();
      Home.index();
    }
    else
    {
      Logger.info("Authentication failed");
      login();
    }
  }
}