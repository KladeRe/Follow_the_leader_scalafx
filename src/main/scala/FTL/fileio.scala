package FTL


import io.circe.generic.auto.*
import io.circe.syntax.*
import io.circe.parser._
import io.circe.Json
import scala.io.Source
import scala.util.Using
import scala.util.{Try, Failure, Success}
import scala.collection.mutable.Buffer
import java.lang.Exception

case class Person(x: Int, y: Int, dx: Int, dy: Int)

class FileErrorException(message: String) extends Exception(message)

// This function imports the data from a JSON file
def getFileData(path: String) =
  val jsonString = Using.resource(Source.fromFile(path)) { source =>
    source.getLines.mkString
  }

  def readJson(text: String ) = decode[List[Person]](text).toTry

  def readText(source: scala.io.Source ) = source.getLines().mkString("\n")

  val fileSource   = Try(scala.io.Source.fromFile(path))
  val stringSource = Try(scala.io.Source.fromString(jsonString))

  val members =
    val temp = Buffer[Person]()
    for
      source <- stringSource
      text = readText(source)
      list = readJson(text)
      member <- list
    do
      temp ++= member
    temp
  if (members.map(person => (person.x, person.y)).distinct.length != members.map(person => (person.x, person.y)).length) then
    throw new FileErrorException("At least two members have the same starting coordinates!")
  if (members.isEmpty) then
    throw new FileErrorException("File is formatted wrong. Remember that the leader is required.")
  else
    members.toArray

