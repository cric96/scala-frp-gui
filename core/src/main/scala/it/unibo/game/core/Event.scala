package it.unibo.game.core

enum Event:
  case TimePassed(deltaTime: Double)
  case MoveTo(position: Space.Point2D)
