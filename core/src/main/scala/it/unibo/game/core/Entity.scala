package it.unibo.game.core

import Space.*

enum Entity(val size: Double, val position: Point2D):
  case Food(override val size: Double, override val position: Point2D) extends Entity(size, position)
  case Player(override val size: Double, override val position: Point2D) extends Entity(size, position)
  val radius = math.sqrt(size / math.Pi)
  val diameter = radius * 2
