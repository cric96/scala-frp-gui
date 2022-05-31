package it.unibo.game.ui

import it.unibo.game.core.{Event, World}
import monix.eval.Task
import monix.reactive.Observable

// a general UI: trait UI[Event, Data] ==> it contains side effects!! (but they are wrapper with monadic abstractions)
// even more general: UI[Event, Data, F[_], R[_]]
trait UI:
  // Event source produced by this UI (outside of "functional" world, unsafe, unpure)
  def events: Observable[Event]
  // Render the world representation. It is an effect, therefore it returns a Task
  def render(world: World): Task[Unit]
