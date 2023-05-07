import FTL.Flock.Leader
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import FTL.Vector2D
import FTL.SharedVariables
class LeaderTest extends AnyFlatSpec with Matchers {
  "seek" should "put the leader closer to the target" in {
    SharedVariables.speed = 3.0
    val maxSpeed = 3.0
    val testLeader = Leader(10, 10, -2, -1)
    testLeader.target = Vector2D(100, 100)
    val distance1 = testLeader.target.sub(testLeader.position).length
    for (i <- 0 until 5) do
      testLeader.movement = testLeader.movement.add(testLeader.seek()).limit(maxSpeed)
      testLeader.position = testLeader.position.add(testLeader.movement)
    val distance2 = testLeader.target.sub(testLeader.position).length
    distance2 should be < distance1
  }

  "wander" should "change the position of the leader" in {
    SharedVariables.speed = 3.0
    val maxSpeed = 3.0
    val testLeader = Leader(10, 10, 1, 2)
    testLeader.target = Vector2D(500, 500)
    val position1 = testLeader.position
    testLeader.movement = testLeader.movement.add(testLeader.wander()).limit(maxSpeed)
    testLeader.position = testLeader.position.add(testLeader.movement)
    val position2 = testLeader.position
    position2 should not equal position1
  }

  "leader" should "not go out of bounds" in {
    SharedVariables.speed = 3.0
    val maxSpeed = 3.0
    val testLeader = Leader(1,1,-3, -3)
    testLeader.target = Vector2D(100, 50)
    testLeader.movement = testLeader.movement.add(testLeader.wander()).add(testLeader.seek()).limit(maxSpeed)
    testLeader.position = testLeader.position.add(testLeader.movement)
    val positionResult = testLeader.position
    positionResult.e1.toInt should be > -3
    positionResult.e2.toInt should be > -3
  }
}