package com.zerra.common.util

abstract class ZerraException(message: String, nested: Exception? = null): Exception(message, nested)
class ModLoadException(message: String, nested: Exception? = null): ZerraException(message, nested)