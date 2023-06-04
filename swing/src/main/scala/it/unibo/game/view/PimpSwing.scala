package it.unibo.game.view

import monix.execution.cancelables.SingleAssignCancelable
import monix.reactive.{Observable, OverflowStrategy}
import monix.reactive.subjects.PublishSubject

import java.awt.Component
import java.awt.event.{MouseEvent, MouseMotionListener}

/** Extend swing to support observable sources */
extension (component: Component)
  def mouseObservable(): Observable[(Int, Int)] =
    /* using subject ==> unsafe..
    val subject = PublishSubject[(Int, Int)]()
    component.addMouseMotionListener(new MouseMotionListener:
      override def mouseDragged(e: MouseEvent): Unit = {}
      override def mouseMoved(e: MouseEvent): Unit =
        subject.onNext((e.getX, e.getY))
    )*/
    // Using observable..
    Observable.create(OverflowStrategy.Unbounded) { subject =>
      component.addMouseMotionListener(new MouseMotionListener:
        override def mouseDragged(e: MouseEvent): Unit = {}
        override def mouseMoved(e: MouseEvent): Unit =
          subject.onNext((e.getX, e.getY))
      )
      SingleAssignCancelable()
    }
