import javax.mail._
import javax.mail.internet._
import javax.mail.Session
import javax.mail.Transport

class SendMail(sender: String, appPassword: String) {
  val host = "smtp.gmail.com" //hard coded because I dont plan on changing this value

  // Getting system properties
  val properties = System.getProperties

  // Setting up mail server
  // also hard coded because I dont plan on changing these values either
  properties.setProperty("mail.smtp.host", host)
  properties.setProperty("mail.smtp.port", "465")
  properties.setProperty("mail.smtp.ssl.enable", "true")
  properties.setProperty("mail.smtp.auth", "true")

  // creating session object to get properties
  val session: Session = Session.getInstance(properties, new Authenticator() {
    override protected def getPasswordAuthentication = new PasswordAuthentication(sender, appPassword)
  })

  /**
   * sends an email to recipient. subject is the subject of the email and text is the body of the email.
   * @param recipient
   * @param subject
   * @param text
   * @return boolean indicating success
   */
  def send(recipient: String, subject: String, text: String): Boolean = {
    try
    {
      // MimeMessage object.
      val message = new MimeMessage(session);

      // Set From Field: adding senders email to from field.
      message.setFrom(new InternetAddress(sender));

      // Set To Field: adding recipient's email to from field.
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

      // Set Subject: subject of the email
      message.setSubject(subject);

      // set body of the email.
      message.setContent(text, "text/html");

      // Send email.
      Transport.send(message);
      println("Mail successfully sent");
      true;
    }
    catch
    {
      case mex: MessagingException => {
        mex.printStackTrace();
        false;
      }
    }
  }


}