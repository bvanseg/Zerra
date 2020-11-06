package com.zerra.client.texture

import com.zerra.common.util.resource.ResourceLocation
import org.lwjgl.BufferUtils
import org.lwjgl.stb.STBImage.stbi_load_from_memory
import org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.NativeResource
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer

object TextureLoader {
    init {
        stbi_set_flip_vertically_on_load(true)
    }

    fun load(location: ResourceLocation, channels: Int = 4): ImageData {
        if (!location.resourceExists)
            throw IOException("Failed to find resource at $location")
        location.use {
            return load(it.inputStream!!, channels)
        }
    }

    fun load(stream: InputStream, channels: Int = 4): ImageData {
        val out = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length: Int
        while (stream.read(buffer).also { length = it } >= 0)
            out.write(buffer, 0, length)
        val data = BufferUtils.createByteBuffer(out.size())
        data.put(out.toByteArray())
        data.flip()
        return load(data, channels)
    }

    fun load(data: ByteBuffer, channels: Int = 4): ImageData {
        MemoryStack.stackPush().use {
            val x = it.callocInt(1)
            val y = it.callocInt(1)
            val channelsInFile = it.callocInt(1)
            val image = stbi_load_from_memory(data, x, y, channelsInFile, channels) ?: throw IOException("Failed to load image data")
            return ImageData(image, x.get(), y.get(), channelsInFile.get())
        }
    }

    class ImageData internal constructor(data: ByteBuffer, val width: Int, val height: Int, val channels: Int) : NativeResource {
        var data: ByteBuffer? = data
            private set

        override fun free() {
            data = null
        }
    }
}