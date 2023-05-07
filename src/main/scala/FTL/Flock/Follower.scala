package FTL.Flock

import FTL.SharedVariables.{separationDistance, slowingdistance, speed}
import FTL.{SharedVariables, Vector2D}
import com.github.plokhotnyuk.rtree2d.core.*
import com.github.plokhotnyuk.rtree2d.core.EuclideanPlane.*

class Follower(boss: Leader, x: Int, y: Int, dx: Int, dy: Int, id: Int) extends Member(x, y, dx, dy):
  var gang_size: Int = 1
  var gang_positions = RTree(Array(1).map(obj => entry(1,2,3)))
  val r = scala.util.Random

  val leader = boss
  def target = this.leader.position.sub(this.leader.movement.setLength(20.0))


  def update(): Unit =
    if (speed > 0) then
      if collisionComing then
        this.movement = this.movement.add(this.steerToAvoid()).add(this.separate()).limit(speed)
      else
        this.movement = this.movement.add(this.arrive().multiply(changeConstant)).add(this.separate().multiply(changeConstant)).limit(speed)
      this.position = this.position.add(this.movement)
      updatePoints()

  def separate(): Vector2D =
    val nearby = gang_positions.nearestK(this.position.e1.toFloat, this.position.e2.toFloat, gang_size, separationDistance)
    val newNearby = nearby.filter(_.value != id)
    var result = new Vector2D(0, 0)

    if (newNearby.isEmpty) then
      return result
    val forces = newNearby.map(member => this.position.sub(new Vector2D(member.maxX, member.maxY)).setLength(1.0 / Math.pow(this.position.sub(new Vector2D(member.maxX, member.maxY)).length, 2)))
    result = forces.foldLeft(new Vector2D(0, 0))((a: Vector2D, b: Vector2D) => a.add(b))

    val distanceToLeader = this.leader.position.sub(this.position).length
    if (distanceToLeader < separationDistance) then
      result = result.add(this.position.sub(this.leader.position).setLength(3/Math.pow(distanceToLeader, 2)))
    result = result.setLength(speed)
    result

  def arrive(): Vector2D =

    val targetOffset = this.target.sub(this.position)
    val distance = targetOffset.length
    val rampedSpeed = speed * (distance / slowingdistance)
    val clipped_speed = rampedSpeed.min(speed)
    val desired_velocity = targetOffset.multiply((clipped_speed / distance))
    desired_velocity.sub(this.movement).limit(speed)


  def collisionComing: Boolean =
    val distance = this.leader.position.sub(this.position).length
    if (distance < 150 && this.position.sub(leader.position).angle(leader.movement) < Math.PI * 0.4) then
      true
    else false

  def steerToAvoid(): Vector2D =
    val relativePos = this.leader.position.sub(this.position)
    var perp = Vector2D(0, 0)
    if (this.leader.movement.cross_product(this.position.sub(this.leader.position)) < 0) then
      perp = relativePos.rotate(Math.PI * (0.5)).setLength(100.0)
    else
      perp = relativePos.rotate(Math.PI * (-0.5)).setLength(100.0)
    val avoidSteer = perp.setLength(speed).sub(this.movement).limit(speed)
    avoidSteer

