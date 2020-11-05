package com.zerra.common.util

import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

interface Reloadable {
    fun reload(backgroundExecutor: Executor, mainExecutor: Executor): CompletableFuture<*>
}