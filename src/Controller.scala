import java.sql.SQLException
import java.util
import java.util._

class Controller(gmail: String, gmailPassword: String, dbUsername: String, dbPassword: String) {
  val sendMail: SendMail = new SendMail(gmail, gmailPassword)
  val queryTables: QueryTables = new QueryTables(dbUsername, dbPassword)

  def generateText(coupons: java.util.ArrayList[String]): String = {
    if(coupons.size() == 0){
      return "<html><body><p>There are no coupons, scrub</p></body></html>"
    }
    val text = "<html><body>" +
    "<h1>Your coupons for the week are: </h1>" +
    "<p>" + generateTextHelper(coupons.size()-1, 0, coupons, "") + "</body></html>"
    text
  }
  private def generateTextHelper(size: Int, iteration: Int, list: java.util.ArrayList[String], text: String): String = {
    val coupon = list.get(iteration);
    val newText = text + coupon + "<br>"
    if(iteration < size){
      return generateTextHelper(size, iteration+1, list, newText)
    }
    return (newText + "</p>")
  }

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

  def sendEmailsToSubscribers(): Unit = {
    val bestText = generateText(getBestCoupons())
    val goodText = generateText(getGoodCoupons())

    val bestEmails = getBestEmails()
    val goodEmails = getGoodEmails()

    sendEmailsHelper(0, bestEmails.size()-1, bestText, bestEmails)
    sendEmailsHelper(0, goodEmails.size()-1, goodText, goodEmails)
  }

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
