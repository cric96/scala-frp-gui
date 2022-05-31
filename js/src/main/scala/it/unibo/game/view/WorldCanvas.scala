package it.unibo.game.view

import it.unibo.game.core.World
import org.scalajs.dom.html.Canvas
import monix.reactive.subjects.PublishSubject
import it.unibo.game.core.Space.*
import org.scalajs.dom

private class WorldCanvas(canvas: Canvas):
  private val width = canvas.clientWidth
  private val height = canvas.clientHeight
  def render(world: World): Unit =
    val context = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
    context.clearRect(0, 0, canvas.clientWidth, canvas.clientHeight)
    context.fillStyle = s"hsla(0,0%,50%,30%)"
    context.fillRect(0, 0, canvas.clientWidth, canvas.clientHeight)
    world.all.foreach { entity =>
      val (x, y) = (entity.position.x * width, entity.position.y * height)
      val (circleWidth, circleHeight) = (entity.radius * width, entity.radius * height)
      context.beginPath()
      context.fillStyle = s"hsl(${(entity.size * 360).toInt},100%,50%)"
      context.strokeStyle = "black"
      context.ellipse(x, y, circleWidth, circleHeight, 0, 0, 2 * math.Pi)
      context.fill()
      context.stroke()
    }
