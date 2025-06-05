package it.unibo.game.update

import it.unibo.game.update.Update.*
import it.unibo.game.core.Entity.Food
import it.unibo.game.core.Space.*
import monix.eval.Task
import it.unibo.game.core.Event.*
import it.unibo.game.core.{Event, World}
import monocle.syntax.all.*
import scala.concurrent.duration.FiniteDuration
import scala.util.Random

object Spawner:
  private val minBound = 0.001
  private val maxBound = 0.01
  def apply(rate: Double)(using random: Random): Update = on[Event.TimePassed]: (_, world) =>
    if (random.nextDouble() < rate)
      Task(world.focus(_.food).modify(_ :+ newFood()))
    else
      Task(world)

  private def newFood()(using random: Random): Food =
    Food(random.between(minBound, maxBound), Point2D(random.nextDouble(), random.nextDouble()))
