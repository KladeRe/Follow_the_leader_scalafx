package FTL.Flock

import FTL.Vector2D

import scala.collection.mutable.Buffer
import scala.collection.parallel.CollectionConverters.*
import FTL.SharedVariables.blobSize

abstract class Member(x: Int, y: Int, dx: Int, dy: Int):
  var position: Vector2D = Vector2D(x, y)
  var movement: Vector2D = Vector2D(dx, dy).setLength(4.0)
  val changeConstant = 1/20.0
  var movementAngle = Math.atan(movement.e2 / movement.e1)
  var head_x = this.position.e1 + Math.cos(movementAngle)*blobSize
  var head_y = this.position.e2 + Math.sin(movementAngle)*blobSize
  var left_x = this.position.e1 + Math.cos(movementAngle+Math.PI*0.75)*blobSize
  var left_y = this.position.e2 + Math.sin(movementAngle+Math.PI*0.75)*blobSize
  var right_x = this.position.e1 + Math.cos(movementAngle-Math.PI*0.75)*blobSize
  var right_y = this.position.e2 + Math.sin(movementAngle-Math.PI*0.75)*blobSize

  def updatePoints() =
    movementAngle = Math.acos(this.movement.e1/this.movement.length)*(movement.e2/movement.e2.abs)
    head_x = this.position.e1 + Math.cos(movementAngle)*blobSize
    head_y = this.position.e2 + Math.sin(movementAngle)*blobSize
    left_x = this.position.e1 + Math.cos(movementAngle+Math.PI*0.75)*blobSize
    left_y = this.position.e2 + Math.sin(movementAngle+Math.PI*0.75)*blobSize
    right_x = this.position.e1 + Math.cos(movementAngle-Math.PI*0.75)*blobSize
    right_y = this.position.e2 + Math.sin(movementAngle-Math.PI*0.75)*blobSize
















