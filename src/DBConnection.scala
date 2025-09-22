import java.sql._;

class DBConnection(userName: String, password: String) {
    //Hardcoded since I'm not changing this
    val url: String = "jdbc:oracle:thin:@localhost:1521:free";
    //THIS CAN THROW AN SQLException
    //Innately, this value is, itself, impure.
    val connection: Connection = DriverManager.getConnection(url, userName, password);

    /**
     * these twin functions are both inherently impure. accessing a database is, inherently impure. the query can vary based
     * on when it is executed. both are just quick and dirty ways to execute query or update.
     * @param query
     * @throws SQLException
     * @return
     */
    @throws[SQLException]("SQL Error")
    def executeQuery(query: String): ResultSet = {
        val statement: Statement = connection.createStatement();
        //statement.close();
        return statement.executeQuery(query);
    }

    /**
     *
     * @param query
     * @throws SQLException
     * @return Result set
     */
    @throws[SQLException]("SQL Error")
    def executeUpdate(query: String): ResultSet = {
        val statement: Statement = connection.createStatement();
        //statement.close();
        return statement.executeQuery(query);
    }

    /**
     * again, inherently impure
     */
    @throws[SQLException]("SQL Error")
    def closeConnection(): Unit = {
        connection.close();
    }

}