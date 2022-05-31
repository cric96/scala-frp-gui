package it.unibo.game.core

import Entity.*
import Space.Point2D
case class World(player: Player, food: Seq[Food], counter: Int = 0):
  val all: Seq[Entity] = food :+ player

object World:
  def empty: World = World(Player(0.005, Point2D(0.5, 0.5)), Seq.empty)
