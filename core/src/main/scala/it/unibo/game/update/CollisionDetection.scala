package it.unibo.game.update

import it.unibo.game.core.{Entity, Event, World}
import it.unibo.game.update.Update.*
import it.unibo.game.core.Event.*
import it.unibo.game.core.Space.*
import it.unibo.game.core.Entity.*
import monix.eval.Task
import monix.reactive.Observable
import monocle.syntax.all.*

object CollisionDetection:
  def apply(): Update = on[TimePassed] { (_: Event, world: World) =>
    Task:
      val foodToEat = world.food.filter(hit(_, world.player)).find(canEat(_, world.player))
      foodToEat match
        case Some(food) =>
          val newFood = world.food.filter(_ != food)
          world
            .focus(_.food)
            .replace(newFood)
            .focus(_.player)
            .modify(updateSize(_, food))
        case None => world
  }

  private def hit(food: Food, player: Player): Boolean =
    val distance = food.position.distanceTo(player.position)
    distance < food.radius + player.radius

  private def canEat(food: Food, player: Player): Boolean = food.size < player.size
  private def updateSize(player: Player, food: Food): Player =
    player.focus(_.size).modify(_ + food.size)
