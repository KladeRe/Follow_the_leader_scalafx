package FTL


import javafx.application.Platform
import scalafx.stage.Screen
import scalafx.application.JFXApp3
import SharedVariables.*

// JFXApp is initialized here
object Follow_the_leader extends JFXApp3 :

  override def start(): Unit =
    val primaryScreen = Screen.primary
    val bounds = primaryScreen.bounds
    windowWidth = bounds.width * 0.7
    windowHeight = bounds.height * 0.9
    stage = new JFXApp3.PrimaryStage {
      title = "ScalaFX Follow the leader simulation"
      width = windowWidth
      height = windowHeight
      scene = new GUI()
    }
    
