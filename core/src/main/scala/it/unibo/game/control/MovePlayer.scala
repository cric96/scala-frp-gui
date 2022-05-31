package it.unibo.game.control

import it.unibo.game.core.{Controller, Entity, Event, World}
import it.unibo.game.core.Entity.Player
import it.unibo.game.core.Event.MoveTo
import it.unibo.game.core.Space.{Point2D, applyVelocity}
import monix.eval.Task
import monocle.syntax.all.*

case class MovePlayer(where: Point2D, velocity: Double) extends Controller:
  override def apply(event: Event, world: World): Task[(World, Controller)] = event match
    case MoveTo(where) => Task(world, this.focus(_.where).replace(where))
    case Event.TimePassed(deltaTime) =>
      Task(world.focus(_.player).modify(goto(_, where, deltaTime, velocity)), this)

  private def goto(player: Player, where: Point2D, deltaTime: Double, velocity: Double): Entity.Player =
    player
      .focus(_.position)
      .modify(applyVelocity(_, (where - player.position).versor * deltaTime * velocity))
