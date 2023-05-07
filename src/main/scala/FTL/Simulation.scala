package FTL
import scala.collection.mutable.Buffer
import com.github.plokhotnyuk.rtree2d.core.*
import EuclideanPlane.*
import FTL.Flock.{Leader, Follower}
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.*
import ExecutionContext.Implicits.global
import scala.concurrent.duration.*


// This is where all the data is stored
class Simulation(membersInfo: Array[FTL.Person]):

  val leader = new Leader(membersInfo.head.x, membersInfo.head.y, membersInfo.head.dx, membersInfo.head.dy)
  var members = Array[Follower]()
  if membersInfo.length > 1 then
    members = membersInfo.tail.indices.map(i => new Follower(leader, membersInfo(i+1).x, membersInfo(i+1).y, membersInfo(i+1).dx, membersInfo(i+1).dy, i)).toArray
  members.foreach(_.gang_size = members.length)
  var positionTree = RTree(
    members.zipWithIndex.map(obj => entry(obj._1.position.e1.toFloat, obj._1.position.e2.toFloat, obj._2))
  )
  members.foreach(_.gang_positions = positionTree)
  
  // Updates the positions and movement vectors of the blobs
  def updateData() =
    this.leader.update()
    val futures = (this.members.indices).map { i =>
      Future {
        this.members(i).update()
      }
    }
    Await.result(Future.sequence(futures), 1.second)
  
    this.positionTree = RTree(
      this.members.zipWithIndex.map(obj => entry(obj._1.position.e1.toFloat, obj._1.position.e2.toFloat, obj._2))
    )
    this.members.foreach(_.gang_positions = this.positionTree)

def getSimulation(path: String): FTL.Simulation =
  val initial_data = getFileData(path)
  new Simulation(initial_data)


  
