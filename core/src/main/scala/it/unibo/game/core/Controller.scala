package it.unibo.game.core

import monix.eval.Task
import monix.reactive.Observable

import scala.reflect.ClassTag

// This should be expressed as a function, (event, world) => Task[(Ager, Control)]
// Or Controller[E, M, F[_]] => (E, M) => F[(M, Control[E, M, F])]
trait Controller extends ((Event, World) => Task[(World, Controller)]):
  def andThen(control: Controller): Controller = (event: Event, world: World) =>
    this(event, world)
      .flatMap { case (world, left) => control(event, world).map { case (world, right) => (left, right, world) } }
      .map { case (left, right, world) => (world, Controller.combineTwo(left, right)) }

// We cannot use type because it became a cyclic reference
object Controller:
  extension (function: (Event, World) => (World, Controller))
    def lift: Controller =
      (event: Event, agar: World) => Task(function(event, agar))

  def same(function: (Event, World) => World): Controller = (event: Event, world: World) =>
    Task(function(event, world), same(function))

  def on[E <: Event](control: (E, World) => Task[World])(using ev: ClassTag[E]): Controller =
    lazy val result: Controller = (event: Event, world: World) =>
      event match
        case event: E => (control(event, world).map(world => (world, result)))
        case _ => Task((world, result))
    result
  val empty: Controller = (_: Event, world: World) => Task((world, empty))

  def combineTwo(engineA: Controller, engineB: Controller): Controller = (event: Event, world: World) =>
    for
      updateA <- engineA.apply(event, world)
      (newWorld, newEngineA) = updateA
      updateB <- engineB(event, newWorld)
      (lastWorld, newEngineB) = updateB
    yield (lastWorld, combineTwo(newEngineA, newEngineB))

  def combine(engines: Controller*): Controller = engines.reduce(_.andThen(_))
