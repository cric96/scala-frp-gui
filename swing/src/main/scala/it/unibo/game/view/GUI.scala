package it.unibo.game.view

import it.unibo.game.core.Event.MoveTo
import it.unibo.game.core.Space.Point2D
import it.unibo.game.core.{Entity, Event, World}
import it.unibo.game.ui.UI
import monix.eval.Task
import monix.reactive.Observable
import monix.reactive.subjects.PublishSubject
import org.w3c.dom.events.MouseEvent
import javax.swing.WindowConstants
import java.awt.event.MouseMotionListener
import java.awt.{Color, Graphics, event}
import javax.swing.{JFrame, JPanel, SwingConstants, SwingUtilities}
class GUI(width: Int, height: Int) extends UI:
  private val frame = JFrame("Game")
  frame.setSize(width, height)
  frame.setVisible(true)
  frame.setLocationRelativeTo(null)
  frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  def events: Observable[Event] = frame.getContentPane
    .mouseObservable()
    .map((x, y) => (x / width.toDouble, y / height.toDouble))
    .map((x, y) => MoveTo(Point2D(x, y)))
  def render(world: World): Task[Unit] = Task {
    SwingUtilities.invokeAndWait { () =>
      if (frame.getContentPane.getComponentCount != 0)
        frame.getContentPane.remove(0)
      frame.getContentPane.add(WorldPane(world, width, height))
      frame.getContentPane.repaint()
    }
  }
