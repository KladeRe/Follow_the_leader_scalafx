package FTL.Flock

import FTL.Flock.Member
import FTL.SharedVariables.*
import FTL.Vector2D

class Leader(x: Int, y: Int, dx: Int, dy: Int) extends Member(x, y, dx, dy):

  val r = scala.util.Random
  var angle = Math.atan(movement.e1 / movement.e2)
  

  def randomPos() =
    Vector2D(r.nextDouble() * windowWidth*0.9+50, r.nextDouble() * windowHeight*0.9+50)

  var target = randomPos()

  def seek(): Vector2D =
    if (this.target.sub(this.position).length < 30) then
      this.target = randomPos()
    val desired_vector = this.target.sub(this.position).setLength(speed)
    val steering = desired_vector.sub(this.movement).limit(speed)
    steering


  def wander(): Vector2D =
    val circleCenter = this.movement.setLength(wanderDistance)
    angle += Math.sin((r.nextDouble()) * Math.PI * 2)*0.2
    val displacement = Vector2D(Math.cos(angle), Math.sin(angle)).setLength(getWanderRadius)
    val wanderTarget = circleCenter.add(displacement)
    val wanderSteering = (wanderTarget).limit(speed)
    wanderSteering

  def update(): Unit =
    if (speed > 0) then
      if manualOn then
        this.movement = this.movement.add(seek().multiply(changeConstant)).updateLeader(speed)
      else
        if (this.position.e1 < 10 || this.position.e1 > windowWidth-10 || this.position.e2 < 10 | this.position.e2 > windowHeight-10) then
          this.movement = this.movement.add(seek().multiply(changeConstant*2)).updateLeader(speed)
          angle = Math.acos(this.movement.e1/this.movement.length)*(movement.e2/movement.e2.abs)
        else
          this.movement = this.movement.add(wander().multiply(changeConstant)).add(seek().multiply(changeConstant)).updateLeader(speed)

      this.position = this.position.add(this.movement)
      updatePoints()
