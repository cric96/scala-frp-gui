package it.unibo.game.view

import it.unibo.game.core.{Entity, World}
import it.unibo.game.core.Entity.Food
import it.unibo.game.core.Event.MoveTo
import it.unibo.game.core.Space.Point2D
import it.unibo.game.core.Space.*
import java.awt.{Color, Graphics, event}
import java.awt.event.MouseMotionListener
import javax.swing.JPanel

private class WorldPane(val world: World, width: Int, height: Int) extends JPanel:
  this.setSize(width, height)
  override def paintComponent(graphics: Graphics): Unit =
    super.paintComponent(graphics)
    graphics.clearRect(0, 0, width, height)
    world.all.foreach { entity =>
      val (x, y) = ((entity.position.x * width).toInt, (entity.position.y * height).toInt)
      val (widthCircle, heightCircle) = ((entity.diameter * width).toInt, (entity.diameter * height).toInt)
      graphics.setColor(Color.getHSBColor(entity.size.toFloat, 1, 0.5))
      graphics.fillOval(x - widthCircle / 2, y - heightCircle / 2, widthCircle, heightCircle)
    }
