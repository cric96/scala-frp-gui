package it.unibo.game.core

import scala.annotation.targetName

object Space:
  opaque type Point2D = (Double, Double)
  object Point2D:
    def apply(x: Double, y: Double): Point2D = (x, y)
    def unapply(p: Point2D): Option[(Double, Double)] = Some((p.x, p.y))

  extension (self: Point2D)
    def x: Double = self._1
    def y: Double = self._2
    @targetName("plus")
    def +(p: Point2D): Point2D = (x + p.x, y + p.y)
    @targetName("minus")
    def -(p: Point2D): Point2D = (x - p.x, y - p.y)
    @targetName("product")
    def *(value: Double): Point2D = (self.x * value, self.y * value)
    @targetName("division")
    def /(value: Double): Point2D = self * (1 / value)
    def module: Double = Math.hypot(self.x, self.y)
    def distanceTo(point: Point2D): Double = Math.hypot(point.x - self.x, point.y - self.y)
    def versor: Point2D = self / module

  def applyVelocity(position: Point2D, delta: Point2D): Point2D = position + delta
