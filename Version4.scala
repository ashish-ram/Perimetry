import ij._
import ij.process._
import ij.gui._
import java.awt._
import ij.plugin.filter._
import ij.plugin._
import java.util._
import ij.ImagePlus
import ij.gui.GenericDialog
import ij.gui.NewImage
import ij.plugin.PlugIn
import java.lang.Object._
import java.awt.event._
import java.awt.Component
import ij.plugin.filter._
import ij.plugin.filter.PlugInFilter
import ij.process._
import java.awt.event._
import Version1._

//remove if not needed

import scala.collection.JavaConversions._

class Stimulus(var flag: Int, n: Int) {
  val alpha: Double = 3.14 * 5 / 180
  val numberOfPoints: Int = flag match {
    case 0 => 4,
    case 1 => 8,
    case 2 => 10,
    case 3 => 16,
    case 4 => 20
  }
  val angularDifference: Double = 6.28 / numberOfPoints

  val theta: Double = (1 + 2 * n) * angularDifference / 2

  var normal_threshold: Int = 245 + generator.nextInt(10)

  var threshold: Int = _

  var intensity: Int = 250

  var generated: Int = 0

  var response: Int = 0

  var generator: Random = new Random()
}

class BlindSpot(var which_eye: String) {
  var intensity: Int = 255

  var status: Int = _

  val (xcord: Int, ycord: Int) = which_eye match {
    case "left" => (539, 475)
    case "right" => (1111, 475)
  }
}

object Version1 {
  val name: String = "patient"
  val eye: String = "left"
  val width: Double = 1650
  val height: Double = 950
  val bitdep: Int = 24
  val backgroundColor: Int = 150

  var stimulus: Stimulus = _
}

class Version1 extends MouseAdapter with PlugIn {

  def run(arg: String) {
    val gd = new GenericDialog("New Image")
    gd.addStringField("Name:", name)
    gd.addStringField("Eye:", eye)
    gd.addNumericField("Width:", width, 0)
    gd.addNumericField("Height:", height, 0)
    gd.addNumericField("BitDepth:", bitdep, 0)
    gd.addNumericField("Background color:", backgroundColor, 0)
    gd.showDialog()
    if (gd.wasCanceled()) return
    name = gd.getNextString
    eye = gd.getNextString
    width = gd.getNextNumber.toInt
    height = gd.getNextNumber.toInt
    bitdep = gd.getNextNumber.toInt
    backgroundColor = gd.getNextNumber.toInt

    stimulus = Array.ofDim[StimulusStimulus, ](5)
    stimulus(0) = Array.ofDim[Stimulus](4)
    stimulus(1) = Array.ofDim[Stimulus](8)
    stimulus(2) = Array.ofDim[Stimulus](10)
    stimulus(3) = Array.ofDim[Stimulus](16)
    stimulus(4) = Array.ofDim[Stimulus](20)
    for (j <- 0 until 4) {
      stimulus(0)(j) = new Stimulus(0, j)
    }
    for (j <- 0 until 8) {
      stimulus(1)(j) = new Stimulus(1, j)
    }
    for (j <- 0 until 10) {
      stimulus(2)(j) = new Stimulus(2, j)
    }
    for (j <- 0 until 16) {
      stimulus(3)(j) = new Stimulus(3, j)
    }
    for (j <- 0 until 20) {
      stimulus(4)(j) = new Stimulus(4, j)
    }
    val im = NewImage.createImage("$name $eye eye", width.toInt, height.toInt, 1, 8, NewImage.FILL_BLACK)
    val ip = im.getProcessor
    im.show()
    val win = im.getWindow
    val canvas = win.getCanvas
    win.setLocationAndSize(0, 0, 1650, 950)
    canvas.addMouseListener(this)
    for (i <- 0 until 10) {
      ip.putPixel(width.toInt / 2 - i, height.toInt / 2 + i, 255)
      ip.putPixel(width.toInt / 2 + i, height.toInt / 2 + i, 255)
      ip.putPixel(width.toInt / 2 - i, height.toInt / 2 - i, 255)
      ip.putPixel(width.toInt / 2 + i, height.toInt / 2 - i, 255)
    }
    val blind = new BlindSpot(eye)
    ip.snapshot()
    ip.setColor(255)
    ip.setRoi(new OvalRoi(blind.xcord - 10, blind.ycord - 10, 20, 20))
    ip.fill()
    ip.reset(ip.getMask)
    im.updateAndDraw()
    ip.snapshot()
    val generator1 = new Random()
    val generator2 = new Random()
    val generator3 = new Random()
    var checkedPointCounter = 0
    while (checkedPointCounter <= 58) {
      val delay = generator3.nextInt()
      val i = generator1.nextInt(5)
      val MAX: Int = i match {
        case 0 => 4
        case 1 => 8
        case 2 => 10
        case 3 => 16
        case 4 => 20
      }
      val j = generator2.nextInt(MAX)
      if (stimulus(i)(j).response == -3) {
        //continue
      }
      get(i, j)
      stimulus(i)(j).response -= 1
      stimulus(i)(j).generated += 1
      ip.setColor(stimulus(i)(j).intensity)
      val r = (height * Math.tan(stimulus(i)(j).alpha) / (2 * Math.tan(24 * 3.14 / 180))).toInt
      val x = (width / 2 + r * Math.cos(stimulus(i)(j).theta)).toInt
      val y = (height / 2 + r * Math.sin(stimulus(i)(j).theta)).toInt
      ip.setRoi(new OvalRoi(x - 5, y - 5, 10, 10))
      ip.fill()
      ip.reset(ip.getMask)
      im.updateAndDraw()
      IJ.wait(300)
      ip.reset()
      im.updateAndDraw()
      IJ.wait(310)
      if (stimulus(i)(j).response == -3) {
        val intensity = stimulus(i)(j).intensity
        IJ.write(s"$checkedPointCounter $i $j $intensity")
        checkedPointCounter += 1
      }
    }
    IJ.showMessage("test completed")
  }

  var ii: Int = 0

  var jj: Int = 0

  def get(i: Int, j: Int) {
    ii = i
    jj = j
  }

  def mouseClicked(e: MouseEvent) {
    stimulus(ii)(jj).response = 0
    stimulus(ii)(jj).intensity = stimulus(ii)(jj).intensity - 20
    IJ.beep()
  }

  def mousePressed(e: MouseEvent) {
  }

  def mouseReleased(e: MouseEvent) {
  }

  def mouseEntered(e: MouseEvent) {
  }

  def mouseExited(e: MouseEvent) {
  }
}
