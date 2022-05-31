package it.unibo.game.view
import it.unibo.game.core.Event.MoveTo
import it.unibo.game.core.Space.Point2D
import it.unibo.game.core.{Event, World}
import org.scalajs.dom.html.Canvas
import it.unibo.game.ui.UI
import monix.eval.Task
import monix.reactive.Observable
import it.unibo.game.view.*

class GUI(canvas: Canvas) extends UI:
  private val pane = WorldCanvas(canvas)
  override def events: Observable[Event] =
    canvas.mouseObservable
      .map((x, y) => (x / canvas.clientWidth.toDouble, y / canvas.clientHeight.toDouble))
      .map((x, y) => MoveTo(Point2D(x, y)))

  override def render(world: World): Task[Unit] = Task {
    pane.render(world)
  }
