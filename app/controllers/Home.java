package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Home extends Controller
{
  public static void index()
  {
    User user = Accounts.getLoggedInUser();
    //OnlineUsers.addOnlineUser(user);
    //ArrayList<User> onlineUsers = OnlineUsers.getListOnline();
    //user.online = true;
    //user.save();
    render(user);
  }

  public static void drop(Long id)
  {
    User user   = Accounts.getLoggedInUser();    
    User friend = User.findById(id);
    user.unfriend(friend);
    Logger.info("Dropping " + friend.email);
    index();
  }  
}