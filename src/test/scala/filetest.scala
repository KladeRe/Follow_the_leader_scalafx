import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import FTL.{FileErrorException, Person, getFileData}

import scala.collection.mutable.ArrayBuffer

class FileTest extends AnyFlatSpec with Matchers {
  "getFileData" should "return all needed data from correctly formatted file" in {
    val result = getFileData("src/test/scala/test1.json")
    result shouldEqual ArrayBuffer(Person(100,100,1,1), Person(200,50,-1,0))
  }

  "getFileData" should "throw Exception when file is formatted wrong" in {
    val result = intercept[FileErrorException] {
      getFileData("src/test/scala/test2.json")
    }

    result.getMessage shouldEqual "File is formatted wrong. Remember that the leader is required."
  }

  "getFileData" should "notice that two blobs have the same coordinates" in {
    val result = intercept[FileErrorException] {
      getFileData("src/test/scala/test3.json")
    }
    result.getMessage shouldEqual "At least two members have the same starting coordinates!"
  }

}