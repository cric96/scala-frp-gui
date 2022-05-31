package it.unibo.game.update

import it.unibo.game.core.{Event, World}

import it.unibo.game.update.Update.*
import monix.eval.Task
import monix.reactive.Observable

import scala.reflect.ClassTag

// This should be expressed as a function, (event, world) => Task[(Ager, Control)]
// Or Controller[E, M, F[_]] => (E, M) => F[(M, Control[E, M, F])]
trait Update extends ((Event, World) => Task[(World, Update)]):
  def andThen(control: Update): Update = (event: Event, world: World) =>
    this(event, world)
      .flatMap { case (world, left) => control(event, world).map { case (world, right) => (left, right, world) } }
      .map { case (left, right, world) => (world, Update.combineTwo(left, right)) }

// We cannot use type because it became a cyclic reference
object Update:
  extension (function: (Event, World) => (World, Update))
    def lift: Update =
      (event: Event, agar: World) => Task(function(event, agar))

  def same(function: (Event, World) => World): Update = (event: Event, world: World) =>
    Task(function(event, world), same(function))

  def on[E <: Event](control: (E, World) => Task[World])(using ev: ClassTag[E]): Update =
    lazy val result: Update = (event: Event, world: World) =>
      event match
        case event: E => (control(event, world).map(world => (world, result)))
        case _ => Task((world, result))
    result
  val empty: Update = (_: Event, world: World) => Task((world, empty))

  def combineTwo(engineA: Update, engineB: Update): Update = (event: Event, world: World) =>
    for
      updateA <- engineA.apply(event, world)
      (newWorld, newEngineA) = updateA
      updateB <- engineB(event, newWorld)
      (lastWorld, newEngineB) = updateB
    yield (lastWorld, combineTwo(newEngineA, newEngineB))

  def combine(engines: Update*): Update = engines.reduce(_.andThen(_))
