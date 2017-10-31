import java.io.*;
import java.util.*;
import java.net.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import java.text.*;
import static java.lang.Math.toIntExact;

/*
 * To execute Java, please define "static void main" on a class
 * named Solution.
 *
 * If you need more classes, simply define them inline.
 */

class Solution {
  
  private static final String apiKey = "9Jz6tLIeJ0yY9vjbEUWaH9fsXA930J9hspPchute";
  private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
  
  
  public static void main(String[] args) throws Exception {
     // Default lat long 
     flyby(1.5,100.75);

      // Errors out as latitude is > 90
     flyby(100.75,1.5);
    
      // Errors out as longitude is > 180
     flyby(1.5,190.5);
    
     // LWSN CS building coords
     flyby(40.427729, -86.916974);
    
     // Empire State Building Coords
     flyby(40.748668, -73.985720);
  }
  
  
  public static void flyby(double latitude, double longitude) throws Exception{
    Date[] history;
    Date prediction;
    Date today = new Date();
    long numElements = 0;
    long diffTotal = 0;
    Date prevObjDate;
    Date lastObjDate;
    
    System.out.println("=======================================");
    
    System.out.println("Lat: " + latitude + ", Lon: " + longitude);
    
    if(latitude > 90 || latitude < -90){
      System.out.println("Latitude is incorrect.");
      System.out.println("=======================================");     
      System.out.println();
      return;
    }
    if(longitude > 180 || longitude < -180){
      System.out.println("Longitude is incorrect.");
      System.out.println("=======================================");      
      System.out.println();
      return;
    }
    
    JSONParser parser = new JSONParser();
    
    String data_string = getHTML("https://api.nasa.gov/planetary/earth/assets?lon=" + longitude + "&lat=" + latitude + "&begin=2014-01-01&api_key=" + apiKey);
    
    JSONObject data = (JSONObject) parser.parse(data_string);
    
    if(data.get("error") != null){
      System.out.println("Coordinates do not have any data, with the following error:");
      System.out.println(data.get("error"));
      System.out.println("=======================================");
      System.out.println();
      
      return;
    }
    
    numElements = (long) data.get("count");
    JSONArray arr = (JSONArray) data.get("results");
    
    Iterator<?> iterator =  arr.iterator();
    if(!iterator.hasNext()){
      System.out.println("Coordinates do not have any data");
    System.out.println("=======================================");
      return;
    }
    
    JSONObject firstObj = (JSONObject) iterator.next();
    
    prevObjDate = df.parse((String) firstObj.get("date"));
    lastObjDate = prevObjDate;
    
    while (iterator.hasNext()) {
      JSONObject obj = (JSONObject) iterator.next();
      lastObjDate = df.parse((String) obj.get("date"));
      
      long diff = Math.abs(lastObjDate.getTime() - prevObjDate.getTime());
      diffTotal += diff;
      
      prevObjDate = lastObjDate;
    }
    
    System.out.println("Last Entry Date " + lastObjDate);
    int diffDays = toIntExact(diffTotal / (24 * 60 * 60 * 1000));
    
    int delta = diffDays / toIntExact(numElements);
    
    System.out.println("Delta " + delta);
    
    Calendar c = Calendar.getInstance();
    c.setTimeInMillis(lastObjDate.getTime());
    
    // while date is in past =>
    while(c.getTime().before(today)){
      c.add(Calendar.DATE, delta);
    }
    
    prediction = c.getTime();
        
    System.out.println("Next Flyby: " + prediction);
    System.out.println("=======================================");
    System.out.println();
  }
    
  public static String getHTML(String urlToOpen) throws Exception {
    try{
      URL url = new URL(urlToOpen);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");
      
      BufferedReader in = new BufferedReader(
      new InputStreamReader(con.getInputStream()));
      String inputLine;
      StringBuffer content = new StringBuffer();
      while ((inputLine = in.readLine()) != null) {
          content.append(inputLine);
      }
      in.close();
      
      return content.toString();
      
    } catch (Exception e){
      return "{}";
    }

   }
  
}


/* 
Your previous Plain Text content is preserved below:

===== Preface =====

This question is very difficult in C and C++, where there is
insufficient library support to answer it in an hour. If you
prefer to program in one of those languages, please ask us to
provide you with a question designed for those languages instead!


===== Intro =====

Here at Delphix, we admire NASA’s engineering mission. But beyond
that, we can use data from NASA to make predictions about the
future. Solving global warming is unfortunately outside the scope
of an interview question, so your goal is somewhat simpler: use
NASA’s public HTTP APIs to create a function which predicts the
next time in the future a satellite image will be taken of a certain location.
This can be handy if you're trying to get your picture onto online
mapping applications like Google Maps. :-)

You should implement this in whatever language you're most
comfortable with -- just make sure your code is production quality,
well designed, and easy to read.

Finally, please help us by keeping this question and your
answer secret so that every candidate has a fair chance in
future Delphix interviews. Thank you!


===== Steps =====

1.  Choose the language you want to code in from the menu
    labeled "Plain Text" in the top right corner of the
    screen. You will see a "Run" button appear on the top
    left -- clicking this will send your code to a Linux
    server and compile / run it. Output will appear on the
    right side of the screen.
    
    For information about what libraries are available for
    your chosen language, see:

      https://coderpad.io/languages

2.  Pull up the documentation for the API you'll be using:

      https://api.nasa.gov/api.html

3.  You'll need an API key in order to query the data from
    NASA. You can use the one that we created:

      9Jz6tLIeJ0yY9vjbEUWaH9fsXA930J9hspPchute

4.  Implement a function flyby() whose method signature
    looks like this (can differ slightly depending on the
    language you chose):

      void flyby(double latitude, double longitude)

    When there is enough data to do so, the function should
    print a prediction for when the next picture will be taken.
    The time returned should be in the future, even if the times
    returned by NASA are out of date.
    The prediction should have a date and time based on the
    average time between successive pictures. In pseudocode, the
    prediction method might look like:

      print "Next time: " + (last_date + avg_time_delta)

    You can use the https://api.nasa.gov/api.html#assets API
    to get the information you will need to compute this.
    Note that the NASA documentation mentions that
    avg_time_delta is usually close to 16 days, but we'd
    like you to calculate it since it's not always the same.

    If you want to change the function signature to deal
    with error conditions or some other complexity not
    captured by the one above, go for it! Just add a comment
    telling us what you changed and why.

5.  Add any tests for your code to the main() method of
    your program so that we can easily run them.


====== FAQs =====

Q:  How do I know if my solution is correct?
A:  Make sure you've read the prompt carefully and you're
    convinced your program does what you think it should
    in the common case. If your program does what the prompt 
    dictates, you will get full credit. We do not use an
    auto-grader, so we do not have any values for you to
    check correctness against.
    
Q:  What is Delphix looking for in a solution?
A:  After submitting your code, we'll have a pair of engineers
    evaluate it and determine next steps in the interview process.
    We are looking for correct, easy-to-read, robust code.
    Specifically, ensure your code is idiomatic and laid out
    logically. Ensure it is correct. Ensure it handles all edge
    cases and error cases elegantly.
    
Q:  How should my output be formatted?
A:  Your output should include a date and time in whatever
    format you find easiest. There are no other strict formatting
    constraints (we just inspect the output for correctness).

Q:  Any suggestions of fun locations I can test with?
A:  Sure! Here are a few:

    Fun location           Latitude    Longitude
    ---------------------  ----------  ------------
    Grand Canyon           36.098592   -112.097796
    Niagra Falls           43.078154   -79.075891
    Four Corners Monument  36.998979   -109.045183
    Delphix San Francisco  37.7937007  -122.4039064

Q:  If I need a clarification, who should I ask?
A:  Send all questions to the email address that sent you
    this document, and an engineer at Delphix will get
    back to you ASAP (we're pretty quick during normal
    business hours).

Q:  How long should this question take me?
A:  Approximately 1 hour, but it could take more or less
    depending on your experience with web APIs and the
    language you choose.

Q:  When is this due?
A:  We will begin grading your answer 24 hours after it is
    sent to you, so that is the deadline.

Q:  How do I turn in my solution?
A:  Anything you've typed into this document will be saved.
    Email us when you are done with your solution. We will
    respond confirming we've received the solution within
    24 hours.

Q:  Can I use any external resources to help me?
A:  Absolutely! Feel free to use any online resources you
    like, but please don't collaborate with anyone else.

Q:  Can I use my favorite library in my program?
A:  Unfortunately, there is no way to load external
    libraries into CoderPad, so you must stick to what
    they provide out of the box for your language (although
    they do support for many popular general-use libraries):

      https://coderpad.io/languages

    If you really want to use something that's not
    available, email the person who sent you this link
    and we will work with you to find a solution.

Q:  Why does my program terminate unexpectedly in
    CoderPad, and why can't I read from stdin or pass
    arguments on the command line?
A:  CoderPad places a limit on the runtime and amount of
    output your code can use, but you should be able to
    make your code fit within those limits. You can hard
    code any arguments or inputs to the program in your
    main() method or in your tests.

Q:  I'm a Vim/Emacs fan -- is there any way to use those
    keybindings? What about changing the tab width? Font
    size?
A:  Yes! Hit the button at the bottom of the screen that
    looks like a keyboard.
 */