package agstack.gramophone.utils

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class EventBus {
  companion object {
    val publisher: PublishSubject<Any> = PublishSubject.create()

    inline fun <reified T> subscribe(): Observable<T> {
      return publisher.filter {
        it is T
      }.map {
        it as T
      }
    }

    fun post(event: Any) {
      publisher.onNext(event)
    }
  }
}