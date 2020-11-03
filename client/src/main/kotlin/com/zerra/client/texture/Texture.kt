package com.zerra.client.texture

import com.zerra.client.util.Bindable
import com.zerra.common.util.resource.ResourceManager

interface Texture : Bindable {
    fun load(resourceManager: ResourceManager, textureManager: TextureManager)
}