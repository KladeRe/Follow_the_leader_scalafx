package FTL

import FTL.{Person, Simulation}

import java.nio.file.Paths
// These are default variables which are initialized when the program starts
object SharedVariables:
  val slowingdistance = 150
  var separationDistance = 50
  var windowWidth: Double = 700
  var windowHeight: Double = 700
  var speed = 0.0
  var blobSize = 10
  var wanderDistance = 32.0
  var wanderRadiusConstant = (1.0 / 2.0)
  var manualOn = false
  var leaderConstantSpeed = false
  def getWanderRadius = wanderRadiusConstant * wanderDistance
  val currentPath = Paths.get(System.getProperty("user.dir"))
  var filePath = Paths.get("src/main/scala/FTL/data/data.json")



