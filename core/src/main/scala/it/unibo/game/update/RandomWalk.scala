package it.unibo.game.update

import it.unibo.game.update.Update.*
import it.unibo.game.core.*
import it.unibo.game.core.Space.*
import it.unibo.game.core.Event.TimePassed
import it.unibo.game.core.Entity.*
import monix.eval.Task
import monocle.syntax.all.*
import scala.util.Random
object RandomWalk:
  def apply(maxVelocity: Double)(using random: Random): Update = on[TimePassed] { (timePassed, world) =>
    val updates = world.food.map(food => moveRandom(food, maxVelocity, timePassed.deltaTime))
    for {
      elements <- Task.parSequence(updates)
    } yield world.focus(_.food).replace(elements)
  }

  private def moveRandom(food: Food, maxVelocity: Double, dt: Double)(using random: Random): Task[Food] = Task {
    val randomPosition = food.position + Point2D(random.between(-1, 1), random.between(-1, 1))
    val versor = randomPosition.versor
    food.focus(_.position).modify(_ + (versor * maxVelocity * dt))
  }
