package it.unibo.game.loop

import it.unibo.game.update.{CollisionDetection, MovePlayer, RandomWalk, Spawner, Update}
import it.unibo.game.core.{Event, TimeFlow, World}
import it.unibo.game.ui.UI
import it.unibo.game.core.Space.Point2D
import it.unibo.game.core.Event.*
import monix.eval.Task
import monix.execution.Ack
import monix.reactive.{Observable, Observer, OverflowStrategy}

import scala.language.postfixOps
import concurrent.duration.DurationInt
import monix.execution.Scheduler.Implicits.global
import monix.reactive.subjects.PublishSubject
import org.reactivestreams.Subscriber

import scala.concurrent.Future
import scala.util.Random

object GameLoop:
  private val time: Observable[Event] = TimeFlow
    .tickEach(33 milliseconds)
    .map(_.toDouble)
    .map(Event.TimePassed.apply)

  def start(ui: UI): Task[Unit] =
    given Random = Random()
    given OverflowStrategy[Event] = OverflowStrategy.Default
    val world = World.empty
    val controls: Update = Update.combine(
      MovePlayer(Point2D(0, 0), 1e-4),
      Spawner(rate = 0.03),
      RandomWalk(0.00005),
      CollisionDetection()
    )

    val init = Task((world, controls))
    val events =
      Observable(time, ui.events.throttleLast(33 milliseconds)).merge
    events
      .scanEval(init) { case ((world, controls), event) => controls(event, world) }
      .doOnNext { case (world, _) => ui.render(world) }
      .completedL
