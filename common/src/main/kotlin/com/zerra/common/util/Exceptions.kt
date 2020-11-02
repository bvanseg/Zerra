package com.zerra.common.util

abstract class ZerraException(override val message: String): Exception(message)
class ModLoadException(override val message: String): ZerraException(message)