package it.unibo.game.view

import monix.reactive.Observable
import monix.reactive.subjects.PublishSubject
import java.awt.Component
import java.awt.event.{MouseEvent, MouseMotionListener}

/** Extend swing to support observable sources */
extension (component: Component)
  def mouseObservable(): Observable[(Int, Int)] =
    val subject = PublishSubject[(Int, Int)]()
    component.addMouseMotionListener(new MouseMotionListener:
      override def mouseDragged(e: MouseEvent): Unit = {}
      override def mouseMoved(e: MouseEvent): Unit =
        subject.onNext((e.getX, e.getY))
    )
    subject
