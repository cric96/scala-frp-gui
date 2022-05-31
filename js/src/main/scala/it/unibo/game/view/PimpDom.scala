package it.unibo.game.view

import monix.reactive.Observable
import org.scalajs.dom.html.Canvas
import org.scalajs.dom
import monix.reactive.subjects.PublishSubject
import monix.eval.Task

extension (canvas: Canvas)
  def mouseObservable: Observable[(Int, Int)] =
    val boundingBox = canvas.getBoundingClientRect()
    val subject = PublishSubject[(Int, Int)]()
    canvas.onmousemove = (e: dom.MouseEvent) =>
      subject.onNext((e.clientX - boundingBox.x).toInt, (e.clientY - boundingBox.y).toInt)
    subject
