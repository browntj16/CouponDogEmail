import java.sql.{ResultSet, SQLException}
import java.util

class QueryTables(username: String, password: String) {
    val emailTable = "sys.emails";
    val couponTable= "sys.coupons";
    val db: DBConnection = new DBConnection(userName = username, password = password);

    @throws[SQLException]("SQL Error")
    def getBestEmails(): util.ArrayList[String] = {
        val rs = db.executeQuery("SELECT email FROM " + emailTable + " WHERE rec_count>=10");
        if(!rs.next()){
            return new util.ArrayList[String]();
        }
        val list = new util.ArrayList[String]();
        resultSetToArrayList(rs, list, "email");
    }
  @throws[SQLException]("SQL Error")
  def getOtherEmails(): util.ArrayList[String] = {
    val rs = db.executeQuery("SELECT email FROM " + emailTable + " WHERE rec_count<10");
    if(!rs.next()){
      return new util.ArrayList[String]();
    }
    val list = new util.ArrayList[String]();
    resultSetToArrayList(rs, list, "email");
  }

  /**
   *
   * @param quality
   * @return
   */
  @throws[SQLException]("SQL Error")
  def getCoupons(quality: Int): util.ArrayList[String] = {
    val rs = db.executeQuery("SELECT coupon FROM " + couponTable + " WHERE quality="+quality);
    if(!rs.next()){ //if result set is empty
      return new util.ArrayList[String]();
    }
    val list = new util.ArrayList[String]();
    resultSetToArrayList(rs, list, "coupon");
  }


  /**
   * make sure you arent on the first result of resultset when you run this
   * @param rs
   * @param list
   */
    def resultSetToArrayList(rs: ResultSet, list: java.util.ArrayList[String], columnName: String): util.ArrayList[String] = {
        list.add(rs.getString(columnName));
        if(rs.next()){
          resultSetToArrayList(rs, list, columnName);
        }
        else{
          rs.close(); //side effect
          list;
        }

    }

}