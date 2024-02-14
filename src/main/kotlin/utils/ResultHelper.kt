package org.example.utils

class ResultHelper {
    companion object {
        fun <T> success(value: T): Result<T> = Result.success(value)
        fun <T> failure(exception: Throwable): Result<T> = Result.failure(exception)

        fun <T> Result<T>.expectSuccess(): T =
            when (this.isSuccess) {
                true -> this.getOrThrow()
                false -> throw AssertionError("Expected success but was failure")
            }

        fun <T> Result<T>.expectFailure(): Throwable =
            when (this.isSuccess) {
                true -> throw AssertionError("Expected failure but was success")
                false -> this.exceptionOrNull() ?: throw AssertionError("Expected failure but got null exception.")
            }
    }
}