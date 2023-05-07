package FTL

import FTL.Follow_the_leader.stage
import SharedVariables.{currentPath, filePath, manualOn, windowHeight, windowWidth}
import com.github.plokhotnyuk.rtree2d.core.EuclideanPlane.entry
import com.github.plokhotnyuk.rtree2d.core.RTree
import scalafx.animation.AnimationTimer
import scalafx.scene.Scene
import scalafx.scene.control.{Alert, Button, SplitPane}
import scalafx.scene.control.Alert.AlertType
import scalafx.stage.FileChooser
import SharedVariables.*
import FTL.Visuals.{Figures, MainMenu}


import java.nio.file.{Path, Paths}
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.*

// A class for the GUI
class GUI() extends Scene :
  var blobs = new Simulation(Array(Person(1,1,1,1)))
  var figures = new Figures(blobs)

  val fileChooser = new FileChooser()
  val mainMenu: MainMenu = new MainMenu {
    playButton.onAction = (event) => try {
      blobs = getSimulation(filePath.toString)
      blobs.leader.target = blobs.leader.randomPos()
      figures = new Figures(blobs) {
        quitButton.onAction = (event) => {
          mainLoop.stop()
          separationDistance = 50
          speed = 0.0
          blobSize = 10
          wanderDistance = 32.0
          wanderRadiusConstant = (1.0 / 2.0)
          manualOn = false
          root = mainMenu
        }
      }
      mainLoop.start()
      for (i <- figures.texts.indices) do
        val bounds = figures.texts(i).boundsInLocal()
        figures.texts(i).x = figures.sideBar.width() * 0.5 - (bounds.getWidth / 2.0)
      root = figures.mainLayout

    } catch {
      case e: Throwable =>
        new Alert(AlertType.Error) {
          initOwner(stage)
          title = "File error"
          headerText = "Error loading file"
          contentText = e.toString
        }.showAndWait()
    }
    selectButton.onAction = (event) => {
      val selectedFile = fileChooser.showOpenDialog(stage)
      val selectedPath: Path = Paths.get(selectedFile.toString)
      filePath = currentPath.relativize(selectedPath)
      path.text = filePath.toString

    }

    leaderButton.onAction = (event) => {
      leaderConstantSpeed = !leaderConstantSpeed
      if leaderConstantSpeed then
        leaderDesc.text = "Constant speed: The speed of the leader is constant"
      else
        leaderDesc.text = "Dynamic speed: The speed of the leader can change"
    }
  }
  private var previousUpdate = System.nanoTime() / 1000000
  private var lag = 0.0
  private val updatesPerSecond: Double = 60
  private val msPerUpdate = (1.0 / updatesPerSecond) * 1000

  // Main loop of the simulation
  val mainLoop = AnimationTimer(time => {

    val now = time / 1000000
    val elapsed = now - previousUpdate
    previousUpdate = now
    lag += elapsed

    while (lag >= msPerUpdate) {
      blobs.updateData()
      lag = lag - msPerUpdate
    }
    
    figures.animate(width(), height(), blobs)

  })
  // Start with the menu
  this.root = mainMenu


