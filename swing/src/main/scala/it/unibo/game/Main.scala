package it.unibo.game

import it.unibo.game.loop.GameLoop
import it.unibo.game.view.GUI

import monix.execution.Scheduler.Implicits.global // Context
@main def mainJVM(): Unit =
  val swingUI = new GUI(800, 800)
  val loop = GameLoop.start(swingUI)
  loop.runAsyncAndForget
