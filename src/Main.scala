
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
object Main {
  def main(args: Array[String]): Unit = {
    val controller = new Controller(gmail = scala.io.StdIn.readLine("Gmail: "),
      gmailPassword = scala.io.StdIn.readLine("Gmail password (the app one): "),
      dbUsername = scala.io.StdIn.readLine("DB Username: "),
      dbPassword = scala.io.StdIn.readLine("DB Password: "))

    controller.sendEmailsToSubscribers()
    //print(scala.io.StdIn.readLine("Ur message, bud: "))
    quality.best

    }

}

