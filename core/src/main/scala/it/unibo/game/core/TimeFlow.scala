package it.unibo.game.core

import monix.reactive.Observable

import scala.concurrent.duration.FiniteDuration

object TimeFlow:
  def tickEach(duration: FiniteDuration): Observable[Long] =
    Observable
      .fromIterable(LazyList.continually(duration))
      .delayOnNext(duration)
      .map(_.toMillis)
