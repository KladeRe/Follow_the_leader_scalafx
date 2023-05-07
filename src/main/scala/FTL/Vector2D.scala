package FTL

// A class for describing vectors
class Vector2D(x: Double, y: Double) {
  var e1 = x
  var e2 = y

  def add(another: Vector2D): Vector2D = 
    Vector2D(this.e1+another.e1, this.e2+another.e2)

  def multiply(c: Double): Vector2D = 
    Vector2D(this.e1*c, this.e2*c)

  def sub(another: Vector2D): Vector2D = 
    Vector2D(this.e1-another.e1, this.e2-another.e2)
    
  def length: Double = 
    Math.sqrt(this.e1*this.e1+this.e2*this.e2)

  def normalize(): Vector2D = 
    this.multiply(1.0/this.length)

  def angle(another: Vector2D): Double = 
    Math.acos((this.e1*another.e1+this.e2*another.e2)/(this.length*another.length))

  def setLength(length: Double): Vector2D = 
    this.normalize().multiply(length)

  def rotate(angle: Double): Vector2D = 
    new Vector2D(e1*Math.cos(angle)-e2*Math.sin(angle),e1*Math.sin(angle)+e2*Math.cos(angle))

  def cross_product(another: Vector2D) = this.e1*another.e2-this.e2*another.e1

  def limit(maxLength: Double): Vector2D =
    if this.length > maxLength then this.setLength(maxLength)
    else this

  val updateLeader: (Double) => Vector2D = if SharedVariables.leaderConstantSpeed then this.setLength else this.limit

  // Used for debugging
  override def toString: String = s"${this.e1} ${this.e2}"

}
