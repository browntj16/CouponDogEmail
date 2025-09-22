object quality extends Enumeration{
  type quality = Int

  //values
  val best = 2
  val good = 1
  val bad = 0
  //the coupons db categorizes coupon quality by number. 2 is the best quality, 1 is good quality, and 0 are bad ones
  //the program does not send out bad coupons
}