package it.unibo.game.view

import monix.reactive.{Observable, OverflowStrategy}
import org.scalajs.dom.html.Canvas
import org.scalajs.dom
import monix.reactive.subjects.PublishSubject
import monix.eval.Task
import monix.execution.cancelables.SingleAssignCancelable

extension (canvas: Canvas)
  def mouseObservable: Observable[(Int, Int)] =
    val boundingBox = canvas.getBoundingClientRect()
    Observable.create(OverflowStrategy.Unbounded) { subject =>
      canvas.onmousemove =
        (e: dom.MouseEvent) => subject.onNext((e.clientX - boundingBox.x).toInt, (e.clientY - boundingBox.y).toInt)
      SingleAssignCancelable()
    }
