import java.sql.SQLException
import java.util
import java.util._

class Controller(gmail: String, gmailPassword: String, dbUsername: String, dbPassword: String) {
  val sendMail: SendMail = new SendMail(gmail, gmailPassword)
  val queryTables: QueryTables = new QueryTables(dbUsername, dbPassword)

  /**
   * generates the body of the email using the coupons
   * @param coupons
   * @return
   */
  def generateText(coupons: java.util.ArrayList[String]): String = {
    if(coupons.size() == 0){
      return "<html><body><p>There are no coupons, scrub</p></body></html>"
    }
    val text = "<html><body>" +
    "<h1>Your coupons for the week are: </h1>" +
    "<p>" + generateTextHelper(coupons.size()-1, 0, coupons, "") + "</body></html>"
    text
  }

  /**
   * helper function for above function. uses recursion to avoid use of variable. barely different than using a for loop,
   * but it lets me pretend like i'm using functional programming :P
   * @param size
   * @param iteration
   * @param list
   * @param text
   * @return
   */
  private def generateTextHelper(size: Int, iteration: Int, list: java.util.ArrayList[String], text: String): String = {
    val coupon = list.get(iteration);
    val newText = text + coupon + "<br>"
    if(iteration < size){
      return generateTextHelper(size, iteration+1, list, newText)
    }
    return (newText + "</p>")
  }

  /**
   * for some reason, I decided to throw the sql exception up instead of handling it locally. historians for ages will debate
   * if I was an idiot, or if I saw something no one else did. I lean towards the former. oh anyways this gets all coupons
   * considered good
   * @return arraylist containing said coupons
   */
  def getGoodCoupons(): java.util.ArrayList[String] = {
    try{
      queryTables.getCoupons(quality.good)
    }
    catch {
      case sqlEx: SQLException => {
        println("SQL Error: " + sqlEx)
        return new util.ArrayList[String]()
      }
    }
  }

  /**
   * gets all coupons considered good and above
   * @return arraylist containing said coupons
   */
  def getBestCoupons(): java.util.ArrayList[String] = {
    try{
      val t = queryTables.getCoupons(quality.best)
      t.addAll(queryTables.getCoupons(quality.good))
      t
    }
    catch {
      case sqlEx: SQLException => {
        println("SQL Error: " + sqlEx)
        return new util.ArrayList[String]()
      }
    }

  }

  /**
   * gets all emails with rec_count less than 10. these emails will not recieve the best coupons
   * @return arraylist containing emails
   */
  def getGoodEmails(): java.util.ArrayList[String] = {
    try{
      queryTables.getOtherEmails()
    }
    catch {
      case sqlEx: SQLException => {
        println("SQL Error: " + sqlEx)
        return new util.ArrayList[String]()
      }
    }

  }

  /**
   * gets arraylist containing emails with rec_count>10
   * @return an array list
   */
  def getBestEmails(): java.util.ArrayList[String] = {
    try{
      queryTables.getBestEmails()
    }
    catch {
      case sqlEx: SQLException => {
        println("SQL Error: " + sqlEx)
        return new util.ArrayList[String]()
      }
    }

  }

  /**
   * sends emails to all in database. those with rec_count < 10 get access to good coupons. the others get access to good and best coupons
   */
  def sendEmailsToSubscribers(): Unit = {
    val bestText = generateText(getBestCoupons())
    val goodText = generateText(getGoodCoupons())

    val bestEmails = getBestEmails()
    val goodEmails = getGoodEmails()

    sendEmailsHelper(0, bestEmails.size()-1, bestText, bestEmails)
    sendEmailsHelper(0, goodEmails.size()-1, goodText, goodEmails)
  }

  /**
   * helper function that lets me pretend i know what functional programming is
   * @param iteration
   * @param size
   * @param text
   * @param emailList
   * @return
   */
  def sendEmailsHelper(iteration: Int, size: Int, text: String, emailList: java.util.ArrayList[String]): Boolean = {
    val email = emailList.get(iteration)
    val subject = "Your coupons for the week are here!"
    sendMail.send(recipient = email, subject = subject, text = text)
    if(iteration < size){
      return sendEmailsHelper(iteration+1, size, text, emailList)
    }
    else{
      return true;
    }
  }

}
