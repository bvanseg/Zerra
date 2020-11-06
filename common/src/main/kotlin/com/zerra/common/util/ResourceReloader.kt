package com.zerra.common.util;

import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

class ResourceReloader {
    private val resources = HashSet<Reloadable>()

    fun add(listener: Reloadable) = resources.add(listener)

    fun reload(backgroundExecutor: Executor, mainExecutor: Executor): CompletableFuture<*> {
        return CompletableFuture.allOf(*resources.map { it.reload(backgroundExecutor, mainExecutor) }.toTypedArray())
    }
}
