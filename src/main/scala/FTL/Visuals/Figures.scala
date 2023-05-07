package FTL.Visuals

import FTL.SharedVariables.*
import scalafx.scene.control.*
import scalafx.scene.layout.Pane
import scalafx.scene.paint.*
import scalafx.scene.shape.{Circle, Polygon}
import scalafx.scene.text.Text
import com.github.plokhotnyuk.rtree2d.core.EuclideanPlane.entry
import com.github.plokhotnyuk.rtree2d.core.RTree
import scalafx.animation.AnimationTimer

import scala.concurrent.Future
import scala.concurrent.duration.*
import scala.concurrent.*
import ExecutionContext.Implicits.global
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.*


// All visual objects of the simulation are stored here
class Figures(blobs: FTL.Simulation):
  val leader = Polygon(blobs.leader.head_x, blobs.leader.head_y, blobs.leader.left_x, blobs.leader.left_y, blobs.leader.right_x, blobs.leader.right_y)
  leader.fill = Color.CadetBlue

  val followers = (blobs.members.indices).toList.map(index => Polygon(blobs.members(index).head_x, blobs.members(index).head_y,
    blobs.members(index).left_x, blobs.members(index).left_y, blobs.members(index).right_x, blobs.members(index).right_y))
  followers.foreach(circle => circle.fill = Color.Crimson)

  var sizeSlider = Slider(5, 15, 10)
  sizeSlider.setMajorTickUnit(1)
  sizeSlider.value.onChange { (_, oldValue, newValue) =>
    blobSize = newValue.intValue()
  }
  var speedSlider = Slider(0, 6, speed)
  speedSlider.setMajorTickUnit(0.1)
  speedSlider.value.onChange { (_, oldValue, newValue) =>
    speed = newValue.intValue
  }
  var wanderDistanceSlider = Slider(10, 100, wanderDistance)
  speedSlider.setMajorTickUnit(1)
  speedSlider.value.onChange { (_, oldValue, newValue) =>
    wanderDistance = newValue.intValue
  }
  var wanderRadiusSlider = Slider(0, 1, wanderRadiusConstant)
  speedSlider.setMajorTickUnit(0.05)
  speedSlider.value.onChange { (_, oldValue, newValue) =>
    wanderRadiusConstant = newValue.intValue
  }
  var separationDistanceSlider = Slider(40, 100, separationDistance)
  separationDistanceSlider.setMajorTickUnit(1)
  separationDistanceSlider.value.onChange { (_, oldValue, newValue) =>
    separationDistance = newValue.intValue
  }
  val sizeText = Text(0, 0, "Size")
  val speedText = Text(0, 0, "Speed")
  val wanderDText = Text(0, 0, "Wander distance")
  val wanderRText = Text(0, 0, "Wander radius")
  val separationText = Text(0, 0, "Separation distance")
  val manualButton = new Button("Manual control")
  manualButton.onAction = (event) => {
    if manualOn then blobs.leader.target = blobs.leader.randomPos()
    manualOn = !manualOn
  }

  val quitButton = new Button("Quit") {
    style = s"-fx-font: normal bold 12px sans-serif; -fx-border-color: black; -fx-background-color: #6eaeee;"
  }

  val simulationPanel = new Pane {
    minWidth = windowWidth * 0.7
    prefWidth = windowWidth * 0.8
    prefHeight = windowHeight
    children = Seq(leader) ++ followers
    style = "-fx-background-color: #444444"
  }

  val sliders = Array(sizeSlider, speedSlider, wanderDistanceSlider, wanderRadiusSlider, separationDistanceSlider)

  val texts = Array(sizeText, speedText, wanderDText, wanderRText, separationText)

  val sideBar = new Pane {
    children = sliders ++ texts ++ Seq(manualButton, quitButton)
    style = "-fx-background-color: orange"
  }

  val mainLayout = new SplitPane {
    items.addAll(simulationPanel, sideBar)
    dividerPositions = 0.8
    onMouseMoved = (event) =>
      if manualOn then
        blobs.leader.target.e1 = event.getX
        blobs.leader.target.e2 = event.getY
  }

  def animate(win_width: Double, win_height: Double, blobs: FTL.Simulation) =

    if (windowWidth != win_width.toInt || windowHeight != win_height.toInt || this.texts.head.x.value != this.sideBar.width() * 0.5 - (this.texts.head.boundsInLocal().getWidth / 2.0)) then

      windowWidth = win_width.toInt
      windowHeight = win_height.toInt
      this.simulationPanel.minWidth = windowWidth * 0.7
      for (i <- this.sliders.indices) do
        this.sliders(i).layoutX = this.sideBar.width.value * 0.25
        this.sliders(i).layoutY = this.sideBar.height.value * ((i + 1).toDouble * 0.1)
        this.sliders(i).prefWidth = this.sideBar.width.value * 0.5
        val bounds = this.texts(i).boundsInLocal()
        this.texts(i).y = this.sideBar.height() * (0.07 + (i * 0.1))
        this.texts(i).x = this.sideBar.width() * 0.5 - (bounds.getWidth / 2.0)
        this.texts(i).style = s"-fx-font: normal bold ${this.sideBar.height() * 0.02}px sans-serif"


      this.manualButton.layoutX = this.sideBar.width.value * 0.25
      this.manualButton.layoutY = this.sideBar.height.value * 0.65
      this.manualButton.prefWidth = this.sideBar.width.value * 0.5
      this.manualButton.prefHeight = this.sideBar.height.value * 0.05

      this.quitButton.layoutX = this.sideBar.width.value * 0.25
      this.quitButton.layoutY = this.sideBar.height.value * 0.8
      this.quitButton.prefWidth = this.sideBar.width.value * 0.5
      this.quitButton.prefHeight = this.sideBar.height.value * 0.05

    if (manualOn) then
      this.manualButton.style = s"-fx-font: normal bold 12px sans-serif; -fx-border-color: black; -fx-background-color: red;"
    else this.manualButton.style = s"-fx-font: normal bold 12px sans-serif; -fx-border-color: black; -fx-background-color: green;"
    this.leader.points(0) = blobs.leader.head_x
    this.leader.points(1) = blobs.leader.head_y
    this.leader.points(2) = blobs.leader.left_x
    this.leader.points(3) = blobs.leader.left_y
    this.leader.points(4) = blobs.leader.right_x
    this.leader.points(5) = blobs.leader.right_y

    val futures = (blobs.members.indices).map { i =>
      Future {
        this.followers.synchronized {
          this.followers(i).points(0) = blobs.members(i).head_x
          this.followers(i).points(1) = blobs.members(i).head_y
          this.followers(i).points(2) = blobs.members(i).left_x
          this.followers(i).points(3) = blobs.members(i).left_y
          this.followers(i).points(4) = blobs.members(i).right_x
          this.followers(i).points(5) = blobs.members(i).right_y
        }
      }
    }
    Await.result(Future.sequence(futures), 1.second)
