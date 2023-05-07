package FTL.Visuals

import FTL.SharedVariables.filePath
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.VBox
import scalafx.scene.text.Font
import scalafx.scene.layout.Background
import scalafx.scene.layout.BackgroundImage
import scalafx.scene.image.Image
import scalafx.scene.layout.BackgroundRepeat
import scalafx.scene.layout.BackgroundPosition
import scalafx.scene.layout.BackgroundSize
import javafx.scene.paint.ImagePattern
import scalafx.scene.image.ImageView
import scalafx.scene.paint.Color


class MainMenu extends VBox:
  this.spacing = 10
  this.alignment = Pos.TopCenter
  this.background = new Background(
    Array(
      new BackgroundImage(
        new Image("file:src/main/scala/FTL/Visuals/blobs.png"),
        BackgroundRepeat.NoRepeat,
        BackgroundRepeat.NoRepeat,
        BackgroundPosition.Center,
        new BackgroundSize(881, 744, false, false, true, true)
      )
    )
  )

  val header = new Label("Follow-the-leader simulation")
  header.padding = Insets(50, 25, 10, 25)
  header.font = Font(30)
  header.textFill = Color.White

  val path = new Label(s"Current path is ${filePath.toString}")
  path.textFill = Color.WhiteSmoke
  
  val leaderDesc = new Label("Dynamic speed: The speed of the leader can change")
  leaderDesc.textFill = Color.WhiteSmoke

  val buttonContainer = new VBox
  buttonContainer.padding = Insets(25, 100, 25, 100)
  buttonContainer.spacing = 20
  buttonContainer.alignment = Pos.TopCenter

  val playButton = new Button {
    text = "Start"
    maxWidth = 300
    prefHeight = 50
  }

  val selectButton = new Button {
    text = "Select file"
    maxWidth = 300
    prefHeight = 50
  }
  
  val leaderButton = new Button {
    text = "Change leader movement style"
    maxWidth = 300
    prefHeight = 50
  } 

  buttonContainer.children = Array(playButton, selectButton, leaderButton)

  this.children = Array(header, path, buttonContainer, leaderDesc)
