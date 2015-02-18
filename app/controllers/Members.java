package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Members extends Controller
{
  public static void index()
  {
    User user = Accounts.getLoggedInUser();
    List<User> users = User.findAll();
    users.remove(user);
    for(User member: users)
    {
    	if((member.statusText == null)||(member.statusText.equals("")))
    	{
    		member.statusText = "No status";
    	}
    }    
    render(users, user);
  }
  
  public static void follow(Long id)
  {
    User user = Accounts.getLoggedInUser();
    User friend = User.findById(id);
    user.befriend(friend);
    Home.index();
  }
}