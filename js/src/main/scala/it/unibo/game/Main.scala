package it.unibo.game
import it.unibo.game.loop.GameLoop
import org.scalajs.dom
import org.scalajs.dom.{document, html}
import monix.execution.Scheduler.Implicits.global
@main def mainJS(): Unit =
  val body = dom.document.body
  val canvas = dom.document.createElement("canvas").asInstanceOf[html.Canvas]
  val div = dom.document.createElement("div").asInstanceOf[html.Div]
  div.style = "text-align: center"
  canvas.style = "border: 5px solid black; display: block; margin: 0 auto"
  val size =
    if (dom.window.innerHeight > dom.window.innerWidth)
      dom.window.innerWidth
    else
      dom.window.innerHeight
  val canvasSize = (size * 0.985).toInt
  canvas.width = canvasSize
  canvas.height = canvasSize
  body.append(canvas)
  val gui = view.GUI(canvas)
  val game = GameLoop.start(gui)
  game.runAsyncAndForget
