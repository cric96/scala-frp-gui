package it.unibo.game.control

import it.unibo.game.core.Controller.*
import it.unibo.game.core.Entity.Food
import it.unibo.game.core.Space.*
import monix.eval.Task
import it.unibo.game.core.Event.*
import it.unibo.game.core.{Controller, Event, World}
import monocle.syntax.all.*
import scala.concurrent.duration.FiniteDuration
import scala.util.Random

object Spawner:
  private val minBound = 0.001
  private val maxBound = 0.01
  def apply(rate: Double)(using random: Random): Controller = on[Event.TimePassed] { (_, world) =>
    if (random.nextDouble() < rate)
      Task(world.focus(_.food).modify(_ :+ newFood()))
    else
      Task(world)
  }

  private def newFood()(using random: Random): Food =
    Food(random.between(minBound, maxBound), Point2D(random.nextDouble(), random.nextDouble()))
