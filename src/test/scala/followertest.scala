import FTL.Flock.{Follower, Leader}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable.Buffer
import FTL.{SharedVariables, Simulation, Vector2D, data}

class FollowerTest extends AnyFlatSpec with Matchers {
  "arrive" should "put follower closer to leader" in {
    SharedVariables.speed = 2.0

    val speed = 4.0
    val membersInfo = Array(FTL.Person(100, 100, 1, 0), FTL.Person(100, 200, 1, 1))
    val test = new FTL.Simulation(membersInfo)
    val testLeader = test.leader
    val testFollower = test.members.head
    testFollower.movement = testFollower.movement.limit(speed)
    val distance1 = testFollower.target.sub(testFollower.position).length
    testFollower.movement = testFollower.movement.add(testFollower.arrive()).limit(speed)
    testFollower.position = testFollower.position.add(testFollower.movement)
    testFollower.movement = testFollower.movement.add(testFollower.arrive()).limit(speed)
    testFollower.position = testFollower.position.add(testFollower.movement)
    testFollower.movement = testFollower.movement.add(testFollower.arrive()).limit(speed)
    testFollower.position = testFollower.position.add(testFollower.movement)
    testFollower.movement = testFollower.movement.add(testFollower.arrive()).limit(speed)
    testFollower.position = testFollower.position.add(testFollower.movement)

    val distance2 = testFollower.target.sub(testFollower.position).length
    distance2 should be < distance1

  }

  "separation" should "stop followers from colliding" in {
    FTL.SharedVariables.speed = 2.0
    val membersInfo = Array(FTL.Person(100, 100, 1, 0), FTL.Person(100, 200, 0, -1), FTL.Person(100, 195, 0, 1))
    val test = new Simulation(membersInfo)
    val testLeader = test.leader
    val followers = test.members
    val testFollower1 = followers.head

    val testFollower2 = followers(1)
    val distance1 = testFollower1.position.sub(testFollower2.position).length
    testFollower1.movement = testFollower1.movement.add(testFollower1.separate())
    testFollower2.movement = testFollower2.movement.add(testFollower2.separate())
    testFollower1.position = testFollower1.position.add(testFollower1.movement)
    testFollower2.position = testFollower2.position.add(testFollower2.movement)
    val distance2 = testFollower1.position.sub(testFollower2.position).length
    distance2 should not equal 0
    testFollower1.movement = testFollower1.movement.add(testFollower1.separate())
    testFollower2.movement = testFollower2.movement.add(testFollower2.separate())
    testFollower1.position = testFollower1.position.add(testFollower1.movement)
    testFollower2.position = testFollower2.position.add(testFollower2.movement)
    val distance3 = testFollower1.position.sub(testFollower2.position).length
    distance3 should not equal 0
  }

  "steerToAvoid" should "evade the leader smoothly" in {
    FTL.SharedVariables.speed = 2.0
    val membersInfo = Array(FTL.Person(100, 100, 1, 0), FTL.Person(150, 100, -1, 0))
    val test = new FTL.Simulation(membersInfo)
    val testLeader = test.leader
    testLeader.target = Vector2D(200, 100)
    val testFollower = test.members.head
    testLeader.update()
    testFollower.update()

    testFollower.position.e2 should not equal 100
    testLeader.update()
    testFollower.update()
    testFollower.position should not equal testLeader.position
  }
}